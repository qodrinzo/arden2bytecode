package arden;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import arden.runtime.ArdenEvent;
import arden.runtime.ArdenRunnable;
import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.BaseExecutionContext;
import arden.runtime.ExecutionContext;
import arden.runtime.MedicalLogicModule;
import arden.runtime.evoke.CallTrigger;
import arden.runtime.evoke.Trigger;

/**
 * <p>
 * The evoke engine waits for periodic or delayed evoke triggers (in the evoke
 * slot) and delayed calls (in the action slot) and invokes MLMs when they are
 * scheduled. MLMs which are waiting for execution are called in the order of
 * their priority (MLMs triggered in the evoke slot) or their urgency (action
 * slot calls).
 * </p>
 * <p>
 * Threads can communicate with the engines scheduling loop via a message queue.
 * Messages are {@link EventCall}s or {@link MlmCall}s. EventCalls are handled
 * first as they only add new MlmCalls to the queue, then MlmCalls are handled
 * in order of their priority/urgency.
 * </p>
 * <p>
 * MlmCalls or EventCalls may trigger other MLMs after a delay, so the engine
 * uses each MLMs {@link Trigger#getNextRunTime(ExecutionContext)} method to
 * check when it should run next. Delayed calls are added to the the queue after
 * their delay has passed, via a {@link ScheduledExecutorService}.
 * </p>
 */
public class EvokeEngine implements Runnable {
	private Comparator<Message> priorityComparator = new Comparator<Message>() {
		@Override
		public int compare(Message m1, Message m2) {
			// highest priority first
			return m2.getPriority() - m1.getPriority();
		}
	};
	// thread-safe queue of messages which are waiting for execution
	private PriorityBlockingQueue<Message> messages = new PriorityBlockingQueue<>(11, priorityComparator);
	private ScheduledExecutorService delayer = Executors.newScheduledThreadPool(1);

	private ExecutionContext context;
	private List<MedicalLogicModule> mlms;

	public EvokeEngine(BaseExecutionContext context, List<MedicalLogicModule> mlms) {
		this.mlms = mlms;
		this.context = context;

		context.setEngine(this);
	}

	public MedicalLogicModule[] findModules(ArdenEvent event) throws InvocationTargetException {
		List<MedicalLogicModule> foundModules = new ArrayList<>();
		for (MedicalLogicModule mlm : mlms) {
			for (Trigger trigger : mlm.getTriggers(context)) {
				if (trigger.runOnEvent(event)) {
					foundModules.add(mlm);
				}
			}
		}
		return foundModules.toArray(new MedicalLogicModule[foundModules.size()]);
	}

	public void callEvent(ArdenEvent event, long delay) {
		/*
		 * Checking the evoke statements may require running the data slot,
		 * which should not run concurrent to other (possibly data changing)
		 * MLMs. Therefore add an EventCall to messages, so it is run on the
		 * engines thread.
		 */
		final EventCall call = new EventCall(event);
		if (delay <= 0) {
			// run event as soon as possible
			messages.add(call);
		} else {
			// add the event call after the delay has passed
			delayer.schedule(new Runnable() {
				@Override
				public void run() {
					messages.add(call);
				}
			}, delay, TimeUnit.MILLISECONDS);
		}
	}

	public void callWithDelay(ArdenRunnable mlm, ArdenValue[] arguments, int urgency, long delay) {
		final MlmCall call = new MlmCall(mlm, arguments, urgency);
		if (delay <= 0) {
			// run MLM as soon as possible
			messages.add(call);
		} else {
			// add the call after the delay has passed
			delayer.schedule(new Runnable() {
				@Override
				public void run() {
					messages.add(call);
				}
			}, delay, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void run() {
		// initialize schedule for fixed time triggers
		scheduleTriggers();

		// the scheduling loop
		while (!Thread.currentThread().isInterrupted()) {
			// wait for messages
			Message message;
			try {
				message = messages.take();
			} catch (InterruptedException e) {
				// shutting down
				break;
			}

			// execute MlmCall or EventCall on this thread
			message.run();
			
			// check for MLMs which may now be triggered
			scheduleTriggers();
		}

		// cancel all delayed tasks
		delayer.shutdownNow();
	}

	private void scheduleTriggers() {
		// schedule MLMs by looking at their EvokeEvents getNextRunTime() method
		Schedule schedule = createSchedule(mlms);

		ArdenTime currentTime = context.getCurrentTime();
		for (Entry<ArdenTime, List<MlmCall>> entry : schedule.entrySet()) {
			ArdenTime nextRuntime = entry.getKey();
			final List<MlmCall> triggeredMlms = entry.getValue();
			final long delay = nextRuntime.value - currentTime.value;

			if (delay <= 0) {
				// run MLMs as soon as possible
				messages.addAll(triggeredMlms);
			} else {
				// add the calls after the delay has passed
				delayer.schedule(new Runnable() {
					@Override
					public void run() {
						/*
						 * PriorityBlockingQueue.addAll() is not atomic! The
						 * first MLM has a high chance to run before all other
						 * MLMs are added, even if it has a lower priority.
						 * Therefore add MLMs in priority order.
						 */
						Collections.sort(triggeredMlms, priorityComparator);
						messages.addAll(triggeredMlms);
					}
				}, delay, TimeUnit.MILLISECONDS);

			}
		}

	}

	private Schedule createSchedule(List<MedicalLogicModule> mlms) {
		Schedule schedule = new Schedule();

		// put MLMs which should run at the same time into groups sorted by time
		for (MedicalLogicModule mlm : mlms) {
			Trigger[] triggers;
			try {
				triggers = mlm.getTriggers(context);
			} catch (InvocationTargetException e) {
				// print error and skip this MLM
				e.printStackTrace();
				continue;
			}
			for (Trigger trigger : triggers) {

				ArdenTime nextRuntime = trigger.getNextRunTime();
				if (nextRuntime == null) {
					// not scheduled
					continue;
				}

				List<MlmCall> scheduleGroup = schedule.get(nextRuntime);
				if (scheduleGroup == null) {
					scheduleGroup = new ArrayList<MlmCall>();
					schedule.put(nextRuntime, scheduleGroup);
				}
				scheduleGroup.add(new MlmCall(mlm, null));
			}
		}

		return schedule;
	}

	@SuppressWarnings("serial")
	private static class Schedule extends TreeMap<ArdenTime, List<MlmCall>> {
		public Schedule() {
			// sort by time
			super(new ArdenTime.NaturalComparator());
		}
	}

	private static interface Message extends Runnable {
		// not necessarily the same as an MLMs priority!
		int getPriority();
	}

	private class EventCall implements Message {
		ArdenEvent event;

		public EventCall(ArdenEvent event) {
			this.event = event;
		}

		@Override
		public int getPriority() {
			// always handle events before MlmCalls
			return Integer.MAX_VALUE;
		}

		@Override
		public void run() {
			// add MlmCalls for all MLMs which should run for the event to queue
			for (MedicalLogicModule mlm : mlms) {
				Trigger[] triggers;
				try {
					triggers = mlm.getTriggers(context);
				} catch (InvocationTargetException e) {
					// print error and skip this MLM
					e.printStackTrace();
					continue;
				}

				for (Trigger trigger : triggers) {
					trigger.scheduleEvent(event);
					if (trigger.runOnEvent(event)) {
						messages.add(new MlmCall(mlm, null));
					}
				}
			}
		}
	}

	private class MlmCall implements Message {
		ArdenRunnable runnable;
		ArdenValue[] args;
		int priority;

		public MlmCall(ArdenRunnable runnable, ArdenValue[] args, int priority) {
			this.runnable = runnable;
			this.args = args;
			this.priority = priority;
		}

		public MlmCall(MedicalLogicModule mlm, ArdenValue[] args) {
			this(mlm, args, (int) Math.round(mlm.getPriority()));
		}

		@Override
		public int getPriority() {
			return priority;
		}

		@Override
		public void run() {
			// run MLM now
			try {
				// TODO use correct delay
				runnable.run(context, args, new CallTrigger());
			} catch (InvocationTargetException e) {
				// print error and skip this MLM
				e.printStackTrace();
			}
		}
	}

}
