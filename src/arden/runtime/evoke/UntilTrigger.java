package arden.runtime.evoke;

import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;

public class UntilTrigger extends Trigger {
	private Trigger cycle;
	private ArdenTime until; // FIXME Should be a boolean expression

	public UntilTrigger(Trigger cycle, ArdenTime until, long primaryTime) {
		super(primaryTime);
		this.cycle = cycle;
		this.until = until;
	}

	public UntilTrigger(Trigger cycle, ArdenTime until) {
		this(cycle, until, NOPRIMARYTIME);
	}

	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		ArdenTime next = cycle.getNextRunTime(context);
		if (until.compareTo(next) > 0) {
			return next;
		}
		return null;
	}

	@Override
	public boolean runOnEvent(String mapping, ArdenTime eventTime) {
		return cycle.runOnEvent(mapping, eventTime);
	}

	@Override
	public ArdenValue setTime(long newPrimaryTime) {
		return new UntilTrigger(cycle, until, newPrimaryTime);
	}

}
