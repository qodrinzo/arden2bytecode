package arden.constants;

import java.io.IOException;

import arden.constants.lexer.LexerException;
import arden.constants.node.Token;
import arden.constants.parser.ParserException;

public class ConstantParserException extends Exception {
	private static final long serialVersionUID = 1L;
	int line;
	int pos;

	public ConstantParserException(ParserException e) {
		super(e);
		if (e.getToken() != null) {
			line = e.getToken().getLine();
			pos = e.getToken().getPos();
		} else {
			line = pos = -1;
		}
	}

	public ConstantParserException(LexerException e) {
		super(e);
		line = pos = -1;
	}

	public ConstantParserException(IOException e) {
		super(e);
		line = pos = -1;
	}

	public ConstantParserException(String message) {
		super(message);
		line = pos = -1;
	}

	public ConstantParserException(Token token, String message) {
		super(message);
		line = token.getLine();
		pos = token.getPos();
	}

	public int getLine() {
		return line;
	}

	public int getPos() {
		return pos;
	}
}