package arden.tests.specification.structureslots;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class DataSlotTest extends SpecificationTest {
	// TODO error on illegal statements	
	
	@Test
	public void testReadStatement() throws Exception {
		String readMapping = getMappings().getReadMapping();
		String readMultiMapping = getMappings().getReadMultipleMapping();
		
		String sortedByTime1 = createCodeBuilder()
				.addData("first_ := READ FIRST {"+readMapping+"};")
				.addData("earliest_  := READ EARLIEST {"+readMapping+"};")
				.addAction("RETURN first_ = earliest_;")
				.toString();
		assertReturns(sortedByTime1, "TRUE");
		
		String sortedByTime2 = createCodeBuilder()
				.addData("last_ := READ LAST {"+readMapping+"};")
				.addData("latest_  := READ LATEST {"+readMapping+"};")
				.addAction("RETURN last_ = latest_;")
				.toString();
		assertReturns(sortedByTime2, "TRUE");
		
		// constraint
		assertEvaluatesTo("READ THE FIRST 2 FROM {"+readMapping+"} WHERE THEY OCCURRED WITHIN THE 10 DAYS SURROUNDING 1990-01-01T00:00:00", "(3,2)");
		
		// brackets
		assertEvaluatesTo("READ SUM ({"+readMapping+"} WHERE IT OCCURRED BEFORE 1995-01-01T00:00:00)", "14");
		
		// no aggregation
		assertEvaluatesTo("READ {"+readMapping+"}", "(5,3,2,4,1)");
		
		String multipleResults = createCodeBuilder()
				.addData("(id, val):= READ LAST 2 FROM {"+readMultiMapping+"};")
				.addAction("RETURN (id, val);")
				.toString();
		assertReturns(multipleResults, "(4,1,\"d\",\"a\")");
		
		String readAs = createCodeBuilder()
				.addData("DbResult := OBJECT [Id, Val];")
				.addData("entries := READ AS DbResult {"+readMultiMapping+"};")
				.addAction("RETURN entries.val;")
				.toString();
		assertReturns(readAs, "(\"e\",\"c\",\"b\",\"d\",\"a\")");
		
		// TODO permitted aggregations:
		// exist,sum, average avg, minimum min, maximum max, last, first, earliest, latest, minimum ... from min ... from, max ... from maximum ... from, last ... from, first ... from, earliest ... from, latest ... from
		// TODO not permitted:
		// Median, Stddev, Variance, Any, All, No, Element, Extract Characters, Seqto, Reverse, Index Extraction
	}

	@Test
	public void testEventStatement() throws Exception {
		// usable as boolean
		String other_mlm = createCodeBuilder()
				.replaceSlotContent("mlmname:", "other_mlm")
				.addData("test_event := EVENT {"+getMappings().getEventMapping()+"};")
				.addEvoke("test_event;")
				.addAction("IF test_event THEN RETURN 1;")
				.addAction("ENDIF;")
				.addAction("RETURN 2;")
				.toString();
		
		String eventAsBoolean = createCodeBuilder()
			.addMlm(other_mlm)
			.addData("test_event := EVENT {"+getMappings().getEventMapping()+"};")
			.clearSlotContent("logic:")
			.addLogic("x := CALL test_event;")
			.addLogic("CONCLUDE TRUE;")
			.addAction("RETURN x;")
			.toString();
		assertReturns(eventAsBoolean, "1");
	}

	@Test
	public void testMlmStatement() throws Exception {
		// mlm search algorithm
		// 1. institution (mlm_self if missing) 2. mlmname 3. validation (only with institution missing) 4. version
		String mlm1_instself = createCodeBuilder()
				.replaceSlotContent("mlmname:", "mlm1")
				.addExpression("\"mlm1_instself\"")
				.toString();
		String mlm1_instother = createCodeBuilder()
				.replaceSlotContent("mlmname:", "mlm1")
				.replaceSlotContent("institution:", "other_institute")
				.addExpression("\"mlm1_instother\"")
				.toString();
		String mlm2_testing = createCodeBuilder()
				.replaceSlotContent("mlmname:", "mlm2")
				.replaceSlotContent("validation:", "testing")
				.addExpression("\"mlm2_testing\"")
				.toString();
		String mlm2_production = createCodeBuilder()
				.replaceSlotContent("mlmname:", "mlm2")
				.replaceSlotContent("validation:", "production")
				.addExpression("\"mlm2_production\"")				
				.toString();
		
		String data = createCodeBuilder()
				.addMlm(mlm1_instother)
				.addMlm(mlm1_instself)
				.addMlm(mlm2_testing)
				.addMlm(mlm2_production)
				.clearSlotContent("logic:")
				.addLogic("found_name := CALL found_mlm;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN found_name;")
				.toString();
		
		String noInstitution = new ArdenCodeBuilder(data).addData("found_mlm := MLM 'mlm1';").toString();
		assertReturns(noInstitution, "\"mlm1_instself\"");
		
		String otherInstitution = new ArdenCodeBuilder(data).addData("found_mlm := MLM 'mlm1' FROM INSTITUTION \"other_institute\";").toString();
		assertReturns(otherInstitution, "\"mlm1_instother\"");
		
		String validation = new ArdenCodeBuilder(data).addData("found_mlm := MLM 'mlm2';").toString();
		assertReturns(validation, "\"mlm2_production\"");
		
		// mlm self
		String mlmSelf = createCodeBuilder()
				.addData("this := MLM MLM_SELF;")
				.addData("arg := ARGUMENT;")
				.clearSlotContent("logic:")
				.addLogic("IF arg IS NOT PRESENT THEN arg := 10;")
				.addLogic("res := CALL this WITH arg-1;")
				.addLogic("ELSEIF arg > 1 THEN res := CALL this WITH (arg-1);")
				.addLogic("ELSE res := 1;")
				.addLogic("ENDIF;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN res * arg;")
				.toString();
		assertReturns(mlmSelf, "3628800");
	}

	@Test
	public void testArgumentStatement() throws Exception {
		String othermlm = createCodeBuilder()
				.replaceSlotContent("mlmname:", "other_mlm")
				.addData("(x,y) := ARGUMENT;")
				.addAction("RETURN (x,y);")
				.toString();
		String mlmSelf = createCodeBuilder()
				.addMlm(othermlm)
				.addData("othermlm := MLM 'other_mlm';")
				.clearSlotContent("logic:")
				.addLogic("res := CALL othermlm WITH \"a\", (1,2);")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN res;")
				.toString();
		assertReturns(mlmSelf, "(\"a\",1,2)");
	}

	@Test
	public void testMessageStatement() throws Exception {
		String message = createCodeBuilder()
				.addData("m := MESSAGE {"+getMappings().getMessageMapping()+"};")
				.toString();
		assertValid(message);
		
		// TODO too implementation specific? 		
		//		String messageAs = createCodeBuilder()
		//				.addData("Email := OBJECT [subject, text];")
		//				.addData("mail := MESSAGE AS Email {"+getMappings().getMessageMapping()+"};")
		//				.toString();
		//		assertValid(messageAs);
		//		
		//		String messageAsDefault = createCodeBuilder()
		//				.addData("Email := OBJECT [subject, text];")
		//				.addData("mail := MESSAGE AS Email;")
		//				.toString();
		//		assertValid(messageAsDefault);
	}

	@Test
	public void testDestinationStatement() throws Exception {
		String destination = createCodeBuilder()
				.addData("d := DESTINATION {"+getMappings().getDestinationMapping()+"};")
				.toString();
		assertValid(destination);
		
		// TODO too implementation specific?		
		//		String destinationAs = createCodeBuilder()
		//				.addData("File := OBJECT [name];")
		//				.addData("d := DESTINATION AS File {"+getMappings().getDestinationMapping()+"};")
		//				.toString();
		//		assertValid(destinationAs);
		//		
		//		String destinationAsDefault = createCodeBuilder()
		//				.addData("File := OBJECT [name];")
		//				.addData("d := DESTINATION AS File;")
		//				.toString();
		//		assertValid(destinationAsDefault);
	}

	@Test
	public void testObjectStatement() throws Exception {
		String object = createCodeBuilder()
				.addData("Patient := OBJECT [Name, DateOfBirth, Id];")
				.addData("p := NEW patient;")
				.toString();
		assertValid(object); 
	}

	@Test
	public void testIncludeStatement() throws Exception {
		String mlm1 = createCodeBuilder()
				.replaceSlotContent("mlmname:", "mlm1")
				.addData("Patient := OBJECT [Name, DateOfBirth, Id];")
				.toString();
		String mlm2 = createCodeBuilder()
				.replaceSlotContent("mlmname:", "mlm2")
				.addData("Patient := OBJECT [Id, Name, DateOfBirth, Gender, Phone];")
				.toString();
		
		String include = createCodeBuilder()
				.addMlm(mlm1)
				.addData("othermlm := MLM 'mlm1';")
				.addData("INCLUDE othermlm;")
				.addData("p := NEW Patient;")
				.addData("p.Id := 5;")
				.addAction("RETURN p.Id;")
				.toString();
		assertReturns(include, "5");
		
		String localPrecedence = createCodeBuilder()
				.addMlm(mlm1)
				.addData("Patient := OBJECT [Id, Name, Phone];")
				.addData("othermlm := MLM 'mlm1';")
				.addData("INCLUDE othermlm;")
				.addData("p := NEW Patient;")
				.addData("p.Phone := 12345;")
				.addAction("RETURN p.Phone;")
				.toString();
		assertReturns(localPrecedence, "12345");
		
		String latestPrecedence = createCodeBuilder()
				.addMlm(mlm1)
				.addMlm(mlm2)
				.addData("mlm1 := MLM 'mlm1';")
				.addData("INCLUDE mlm1;")
				.addData("mlm2 := MLM 'mlm2';")
				.addData("INCLUDE mlm2;")
				.addData("p := NEW Patient;")
				.addData("p.Gender := \"f\";")
				.addAction("RETURN p.Gender;")
				.toString();
		assertReturns(latestPrecedence, "\"f\"");
	}

}
