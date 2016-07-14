package arden.runtime.events;

import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;

public class FixedDateEvokeEvent extends EvokeEvent {

	private ArdenTime date;
	protected boolean triggered = false;

	public FixedDateEvokeEvent(ArdenTime date, long primaryTime) {
		super(primaryTime);
		this.date = date;
	}

	public FixedDateEvokeEvent(ArdenTime date) {
		this(date, NOPRIMARYTIME);
	}

	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		if (triggered) {
			// Don't run again
			return null;
		}
		triggered = true;
		return date;
	}

	@Override
	public boolean runOnEvent(String event, ArdenTime eventTime) {
		return false;
	}

	@Override
	public ArdenValue setTime(long newPrimaryTime) {
		return new FixedDateEvokeEvent(date, newPrimaryTime);
	}

}
