package arden.tests.specification.testcompiler.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

import arden.runtime.ArdenDuration;
import arden.runtime.ArdenEvent;
import arden.runtime.ArdenRunnable;
import arden.runtime.ArdenString;
import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;
import arden.runtime.MedicalLogicModule;
import arden.runtime.evoke.Trigger;
import arden.tests.specification.testcompiler.TestCompiler;
import arden.tests.specification.testcompiler.TestCompilerDelayedMessage;

/**
 * An engine for the
 * {@link TestCompiler#compileAndRunForEvent(String, String, int)} method. <br>
 * Skips delays by dynamically changing the {@link #getCurrentTime()} value, so
 * tests don't block.
 */
public class TestEngine extends TestContext {
	private static Comparator<Call> priorityComparator = new Comparator<Call>() {
		@Override
		public int compare(Call m1, Call m2) {
			// highest priority first
			return (int) (m2.getPriority() - m1.getPriority());
		}
	};
	private List<MedicalLogicModule> mlms = new ArrayList<>();
	private Schedule scheduledCalls = new Schedule();
	private Queue<TestCompilerDelayedMessage> messages = new LinkedList<>();
	private ArdenTime startTime;
	private ArdenTime currentTime;

	public TestEngine(List<MedicalLogicModule> mlms, MedicalLogicModule callingMlm) {
		super(mlms);
		this.mlms.addAll(mlms);
	}

	public void callEvent(ArdenEvent event) {
		this.startTime = new ArdenTime(event.eventTime);
		currentTime = startTime;

		// check if MLMs are directly triggered
		for (MedicalLogicModule mlm : mlms) {
			try {
				for (Trigger trigger : mlm.getTriggers(this, null)) {
					trigger.scheduleEvent(event);
					if (trigger.runOnEvent(event)) {
						scheduledCalls.add(currentTime, new MlmCall(mlm, this, null));
					}
				}
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		// schedule MLMs which may now be triggered
		Schedule additionalSchedule = createSchedule(mlms);
		scheduledCalls.add(additionalSchedule);
	}

	@Override
	public void callWithDelay(ArdenRunnable mlm, ArdenValue[] arguments, ArdenValue delayValue) {
		ArdenDuration delayDuration = (ArdenDuration) delayValue;
		ArdenTime nextRuntime = new ArdenTime(currentTime.add(delayDuration));
		scheduledCalls.add(nextRuntime, new MlmCall((MedicalLogicModule) mlm, this, arguments));
	}

	@Override
	public void callEventWithDelay(ArdenEvent event, ArdenValue delayValue) {
		ArdenDuration delayDuration = (ArdenDuration) delayValue;
		ArdenTime nextRuntime = new ArdenTime(currentTime.add(delayDuration));
		scheduledCalls.add(nextRuntime, new EventCall(event, this));
	}

	@Override
	public void write(ArdenValue message, String destination) {
		// save messages
		String stringMessage;
		if (message instanceof ArdenString) {
			stringMessage = ((ArdenString) message).value;
		} else {
			stringMessage = message.toString();
		}
		long delay = currentTime.value - startTime.value;
		messages.add(new TestCompilerDelayedMessage(delay, stringMessage));
	}

	@Override
	public List<String> getMessages() {
		throw new UnsupportedOperationException();
	}

	public TestCompilerDelayedMessage getNextDelayedMessage() throws InvocationTargetException {
		while (messages.isEmpty()) {
			// get the next MLM
			Entry<ArdenTime, Queue<Call>> nextEntry = scheduledCalls.firstEntry();
			if (nextEntry == null) {
				throw new IllegalStateException("There are no more messages");
			}
			ArdenTime nextRunTime = nextEntry.getKey();
			Queue<Call> queueCalls = nextEntry.getValue();
			Call nextCall = queueCalls.poll(); // highest priority
			if (queueCalls.isEmpty()) {
				// remove empty list
				scheduledCalls.pollFirstEntry();
			}

			// run the next MLM
			if (nextRunTime.value >= currentTime.value) {
				// skip delay
				currentTime = new ArdenTime(nextRunTime.value + 1);
			}
			nextCall.run();

			// schedule MLMs which may now be triggered
			Schedule additionalSchedule = createSchedule(mlms);
			scheduledCalls.add(additionalSchedule);
		}

		return messages.remove();
	}

	@Override
	public ArdenTime getCurrentTime() {
		return currentTime;
	}

	private Schedule createSchedule(List<MedicalLogicModule> mlms) {
		Schedule schedule = new Schedule();

		// put MLMs which should run at the same time into groups sorted by time
		for (MedicalLogicModule mlm : mlms) {
			try {
				for (Trigger trigger : mlm.getTriggers(this, null)) {
					ArdenTime nextRuntime = trigger.getNextRunTime(this);
					if (nextRuntime == null) {
						// not scheduled
						continue;
					}

					schedule.add(nextRuntime, new MlmCall(mlm, this, null));
				}
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		return schedule;
	}

	@SuppressWarnings("serial")
	private static class Schedule extends TreeMap<ArdenTime, Queue<Call>> {
		public Schedule() {
			// sort by time
			super(new ArdenTime.NaturalComparator());
		}

		public void add(Schedule additionalSchedule) {
			for (Entry<ArdenTime, Queue<Call>> entry : additionalSchedule.entrySet()) {
				ArdenTime time = entry.getKey();
				Queue<Call> scheduleGroup = get(time);
				if (scheduleGroup == null) {
					put(time, entry.getValue());
				} else {
					scheduleGroup.addAll(entry.getValue());
				}
			}
		}

		public void add(ArdenTime nextRunTime, Call mlm) {
			Queue<Call> scheduleGroup = get(nextRunTime);
			if (scheduleGroup == null) {
				scheduleGroup = new PriorityQueue<Call>(3, priorityComparator);
				put(nextRunTime, scheduleGroup);
			}
			scheduleGroup.add(mlm);
		}
	}

	private static abstract class Call implements Runnable {
		// not necessarily the same as an MLMs priority!
		final int priority;

		Call(int priority) {
			this.priority = priority;
		}

		int getPriority() {
			return priority;
		}
	}

	private static class MlmCall extends Call {
		final MedicalLogicModule mlm;
		final ArdenValue[] args;
		final ExecutionContext context;

		public MlmCall(MedicalLogicModule mlm, ExecutionContext context, ArdenValue[] args, int priority) {
			super(priority);
			this.mlm = mlm;
			this.args = args;
			this.context = context;
		}

		public MlmCall(MedicalLogicModule mlm, ExecutionContext context, ArdenValue[] args) {
			this(mlm, context, args, (int) Math.round(mlm.getPriority()));
		}

		@Override
		public void run() {
			try {
				mlm.run(context, args);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private class EventCall extends Call {
		final ArdenEvent event;
		final ExecutionContext context;

		public EventCall(ArdenEvent event, ExecutionContext context) {
			super(Integer.MAX_VALUE); // always handle events before calls
			this.event = event;
			this.context = context;
		}

		@Override
		public void run() {
			for (ArdenRunnable mlm : context.findModules(event)) {
				MlmCall call = new MlmCall((MedicalLogicModule) mlm, context, null);
				scheduledCalls.add(context.getCurrentTime(), call);
			}
		}
	}

}
