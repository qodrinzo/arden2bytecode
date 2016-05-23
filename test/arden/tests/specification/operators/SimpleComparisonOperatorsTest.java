package arden.tests.specification.operators;

import org.junit.Test;

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
	public void testNotEqual() throws Exception {
		assertEvaluatesTo("1 <> 2", "TRUE");
		assertEvaluatesTo("(1,2,\"a\") NE (NULL,2,3)", "(NULL,FALSE,TRUE)");
		assertEvaluatesTo("(3/0) IS NOT EQUAL (3/0)", "NULL");
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
	public void testLessEqual() throws Exception {
		assertEvaluatesTo("1 <= 2", "TRUE");
		assertEvaluatesTo("1990-03-02T00:00:00 WAS LESS THAN OR EQUAL 1990-03-10T00:00:00", "TRUE");
		assertEvaluatesTo("2 days LE 1 year", "TRUE");
		assertEvaluatesTo("\"aaa\" WERE LESS THAN OR EQUAL \"aab\"", "TRUE");
		assertEvaluatesTo("\"aaa\" IS NOT GREATER THAN 1", "NULL");
	}

	@Test
	public void testGreater() throws Exception {
		assertEvaluatesTo("1 > 2", "FALSE");
		assertEvaluatesTo("1990-03-02T00:00:00 WAS GREATER THAN 1990-03-10T00:00:00", "FALSE");
		assertEvaluatesTo("2 days GT 1 year", "FALSE");
		assertEvaluatesTo("\"aaa\" WERE GREATER THAN \"aab\"", "FALSE");
		assertEvaluatesTo("\"aaa\" IS NOT LESS THAN OR EQUAL 1", "NULL");
	}

	@Test
	public void testGreaterEqual() throws Exception {
		assertEvaluatesTo("1 >= 2", "FALSE");
		assertEvaluatesTo("1990-03-02T00:00:00 WAS GREATER THAN OR EQUAL 1990-03-10T00:00:00", "FALSE");
		assertEvaluatesTo("2 days GE 1 year", "FALSE");
		assertEvaluatesTo("\"aaa\" WERE GREATER THAN OR EQUAL \"aab\"", "FALSE");
		assertEvaluatesTo("\"aaa\" IS NOT LESS THAN 1", "NULL");
	}

}
