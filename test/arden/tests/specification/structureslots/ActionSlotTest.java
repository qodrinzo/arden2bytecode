package arden.tests.specification.structureslots;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;
import arden.tests.specification.testcompiler.TestCompilerException;

public class ActionSlotTest extends SpecificationTest {
	// TODO error on illegal statements
	
	@Test
	public void testWriteStatement() throws Exception {
		String data = new ArdenCodeBuilder()
				.addData("text := \"test message\";")
				.addData("msg := MESSAGE {"+getMappings().getMessageMapping()+"};")
				.addData("dest := DESTINATION {"+getMappings().getDestinationMapping()+"};")
				.toString();
		
		String textDefaultDestination = new ArdenCodeBuilder(data).addAction("WRITE text;").toString();
		assertWrites(textDefaultDestination, "test message");
		
		String textAtDestination = new ArdenCodeBuilder(data).addAction("WRITE text AT dest;").toString();
		assertWrites(textAtDestination, "test message");
		
		String messageDefaultDestination = new ArdenCodeBuilder(data).addAction("WRITE msg;").toString();
		assertWrites(messageDefaultDestination, "test message");
		
		String messageAtDestination = new ArdenCodeBuilder(data).addAction("WRITE msg AT dest;").toString();
		assertWrites(messageAtDestination, "test message");
				
		// TODO test if output is grouped with multiple async mlms
	}
	
	private void assertStatementReturns(String statement, String expected) throws TestCompilerException {
		String code = new ArdenCodeBuilder().addAction(statement).toString();
		assertReturns(code, expected);
	}
	
	@Test
	public void testReturnStatement() throws Exception {
		assertStatementReturns("RETURN 5;","5");
		assertStatementReturns("RETURN (5, 3);","(5,3)");
		assertStatementReturns("RETURN 1; RETURN 2;", "1");
		
		String multiReturn = new ArdenCodeBuilder().addAction("RETURN 5, (\"a\", \"b\");").toString();
		assertReturns(multiReturn, "5", "(\"a\",\"b\")");
		
		String empty = new ArdenCodeBuilder().toString();
		assertNoReturn(empty);
	}


	@Test
	public void testCallStatement() throws Exception {
		String othermlm = new ArdenCodeBuilder()
				.replaceSlotContent("mlmname:", "other_mlm")
				.addAction("WRITE \"test message\";")
				.toString();
		String data = new ArdenCodeBuilder()
				.addMlm(othermlm)
				.addData("othermlm := MLM 'other_mlm';")
				.toString();
		
		String call = new ArdenCodeBuilder(data).addAction("CALL othermlm;").toString();
		assertValid(call);
		
		String delay = new ArdenCodeBuilder(data)
				.addAction("CALL othermlm DELAY 1 MINUTE;")
				.addAction("RETURN TRUE")
				.toString();
		assertReturns(delay, "TRUE");
		
		String args = new ArdenCodeBuilder(data).addAction("CALL othermlm WITH 5;").toString();
		assertValid(args);
		
		String argsDelay = new ArdenCodeBuilder(data).addAction("CALL othermlm WITH 1,2,3 DELAY 10 SECONDS;").toString();
		assertValid(argsDelay);
		
		String invalidReturn = new ArdenCodeBuilder(data).addAction("x := CALL othermlm;").toString();
		assertInvalid(invalidReturn);
		
		// TODO test if arguments are ignored on event call
	}

}
