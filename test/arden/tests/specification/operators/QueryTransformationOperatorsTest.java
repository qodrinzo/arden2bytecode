package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.SpecificationTest;

public class QueryTransformationOperatorsTest extends SpecificationTest {

	@Test
	public void testInterval() throws Exception {
		String data = createCodeBuilder()
				.addData("x := 1; TIME x := 1990-03-15T15:00:00;")
				.addData("y := 2; TIME y := 1990-03-16T15:00:00;")
				.addData("z := 3; TIME z := 1990-03-18T21:00:00;")
				.addData("mylist := (x, y, z);")
				.toString();

		assertEvaluatesToWithData(data, "INTERVAL mylist", "(86400 SECONDS,194400 SECONDS)");
		assertEvaluatesToWithData(data, "INTERVAL x", "()");
		assertEvaluatesTo("INTERVAL OF (3,4)", "NULL");
		assertEvaluatesTo("INTERVAL OF ()", "NULL");

		// primary times lost
		assertEvaluatesToWithData(data, "THE TIME OF THE FIRST INTERVAL OF mylist", "NULL");
	}

}
