package arden.tests.specification.testcompiler;

/**
 * These settings influence, how the tests are run. See each fields comment for
 * more information.
 */
public class TestCompilerSettings {

	/**
	 * The highest Arden Syntax version, which this compiler supports.
	 * <p>
	 * Tests that require features from newer versions will be skipped.<br>
	 * The chosen {@linkplain ArdenCodeBuilder#ArdenCodeBuilder(ArdenVersion)
	 * MLM template} may change depending on this version. See
	 * {@link SpecificationTest#createCodeBuilder()} for how the template for
	 * MLMs is chosen, depending on the targetVersion.
	 * </p>
	 */
	public final ArdenVersion targetVersion;

	/**
	 * The lowest Arden Syntax version, which this compiler supports.
	 * <p>
	 * Tests that require deprecated or removed features from older versions
	 * will be skipped. E.g. tests for the "filename:" slot or the free text in
	 * the "citations:" and "links:" slot, when lowestVersion is Version 2.
	 * </p>
	 * <p>
	 * This is also used in tests, that create MLMs in every version from
	 * lowestVersion to {@linkplain #targetVersion} (inclusive), and check that
	 * no languages features newer than the specified version are usable. <br>
	 * These tests are necessary as otherwise portability of the MLMs can not be
	 * guaranteed.
	 * </p>
	 */
	public final ArdenVersion lowestVersion;

	/**
	 * Used to check if runtime tests (e.g. operator tests, which check return
	 * values) should be run by calling
	 * {@link TestCompiler#compileAndRun(String)}, or only compiled to check for
	 * syntax errors by calling {@link TestCompiler#compile(String)}. <br>
	 * This is useful for testing parsers in text editors, which can't run code.
	 */
	public final boolean isRuntimeSupported;

	/**
	 * Whether possibly long running tests that call the
	 * {@link TestCompiler#compileAndRunForEvent(String, String, int)} method
	 * should be run.
	 */
	public final boolean runDelayTests;

	public TestCompilerSettings(ArdenVersion targetVersion, ArdenVersion lowestVersion, boolean isRuntimeSupported,
			boolean runDelayTests) {
		this.targetVersion = targetVersion;
		this.lowestVersion = lowestVersion;
		this.isRuntimeSupported = isRuntimeSupported;
		this.runDelayTests = runDelayTests;
	}
}
