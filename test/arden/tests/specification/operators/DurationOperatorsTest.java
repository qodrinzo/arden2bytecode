package arden.tests.specification.operators;

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
	public void testDecimal() throws Exception {
		assertEvaluatesTo("0.5 YEARS", "6 months");
		assertEvaluatesTo("0.5 MINUTES", "30 seconds");
		assertEvaluatesTo("0.5 SECONDS", "0.5 seconds");
	}

}
