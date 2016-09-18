package arden.runtime.evoke;

import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;

public class FixedDateTrigger extends Trigger {

	private ArdenTime date;
	protected boolean triggered = false;

	public FixedDateTrigger(ArdenTime date, long primaryTime) {
		super(primaryTime);
		this.date = date;
	}

	public FixedDateTrigger(ArdenTime date) {
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
	public boolean runOnEvent(String mapping, ArdenTime eventTime) {
		return false;
	}

	@Override
	public ArdenValue setTime(long newPrimaryTime) {
		return new FixedDateTrigger(date, newPrimaryTime);
	}

}
