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
	private static Comparator<MlmCall> priorityComparator = new Comparator<MlmCall>() {
		@Override
		public int compare(MlmCall m1, MlmCall m2) {
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
			Trigger trigger;
			try {
				trigger = mlm.getTrigger(this, null);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
			trigger.scheduleEvent(event);
			if (trigger.runOnEvent(event)) {
				scheduledCalls.add(currentTime, new MlmCall(mlm, this, null));
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
			Entry<ArdenTime, Queue<MlmCall>> nextEntry = scheduledCalls.firstEntry();
			if (nextEntry == null) {
				throw new IllegalStateException("There are no more messages");
			}
			ArdenTime nextRunTime = nextEntry.getKey();
			Queue<MlmCall> queueCalls = nextEntry.getValue();
			MlmCall nextMlm = queueCalls.poll(); // highest priority
			if (queueCalls.isEmpty()) {
				// remove empty list
				scheduledCalls.pollFirstEntry();
			}

			// run the next MLM
			if (nextRunTime.value >= currentTime.value) {
				currentTime = new ArdenTime(nextRunTime.value+1); // skip delay
			}
			nextMlm.run();

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
			Trigger trigger;
			try {
				trigger = mlm.getTrigger(this, null);
			} catch (InvocationTargetException e) {
				// print error and skip this MLM
				e.printStackTrace();
				continue;
			}

			ArdenTime nextRuntime = trigger.getNextRunTime(this);
			if (nextRuntime == null) {
				// not scheduled
				continue;
			}

			schedule.add(nextRuntime, new MlmCall(mlm, this, null));
		}

		return schedule;
	}

	@SuppressWarnings("serial")
	private static class Schedule extends TreeMap<ArdenTime, Queue<MlmCall>> {
		public Schedule() {
			// sort by time
			super(new ArdenTime.NaturalComparator());
		}

		public void add(Schedule additionalSchedule) {
			for (Entry<ArdenTime, Queue<MlmCall>> entry : additionalSchedule.entrySet()) {
				ArdenTime time = entry.getKey();
				Queue<MlmCall> scheduleGroup = get(time);
				if (scheduleGroup == null) {
					put(time, entry.getValue());
				} else {
					scheduleGroup.addAll(entry.getValue());
				}
			}
		}

		public void add(ArdenTime nextRunTime, MlmCall mlm) {
			Queue<MlmCall> scheduleGroup = get(nextRunTime);
			if (scheduleGroup == null) {
				scheduleGroup = new PriorityQueue<MlmCall>(3, priorityComparator);
				put(nextRunTime, scheduleGroup);
			}
			scheduleGroup.add(mlm);
		}
	}

	private static class MlmCall implements Runnable {
		final MedicalLogicModule mlm;
		final ArdenValue[] args;
		final int priority;
		final ExecutionContext context;

		public MlmCall(MedicalLogicModule mlm, ExecutionContext context, ArdenValue[] args, int priority) {
			this.mlm = mlm;
			this.args = args;
			this.priority = priority;
			this.context = context;
		}

		public MlmCall(MedicalLogicModule mlm, ExecutionContext context, ArdenValue[] args) {
			this(mlm, context, args, (int) Math.round(mlm.getPriority()));
		}

		public int getPriority() {
			return priority;
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

}
