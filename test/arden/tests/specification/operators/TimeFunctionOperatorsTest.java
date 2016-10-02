package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class TimeFunctionOperatorsTest extends SpecificationTest {

	@Test
	public void testTimeAssignment() throws Exception {
		assertValidStatement("x := 1; TIME x := 1990-03-15T15:00:00;");
		assertValidStatement("x := \"x\"; TIME OF x := 1990-03-15T15:00:00;");
	}

	@Test
	public void testTime() throws Exception {
		String data = createCodeBuilder()
				.addData("x := 1; TIME x := 1990-03-15T15:00:00;")
				.addData("y := 2; TIME y := 1990-03-16T15:00:00;")
				.addData("z := 3; TIME z := 1990-03-18T21:00:00;")
				.addData("mylist := (x, y, z);")
				.toString();
		assertEvaluatesToWithData(data, "TIME FIRST mylist", "1990-03-15T15:00:00");
		assertEvaluatesToWithData(data, "TIME OF TIME OF FIRST mylist", "1990-03-15T15:00:00");
		assertEvaluatesTo("TIME OF (3,4)", "(NULL,NULL)");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_5)
	public void testTimeObjects() throws Exception {
		String data = createCodeBuilder()
				.addData("obj := OBJECT [attr1, attr2];")
				.addData("x := 1; TIME x := 2004-01-16T00:00:00;")
				.addData("y := 2.0; TIME y := 2004-01-17T00:00:00;")
				.toString();

		String noAttrs = new ArdenCodeBuilder(data)
				.addData("result := NEW obj;")
				.addAction("RETURN TIME OF result;")
				.toString();
		assertReturns(noAttrs, "NULL");

		String attrTime = new ArdenCodeBuilder(data)
				.addData("result := NEW obj WITH x, x;")
				.addAction("RETURN TIME OF result.attr1;")
				.toString();
		assertReturns(attrTime, "2004-01-16T00:00:00");

		String sameAttrTimes = new ArdenCodeBuilder(data)
				.addData("result := NEW obj WITH x, x;")
				.addAction("RETURN TIME OF result;")
				.toString();
		assertReturns(sameAttrTimes, "2004-01-16T00:00:00");

		String differentAttrTimes = new ArdenCodeBuilder(data)
				.addData("result := NEW obj WITH x, y;")
				.addAction("RETURN TIME OF result;")
				.toString();
		assertReturns(differentAttrTimes, "NULL");

		String listAttr = new ArdenCodeBuilder(data)
				.addData("result := NEW obj WITH x, (x,x);")
				.addAction("RETURN TIME OF result;")
				.toString();
		assertReturns(listAttr, "NULL");

		/*
		 * Is "TIME OF result.value := ..." valid? It is given as an example in
		 * the specification, but the the grammar rule <time_becomes> only
		 * allows <identifier> not <identifier_or_object_ref>. Also "value" is a
		 * reserved word.
		 */
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6, max = ArdenVersion.V2_6, pedantic = true)
	public void testAt() throws Exception {
		assertEvaluatesTo("1980-01-01 AT 15:00:00", "1980-01-01T15:00:00");
		assertEvaluatesTo("1980-01-01T00:00:30 AT 15:00", "1980-01-01T15:00:00");
		assertEvaluatesTo("(2010-12-20T12:34:56 + 5 MINUTES) AT 00:00:00", "2010-12-20T00:00:00");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_7)
	public void testAtTime() throws Exception {
		assertEvaluatesTo("1980-01-01 ATTIME 15:00:00", "1980-01-01T15:00:00");
		assertEvaluatesTo("1980-01-01T00:00:30 ATTIME 15:00", "1980-01-01T15:00:00");
		assertEvaluatesTo("(2010-12-20T12:34:56 + 5 MINUTES) ATTIME 00:00:00", "2010-12-20T00:00:00");
	}

}
