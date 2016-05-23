package arden.tests.specification.structureslots;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class LogicSlotTest extends SpecificationTest {
	// TODO error on illegal statements
	
	private static final String EMPTY_LOGIC_SLOT = new ArdenCodeBuilder().clearSlotContent("logic:").toString();

	@Test
	public void testAssignmentStatement() throws Exception {
		String data = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addData("Pixel := OBJECT [x, y];")
				.addData("p := new Pixel;")
				.toString();
		
		String let = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT).addLogic("LET v BE 5;").toString();
		assertValid(let);
		
		String assignNull = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT).addLogic("x := NULL;").toString();
		assertValid(assignNull);	
		
		String notReference = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("a := 5;")
				.addLogic("b := a + 3;")
				.addLogic("a := 0;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN b;")
				.toString();
		assertReturns(notReference, "8");
		
		String attrs = new ArdenCodeBuilder(data)
				.addLogic("p.x := 0;")
				.addLogic("p.y := 10;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN p.x;")
				.toString();
		assertReturns(attrs, "0");
		
		String invalidAttr = new ArdenCodeBuilder(data)
				.addLogic("p.w := 10;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN p.w;")
				.toString();
		assertReturns(invalidAttr, "NULL");
		
		String reference = new ArdenCodeBuilder(data)
				.addLogic("p.x := 10;")
				.addLogic("p2 := p;")
				.addLogic("p2.x := 5;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN p.x;")
				.toString();
		assertReturns(reference, "5");
	}
	
	@Test
	public void testReassignment() throws Exception {
		String object = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addData("Patient := OBJECT [Name, DateOfBirth, Id];")
				.addLogic("Patient := 5;")
				.toString();
		assertInvalid(object);
		
		String event = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addData("e := EVENT {something happens};")
				.addLogic("e := 5;")
				.toString();
		assertInvalid(event);
		
		String mlm = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addData("mymlm := MLM 'testmlm';")
				.addLogic("mymlm := 3;")
				.toString();
		assertInvalid(mlm);
		
		String interface_ = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addData("curl := INTERFACE {curl};")
				.addLogic("curl := NULL;")
				.toString();
		assertInvalid(interface_);
		
		String otherMlm = new ArdenCodeBuilder()
				.replaceSlotContent("mlmname:", "other_mlm")
				.addAction("RETURN 1")
				.toString();
		String chooseMlmOrInterface = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addMlm(otherMlm)
				.addData("IF FALSE THEN x := MLM 'other_mlm';")
				.addData("ELSE x := INTERFACE {"+getMappings().getInterfaceMapping()+"};")
				.addData("ENDIF;")
				.addLogic("result := CALL x WITH 3, 4;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN result;")
				.toString();
		assertReturns(chooseMlmOrInterface, "7");
	}

	@Test
	public void testIfThenStatement() throws Exception {
		String if_ = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("IF 5 = 5 THEN a := 0;")
				.addLogic("ENDIF;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN a;")
				.toString();
		assertReturns(if_, "0");
		
		String else_ = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addAction("IF ,TRUE THEN a := 0;")
				.addAction("ELSE a := 1;")
				.addAction("ENDIF;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN a;")
				.toString();
		assertReturns(else_, "1");
		
		String elseif1 = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("IF 5 THEN a := 0;")
				.addLogic("ELSEIF TRUE THEN a := 1;")
				.addLogic("ENDIF;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN a;")
				.toString();
		assertReturns(elseif1, "1");
		
		String elseif2 = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("IF NULL THEN a := 0;")
				.addLogic("ELSEIF FALSE THEN a := 1;")
				.addLogic("ELSEIF TRUE THEN a := 3;")
				.addLogic("ELSE a := 4;")
				.addLogic("ENDIF;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN a;")
				.toString();
		assertReturns(elseif2, "3");
	}
	
	@Test
	public void testConcludeStatement() throws Exception {
		String conclude = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN TRUE;")
				.toString();
		assertReturns(conclude, "TRUE");
		
		String concludeVariable = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("a := 5;")
				.addLogic("CONCLUDE TRUE;")
				.addLogic("a := 3;")
				.addLogic("CONCLUDE FALSE;")
				.addAction("RETURN a;")
				.toString();
		assertReturns(concludeVariable, "5");
		
		String noAction1 = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("CONCLUDE FALSE;")
				.addAction("RETURN TRUE;")
				.toString();
		assertNoReturn(noAction1);
				
		String noAction2 = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.clearSlotContent("logic:")
				.addAction("RETURN TRUE;")
				.toString();
		assertNoReturn(noAction2);
				
		String noAction3 = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("CONCLUDE NULL;")
				.addAction("RETURN TRUE;")
				.toString();
		assertNoReturn(noAction3);
				
		String noAction4 = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("CONCLUDE ,TRUE;")
				.addAction("RETURN TRUE;")
				.toString();
		assertNoReturn(noAction4);
		
		String noAction5 = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("CONCLUDE FALSE;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN TRUE;")
				.toString();
		assertNoReturn(noAction5);
	}

	@Test
	public void testMlmCallStatement() throws Exception {
		// other mlm which is called
		String otherMlm = new ArdenCodeBuilder()
				.replaceSlotContent("mlmname:", "other_mlm")
				.addData("(a, b) := ARGUMENT;")
				.addAction("IF a IS PRESENT THEN RETURN a, b; ENDIF;")
				.toString();
		String data = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addMlm(otherMlm)
				.addData("othermlm := MLM 'other_mlm';")
				.toString();
		
		String noReturn = new ArdenCodeBuilder(data)
				.addLogic("a := CALL othermlm;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN a;")
				.toString();
		assertReturns(noReturn, "NULL");
		
		String param = new ArdenCodeBuilder(data)
				.addLogic("a := CALL othermlm WITH 3;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN a;")
				.toString();
		assertReturns(param, "3");
		
		String nullParam = new ArdenCodeBuilder(data)
				.addLogic("a := CALL othermlm WITH NULL;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN a;")
				.toString();
		assertReturns(nullParam, "NULL");
		
		String multiParams = new ArdenCodeBuilder(data)
				.addLogic("(a,b,c) := CALL othermlm WITH 1,2,3;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN (a,b,c);")
				.toString();
		assertReturns(multiParams, "(1,2,NULL)");
		
		String listParam = new ArdenCodeBuilder(data)
				.addLogic("(a, b) := CALL othermlm WITH (1,2,3), 2;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN a*b;")
				.toString();
		assertReturns(listParam, "(2,4,6)");
	}
	
	@Test
	public void testEventCallStatement() throws Exception {
		String eventAssignment = "test_event := EVENT {" + getMappings().getEventMapping() + "};";
		
		String mlm1 = new ArdenCodeBuilder()
				.replaceSlotContent("mlmname:", "mlm1")
				.addData(eventAssignment)
				.addData("(a, b) := ARGUMENT;")
				.addEvoke("test_event;")
				.addAction("RETURN a+b;")
				.toString();
		
		String mlm2 = new ArdenCodeBuilder()
				.replaceSlotContent("mlmname:", "mlm2")
				.addData(eventAssignment)
				.addData("(a, b) := ARGUMENT;")
				.addEvoke("test_event;")
				.addAction("RETURN a*b;")
				.toString();
		
		String mlm3 = new ArdenCodeBuilder()
				.replaceSlotContent("mlmname:", "mlm3")
				.addData(eventAssignment)
				.addEvoke("test_event;")
				.addAction("RETURN NULL;")
				.toString();
		
		String eventCall = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addMlm(mlm1)
				.addMlm(mlm2)
				.addMlm(mlm3)
				.addData(eventAssignment)
				.addLogic("result_collection := CALL test_event WITH 3,4;")
				.addLogic("mlm1_success := 7 IS IN result_collection;")
				.addLogic("mlm2_success := 12 IS IN result_collection;")
				.addLogic("mlm3_success := NULL IS NOT IN result_collection;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN mlm1_success AND mlm2_success AND mlm3_success;")
				.toString();
		assertReturns(eventCall, "TRUE");
	}
	
	@Test
	public void testInterfaceCallStatement() throws Exception {
		String interfaceCall = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addData("test_interface := INTERFACE {" + getMappings().getInterfaceMapping() + "};")
				.addLogic("(x, y, z) := CALL test_interface WITH 3, 4;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN (x,y,z);")
				.toString();
		assertReturns(interfaceCall, "(7,12,NULL)");
	}

	@Test
	public void testWhileLoop() throws Exception {
		String whileLoop = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addData("i := 0;")
				.addData("c := 100;")
				.addLogic("WHILE c > 0 DO c := c-7;")
				.addLogic("i := i+1;")
				.addLogic("ENDDO;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN (c, i);")
				.toString();
		assertReturns(whileLoop, "(-5,15)");
	}

	@Test
	public void testForLoop() throws Exception {
		String forLoop1 = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addData("fac := 1;")
				.addLogic("FOR i IN 1 SEQTO 5 DO fac := fac*i;")
				.addLogic("ENDDO;")
				.addLogic("CONCLUDE TRUE;")
				.addAction(" RETURN fac;")
				.toString();
		assertReturns(forLoop1, "120");
		
		String forLoop2 = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("FOR i IN () DO CONCLUDE FALSE;")
				.addLogic("ENDDO;")
				.addLogic("CONCLUDE TRUE")
				.addAction("RETURN 1;")
				.toString();
		assertReturns(forLoop2, "1");
		
		String forLoop3 = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("FOR i IN NULL DO CONCLUDE FALSE;")
				.addLogic("ENDDO;")
				.addLogic("CONCLUDE TRUE")
				.addAction("RETURN 1;")
				.toString();
		assertReturns(forLoop3, "1");
		
		String reassign = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addLogic("FOR i IN (1,2,3) DO i := 1;")
				.addLogic("ENDDO;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN FALSE;")
				.toString();
		assertInvalid(reassign);
	}

	@Test
	public void testNewStatement() throws Exception {
		String data = new ArdenCodeBuilder(EMPTY_LOGIC_SLOT)
				.addData("Patient := OBJECT [Name, DateOfBirth, Id];")
				.toString();
		
		String new_ = new ArdenCodeBuilder(data).addLogic("blankPatient := NEW Patient;").toString();
		assertValid(new_);
		
		String newWith = new ArdenCodeBuilder(data)
				.addLogic("john := NEW Patient WITH \"John Doe\", \"1970-01-01T00:00:00\", \"1\", \"X\";")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN john.Name;")
				.toString();
		assertReturns(newWith, "\"John Doe\"");
		
		String uninitializedAttr = new ArdenCodeBuilder(data)
				.addLogic("john := NEW Patient WITH \"John Doe\", \"1970-01-01T00:00:00\";")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN john.Id;")
				.toString();
		assertReturns(uninitializedAttr, "NULL");
	}

}
