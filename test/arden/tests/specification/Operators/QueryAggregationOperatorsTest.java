package arden.tests.specification.Operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class QueryAggregationOperatorsTest extends SpecificationTest {
	
	private static final String DATA = new ArdenCodeBuilder()
			.addData("x := 12; TIME x := 1990-03-15T15:00:00;")
			.addData("y := 13; TIME y := 1990-03-16T15:00:00;")
			.addData("z := 14; TIME z := 1990-03-17T15:00:00;")
			.addData("mylist := (x, y, z);")
			.addData("a := 4; TIME a := 1990-03-16T15:00:00;")
			.toString();

	@Test
	public void testNearest() throws Exception {
		assertEvaluatesToWithData(DATA, "NEAREST (2 days before 1990-03-18T16:00:00) FROM mylist","13");
		assertEvaluatesToWithData(DATA, "NEAREST 1990-03-16T16:00:00 FROM a","4");
		assertEvaluatesToWithData(DATA, "NEAREST 1990-03-16T16:00:00 FROM (a, 5)","NULL");
		// element with smallest index when tied 
		assertEvaluatesToWithData(DATA, "NEAREST 1990-03-16T16:00:00 FROM (a,mylist)","4");
		assertEvaluatesTo("NEAREST 1990-03-16T16:00:00 FROM (3,4)","NULL");
		assertEvaluatesTo("NEAREST 1990-03-16T16:00:00 FROM ()","NULL");
	}

	@Test
	public void testIndexNearest() throws Exception {
		assertEvaluatesToWithData(DATA, "INDEX NEAREST 1990-03-16T16:00:00 FROM mylist","2");
		assertEvaluatesTo("INDEX NEAREST 1990-03-16T16:00:00 FROM (3,4)","NULL");
		// smallest index when tied
		assertEvaluatesToWithData(DATA, "INDEX NEAREST 1990-03-16T16:00:00 FROM (a,mylist)","1");
	}

	@Test
	public void testSlope() throws Exception {
		assertEvaluatesToWithData(DATA, "SLOPE OF mylist IS WITHIN 0.99 TO 1.01","TRUE");
		assertEvaluatesToWithData(DATA, "TIME (SLOPE OF mylist)","NULL");
		assertEvaluatesTo("SLOPE (3,4)","NULL");
		assertEvaluatesTo("SLOPE ()", "NULL");
		assertEvaluatesToWithData(DATA, "SLOPE OF a","NULL");
		// same primary time -> null
		assertEvaluatesToWithData(DATA, "SLOPE OF (a, y)","NULL");
	}

}
