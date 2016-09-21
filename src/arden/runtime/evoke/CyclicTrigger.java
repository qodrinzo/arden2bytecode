package arden.runtime.evoke;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import arden.runtime.ArdenDuration;
import arden.runtime.ArdenEvent;
import arden.runtime.ArdenTime;
import arden.runtime.ExecutionContext;

public class CyclicTrigger implements Trigger {

	private ArdenDuration interval;
	private ArdenDuration length;
	private Trigger starting;
	private List<ScheduledCycle> scheduledCycles = new ArrayList<>();
	private long currentDelay = 0;

	public CyclicTrigger(ArdenDuration interval, ArdenDuration length, Trigger starting) {
		this.interval = interval;
		this.length = length;
		this.starting = starting;
	}

	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		ArdenTime current = context.getCurrentTime();

		// Check if the starting trigger has happened
		ArdenTime startTime = starting.getNextRunTime(context);
		if (startTime != null) {
			ArdenTime end = new ArdenTime(startTime.add(length));
			ScheduledCycle cycle = new ScheduledCycle(startTime, end);
			scheduledCycles.add(cycle);
		}

		ScheduledCycle closestCycle = null;
		Iterator<ScheduledCycle> iterator = scheduledCycles.iterator();
		while (iterator.hasNext()) {
			ScheduledCycle cycle = iterator.next();

			// Calculate next run time
			boolean sameTimeButNotStartTime = current.compareTo(cycle.next) == 0 && current.compareTo(cycle.start) != 0;
			while (cycle.next.compareTo(cycle.end) <= 0 && ( // cycle has not ended
						current.compareTo(cycle.next) > 0 // cycles next value lies in the past 
						|| sameTimeButNotStartTime)) { // or value is same, but not the start (start is inclusive)
				cycle.next = new ArdenTime(cycle.next.add(interval));
				cycle.nextReturned = false;
			}

			if (cycle.next.compareTo(cycle.end) > 0) {
				// Cycle is already finished.
				iterator.remove();
				continue;
			}

			if (cycle.nextReturned) {
				// This run time was already returned
				continue;
			}

			// Calculate cycle, which runtime is closest to the current time
			if (closestCycle == null) {
				closestCycle = cycle;
			} else if (cycle.next.compareTo(closestCycle.next) < 0) {
				closestCycle = cycle;
			}
		}

		if (closestCycle == null) {
			return null;
		}

		closestCycle.nextReturned = true;
		currentDelay = closestCycle.next.value - closestCycle.start.value;
		return closestCycle.next;

	}

	@Override
	public boolean runOnEvent(ArdenEvent event) {
		return false;
	}

	@Override
	public void scheduleEvent(ArdenEvent event) {
		starting.scheduleEvent(event);
		if (starting.runOnEvent(event)) {
			ArdenEvent startEvent = starting.getTriggeringEvent();

			ArdenTime startTime = new ArdenTime(startEvent.eventTime);
			ArdenTime end = new ArdenTime(startTime.add(length));
			ScheduledCycle cycle = new ScheduledCycle(startTime, end);
			scheduledCycles.add(cycle);
		}
	}

	public ArdenEvent getTriggeringEvent() {
		return starting.getTriggeringEvent();
	}

	@Override
	public long getDelay() {
		return currentDelay + starting.getDelay();
	}

	private class ScheduledCycle {
		ArdenTime start;
		ArdenTime next;
		ArdenTime end;
		boolean nextReturned = false;

		public ScheduledCycle(ArdenTime start, ArdenTime end) {
			this.start = start;
			this.next = this.start;
			this.end = end;
		}
	}

}
