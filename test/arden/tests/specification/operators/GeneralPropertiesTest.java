package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;
import arden.tests.specification.testcompiler.TestCompilerException;

public class GeneralPropertiesTest extends SpecificationTest {

	@Test
	public void testDifferentNumberOfElements() throws Exception {
		assertEvaluatesTo("(1,2) + (1,2,3)", "NULL");
	}

	@Test
	public void testSingleElementReplication() throws Exception {
		assertEvaluatesTo("1+(3,4)", "(4,5)");
	}

	@Test
	public void testPrecedence() throws Exception {
		assertEvaluatesTo("1+2*3", "7");
		
		assertEvaluatesTo("LENGTH (TRIM \" x  \")", "1");
		assertInvalidExpression("LENGTH TRIM \" x  \"");
		
		// TODO is this really invalid?
		// the specifications grammar and precedence table seem to be incompatible
		//assertInvalidExpression("TIME UPPERCASE \" x  \"");
		assertEvaluatesTo("TIME (UPPERCASE \" x  \")", "NULL");
		
		
		assertEvaluatesTo("(COUNT (SQRT 5)) DAYS BEFORE 2015-01-05T00:00:00", "2015-01-04T00:00:00");
		assertEvaluatesTo("COUNT SQRT 5 DAYS BEFORE 2015-01-05T00:00:00", "2015-01-04T00:00:00");
		
		assertEvaluatesTo("SUBSTRING (FLOOR (SQRT 5)) CHARACTERS STARTING AT (LENGTH OF \"123\") FROM \"abcdefg\"", "\"cd\"");
		assertEvaluatesTo("SUBSTRING FLOOR SQRT 5 CHARACTERS STARTING AT LENGTH OF \"123\" FROM \"abcdefg\"", "\"cd\"");
	}

	@Test
	public void testAssociativity() throws Exception {
		// left associative
		assertEvaluatesTo("3-4-5", "-6");
		
		// right associative
		assertEvaluatesTo("FLOOR ABS SQRT 5", "2");
		
		// non associative
		assertInvalidExpression("2**3**4");
	}

	@Test
	public void testPrimaryTimeHandling() throws Exception {
		String data = new ArdenCodeBuilder()
				.addData("w := 1;")
				.addData("x := 5; TIME x := 1990-01-01T00:00:00;")
				.addData("y := 3; TIME y := TIME x;")
				.addData("z := 2; TIME z := 1995-01-01T00:00:00;")
				.toString();
		
		assertVariableReturnsTimeWithData(data, "COS x", "1990-01-01T00:00:00");
		assertVariableReturnsTimeWithData(data, "x * y", "1990-01-01T00:00:00");
		assertVariableReturnsTimeWithData(data, "x > y", "1990-01-01T00:00:00");
		assertVariableReturnsTimeWithData(data, "x * z", "NULL");
		assertVariableReturnsTimeWithData(data, "y * w", "NULL");
	}
	
	/**
	 * Saves expression into a variable and returns its time
	 */
	private void assertVariableReturnsTimeWithData(String data, String expression, String expectedTime)
			throws TestCompilerException {
		String time = new ArdenCodeBuilder(data)
				.addData("v :=" + expression + ";")
				.addAction("RETURN TIME v;")
				.toString();
		assertReturns(time, expectedTime);
	}
}
