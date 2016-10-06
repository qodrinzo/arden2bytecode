package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
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
		assertEvaluatesTo("1,2", "(1,2)");
		assertEvaluatesTo("(1,2), 3", "(1,2,3)");
		assertEvaluatesTo("1, (3,4)", "(1,3,4)");
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
	@Compatibility(min = ArdenVersion.V2)
	public void testSortData() throws Exception {
		assertEvaluatesTo("SORT (3,2,1)", "(1,2,3)");
		assertEvaluatesTo("SORT DATA (3,2,1)", "(1,2,3)");
		assertEvaluatesTo("SORT (3,2,1,NULL)", "NULL");
		assertEvaluatesTo("SORT (\"a\",1)", "NULL");
		assertEvaluatesTo("SORT ()", "()");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testSortTime() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "SORT TIME (y,x)", "(1,2)");
		assertEvaluatesToWithData(data, "SORT TIME (data1, data2)", "(1,3,2)");
		assertEvaluatesToWithData(data, "SORT TIME (data1, 5)", "NULL");
		assertEvaluatesToWithData(data, "SORT TIME ()", "()");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testSortApplicability() throws Exception {
		String data = createCodeBuilder()
				.addData("x := 1; APPLICABILITY x := TRUTH VALUE 0.3;")
				.addData("y := 2; APPLICABILITY y := TRUTH VALUE 0.9;")
				.addData("z := 3; APPLICABILITY z := FALSE;")
				.addData("data2 := (x,y);")
				.toString();
		assertEvaluatesToWithData(data, "SORT APPLICABILITY (x,y,z)", "(3,1,2)");
		assertEvaluatesToWithData(data, "SORT APPLICABILITY (x,y,5)", "(1,2,3)");

		/*
		 * The example implies that NULL has NULL as applicability, but the
		 * specification also says "Since null is not allowed as degree of
		 * applicability, a value is always returned".
		 */
		// assertEvaluatesTo("SORT APPLICABILITY (3,1,2,NULL)", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testSortUsing() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "SORT (x,y) USING TIME OF IT", "(1,2)");
		assertEvaluatesTo("SORT (3,2,1) USING IT", "(1,2,3)");
		assertEvaluatesTo("SORT (20:00:00,10:00:00,15:00:00) USING EXTRACT HOUR IT", "(10:00:00,15:00:00,20:00:00)");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testAddTo() throws Exception {
		assertEvaluatesTo("ADD 4 TO (1,2,3)", "(1,2,3,4)");
		assertEvaluatesTo("ADD NULL TO (1,2,3) AT 2", "(1,NULL,2,3)");
		assertEvaluatesTo("ADD NULL TO (1,2,3) AT 1+1", "(1,NULL,2,3)");
		assertEvaluatesTo("ADD \"xyz\" TO NULL", "(NULL,\"xyz\")");
		assertEvaluatesTo("ADD \"xyz\" TO NULL", "(NULL,\"xyz\")");
		assertEvaluatesTo("ADD 4 TO (1,2,3) AT 5", "(1,2,3,4)");
		assertEvaluatesTo("ADD 4 TO (1,2,3) AT 0", "(4,1,2,3)");
		assertEvaluatesTo("ADD 4 TO (1,2,3) AT (-1)", "(4,1,2,3)");
		assertEvaluatesTo("ADD 4 TO (1,2,3) AT (1,2)", "(4,1,4,2,3)");
		assertEvaluatesTo("ADD (4,5) TO (1,2,3)", "(1,2,3,4,5)");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testRemoveFrom() throws Exception {
		assertEvaluatesTo("REMOVE 1 FROM (3,2,1)", "(2,1)");
		assertEvaluatesTo("REMOVE (2,4) FROM (5,4,3,2,1)", "(5,3,1)");
		assertEvaluatesTo("REMOVE NULL FROM (1,2,3)", "(1,2,3)");
		assertEvaluatesTo("REMOVE 5 FROM (1,2,3)", "(1,2,3)");
		assertEvaluatesTo("REMOVE 2 FROM NULL", "(,NULL)");
		assertEvaluatesTo("REMOVE 1 FROM NULL", "()");
	}

}
