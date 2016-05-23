package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class TransformationOperatorsTest extends SpecificationTest {
	
	private static final String DATA = new ArdenCodeBuilder()
			.addData("w := 4; TIME w := 1995-01-01T00:00:00;")
			.addData("v := 5; TIME v := 2000-01-01T00:00:00;")
			.addData("x := 5; TIME x := 1990-01-01T00:00:00;")
			.addData("y := 3; TIME y := TIME x;")
			.addData("z := 2; TIME z := 1990-01-03T00:00:00;")
			.addData("mylist := (w,v,z);")
			.toString();
	
	@Test
	public void testMinimumMaximum() throws Exception {
		assertEvaluatesTo("MINIMUM 2 FROM (11,14,13,12)","(11,12)");
		assertEvaluatesTo("MINIMUM 2 FROM (12,14,13,11)","(12,11)");
		assertEvaluatesTo("MINIMUM 2 FROM 3","(,3)");
		assertEvaluatesTo("MINIMUM 2 FROM (3, \"asdf\")","NULL");
		assertEvaluatesTo("MIN 2 FROM ()","()");
		assertEvaluatesTo("MIN 0 FROM (2,3)","()");
		assertEvaluatesTo("MIN 3 FROM (3,5,1,2,4,2)","(1,2,2)");
		assertEvaluatesTo("MIN 9 FROM (3,2,1)","(3,2,1)");
		assertEvaluatesTo("MIN \"x\" FROM (2,3)","NULL");
		assertEvaluatesTo("MIN (-3) FROM (2,3)","NULL");
		assertEvaluatesToWithData(DATA, "TIME FIRST MIN 1 FROM (x,y,z)","1990-01-03T00:00:00");
		assertEvaluatesTo("MAXIMUM 2 FROM (11,14,13,12)","(14,13)");
		assertEvaluatesTo("MAXIMUM 2 FROM (11,13,14,12)","(13,14)");
		assertEvaluatesTo("MAXIMUM 2 FROM 3","(,3)");
		assertEvaluatesTo("MAXIMUM 2 FROM (3, \"asdf\")","NULL");
		assertEvaluatesTo("MAX 2 FROM ()","()");
		assertEvaluatesTo("MAX 0 FROM (1,2,3)","()");
		assertEvaluatesTo("MAX 3 FROM (1,5,2,4,1,4)","(5,4,4)");
		
		// latest of elements with primary time on tie
		assertEvaluatesToWithData(DATA, "TIME FIRST MAX 1 FROM (x,v,5)", "2000-01-01T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME FIRST MAX 1 FROM (v,x,5)", "2000-01-01T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME FIRST MIN 1 FROM (x,v,5)", "2000-01-01T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME FIRST MIN 1 FROM (v,x,5)", "2000-01-01T00:00:00");
	}
	
	@Test
	public void testFirstLast() throws Exception {
		assertEvaluatesTo("FIRST 2 FROM (11,14,13,12)","(11,14)");
		assertEvaluatesTo("FIRST 2 FROM 3","(,3)");
		assertEvaluatesTo("FIRST 2 FROM (NULL,1,2,NULL)","(NULL,1)");
		assertEvaluatesTo("FIRST 2 FROM ()","()");
		assertEvaluatesTo("FIRST (-2) FROM 3","NULL");
		assertEvaluatesTo("LAST 2 FROM (11,14,13,12)","(13,12)");
		assertEvaluatesTo("LAST 2 FROM 3","(,3)");
		assertEvaluatesTo("LAST 2 FROM (NULL,1,2,NULL)","(2,NULL)");
		assertEvaluatesTo("LAST 2 FROM ()","()");
	}
	
	@Test
	public void testIncreaseDecrease() throws Exception {
		assertEvaluatesTo("INCREASE (11,15,13,12)","(4,-2,-1)");
		assertEvaluatesTo("INCREASE 3","()");
		assertEvaluatesTo("INCREASE ()","NULL");
		assertEvaluatesTo("INCREASE (1990-03-01,1990-03-02)","(,86400 seconds)");
		assertEvaluatesTo("INCREASE (1 day, 2 days)","(,86400 seconds)");
		assertEvaluatesTo("DECREASE (11,15,13,12)","(-4,2,1)");
		assertEvaluatesTo("DECREASE 3","()");
		assertEvaluatesTo("DECREASE ()","NULL");
		assertEvaluatesTo("DECREASE (1990-03-01,1990-03-02)","(,-86400 seconds)");
		assertEvaluatesTo("DECREASE (1 day, 2 days)","(,-86400 seconds)");
		
		// primary time of second item is kept
		assertEvaluatesToWithData(DATA, "TIME FIRST INCREASE (x,z,v)", "1990-01-03T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME FIRST INCREASE (x,v,z)", "2000-01-01T00:00:00");
	}
	
	@Test
	public void testPercent() throws Exception {
		assertEvaluatesTo("PERCENT INCREASE (11,15,13) IS WITHIN (36.36, -13.34) TO (36.37, -13.33)","(TRUE,TRUE)");
		assertEvaluatesTo("% INCREASE 3","()");
		assertEvaluatesTo("% INCREASE ()","NULL");
		assertEvaluatesTo("PERCENT INCREASE (1 day, 2 days)","(,100)");
		assertEvaluatesTo("PERCENT DECREASE (11,15,13) IS WITHIN (-36.36, 13.34) TO (-36.37, 13.33)","(TRUE,TRUE)");
		assertEvaluatesTo("% DECREASE 3","()");
		assertEvaluatesTo("% DECREASE ()","NULL");
		assertEvaluatesTo("PERCENT DECREASE (1 day, 2 days)","(,-100)");
		
		// primary time of second item is kept
		assertEvaluatesToWithData(DATA, "TIME FIRST PERCENT INCREASE (z,v)", "2000-01-01T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME FIRST PERCENT DECREASE (v,z)", "1990-01-03T00:00:00");
	}
	
	@Test
	public void testEarliestLatest() throws Exception {
		assertEvaluatesTo("EARLIEST 2 FROM ()", "()");
		assertEvaluatesToWithData(DATA, "EARLIEST 2 FROM (w,v,z)", "(4,2)");
		assertEvaluatesToWithData(DATA, "EARLIEST (-2) FROM (w,v,z)", "NULL");
		assertEvaluatesToWithData(DATA, "EARLIEST 99 FROM (w,v,z)", "(4,5,2)");
		assertEvaluatesToWithData(DATA, "EARLIEST 2 FROM (w,v,z,5)", "NULL");
		assertEvaluatesToWithData(DATA, "EARLIEST 1 FROM (y,x)", "(,3)");
		assertEvaluatesToWithData(DATA, "EARLIEST 1 FROM (x,y)", "(,5)");
		assertEvaluatesTo("LATEST 2 FROM ()", "()");
		assertEvaluatesToWithData(DATA, "LATEST 2 FROM (v,w,z)", "(5,4)");
		assertEvaluatesToWithData(DATA, "LATEST 2 FROM (w,v,z)", "(4,5)");
	}
	
	@Test
	public void testIndex() throws Exception {
		assertEvaluatesTo("INDEX MINIMUM 2 FROM (11,14,13,12)","(1,4)");
		assertEvaluatesTo("INDEX MINIMUM 3 FROM (3,5,1,2,4,2)","(3,4,6)");
		assertEvaluatesTo("INDEX MIN 2 FROM (3, \"asdf\")","null");
		assertEvaluatesTo("INDEX MINIMUM 2 FROM 3","(,1)");
		assertEvaluatesTo("INDEX MINIMUM 0 FROM (2,3)","()");
		assertEvaluatesTo("INDEX MAXIMUM 2 FROM (11,14,13,12)","(2,3)");
		assertEvaluatesTo("INDEX MAXIMUM 3 FROM (3,5,1,2,4,2)","(1,2,5)");
		assertEvaluatesTo("INDEX MAX 2 FROM (3, \"asdf\")","null");
		assertEvaluatesTo("INDEX MAXIMUM 2 FROM 3","(,1)");
		assertEvaluatesTo("INDEX MAXIMUM 0 FROM (2,3)","()");
		
		// latest of elements with primary time on tie
		assertEvaluatesToWithData(DATA, "FIRST INDEX MAXIMUM 1 FROM (v,x)", "1");
		assertEvaluatesToWithData(DATA, "FIRST INDEX MAXIMUM 1 FROM (x,v)", "2");
		
		// primary time lost
		assertEvaluatesToWithData(DATA, "TIME FIRST INDEX MAXIMUM 2 FROM (x,y,z)", "NULL");
	}
	
	@Test
	public void testIndexFirstLast() throws Exception {
		assertInvalidExpression("INDEX FIRST 2 FROM (1,2,3)");
		assertInvalidExpression("INDEX LAST 2 FROM (1,2,3)");
	}

}
