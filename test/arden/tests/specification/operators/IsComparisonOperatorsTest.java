package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class IsComparisonOperatorsTest extends SpecificationTest {

	@Test
	public void testWithinTo() throws Exception {
		assertEvaluatesTo("3 IS WITHIN 2 TO 5", "TRUE");
		assertEvaluatesTo("1990-03-10T00:00:00 IS WITHIN 1990-03-05T00:00:00 TO 1990-03-15T00:00:00", "TRUE");
		assertEvaluatesTo("3 DAYS WERE WITHIN 2 DAYS TO 5 MONTHS", "TRUE");
		assertEvaluatesTo("\"ccc\" WAS WITHIN \"a\" TO \"d\"", "TRUE");
		assertEvaluatesTo("(1,2) IS WITHIN (0,2) TO (3,4)", "(TRUE,TRUE)");
		assertEvaluatesTo("(1,2) IS WITHIN 2 TO (3,4)", "(FALSE,TRUE)");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testWithinToTimeOfDay() throws Exception {
		assertEvaluatesTo("2000-06-06T13:00:00 IS WITHIN 12:30:00 TO 14:00:00", "TRUE");
		assertEvaluatesTo("2000-06-06T13:00:00 IS WITHIN 13:30:00 TO 22:00:00", "FALSE");
		assertEvaluatesTo("2000-06-06T01:00:00 IS WITHIN 22:00:00 TO 02:00:00", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testWithinToDayOfWeek() throws Exception {
		assertEvaluatesTo("WEDNESDAY IS WITHIN MONDAY TO FRIDAY", "TRUE");
		assertEvaluatesTo("WEDNESDAY IS WITHIN FRIDAY TO SUNDAY", "FALSE");
		assertEvaluatesTo("SUNDAY IS WITHIN SATURDAY TO MONDAY", "FALSE");
	}

	@Test
	public void testWithin() throws Exception {
		assertEvaluatesTo("1990-03-08T00:00:00 WERE WITHIN 3 days PRECEDING 1990-03-10T00:00:00", "TRUE");
		assertEvaluatesTo("1990-03-08T00:00:00 IS WITHIN 3 days FOLLOWING 1990-03-10T00:00:00", "FALSE");
		assertEvaluatesTo("1990-03-08T00:00:00 IS WITHIN 3 days SURROUNDING 1990-03-10T00:00:00", "TRUE");
		assertEvaluatesTo("1990-03-08T00:00:00 WAS WITHIN PAST 3 days", "FALSE");
		assertEvaluatesTo("1990-03-08T11:11:11 IS WITHIN SAME DAY AS 1990-03-08T01:01:01", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testWithinTimeOfDay() throws Exception {
		assertEvaluatesTo("1990-03-10T15:00:00 IS WITHIN 5 HOURS PRECEDING 17:00:00", "TRUE");
		assertEvaluatesTo("1990-03-10T15:00:00 IS WITHIN 1 HOUR PRECEDING 17:00:00", "FALSE");
		assertEvaluatesTo("1990-03-10T15:00:00 IS WITHIN 2 HOURS FOLLOWING 14:00:00", "TRUE");
		assertEvaluatesTo("1990-03-10T15:00:00 IS WITHIN 30 MINUTES FOLLOWING 14:00:00", "FALSE");
		assertEvaluatesTo("1990-03-10T15:00:00 IS WITHIN 2 HOURS SURROUNDING 16:00:00", "TRUE");
		assertEvaluatesTo("1990-03-10T15:00:00 IS WITHIN 2 HOURS SURROUNDING 14:00:00", "TRUE");
		assertEvaluatesTo("1990-03-10T15:00:00 IS WITHIN 1 HOUR SURROUNDING 13:00:00", "FALSE");
		assertEvaluatesTo("1990-03-10T15:00:00 IS WITHIN SAME DAY AS 15:00:00", "NULL");
		assertEvaluatesTo("15:00:00 IS WITHIN SAME DAY AS 1990-03-10T15:00:00", "NULL");
		assertEvaluatesTo("16:00:00 IS WITHIN PAST 5 DAYS", "NULL");
	}

	@Test
	public void testBeforeAfter() throws Exception {
		assertEvaluatesTo("1990-03-08T00:00:00 IS BEFORE 1990-03-07T00:00:00", "FALSE");
		assertEvaluatesTo("1990-03-08T00:00:00 IS AFTER 1990-03-07T00:00:00", "TRUE");
		assertEvaluatesTo("1990-03-08T00:00:00 IS BEFORE 1990-03-08T00:00:01", "TRUE");
		assertEvaluatesTo("1990-03-08T00:00:02 IS BEFORE 1990-03-08T00:00:01", "FALSE");

		// not inclusive
		assertEvaluatesTo("1990-03-08T00:50:00 IS BEFORE 1990-03-08T00:50:00", "FALSE");
		assertEvaluatesTo("1990-03-08T00:50:00 IS AFTER 1990-03-08T00:50:00", "FALSE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testBeforeAfterTimeOfDay() throws Exception {
		assertEvaluatesTo("1990-03-08T05:00:00 IS AFTER 18:00:00", "FALSE");
		assertEvaluatesTo("1990-03-08T05:00:00 IS BEFORE 18:00:00", "TRUE");
		assertEvaluatesTo("1990-03-08T20:00:00 IS AFTER 18:00:00", "TRUE");
		assertEvaluatesTo("1990-03-08T20:00:00 IS BEFORE 18:00:00", "FALSE");

		// not inclusive
		assertEvaluatesTo("1990-03-08T18:00:00 IS BEFORE 18:00:00", "FALSE");
		assertEvaluatesTo("1990-03-08T18:00:00 IS BEFORE 18:00:00", "FALSE");
	}

	@Test
	public void testIsIn() throws Exception {
		assertEvaluatesTo("2 IS IN (3,2,6)", "TRUE");
		assertEvaluatesTo("2 IS IN (4,5,6)", "FALSE");
		assertEvaluatesTo("(3,4) IS IN (4,5,6)", "(FALSE,TRUE)");
		assertEvaluatesTo("(1,2,3) IS IN (0,3)", "(FALSE,FALSE,TRUE)");
		assertEvaluatesTo("NULL IS IN (1/0,2)", "TRUE");
		assertEvaluatesTo("2 IS NOT IN (4,5,6)", "TRUE");
		assertEvaluatesTo("2 IS IN (4,5,6)", "FALSE");

		String data = createCodeBuilder()
				.addData("x := 5; TIME x := 1990-01-01T00:00:00;")
				.addData("y := 3; TIME y := 1990-01-02T00:00:00;")
				.addData("z := 2; TIME z := 1990-01-03T00:00:00;")
				.toString();
		assertEvaluatesToWithData(data, "TIME FIRST ((x,y) IS IN (x,y,z))", "1990-01-01T00:00:00");
		assertEvaluatesToWithData(data, "TIME FIRST ((5,y) IS IN (x,y,z))", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testIsInFuzzy() throws Exception {
		String data = createCodeBuilder()
				.addData("middle_aged := FUZZY SET (15, truth value 0),(20, truth value 1),(60, truth value 1), (70, truth value 0);")
				.addData("cook_time := 5 MINUTES FUZZIFIED BY 1 MINUTE;")
				.toString();
		assertEvaluatesToWithData(data, "40 IS IN middle_aged", "TRUE");
		assertEvaluatesToWithData(data, "10 IN middle_aged", "FALSE");
		assertEvaluatesToWithData(data, "(17.5 IS IN middle_aged) IS WITHIN TRUTH VALUE 0.49 TO TRUTH VALUE 0.51", "TRUE");
		assertEvaluatesToWithData(data, "5 MINUTES IS IN cook_time", "TRUE");
		assertEvaluatesToWithData(data, "(5.5 MINUTES IS IN cook_time) IS WITHIN TRUTH VALUE 0.49 TO TRUTH VALUE 0.51", "TRUE");
		assertEvaluatesToWithData(data, "2 HOURS IS IN cook_time", "FALSE");
		assertEvaluatesToWithData(data, "(4 IS IN 5 FUZZIFIED BY 2) IS WITHIN TRUTH VALUE 0.49 TO TRUTH VALUE 0.51", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_1)
	public void testIn() throws Exception {
		assertEvaluatesTo("2 IN (3,2,6)", "TRUE");
		assertEvaluatesTo("2 IN (4,5,6)", "FALSE");
		assertEvaluatesTo("2 NOT IN (4,5,6)", "TRUE");
		assertEvaluatesTo("NULL IN (1/0,2)", "TRUE");
	}

	@Test
	public void testPresentNull() throws Exception {
		assertEvaluatesTo("3 IS PRESENT", "TRUE");
		assertEvaluatesTo("NULL IS PRESENT", "FALSE");
		assertEvaluatesTo("(3,NULL) IS NOT NULL", "(TRUE,FALSE)");
		assertEvaluatesTo("(3,NULL) IS NULL", "(FALSE,TRUE)");
	}

	@Test
	public void testType() throws Exception {
		assertEvaluatesTo("FALSE IS BOOLEAN", "TRUE");
		assertEvaluatesTo("FALSE IS NOT BOOLEAN", "FALSE");
		assertEvaluatesTo("NULL IS BOOLEAN", "FALSE");
		assertEvaluatesTo("(NULL, FALSE, \"asdf\") IS BOOLEAN", "(FALSE,TRUE,FALSE)");
		assertEvaluatesTo("3 IS NUMBER", "TRUE");
		assertEvaluatesTo("\"abc\" IS STRING", "TRUE");
		assertEvaluatesTo("1991-03-12T00:00:00 IS TIME", "TRUE");
		assertEvaluatesTo("(3 DAYS) IS DURATION", "TRUE");
		assertEvaluatesTo("(1, 2, 3) IS LIST", "TRUE");
		assertEvaluatesTo("(,3) IS LIST", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testTypeTruthValue() throws Exception {
		assertEvaluatesTo("TRUTH VALUE 1 IS BOOLEAN", "TRUE");
		assertEvaluatesTo("TRUTH VALUE 0 IS BOOLEAN", "TRUE");
		assertEvaluatesTo("TRUTH VALUE 0.5 IS BOOLEAN", "FALSE");
		assertEvaluatesTo("TRUTH VALUE 0.5 IS TRUTH VALUE", "TRUE");
		assertEvaluatesTo("TRUTH VALUE 0.5 IS NOT TRUTH VALUE", "FALSE");
		assertEvaluatesTo("3 IS TRUTH VALUE", "FALSE");
		assertEvaluatesTo("TRUTH VALUE 1 IS TRUTH VALUE", "TRUE");
		assertEvaluatesTo("TRUE IS TRUTH VALUE", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testTypeTimeOfDay() throws Exception {
		assertEvaluatesTo("20:00:00 IS TIME", "FALSE");
		assertEvaluatesTo("20:00:00 IS TIME OF DAY", "TRUE");
		assertEvaluatesTo("10:30:12.123 IS TIME OF DAY", "TRUE");
		assertEvaluatesTo("1990-01-01T20:00:00 IS TIME OF DAY", "FALSE");
		assertEvaluatesTo("NULL IS TIME OF DAY", "FALSE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testTypeLinguisticVariable() throws Exception {
		String data = createCodeBuilder()
				.addData("RangeOfAge := LINGUISTIC VARIABLE [young, middleAge, old];")
				.addData("Age := new RangeOfAge;")
				.addData("Age.young := FUZZY SET (0 YEARS, TRUE), (25 YEARS, TRUE),(35 YEARS, FALSE);")
				.addData("Age.middleAge := FUZZY SET (25 YEARS, FALSE), (35 YEARS, TRUE), (55 YEARS, TRUE), (65 YEARS, FALSE);")
				.addData("Age.old := FUZZY SET (55 YEARS, FALSE), (65 YEARS, TRUE);")
				.toString();
		assertEvaluatesToWithData(data, "RangeOfAge IS LINGUISTIC VARIABLE", "TRUE");
		assertEvaluatesTo("3 IS LINGUISTIC VARIABLE", "FALSE");
		assertEvaluatesTo("3 IS NOT LINGUISTIC VARIABLE", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_5)
	public void testTypeObject() throws Exception {
		String pixelObject = createCodeBuilder()
				.addData("Pixel := OBJECT [x, y];")
				.addData("Image := OBJECT [pixel_list];")
				.addData("p := NEW Pixel;")
				.toString();
		assertEvaluatesToWithData(pixelObject, "p IS OBJECT", "TRUE");
		assertEvaluatesToWithData(pixelObject, "p IS Pixel", "TRUE");
		assertEvaluatesToWithData(pixelObject, "p IS Image", "FALSE");
		assertEvaluatesToWithData(pixelObject, "3 IS Image", "FALSE");
		assertEvaluatesTo("3 IS OBJECT", "FALSE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testTypeFuzzy() throws Exception {
		assertEvaluatesTo("3 IS FUZZY", "FALSE");
		assertEvaluatesTo("3 IS NOT FUZZY", "TRUE");
		assertEvaluatesTo("TRUTH VALUE 0.5 IS FUZZY", "FALSE");
		assertEvaluatesTo("(FUZZY SET (0, TRUTH VALUE 0), (1, TRUTH VALUE 1)) IS FUZZY", "TRUE");
		assertEvaluatesTo("(2000-01-01T00:00:00 FUZZIFIED BY 2 days) IS FUZZY", "TRUE");
		assertEvaluatesTo("(1 WEEK FUZZIFIED BY 2 days) IS FUZZY", "TRUE");
		assertEvaluatesTo("(1 WEEK FUZZIFIED BY 2 days) IS NOT FUZZY", "FALSE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testTypeCrisp() throws Exception {
		assertEvaluatesTo("3 IS CRISP", "TRUE");
		assertEvaluatesTo("3 IS NOT CRISP", "FALSE");
		assertEvaluatesTo("TRUTH VALUE 0.5 IS CRISP", "TRUE");
		assertEvaluatesTo("(FUZZY SET (0, TRUTH VALUE 0), (1, TRUTH VALUE 1)) IS CRISP", "FALSE");
		assertEvaluatesTo("(2000-01-01T00:00:00 FUZZIFIED BY 2 days) IS CRISP", "FALSE");
		assertEvaluatesTo("(1 WEEK FUZZIFIED BY 2 days) IS CRISP", "FALSE");
		assertEvaluatesTo("(1 WEEK FUZZIFIED BY 2 days) IS NOT CRISP", "TRUE");
	}

}
