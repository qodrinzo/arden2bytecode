package arden.tests.specification.testcompiler;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

import org.junit.Rule;

import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;

/**
 * Base test class which all tests extend. Adds useful asserts, annotations and shared access
 * to the compiler.
 */
public abstract class SpecificationTest {
	
	/** Initialise your compiler here. It will be used by all tests. */
	private final TestCompiler compiler = new arden.tests.specification.testcompiler.impl.TestCompilerImpl();
	private final TestCompilerSettings settings = compiler.getSettings();
	
	// Decorator which intercepts calls to the compiler to skip unsupported tests
	private TestCompiler runtimeCheckedCompiler = new TestCompiler() {
		@Override
		public TestCompilerSettings getSettings() {
			return compiler.getSettings();
		};
		@Override
		public TestCompilerMappings getMappings() {
			TestCompilerMappings mappings = compiler.getMappings();
			// only run test if mappings are provided
			assumeNotNull("Compiler doesn't support tests with mappings", mappings);
			return mappings;
		};
		@Override
		public TestCompilerResult compileAndRun(String code) throws TestCompilerException {
			// only run tests if runtime is supported
			assumeTrue("Compiler doesn't support runtime tests", getSettings().isRuntimeSupported);
			return compiler.compileAndRun(code);
		}
		@Override
		public void compile(String code) throws TestCompilerCompiletimeException {
			compiler.compile(code);
		}
		@Override
		public TestCompilerDelayedMessage[] compileAndRunForEvent(String code, String eventMapping,
				int messagesToCollect) throws TestCompilerException {
			assumeTrue("Compiler doesn't support runtime tests", getSettings().isRuntimeSupported);
			assumeTrue("Compiler doesn't support event tests", getSettings().runDelayTests);
			return compiler.compileAndRunForEvent(code, eventMapping, messagesToCollect);
		}
	};
	
	protected TestCompilerSettings getSettings() {
		return settings;
	}
	
	protected TestCompiler getCompiler() {
		return runtimeCheckedCompiler;
	}
	
	protected TestCompilerMappings getMappings() {
		return runtimeCheckedCompiler.getMappings();
	}

	/**
	 * Initializes a code builder with a
	 * {@link ArdenCodeBuilder#ArdenCodeBuilder(ArdenVersion) template}
	 * compatible to the highest possible version. <br>
	 * The highest possible version is either the compiler's
	 * {@link TestCompilerSettings#targetVersion target version} or the tests
	 * {@link Compatibility#max() max compatible version}, whichever is lower.
	 * 
	 * @return the {@link ArdenCodeBuilder}
	 */
	protected ArdenCodeBuilder createCodeBuilder() {
		ArdenVersion maxPossibleVersion;
		if (settings.targetVersion.ordinal() <= compatibilityRule.getCurrentTestMaxVersion().ordinal()) {
			maxPossibleVersion = settings.targetVersion;
		} else {
			maxPossibleVersion = compatibilityRule.getCurrentTestMaxVersion();
		}

		return new ArdenCodeBuilder(maxPossibleVersion);
	}

	protected ArdenCodeBuilder createEmptyLogicSlotCodeBuilder() {
		return createCodeBuilder().clearSlotContent("logic:");
	}

	// Used for compatibility tests with the <code>@Compatibility</code> annotation.
	@Rule
	public CompatibilityRule compatibilityRule = new CompatibilityRule(getSettings());
	
	
	/**
	 * Tests if the result of the evaluated expressions is equal to the expected
	 * string (case insensitive).
	 */
	protected void assertEvaluatesTo(String expression, String expected) throws TestCompilerException {
		assertEvaluatesToWithData(null, expression, expected);
	}
	
	protected void assertEvaluatesToWithData(String dataCode, String expression, String expected) throws TestCompilerException {
		ArdenCodeBuilder builder;
		if(dataCode != null) {
			builder = new ArdenCodeBuilder(dataCode);
		} else {
			builder = createCodeBuilder();
		}
		String code = builder.addExpression(expression).toString();
		assertReturns(code, expected);
	}
	
	protected void assertReturns(String code, String... expected) throws TestCompilerException {
		if(!getSettings().isRuntimeSupported) {
			assertValid(code);
			return;
		}
		
		TestCompilerResult result = getCompiler().compileAndRun(code);
		if(expected.length == 0) {
			// no return values
			if(result.returnValues.isEmpty()) {
				// test passed
			} else {
				// a single "NULL" is also okay
				assertEquals("Too many return values.", 1, result.returnValues.size());
				assertEquals("null", result.returnValues.get(0).toLowerCase());
			}
		} else if (expected.length == 1) {
			// single return value
			assertEquals("Wrong number of return values.", 1, result.returnValues.size());
			String returnValue = result.returnValues.get(0);
			assertEquals(expected[0].toLowerCase(), returnValue.toLowerCase());
		} else {
			// multiple return values
			assertEquals("Too many or few return values.", expected.length, result.returnValues.size());
			String[] expected_lowercase = new String[expected.length];
			String[] returnValues_lowercase = new String[result.returnValues.size()];
			for (int i = 0; i < expected.length; i++) {
				expected_lowercase[i] = expected[i].toLowerCase();
				returnValues_lowercase[i] = result.returnValues.get(i).toLowerCase();
			}
			assertArrayEquals(expected_lowercase, returnValues_lowercase);
		}
	}
	
	protected void assertNoReturn(String code) throws TestCompilerException {
		assertReturns(code); // no expected values
	}
	
	protected void assertWrites(String code, String expected) throws TestCompilerException {
		if (!getSettings().isRuntimeSupported) {
			assertValid(code);
			return;
		}

		TestCompilerResult result = getCompiler().compileAndRun(code);
		String message = result.messages.get(0);
		assertEquals(expected.toLowerCase(), message.toLowerCase());
	}
	
	protected void assertValidStatement(String statement) throws TestCompilerException {
		String code = createCodeBuilder().addData(statement).toString();
		assertValid(code);
	}
	
	protected void assertInvalidStatement(String statement) throws TestCompilerException {
		String code = createCodeBuilder().addData(statement).toString();
		assertInvalid(code);
	}
	
	protected void assertInvalidExpression(String expression) throws TestCompilerException {
		String code = createCodeBuilder().addExpression(expression).toString();
		assertInvalid(code);
	}
	
	protected void assertValid(String code) throws TestCompilerCompiletimeException {
		getCompiler().compile(code);
	}
	
	protected void assertInvalid(String code) {
		try {
			getCompiler().compile(code);
			fail("Expected a " + TestCompilerCompiletimeException.class.getSimpleName() + " to be thrown.");
		} catch(TestCompilerCompiletimeException e) {
			// test passed
		}
	}

	protected void assertValidSlot(String slotname, String slotcontent) throws TestCompilerCompiletimeException {
		String code = createCodeBuilder().replaceSlotContent(slotname, slotcontent).toString();
		assertValid(code);
	}

	protected void assertInvalidSlot(String slotname, String slotcontent) {
		String code = createCodeBuilder().replaceSlotContent(slotname, slotcontent).toString();
		assertInvalid(code);
	}

	protected void assertSlotIsRequired(String slotname) {
		String missingSlot = createCodeBuilder().removeSlot(slotname).toString();
		assertInvalid(missingSlot);
	}

	protected void assertSlotIsOptional(String slotname) throws TestCompilerCompiletimeException {
		String missingSlot = createCodeBuilder().removeSlot(slotname).toString();
		assertValid(missingSlot);
	}

	protected void assertWritesAfterEvent(String code, String event, String... messages) throws TestCompilerException {
		if (!getSettings().isRuntimeSupported || !getSettings().runDelayTests) {
			assertValid(code);
			return;
		}
		TestCompilerDelayedMessage[] delayedMessages = getCompiler().compileAndRunForEvent(code, event, messages.length);
		for (int i = 0; i < messages.length; i++) {
			assertEquals(messages[i].toLowerCase(), delayedMessages[i].message.toLowerCase());
		}
	}

	protected void assertDelayedBy(String code, String event, long... delayMillis) throws TestCompilerException {
		if (!getSettings().isRuntimeSupported || !getSettings().runDelayTests) {
			assertValid(code);
			return;
		}
		TestCompilerDelayedMessage[] messages = getCompiler().compileAndRunForEvent(code, event, delayMillis.length);
		for (int i = 0; i < delayMillis.length; i++) {
			assertEquals(delayMillis[i], messages[i].delayMillis, TestCompilerDelayedMessage.PRECISION_MILLIS);
		}
	}

}
