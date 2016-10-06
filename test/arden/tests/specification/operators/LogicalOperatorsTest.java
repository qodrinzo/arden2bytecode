package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.ArdenVersion;
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
		assertEvaluatesTo("NULL AND FALSE", "FALSE");
	}

	@Test
	public void testNot() throws Exception {
		assertEvaluatesTo("NOT FALSE", "TRUE");
		assertEvaluatesTo("NOT 5", "NULL");
		assertEvaluatesTo("NOT NULL", "NULL");
		assertEvaluatesTo("NOT (FALSE, TRUE)", "(TRUE,FALSE)");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testOrTruthValue() throws Exception {
		assertEvaluatesTo("TRUTH VALUE 0.1 OR TRUTH VALUE 0.7", "truth value 0.7");
		assertEvaluatesTo("TRUTH VALUE 0.7 OR TRUTH VALUE 0.1", "truth value 0.7");
		assertEvaluatesTo("TRUTH VALUE 0.1 OR TRUTH VALUE 1", "TRUE");
		assertEvaluatesTo("FALSE OR TRUTH VALUE 0.3", "truth value 0.3");
		assertEvaluatesTo("TRUE OR TRUTH VALUE 0.3", "TRUE");
		assertEvaluatesTo("FALSE OR TRUTH VALUE 0", "FALSE");
		assertEvaluatesTo("TRUTH VALUE 0.5 OR 5", "NULL");
		assertEvaluatesTo("TRUTH VALUE 0.5 OR NULL", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testAndTruthValue() throws Exception {
		assertEvaluatesTo("TRUTH VALUE 0.1 AND TRUTH VALUE 0.7", "truth value 0.1");
		assertEvaluatesTo("TRUTH VALUE 0.7 AND TRUTH VALUE 0.1", "truth value 0.1");
		assertEvaluatesTo("TRUTH VALUE 0.1 AND TRUTH VALUE 1", "truth value 0.1");
		assertEvaluatesTo("FALSE AND TRUTH VALUE 0.3", "FALSE");
		assertEvaluatesTo("TRUE AND TRUTH VALUE 0.3", "truth value 0.3");
		assertEvaluatesTo("FALSE AND TRUTH VALUE 0", "FALSE");
		assertEvaluatesTo("TRUTH VALUE 0.5 AND 5", "NULL");
		assertEvaluatesTo("TRUTH VALUE 0.5 AND NULL", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testNotTruthValue() throws Exception {
		assertEvaluatesTo("NOT TRUTH VALUE 0.4", "TRUTH VALUE 0.6");
		assertEvaluatesTo("NOT TRUTH VALUE 0.6", "TRUTH VALUE 0.4");
		assertEvaluatesTo("NOT TRUTH VALUE 1", "FALSE");
		assertEvaluatesTo("NOT TRUTH VALUE 0", "TRUE");
	}

}
