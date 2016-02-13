package arden.tests.specification.Operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class IsComparisonOperatorsTest extends SpecificationTest {

	@Test
	public void testIsWithinTo() throws Exception {
		assertEvaluatesTo("3 IS WITHIN 2 TO 5", "TRUE");
		assertEvaluatesTo("1990-03-10T00:00:00 IS WITHIN 1990-03-05T00:00:00 TO 1990-03-15T00:00:00", "TRUE");
		assertEvaluatesTo("3 DAYS WERE WITHIN 2 DAYS TO 5 MONTHS", "TRUE");
		assertEvaluatesTo("\"ccc\" WAS WITHIN \"a\" TO \"d\"", "TRUE");
	}

	@Test
	public void testIsWithin() throws Exception {
		assertEvaluatesTo("1990-03-08T00:00:00 WERE WITHIN 3 days PRECEDING 1990-03-10T00:00:00", "TRUE");
		assertEvaluatesTo("1990-03-08T00:00:00 IS WITHIN 3 days FOLLOWING 1990-03-10T00:00:00", "FALSE");
		assertEvaluatesTo("1990-03-08T00:00:00 IS WITHIN 3 days SURROUNDING 1990-03-10T00:00:00", "TRUE");
		assertEvaluatesTo("1990-03-08T00:00:00 WAS WITHIN PAST 3 days", "FALSE");
		assertEvaluatesTo("1990-03-08T11:11:11 IS WITHIN SAME DAY AS 1990-03-08T01:01:01", "TRUE");
	}

	@Test
	public void testBeforeAfter() throws Exception {
		assertEvaluatesTo("1990-03-08T00:00:00 IS BEFORE 1990-03-07T00:00:00", "FALSE");
		assertEvaluatesTo("1990-03-08T00:00:00 IS AFTER 1990-03-07T00:00:00", "TRUE");
		assertEvaluatesTo("1990-03-08T00:00:00 IS BEFORE 1990-03-08T00:00:01", "TRUE");
		assertEvaluatesTo("1990-03-08T00:00:02 IS BEFORE 1990-03-08T00:00:01", "FALSE");
	}

	@Test
	public void testIn() throws Exception {
		assertEvaluatesTo("2 IS IN (4,5,6)", "FALSE");
		assertEvaluatesTo("(3,4) IS IN (4,5,6)", "(FALSE,TRUE)");
		assertEvaluatesTo("NULL IN (1/0,2)", "TRUE");
		assertEvaluatesTo("2 IS NOT IN (4,5,6)", "TRUE");
		assertEvaluatesTo("2 IN (4,5,6)", "FALSE");
		
		String data = new ArdenCodeBuilder()
				.addData("x := 5; TIME x := 1990-01-01T00:00:00;")
				.addData("y := 3; TIME y := 1990-01-02T00:00:00;")
				.addData("z := 2; TIME z := 1990-01-03T00:00:00;")
				.toString();
		assertEvaluatesToWithData(data, "TIME FIRST ((x,y) IS IN (x,y,z))", "1990-01-01T00:00:00");
		assertEvaluatesToWithData(data, "TIME FIRST ((5,y) IS IN (x,y,z))", "NULL");
	}

	@Test
	public void testPresentNull() throws Exception {
		assertEvaluatesTo("3 IS PRESENT", "TRUE");
		assertEvaluatesTo("NULL IS PRESENT", "FALSE");
		assertEvaluatesTo("(3,NULL) IS NOT NULL", "(TRUE,FALSE)");
		assertEvaluatesTo("(3,NULL) IS NULL", "(FALSE,TRUE)");
	}

	@Test
	public void testTypeCheck() throws Exception {
		assertEvaluatesTo("FALSE IS BOOLEAN", "TRUE");
		assertEvaluatesTo("NULL IS BOOLEAN", "FALSE");
		assertEvaluatesTo("(NULL, FALSE, \"asdf\") IS BOOLEAN", "(FALSE,TRUE,FALSE)");
		assertEvaluatesTo("3 IS NUMBER", "TRUE");
		assertEvaluatesTo("\"abc\" IS STRING", "TRUE");
		assertEvaluatesTo("1991-03-12T00:00:00 IS TIME", "TRUE");
		assertEvaluatesTo("(3 DAYS) IS DURATION", "TRUE");
		assertEvaluatesTo("(1, 2, 3) IS LIST", "TRUE");
		assertEvaluatesTo("(,3) IS LIST", "TRUE");
	}

	@Test
	public void testObjectCheck() throws Exception {
		String pixelObject = new ArdenCodeBuilder()
				.addData("Pixel := OBJECT [x, y];")
				.addData("p := new Pixel;")
				.toString();
		
		assertEvaluatesToWithData(pixelObject, "p IS OBJECT", "TRUE");
		assertEvaluatesToWithData(pixelObject, "p IS Pixel", "TRUE");
	}
}
