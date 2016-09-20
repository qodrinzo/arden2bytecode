package arden.runtime.evoke;

import java.util.Arrays;
import java.util.List;

import arden.runtime.ArdenEvent;
import arden.runtime.ArdenTime;
import arden.runtime.ExecutionContext;

public class AnyTrigger implements Trigger {

	private List<Trigger> triggers;

	public AnyTrigger(Trigger[] triggers) {
		this.triggers = Arrays.asList(triggers);
	}

	public AnyTrigger(List<Trigger> triggers) {
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
	public boolean runOnEvent(ArdenEvent event) {
		for (Trigger trigger : triggers) {
			if (trigger.runOnEvent(event)) {
				return true;
			}
		}
		return false;
	}

}
