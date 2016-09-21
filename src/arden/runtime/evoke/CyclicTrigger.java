package arden.runtime.evoke;

import arden.runtime.ArdenDuration;
import arden.runtime.ArdenEvent;
import arden.runtime.ArdenTime;
import arden.runtime.ExecutionContext;

public class CyclicTrigger implements Trigger {

	private ArdenDuration interval;
	private ArdenTime starting; // FIXME Should be a a Trigger
	private ArdenTime next;
	private boolean triggered = false;
	private ArdenTime limit;

	public CyclicTrigger(ArdenDuration interval, ArdenDuration length, ArdenTime starting) {
		this.interval = interval;
		this.starting = starting;
		this.next = this.starting;
		this.limit = new ArdenTime(starting.add(length));
	}

	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		ArdenTime current = context.getCurrentTime();

		// Check if already finished, but return starting time once, even if
		// finished
		if (next != starting && next.compareTo(limit) > 0) {
			// Already finished
			return null;
		}

		// Calculate next cycle
		while (current.compareTo(next) >= 0) {
			next = new ArdenTime(next.add(interval));
			triggered = false;
			if (next.compareTo(limit) > 0) {
				// Already finished
				return null;
			}
		}

		if (triggered) {
			// this cycle was already returned
			return null;
		}

		triggered = true;
		return next;
	}

	@Override
	public boolean runOnEvent(ArdenEvent event) {
		return false;
	}

	@Override
	public void scheduleEvent(ArdenEvent event) {

	}

	public ArdenEvent getTriggeringEvent() {
		return null;
	}

	@Override
	public long getDelay() {
		return 0;
	}

}
