package arden.tests.specification;

import org.junit.Test;

import arden.tests.specification.testcompiler.SpecificationTest;

public class DataTypesTest extends SpecificationTest {
	
	private static final String DELAY = "FOR i IN 1 SEQTO 100000 DO x := 0; ENDDO;";
	
	@Test
	public void testNull() throws Exception {
		assertEvaluatesTo("NULL || 1/0 || TRUE +1", "\"nullnullnull\"");
	}

	@Test
	public void testNumber() throws Exception {
		// floating point arithmetic
		assertEvaluatesTo("1/2", "0.5");
	}

	@Test
	public void testTime() throws Exception {
		// time before 1800-01-01
		//assertInvalidStatement("x := 1500-01-01T00:00:00;");
		
		String constantNow = createCodeBuilder()
				.addData("n := NOW;")
				.addAction(DELAY)
				.addAction("RETURN n = NOW;")
				.toString();
		assertReturns(constantNow, "TRUE");
		
		String currentTime = createCodeBuilder()
				.addData("c := CURRENTTIME;")
				.addAction(DELAY)
				.addAction("RETURN c < CURRENTTIME;")
				.toString();
		assertReturns(currentTime, "TRUE");

		// EVENTTIME <= TRIGGERTIME <= NOW <= CURRENTTIME
		assertEvaluatesTo("NOW <= CURRENTTIME", "TRUE");
		assertEvaluatesTo("TRIGGERTIME <= NOW", "TRUE");
		assertEvaluatesTo("EVENTTIME <= TRIGGERTIME", "TRUE");
	}

	@Test
	public void testDuration() throws Exception {
		// time - time
		assertEvaluatesTo("1990-03-01T00:00:00 - 1990-02-01T00:00:00", "2419200 seconds");
		
		// time + seconds
		assertEvaluatesTo("1990-02-01T00:00:00 + 2419201 seconds" , "1990-03-01T00:00:01");
		
		// time + months
		assertEvaluatesTo("1991-01-31T00:00:00 + 1 month" , "1991-02-28T00:00:00");
		assertEvaluatesTo("1991-01-31T00:00:00 + 1.1 months" , "1991-03-03T01:02:54.6");
		assertEvaluatesTo("1991-04-30T00:00:00 - 0.1 months" , "1991-04-26T22:57:05.4");
		
		// month/seconds
		assertEvaluatesTo("1 month / 1 second", "2629746");
	}

	@Test
	public void testList() throws Exception {
		assertValidStatement("x := \"Milk\", 5, TRUE, NULL, 1 second");
		
		// empty List
		assertValidStatement("x := ();");
		assertValidStatement("x := (         );");
		
		// single element list
		assertValidStatement("x := ,5;");
		assertValidStatement("x := ,NULL;");
		
		// conversion of single element to list
		assertEvaluatesTo("COUNT 5", "1");
	}
}
