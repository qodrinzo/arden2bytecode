package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class TransformationOperatorsTest extends SpecificationTest {

	private String createData() {
		return createCodeBuilder()
				.addData("w := 4; TIME w := 1995-01-01T00:00:00;")
				.addData("v := 5; TIME v := 2000-01-01T00:00:00;")
				.addData("x := 5; TIME x := 1990-01-01T00:00:00;")
				.addData("y := 3; TIME y := TIME x;")
				.addData("z := 2; TIME z := 1990-01-03T00:00:00;")
				.addData("mylist := (w,v,z);")
				.toString();
	}

	@Test
	public void testMinimumMaximum() throws Exception {
		String data = createData();
		assertEvaluatesTo("MINIMUM 2 FROM (11,14,13,12)", "(11,12)");
		assertEvaluatesTo("MINIMUM 2 FROM (12,14,13,11)", "(12,11)");
		assertEvaluatesTo("MINIMUM 2 FROM 3", "(,3)");
		assertEvaluatesTo("MINIMUM 2 FROM (3, \"asdf\")", "NULL");
		assertEvaluatesTo("MIN 2 FROM ()", "()");
		assertEvaluatesTo("MIN 0 FROM (2,3)", "()");
		assertEvaluatesTo("MIN 3 FROM (3,5,1,2,4,2)", "(1,2,2)");
		assertEvaluatesTo("MIN 9 FROM (3,2,1)", "(3,2,1)");
		assertEvaluatesTo("MIN \"x\" FROM (2,3)", "NULL");
		assertEvaluatesTo("MIN (-3) FROM (2,3)", "NULL");
		assertEvaluatesToWithData(data, "TIME FIRST MIN 1 FROM (x,y,z)", "1990-01-03T00:00:00");
		assertEvaluatesTo("MAXIMUM 2 FROM (11,14,13,12)", "(14,13)");
		assertEvaluatesTo("MAXIMUM 2 FROM (11,13,14,12)", "(13,14)");
		assertEvaluatesTo("MAXIMUM 2 FROM 3", "(,3)");
		assertEvaluatesTo("MAXIMUM 2 FROM (3, \"asdf\")", "NULL");
		assertEvaluatesTo("MAX 2 FROM ()", "()");
		assertEvaluatesTo("MAX 0 FROM (1,2,3)", "()");
		assertEvaluatesTo("MAX 3 FROM (1,5,2,4,1,4)", "(5,4,4)");

		// latest of elements with primary time on tie
		assertEvaluatesToWithData(data, "TIME FIRST MAX 1 FROM (x,v,5)", "2000-01-01T00:00:00");
		assertEvaluatesToWithData(data, "TIME FIRST MAX 1 FROM (v,x,5)", "2000-01-01T00:00:00");
		assertEvaluatesToWithData(data, "TIME FIRST MIN 1 FROM (x,v,5)", "2000-01-01T00:00:00");
		assertEvaluatesToWithData(data, "TIME FIRST MIN 1 FROM (v,x,5)", "2000-01-01T00:00:00");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testMinimumMaximumUsing() throws Exception {
		assertEvaluatesTo("MIN 2 FROM (0,30,90,180,200,300) USING COSINE OF IT", "(90,180)");
		assertEvaluatesTo("MIN 2 FROM (0,30,180,90,200,300) USING COSINE OF IT", "(180,90)");
		assertEvaluatesTo("MAX 2 FROM (0,30,90,180,200,300) USING SINE OF IT", "(0,90)");
		assertEvaluatesTo("MAX 2 FROM (90,30,0,180,200,300) USING SINE OF IT", "(90,0)");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testFirst() throws Exception {
		assertEvaluatesTo("FIRST 2 FROM (11,14,13,12)", "(11,14)");
		assertEvaluatesTo("FIRST 1 FROM (11,14,13,12)", "(,11)");
		assertEvaluatesTo("FIRST 2 FROM 3", "(,3)");
		assertEvaluatesTo("FIRST 2 FROM (NULL,1,2,NULL)", "(NULL,1)");
		assertEvaluatesTo("FIRST 2 FROM ()", "()");
		assertEvaluatesTo("FIRST 1 FROM ()", "()");
		assertEvaluatesTo("FIRST (-2) FROM 3", "NULL");

		// primary time preserved
		assertEvaluatesToWithData(createData(), "TIME FIRST FIRST 1 FROM mylist", "1995-01-01T00:00:00");
	}

	@Test
	@Compatibility(max = ArdenVersion.V1, pedantic = true)
	public void testFirstV1() throws Exception {
		// "FIRST ... FROM" from version 1 = "EARLIEST ... FROM" from version 2
		assertEvaluatesToWithData(createData(), "FIRST 2 FROM (w,v,z)", "(4,2)");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testLast() throws Exception {
		assertEvaluatesTo("LAST 2 FROM (11,14,13,12)", "(13,12)");
		assertEvaluatesTo("LAST 2 FROM 3", "(,3)");
		assertEvaluatesTo("LAST 2 FROM (NULL,1,2,NULL)", "(2,NULL)");
		assertEvaluatesTo("LAST 2 FROM ()", "()");

		// primary time preserved
		assertEvaluatesToWithData(createData(), "TIME FIRST LAST 1 FROM mylist", "1990-01-03T00:00:00");
	}

	@Test
	@Compatibility(max = ArdenVersion.V1, pedantic = true)
	public void testLastV1() throws Exception {
		// "LAST ... FROM" from version 1 = "LATEST ... FROM" from version 2
		assertEvaluatesToWithData(createData(), "LAST 2 FROM (v,w,z)", "(5,4)");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testSublist() throws Exception {
		assertEvaluatesTo("SUBLIST 3 ELEMENTS FROM (1,2,3,4,5)", "(1,2,3)");
		assertEvaluatesTo("SUBLIST 10 ELEMENTS FROM (1,2,3,4,5)", "(1,2,3,4,5)");
		assertEvaluatesTo("SUBLIST 2 ELEMENTS STARTING AT 2 FROM (1,2,3,4,5)", "(2,3)");
		assertEvaluatesTo("SUBLIST 10 ELEMENTS STARTING AT 2 FROM (1,2,3,4,5)", "(2,3,4,5)");
		assertEvaluatesTo("SUBLIST 2.5 ELEMENTS FROM (1,2,3,4,5)", "NULL");
		assertEvaluatesTo("SUBLIST 2 ELEMENTS STARTING AT 2.5 FROM (1,2,3,4,5)", "NULL");
		assertEvaluatesTo("SUBLIST 2 ELEMENTS STARTING AT 10 FROM (1,2,3,4,5)", "()");
		assertEvaluatesTo("SUBLIST 1 ELEMENTS STARTING AT 3 FROM (1,2,3,4,5)", "(,3)");
		assertEvaluatesTo("SUBLIST (-1) ELEMENTS STARTING AT 3 FROM (1,2,3,4,5)", "(,3)");
		assertEvaluatesTo("SUBLIST (-3) ELEMENTS STARTING AT 3 FROM (1,2,3,4,5)", "(1,2,3)");

		// primary time preserved
		assertEvaluatesToWithData(createData(), "TIME FIRST (SUBLIST 1 ELEMENTS FROM w)", "1995-01-01T00:00:00");
	}

	@Test
	public void testIncreaseDecrease() throws Exception {
		assertEvaluatesTo("INCREASE (11,15,13,12)", "(4,-2,-1)");
		assertEvaluatesTo("INCREASE 3", "()");
		assertEvaluatesTo("INCREASE ()", "NULL");
		assertEvaluatesTo("INCREASE (1990-03-01,1990-03-02)", "(,86400 seconds)");
		assertEvaluatesTo("INCREASE (1 day, 2 days)", "(,86400 seconds)");
		assertEvaluatesTo("DECREASE (11,15,13,12)", "(-4,2,1)");
		assertEvaluatesTo("DECREASE 3", "()");
		assertEvaluatesTo("DECREASE ()", "NULL");
		assertEvaluatesTo("DECREASE (1990-03-01,1990-03-02)", "(,-86400 seconds)");
		assertEvaluatesTo("DECREASE (1 day, 2 days)", "(,-86400 seconds)");

		// primary time of second item is kept
		String data = createData();
		assertEvaluatesToWithData(data, "TIME FIRST INCREASE (x,z,v)", "1990-01-03T00:00:00");
		assertEvaluatesToWithData(data, "TIME FIRST INCREASE (x,v,z)", "2000-01-01T00:00:00");
	}

	@Test
	public void testPercent() throws Exception {
		assertEvaluatesTo("PERCENT INCREASE (11,15,13) IS WITHIN (36.36, -13.34) TO (36.37, -13.33)", "(TRUE,TRUE)");
		assertEvaluatesTo("% INCREASE 3", "()");
		assertEvaluatesTo("% INCREASE ()", "NULL");
		assertEvaluatesTo("PERCENT INCREASE (1 day, 2 days)", "(,100)");
		assertEvaluatesTo("PERCENT DECREASE (11,15,13) IS WITHIN (-36.37, 13.33) TO (-36.36, 13.34)", "(TRUE,TRUE)");
		assertEvaluatesTo("% DECREASE 3", "()");
		assertEvaluatesTo("% DECREASE ()", "NULL");
		assertEvaluatesTo("PERCENT DECREASE (1 day, 2 days)", "(,-100)");

		// primary time of second item is kept
		String data = createData();
		assertEvaluatesToWithData(data, "TIME FIRST PERCENT INCREASE (z,v)", "2000-01-01T00:00:00");
		assertEvaluatesToWithData(data, "TIME FIRST PERCENT DECREASE (v,z)", "1990-01-03T00:00:00");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testEarliestLatest() throws Exception {
		String data = createData();
		assertEvaluatesTo("EARLIEST 2 FROM ()", "()");
		assertEvaluatesToWithData(data, "EARLIEST 2 FROM (w,v,z)", "(4,2)");
		assertEvaluatesToWithData(data, "EARLIEST (-2) FROM (w,v,z)", "NULL");
		assertEvaluatesToWithData(data, "EARLIEST 99 FROM (w,v,z)", "(4,5,2)");
		assertEvaluatesToWithData(data, "EARLIEST 2 FROM (w,v,z,5)", "NULL");
		assertEvaluatesToWithData(data, "EARLIEST 1 FROM (y,x)", "(,3)");
		assertEvaluatesToWithData(data, "EARLIEST 1 FROM (x,y)", "(,5)");
		assertEvaluatesTo("LATEST 2 FROM ()", "()");
		assertEvaluatesToWithData(data, "LATEST 2 FROM (v,w,z)", "(5,4)");
		assertEvaluatesToWithData(data, "LATEST 2 FROM (w,v,z)", "(4,5)");

		// primary time preserved
		assertEvaluatesToWithData(data, "TIME FIRST EARLIEST 1 FROM mylist", "1990-01-03T00:00:00");
		assertEvaluatesToWithData(data, "TIME FIRST LATEST 1 FROM mylist", "2000-01-01T00:00:00");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testEarliestLatestUsing() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "EARLIEST 2 FROM (w,v,z) USING TIME OF IT", "(4,2)");
		assertEvaluatesToWithData(data, "LATEST 2 FROM (v,w,z) USING TIME OF IT", "(5,4)");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testIndex() throws Exception {
		assertEvaluatesTo("INDEX MINIMUM 2 FROM (11,14,13,12)", "(1,4)");
		assertEvaluatesTo("INDEX MINIMUM 3 FROM (3,5,1,2,4,2)", "(3,4,6)");
		assertEvaluatesTo("INDEX MIN 2 FROM (3, \"asdf\")", "null");
		assertEvaluatesTo("INDEX MINIMUM 2 FROM 3", "(,1)");
		assertEvaluatesTo("INDEX MINIMUM 0 FROM (2,3)", "()");
		assertEvaluatesTo("INDEX MAXIMUM 2 FROM (11,14,13,12)", "(2,3)");
		assertEvaluatesTo("INDEX MAXIMUM 3 FROM (3,5,1,2,4,2)", "(1,2,5)");
		assertEvaluatesTo("INDEX MAX 2 FROM (3, \"asdf\")", "null");
		assertEvaluatesTo("INDEX MAXIMUM 2 FROM 3", "(,1)");
		assertEvaluatesTo("INDEX MAXIMUM 0 FROM (2,3)", "()");

		// latest of elements with primary time on tie
		String data = createData();
		assertEvaluatesToWithData(data, "FIRST INDEX MAXIMUM 1 FROM (v,x)", "1");
		assertEvaluatesToWithData(data, "FIRST INDEX MAXIMUM 1 FROM (x,v)", "2");

		// primary time lost
		assertEvaluatesToWithData(data, "TIME FIRST INDEX MAXIMUM 2 FROM (x,y,z)", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testIndexFirstLast() throws Exception {
		assertInvalidExpression("INDEX FIRST 2 FROM (1,2,3)");
		assertInvalidExpression("INDEX LAST 2 FROM (1,2,3)");
	}

}
