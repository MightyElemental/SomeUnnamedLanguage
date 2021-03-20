package tk.mightyelemental.sul;

import static tk.mightyelemental.sul.SULExceptions.*;
import static tk.mightyelemental.sul.Utils.isNumber;

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
	 * @param lineNum the line number the code belongs to
	 */
	public static void set( Token[] line, int lineNum ) {
		// Ensure variable starts with variable symbol
		if (line[1].getType() != Token.Type.Variable) {
			SULExceptions.invalidSyntaxException("First argument must be a variable", lineNum, line);
		}
		// Ensure command has enough tokens
		if (line.length < 4) {
			commandIncompleteException("Line is incomplete!", lineNum, line);
		}
		StringBuilder buffer = new StringBuilder();
		boolean isString = false;
		for (int i = 3; i < line.length; i++) {
			Token token = line[i];
			switch (token.getType()) { // TODO: Add list types
				case Number:
					buffer.append(token);
					break;
				case String:
					buffer.append(token.getData().replaceAll("\"", ""));
					isString = true;
					break;
				case Variable:
					if (doesVarExist(token)) {
						buffer.append(getVarVal(token).toString());
					} else {
						varNotSetException(lineNum, token);
					}
					break;
				default:
					invalidSyntaxException(
							"-Strings must be contained within quotes.\n" + "-Variables must start with a colon.\n"
									+ "-Numbers can only contain the numbers 0-9, '-', and '.'",
							lineNum, token);
					break;
			}
		}

		if (isString) { // TODO: Add boolean and list values
			variables.put(line[1].getData(), buffer.toString());
		} else {
			variables.put(line[1].getData(), Double.parseDouble(buffer.toString()));
		}

		// System.out.println(variables);
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
	 * @param lineNum the line number the code belongs to
	 */
	public static void display( Token[] line, int lineNum ) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 1; i < line.length; i++) {
			Token token = line[i];
			switch (token.getType()) { // TODO: Add list types
				case Number:
					buffer.append(token.getData());
					break;
				case String:
					buffer.append(token.getData().replaceAll("\"", ""));
					break;
				case Variable:
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
					break;
				default:
					invalidSyntaxException("Strings must be contained within quotes and variables must start with a colon.",
							lineNum, line);
					break;

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
	 * @param lineNum the line number the code belongs to
	 */
	public static void add( Token[] line, int lineNum ) {
		double amount = 0;
		double oldAmount = 0;
		boolean valueSetFlag = false;

		switch (line[1].getType()) { // TODO: Add list types
			case Number:
				amount = Double.parseDouble(line[1].getData());
				valueSetFlag = true;
				break;
			case Variable:
				if (doesVarExist(line[1])) {
					String varVal = getVarVal(line[1]).toString();
					if (isNumber(varVal)) {
						amount = Double.parseDouble(varVal);
						valueSetFlag = true;
					}
				} else {
					varNotSetException(lineNum, line[1]);
				}
				break;
			default:
				break;
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

		variables.put(line[3].getData(), oldAmount + amount);
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
	 * Get the value of variable
	 * 
	 * @param var the name of the variable
	 * @return The value of the object, or {@code null} if the variable does not exist
	 * @see #getVarVal(String)
	 */
	public static Object getVarVal( Token var ) {
		return getVarVal(var.getData());
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

	/**
	 * Check if a variable exists
	 * 
	 * @param var the name of the variable
	 * @return {@code true} if the variable exists
	 * @see #doesVarExist(String)
	 */
	public static boolean doesVarExist( Token var ) {
		return doesVarExist(var.getData());
	}

}