package arden.tests.specification.testcompiler;

import java.util.LinkedList;
import java.util.List;

/**
 * Container for the result of an MLM execution. Contains the return value of
 * the MLM and the outputtext (e.g. messages at destinations).
 * <p>
 * For durations the internal representation (seconds, months), not the localized string
 * representation, must be used. <br>
 * For times the trailing zeros must be removed. <br>
 * For List no whitespace is allowed before/after commas and single element lists start with a comma.
 * <h4>Allowed:</h4>
 * <ul>
 *   <li>5 seconds</li>
 *   <li>10 months</li>
 *   <li>1990-11-26T22:57:05.4</li>
 *   <li>(1,2,3)</li>
 *   <li>(,1)</li>
 * </ul>
 * <h4>Not allowed:</h4>
 * <ul>
 *   <li>10 Monate</li>
 *   <li>2 years</li>
 *   <li>1990-11-26T22:57:05.400</li>
 *   <li>(1, 2, 3)</li>
 *   <li>(1)</li>
 * </ul>
 */
public class TestCompilerResult {
	public final List<String> returnValues = new LinkedList<String>();
	public final List<TestCompilerOutputText> outputTexts = new LinkedList<TestCompilerOutputText>();

	/**
	 * Output of the "WRITE" statement. For example: <br>
	 * 
	 * <code>dest := DESTINATION{log.txt}; WRITE "hello world" at dest;</code>
	 */
	public static class TestCompilerOutputText {
		public final String destination;
		public final String text;

		public TestCompilerOutputText(String destination, String text) {
			this.destination = destination;
			this.text = text;
		}
	}
}
