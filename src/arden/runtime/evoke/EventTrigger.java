package arden.runtime.evoke;

import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;

public class EventTrigger extends Trigger {
	private String mapping;

	public EventTrigger(String mapping, long primaryTime) {
		super(primaryTime);
		if (mapping == null) {
			throw new NullPointerException();
		}
		this.mapping = mapping;
	}

	public EventTrigger(String mapping) {
		this(mapping, NOPRIMARYTIME);
	}

	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		return null;
	}

	@Override
	public boolean runOnEvent(String mapping, ArdenTime eventTime) {
		if (this.mapping.equalsIgnoreCase(mapping)) {
			return true;
		}
		return false;
	}

	@Override
	public ArdenValue setTime(long newPrimaryTime) {
		return new EventTrigger(mapping, newPrimaryTime);
	}

}
