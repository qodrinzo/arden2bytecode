package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.SpecificationTest;
import arden.tests.specification.testcompiler.TestCompilerCompiletimeException;

public class WhereOperatorTest extends SpecificationTest {

	@Test
	public void testListCondition() throws Exception {
		assertEvaluatesTo("(1,2,3,4,5) WHERE (TRUE,FALSE,TRUE,NULL,5)", "(1,3)");
		assertEvaluatesTo("1 WHERE TRUE", "1");
		assertEvaluatesTo("1 WHERE FALSE", "()");
		assertEvaluatesTo("(1,2,3) WHERE TRUE", "(1,2,3)");
		assertEvaluatesTo("1 WHERE (TRUE,FALSE,TRUE)", "(1,1)");
		
		String data = createCodeBuilder()
				.addData("x := 5; TIME x := 1990-01-01T00:00:00;")
				.addData("y := 3; TIME y := 1990-01-02T00:00:00;")
				.addData("z := 2; TIME z := 1990-01-03T00:00:00;")
				.toString();
		assertEvaluatesToWithData(data, "TIME (x WHERE TRUE)", "1990-01-01T00:00:00");
		assertEvaluatesToWithData(data, "TIME FIRST ((x,y,z) WHERE (FALSE, TRUE, TRUE))", "1990-01-02T00:00:00");
	}

	@Test
	public void testItemCondition() throws Exception {
		String data = createCodeBuilder()
				.addData("mylist := (1,2,\"a\");")
				.toString();
		assertEvaluatesToWithData(data, "mylist WHERE mylist <= 2", "(1,2)");
		
		assertEvaluatesTo("(1,2,\"a\") WHERE IT IS NUMBER", "(1,2)");
		assertEvaluatesTo("(1,2,\"a\") WHERE THEY ARE NUMBER", "(1,2)");
		assertEvaluatesTo("(1,2,3,4) WHERE (COUNT ((1,2,3,\"x\") WHERE IT IS NUMBER)) <= IT", "(3,4)");
	}
	
	@Test
	public void testInvalidIt() throws Exception {
		// IT outside of WHERE -> error at compile time or NULL
		String itOutsideOfWhere = createCodeBuilder().addAction("RETURN IT").toString();
		try {
			getCompiler().compile(itOutsideOfWhere);
			// no error at compile time -> must return NULL
			assertReturns(itOutsideOfWhere, "NULL");
		} catch(TestCompilerCompiletimeException e) {
			// error at compile time -> test passed
		}
	}

}
