package arden.runtime.evoke;

import arden.runtime.ArdenEvent;
import arden.runtime.ArdenTime;
import arden.runtime.ExecutionContext;

public class FixedDateTrigger implements Trigger {

	private ArdenTime date;
	protected boolean triggered = false;

	public FixedDateTrigger(ArdenTime date) {
		this.date = date;
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
	public boolean runOnEvent(ArdenEvent event) {
		return false;
	}

}
