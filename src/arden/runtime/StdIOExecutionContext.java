package arden.runtime;

import java.util.Scanner;

import arden.CommandLineOptions;
import arden.constants.ConstantParser;
import arden.constants.ConstantParser.ConstantParserException;

/** Reads and prints queries from/to StdIO. */
public class StdIOExecutionContext extends BaseExecutionContext {
	Scanner sc = new Scanner(System.in);

	public StdIOExecutionContext(CommandLineOptions options) {
		super(options);
	}

	public DatabaseQuery createQuery(String mapping) {
		System.out.println("Query mapping: \"" + mapping + "\". Enter result as "
				+ "constant Arden Syntax expression (Strings in quotes)");
		System.out.print(" >");
		String line = null;
		if (sc.hasNext()) {
			line = sc.nextLine();
		}
		ArdenValue[] val = null;
		try {
			val = new ArdenValue[] { ConstantParser.parse(line) };
		} catch (ConstantParserException e) {
			System.out.println("Error parsing at char: " + e.getPos());
			System.out.println("Message: " + e.getMessage());
		}
		return new MemoryQuery(val);
	}

	public ArdenValue getMessage(String mapping) {
		System.out.println("Message, mapping: " + mapping);
		return new ArdenString(mapping);
	}

	public void write(ArdenValue message, String destination) {
		if ("stdout".equalsIgnoreCase(destination)) {
			// just print string
			if (message instanceof ArdenString) {
				System.out.println(ArdenString.getStringFromValue(message));
			} else {
				System.out.println(message);
			}
		} else {
			// prepend destination to printed string
			System.out.print("Destination: \"");
			System.out.print(destination);
			System.out.print("\" Message: ");
			if (message instanceof ArdenString) {
				System.out.println(ArdenString.getStringFromValue(message));
			} else {
				System.out.println(message);
			}
		}
	}

}
