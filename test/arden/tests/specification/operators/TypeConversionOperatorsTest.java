package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class TypeConversionOperatorsTest extends SpecificationTest {

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testAsNumber() throws Exception {
		assertEvaluatesTo("\"5\" AS NUMBER", "5");
		assertEvaluatesTo("\"xyz\" AS NUMBER", "NULL");
		// TODO truth value as number?
		assertEvaluatesTo("TRUE AS NUMBER", "1");
		assertEvaluatesTo("FALSE AS NUMBER", "0");
		assertEvaluatesTo("6 AS NUMBER", "6");
		assertEvaluatesTo("(\"7\", 8, \"2.3E+2\", 4.1E+3, \"ABC\", NULL, TRUE, FALSE, 1997-10-31T00:00:00, now, 3 days) AS NUMBER", "(7,8,230,4100,NULL,NULL,1,0,NULL,NULL,NULL)");
		assertEvaluatesTo("() AS NUMBER", "()");

		// primary time is preserved
		String data = createCodeBuilder()
				.addData("x := \"5\"; TIME x := 1997-10-31T00:00:00;")
				.toString();
		assertEvaluatesToWithData(data, "TIME (x AS NUMBER)", "1997-10-31T00:00:00");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testAsTime() throws Exception {
		assertEvaluatesTo("\"1999-12-12\" AS TIME", "1999-12-12T00:00:00");
		assertEvaluatesTo("\"1997-10-31T12:34:56\" AS TIME", "1997-10-31T12:34:56");
		assertEvaluatesTo("\"1990-01-01T12:00:00.15\" AS TIME", "1990-01-01T12:00:00.15");
		assertEvaluatesTo("1999-12-12 AS TIME", "1999-12-12T00:00:00");
		assertEvaluatesTo("\"xyz\" AS TIME", "NULL");
		assertEvaluatesTo("() AS TIME", "()");

		// primary time is preserved
		String data = createCodeBuilder()
				.addData("t := \"2000-10-31T00:00:00\"; TIME t := 1997-10-31T00:00:00;")
				.toString();
		assertEvaluatesToWithData(data, "TIME (x AS TIME)", "1997-10-31T00:00:00");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testAsString() throws Exception {
		assertEvaluatesTo("5 AS STRING", "\"5\"");
		assertEvaluatesTo("NULL AS STRING", "\"null\"");
		assertEvaluatesTo("TRUE AS STRING", "\"true\"");
		assertEvaluatesTo("FALSE AS STRING", "\"false\"");
		assertEvaluatesTo("\"7\" AS STRING", "\"7\"");
		assertEvaluatesTo("4.1E+3 AS STRING", "\"4100\"");
		assertEvaluatesTo("3 days AS STRING", "\"3 days\"");
		assertEvaluatesTo("1997-10-31T00:00:00 AS STRING", "\"1997-10-31T00:00:00\"");
		assertEvaluatesTo("() AS STRING", "()");

		// primary time is preserved
		String data = createCodeBuilder()
				.addData("x := 5; TIME x := 1997-10-31T00:00:00;")
				.toString();
		assertEvaluatesToWithData(data, "TIME (x AS STRING)", "1997-10-31T00:00:00");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testAsTruthValue() throws Exception {
		assertEvaluatesTo("(0.33 AS TRUTH VALUE) IS WITHIN TRUTH VALUE 0.32 TO TRUTH VALUE 0.34", "TRUE");
		assertEvaluatesTo("\"xyz\" AS TRUTH VALUE", "NULL");
		assertEvaluatesTo("400 AS TRUTH VALUE", "NULL");
		assertEvaluatesTo("TRUE AS TRUTH VALUE", "TRUE");
		assertEvaluatesTo("FALSE AS TRUTH VALUE", "FALSE");
		assertEvaluatesTo("0 AS TRUTH VALUE", "FALSE");
		assertEvaluatesTo("1 AS TRUTH VALUE", "TRUE");
		assertEvaluatesTo("() AS TRUTH VALUE", "()");

		// primary time is preserved
		String data = createCodeBuilder()
				.addData("x := 0.8; TIME x := 1997-10-31T00:00:00;")
				.toString();
		assertEvaluatesToWithData(data, "TIME (x AS TRUTH VALUE)", "1997-10-31T00:00:00");
	}
}
