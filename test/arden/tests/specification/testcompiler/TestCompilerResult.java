package arden.tests.specification.testcompiler;

import java.util.LinkedList;
import java.util.List;

/**
 * Container for the result of an MLM execution. Contains the return values of
 * the MLM and the written messages.
 * 
 * <p>
 * For durations the internal representation (seconds, months), not the
 * localized string representation, must be used. It must be correctly pluralized<br>
 * For times the trailing zeros must be removed. <br>
 * For lists no whitespace is allowed before/after commas and single element
 * lists start with a comma. <br>
 * Truth values 0 and 1 must return FALSE/TRUE. <br>
 * String constants must be enclosed in double quotes <br>
 * Case doesn't matter. <br>
 * </p>
 * 
 * <h4>Allowed:</h4>
 * <ul>
 *   <li>"A string"</li>
 *   <li>5 seconds</li>
 *   <li>1 second</li>
 *   <li>10 months</li>
 *   <li>1990-11-26T22:57:05.4</li>
 *   <li>(1,2,3)</li>
 *   <li>(,1)</li>
 *   <li>()</li>
 *   <li>TRUE</li>
 * </ul>
 * <h4>Not allowed:</h4>
 * <ul>
 *   <li>10 Monate</li>
 *   <li>2 years</li>
 *   <li>1990-11-26T22:57:05.400</li>
 *   <li>(1, 2, 3)</li>
 *   <li>(1)</li>
 *   <li>truth value 1</li>
 * </ul>
 */
public class TestCompilerResult {
	/**
	 * Output of the <code>RETURN</code> statement.
	 */
	public final List<String> returnValues = new LinkedList<String>();
	
	/**
	 * Output of the <code>WRITE</code> statement. Destination doesn't matter.
	 */
	public final List<String> messages = new LinkedList<String>();

}
