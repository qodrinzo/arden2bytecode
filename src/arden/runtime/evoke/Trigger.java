package arden.runtime.evoke;

import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;


public abstract class Trigger extends ArdenValue {
	
	public Trigger() {
	}
	
	public Trigger(long primaryTime) {
		//super(primaryTime);
	}

	/** When should the evoke event run next. */
	public abstract ArdenTime getNextRunTime(ExecutionContext context);
	
	/** Whether to run for an external event such as 'penicillin_storage' */
	public abstract boolean runOnEvent(String mapping, ArdenTime eventTime);
	
}
