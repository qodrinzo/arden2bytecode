package arden.runtime.events;

import java.util.Arrays;
import java.util.List;

import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;

public class AnyEvokeEvent extends EvokeEvent {
	private List<EvokeEvent> events;

	public AnyEvokeEvent(EvokeEvent[] events, long primaryTime) {
		this.events = Arrays.asList(events);
	}

	public AnyEvokeEvent(EvokeEvent[] events) {
		this(events, NOPRIMARYTIME);
	}

	public AnyEvokeEvent(List<EvokeEvent> events, long primaryTime) {
		super(primaryTime);
		this.events = events;
	}

	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		// Find oldest event
		ArdenTime oldest = null;
		for (EvokeEvent e : events) {
			ArdenTime nextRunTime = e.getNextRunTime(context);
			if (nextRunTime != null && (oldest == null || oldest.compareTo(nextRunTime) > 0)) {
				oldest = nextRunTime;
			}
		}

		return oldest;
	}

	@Override
	public boolean runOnEvent(String event, ArdenTime eventTime) {
		for (EvokeEvent e : events) {
			if (e.runOnEvent(event, eventTime)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ArdenValue setTime(long newPrimaryTime) {
		return new AnyEvokeEvent(events, newPrimaryTime);
	}

}
