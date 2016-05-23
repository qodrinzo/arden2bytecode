package arden.tests.specification.structureslots;

import static org.junit.Assert.fail;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;
import arden.tests.specification.testcompiler.TestCompilerCompiletimeException;
import arden.tests.specification.testcompiler.TestCompilerException;

public class TokensTest extends SpecificationTest {
	
	public void assertInvalidIdentifier(String reservedWord) {
		try {
			String invalidIdentifier = new ArdenCodeBuilder().addAction(reservedWord+ " := 5;").toString();
			getCompiler().compile(invalidIdentifier);
			fail("Expected an " + TestCompilerException.class.getSimpleName() + " to be thrown for reserved word: \"" + reservedWord+ "\"");
		} catch(TestCompilerCompiletimeException e) {
			// test passed
		}
	}

	@Test
	public void testReservedWords() throws Exception {
		
		// reserved word (keywords)
		String[] reservedWords = {
				"abs", "action", "Abs", "action", "after", "ago", "alert", "all", "and", "any",
				"arcos", "arcsin", "arctan", "arden", "are", "argument", "as", "at", "attribute", "author", "average",
				"avg", "be", "before", "Boolean", "call", "ceiling", "characters", "citations", "conclude", "cos",
				"cosine", "count", "clone", "currenttime", "data", "data_driven", "data-driven", "date", "day", "days",
				"decrease", "delay", "destination", "do", "duration", "earliest", "else", "elseif", "enddo", "endif",
				"end", "eq", "equal", "event", "eventtime", "every", "evoke", "exist", "exists", "exp", "expired",
				"explanation", "extract", "false", "filename", "find", "first", "floor", "following", "for",
				"formatted", "from", "ge", "greater", "gt", "hour", "hours", "if", "in", "include", "increase", "index",
				"institution", "int", "interface", "interval", "is", "it", "keywords", "knowledge", "last", "latest",
				"le", "left", "length", "less", "let", "library", "links", "list", "log", "log10", "logic", "lowercase",
				"lt", "maintenance", "matches", "max", "maximum", "median", "merge", "message", "min", "minimum",
				"minute", "minutes", "mlm", "mlmname", "mlm_self", "month", "months", "names", "ne", "nearest", "new",
				"no", "not", "now", "null", "number", "object", "occur", "occurred", "occurs", "of", "or", "past",
				"pattern", "percent", "preceding", "present", "priority", "production", "purpose", "read", "refute",
				"research", "return", "reverse", "right", "round", "same", "second", "seconds", "seqto", "sin", "sine",
				"slope", "sort", "specialist", "sqrt", "starting", "stddev", "string", "substring", "sum", "support",
				"surrounding", "tan", "tangent", "testing", "than", "the", "then", "they", "time", "title", "to",
				"triggertime", "trim", "true", "truncate", "type", "unique", "until", "uppercase", "urgency",
				"validation", "variance", "version", "was", "week", "weeks", "were", "where", "while", "with", "within",
				"write", "year", "years"
		};
		for (String reservedWord : reservedWords) {
			assertInvalidIdentifier(reservedWord);
		}
		
		// words reserved for future use
		String[] reservedFutureWords = {
				"union", "intersect", "excluding", "citation", "select"
		};
		for (String reservedWord : reservedFutureWords) {
			assertInvalidIdentifier(reservedWord);
		}
		
		// the
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
		
		String case_ = new ArdenCodeBuilder()
				.addData("AbCd := 5;")
				.addAction("abcd := ABCD + 5;")
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
		// date
		assertValidStatement("t := 1990-01-01;");
		
		// datetime
		assertValidStatement("t := 1990-01-01T12:00:00;");
		
		// fractional seconds
		assertValidStatement("t := 1990-01-01T12:00:00.15;");
		
		// timezones
		assertValidStatement("t := 1990-01-01T12:00:00.15Z;");
		assertValidStatement("t := 1990-01-01T12:00:00.15-05:00;");
		
		// fractionals seconds + timezones
		assertValidStatement("t := 1990-01-01T12:00:00.12345+01:00;");
		
		// case insensitive
		assertValidStatement("t := 1990-01-01t12:00:00;");
		assertValidStatement("t := 1990-01-01T12:00:00.15z;");
		
		// construction
		assertValidStatement("t := 1990-01-01 + 5 years + (6-3)months + 123 days;");
		assertEvaluatesTo("1800-01-01 + (1993-1800)years + (5-1)months + (17-1)days", "1993-05-17T00:00:00");
	}

	@Test
	public void testStringConstants() throws Exception {
		// no length limit
		assertValidStatement("x := \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque libero felis, bibendum at ullamcorper ac, dictum eget eros.\";"); 
		
		// interalQuotationMarks
		assertValidStatement("x := \"Lorem \"\"ipsum\"\" dolor sit amet\";");
		
		// single line break
		assertEvaluatesTo("\"Lorem     \n    ipsum\"", "\"Lorem ipsum\"");
		
		// multi line break
		assertEvaluatesTo("\"Lorem     \n\n\n\n    ipsum\"", "\"Lorem\nipsum\"");
		
		// empty string
		assertEvaluatesTo("\"\"", "\"\"");
	}
	
	@Test
	public void testTermConstant() throws Exception {
		String term = new ArdenCodeBuilder().addData("x := MLM 'other_mlm';").toString();
		assertValid(term);
	}

	@Test
	public void testMappingClauses() throws Exception {
		// READ only in data slot
		String mapping = new ArdenCodeBuilder().addData("x := READ {Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque libero felis, bibendum at ullamcorper ac, dictum eget eros.};").toString();
		assertValid(mapping);
	}

	@Test
	public void testComments() throws Exception {
		assertValidStatement("/* Lorem /* ipsum // */");
		assertValidStatement("// Lorem // */ ipsum \n");
		assertValidStatement("a/**/:=/*xyz*/5;");
	}

	@Test
	public void testWhiteSpace() throws Exception {
		assertValidStatement("x := 3+4;");
		assertValidStatement("x := 3 + 4;");
	}
}
