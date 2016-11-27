package arden.tests.specification.testcompiler.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.TreeMap;

import arden.runtime.ArdenDuration;
import arden.runtime.ArdenEvent;
import arden.runtime.ArdenRunnable;
import arden.runtime.ArdenString;
import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;
import arden.runtime.ExecutionContextHelpers;
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
	private List<MedicalLogicModule> mlms = new ArrayList<>();
	private Schedule scheduledCalls = new Schedule();
	private Queue<TestCompilerDelayedMessage> messages = new LinkedList<>();
	private ArdenTime startTime;
	private ArdenTime currentTime;

	public TestEngine(List<MedicalLogicModule> mlms, ArdenTime startTime) {
		super(mlms);
		this.mlms.addAll(mlms);
		this.startTime = startTime;
		currentTime = startTime;
	}

	@Override
	public void call(ArdenRunnable mlm, ArdenValue[] arguments, ArdenValue delayValue, Trigger trigger,
			double urgency) {
		ArdenDuration delayDuration = (ArdenDuration) delayValue;
		ArdenTime nextRuntime = new ArdenTime(currentTime.add(delayDuration));
		Trigger calleeTrigger = ExecutionContextHelpers.combine(trigger,
				ExecutionContextHelpers.delayToMillis(delayValue));
		scheduledCalls.add(nextRuntime, new MlmCall((MedicalLogicModule) mlm, arguments, calleeTrigger));
	}

	@Override
	public void call(ArdenEvent event, ArdenValue delayValue, double urgency) {
		ArdenDuration delayDuration = (ArdenDuration) delayValue;
		ArdenTime nextRuntime = new ArdenTime(currentTime.add(delayDuration));
		ArdenEvent eventAfterDelay = ExecutionContextHelpers.combine(event,
				ExecutionContextHelpers.delayToMillis(delayValue));
		scheduledCalls.add(nextRuntime, new EventCall(eventAfterDelay, this));
	}

	@Override
	public void write(ArdenValue message, ArdenValue destination, double urgency) {
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
			if (nextRunTime.value > currentTime.value) {
				// skip delay
				currentTime = new ArdenTime(nextRunTime.value);
			}
			nextCall.run();

			// schedule MLMs which may now be triggered
			Schedule additionalSchedule = createSchedule(mlms);
			scheduledCalls.add(additionalSchedule);
		}

		return messages.remove();
	}

	@Override
	public ArdenEvent getEvent(String mapping) {
		return new ArdenEvent(mapping, getCurrentTime().value);
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
				for (Trigger trigger : mlm.getTriggers(this)) {
					ArdenTime nextRuntime = trigger.getNextRunTime();
					if (nextRuntime == null) {
						// not scheduled
						continue;
					}
					
					schedule.add(nextRuntime, new MlmCall(mlm, null, trigger));
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
				scheduleGroup = new LinkedList<Call>();
				put(nextRunTime, scheduleGroup);
			}
			scheduleGroup.add(mlm);
		}
	}

	private static abstract class Call implements Runnable {
	}

	private class MlmCall extends Call {
		final MedicalLogicModule mlm;
		final ArdenValue[] args;
		final Trigger trigger;

		public MlmCall(MedicalLogicModule mlm, ArdenValue[] args, Trigger trigger) {
			this.mlm = mlm;
			this.args = args;
			this.trigger = trigger;
		}

		@Override
		public void run() {
			try {
				mlm.run(TestEngine.this, args, trigger);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private class EventCall extends Call {
		final ArdenEvent event;
		final ExecutionContext context;

		public EventCall(ArdenEvent event, ExecutionContext context) {
			this.event = event;
			this.context = context;
		}

		@Override
		public void run() {
			for (MedicalLogicModule mlm : mlms) {
				Trigger[] triggers;
				try {
					triggers = mlm.getTriggers(context);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}

				for (Trigger trigger : triggers) {
					trigger.scheduleEvent(event);
					if (trigger.runOnEvent(event)) {
						MlmCall call = new MlmCall((MedicalLogicModule) mlm, null, trigger);
						scheduledCalls.add(context.getCurrentTime(), call);
					}
				}
			}
		}
	}

}
