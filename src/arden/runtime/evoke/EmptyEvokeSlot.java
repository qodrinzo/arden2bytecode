package arden.runtime.evoke;

import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;

public class EmptyEvokeSlot extends Trigger {

	public EmptyEvokeSlot(long primaryTime) {
		super(primaryTime);
	}

	public EmptyEvokeSlot() {
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
		return new EmptyEvokeSlot(newPrimaryTime);
	}

}
