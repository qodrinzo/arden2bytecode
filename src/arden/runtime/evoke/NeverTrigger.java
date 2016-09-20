package arden.runtime.evoke;

import arden.runtime.ArdenEvent;
import arden.runtime.ArdenTime;
import arden.runtime.ExecutionContext;

public class NeverTrigger implements Trigger {

	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		return null;
	}

	@Override
	public boolean runOnEvent(ArdenEvent event) {
		return false;
	}

	@Override
	public void scheduleEvent(ArdenEvent event) {
	}

}
