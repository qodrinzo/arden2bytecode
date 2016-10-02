package arden.tests.specification.structureslots;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;
import arden.tests.specification.testcompiler.TestCompilerException;

public class ActionSlotTest extends SpecificationTest {
	// TODO error on illegal statements

	@Test
	@Compatibility(max = ArdenVersion.V2_1, pedantic = true)
	public void testAssignmentForbidden() throws Exception {
		// assignment not allowed before v2.5
		String actionAssignment = createCodeBuilder().addAction("a := 5;").toString();
		assertInvalid(actionAssignment);
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_5)
	public void testAssignment() throws Exception {
		String actionAssignment = createCodeBuilder()
				.addAction("a := 5;")
				.addAction("RETURN a;")
				.toString();
		assertReturns(actionAssignment, "5");
	}

	@Test
	public void testWriteStatement() throws Exception {
		String data = createCodeBuilder()
				.addData("text := \"test message\";")
				.addData("msg := MESSAGE {" + getMappings().getMessageMapping() + "};")
				.addData("dest := DESTINATION {" + getMappings().getDestinationMapping() + "};")
				.toString();

		String textDefaultDestination = new ArdenCodeBuilder(data).addAction("WRITE text;").toString();
		assertWrites(textDefaultDestination, "test message");

		String textAtDestination = new ArdenCodeBuilder(data).addAction("WRITE text AT dest;").toString();
		assertWrites(textAtDestination, "test message");

		String messageDefaultDestination = new ArdenCodeBuilder(data).addAction("WRITE msg;").toString();
		assertWrites(messageDefaultDestination, "test message");

		String messageAtDestination = new ArdenCodeBuilder(data).addAction("WRITE msg AT dest;").toString();
		assertWrites(messageAtDestination, "test message");

		// TODO test if output is grouped with multiple async mlms?
	}

	@Test
	public void testReturnStatement() throws Exception {
		assertStatementReturns("RETURN 5;", "5");
		assertStatementReturns("RETURN (5, 3);", "(5,3)");
		assertStatementReturns("RETURN 1; RETURN 2;", "1");

		// multi return
		assertStatementReturns("RETURN 5, (\"a\", \"b\");", "5", "(\"a\",\"b\")");

		String empty = createCodeBuilder().toString();
		assertNoReturn(empty);
	}

	private void assertStatementReturns(String statement, String... expected) throws TestCompilerException {
		String code = createCodeBuilder().addAction(statement).toString();
		assertReturns(code, expected);
	}

	@Test(timeout = 5000)
	public void testMlmCallStatement() throws Exception {
		String startEvent = getMappings().createEventMapping();
		String othermlm = createCodeBuilder()
				.setName("other_mlm")
				.addAction("WRITE \"test message\";")
				.toString();
		String data = createCodeBuilder()
				.addMlm(othermlm)
				.addData("other_mlm := MLM 'other_mlm';")
				.addData("start_event := EVENT {" + startEvent + "};")
				.addEvoke("start_event")
				.toString();

		String call = new ArdenCodeBuilder(data).addAction("CALL other_mlm;").toString();
		assertDelayedBy(call, startEvent, 0);

		String delay = new ArdenCodeBuilder(data).addAction("CALL other_mlm DELAY .7 SECONDS;").toString();
		assertDelayedBy(delay, startEvent, 700);

		String invalidReturn = new ArdenCodeBuilder(data).addAction("x := CALL other_mlm;").toString();
		assertInvalid(invalidReturn);
	}

	@Test(timeout = 5000)
	@Compatibility(min = ArdenVersion.V2)
	public void testMlmCallArguments() throws Exception {
		String startEvent = getMappings().createEventMapping();
		String othermlm = createCodeBuilder()
				.setName("other_mlm")
				.addData("(arg1, arg2, arg3) := ARGUMENT")
				.addAction("WRITE arg1 || arg2 || arg3;")
				.toString();
		String data = createCodeBuilder()
				.addMlm(othermlm)
				.addData("other_mlm := MLM 'other_mlm';")
				.addData("start_event := EVENT {" + startEvent + "};")
				.addEvoke("start_event")
				.toString();

		String args = new ArdenCodeBuilder(data).addAction("CALL other_mlm WITH 5;").toString();
		assertWritesAfterEvent(args, startEvent, "5NULLNULL");

		String argsDelay = new ArdenCodeBuilder(data)
				.addAction("CALL other_mlm WITH 1,2,3 DELAY 0.6 SECONDS;")
				.toString();
		assertWritesAfterEvent(argsDelay, startEvent, "123");
		assertDelayedBy(argsDelay, startEvent, 600);
	}

	@Test(timeout = 5000)
	public void testEventCallStatement() throws Exception {
		String startEvent = getMappings().createEventMapping();
		String callEvent = getMappings().createEventMapping();
		String othermlm = createCodeBuilder()
				.setName("other_mlm")
				.addData("call_event := EVENT {" + callEvent + "};")
				.addEvoke("call_event")
				.addAction("WRITE \"success\";")
				.toString();
		String data = createCodeBuilder()
				.addMlm(othermlm)
				.addData("start_event := EVENT {" + startEvent + "};")
				.addData("call_event := EVENT {" + callEvent + "};")
				.addEvoke("start_event")
				.toString();
	
		String eventCall = new ArdenCodeBuilder(data).addAction("CALL call_event;").toString();
		assertDelayedBy(eventCall, startEvent, 0);
	
		String delay = new ArdenCodeBuilder(data).addAction("CALL call_event DELAY .7 SECONDS;").toString();
		assertDelayedBy(delay, startEvent, 700);
	
		String invalidReturn = new ArdenCodeBuilder(data).addAction("x := CALL call_event;").toString();
		assertInvalid(invalidReturn);
	}

	@Test(timeout = 5000)
	@Compatibility(min = ArdenVersion.V2)
	public void testEventCallArguments() throws Exception {
		String startEvent = getMappings().createEventMapping();
		String callEvent = getMappings().createEventMapping();
		String othermlm = createCodeBuilder()
				.setName("other_mlm")
				.addData("call_event := EVENT {" + callEvent + "};")
				.addData("arg := ARGUMENT;")
				.addEvoke("call_event")
				.addAction("WRITE arg;")
				.toString();
		String data = createCodeBuilder()
				.addMlm(othermlm)
				.addData("start_event := EVENT {" + startEvent + "};")
				.addData("call_event := EVENT {" + callEvent + "};")
				.addEvoke("start_event")
				.toString();

		String args = new ArdenCodeBuilder(data).addAction("CALL call_event WITH 5;").toString();
		assertWritesAfterEvent(args, startEvent, "NULL");

		String argsDelay = new ArdenCodeBuilder(data)
				.addAction("CALL call_event WITH 1,2,3 DELAY 0.6 SECONDS;")
				.toString();
		assertDelayedBy(argsDelay, startEvent, 600);
	}

}
