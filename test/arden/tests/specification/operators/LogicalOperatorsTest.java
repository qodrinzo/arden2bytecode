package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.SpecificationTest;

public class LogicalOperatorsTest extends SpecificationTest {

	@Test
	public void testOr() throws Exception {
		assertEvaluatesTo("TRUE OR FALSE", "TRUE");
		assertEvaluatesTo("FALSE OR FALSE", "FALSE");
		assertEvaluatesTo("TRUE OR NULL", "TRUE");
		assertEvaluatesTo("FALSE OR NULL", "NULL");
		assertEvaluatesTo("FALSE OR 5", "NULL");
		assertEvaluatesTo("(TRUE,FALSE) OR (FALSE,TRUE)", "(TRUE,TRUE)");
		assertEvaluatesTo("() OR ()", "()");
	}

	@Test
	public void testAnd() throws Exception {
		assertEvaluatesTo("TRUE AND FALSE", "FALSE");
		assertEvaluatesTo("TRUE AND TRUE", "TRUE");
		assertEvaluatesTo("TRUE AND NULL", "NULL");
		assertEvaluatesTo("FALSE AND NULL", "FALSE");
	}

	@Test
	public void testNot() throws Exception {
		assertEvaluatesTo("NOT FALSE", "TRUE");
		assertEvaluatesTo("NOT NULL", "NULL");
	}

}
