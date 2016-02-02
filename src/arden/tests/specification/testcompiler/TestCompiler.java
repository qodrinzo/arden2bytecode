package arden.tests.specification.testcompiler;

public interface TestCompiler {
	/**
	 * Compile the given code
	 * 
	 * @param code Arden Syntax code, which may contain multiple mlms.
	 * @throws TestCompilerCompiletimeException e.g. on a a lexer, parser or validation error
	 */
	public void compile(String code) throws TestCompilerCompiletimeException;
	
	/**
	 * Compile and run the given code
	 * 
	 * @param code Arden Syntax code, which may contain multiple mlms. In that case the first mlm is run. 
	 * @return {@link TestCompilerResult}
	 * @throws TestCompilerException
	 */
	public TestCompilerResult compileAndRun(String code, String... args) throws TestCompilerException;

	/**
	 * This method is used to test <code>INTERFACE</code> mappings. <br>
	 * It must be possible to <code>CALL</code> the interface for this mapping.
	 * It must accept parameters and return the following two values: <br>
	 * <ol>
	 * <li><code>args[0] + args[1]</code></li>
	 * <li><code>args[0] * args[1]</code></li>
	 * </ol> 
	 * 
	 * @return a mapping for an interface
	 */
	public String getTestInterfaceMapping();
	
	/**
	 * This method is used to test events. <br>
	 * Test mlms must be able to subscribe to the event for this mapping via the evoke slot.
	 * It must also be possible to <code>CALL</code> the event.
	 * 
	 * @return a mapping for an event 
	 */
	public String getTestEventMapping();
	
	/**
	 * This method is used to test messages.
	 * The message for this mapping must contain the text "test message".
	 * 
	 * @return a mapping for a message
	 */
	public String getTestMessageMapping();
	
	/**
	 * This method is used to test destinations.
	 * 
	 * @return a mapping for a message
	 */
	public String getTestDestinationMapping();

	/**
	 * This method is used to test the <code>READ</code> statement. <br>
	 * When the mapping is read, it must return a list of the following values with their respective primary time:
	 * 
	 * <table summary="database content">
	 *   <tr>
	 *      <th>Value</th><th>Primary Time</th>
	 *   </tr>
	 *   <tr>
	 *      <td>1</td><td>2000-01-01T00:00:00</td>
	 *   </tr>
	 *   <tr>
	 *      <td>2</td><td>1990-01-02T00:00:00</td>
	 *   </tr>
	 *   <tr>
	 *      <td>3</td><td>1990-01-01T00:00:00</td>
	 *   </tr>
	 *   <tr>
	 *      <td>4</td><td>1990-01-03T00:00:00</td>
	 *   </tr>
	 *   <tr>
	 *      <td>5</td><td>1970-01-01T00:00:00</td>
	 *   </tr>
	 * </table>
	 * 
	 * @return a mapping for a database query
	 */
	public String getTestReadMapping();
	
	/**
	 * This method is used to test the <code>READ</code> and <code>READ AS</code> statements. <br>
	 * When the mapping is read, it must return two lists, one for each of the following value columns:
	 * 
	 * <table summary="database content">
	 *   <tr>
	 *      <th>Value1</th><th>Value2</th><th>Primary Time</th>
	 *   </tr>
	 *   <tr>
	 *      <td>1</td><td>"a"</td><td>2000-01-01T00:00:00</td>
	 *   </tr>
	 *   <tr>
	 *      <td>2</td><td>"b"</td><td>1990-01-02T00:00:00</td>
	 *   </tr>
	 *   <tr>
	 *      <td>3</td><td>"c"</td><td>1990-01-01T00:00:00</td>
	 *   </tr>
	 *   <tr>
	 *      <td>4</td><td>"d"</td><td>1990-01-03T00:00:00</td>
	 *   </tr>
	 *   <tr>
	 *      <td>5</td><td>"e"</td><td>1970-01-01T00:00:00</td>
	 *   </tr>
	 * </table>
	 * @return a mapping for a database query
	 */
	public String getTestReadMultipleMapping();
}
