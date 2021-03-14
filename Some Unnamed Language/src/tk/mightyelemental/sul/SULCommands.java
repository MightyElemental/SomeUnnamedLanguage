package tk.mightyelemental.sul;

import static tk.mightyelemental.sul.SULExceptions.commandIncompleteException;
import static tk.mightyelemental.sul.SULExceptions.incompatibleTypeException;
import static tk.mightyelemental.sul.SULExceptions.invalidSyntaxException;
import static tk.mightyelemental.sul.SULExceptions.varNotSetException;

import java.util.HashMap;

public class SULCommands {

	private SULCommands() {
	}

	public static HashMap<String, Object> variables = new HashMap<String, Object>();

	/**
	 * Define a variable with a value.<br>
	 * Examples:
	 * 
	 * <pre>
	 * set :name to "Jack"
	 * set :num to 123
	 * </pre>
	 * 
	 * @param line the line of code to process
	 */
	public static void set( String[] line, int lineNum ) {
		// Ensure variable starts with variable symbol
		if (!line[1].startsWith(":")) {
			System.err.println("Variable names must start with a ':'");
			return;
		}
		// Ensure command has enough tokens
		if (line.length < 4) {
			commandIncompleteException("Line is incomplete!", lineNum, line);
		}
		StringBuilder buffer = new StringBuilder();
		boolean isString = false;
		for (int i = 3; i < line.length; i++) {
			String token = line[i];
			if (token.startsWith(":")) {
				if (doesVarExist(token)) {
					buffer.append(getVarVal(token).toString());
				} else {
					varNotSetException(lineNum, token);
				}
			} else if (token.startsWith("\"")) {
				buffer.append(token.replaceAll("\"", ""));
				isString = true;
			} else if (isNumber(token)) {
				buffer.append(token);
			} else {
				invalidSyntaxException("-Strings must be contained within quotes.\n"
						+ "-Variables must start with a colon.\n" + "-Numbers can only contain the numbers 0-9, '-', and '.'",
						lineNum, token);
			}
		}

		if (isString) {
			variables.put(line[1], buffer.toString());
		} else {
			variables.put(line[1], Double.parseDouble(buffer.toString()));
		}

		// System.out.println(variables);
	}

	/**
	 * Test if a String is a valid number
	 * 
	 * @param val the String to test
	 * @return {@code true} if the String is a valid number, {@code false} otherwise
	 */
	private static boolean isNumber( String val ) {
		try {
			Double.parseDouble(val);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Prints out a string to the console.<br>
	 * Examples:
	 * 
	 * <pre>
	 * display "Hello World!"
	 * display "The number " :num " is a pretty good number"
	 * </pre>
	 * 
	 * @param line the line of code to process
	 */
	public static void display( String[] line, int lineNum ) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 1; i < line.length; i++) {
			String token = line[i];
			if (token.startsWith(":")) {

				if (doesVarExist(token)) {
					Object varVal = getVarVal(token);
					if (varVal instanceof Double) {
						buffer.append(Utils.numberToString((Double) varVal));
					} else {
						buffer.append(varVal.toString());
					}
				} else {
					varNotSetException(lineNum, token);
				}

			} else if (token.startsWith("\"")) {
				buffer.append(token.replaceAll("\"", ""));
			} else {
				invalidSyntaxException("Strings must be contained within quotes and variables must start with a colon.",
						lineNum, line);
			}
		}
		System.out.println(buffer.toString());
	}

	/**
	 * Adds a number to a number variable.<br>
	 * Examples:
	 * 
	 * <pre>
	 * add 123 to :num
	 * add :num to :num
	 * </pre>
	 * 
	 * @param line the line of code to process
	 */
	public static void add( String[] line, int lineNum ) {
		double amount = 0;
		double oldAmount = 0;
		boolean valueSetFlag = false;

		if (line[1].startsWith(":")) { // Check if value is a variable
			if (doesVarExist(line[1])) {
				String varVal = getVarVal(line[1]).toString();
				if (isNumber(varVal)) {
					amount = Double.parseDouble(varVal);
					valueSetFlag = true;
				}
			} else {
				varNotSetException(lineNum, line[1]);
			}
		} else if (isNumber(line[1])) {
			amount = Double.parseDouble(line[1]);
			valueSetFlag = true;
		}

		// If a value couldn't be found, throw an error
		if (!valueSetFlag) {
			incompatibleTypeException("A number, or a number variable, needs to be used in the first argument", lineNum,
					line);
		}

		if (doesVarExist(line[3])) {
			Object o = getVarVal(line[3]);
			oldAmount = Double.parseDouble(o.toString());
		} else {
			varNotSetException(lineNum, line[3]);
		}

		variables.put(line[3], oldAmount + amount);
	}

	/**
	 * Get the value of variable
	 * 
	 * @param var the name of the variable
	 * @return The value of the object, or {@code null} if the variable does not exist
	 */
	public static Object getVarVal( String var ) {
		return variables.get(var);
	}

	/**
	 * Check if a variable exists
	 * 
	 * @param var the name of the variable
	 * @return {@code true} if the variable exists
	 */
	public static boolean doesVarExist( String var ) {
		return variables.containsKey(var);
	}

}