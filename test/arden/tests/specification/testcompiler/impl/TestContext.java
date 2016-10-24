package arden.tests.specification.testcompiler.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import arden.compiler.CompiledMlm;
import arden.runtime.ArdenEvent;
import arden.runtime.ArdenList;
import arden.runtime.ArdenNumber;
import arden.runtime.ArdenRunnable;
import arden.runtime.ArdenString;
import arden.runtime.ArdenValue;
import arden.runtime.DatabaseQuery;
import arden.runtime.ExecutionContext;
import arden.runtime.MaintenanceMetadata.Validation;
import arden.tests.specification.testcompiler.TestCompiler;

/**
 * See the requirements at the {@link TestCompiler} interface.
 */
public class TestContext extends ExecutionContext {
	static final String INTERFACE_MAPPING = "test interface";
	static final String MESSAGE_MAPPING = "test message";
	static final String READ_MAPPING = "select id from database";
	static final String READ_MULTIPLE_MAPPING = "select id,value from database";
	static final String DESTINATION_MAPPING = "dest";
	
	// TODO DatabaseQuery should automatically sort by time
	private final ArdenValue[] values1 = new ArdenValue[] {
			ArdenNumber.create(5, new GregorianCalendar(1970,Calendar.JANUARY,1).getTimeInMillis()),
			ArdenNumber.create(3, new GregorianCalendar(1990,Calendar.JANUARY,1).getTimeInMillis()),
			ArdenNumber.create(2, new GregorianCalendar(1990,Calendar.JANUARY,2).getTimeInMillis()),
			ArdenNumber.create(4, new GregorianCalendar(1990,Calendar.JANUARY,3).getTimeInMillis()),
			ArdenNumber.create(1, new GregorianCalendar(2000,Calendar.JANUARY,1).getTimeInMillis())
	};
	private final ArdenValue[] values2 = new ArdenValue[] {
			new ArdenString("e", new GregorianCalendar(1970,Calendar.JANUARY,1).getTimeInMillis()),
			new ArdenString("c", new GregorianCalendar(1990,Calendar.JANUARY,1).getTimeInMillis()),
			new ArdenString("b", new GregorianCalendar(1990,Calendar.JANUARY,2).getTimeInMillis()),
			new ArdenString("d", new GregorianCalendar(1990,Calendar.JANUARY,3).getTimeInMillis()),
			new ArdenString("a", new GregorianCalendar(2000,Calendar.JANUARY,1).getTimeInMillis())
	};
	
	private List<String> messages = new LinkedList<String>();
	private List<CompiledMlm> mlms;
	private String institutionSelf;
	
	public TestContext(List<CompiledMlm> mlms, String institutionSelf) {
		this.mlms = mlms;
		this.institutionSelf = institutionSelf;
	}
	
	@Override
	public void callWithDelay(ArdenRunnable mlm, ArdenValue[] arguments, ArdenValue delay) {
		// run immediately
		try {
			mlm.run(this, arguments);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public ArdenRunnable findModule(String name, String institution) {
		// TODO use mlm search algorithm from BaseExecutionContext
		institution = institution == null ? "" : institution.trim();
		name = name.trim();
		boolean skipValidation = !institution.isEmpty();
		
		// find mlms with matching institution and name
		List<CompiledMlm> rightNameAndInstitution = new ArrayList<CompiledMlm>();
		if (institution.isEmpty()) {
			institution = this.institutionSelf;
		}
		for (CompiledMlm mlm : mlms) {
			String curMlmname = mlm.getMaintenance().getMlmName().trim();
			String curInstitution = mlm.getMaintenance().getInstitution().trim();
			if(curInstitution.equals(institution) && curMlmname.equals(name)) {
				rightNameAndInstitution.add(mlm);
			}
		}
		
		if(rightNameAndInstitution.isEmpty()) {
			throw new IllegalArgumentException("Module \""+name+" not found");
		}
		if(rightNameAndInstitution.size() == 1 || skipValidation) {
			return rightNameAndInstitution.get(0);
		} 

		// compare by validation
		CompiledMlm bestMatch = rightNameAndInstitution.get(0);
		for (CompiledMlm mlm : rightNameAndInstitution) {
			bestMatch = findBetterValidation(bestMatch, mlm);
		}
		return bestMatch;
	}

	private CompiledMlm findBetterValidation(CompiledMlm mlm1, CompiledMlm mlm2) {
		Validation v1 = mlm1.getMaintenance().getValidation();
		Validation v2 = mlm2.getMaintenance().getValidation();
		return v1.compareTo(v2) > 0 ? mlm1 : mlm2;
	}

	
	@Override
	public ArdenRunnable findInterface(String mapping) {
		if(INTERFACE_MAPPING.equals(mapping)) {
			return new ArdenRunnable() {
				
				@Override
				public ArdenValue[] run(ExecutionContext context, ArdenValue[] arguments) throws InvocationTargetException {
					// 	 RETURN (args[0] + args[1], args[0] * args[1]);
					ArdenNumber firstArg = (ArdenNumber) arguments[0];
					ArdenNumber secondArg = (ArdenNumber) arguments[1];
					ArdenNumber firstReturn = new ArdenNumber(firstArg.value + secondArg.value);
					ArdenNumber secondReturn = new ArdenNumber(firstArg.value * secondArg.value);
					return new ArdenValue[] {firstReturn, secondReturn}; 
				}
			};
			
		}
		return super.findInterface(mapping);
	}
	
	@Override
	public ArdenEvent getEvent(String mapping) {
		return new ArdenEvent(mapping);
	}

	@Override
	public DatabaseQuery createQuery(String mapping) {
		if (READ_MAPPING.equals(mapping)) {
			return new DatabaseQuery() {
				
				@Override
				public ArdenValue[] execute() {
					// single column
					return new ArdenValue[] {new ArdenList(values1)};
				}
			};
		} else if (READ_MULTIPLE_MAPPING.equals(mapping)) {
			return new DatabaseQuery() {
				
				@Override
				public ArdenValue[] execute() {
					// two columns
					return new ArdenValue[] {new ArdenList(values1), new ArdenList(values2)};
				}
			};		
		}
		return DatabaseQuery.NULL;
	}

	@Override
	public void write(ArdenValue message, String destination) {
		// save messages
		String stringMessage = ((ArdenString) message).value;
		messages.add(stringMessage);
	}

	public List<String> getMessages() {
		return messages;
	}

}