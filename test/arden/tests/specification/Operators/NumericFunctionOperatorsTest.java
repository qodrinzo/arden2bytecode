package arden.tests.specification.Operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class NumericFunctionOperatorsTest extends SpecificationTest {

	@Test
	public void testNumericFunctions() throws Exception {
		assertEvaluatesTo("ARCCOS 1","0");
		assertEvaluatesTo("ARCCOS (-1) FORMATTED WITH \"%.2f\"", "\"3.14\"");
		assertEvaluatesTo("ARCSIN 0","0");
		assertEvaluatesTo("ARCSIN .5 FORMATTED WITH \"%.2f\"", "\"0.52\"");
		assertEvaluatesTo("ARCTAN 0","0");
		assertEvaluatesTo("ARCTAN .5 FORMATTED WITH \"%.2f\"", "\"0.46\"");
		assertEvaluatesTo("COSINE 0","1");
		assertEvaluatesTo("COS 0","1");
		assertEvaluatesTo("SINE 0","0");
		assertEvaluatesTo("SIN 0","0");
		assertEvaluatesTo("TANGENT 0","0");
		assertEvaluatesTo("TAN 0","0");
		assertEvaluatesTo("EXP 0","1");
		assertEvaluatesTo("LOG 1","0");
		assertEvaluatesTo("LOG 10 FORMATTED WITH \"%.2f\"", "\"2.30\"");
		assertEvaluatesTo("LOG 0","NULL");
		assertEvaluatesTo("LOG10 100","2");
		assertEvaluatesTo("SQRT 4","2");
		assertEvaluatesTo("SQRT(-1)","NULL");
		assertEvaluatesTo("ABS (-1.5)","1.5");
	}
	
	@Test
	public void testRounding() throws Exception {
		assertEvaluatesTo("INT (-1.5)","-2");
		assertEvaluatesTo("INT (-2.0)","-2");
		assertEvaluatesTo("INT (1.5)","1");
		assertEvaluatesTo("FLOOR (-2.5)","-3");
		assertEvaluatesTo("FLOOR (-3.1)","-4");
		assertEvaluatesTo("FLOOR (-4)","-4");
		assertEvaluatesTo("CEILING (-1.5)","-1");
		assertEvaluatesTo("CEILING (-1.0)","-1");
		assertEvaluatesTo("CEILING 1.5","2");
		assertEvaluatesTo("CEILING (-2.5)","-2");
		assertEvaluatesTo("CEILING (-3.9)","-3");
		assertEvaluatesTo("TRUNCATE (-1.5)","-1");
		assertEvaluatesTo("TRUNCATE (-1.0)","-1");
		assertEvaluatesTo("TRUNCATE 1.5","1");
		assertEvaluatesTo("ROUND 0.5","1");
		assertEvaluatesTo("ROUND 3.4","3");
		assertEvaluatesTo("ROUND 3.5","4");
		assertEvaluatesTo("ROUND (-3.5)","-4");
		assertEvaluatesTo("ROUND (-3.4)","-3");
		assertEvaluatesTo("ROUND (-3.7)","-4");
	}
	
	@Test
	public void testAsNumber() throws Exception {
		assertEvaluatesTo("\"5\" AS NUMBER","5");
		assertEvaluatesTo("\"xyz\" AS NUMBER","NULL");
		assertEvaluatesTo("TRUE AS NUMBER","1");
		assertEvaluatesTo("FALSE AS NUMBER","0");
		assertEvaluatesTo("6 AS NUMBER","6");
		assertEvaluatesTo("(\"7\", 8, \"2.3E+2\", 4.1E+3, \"ABC\", NULL, TRUE, FALSE, 1997-10-31T00:00:00, now, 3 days) AS NUMBER","(7,8,230,4100,NULL,NULL,1,0,NULL,NULL,NULL)");
		assertEvaluatesTo("() AS NUMBER","()");
		
		// primary time is preserved
		String data = new ArdenCodeBuilder().addData("x := \"5\"; TIME x := 1997-10-31T00:00:00;").toString();
		assertEvaluatesToWithData(data, "TIME (x AS NUMBER)", "1997-10-31T00:00:00");
	}
	
}
