package arden.runtime.evoke;

import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExecutionContext;

public abstract class Trigger extends ArdenValue {

	public Trigger() {
	}

	public Trigger(long primaryTime) {
		// super(primaryTime);
	}

	/**
	 * <p>
	 * Calculates when the trigger should run next. Triggers are stateful, i.e.
	 * they will be marked as done/scheduled, when this method is called. In
	 * other word they will return <code>null</code> or a different time the
	 * next time this method is called.
	 * </p>
	 * 
	 * <p>
	 * For example the cyclic trigger:
	 * 
	 * <pre>
	 * {@code
	 * EVERY 5 MINUTES FOR 1 HOUR STARTING TIME OF an_event
	 * }
	 * </pre>
	 * 
	 * will return <code>null</code> until <code>an_event</code> has happened.
	 * Immediately after the event it will return the time of the event. It will
	 * then return <code>null</code> until 5 minutes have passed. Then it will
	 * return the time of the event + 5 minutes. After that it will return
	 * <code>null</code> again until another 5 minutes have passed, etc.
	 * </p>
	 * 
	 * @return The next run time or null if there is no next run time.
	 */
	public abstract ArdenTime getNextRunTime(ExecutionContext context);

	/**
	 * Whether to run for an external event such as 'penicillin_storage'. This
	 * will also mark some triggers as scheduled similar to
	 * {@link #getNextRunTime(ExecutionContext)}.
	 */
	public abstract boolean runOnEvent(String mapping, ArdenTime eventTime);

}
