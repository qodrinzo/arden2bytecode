package arden.tests.specification.testcompiler;

/**
 * Container for test mappings.
 * See each methods comment for more information.
 */
public class TestCompilerMappings {
	public String interfaceMapping;
	public String eventMapping;
	public String messageMapping;
	public String destinationMapping;
	public String readMapping;
	public String readMultipleMapping;

	public TestCompilerMappings(String interfaceMapping, String eventMapping, String messageMapping,
			String destinationMapping, String readMapping, String readMultipleMapping) {
		this.interfaceMapping = interfaceMapping;
		this.eventMapping = eventMapping;
		this.messageMapping = messageMapping;
		this.destinationMapping = destinationMapping;
		this.readMapping = readMapping;
		this.readMultipleMapping = readMultipleMapping;
	}
	
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
	public String getInterfaceMapping() {
		return interfaceMapping;
	}
	
	/**
	 * This method is used to test events. <br>
	 * Test mlms must be able to subscribe to the event for this mapping via the evoke slot.
	 * It must also be possible to <code>CALL</code> the event.
	 * 
	 * @return a mapping for an event
	 */
	public String getEventMapping() {
		return eventMapping;
	}
	
	/**
	 * This method is used to test messages.
	 * The message for this mapping must contain the text "test message".
	 * 
	 * @return a mapping for a message
	 */
	public String getMessageMapping() {
		return messageMapping;
	}
	
	/**
	 * This method is used to test destinations.
	 * 
	 * @return a mapping for a message
	 */
	public String getDestinationMapping() {
		return destinationMapping;
	}

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
	public String getReadMapping() {
		return readMapping;
	}
	
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
	public String getReadMultipleMapping() {
		return readMultipleMapping;
	}

}
