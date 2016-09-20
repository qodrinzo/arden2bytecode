package arden.runtime.evoke;

import arden.runtime.ArdenEvent;
import arden.runtime.ArdenTime;
import arden.runtime.ExecutionContext;

public interface Trigger {

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
	public ArdenTime getNextRunTime(ExecutionContext context);

	/**
	 * Whether to immediately (no delay) run for an external event such as
	 * 'penicillin_storage'. The event will be compared with others via it's
	 * {@link #equals(Object)} method, which can be overridden. <br>
	 * No state is changed in contrast to {@link #scheduleEvent(ArdenEvent)}.
	 * 
	 * <p>
	 * E.g. the trigger <code>10 SECONDS AFTER TIME OF an_event</code> will
	 * return false, when this method called with a matching
	 * <code>an_event</code>.
	 * </p>
	 * 
	 * @param event
	 *            The event to check.
	 * @return whether this trigger is immediately triggered by the event.
	 */
	public boolean runOnEvent(ArdenEvent event);

	/**
	 * This method will mark some triggers as scheduled similar to
	 * {@link #getNextRunTime(ExecutionContext)}.
	 * 
	 * <p>
	 * E.g. after scheduling a matching <code>an_event</code> for the trigger
	 * <code>10 SECONDS AFTER TIME OF an_event</code>,
	 * {@link #getNextRunTime(ExecutionContext)} will return a time, that is 10
	 * seconds after the events {@link ArdenEvent#eventTime}.
	 * </p>
	 * 
	 * @param event
	 * @return
	 */
	public void scheduleEvent(ArdenEvent event);

}
