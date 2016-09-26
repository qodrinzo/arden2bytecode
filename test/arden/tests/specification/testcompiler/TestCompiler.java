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
	 *            Arden Syntax code, which may contain multiple mlms.
	 * @throws TestCompilerCompiletimeException
	 *             e.g. on a a lexer, parser or validation error
	 */
	public void compile(String code) throws TestCompilerCompiletimeException;

	/**
	 * Compile and run the given code
	 * 
	 * @param code
	 *            Arden Syntax code, which may contain multiple mlms. In that
	 *            case the first mlm is run.
	 * @return {@link TestCompilerResult}
	 * @throws TestCompilerException
	 * @see TestCompilerResult
	 */
	public TestCompilerResult compileAndRun(String code) throws TestCompilerException;

}
