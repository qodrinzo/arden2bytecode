package arden.tests.specification.testcompiler;

/**
 * Represents a message that is written after an event. See each fields comment
 * for more information.
 */
public class TestCompilerDelayedMessage {
	public static final int PRECISION_MILLIS = 200;

	/**
	 * The delay between the occurrence/call of an event and the time the
	 * message arrived. For example:
	 * <ol>
	 * <li>MLM1 has the following trigger:
	 * <code>5 SECONDS AFTER TIME OF the_event;</code>
	 * <li>MLM1 calls MLM2 with a delay: <code>CALL mlm2 DELAY 2 SECONDS;</code>
	 * <li>MLM2 writes a message: <code>WRITE "a message";</code>
	 * </ol>
	 * 
	 * The expected delay would be 5 seconds + 2 seconds = 7 seconds, i.e. 7000
	 * milliseconds. <br>
	 * This is similar to <code>TRIGGERTIME - EVENTTIME</code> if
	 * <code>EVENTTIME</code> is the time the event is called. <br>
	 * 
	 * <p>
	 * Should be precise up to {@value #PRECISION_MILLIS} milliseconds.
	 * </p>
	 */
	public final long delayMillis;

	/**
	 * The text/message from a <code>WRITE</code> statement as a String.
	 */
	public final String message;

	public TestCompilerDelayedMessage(long delayMillis, String message) {
		this.delayMillis = delayMillis;
		this.message = message;
	}
}
