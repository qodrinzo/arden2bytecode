package arden.tests.specification.testcompiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public abstract class SpecificationTest {
	// Initialise your compiler here. It will be used by all tests.
	private TestCompiler compiler = new arden.tests.specification.testcompiler.impl.TestCompilerImpl();

	public TestCompiler getCompiler() {
		return compiler;
	}

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
			builder = new ArdenCodeBuilder();
		}
		String code = builder.addExpression(expression).toString();
		assertReturns(code, expected);
	}
	
	protected void assertReturns(String code, String expected) throws TestCompilerException {
		TestCompilerResult result = getCompiler().compileAndRun(code);
		assertEquals("Too many return values.", 1, result.returnValues.size());
		String returnValue = result.returnValues.get(0);
		assertEquals(expected.toLowerCase(), returnValue.toLowerCase());
	}
	
	protected void assertNoReturn(String code) throws TestCompilerException {
		TestCompilerResult result = getCompiler().compileAndRun(code);
		if(result.returnValues.isEmpty()) {
			// test passed
		} else {
			// a single "NULL" is also okay
			assertEquals("Too many return values.", 1, result.returnValues.size());
			assertEquals("null", result.returnValues.get(0).toLowerCase());
		}
	}
	
	protected void assertValidStatement(String statement) throws TestCompilerException {
		String code = new ArdenCodeBuilder().addAction(statement).toString();
		assertValid(code);
	}
	
	protected void assertInvalidStatement(String statement) throws TestCompilerException {
		String code = new ArdenCodeBuilder().addAction(statement).toString();
		assertInvalid(code);
	}
	
	protected void assertInvalidExpression(String expression) throws TestCompilerException {
		String code = new ArdenCodeBuilder().addExpression(expression).toString();
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
	
	protected void assertException(String code) {
		try {
			getCompiler().compileAndRun(code);
			fail("Expected a " + TestCompilerException.class.getSimpleName() + " to be thrown.");
		} catch(TestCompilerException e) {
			// test passed
		}
	}
	
	
}
