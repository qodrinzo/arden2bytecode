package arden.tests.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.internal.ArrayComparisonFailure;

import arden.compiler.CompiledMlm;
import arden.compiler.Compiler;
import arden.runtime.ArdenTime;
import arden.runtime.MedicalLogicModule;

public abstract class ImplementationTest {
	private static Calendar calendar = null;
	
	static ArdenTime createDate(int year, int month, int day) {
		if (calendar == null) {
			calendar = new GregorianCalendar();
		}
		calendar.clear();
		calendar.set(year, month, day);
		return new ArdenTime(calendar.getTimeInMillis());
	}
	
	static ArdenTime createDateTime(int year, int month, int day, int hour, int minutes, int seconds) {
		if (calendar == null) {
			calendar = new GregorianCalendar();
		}
		calendar.clear();
		calendar.set(year, month, day, hour, minutes, seconds);
		return new ArdenTime(calendar.getTimeInMillis());
	}
	
	protected static String inputStreamToString(InputStream in) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		StringBuilder stringBuilder = new StringBuilder();
	
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append("\n");
		}
	
		bufferedReader.close();
		return stringBuilder.toString();
	}

	protected static MedicalLogicModule compile(String filename) throws Exception {
		Compiler c = new Compiler();
		c.enableDebugging(filename);
		CompiledMlm mlm = c
				.compileMlm(new InputStreamReader(ImplementationTest.class.getResourceAsStream(filename)));
		return mlm;
	}
	
	protected static void assertArrayNotEquals(Object[] expecteds, Object[] actuals) throws AssertionError {
		boolean exceptionThrown = false;
		try {
			Assert.assertArrayEquals(expecteds, actuals);
		} catch (ArrayComparisonFailure f) {
			exceptionThrown = true;
		}
		if (!exceptionThrown) {
			throw new AssertionError("Array is same.");
		}
	}
	
}
