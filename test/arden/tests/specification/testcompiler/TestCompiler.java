package arden.tests.specification.testcompiler;

/**
 * Implements this interface and create an instance in {@link SpecificationTest}
 * so all tests can access the compiler.
 */
public interface TestCompiler {

	/**
	 * These settings influence, how the tests are run, e.g. if backward
	 * compatibility tests should be skipped, or what value the "arden:" slot is
	 * set to.
	 * 
	 * @return {@link TestCompilerSettings}
	 * @see TestCompilerSettings
	 */
	public TestCompilerSettings getSettings();

	/**
	 * Used to insert valid mappings into tests, e.g. a <code>READ</code>
	 * mapping or <code>INTERFACE</code> mapping .
	 * 
	 * @return {@link TestCompilerMappings} or <code>null</code> to skip tests
	 *         which required mappings.
	 * @see TestCompilerMappings
	 */
	public TestCompilerMappings getMappings();

	/**
	 * Compile the given code
	 * 
	 * @param code
	 *            Arden Syntax code, which may contain multiple MLMs.
	 * @throws TestCompilerCompiletimeException
	 *             e.g. on a a lexer, parser or validation error
	 */
	public void compile(String code) throws TestCompilerCompiletimeException;

	/**
	 * Compile and run the given code
	 * 
	 * @param code
	 *            Arden Syntax code, which may contain multiple MLMs. In that
	 *            case the first mlm is run.
	 * @return {@link TestCompilerResult}
	 * @throws TestCompilerException
	 *             e.g. a {@link TestCompilerRuntimeException} on a runtime
	 *             error
	 * @see TestCompilerResult
	 */
	public TestCompilerResult compileAndRun(String code) throws TestCompilerException;

	/**
	 * Compile one or multiple MLMs and call an event that triggers them. Then
	 * collect a certain number of {@link TestCompilerDelayedMessage#message
	 * messages} and their {@link TestCompilerDelayedMessage#delayMillis delay},
	 * and return them as a list of {@link TestCompilerDelayedMessage}s. <br>
	 * Used to test delayed/cyclic triggers and delayed calls.
	 *
	 * <p>
	 * Example calculation:
	 * 
	 * <pre>
	 * engine.setMlms(compiler.compile(code));
	 * long startTime = System.currentTimeMillis();
	 * engine.callEvent(eventMapping);
	 * while (messages.size() < messagesToCollect) {
	 * 	// blocks until a message is received
	 * 	String message = engine.getNextMessage();
	 * 	long delay = System.currentTimeMillis() - startTime;
	 * 	messages.add(new TestCompilerDelayedMessage(delay, message));
	 * }
	 * engine.shutdown();
	 * </pre>
	 * </p>
	 * 
	 * <p>
	 * This method may block the execution of the current test until a result is
	 * available.<br>
	 * The long running tests that use this method can be skipped via the
	 * {@link TestCompilerSettings#runDelayedTests} setting.
	 * </p>
	 * 
	 * @param code
	 *            Arden Syntax code, which may contain multiple MLMs. All MLMs
	 *            should wait for events.
	 * @param eventMapping
	 *            The mapping for an event, that must be called.
	 * @param messagesToCollect
	 *            The number of messages that should be collected and returned.
	 * @return The {@link TestCompilerDelayedMessage messages} that were
	 *         collected with their respective delay.
	 * @throws TestCompilerException
	 * @see {@link TestCompilerDelayedMessage}
	 */
	public TestCompilerDelayedMessage[] compileAndRunForEvent(String code, String eventMapping, int messagesToCollect)
			throws TestCompilerException;
}
