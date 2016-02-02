package arden.tests.specification.Operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class AggregationOperatorsTest extends SpecificationTest {
	
	private static final String DATA = new ArdenCodeBuilder()
			.addData("u := 5; TIME u := 1995-01-01T00:00:00;")
			.addData("v := 5; TIME v := 2000-01-01T00:00:00;")
			.addData("w := 5; TIME w := TIME v;")
			.addData("x := 5; TIME x := 1990-01-01T00:00:00;")
			.addData("y := 3; TIME y := TIME x;")
			.addData("z := 2; TIME z := 1990-01-03T00:00:00;")
			.toString();
			
	@Test
	public void testCount() throws Exception {
		assertEvaluatesTo("COUNT(12,13,14,NULL)", "4");
		assertEvaluatesTo("COUNT OF \"asdf\"", "1");
		assertEvaluatesTo("COUNT OF()", "0");
		assertEvaluatesTo("COUNT NULL", "1");
		assertEvaluatesToWithData(DATA, "TIME COUNT (x,y)", "NULL");
	}

	@Test
	public void testExists() throws Exception {
		assertEvaluatesTo("EXIST (12,13,14)", "TRUE");
		assertEvaluatesTo("EXIST OF NULL", "FALSE");
		assertEvaluatesTo("EXIST ()", "FALSE");
		assertEvaluatesTo("EXISTS (\"plugh\",NULL)", "TRUE");
		assertEvaluatesToWithData(DATA, "TIME EXIST (x,y)", "1990-01-01T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME EXIST (x,z)", "NULL");
	}

	@Test
	public void testAverage() throws Exception {
		assertEvaluatesTo("AVERAGE (12,13,17)", "14");
		assertEvaluatesTo("AVG 3", "3");
		assertEvaluatesTo("AVG OF ()", "NULL");
		assertEvaluatesTo("AVERAGE OF (1990-03-10T03:10:00, 1990-03-12T03:10:00)", "1990-03-11T03:10:00");
		assertEvaluatesTo("AVERAGE (2 days, 3 days, 4 days) = 3 days", "TRUE");
		assertEvaluatesToWithData(DATA, "TIME AVERAGE (x,y)", "1990-01-01T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME AVERAGE (x,z)", "NULL");
	}

	@Test
	public void testMedian() throws Exception {
		assertEvaluatesTo("MEDIAN (12,17,13)", "13");
		assertEvaluatesTo("MEDIAN 3", "3");
		assertEvaluatesTo("MEDIAN ()", "NULL");
		assertEvaluatesTo("MEDIAN (1990-03-10T03:10:00, 1990-03-11T03:10:00, 1990-03-28T03:10:00)", "1990-03-11T03:10:00");
		assertEvaluatesTo("MEDIAN (1 hour, 3 days, 4 years) = 3 days", "TRUE");
		assertEvaluatesTo("MEDIAN OF (0,5)", "2.5");
		assertEvaluatesToWithData(DATA, "TIME MEDIAN (x,y,z)", "1990-01-01T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME MEDIAN (x,y)", "1990-01-01T00:00:00");
		
		// latest of elements with primary time on tie
		assertEvaluatesToWithData(DATA, "TIME MEDIAN (x,u,v)", "2000-01-01T00:00:00");
		// tie -> average of middle 2 elements with latest time -> if same keep primary
		assertEvaluatesToWithData(DATA, "TIME MEDIAN (v,x,w,u)", "2000-01-01T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME MEDIAN (x,z)", "NULL");
	}

	@Test
	public void testSum() throws Exception {
		assertEvaluatesTo("SUM (12,13,14)","39");
		assertEvaluatesTo("SUM 3","3");
		assertEvaluatesTo("SUM ()","0");
		assertEvaluatesTo("SUM (1 day, 6 days) = 7 days","TRUE");
		assertEvaluatesToWithData(DATA, "TIME SUM (x,y)","1990-01-01T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME SUM (x,z)","NULL");
	}

	@Test
	public void testStddev() throws Exception {
		assertEvaluatesTo("STDDEV (12,13,14,15,16) FORMATTED WITH \"%.3f\"","\"1.581\"");
		assertEvaluatesTo("STDDEV 3","NULL");
		assertEvaluatesTo("STDDEV ()","NULL");
		assertEvaluatesToWithData(DATA, "TIME STDDEV (x,y)","1990-01-01T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME STDDEV (x,z)","NULL");
	}

	@Test
	public void testVariance() throws Exception {
		assertEvaluatesTo("VARIANCE (12,13,14,15,16)","2.5");
		assertEvaluatesTo("VARIANCE 3","NULL");
		assertEvaluatesTo("VARIANCE ()","NULL");
		assertEvaluatesToWithData(DATA, "TIME VARIANCE (x,y)","1990-01-01T00:00:00");
		assertEvaluatesToWithData(DATA, "TIME VARIANCE (x,z)","NULL");
	}

	@Test
	public void testMinMax() throws Exception {
		assertEvaluatesTo("MINIMUM (12,13,14)","12");
		assertEvaluatesTo("MIN 3","3");
		assertEvaluatesTo("MINIMUM ()","NULL");
		assertEvaluatesTo("MINIMUM (1,\"abc\")","NULL");
		assertEvaluatesToWithData(DATA, "TIME MINIMUM (x,y,z)","1990-01-03T00:00:00");
		assertEvaluatesTo("MAXIMUM (12,13,14)","14");
		assertEvaluatesTo("MAX 3","3");
		assertEvaluatesTo("MAXIMUM ()","NULL");
		assertEvaluatesTo("MAXIMUM (1,\"abc\")","NULL");
		// latest time of tied elements
		assertEvaluatesToWithData(DATA, "TIME MAXIMUM (v,x,y)","2000-01-01T00:00:00");
	}

	@Test
	public void testLastFirst() throws Exception {
		assertEvaluatesTo("LAST (12,13,14)","14");
		assertEvaluatesTo("LAST 3","3");
		assertEvaluatesTo("LAST ()","NULL");
		assertEvaluatesTo("FIRST (12,13,14)","12");
		assertEvaluatesTo("FIRST 3","3");
		assertEvaluatesTo("FIRST ()","NULL");
		assertEvaluatesToWithData(DATA, "TIME LAST (x,y,z)","1990-01-03T00:00:00");
	}

	@Test
	public void testAny() throws Exception {
		assertEvaluatesTo("ANY (TRUE,FALSE,FALSE)","TRUE");
		assertEvaluatesTo("ANY FALSE","FALSE");
		assertEvaluatesTo("ANY ()","FALSE");
		assertEvaluatesTo("ANY (3, 5, \"red\")","NULL");
		assertEvaluatesTo("ANY (FALSE, FALSE)","FALSE");
		assertEvaluatesTo("ANY (FALSE, NULL)","NULL");
	}

	@Test
	public void testAll() throws Exception {
		assertEvaluatesTo("ALL (TRUE,FALSE,FALSE)","FALSE");
		assertEvaluatesTo("ALL FALSE","FALSE");
		assertEvaluatesTo("ALL ()","TRUE");
		assertEvaluatesTo("ALL (3, 5, \"red\")","NULL");
		assertEvaluatesTo("ALL (TRUE, NULL)","NULL");
	}

	@Test
	public void testNo() throws Exception {
		assertEvaluatesTo("NO (TRUE,FALSE,FALSE)","FALSE");
		assertEvaluatesTo("NO FALSE","TRUE");
		assertEvaluatesTo("NO ()","TRUE");
		assertEvaluatesTo("NO (3, 5, \"red\")","NULL");
		assertEvaluatesTo("NO (FALSE, NULL)","NULL");
	}

	@Test
	public void testLatestEarliest() throws Exception {
		assertEvaluatesTo("LATEST ()","NULL");
		assertEvaluatesToWithData(DATA, "LATEST (v,x,z)","5");
		assertEvaluatesTo("EARLIEST ()","NULL");
		assertEvaluatesToWithData(DATA, "EARLIEST (v,z)","2");
	}

	@Test
	public void testElement() throws Exception {
		assertEvaluatesTo("(10,20,30,40)[2]","20");
		assertEvaluatesTo("(10,20)[()]","()");
		assertEvaluatesTo("(10,20)[1.5,2]","(NULL,20)");
		assertEvaluatesTo("(10,20,30,40,50)[1,3,5]","(10,30,50)");
		assertEvaluatesTo("(10,20,30,40,50)[1,(3,5)]","(10,30,50)");
		assertEvaluatesTo("(10,20,30,40,50)[1 SEQTO 3]","(10,20,30)");
		assertEvaluatesToWithData(DATA, "TIME FIRST (x,y,z)[2,3]","1990-01-01T00:00:00");
	}

	@Test
	public void testExtractCharacters() throws Exception {
		assertEvaluatesTo("EXTRACT CHARACTERS \"abc\"","(\"a\",\"b\",\"c\")");
		assertEvaluatesTo("EXTRACT CHARACTERS (\"ab\",\"c\")","(\"a\",\"b\",\"c\")");
		assertEvaluatesTo("EXTRACT CHARACTERS ()","()");
		assertEvaluatesTo("EXTRACT CHARACTERS \"\"","()");
		assertEvaluatesTo("STRING REVERSE EXTRACT CHARACTERS \"abcde\"","\"edcba\"");
	}

	@Test
	public void testSeqto() throws Exception {
		assertEvaluatesTo("2 SEQTO 4","(2,3,4)");
		assertEvaluatesTo("4 SEQTO 2","()");
		assertEvaluatesTo("4.5 SEQTO 2","NULL");
		// written as (2) instead of (,2) in specification, faulty example?
		assertEvaluatesTo("2 SEQTO 2","(,2)");
		assertEvaluatesTo("-3 SEQTO -1","(-3,-2,-1)");
		assertEvaluatesTo("2 * (1 SEQTO 4)","(2,4,6,8)");
		assertEvaluatesTo("(1.5 SEQTO 5)","NULL");
		
	}

	@Test
	public void testReverse() throws Exception {
		assertEvaluatesTo("REVERSE (1,2,3)","(3,2,1)");
		assertEvaluatesTo("REVERSE (1 SEQTO 6)","(6,5,4,3,2,1)");
		assertEvaluatesTo("REVERSE ()","()");
	}

	@Test
	public void testIndexExtraction() throws Exception {
		assertEvaluatesTo("INDEX EARLIEST ()","NULL");
		assertEvaluatesToWithData(DATA, "INDEX EARLIEST (v,x,y)","2");
		assertEvaluatesToWithData(DATA, "TIME INDEX EARLIEST (v,x,y)","1990-01-01T00:00:00");
		assertEvaluatesTo("INDEX LATEST ()","NULL");
		assertEvaluatesToWithData(DATA, "INDEX LATEST (v,x,y)","1");
		assertEvaluatesTo("INDEX MINIMUM (12,13,14)","1");
		assertEvaluatesTo("INDEX MIN 3","1");
		assertEvaluatesTo("INDEX MINIMUM ()","NULL");
		assertEvaluatesTo("INDEX MINIMUM (1,\"abc\")","NULL");
		assertEvaluatesTo("INDEX MAXIMUM (12,13,14)","3");
		assertEvaluatesTo("INDEX MAX 3","1");
		assertEvaluatesTo("INDEX MAXIMUM ()","NULL");
		assertEvaluatesTo("INDEX MAXIMUM (1,\"abc\")","NULL");
	}

	@Test
	public void testIndexFirstLast() throws Exception {
		assertInvalidExpression("INDEX FIRST (12,13,14)");
		assertInvalidExpression("INDEX LAST (12,13,14)");
	}

}
