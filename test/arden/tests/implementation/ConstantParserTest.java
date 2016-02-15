package arden.tests.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import arden.constants.ConstantParser;
import arden.runtime.ArdenList;
import arden.runtime.ArdenNumber;
import arden.runtime.ArdenString;
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
	public void testSingleElementList() throws Exception {
		ArdenList list = new ArdenList(new ArdenValue[]{
				ArdenNumber.create(1.0, ArdenValue.NOPRIMARYTIME)});
		assertEquals(list, ConstantParser.parse("(,1)"));
	}
	
	@Test
	public void testList() throws Exception {
		ArdenList list = new ArdenList(new ArdenValue[]{
				ArdenNumber.create(1.0, ArdenValue.NOPRIMARYTIME),
				ArdenNumber.create(2.0, ArdenValue.NOPRIMARYTIME),
				ArdenNumber.create(3.0, ArdenValue.NOPRIMARYTIME)
		});
		assertEquals(list, ConstantParser.parse("(1,2,3)"));
	}
	
	@Test
	public void testListConcat() throws Exception {
		ArdenList list = new ArdenList(new ArdenValue[]{
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
	
}
