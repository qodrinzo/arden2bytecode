package arden.constants;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;

import arden.constants.analysis.DepthFirstAdapter;
import arden.constants.lexer.Lexer;
import arden.constants.lexer.LexerException;
import arden.constants.node.ADateAtom;
import arden.constants.node.ADtimeAtom;
import arden.constants.node.AFalseArdenBoolean;
import arden.constants.node.AListExpr;
import arden.constants.node.AListatomExpr;
import arden.constants.node.ANullAtom;
import arden.constants.node.ANumberAtom;
import arden.constants.node.AParAtom;
import arden.constants.node.AStringAtom;
import arden.constants.node.ATrueArdenBoolean;
import arden.constants.node.Start;
import arden.constants.node.TArdenDate;
import arden.constants.node.TArdenDateTime;
import arden.constants.node.TArdenString;
import arden.constants.node.Token;
import arden.constants.parser.Parser;
import arden.constants.parser.ParserException;
import arden.runtime.ArdenBoolean;
import arden.runtime.ArdenList;
import arden.runtime.ArdenNull;
import arden.runtime.ArdenNumber;
import arden.runtime.ArdenString;
import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExpressionHelpers;

public class ConstantParser {
	
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
	
	public ConstantParser() {
	}
	
	private class AstVisitor extends DepthFirstAdapter {
		Deque<ArdenValue> stack;
		ArdenValue result;
		long primaryTime;
		
		private boolean isWhitespace(char c) {
			return c == ' ' || c == '\t' || c == '\n' || c == '\r';
		}
		
		public String parseString(TArdenString literal) throws ConstantParserException {
			String input = literal.getText();
			if (input.length() < 2 || input.charAt(0) != '"' || input.charAt(input.length() - 1) != '"')
				throw new ConstantParserException(literal, "Invalid string literal");
			StringBuilder output = new StringBuilder();
			for (int i = 1; i < input.length() - 1; i++) {
				char c = input.charAt(i);
				if (c == '\r' || c == '\n') {
					// (see spec for special rules in this case)
					while (output.length() > 0 && isWhitespace(output.charAt(output.length() - 1)))
						output.deleteCharAt(output.length() - 1);
					int numLineFeed = 0;
					while (isWhitespace(c)) {
						if (c == '\n')
							numLineFeed++;
						c = input.charAt(++i);
					}
					if (numLineFeed > 1)
						output.append('\n');
					else
						output.append(' ');
				}
				if (c == '"') {
					i += 1;
					if (input.charAt(i) != '"')
						throw new ConstantParserException(literal, "Invalid string literal");
				}
				output.append(c);
			}
			return output.toString();
		}
		
		public long parseIsoDateTime(TArdenDateTime dateTime) throws ConstantParserException {
			/*
			 * SimpleDateFormat is very bad at parsing ISO 8601. Therefore we
			 * change the text to a parsable format by extracting the fractional
			 * seconds and changing the timezone part.
			 */
			String text = dateTime.getText().toUpperCase(); // allow lowercase 't' and 'z'
			StringBuilder parsableText = new StringBuilder(text);
			DateFormat format;
			long millis = 0;
			
			// parse fractional seconds if present
			int millisPos = ArdenTime.isoDateTimeLength;
			if(millisPos < text.length() && text.charAt(millisPos) == '.') {
				int multiplier = 100;
				millisPos++;
				while (millisPos < text.length()) {
					char c = text.charAt(millisPos);
					boolean isDigit = c >= '0' && c <= '9';
					if (isDigit) {
						millis += (c - '0') * multiplier;
						multiplier /= 10;
						millisPos++;
					} else {
						// timezone follows
						break;
					}
				}
				// remove fractional seconds part
				parsableText.delete(ArdenTime.isoDateTimeLength, millisPos);
			}
			
			// parse timezone if present
			int timezonePos = ArdenTime.isoDateTimeLength;
			if(timezonePos < parsableText.length()) {
				// has timezone
				format = ArdenTime.isoDateTimeFormatWithGmtTimeZone;
				switch (parsableText.charAt(timezonePos)) {
				case 'Z':
					// 2000-01-01T00:00:00Z -> 2000-01-01T00:00:00GMT-00:00
					parsableText.replace(timezonePos, timezonePos+1, "GMT-00:00");
					break;
				case '+': // fall through
				case '-':
					// 2000-01-01T00:00:00+01:00 -> 2000-01-01T00:00:00GMT+01:00 
					parsableText.insert(timezonePos, "GMT");
					break;
				default:
					throw new ConstantParserException(dateTime, "Invalid DateTime literal");
				}
			} else {
				// no timezone
				format = ArdenTime.isoDateTimeFormat;
			}
			
			Date date;
			try {
				date = format.parse(parsableText.toString());
			} catch (ParseException e) {
				throw new ConstantParserException(dateTime, "Invalid DateTime literal");
			}
			return date.getTime() + millis;
		}

		public long parseIsoDate(TArdenDate isoDate) throws ConstantParserException {
			try {
				return ArdenTime.isoDateFormat.parse(isoDate.getText()).getTime();
			} catch (ParseException e) {
				throw new ConstantParserException(e.getMessage());
			}
		}
		
		public AstVisitor() {
			stack = new LinkedList<ArdenValue>();
			primaryTime = System.currentTimeMillis();
			result = null;
		}
		
		@Override
		public void caseATrueArdenBoolean(ATrueArdenBoolean node) {
			stack.push(ArdenBoolean.create(true, primaryTime));
		}
		
		@Override
		public void caseAFalseArdenBoolean(AFalseArdenBoolean node) {
			stack.push(ArdenBoolean.create(false, primaryTime));
		}
		
		@Override
		public void caseAStringAtom(AStringAtom node) {
			TArdenString string = node.getArdenString();
			try {
				stack.push(new ArdenString(parseString(string), primaryTime));
			} catch (ConstantParserException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public void caseANumberAtom(ANumberAtom node) {
			String numStr = node.getArdenNumber().getText();
			double d = Double.NaN;
			try {
				d = Double.parseDouble(numStr);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Not a valid number at pos: " + node.getArdenNumber().getPos());
			}
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				throw new RuntimeException("Not a valid number at pos: "  + node.getArdenNumber().getPos());
			}
			stack.push(ArdenNumber.create(d, primaryTime));
		}
		
		@Override
		public void caseANullAtom(ANullAtom node) {
			stack.push(ArdenNull.create(primaryTime));
		}
		
		@Override
		public void caseAListatomExpr(AListatomExpr node) {
			node.getAtom().apply(this);
			stack.push(ExpressionHelpers.unaryComma(stack.pop()));
		}
		
		@Override
		public void caseAListExpr(AListExpr node) {
			node.getExpr().apply(this);
			node.getAtom().apply(this);
			ArdenValue atomValue = stack.pop();
			stack.push(ExpressionHelpers.binaryComma(stack.pop(), atomValue));
		}
		
		@Override
		public void caseADateAtom(ADateAtom node) {
			TArdenDate date = node.getArdenDate();
			try {
				stack.push(new ArdenTime(parseIsoDate(date), primaryTime));
			} catch (ConstantParserException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public void caseADtimeAtom(ADtimeAtom node) {
			TArdenDateTime dateTime = node.getArdenDateTime();
			try {
				stack.push(new ArdenTime(parseIsoDateTime(dateTime), primaryTime));
			} catch (ConstantParserException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public void caseAParAtom(AParAtom node) {
			stack.push(ArdenList.EMPTY);
		}
		
		@Override
		public void outStart(Start node) {
			if (stack.size() != 1) {
				throw new RuntimeException("Input is malformed in terms of parentheses.");
			}
			result = stack.getFirst();
		}
		
		public ArdenValue getResult() {
			return result;
		}
	}
	
	public static ArdenValue parse(String input) throws ConstantParserException {
		return new ConstantParser().doParse(input);
	}
	
	public ArdenValue doParse(String input) throws ConstantParserException {		
		if (input == null) {
			return ArdenNull.create(System.currentTimeMillis());
		}
		StringReader reader = new StringReader(input);

		Lexer lexer = new Lexer(new PushbackReader(reader, 256));
		Parser parser = new Parser(lexer);
		Start syntaxTree;
		try {
			syntaxTree = parser.parse();
		} catch (ParserException e) {
			throw new ConstantParserException(e);
		} catch (LexerException e) {
			throw new ConstantParserException(e);
		} catch (IOException e) {
			throw new ConstantParserException(e);
		}
		AstVisitor visitor = new AstVisitor(); 
		syntaxTree.apply(visitor);
		return visitor.getResult();
	}
}
