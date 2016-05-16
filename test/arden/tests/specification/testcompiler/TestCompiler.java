package arden.tests.specification.testcompiler;

/**
 * Implements this interface and add create an instance in
 * {@link SpecificationTest} so all tests can access the compiler.
 */
public interface TestCompiler {

	/**
	 * Used to check whether only compiletime tests (e.g. grammar tests, compile
	 * time errors) should be run, or both compiletime and runtime tests (e.g.
	 * operator tests) should also be run.
	 * 
	 * @return <code>true</code> if runtime tests should run, <code>false</code>
	 *         otherwise.
	 */
	public boolean isRuntimeSupported();

	/**
	 * Used to only run tests for Arden Syntax versions which are supported.
	 * 
	 * @param major
	 *            Major version number, e.g. 2 for version 2.9
	 * @param minor
	 *            Minor version number, e.g. 9 for version 2.9
	 * @return <code>true</code> if tests for this version should run,
	 *         <code>false</code> otherwise.
	 */
	public boolean isVersionSupported(int major, int minor);
	
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
	 * @see TestCompilerResult
	 */
	public TestCompilerResult compileAndRun(String code) throws TestCompilerException;
	
	/**
	 * Used to insert mappings into tests, e.g. a <code>READ</code> mapping or <code>INTERFACE</code> mapping . 
	 * 
	 * @return {@link TestCompilerMappings} or <code>null</code> to skip tests which required mappings.
	 * @see TestCompilerMappings
	 */
	public TestCompilerMappings getMappings();

}
