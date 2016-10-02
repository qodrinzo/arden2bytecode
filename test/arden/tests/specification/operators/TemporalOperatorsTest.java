package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class TemporalOperatorsTest extends SpecificationTest {

	@Test
	public void testAfter() throws Exception {
		assertEvaluatesTo("2 DAYS AFTER 1990-03-13T00:00:00", "1990-03-15T00:00:00");
	}

	@Test
	public void testBefore() throws Exception {
		assertEvaluatesTo("2 DAYS BEFORE 1990-03-13T00:00:00", "1990-03-11T00:00:00");
	}

	@Test
	public void testAgo() throws Exception {
		assertEvaluatesTo("2 DAYS AGO = NOW - 2 days", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_1)
	public void testFrom() throws Exception {
		assertEvaluatesTo("2 DAYS FROM 2000-09-11T00:08:00", "2000-09-13T00:08:00");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testTimeOfDay() throws Exception {
		assertEvaluatesTo("TIME OF DAY OF 1990-01-01T12:30:30", "12:30:30");
		assertEvaluatesTo("TIME OF DAY OF 1990-01-01T12:30:30.123", "12:30:30.123");
		assertEvaluatesTo("TIME OF DAY OF 5", "NULL");

		String data = createCodeBuilder()
				.addData("t := 1990-01-01T12:30:30;")
				.addData("TIME t := 1980-01-01T00:00:00;")
				.toString();
		assertEvaluatesToWithData(data, "TIME OF DAY OF t", "12:30:30");
		assertEvaluatesToWithData(data, "TIME OF (TIME OF DAY OF t)", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testDayOfWeek() throws Exception {
		assertEvaluatesTo("DAY OF WEEK OF 2006-05-26T13:20:00", "5");
		assertEvaluatesTo("DAY OF WEEK OF (2006-06-03T09:04:00, 2006-06-06T16:40:00)", "(6,2)");
		assertEvaluatesTo("DAY OF WEEK OF 08:30:00", "NULL");
		assertEvaluatesTo("DAY OF WEEK OF 2006-05-26T13:20:00 = FRIDAY", "TRUE");
		assertEvaluatesTo("DAY OF WEEK OF (2006-06-03T09:04:00, 2006-06-06T16:40:00) IS IN (SATURDAY, SUNDAY)", "(TRUE,FALSE)");

		String data = createCodeBuilder()
				.addData("t := 1990-01-01T12:30:30;")
				.addData("TIME T := 1970-01-01T00:00:00;")
				.toString();
		assertEvaluatesToWithData(data, "DAY OF WEEK OF TIME OF t", "4");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testExtract() throws Exception {
		assertEvaluatesTo("EXTRACT YEAR 1990-01-03T14:23:17.3", "1990");
		assertEvaluatesTo("EXTRACT YEAR (1 YEAR)", "NULL");
		assertEvaluatesTo("EXTRACT MONTH 1990-01-03T14:23:17.3", "1");
		assertEvaluatesTo("EXTRACT MONTH 1", "NULL");
		assertEvaluatesTo("EXTRACT DAY 1990-01-03T14:23:17.3", "3");
		assertEvaluatesTo("EXTRACT DAY \"this is not a time\"", "NULL");
		assertEvaluatesTo("EXTRACT HOUR 1990-01-03T14:23:17.3", "14");
		assertEvaluatesTo("EXTRACT HOUR (1 HOUR)", "NULL");
		assertEvaluatesTo("EXTRACT MINUTE 1990-01-03T14:23:17.3", "23");
		assertEvaluatesTo("EXTRACT MINUTE 1990-01-03", "0");
		// date before 1800-01-01? faulty example in specification?
		// assertEvaluatesTo("EXTRACT MINUTE 0000-00-00", "NULL");
		assertEvaluatesTo("EXTRACT SECOND 1990-01-03T14:23:17.3", "17.3");
		assertEvaluatesTo("EXTRACT SECOND (1 second)", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testExtractTimeOfDay() throws Exception {
		assertEvaluatesTo("EXTRACT YEAR 14:23:17.3", "NULL");
		assertEvaluatesTo("EXTRACT MONTH 14:23:17.3", "NULL");
		assertEvaluatesTo("EXTRACT DAY 14:23:17.3", "NULL");
		assertEvaluatesTo("EXTRACT HOUR 14:23:17.3", "14");
		assertEvaluatesTo("EXTRACT MINUTE 14:23:17.3", "23");
		assertEvaluatesTo("EXTRACT SECOND 14:23:17.3", "17.3");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testReplaceWith() throws Exception {
		String data = createCodeBuilder()
				.addData("d := 1990-01-03T14:23:17.3;")
				.addData("TIME OF d := 1980-01-01T00:00:00;")
				.addData("d2 := (1980-12-01T00:00:00,1970-01-01T00:00:00);")
				.addData("t := 20:00:00;")
				.addData("TIME OF t := 1980-01-01T00:00:00;")
				.toString();

		// year
		assertEvaluatesToWithData(data, "REPLACE YEAR d WITH 2000", "2000-01-03T14:23:17.3");
		assertEvaluatesToWithData(data, "REPLACE YEAR OF d WITH (2000, 1980)", "(2000-01-03T14:23:17.3,1980-01-03T14:23:17.3)");
		assertEvaluatesToWithData(data, "REPLACE YEAR d WITH 2000.123", "2000-01-03T14:23:17.3");
		assertEvaluatesToWithData(data, "REPLACE YEAR OF d WITH 1799", "NULL");
		assertEvaluatesToWithData(data, "REPLACE YEAR OF d WITH NULL", "NULL");
		assertEvaluatesToWithData(data, "REPLACE YEAR OF d WITH 5", "NULL");
		assertEvaluatesToWithData(data, "TIME OF REPLACE YEAR OF d WITH 2000", "1980-01-01T00:00:00");
		assertEvaluatesToWithData(data, "REPLACE YEAR OF d2 WITH 2000", "(2000-12-01T00:00:00,2000-01-01T00:00:00)");
		assertEvaluatesToWithData(data, "REPLACE YEAR OF d2 WITH (1990,2000)", "(1990-12-01T00:00:00,2000-01-01T00:00:00)");
		assertEvaluatesToWithData(data, "REPLACE YEAR OF d2 WITH (1990,2000,2010)", "NULL");
		assertEvaluatesToWithData(data, "REPLACE YEAR OF t WITH 1990", "NULL");

		// month
		assertEvaluatesToWithData(data, "REPLACE MONTH OF d WITH 5", "1990-05-03T14:23:17.3");
		assertEvaluatesToWithData(data, "REPLACE MONTH OF d WITH 12", "1990-12-03T14:23:17.3");
		assertEvaluatesToWithData(data, "REPLACE MONTH OF d WITH 14", "NULL");
		assertEvaluatesToWithData(data, "REPLACE MONTH OF d WITH (-2)", "NULL");
		assertEvaluatesToWithData(data, "REPLACE MONTH OF d WITH \"5\"", "NULL");
		assertEvaluatesToWithData(data, "REPLACE MONTH OF d WITH 0.7", "NULL");
		assertEvaluatesToWithData(data, "REPLACE MONTH OF d WITH 2.7", "1990-02-03T14:23:17.3;");
		assertEvaluatesToWithData(data, "TIME OF REPLACE MONTH OF d WITH 3", "1980-01-01T00:00:00");

		// day
		assertEvaluatesToWithData(data, "REPLACE DAY OF d WITH 25", "1990-01-25T14:23:17.3");
		assertEvaluatesToWithData(data, "TIME OF REPLACE DAY OF d WITH 25", "1980-01-01T00:00:00");
		assertEvaluatesToWithData(data, "REPLACE DAY OF d WITH 50", "NULL");
		assertEvaluatesToWithData(data, "REPLACE DAY OF d WITH 0.3", "NULL");
		assertEvaluatesToWithData(data, "REPLACE DAY OF d WITH 0.3", "NULL");
		assertEvaluatesToWithData(data, "REPLACE DAY OF 2000-02-01T00:00:00 WITH 30", "NULL");

		// hour
		assertEvaluatesToWithData(data, "REPLACE HOUR OF d WITH 0", "1990-01-03T00:23:17.3");
		assertEvaluatesToWithData(data, "REPLACE HOUR OF d WITH 23", "1990-01-03T23:23:17.3");
		assertEvaluatesToWithData(data, "REPLACE HOUR OF d WITH 24", "NULL");
		assertEvaluatesToWithData(data, "REPLACE HOUR OF d WITH 6.6", "1990-01-03T00:06:17.3");
		assertEvaluatesToWithData(data, "REPLACE HOUR OF t WITH 12", "12:00:00");
		assertEvaluatesToWithData(data, "TIME OF REPLACE HOUR OF t WITH 12", "1980-01-01T00:00:00");

		// minute
		assertEvaluatesToWithData(data, "REPLACE MINUTE OF d WITH 0", "1990-01-03T14:00:17.3");
		assertEvaluatesToWithData(data, "REPLACE MINUTE OF d WITH 59", "1990-01-03T14:59:17.3");
		assertEvaluatesToWithData(data, "REPLACE MINUTE OF d WITH 60", "NULL");
		assertEvaluatesToWithData(data, "REPLACE MINUTE OF t WITH 23", "20:23:00");
		assertEvaluatesToWithData(data, "REPLACE MINUTE OF t WITH 23.7", "20:23:00");
		assertEvaluatesToWithData(data, "TIME OF REPLACE MINUTE OF t WITH 12", "1980-01-01T00:00:00");

		// second
		assertEvaluatesToWithData(data, "REPLACE SECOND OF d WITH 0", "1990-01-03T14:23:00");
		assertEvaluatesToWithData(data, "REPLACE SECOND OF d WITH 12.35", "1990-01-03T14:23:12.35");
		assertEvaluatesToWithData(data, "REPLACE SECOND OF t WITH 60", "NULL");
		assertEvaluatesToWithData(data, "REPLACE SECOND OF t WITH 1", "20:00:01");
		assertEvaluatesToWithData(data, "REPLACE SECOND OF t WITH 12.7", "20:00:12.7");
	}

}
