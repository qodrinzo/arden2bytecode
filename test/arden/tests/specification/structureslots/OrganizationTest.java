package arden.tests.specification.structureslots;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.SpecificationTest;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;

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
	@Compatibility(min = ArdenVersion.V2) // mlmname
	public void testScope() throws Exception {
		// entire mlm
		String slotScope = createCodeBuilder()
				.addData("x := 5;")
				.addAction("RETURN x")
				.toString();
		assertReturns(slotScope, "5");

		// not other mlms
		String otherMlm = createEmptyLogicSlotCodeBuilder()
				.replaceSlotContent("mlmname:", "other_mlm")
				.addData("a := 1;")
				.addData("c := 2;")
				.addLogic("a := 3;")
				.addLogic("CONCLUDE TRUE")
				.addAction("RETURN a;")
				.toString();
		String scope = createEmptyLogicSlotCodeBuilder()
				.addMlm(otherMlm)
				.addData("a := 4;")
				.addLogic("b := 5;")
				.addLogic("c := c;")
				.addLogic("CONCLUDE TRUE")
				.addAction("RETURN (a,b,c);")
				.toString();
		assertReturns(scope, "(4,5,NULL)");
	}
}
