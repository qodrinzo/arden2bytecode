package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.SpecificationTest;

public class ListOperatorsTest extends SpecificationTest {

	private String createData() {
		return createCodeBuilder()
				.addData("data1 := 3; TIME data1 := 1991-01-02T00:00:00;")
				.addData("x := 1; TIME x := 1991-01-01T00:00:00;")
				.addData("y := 2; TIME y := 1991-01-03T00:00:00;")
				.addData("data2 := (x,y);")
				.toString();
	}

	@Test
	public void testConcatenation() throws Exception {
		assertEvaluatesTo("(1,2), 3", "(1,2,3)");
	}

	@Test
	public void testSingleElementList() throws Exception {
		assertEvaluatesTo(", 5", "(,5)");
		assertEvaluatesTo(",NULL", "(,NULL)");
	}

	@Test
	public void testMerge() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "data1 MERGE data2", "(1,3,2)");
		assertEvaluatesToWithData(data, "(1,2) MERGE (3,4)", "NULL");
		assertEvaluatesToWithData(data, "(data1, 4) MERGE data2", "NULL");
		assertEvaluatesToWithData(data, "(data1 MERGE data2) = (SORT TIME (data1,data2))", "(TRUE,TRUE,TRUE)");
	}

	@Test
	public void testSortData() throws Exception {
		assertEvaluatesTo("SORT (3,2,1)", "(1,2,3)");
		assertEvaluatesTo("SORT DATA (3,2,1)", "(1,2,3)");
		assertEvaluatesTo("SORT (3,2,1,NULL)", "NULL");
		assertEvaluatesTo("SORT (\"a\",1)", "NULL");
		assertEvaluatesTo("SORT ()", "()");
	}

	@Test
	public void testSortTime() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "SORT TIME (y,x)", "(1,2)");
		assertEvaluatesToWithData(data, "SORT TIME (data1, data2)", "(1,3,2)");
		assertEvaluatesToWithData(data, "SORT TIME (data1, 5)", "NULL");
		assertEvaluatesToWithData(data, "SORT TIME ()", "()");
	}
	

}
