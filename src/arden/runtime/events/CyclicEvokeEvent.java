package arden.runtime.events;

import arden.runtime.ArdenDuration;
import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;

public class CyclicEvokeEvent extends EvokeEvent {

	private ArdenDuration interval;
	private ArdenDuration length;
	private ArdenTime starting; // FIXME Should be an EvokeEvent
	private ArdenTime next;
	private boolean triggered = false;
	private ArdenTime limit;

	public CyclicEvokeEvent(ArdenDuration interval, ArdenDuration length, ArdenTime starting, long primaryTime) {
		this.interval = interval;
		this.starting = starting;
		this.next = this.starting;
		this.limit = new ArdenTime(starting.add(length));
	}

	public CyclicEvokeEvent(ArdenDuration interval, ArdenDuration length, ArdenTime starting) {
		this(interval, length, starting, NOPRIMARYTIME);
	}

	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		ArdenTime current = context.getCurrentTime();
		
		// Check if already finished, but return starting time once, even if finished
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
		
		if(triggered) {
			// this cycle was already returned
			return null;
		}
		
		triggered = true;
		return next;
	}

	@Override
	public boolean runOnEvent(String event, ArdenTime eventTime) {
		return false;
	}

	@Override
	public ArdenValue setTime(long newPrimaryTime) {
		return new CyclicEvokeEvent(interval, length, starting, newPrimaryTime);
	}

}
