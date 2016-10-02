package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class FuzzyOperatorsTest extends SpecificationTest {

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testFuzzySet() throws Exception {
		assertEvaluatesTo("(FUZZY SET (1, TRUTH VALUE 0), (\"2\", TRUTH VALUE 1)) IS PRESENT", "FALSE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testFuzzified() throws Exception {
		assertEvaluatesTo("(7 FUZZIFIED BY \"x\") IS PRESENT", "FALSE");
		assertEvaluatesTo("(7 FUZZIFIED BY 2) IS PRESENT", "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testDefuzzified() throws Exception {
		assertEvaluatesTo("DEFUZZIFIED 7 FUZZIFIED BY 2;", "7");

		String p0 = "(0, TRUTH VALUE 0), ";
		String p2 = "(2, TRUTH VALUE 0), ";
		String p3 = "(3, TRUTH VALUE 0.5), ";
		String p4 = "(4, TRUTH VALUE 0.5), ";
		String p5 = "(5, TRUTH VALUE 1), ";
		String p6 = "(6, TRUTH VALUE 1), ";
		String p7 = "(7, TRUTH VALUE 0)";
		String meanOfMaximum = createCodeBuilder()
				.addData("s := FUZZY SET " + p0 + p2 + p3 + p4 + p5 + p6 + p7 + ";")
				.addAction("RETURN (DEFUZZIFIED s) IS WITHIN TRUTH VALUE 5.49 TO TRUTH VALUE 5.51;")
				.toString();
		assertReturns(meanOfMaximum, "TRUE");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testApplicability() throws Exception {
		assertEvaluatesTo("APPLICABILITY \"xyz\"", "TRUE");
		assertEvaluatesTo("APPLICABILITY APPLICABILITY \"xyz\"", "TRUE");
		assertEvaluatesTo("NOT APPLICABILITY \"xyz\"", "FALSE");

		String data = createCodeBuilder()
				.addData("x := 5; APPLICABILITY OF x := TRUTH VALUE 0.3; TIME OF x := 1990-01-01T10:00:00;")
				.addData("y := 3; APPLICABILITY OF y := TRUE;")
				.toString();
		assertEvaluatesToWithData(data, "(APPLICABILITY x) IS WITHIN TRUTH VALUE 0.29 TO TRUTH VALUE 0.31", "TRUE");
		assertEvaluatesToWithData(data, "(APPLICABILITY APPLICABILITY x) IS WITHIN TRUTH VALUE 0.29 TO TRUTH VALUE 0.31", "TRUE");
		assertEvaluatesToWithData(data, "APPLICABILITY OF y", "TRUE");
		assertEvaluatesToWithData(data, "APPLICABILITY OF APPLICABILITY OF y", "TRUE");

		// primary time preserved
		assertEvaluatesToWithData(data, "TIME APPLICABILITY x", "1990-01-01T10:00:00");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testApplicabilityObjects() throws Exception {
		String data = createCodeBuilder()
				.addData("obj := OBJECT [attr1, attr2];")
				.addData("empty_obj := OBJECT [];")
				.addData("x := 1; APPLICABILITY x := TRUTH VALUE 0.4;")
				.addData("y := 2.0; APPLICABILITY y := TRUE;")
				.toString();

		/*
		 * Applicability of objects returns NULL sometimes, but the
		 * specification also says "Since null is not allowed as degree of
		 * applicability, a value is always returned".
		 */

		String noAttrs = new ArdenCodeBuilder(data)
				.addData("result := NEW empty_obj;")
				.addAction("RETURN APPLICABILITY OF result;")
				.toString();
		assertReturns(noAttrs, "NULL");

		String attrTime = new ArdenCodeBuilder(data)
				.addData("result := NEW obj WITH x, x;")
				.addAction("RETURN (APPLICABILITY OF result.attr1) IS WITHIN TRUTH VALUE 0.39 TO TRUTH VALUE 0.41;")
				.toString();
		assertReturns(attrTime, "TRUE");

		String sameAttrTimes = new ArdenCodeBuilder(data)
				.addData("result := NEW obj WITH x, x;")
				.addAction("RETURN (APPLICABILITY OF result) IS WITHIN TRUTH VALUE 0.39 TO TRUTH VALUE 0.41;")
				.toString();
		assertReturns(sameAttrTimes, "TRUE");

		String differentAttrTimes = new ArdenCodeBuilder(data)
				.addData("result := NEW obj WITH x, y;")
				.addAction("RETURN APPLICABILITY OF result;")
				.toString();
		assertReturns(differentAttrTimes, "NULL");

		String listAttr = new ArdenCodeBuilder(data)
				.addData("result := NEW obj WITH x, (x,x);")
				.addAction("RETURN APPLICABILITY OF result;")
				.toString();
		assertReturns(listAttr, "NULL");

		/*
		 * Is "APPLICABILITY OF result.value := ..." valid? It is given as an
		 * example in the specification, but the the grammar rule
		 * <<applicability_becomes>> only allows <identifier> not
		 * <identifier_or_object_ref>. Also "value" is a reserved word.
		 */
	}

}
