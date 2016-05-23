package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class StringOperatorsTest extends SpecificationTest {
	
	private static final String DATA = new ArdenCodeBuilder()
			.addData("x := \"hello world\"; TIME x := 1990-01-01T00:00:00;")
			.toString();

	@Test
	public void testConcatenation() throws Exception {
		assertEvaluatesTo("null || 3", "\"null3\"");
		assertEvaluatesTo("4 || 5", "\"45\"");
		assertEvaluatesTo("4.7 || \"four\"", "\"4.7four\"");
		assertEvaluatesTo("true || \"\"", "\"true\"");
		assertEvaluatesTo("3 days || \" left\"", "\"3 days left\"");
		assertEvaluatesTo("\"on \" || 1990-03-15T13:45:01", "\"on 1990-03-15T13:45:01\"");
		assertEvaluatesTo("\"list=\" || (1,2,3)", "\"list=(1,2,3)\"");
		assertEvaluatesToWithData(DATA, "TIME (x || x)", "NULL");
	}

	@Test
	public void testFormattedWith() throws Exception {
		assertEvaluatesTo("(1,2,3.3) formatted with \"%2.2d::%2.2d::%2.2d\"", "\"01::02::03\"");
		assertEvaluatesTo("10.60528 formatted with \"The result was %.2f mg\"", "\"The result was 10.61 mg\"");
		// TODO how to check implementation independent
		//assertEvaluatesTo("1998-01-10T17:25:00 formatted with \"The date was %.2t\"", "\"The date was Jan 10 1998\"");
		assertEvaluatesTo("1998-01-10T17:25:00 formatted with \"The year was %.0t\"", "\"The year was 1998\"");
		assertEvaluatesTo("(\"ten\", \"twenty\", \"thirty\") formatted with \"%s, %s, %s or more\"", "\"ten, twenty, thirty or more\"");
		assertEvaluatesTo("1 formatted with \"-%2.2d%%-\"", "\"-01%-\"");
		assertEvaluatesTo("(\"a\", \"b\", \"c\") formatted with \"%s ~ %s ~ %s\"", "\"a ~ b ~ c\"");
		assertEvaluatesTo("(97, 98, 99) formatted with \"%c, %c, %c\"", "\"a, b, c\"");
		assertEvaluatesTo("5.1234 formatted with \"%3.5f\"", "\"5.12340\"");
		assertEvaluatesTo("12345678 formatted with \"%i\"", "\"12345678\"");
		assertEvaluatesTo("1 formatted with \"%3d%%\"", "\"  1%\"");
		assertEvaluatesTo("1 formatted with \"%03d%%\"", "\"001%\"");
		assertEvaluatesTo("(4,42) formatted with \"%0*d\"", "\"0042\"");
		assertEvaluatesTo("1 formatted with \"%-3i\"", "\"1  \"");
		assertEvaluatesTo("1 formatted with \"%-+3i\"", "\"+1 \"");
		assertEvaluatesTo("\"abc\" formatted with \"%3.2s\"", "\" ab\"");
		assertEvaluatesTo("5.1234 formatted with \"%.4e\"", "\"5.1234e+000\"");
		assertEvaluatesTo("5.1234 formatted with \"%.4g\"", "\"5.123\"");
		assertEvaluatesTo("8 formatted with \"%o\"", "\"10\"");
		assertEvaluatesTo("8 formatted with \"%u\"", "\"8\"");
		assertEvaluatesTo("63 formatted with \"%x\"", "\"3f\"");
		assertInvalidExpression("8 formatted with \"%n\"");
		assertInvalidExpression("8 formatted with \"%p\"");
	}

	@Test
	public void testString() throws Exception {
		assertEvaluatesTo("STRING (\"a\",\"bc\")", "\"abc\"");
		assertEvaluatesTo("STRING ()", "\"\"");
		assertEvaluatesToWithData(DATA, "TIME STRING x", "NULL");
		assertEvaluatesTo("STRING ()", "\"\"");
		assertEvaluatesTo("STRING REVERSE EXTRACT CHARACTERS \"abcde\"", "\"edcba\"");
	}

	@Test
	public void testMatches() throws Exception {
		assertEvaluatesTo("\"fatal heart attack\" MATCHES PATTERN \"%heart%\"", "TRUE");
		assertEvaluatesTo("\"fatal heart attack\" MATCHES PATTERN \"heart\"", "FALSE");
		assertEvaluatesTo("\"abnormal values\" MATCHES PATTERN \"%value_\"", "TRUE");
		assertEvaluatesTo("(\"stunned myocardium\", \"myocardial infarction\") MATCHES PATTERN \"%myocardium\"", "(TRUE,FALSE)");
		assertEvaluatesTo("\"5%\" MATCHES PATTERN \"_\\%\"", "TRUE");
		assertEvaluatesToWithData(DATA, "TIME (x MATCHES PATTERN \"%hello%\")", "NULL");
	}

	@Test
	public void testLength() throws Exception {
		assertEvaluatesTo("LENGTH OF \" Example String \"", "16");
		assertEvaluatesTo("LENGTH \"\"", "0");
		assertEvaluatesTo("LENGTH ()", "NULL");
		assertEvaluatesTo("LENGTH OF NULL", "NULL");
		assertEvaluatesTo("LENGTH OF (\"Negative\", \"Pos\", 2)", "(8,3,NULL)");
		assertEvaluatesToWithData(DATA, "TIME LENGTH x", "NULL");
	}

	@Test
	public void testCase() throws Exception {
		assertEvaluatesTo("UPPERCASE \"Example String\"", "\"EXAMPLE STRING\"");
		assertEvaluatesTo("UPPERCASE \"\"", "\"\"");
		assertEvaluatesTo("LOWERCASE NULL", "NULL");
		assertEvaluatesTo("UPPERCASE ()", "NULL");
		assertEvaluatesTo("LOWERCASE 12.8", "NULL");
		assertEvaluatesTo("UPPERCASE (\"5-Hiaa\", \"Pos\", 2)", "(\"5-HIAA\",\"POS\",NULL)");
		assertEvaluatesToWithData(DATA, "TIME (UPPERCASE x)", "1990-01-01T00:00:00");
	}

	@Test
	public void testTrim() throws Exception {
		assertEvaluatesTo("TRIM \" example \"", "\"example\"");
		assertEvaluatesTo("TRIM \"\"", "\"\"");
		assertEvaluatesTo("TRIM ()", "NULL");
		assertEvaluatesTo("TRIM LEFT \" result: \"", "\"result: \"");
		assertEvaluatesTo("TRIM RIGHT \" result: \"", "\" result:\"");
		assertEvaluatesTo("TRIM (\" 5 N\", \"2 E \", 2)", "(\"5 N\",\"2 E\",NULL)");
		assertEvaluatesToWithData(DATA, "TIME (TRIM x)", "1990-01-01T00:00:00");
	}

	@Test
	public void testFind() throws Exception {
		assertEvaluatesTo("FIND \"E\" IN STRING \"Example Here\"", "1");
		assertEvaluatesTo("FIND \"e\" IN STRING \"Example Here\"", "7");
		assertEvaluatesTo("FIND \"ple\" IN STRING \"Example Here\"", "5");
		assertEvaluatesTo("FIND \"s\" IN STRING \"Example Here\"", "0");
		assertEvaluatesTo("FIND 2 IN STRING \"Example Here\"", "NULL");
		assertEvaluatesTo("FIND \"a\" STRING 510", "NULL");
		assertEvaluatesTo("FIND \"t\" STRING (\"start\", \"meds\", \"halt\")", "(2,0,4)");
		assertEvaluatesTo("FIND \"e\" IN STRING \"Example Here\" STARTING AT 1", "7");
		assertEvaluatesTo("FIND \"e\" IN STRING \"Example Here\" STARTING AT 1.5", "NULL");
		assertEvaluatesTo("FIND \"e\" IN STRING \"Example Here\" STARTING AT \"x\"", "NULL");
		assertEvaluatesTo("FIND \"e\" IN STRING \"Example Here\" STARTING AT 99", "0");
		assertEvaluatesTo("FIND \"e\" IN STRING \"Example Here\" STARTING AT (10,11)", "(10,12)");
		assertEvaluatesToWithData(DATA, "TIME (FIND \"e\" IN STRING x)", "NULL");
	}

	@Test
	public void testSubstring() throws Exception {
		assertEvaluatesTo("SUBSTRING 2 CHARACTERS FROM \"abcdefg\"", "\"ab\"");
		assertEvaluatesTo("SUBSTRING 100 CHARACTERS FROM \"abcdefg\"", "\"abcdefg\"");
		assertEvaluatesTo("SUBSTRING 3 CHARACTERS STARTING AT 4 FROM \"abcdefg\"", "\"def\"");
		assertEvaluatesTo("SUBSTRING 20 CHARACTERS STARTING AT 4 FROM \"abcdefg\"", "\"defg\"");
		assertEvaluatesTo("SUBSTRING 2.3 CHARACTERS FROM \"abcdefg\"", "NULL");
		assertEvaluatesTo("SUBSTRING 2 CHARACTERS STARTING AT 4.7 FROM \"abcdefg\"", "NULL");
		assertEvaluatesTo("SUBSTRING 3 CHARACTERS STARTING AT \"c\" FROM \"abcdefg\"", "NULL");
		assertEvaluatesTo("SUBSTRING \"b\" CHARACTERS STARTING AT 4 FROM \"abcdefg\"", "NULL");
		assertEvaluatesTo("SUBSTRING 3 CHARACTERS STARTING AT 4 FROM 281471", "NULL");
		assertEvaluatesTo("SUBSTRING 1 CHARACTERS STARTING AT 4 FROM \"abcdefg\"", "\"d\"");
		assertEvaluatesTo("SUBSTRING -1 CHARACTERS STARTING AT 4 FROM \"abcdefg\"", "\"d\"");
		assertEvaluatesTo("SUBSTRING -3 CHARACTERS STARTING AT 4 FROM \"abcdefg\"", "\"bcd\"");
		assertEvaluatesTo("SUBSTRING 1 CHARACTERS FROM \"abcdefg\"", "\"a\"");
		assertEvaluatesTo("SUBSTRING -1 CHARACTERS STARTING AT LENGTH OF \"abcdefg\" FROM \"abcdefg\"", "\"g\"");
		assertEvaluatesTo("SUBSTRING 3 CHARACTERS FROM (\"Positive\",\"Negative\",2)", "(\"Pos\",\"Neg\",NULL)");
		assertEvaluatesToWithData(DATA, "TIME (SUBSTRING 3 CHARACTERS FROM x)", "1990-01-01T00:00:00");
	}

}
