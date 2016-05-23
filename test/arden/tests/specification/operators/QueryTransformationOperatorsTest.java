package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class QueryTransformationOperatorsTest extends SpecificationTest {
	
	private static final String DATA = new ArdenCodeBuilder()
			.addData("x := 1; TIME x := 1990-03-15T15:00:00;")
			.addData("y := 2; TIME y := 1990-03-16T15:00:00;")
			.addData("z := 3; TIME z := 1990-03-18T21:00:00;")
			.addData("mylist := (x, y, z);")
			.toString();

	@Test
	public void testInterval() throws Exception {
		assertEvaluatesToWithData(DATA, "INTERVAL mylist","(86400 seconds,194400 seconds)");
		assertEvaluatesTo("INTERVAL OF (3,4)","null");
		assertEvaluatesTo("INTERVAL OF ()","null");
		
		// primary times lost
		assertEvaluatesToWithData(DATA, "THE TIME OF THE FIRST INTERVAL OF mylist","NULL");
	}

}
