package arden.tests.specification.structureslots;

import static org.junit.Assert.fail;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;
import arden.tests.specification.testcompiler.TestCompilerCompiletimeException;
import arden.tests.specification.testcompiler.TestCompilerException;

public class TokensTest extends SpecificationTest {

	protected void assertReservedWord(String reservedWord) {
		try {
			/*
			 * The BNF allows the reserved word "NOW" to be used in the
			 * statement "NOW := ...;", but not "LET NOW BE ...;" (see
			 * <identifier_becomes>).
			 */
			String invalidIdentifier = createCodeBuilder()
					.addAction("LET " + reservedWord + " BE 5;")
					.toString();
			getCompiler().compile(invalidIdentifier);
			fail("Expected an " + TestCompilerException.class.getSimpleName() + " to be thrown for reserved word: \""
					+ reservedWord + "\"");
		} catch (TestCompilerCompiletimeException e) {
			// test passed
		}
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testReservedWords() throws Exception {
		String[] reservedWords = { "abs", "destination", "increase", "month", "action", "do", "index", "months",
				"after", "duration", "institution", "ne", "ago", "earliest", "int", "nearest", "alert", "else",
				"interface", "no", "all", "elseif", "interval", "not", "and", "enddo", "is", "now", "any", "endif",
				"it", "null", "arccos", "end", "keywords", "number", "arcsin", "eq", "knowledge", "occur", "arctan",
				"equal", "last", "occurred", "arden", "event", "latest", "occurs", "are", "eventtime", "le", "of",
				"argument", "every", "less", "or", "as", "evoke", "let", "past", "at", "exist", "library", "pattern",
				"author", "exists", "links", "percent", "average", "exp", "list", "preceding", "avg", "expired", "log",
				"present", "be", "explanation", "log10", "priority", "before", "extract", "logic", "production",
				"Boolean", "false", "lt", "purpose", "call", "filename", "maintenance", "read", "ceiling", "first",
				"matches", "refute", "characters", "floor", "max", "research", "citations", "following", "maximum",
				"return", "conclude", "for", "median", "reverse", "cos", "formatted", "merge", "round", "cosine",
				"from", "message", "same", "count", "ge", "min", "second", "data", "greater", "minimum", "seconds",
				"date", "gt", "minute", "seqto", "day", "hour", "minutes", "sin", "days", "hours", "mlm", "sine",
				"decrease", "if", "mlmname", "slope", "delay", "in", "mlm_self", "sort", "specialist", "testing",
				"truncate", "weeks", "sqrt", "than", "type", "were", "starting", "the", "unique", "where", "stddev",
				"then", "until", "while", "string", "they", "urgency", "with", "sum", "time", "validation", "within",
				"support", "title", "variance", "write", "surrounding", "to", "version", "year", "tan", "triggertime",
				"was", "year", "tangent", "true", "week" };
		for (String reservedWord : reservedWords) {
			assertReservedWord(reservedWord);
		}
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_1)
	public void testReservedWordsV2_1() throws Exception {
		String[] reservedWords = { "left", "length", "find", "currenttime", "lowercase", "data_driven", "data-driven",
				"right", "substring", "trim", "uppercase", "years" };
		for (String reservedWord : reservedWords) {
			assertReservedWord(reservedWord);
		}
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_5)
	public void testReservedWordsV2_5() throws Exception {
		String[] reservedWords = { "Abs", /* typo: "arcos", */
				"include", "attribute", "names", "new", "object", "clone" };
		for (String reservedWord : reservedWords) {
			assertReservedWord(reservedWord);
		}
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testReservedWordsV2_6() throws Exception {
		String[] reservedWords = { "localized", "friday", "default", "monday", "by", "language", "saturday", "thursday",
				"wednesday", "today", "tomorrow", "resources", "sunday", "tuesday" };
		for (String reservedWord : reservedWords) {
			assertReservedWord(reservedWord);
		}
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_7)
	public void testReservedWordsV2_7() throws Exception {
		assertReservedWord("attime"); // missing from specification
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_8)
	public void testReservedWordsV2_8() throws Exception {
		String[] reservedWords = { "add", "least", "aretrue", "elements", "breakloop", "case", "istrue", "most",
				"sublist", "remove", "replace", "switch", "using" };
		for (String reservedWord : reservedWords) {
			assertReservedWord(reservedWord);
		}
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9)
	public void testReservedWordsV2_9() throws Exception {
		String[] reservedWords = { "aggregate", "crisp", "applicability", "defuzzified", "linguistic", "fuzzified",
				"fuzzy", "endswitch", "truth", "value", "variable", "set" };
		for (String reservedWord : reservedWords) {
			assertReservedWord(reservedWord);
		}
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testFutureReservedWords() throws Exception {
		String[] reservedFutureWords = { "union", "intersect", "excluding", "citation", "select" };
		for (String reservedWord : reservedFutureWords) {
			assertReservedWord(reservedWord);
		}
	}

	@Test
	public void testThe() throws Exception {
		assertValidStatement("THE LET THE x BE THE THE 5 THE;");
	}

	@Test
	public void testIdentifiers() throws Exception {
		assertValidStatement("Asdf123_X_5 := 5;");

		// invalid identifier
		assertInvalidStatement("a-b := 5;");
		assertInvalidStatement("1var := 5;");
		assertInvalidStatement("_var := 5;");

		// invalid length
		assertInvalidStatement("Lorem_ipsum_dolor_sit_amet_consectetur_adipiscing_elit_Quisque_libero_felis_bibendum_at_ullamcorper_ac_dictum_eget_eros := 5;");
		assertValidStatement("Lorem_ipsum_dolor_sit_amet_consectetur_adipiscing_elit_Quisque_libero_felis_bibe := 5;");

		String case_ = createCodeBuilder()
				.addData("AbCd := 5;")
				.addData("abcd := ABCD + 5;")
				.addAction(" RETURN abCD;")
				.toString();
		assertReturns(case_, "10");
	}

	@Test
	public void testNumberConstants() throws Exception {
		assertValidStatement("x := 500;");

		// decimal point
		assertValidStatement("x := 0.5;");
		assertValidStatement("x := 5.;");
		assertValidStatement("x := .5;");

		// exponent
		assertValidStatement("x := 12e10;");
		assertValidStatement("x := 10e13;");
		assertValidStatement("x := .1e-1;");
	}

	@Test
	public void testTimeConstants() throws Exception {
		assertValidStatement("t := 1990-01-01;");
		assertValidStatement("t := 1990-01-01T12:00:00;");

		// fractionals seconds + timezones
		assertValidStatement("t := 1990-01-01T12:00:00.15;");
		assertValidStatement("t := 1990-01-01T12:00:00.15Z;");
		assertValidStatement("t := 1990-01-01T12:00:00.15-05:00;");
		assertValidStatement("t := 1990-01-01T12:00:00.12345+01:00;");

		// case insensitive
		assertValidStatement("t := 1990-01-01t12:00:00;");
		assertValidStatement("t := 1990-01-01T12:00:00.15z;");
	}

	@Test
	public void testStringConstants() throws Exception {
		String lb = System.lineSeparator();

		// no length limit
		assertValidStatement(
				"x := \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque libero felis, bibendum at ullamcorper ac, dictum eget eros.\";");

		// interalQuotationMarks
		assertValidStatement("x := \"Lorem \"\"ipsum\"\" dolor sit amet\";");

		// single line break
		assertEvaluatesTo("\"Lorem     " + lb + "    ipsum\"", "\"Lorem ipsum\"");

		// multi line break
		assertEvaluatesTo("\"Lorem     " + lb + lb + lb + lb + "    ipsum\"", "\"Lorem" + lb + "ipsum\"");

		// empty string
		assertEvaluatesTo("\"\"", "\"\"");
	}

	@Test
	public void testTermConstant() throws Exception {
		String term = createCodeBuilder().addData("x := MLM 'other_mlm';").toString();
		assertValid(term);
	}

	@Test
	public void testMappingClauses() throws Exception {
		String mapping = createCodeBuilder()
				.addData("x := READ {Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque libero felis, bibendum at ullamcorper ac, dictum eget eros.};")
				.toString();
		assertValid(mapping);
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testSingleLineComments() throws Exception {
		assertValidStatement("// Lorem // */ ipsum " + System.lineSeparator());
		assertValidStatement("/* Lorem /* ipsum // */");
	}

	@Test
	public void testMultiLineComments() throws Exception {
		assertValidStatement("a/**/:=/*xyz*/5;");
		assertValidStatement("a/*.../*...*/:=5;");
	}

	@Test
	public void testWhiteSpace() throws Exception {
		assertValidStatement("x := 3+4;");
		assertValidStatement("x := 3 + 4;");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testTimeOfDayConstants() throws Exception {
		assertValidStatement("t := 18:30");
		assertValidStatement("t := 18:30:30");

		// midnight
		assertValidStatement("t := 00:00:00.000");
		assertInvalidStatement("t := 24:00");
		assertInvalidStatement("t := 23:60");

		// fractionals seconds + timezones
		assertValidStatement("t := 12:34:56;");
		assertValidStatement("t := 12:34:30.56;");
		assertValidStatement("t := 12:34Z;");
		assertValidStatement("t := 12:34:56Z;");
		assertValidStatement("t := 12:34-05:00;");
		assertValidStatement("t := 12:34:56-05:00;");
		assertValidStatement("t := 12:34:56.12345+01:00;");

		// case insensitive
		assertValidStatement("t := 22:22z;");
	}

}
