package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class SimpleComparisonOperatorsTest extends SpecificationTest {

	@Test
	public void testEqual() throws Exception {
		assertEvaluatesTo("1 IS EQUAL 2", "FALSE");
		assertEvaluatesTo("5 EQ NULL", "NULL");
		assertEvaluatesTo("(\"a\",\"b\",\"c\") = (\"a\",\"b\",\"x\")", "(TRUE,TRUE,FALSE)");
		assertEvaluatesTo("(1,2,\"a\") = (NULL,2,3)", "(NULL,TRUE,FALSE)");
		assertEvaluatesTo("5 EQ ()", "()");
		assertEvaluatesTo("(1,2,3) = ()", "NULL");
		assertEvaluatesTo("NULL = ()", "()");
		assertEvaluatesTo("() = ()", "()");
		assertEvaluatesTo("(1,2,3) = NULL", "(NULL,NULL,NULL)");
		assertEvaluatesTo("NULL = NULL", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testEqualTimeOfDay() throws Exception {
		assertEvaluatesTo("1990-12-10T12:20:30 = 12:20:30", "TRUE");
		assertEvaluatesTo("12:20:30 = 1990-12-10T12:20:30", "TRUE");
		assertEvaluatesTo("1990-12-10T12:20:30 = 12:20:31", "FALSE");
		assertEvaluatesTo("08:20:00 + 5 MINUTES = 08:25:00", "TRUE");
		assertEvaluatesTo("08:20:00 + 4 MINUTES = 08:25:00", "FALSE");
		assertEvaluatesTo("1990-12-10T12:20:31 = 12:20:30", "FALSE");
		assertEvaluatesTo("1990-12-10T00:00:00 = 23:59:59", "FALSE");
	}

	@Test
	public void testNotEqual() throws Exception {
		assertEvaluatesTo("1 <> 2", "TRUE");
		assertEvaluatesTo("(1,2,\"a\") NE (NULL,2,3)", "(NULL,FALSE,TRUE)");
		assertEvaluatesTo("(3/0) IS NOT EQUAL (3/0)", "NULL");

	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testNotEqualTimeOfDay() throws Exception {
		assertEvaluatesTo("1990-12-10T12:20:30 <> 12:20:30", "FALSE");
		assertEvaluatesTo("12:20:30 <> 1990-12-10T12:20:30", "FALSE");
		assertEvaluatesTo("1990-12-10T12:20:30 <> 12:20:31", "TRUE");
		assertEvaluatesTo("08:20:00 + 5 MINUTES <> 08:25:00", "FALSE");
		assertEvaluatesTo("08:20:00 + 4 MINUTES <> 08:25:00", "TRUE");
		assertEvaluatesTo("1990-12-10T12:20:31 <> 12:20:30", "TRUE");
		assertEvaluatesTo("1990-12-10T00:00:00 <> 23:59:59", "TRUE");
	}

	@Test
	public void testLess() throws Exception {
		assertEvaluatesTo("1 < 2", "TRUE");
		assertEvaluatesTo("1990-03-02T00:00:00 WAS LESS THAN 1990-03-10T00:00:00", "TRUE");
		assertEvaluatesTo("2 DAYS LT 1 YEAR", "TRUE");
		assertEvaluatesTo("\"aaa\" WERE LESS THAN \"aab\"", "TRUE");
		assertEvaluatesTo("\"a\" IS NOT GREATER THAN OR EQUAL 1", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testLessTimeOfDay() throws Exception {
		assertEvaluatesTo("1990-12-10T12:20:30 < 12:20:30", "FALSE");
		assertEvaluatesTo("12:20:30 < 1990-12-10T12:20:30", "FALSE");
		assertEvaluatesTo("1990-12-10T12:20:30 < 12:20:31", "TRUE");
		assertEvaluatesTo("08:20:00 + 5 MINUTES < 08:25:00", "FALSE");
		assertEvaluatesTo("08:20:00 + 4 MINUTES < 08:25:00", "TRUE");
		assertEvaluatesTo("1990-12-10T12:20:31 < 12:20:30", "FALSE");
		assertEvaluatesTo("1990-12-10T00:00:00 < 23:59:59", "TRUE");
	}

	@Test
	public void testLessEqual() throws Exception {
		assertEvaluatesTo("1 <= 2", "TRUE");
		assertEvaluatesTo("1990-03-02T00:00:00 WAS LESS THAN OR EQUAL 1990-03-10T00:00:00", "TRUE");
		assertEvaluatesTo("2 days LE 1 year", "TRUE");
		assertEvaluatesTo("\"aaa\" WERE LESS THAN OR EQUAL \"aab\"", "TRUE");
		assertEvaluatesTo("\"aaa\" IS NOT GREATER THAN 1", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testLessEqualTimeOfDay() throws Exception {
		assertEvaluatesTo("1990-12-10T12:20:30 <= 12:20:30", "TRUE");
		assertEvaluatesTo("12:20:30 <= 1990-12-10T12:20:30", "TRUE");
		assertEvaluatesTo("1990-12-10T12:20:30 <= 12:20:31", "TRUE");
		assertEvaluatesTo("08:20:00 + 5 MINUTES <= 08:25:00", "TRUE");
		assertEvaluatesTo("08:20:00 + 4 MINUTES <= 08:25:00", "TRUE");
		assertEvaluatesTo("1990-12-10T12:20:31 <= 12:20:30", "FALSE");
		assertEvaluatesTo("1990-12-10T00:00:00 <= 23:59:59", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testLessEqualFuzzy() throws Exception {
		String data = createCodeBuilder()
				.addData("young := FUZZY SET (0 YEARS, TRUTH VALUE 1),(15 YEARS, TRUTH VALUE 1),(20 YEARS, TRUTH VALUE 0);")
				.addData("middle_aged := FUZZY SET (15 YEARS, TRUTH VALUE 0),(20 YEARS, TRUTH VALUE 1),(60 YEARS, TRUTH VALUE 1), (70 YEARS, truth value 0);")
				.toString();
		assertEvaluatesToWithData(data, "25 YEARS <= young", "FALSE");
		assertEvaluatesToWithData(data, "25 YEARS <= middle_aged", "TRUE");
		assertEvaluatesToWithData(data, "10 YEARS <= young", "TRUE");
		assertEvaluatesToWithData(data, "10 YEARS <= middle_aged", "TRUE");
		assertEvaluatesToWithData(data, "(17.5 YEARS <= young) IS WITHIN TRUTH VALUE 0.49 TO TRUTH VALUE 0.51", "TRUE");
		assertEvaluatesToWithData(data, "17.5 YEARS <= middle_aged", "TRUE");
	}

	@Test
	public void testGreater() throws Exception {
		assertEvaluatesTo("1 > 2", "FALSE");
		assertEvaluatesTo("1990-03-02T00:00:00 WAS GREATER THAN 1990-03-10T00:00:00", "FALSE");
		// false := 1990-03-02T00:00:00 > 13:00:00
		assertEvaluatesTo("2 days GT 1 year", "FALSE");
		assertEvaluatesTo("\"aaa\" WERE GREATER THAN \"aab\"", "FALSE");
		assertEvaluatesTo("\"aaa\" IS NOT LESS THAN OR EQUAL 1", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testGreaterTimeOfDay() throws Exception {
		assertEvaluatesTo("1990-12-10T12:20:30 > 12:20:30", "FALSE");
		assertEvaluatesTo("12:20:30 > 1990-12-10T12:20:30", "FALSE");
		assertEvaluatesTo("1990-12-10T12:20:30 > 12:20:31", "FALSE");
		assertEvaluatesTo("08:20:00 + 5 MINUTES > 08:25:00", "FALSE");
		assertEvaluatesTo("08:20:00 + 4 MINUTES > 08:25:00", "FALSE");
		assertEvaluatesTo("1990-12-10T12:20:31 > 12:20:30", "TRUE");
		assertEvaluatesTo("1990-12-10T00:00:00 > 23:59:59", "FALSE");
	}

	@Test
	public void testGreaterEqual() throws Exception {
		assertEvaluatesTo("1 >= 2", "FALSE");
		assertEvaluatesTo("1990-03-02T00:00:00 WAS GREATER THAN OR EQUAL 1990-03-10T00:00:00", "FALSE");
		assertEvaluatesTo("2 days GE 1 year", "FALSE");
		assertEvaluatesTo("\"aaa\" WERE GREATER THAN OR EQUAL \"aab\"", "FALSE");
		assertEvaluatesTo("\"aaa\" IS NOT LESS THAN 1", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testGreaterEqualTimeOfDay() throws Exception {
		assertEvaluatesTo("1990-12-10T12:20:30 >= 12:20:30", "TRUE");
		assertEvaluatesTo("12:20:30 >= 1990-12-10T12:20:30", "TRUE");
		assertEvaluatesTo("1990-12-10T12:20:30 >= 12:20:31", "FALSE");
		assertEvaluatesTo("08:20:00 + 5 MINUTES >= 08:25:00", "TRUE");
		assertEvaluatesTo("08:20:00 + 4 MINUTES >= 08:25:00", "FALSE");
		assertEvaluatesTo("1990-12-10T12:20:31 >= 12:20:30", "TRUE");
		assertEvaluatesTo("1990-12-10T00:00:00 >= 23:59:59", "FALSE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testGreaterEqualFuzzy() throws Exception {
		String data = createCodeBuilder()
				.addData("young := FUZZY SET (0 YEARS, truth value 1),(15 YEARS, truth value 1),(20 YEARS, truth value 0);")
				.addData("middle_aged := FUZZY SET (15 YEARS, truth value 0),(20 YEARS, truth value 1),(60 YEARS, truth value 1), (70 YEARS, truth value 0);")
				.toString();
		assertEvaluatesToWithData(data, "25 YEARS >= young", "TRUE");
		assertEvaluatesToWithData(data, "25 YEARS >= middle_aged", "TRUE");
		assertEvaluatesToWithData(data, "10 YEARS >= young", "TRUE");
		assertEvaluatesToWithData(data, "10 YEARS >= middle_aged", "FALSE");
		assertEvaluatesToWithData(data, "(17.5 YEARS >= young) IS WITHIN TRUTH VALUE 0.49 TO TRUTH VALUE 0.51", "TRUE");
		assertEvaluatesToWithData(data, "17.5 YEARS >= middle_aged", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testTruthValueComparison() throws Exception {
		// truth value is an ordered type
		assertEvaluatesTo("TRUTH VALUE 0.3 = TRUTH VALUE 0.3", "TRUE");
		assertEvaluatesTo("TRUTH VALUE 0.3 < TRUTH VALUE 0.6", "TRUE");
		assertEvaluatesTo("TRUTH VALUE 0.3 > TRUTH VALUE 0.6", "FALSE");
		assertEvaluatesTo("FALSE < TRUTH VALUE 0.5", "TRUE");
		assertEvaluatesTo("TRUE < TRUTH VALUE 0.5", "FALSE");
		assertEvaluatesTo("FALSE > TRUTH VALUE 0.5", "FALSE");
		assertEvaluatesTo("TRUE > TRUTH VALUE 0.5", "TRUE");
	}

}
