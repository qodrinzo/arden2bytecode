package arden.constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Stack;

import arden.constants.analysis.DepthFirstAdapter;
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
import arden.constants.node.TArdenDate;
import arden.constants.node.TArdenDateTime;
import arden.constants.node.TArdenNumber;
import arden.constants.node.TArdenString;
import arden.runtime.ArdenBoolean;
import arden.runtime.ArdenList;
import arden.runtime.ArdenNull;
import arden.runtime.ArdenNumber;
import arden.runtime.ArdenString;
import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.runtime.ExpressionHelpers;

class ConstantVisitor extends DepthFirstAdapter {
	Stack<ArdenValue> stack = new Stack<>();
	long primaryTime = System.currentTimeMillis();

	private static boolean isWhitespace(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}
	
	private static String parseString(TArdenString literal) throws ConstantParserException {
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

	private static long parseIsoDateTime(TArdenDateTime dateTime) throws ConstantParserException {
		/*
		 * SimpleDateFormat is very bad at parsing ISO 8601. Therefore we
		 * change the text to a parsable format by extracting the fractional
		 * seconds and changing the timezone part.
		 */

		// allow lowercase 't' and 'z'
		String text = dateTime.getText().toUpperCase();

		StringBuilder parsableText = new StringBuilder(text);
		DateFormat format;
		long millis = 0;

		// parse fractional seconds if present
		int millisPos = ArdenTime.isoDateTimeLength;
		if (millisPos < text.length() && text.charAt(millisPos) == '.') {
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
		if (timezonePos < parsableText.length()) {
			// has timezone
			format = ArdenTime.isoDateTimeFormatWithGmtTimeZone;
			switch (parsableText.charAt(timezonePos)) {
			case 'Z':
				// 2000-01-01T00:00:00Z -> 2000-01-01T00:00:00GMT-00:00
				parsableText.replace(timezonePos, timezonePos + 1, "GMT-00:00");
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

	private static long parseIsoDate(TArdenDate isoDate) throws ConstantParserException {
		try {
			return ArdenTime.isoDateFormat.parse(isoDate.getText()).getTime();
		} catch (ParseException e) {
			throw new ConstantParserException(isoDate, e.getMessage());
		}
	}
	
	private static double parseNumber(TArdenNumber ardenNumber) throws ConstantParserException {
		double d = Double.NaN;
		try {
			d = Double.parseDouble(ardenNumber.getText());
		} catch (NumberFormatException e) {
			throw new ConstantParserException(ardenNumber, "Not a valid number");
		}
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			throw new ConstantParserException(ardenNumber, "Not a valid number");
		}
		return d;
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
		try {
			String string = parseString(node.getArdenString());
			stack.push(new ArdenString(string, primaryTime));
		} catch (ConstantParserException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void caseANumberAtom(ANumberAtom node) {
		try {
			double value = parseNumber(node.getArdenNumber());
			stack.push(ArdenNumber.create(value, primaryTime));
		} catch (ConstantParserException e) {
			throw new RuntimeException(e);
		}
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
		try {
			long millis = parseIsoDate(node.getArdenDate());
			stack.push(new ArdenTime(millis, primaryTime));
		} catch (ConstantParserException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void caseADtimeAtom(ADtimeAtom node) {
		try {
			long millis = parseIsoDateTime(node.getArdenDateTime());
			stack.push(new ArdenTime(millis, primaryTime));
		} catch (ConstantParserException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void caseAParAtom(AParAtom node) {
		stack.push(ArdenList.EMPTY);
	}

	public ArdenValue getResult() {
		if (stack.size() != 1) {
			throw new RuntimeException(new ConstantParserException("Input is malformed in terms of parentheses."));
		}
		return stack.peek();
	}
}