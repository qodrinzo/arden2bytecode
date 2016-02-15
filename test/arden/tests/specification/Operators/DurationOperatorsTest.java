package arden.tests.specification.Operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.SpecificationTest;

public class DurationOperatorsTest extends SpecificationTest {

	@Test
	public void testDuration() throws Exception {
		assertEvaluatesTo("2 YEARS", "24 months");
		assertEvaluatesTo("1 YEAR", "12 months");
		assertEvaluatesTo("2 MONTHS", "2 months");
		assertEvaluatesTo("1 WEEK", "604800 seconds");
		assertEvaluatesTo("1 DAY", "86400 seconds");
		assertEvaluatesTo("1 HOUR", "3600 seconds");
		assertEvaluatesTo("1 MINUTE", "60 seconds");
		assertEvaluatesTo("1 SECOND", "1 second");
	}

	@Test
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
		// date before 1800-01-01? faulty example in language specification? 
		// assertEvaluatesTo("EXTRACT MINUTE 0000-00-00", "NULL");
		assertEvaluatesTo("EXTRACT SECOND 1990-01-03T14:23:17.3", "17.3");
		assertEvaluatesTo("EXTRACT SECOND (1 second)", "NULL");
	}

}
