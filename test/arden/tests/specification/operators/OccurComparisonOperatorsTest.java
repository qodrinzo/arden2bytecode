package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.SpecificationTest;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;

public class OccurComparisonOperatorsTest extends SpecificationTest {

	private String createData() {
		return createCodeBuilder()
				.addData("x := 5; TIME x := 1990-01-01T00:00:00;")
				.addData("y := 3; TIME y := 1990-01-02T00:00:00;")
				.addData("z := 2; TIME z := 1990-01-03T00:00:00;")
				.toString();
	}

	@Test
	public void testTimeOfEquivalence() throws Exception {
		assertEvaluatesToWithData(createData(), "(x OCCURRED NOT BEFORE 1990-03-05T11:11:11) = (TIME OF x IS NOT BEFORE 1990-03-05T11:11:11)", "TRUE");
	}

	@Test
	public void testWithin() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "y OCCURRED WITHIN TIME OF x TO TIME OF z", "TRUE");
		assertEvaluatesToWithData(data, "y OCCURRED WITHIN 3 DAYS PRECEDING (1990-03-10T00:00:00,NULL,TIME z)", "(FALSE,NULL,TRUE)");
		assertEvaluatesToWithData(data, "x OCCURRED WITHIN 3 DAYS FOLLOWING (1989-12-31T00:00:00,NULL,TIME z)", "(TRUE,NULL,FALSE)");
		assertEvaluatesToWithData(data, "x OCCURRED WITHIN 3 DAYS SURROUNDING (TIME z,1990-01-02, 1989-12-31)", "(TRUE,TRUE,TRUE)");
		assertEvaluatesToWithData(data, "z OCCURRED WITHIN SAME DAY AS (FALSE,(TIME z)-1 SECOND,TIME z,1991-01-03)", "(NULL,FALSE,TRUE,FALSE)");
	}

	@Test
	public void testEqual() throws Exception {
		assertEvaluatesToWithData(createData(), "x OCCURRED EQUAL TIME OF x", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_1)
	public void testAt() throws Exception {
		assertEvaluatesToWithData(createData(), "y OCCURRED AT TIME OF x", "FALSE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testTimeOfDay() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "y OCCURRED WITHIN 2 HOURS SURROUNDING 01:00:00", "TRUE");
		assertEvaluatesToWithData(data, "y OCCURRED WITHIN 5 MINUTES SURROUNDING 01:00:00", "FALSE");
		assertEvaluatesToWithData(data, "y OCCURRED AT 00:00:00", "TRUE");
		assertEvaluatesToWithData(data, "y OCCURRED AT 00:00:01", "FALSE");
	}

}
