package arden.runtime.evoke;

import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;

public class NeverTrigger extends Trigger {

	public NeverTrigger(long primaryTime) {
		super(primaryTime);
	}

	public NeverTrigger() {
		this(NOPRIMARYTIME);
	}

	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		return null;
	}

	@Override
	public boolean runOnEvent(String mapping, ArdenTime eventTime) {
		return false;
	}

	@Override
	public ArdenValue setTime(long newPrimaryTime) {
		return new NeverTrigger(newPrimaryTime);
	}

}
