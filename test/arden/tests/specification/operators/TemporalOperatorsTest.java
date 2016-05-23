package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.SpecificationTest;

public class TemporalOperatorsTest extends SpecificationTest {
	
	@Test
	public void testAfter() throws Exception {
		assertEvaluatesTo("2 DAYS AFTER 1990-03-13T00:00:00", "1990-03-15T00:00:00");
	}
	
	@Test
	public void testFrom() throws Exception {
		assertEvaluatesTo("2 DAYS FROM 2000-09-11T00:08:00", "2000-09-13T00:08:00");
	}
	
	@Test
	public void testBefore() throws Exception {
		assertEvaluatesTo("2 DAYS BEFORE 1990-03-13T00:00:00", "1990-03-11T00:00:00");
	}

	@Test
	public void testAgo() throws Exception {
		assertEvaluatesTo("2 DAYS AGO = NOW - 2 days", "TRUE");
	}
	
}
