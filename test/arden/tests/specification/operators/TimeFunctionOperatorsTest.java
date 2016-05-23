package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class TimeFunctionOperatorsTest extends SpecificationTest {
	
	@Test
	public void testTimeAssignment() throws Exception {
		assertValidStatement("x := 1; TIME x := 1990-03-15T15:00:00;");
		assertValidStatement("x := \"x\"; TIME OF x := 1990-03-15T15:00:00;");
	}
	
	@Test
	public void testTime() throws Exception {
		String data = new ArdenCodeBuilder()
				.addData("x := 1; TIME x := 1990-03-15T15:00:00;")
				.addData("y := 2; TIME y := 1990-03-16T15:00:00;")
				.addData("z := 3; TIME z := 1990-03-18T21:00:00;")
				.addData("mylist := (x, y, z);")
				.toString();
		
		assertEvaluatesToWithData(data, "TIME mylist[1]","1990-03-15T15:00:00");
		assertEvaluatesToWithData(data, "TIME OF TIME OF mylist[1]","1990-03-15T15:00:00");
		assertEvaluatesTo("TIME OF (3,4)","(null,null)");
	}
	
	@Test
	public void testTimeObjects() throws Exception {
		String data = new ArdenCodeBuilder()
				.addData("obj := OBJECT [attr1, attr2];")
				.addData("x := 1; TIME x := 2004-01-16T00:00:00;")
				.addData("y := 2.0; TIME y := 2004-01-17T00:00:00;")
				.toString();
		
		String attrTime = new ArdenCodeBuilder(data)
				.addData("result1 := NEW obj WITH x, x;")
				.addAction("RETURN TIME OF result1.attr1;")
				.toString();
		assertReturns(attrTime, "2004-01-16T00:00:00");
		
		String sameAttrTimes = new ArdenCodeBuilder(data)
				.addData("result2 := NEW obj WITH x, x;")
				.addAction("RETURN TIME OF result2")
				.toString();
		assertReturns(sameAttrTimes, "2004-01-16T00:00:00");
		
		String differentAttrTimes = new ArdenCodeBuilder(data)
				.addData("result3 := NEW obj WITH x, y;")
				.addAction("RETURN TIME OF result3")
				.toString();
		assertReturns(differentAttrTimes, "NULL");
		
		String listAttr = new ArdenCodeBuilder(data)
				.addData("result4 := NEW obj WITH x, (x,x);")
				.addAction("RETURN TIME OF result4")
				.toString();
		assertReturns(listAttr, "NULL");
		
		/*
		 * TODO is "TIME OF result.value := ..." valid?
		 * It is given as an example in the specification, but the the grammar
		 * rule <time_becomes> only allows <identifier> not
		 * <identifier_or_object_ref>
		 */
		//		String attrTimeAssignment = new ArdenCodeBuilder(data)
		//				.addData("result5 := NEW obj WITH x, y;")
		//				.addData("TIME OF result5.attr1 := 2000-01-16T00:00:00;")
		//				.addAction("RETURN TIME OF result5.attr1;")
		//				.toString();
		//		assertReturns(attrTimeAssignment, "2000-01-16T00:00:00");
	}

}
