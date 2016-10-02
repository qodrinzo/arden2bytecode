package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class QueryAggregationOperatorsTest extends SpecificationTest {

	private String createData() {
		return createCodeBuilder()
				.addData("x := 12; TIME x := 1990-03-15T15:00:00;")
				.addData("y := 13; TIME y := 1990-03-16T15:00:00;")
				.addData("z := 14; TIME z := 1990-03-17T15:00:00;")
				.addData("mylist := (x, y, z);")
				.addData("a := 4; TIME a := 1990-03-16T15:00:00;")
				.toString();
	}

	@Test
	public void testNearest() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "NEAREST (2 days before 1990-03-18T16:00:00) FROM mylist", "13");
		assertEvaluatesToWithData(data, "NEAREST 1990-03-16T16:00:00 FROM a", "4");
		assertEvaluatesToWithData(data, "NEAREST 1990-03-16T16:00:00 FROM (a, 5)", "NULL");
		// element with smallest index when tied
		assertEvaluatesToWithData(data, "NEAREST 1990-03-16T16:00:00 FROM (a,mylist)", "4");
		assertEvaluatesTo("NEAREST 1990-03-16T16:00:00 FROM (3,4)", "NULL");
		assertEvaluatesTo("NEAREST 1990-03-16T16:00:00 FROM ()", "NULL");
		// primary time preserved
		assertEvaluatesToWithData(data, "TIME NEAREST (2 days before 1990-03-18T16:00:00) FROM mylist", "1990-03-16T15:00:00");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testNearestTimeOfDay() throws Exception {
		String data = createCodeBuilder()
				.addData("x := 1; TIME x := TODAY ATTIME 08:00:00;")
				.addData("y := 2; TIME y := TODAY ATTIME 12:00:00;")
				.addData("z := 3; TIME z := TODAY ATTIME 20:00:00;")
				.addData("mylist := (x, y, z);")
				.toString();
		assertEvaluatesToWithData(data, "NEAREST 08:10:00 FROM mylist", "1");
		assertEvaluatesToWithData(data, "NEAREST 11:30:00 FROM mylist", "2");
		assertEvaluatesToWithData(data, "NEAREST 19:00:00 FROM mylist", "3");
		assertEvaluatesToWithData(data, "NEAREST 20:00:00 FROM mylist", "3");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testIndexNearest() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "INDEX NEAREST 1990-03-16T16:00:00 FROM mylist", "2");
		assertEvaluatesTo("INDEX NEAREST 1990-03-16T16:00:00 FROM (3,4)", "NULL");
		// smallest index when tied
		assertEvaluatesToWithData(data, "INDEX NEAREST 1990-03-16T16:00:00 FROM (a,mylist)", "1");
		// primary time not preserved
		assertEvaluatesToWithData(data, "TIME (INDEX NEAREST 1990-03-16T16:00:00 FROM mylist)", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testIndexOf() throws Exception {
		assertEvaluatesTo("INDEX OF \"x\" FROM (1,2,3,\"x\",\"y\")", "(,4)");
		assertEvaluatesTo("INDEX OF 2 FROM (1,2,3,\"x\",\"y\")", "(,2)");
		assertEvaluatesTo("INDEX OF 4 FROM (1,2,3,\"x\",\"y\")", "NULL");
		assertEvaluatesTo("INDEX OF NULL FROM (1,2,3,\"x\",\"y\")", "NULL");
		assertEvaluatesTo("INDEX OF 1 FROM NULL", "NULL");
		assertEvaluatesTo("INDEX OF NULL FROM NULL", "(,1)");
		assertEvaluatesTo("INDEX OF 2 FROM 2", "(,1)");
		assertEvaluatesTo("INDEX OF 2 FROM (1,2,1,2,2)", "(2,4,5)");
		assertEvaluatesTo("INDEX OF NULL FROM (1,NULL,2,NULL)", "(2,4)");
		// primary time not preserved
		assertEvaluatesToWithData(createData(), "TIME FIRST (INDEX OF 13 FROM mylist)", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testAtLeast() throws Exception {
		assertEvaluatesTo("AT LEAST 1 IsTrue FROM (FALSE, FALSE, TRUE, FALSE)", "TRUE");
		assertEvaluatesTo("AT LEAST 2 AreTrue FROM (FALSE, FALSE, TRUE, FALSE)", "FALSE");
		assertEvaluatesTo("AT LEAST 2 FROM (TRUE, FALSE, TRUE, TRUE)", "TRUE");
		assertEvaluatesTo("AT LEAST 5 FROM (TRUE, FALSE)", "FALSE");
		assertEvaluatesTo("AT LEAST 1 IsTrue FROM (TRUE, FALSE, \"TRUE\")", "NULL");
		assertEvaluatesTo("AT LEAST \"1\" FROM (TRUE, FALSE)", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testAtLeastTruthValue() throws Exception {
		// specification has "AT LEAST ... OF" in examples instead of "AT LEAST ... FROM"
		assertEvaluatesTo("(AT LEAST 2 FROM (TRUE, TRUTH VALUE 0.7, TRUTH VALUE 0.3)) IS WITHIN TRUTH VALUE 0.69 TO TRUTH VALUE 0.71", "TRUE");
		assertEvaluatesTo("AT LEAST 10 AreTrue FROM (TRUTH VALUE 0.7, TRUTH VALUE 0.1)", "FALSE");
		assertEvaluatesTo("AT LEAST \"1\" IsTrue FROM (TRUTH VALUE 0.3, FALSE)", "NULL");
		assertEvaluatesTo("AT LEAST 2 FROM (TRUE, 5, TRUTH VALUE 0.3, TRUE)", "NULL");
		assertEvaluatesTo("APPLICABILITY OF (AT LEAST 2 FROM (TRUE, 5, TRUTH VALUE 0.3, TRUE))", "TRUE");
		assertEvaluatesTo("APPLICABILITY OF (AT LEAST 2 FROM (FALSE, TRUTH VALUE 0.3, TRUE))", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testAtMost() throws Exception {
		assertEvaluatesTo("AT MOST 1 IsTrue FROM (FALSE, FALSE, TRUE, FALSE)", "TRUE");
		assertEvaluatesTo("AT MOST 2 AreTrue FROM (FALSE, FALSE, TRUE, FALSE)", "TRUE");
		assertEvaluatesTo("AT MOST 2 FROM (TRUE, FALSE, TRUE, TRUE)", "FALSE");
		assertEvaluatesTo("AT MOST 5 FROM (TRUE, FALSE)", "FALSE");
		assertEvaluatesTo("AT MOST 1 IsTrue FROM (TRUE, FALSE, \"TRUE\")", "NULL");
		assertEvaluatesTo("AT MOST \"1\" FROM (TRUE, FALSE)", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testAtMostTruthValue() throws Exception {
		// specification has "AT MOST ... OF" as examples instead of "AT MOST ... FROM"
		assertEvaluatesTo("(AT MOST 2 FROM (TRUE, FALSE, TRUTH VALUE 0.7, TRUTH VALUE 0.3)) IS WITHIN TRUTH VALUE 0.29 TO TRUTH VALUE 0.31", "TRUE");
		assertEvaluatesTo("AT MOST 10 AreTrue FROM (TRUTH VALUE 0.7, TRUTH VALUE 0.1)", "FALSE");
		assertEvaluatesTo("AT MOST \"1\" IsTrue FROM (TRUTH VALUE 0.3, FALSE)", "NULL");
		assertEvaluatesTo("AT MOST 2 FROM (TRUE, 5, TRUTH VALUE 0.3, TRUE)", "NULL");
	}

	@Test
	public void testSlope() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "SLOPE OF mylist IS WITHIN 0.99 TO 1.01", "TRUE");
		assertEvaluatesToWithData(data, "TIME (SLOPE OF mylist)", "NULL");
		assertEvaluatesTo("SLOPE (3,4)", "NULL");
		assertEvaluatesTo("SLOPE ()", "NULL");
		assertEvaluatesToWithData(data, "SLOPE OF a", "NULL");
		// same primary time -> null
		assertEvaluatesToWithData(data, "SLOPE OF (a, y)", "NULL");
		// primary time not preserved
		assertEvaluatesToWithData(data, "TIME OF (SLOPE OF mylist)", "NULL");
	}

}
