package arden.tests.specification.Operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.SpecificationTest;
 
public class ArithmeticOperatorsTest extends SpecificationTest {
	
	@Test
	public void testUnary() throws Exception {
		// +
		assertEvaluatesTo("+ 2", "2");
		assertEvaluatesTo("+ \"asdf\"", "NULL");
		assertEvaluatesTo("+ 2 days", "172800 seconds");
		
		// -
		assertEvaluatesTo("- 2", "-2");
		assertEvaluatesTo("- (2 days)", "-172800 seconds");
		
		assertInvalidExpression("3 + -4");
	}

	@Test
	public void testBinary() throws Exception {
		// +
		assertEvaluatesTo("4 + 2", "6");
		assertEvaluatesTo("5 + ()", "()");
		assertEvaluatesTo("(1,2,3) + ()", "NULL");
		assertEvaluatesTo("NULL + ()", "()");
		assertEvaluatesTo("5 + NULL", "NULL");
		assertEvaluatesTo("(1,2,3) + NULL", "(NULL,NULL,NULL)");
		assertEvaluatesTo("NULL + NULL", "NULL");
		assertEvaluatesTo("1 day + 2 days", "259200 seconds");
		assertEvaluatesTo("1990-03-13T00:00:00 + 2 days", "1990-03-15T00:00:00");
		// date before 1800-01-01? faulty example in language specification? 
		//assertEvaluatesTo("0000-00-00 + 1993 years + 5 months + 17 days", "1993-05-17T00:00:00");
		assertEvaluatesTo("2 days + 1990-03-13T00:00:00", "1990-03-15T00:00:00");
		
		// -
		assertEvaluatesTo("6 - 2", "4");
		assertEvaluatesTo("3 days - 2 days", "86400 seconds");
		assertEvaluatesTo("1990-03-15T00:00:00 - 2 days", "1990-03-13T00:00:00");
		assertEvaluatesTo("1990-03-15T00:00:00 - 1990-03-13T00:00:00", "172800 seconds");
		
		// *
		assertEvaluatesTo("4 * 2", "8");
		assertEvaluatesTo("3 * 2 days", "518400 seconds");
		assertEvaluatesTo("2 days * 3", "518400 seconds");
		
		// /
		assertEvaluatesTo("8 / 2", "4");
		assertEvaluatesTo("6 days / 3", "172800 seconds");
		assertEvaluatesTo("2 minutes / 1 second", "120");
		assertEvaluatesTo("3 years / 1 month", "36");
		assertEvaluatesTo("5/0", "NULL");
		
		// **
		assertEvaluatesTo("3 ** 2", "9");
	}

	@Test
	public void testInvalidOperand() throws Exception {
		assertEvaluatesTo("3**1991-03-24T00:00:00", "NULL");
	}
	
	@Test
	public void testOverflow() throws Exception {
		assertEvaluatesTo("999 ** 999", "NULL");
	}

}
