package arden.runtime.evoke;

import java.util.Arrays;
import java.util.List;

import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;

public class AnyTrigger extends Trigger {
	
	private List<Trigger> triggers;

	public AnyTrigger(Trigger[] triggers, long primaryTime) {
		this.triggers = Arrays.asList(triggers);
	}

	public AnyTrigger(Trigger[] triggers) {
		this(triggers, NOPRIMARYTIME);
	}

	public AnyTrigger(List<Trigger> triggers, long primaryTime) {
		super(primaryTime);
		this.triggers = triggers;
	}

	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		// Find oldest trigger/event
		ArdenTime oldest = null;
		for (Trigger trigger : triggers) {
			ArdenTime nextRunTime = trigger.getNextRunTime(context);
			if (nextRunTime != null && (oldest == null || oldest.compareTo(nextRunTime) > 0)) {
				oldest = nextRunTime;
			}
		}

		return oldest;
	}

	@Override
	public boolean runOnEvent(String mapping, ArdenTime eventTime) {
		for (Trigger trigger : triggers) {
			if (trigger.runOnEvent(mapping, eventTime)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ArdenValue setTime(long newPrimaryTime) {
		return new AnyTrigger(triggers, newPrimaryTime);
	}

}
