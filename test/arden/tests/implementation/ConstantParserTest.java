package arden.tests.implementation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import arden.constants.ConstantParser;
import arden.runtime.ArdenList;
import arden.runtime.ArdenNumber;
import arden.runtime.ArdenString;
import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;

public class ConstantParserTest extends ImplementationTest {

	@Test
	public void testEmptyList() throws Exception {
		assertEquals(ArdenList.EMPTY, ConstantParser.parse("()"));
	}

	@Test
	public void testNumber() throws Exception {
		assertEquals(ArdenNumber.create(5.0, ArdenValue.NOPRIMARYTIME), ConstantParser.parse("5.0"));
		assertFalse(ConstantParser.parse("5.0").equals(ArdenNumber.create(4.0, ArdenValue.NOPRIMARYTIME)));
	}
	
	@Test
	public void testSign() throws Exception {
		assertEquals(ArdenNumber.create(-5.0, ArdenValue.NOPRIMARYTIME), ConstantParser.parse("-5.0"));
		assertEquals(ArdenNumber.create(+5.0, ArdenValue.NOPRIMARYTIME), ConstantParser.parse("+5.0"));
		assertFalse(ConstantParser.parse("-5.0").equals(ArdenNumber.create(+5.0, ArdenValue.NOPRIMARYTIME)));
	}

	@Test
	public void testSingleElementList() throws Exception {
		ArdenList list = new ArdenList(new ArdenValue[] { ArdenNumber.create(1.0, ArdenValue.NOPRIMARYTIME) });
		assertEquals(list, ConstantParser.parse("(,1)"));
	}

	@Test
	public void testList() throws Exception {
		ArdenList list = new ArdenList(new ArdenValue[] {
				ArdenNumber.create(1.0, ArdenValue.NOPRIMARYTIME),
				ArdenNumber.create(2.0, ArdenValue.NOPRIMARYTIME),
				ArdenNumber.create(3.0, ArdenValue.NOPRIMARYTIME)
		});
		assertEquals(list, ConstantParser.parse("(1,2,3)"));
	}

	@Test
	public void testListConcat() throws Exception {
		ArdenList list = new ArdenList(new ArdenValue[] {
				ArdenNumber.create(1.0, ArdenValue.NOPRIMARYTIME),
				ArdenNumber.create(2.1, ArdenValue.NOPRIMARYTIME),
				ArdenNumber.create(2.2, ArdenValue.NOPRIMARYTIME),
				ArdenNumber.create(2.3, ArdenValue.NOPRIMARYTIME),
				ArdenNumber.create(3.0, ArdenValue.NOPRIMARYTIME)
		});
		assertEquals(list, ConstantParser.parse("(1,(2.1,2.2,2.3),3)"));
	}

	@Test
	public void testString() throws Exception {
		assertEquals(new ArdenString("vfs\"dkj"), ConstantParser.parse("\"vfs\"\"dkj\""));
	}

	@Test
	public void testDate() throws Exception {
		Calendar calendar = Calendar.getInstance(); // local timezone
		calendar.set(1989, Calendar.AUGUST, 8, 0, 0, 0);
		calendar.clear(Calendar.MILLISECOND);

		ArdenTime ardenTime = new ArdenTime(calendar.getTimeInMillis());
		String isoString = ArdenTime.isoDateFormat.format(calendar.getTime());

		assertEquals(ardenTime, ConstantParser.parse(isoString));
	}

	@Test
	public void testDatetime() throws Exception {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5"));
		calendar.set(1989, Calendar.AUGUST, 8, 1, 2, 3);
		calendar.set(Calendar.MILLISECOND, 45);

		ArdenTime ardenTime = new ArdenTime(calendar.getTimeInMillis());

		DateFormat format = (DateFormat) ArdenTime.isoDateTimeFormatWithMillis.clone();
		format.setTimeZone(TimeZone.getTimeZone("GMT+5"));
		String isoString = format.format(calendar.getTimeInMillis());
		isoString += "+05:00";

		assertEquals(ardenTime, ConstantParser.parse(isoString));
	}

	@Test
	public void testMuliReturn() throws Exception {
		Calendar calendar = Calendar.getInstance(); // local timezone
		calendar.set(1989, Calendar.AUGUST, 5, 0, 0, 0);
		calendar.clear(Calendar.MILLISECOND);
		ArdenTime ardenTime = new ArdenTime(calendar.getTimeInMillis());
		String isoString = ArdenTime.isoDateFormat.format(calendar.getTime());

		ArdenValue[] returns = new ArdenValue[] { new ArdenNumber(1), new ArdenString("asdf"), ardenTime };
		assertArrayEquals(returns, ConstantParser.parseMultiple("1, \"asdf\", " + isoString));
	}

}
