package arden.runtime.evoke;

import arden.runtime.ArdenEvent;
import arden.runtime.ArdenTime;
import arden.runtime.ExecutionContext;

public class EventTrigger implements Trigger {
	private ArdenEvent event;

	public EventTrigger(ArdenEvent event) {
		if (event == null) {
			throw new NullPointerException();
		}
		this.event = event;
	}

	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		return null;
	}

	@Override
	public boolean runOnEvent(ArdenEvent event) {
		if (event.equals(this.event)) {
			return true;
		}
		return false;
	}

}
