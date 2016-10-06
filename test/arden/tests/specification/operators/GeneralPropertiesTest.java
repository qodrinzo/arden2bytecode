package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

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
	public void testPrimaryTimeHandling() throws Exception {
		String data = createCodeBuilder()
				.addData("w := 1;")
				.addData("x := 5; TIME x := 1990-01-01T00:00:00;")
				.addData("y := 3; TIME y := TIME x;")
				.addData("z := 2; TIME z := 1995-01-01T00:00:00;")
				.toString();

		assertEvaluatesToWithData(data, "TIME COS x", "1990-01-01T00:00:00");
		assertEvaluatesToWithData(data, "TIME (x * y)", "1990-01-01T00:00:00");
		assertEvaluatesToWithData(data, "TIME (x > y)", "1990-01-01T00:00:00");
		assertEvaluatesToWithData(data, "TIME (x * z)", "NULL");
		assertEvaluatesToWithData(data, "TIME (y * w)", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testApplicabilityHandling() throws Exception {
		assertEvaluatesTo("APPLICABILITY 1", "TRUE");
		assertEvaluatesTo("APPLICABILITY COS 1", "TRUE");

		// minimum applicability of parameters
		String data = createCodeBuilder().addData("x := 1;")
				.addData("y := 2;")
				.addData("APPLICABILITY x := TRUTH VALUE 0.5;")
				.addData("APPLICABILITY y := TRUTH VALUE 0.7;")
				.toString();
		assertEvaluatesToWithData(data, "APPLICABILITY COS y", "TRUE");
		assertEvaluatesToWithData(data, "APPLICABILITY (x * y)", "truth value 0.5");
		assertEvaluatesToWithData(data, "APPLICABILITY (y * x)", "truth value 0.5");
	}

	@Test
	public void testPrecedence() throws Exception {
		assertEvaluatesTo("1+2*3", "7");
		assertEvaluatesTo("(COUNT (SQRT 5)) DAYS BEFORE 2015-01-05T00:00:00", "2015-01-04T00:00:00");
		assertEvaluatesTo("COUNT SQRT 5 DAYS BEFORE 2015-01-05T00:00:00", "2015-01-04T00:00:00");
	}

	@Test
	public void testAssociativity() throws Exception {
		// left associative
		assertEvaluatesTo("3-4-5", "-6");

		// right associative
		assertEvaluatesTo("ABS SQRT LOG10 10000", "2");

		// non associative
		assertInvalidExpression("2**3**4");
		assertEvaluatesTo("(2**3)**4", "4096");

	}
}
