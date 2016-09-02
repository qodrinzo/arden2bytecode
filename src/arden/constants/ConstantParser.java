package arden.constants;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import arden.constants.lexer.Lexer;
import arden.constants.lexer.LexerException;
import arden.constants.node.Node;
import arden.constants.node.Start;
import arden.constants.parser.Parser;
import arden.constants.parser.ParserException;
import arden.runtime.ArdenList;
import arden.runtime.ArdenNull;
import arden.runtime.ArdenValue;

public class ConstantParser {

	/**
	 * Translates code for an Arden Syntax constant to an {@link ArdenValue}.
	 * The constant may e.g. be a number, a string (surrounded by double quotes)
	 * or a list (with or without parentheses).
	 * 
	 * @param input
	 *            A single Arden Syntax constant
	 * @return The constant translated to an ArdenValue
	 */
	public static ArdenValue parse(String input) throws ConstantParserException {
		if (input == null) {
			return ArdenNull.create(System.currentTimeMillis());
		}

		try {
			Start syntaxTree = parseAST(input);
			ConstantVisitor visitor = new ConstantVisitor();
			syntaxTree.apply(visitor);
			return visitor.getResult();
		} catch (RuntimeException e) {
			throw (ConstantParserException) e.getCause();
		}
	}

	/**
	 * Translates a comma separated list of Arden Syntax constants to
	 * {@link ArdenValue}s. Constants with parentheses are considered as a
	 * single {@link ArdenList}. This is equivalent to how the Arden Syntax
	 * RETURN statement handles multi-returns and lists.
	 * 
	 * @param input
	 *            Arden Syntax constants
	 * @return The constants translated to ArdenValues
	 * @throws ConstantParserException
	 */
	public static ArdenValue[] parseMultiple(String input) throws ConstantParserException {
		if (input == null) {
			return new ArdenValue[] { ArdenNull.create(System.currentTimeMillis()) };
		}

		Start syntaxTree = parseAST(input);

		MultiVisitor visitor = new MultiVisitor();
		try {
			syntaxTree.apply(visitor);
			return visitor.getResult();
		} catch (RuntimeException e) {
			throw (ConstantParserException) e.getCause();
		}
	}

	public static Start parseAST(String input) throws ConstantParserException {
		StringReader reader = new StringReader(input);
		Lexer lexer = new Lexer(new PushbackReader(reader, 256));
		Parser parser = new Parser(lexer);
		try {
			return parser.parse();
		} catch (ParserException e) {
			throw new ConstantParserException(e);
		} catch (LexerException e) {
			throw new ConstantParserException(e);
		} catch (IOException e) {
			throw new ConstantParserException(e);
		}
	}
	
	public static ArdenValue parseValue(Node node) {
		ConstantVisitor visitor = new ConstantVisitor();
		node.apply(visitor);
		return visitor.getResult();
	}

}
