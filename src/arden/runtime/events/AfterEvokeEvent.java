package arden.runtime.events;

import java.util.SortedSet;
import java.util.TreeSet;

import arden.runtime.ArdenDuration;
import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;

public class AfterEvokeEvent extends EvokeEvent {

	EvokeEvent target;
	ArdenDuration duration;
	SortedSet<ArdenTime> additionalSchedules;
	
	public AfterEvokeEvent(ArdenDuration duration, EvokeEvent target, long primaryTime) {
		super(primaryTime);
		this.duration = duration;
		this.target = target;
		this.additionalSchedules = new TreeSet<ArdenTime>(new ArdenTime.NaturalComparator());
	}
	
	public AfterEvokeEvent(ArdenDuration duration, EvokeEvent target) {
		this(duration, target, NOPRIMARYTIME);
	}
	
	@Override
	public ArdenTime getNextRunTime(ExecutionContext context) {
		ArdenTime nextRunTime = target.getNextRunTime(context);
		if (nextRunTime != null) {
			nextRunTime = new ArdenTime(nextRunTime.add(duration));
		}
		
		// Select oldest schedule if it is older than nextRunTime
		if (!additionalSchedules.isEmpty()) {
			if (additionalSchedules.comparator().compare(additionalSchedules.first(), nextRunTime) < 0) {
				ArdenTime oldestSchedule = additionalSchedules.first();
				nextRunTime = oldestSchedule;
				additionalSchedules.remove(oldestSchedule);
			}
		}
		
		return nextRunTime;
	}

	@Override
	public boolean runOnEvent(String event, ArdenTime eventTime) {
		if (target.runOnEvent(event, eventTime)) {
			// trigger in 'duration' after current time
			additionalSchedules.add(new ArdenTime(eventTime.add(duration)));
		}
		return false;
	}

	@Override
	public ArdenValue setTime(long newPrimaryTime) {
		return new AfterEvokeEvent(duration, target, newPrimaryTime);
	}
	
}
