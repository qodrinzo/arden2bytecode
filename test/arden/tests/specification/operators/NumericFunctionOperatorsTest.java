package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class NumericFunctionOperatorsTest extends SpecificationTest {

	@Test
	public void testNumericFunctions() throws Exception {
		assertEvaluatesTo("ARCCOS 1", "0");
		assertEvaluatesTo("ARCCOS (-1) IS WITHIN 3.141 TO 3.142", "TRUE");
		assertEvaluatesTo("ARCSIN 0", "0");
		assertEvaluatesTo("ARCSIN .5 IS WITHIN 0.523 TO 0.524", "TRUE");
		assertEvaluatesTo("ARCTAN 0", "0");
		assertEvaluatesTo("ARCTAN .5 IS WITHIN 0.463 TO 0.464", "TRUE");
		assertEvaluatesTo("COSINE 0", "1");
		assertEvaluatesTo("COS 0", "1");
		assertEvaluatesTo("SINE 0", "0");
		assertEvaluatesTo("SIN 0", "0");
		assertEvaluatesTo("TANGENT 0", "0");
		assertEvaluatesTo("TAN 0", "0");
		assertEvaluatesTo("EXP 0", "1");
		assertEvaluatesTo("LOG 1", "0");
		assertEvaluatesTo("LOG 10 IS WITHIN 2.302 TO 2.303", "TRUE");
		assertEvaluatesTo("LOG 0", "NULL");
		assertEvaluatesTo("LOG10 100", "2");
		assertEvaluatesTo("SQRT 4", "2");
		assertEvaluatesTo("SQRT(-1)", "NULL");
		assertEvaluatesTo("ABS (-1.5)", "1.5");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testRounding() throws Exception {
		assertEvaluatesTo("INT (-1.5)", "-2");
		assertEvaluatesTo("INT (-2.0)", "-2");
		assertEvaluatesTo("INT (1.5)", "1");
		assertEvaluatesTo("FLOOR (-2.5)", "-3");
		assertEvaluatesTo("FLOOR (-3.1)", "-4");
		assertEvaluatesTo("FLOOR (-4)", "-4");
		assertEvaluatesTo("CEILING (-1.5)", "-1");
		assertEvaluatesTo("CEILING (-1.0)", "-1");
		assertEvaluatesTo("CEILING 1.5", "2");
		assertEvaluatesTo("CEILING (-2.5)", "-2");
		assertEvaluatesTo("CEILING (-3.9)", "-3");
		assertEvaluatesTo("TRUNCATE (-1.5)", "-1");
		assertEvaluatesTo("TRUNCATE (-1.0)", "-1");
		assertEvaluatesTo("TRUNCATE 1.5", "1");
		assertEvaluatesTo("ROUND 0.5", "1");
		assertEvaluatesTo("ROUND 3.4", "3");
		assertEvaluatesTo("ROUND 3.5", "4");
		assertEvaluatesTo("ROUND (-3.5)", "-4");
		assertEvaluatesTo("ROUND (-3.4)", "-3");
		assertEvaluatesTo("ROUND (-3.7)", "-4");
	}

}
