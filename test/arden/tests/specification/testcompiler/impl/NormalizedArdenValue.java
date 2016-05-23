package arden.tests.specification.testcompiler.impl;

import arden.runtime.ArdenDuration;
import arden.runtime.ArdenList;
import arden.runtime.ArdenNumber;
import arden.runtime.ArdenObject;
import arden.runtime.ArdenTime;
import arden.runtime.ArdenValue;
import arden.tests.specification.testcompiler.TestCompilerResult;

/**
 * Wrapper to correctly print the normalized internal representation of an ArdenValue. <br>
 * e.g. "1 day" -> "86400 seconds"
 * @see TestCompilerResult
 */
class NormalizedArdenValue extends ArdenValue {
	private ArdenValue value;

	public NormalizedArdenValue(ArdenValue value) {
		this.value = value;
	}

	@Override
	public ArdenValue setTime(long newPrimaryTime) {
		// not used
		return null;
	}
	
	@Override
	public String toString() {
		if (value instanceof ArdenList) {
			// convert child elements
			ArdenList list = (ArdenList) value;
			for (int i = 0; i < list.values.length; i++) {
				list.values[i] = new NormalizedArdenValue(list.values[i]);
			}
			return list.toString();
		} else if (value instanceof ArdenObject) {
			// convert fields
			ArdenObject object = (ArdenObject) value;
			for (int i = 0; i < object.fields.length; i++) {
				object.fields[i] = new NormalizedArdenValue(object.fields[i]);
			}
			return object.toString();
		} else if (value instanceof ArdenDuration) {
			// use only internal representation (seconds, months)
			ArdenDuration duration = (ArdenDuration) value;
			String unit = duration.isMonths? "months" : "seconds";
			if (duration.value == 1)
				return "1 " + unit.substring(0, unit.length() - 1);
			else
				return ArdenNumber.toString(duration.value) + " " + unit;
		} else if (value instanceof ArdenTime) {
			// remove trailing zeros
			ArdenTime time = (ArdenTime) value;
			if(time.value % 1000 != 0) {
				return time.toString().replaceAll("0*$", "");
			}
		}
		return value.toString();
	}
	
}