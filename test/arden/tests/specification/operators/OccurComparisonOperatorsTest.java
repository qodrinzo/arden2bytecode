package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class OccurComparisonOperatorsTest extends SpecificationTest {

	private static final String DATA = new ArdenCodeBuilder()
			.addData("x := 5; TIME x := 1990-01-01T00:00:00;")
			.addData("y := 3; TIME y := 1990-01-02T00:00:00;")
			.addData("z := 2; TIME z := 1990-01-03T00:00:00;")
			.toString();
	
	@Test
	public void testTimeOfEquivalence() throws Exception {
		assertEvaluatesToWithData(DATA,
				"(x OCCURRED NOT BEFORE 1990-03-05T11:11:11) = (TIME OF x IS NOT BEFORE 1990-03-05T11:11:11)",
				"TRUE");
	}

	@Test
	public void testWithin() throws Exception {
		assertEvaluatesToWithData(DATA, "y OCCURRED WITHIN TIME OF x TO TIME OF z", "TRUE");
		assertEvaluatesToWithData(DATA, "y OCCURRED WITHIN 3 DAYS PRECEDING (1990-03-10T00:00:00,NULL,TIME z)", "(FALSE,NULL,TRUE)");
		assertEvaluatesToWithData(DATA, "x OCCURRED WITHIN 3 DAYS FOLLOWING (1989-12-31T00:00:00,NULL,TIME z)", "(TRUE,NULL,FALSE)");
		assertEvaluatesToWithData(DATA, "x OCCURRED WITHIN 3 DAYS SURROUNDING (TIME z,1990-01-02, 1989-12-31)", "(TRUE,TRUE,TRUE)");
		assertEvaluatesToWithData(DATA,	"z OCCURRED WITHIN SAME DAY AS (FALSE,(TIME z)-1 SECOND,TIME z,1991-01-03)","(NULL,FALSE,TRUE,FALSE)");
	}

	@Test
	public void testEqual() throws Exception {
		assertEvaluatesToWithData(DATA, "x OCCURRED EQUAL TIME OF x", "TRUE");
	}

	@Test
	public void testAt() throws Exception {
		assertEvaluatesToWithData(DATA, "y OCCURRED AT TIME OF x", "FALSE");
	}

}
