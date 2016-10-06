package arden.runtime.evoke;

import arden.runtime.ArdenEvent;
import arden.runtime.ArdenTime;
import arden.runtime.ExecutionContext;

public class EventTrigger implements Trigger {

	private ArdenEvent event;
	private ArdenEvent triggeringEvent = null;

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
		return event.equals(this.event);
	}

	@Override
	public void scheduleEvent(ArdenEvent event) {
		// saves primarytime and eventtime
		triggeringEvent = event.equals(this.event) ? event : null;
	}

	@Override
	public ArdenEvent getTriggeringEvent() {
		return triggeringEvent;
	}

	@Override
	public long getDelay() {
		return 0;
	}

}
