// arden2bytecode
// Copyright (c) 2010, Daniel Grunwald
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification, are
// permitted provided that the following conditions are met:
//
// - Redistributions of source code must retain the above copyright notice, this list
//   of conditions and the following disclaimer.
//
// - Redistributions in binary form must reproduce the above copyright notice, this list
//   of conditions and the following disclaimer in the documentation and/or other materials
//   provided with the distribution.
//
// - Neither the name of the owner nor the names of its contributors may be used to
//   endorse or promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS &AS IS& AND ANY EXPRESS
// OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
// AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
// CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
// IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
// OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package arden.compiler;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import arden.compiler.node.ACommaExpr;
import arden.compiler.node.AExsortExpr;
import arden.compiler.node.AMappingFactor;
import arden.compiler.node.ASortExpr;
import arden.compiler.node.PExpr;
import arden.compiler.node.PExprSort;
import arden.compiler.node.PMappingFactor;
import arden.compiler.node.TIsoDate;
import arden.compiler.node.TIsoDateTime;
import arden.compiler.node.TNumberLiteral;
import arden.compiler.node.TStringLiteral;
import arden.compiler.node.TTerm;
import arden.runtime.ArdenTime;

/**
 * Static methods that do help using the parse tree.
 * 
 * @author Daniel Grunwald
 */
final class ParseHelpers {
	/** Returns the value represented by a string literal. */
	public static String getLiteralStringValue(TStringLiteral literal) {
		String input = literal.getText();
		if (input.length() < 2 || input.charAt(0) != '"' || input.charAt(input.length() - 1) != '"')
			throw new RuntimeCompilerException(literal, "Invalid string literal");
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
					throw new RuntimeCompilerException(literal, "Invalid string literal");
			}
			output.append(c);
		}
		return output.toString();
	}

	private static boolean isWhitespace(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}

	public static String getStringForMapping(PMappingFactor mapping) {
		return ((AMappingFactor) mapping).getDataMapping().getText();
	}

	public static double getLiteralDoubleValue(TNumberLiteral number) {
		String input = number.getText();
		double d;
		try {
			d = Double.parseDouble(input);
		} catch (NumberFormatException e) {
			throw new RuntimeCompilerException(e.getMessage());
		}
		if (Double.isInfinite(d) || Double.isNaN(d))
			throw new RuntimeCompilerException(number, "Invalid number literal: " + input);
		return d;
	}
	
	public static long parseIsoDateTime(TIsoDateTime dateTime) {
		/*
		 * SimpleDateFormat is very bad at parsing ISO 8601. Therefore we change
		 * the text to a parsable format by extracing the fractional seconds and
		 * changing the timezone part.
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
				// 2000-01-01T00:00:00Z -> 2000-01-01T00:00:00ZGMT-00:00
				parsableText.replace(timezonePos, timezonePos+1, "GMT-00:00");
				break;
			case '+': // fall through
			case '-':
				// 2000-01-01T00:00:00+01:00 -> 2000-01-01T00:00:00GMT+01:00 
				parsableText.insert(timezonePos, "GMT");
				break;
			default:
				throw new RuntimeCompilerException(dateTime, "Invalid DateTime literal");
			}
		} else {
			// no timezone
			format = ArdenTime.isoDateTimeFormat;
		}
		
		Date date;
		try {
			date = format.parse(parsableText.toString());
		} catch (ParseException e) {
			throw new RuntimeCompilerException(dateTime, "Invalid DateTime literal");
		}
		return date.getTime() + millis;
	}

	public static long parseIsoDate(TIsoDate date) {
		try {
			return ArdenTime.isoDateFormat.parse(date.getText()).getTime();
		} catch (ParseException e) {
			throw new RuntimeCompilerException(e.getMessage());
		}
	}

	/** Returns the list of comma-separated terms in the expression */
	public static List<PExprSort> toCommaSeparatedList(PExpr expr) {
		// expr =
		// {sort} expr_sort
		// | {exsort} expr comma expr_sort
		// | {comma} comma expr_sort;
		final ArrayList<PExprSort> output = new ArrayList<PExprSort>();
		expr.apply(new VisitorBase() {
			@Override
			public void caseASortExpr(ASortExpr node) {
				output.add(node.getExprSort());
			};

			@Override
			public void caseAExsortExpr(AExsortExpr node) {
				node.getExpr().apply(this);
				output.add(node.getExprSort());
			}

			@Override
			public void caseACommaExpr(ACommaExpr node) {
				output.add(node.getExprSort());
			}
		});
		return output;
	}

	public static String getMlmName(TTerm term) {
		String input = term.getText();
		if (input.length() < 2 || input.charAt(0) != '\'' || input.charAt(input.length() - 1) != '\'')
			throw new RuntimeCompilerException(term, "Invalid term");
		return input.substring(1, input.length() - 1);
	}
}
