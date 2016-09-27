package arden.tests.specification.structureslots;

import org.junit.Test;

import arden.tests.specification.testcompiler.SpecificationTest;

public class OrganizationTest extends SpecificationTest {
	
	@Test
	public void testStatements() throws Exception {
		// optional semicolon for last statement in slot
		assertValidStatement("x := 5; y := 6");
		assertValidStatement("x := 5; y := 6;");
		
		// ;;; invalid
		assertInvalidStatement("x := 5; y := 6;;; //");
		assertValidStatement("x := 5; y := 6;/**/;; //");
	}

	@Test
	public void testExpressions() throws Exception {
		// nested
		assertValidStatement("x := 5; y := (1+2) - 3*x >= -10 AND SIN 0 = 0;");
	}

	@Test
	public void testVariables() throws Exception {
		// null if no value assigned
		String useBeforeAssign = createCodeBuilder()
				.addData("x := x;")
				.addAction("RETURN x;")
				.toString();
		assertReturns(useBeforeAssign, "NULL");

		String uninitialized = createCodeBuilder()
				.addData("IF FALSE THEN x := 0;")
				.addData("ENDIF;")
				.addAction("RETURN x;")
				.toString();
		assertReturns(uninitialized, "NULL");
	}
	
	@Test
	public void testScope() throws Exception {
		// entire mlm
		String slotScope = createCodeBuilder()
				.addData("x := 5;")
				.addAction("RETURN x")
				.toString();
		assertReturns(slotScope, "5");
		
		// not other mlms
		String otherMlm = createCodeBuilder()
				.replaceSlotContent("mlmname:", "other_mlm")
				.addData("a := 1;")
				.addData("c := 2;")
				.addAction("a := 3;")
				.addAction("RETURN a;")
				.toString();
		String scope = createCodeBuilder()
				.addMlm(otherMlm)
				.addData("a := 4;")
				.addAction("b := 5;")
				.addAction("c := c;")
				.addAction("RETURN (a,b,c);")
				.toString();
		assertReturns(scope, "(4,5,NULL)");
	}
}
