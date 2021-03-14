package tk.mightyelemental.sul;

import static tk.mightyelemental.sul.SULExceptions.*;

import java.util.HashMap;

public class SULCommands {

	private SULCommands() {
	}

	public static HashMap<String, Object> variables = new HashMap<String, Object>();

	public static void set( String[] line ) {
		// Ensure variable starts with variable symbol
		if (!line[1].startsWith(":")) {
			System.err.println("Variable names must start with a ':'");
			return;
		}
		// Ensure command has enough tokens
		if (line.length < 4) {
			commandIncompleteException("Line is incomplete!", line);
		}
		StringBuilder buffer = new StringBuilder();
		// boolean isString = false;
		for (int i = 3; i < line.length; i++) {
			String token = line[i];
			if (token.startsWith(":")) {
				if (doesVarExist(token)) {
					buffer.append(getVarVal(token).toString());
				} else {
					varNotSetException(token);
				}
			} else if (token.startsWith("\"")) {
				buffer.append(token.replaceAll("\"", ""));
				// isString = true;
			} else if (isNumber(token)) {
				buffer.append(token);
			} else {
				invalidSyntaxException("-Strings must be contained within quotes.\n"
						+ "-Variables must start with a colon.\n" + "-Numbers can only contain the numbers 0-9, '-', and '.'",
						token);
			}
		}

		variables.put(line[1], buffer.toString());

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
			Float.parseFloat(val);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String display( String[] line ) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 1; i < line.length; i++) {
			String token = line[i];
			if (token.startsWith(":")) {

				if (doesVarExist(token)) {
					buffer.append(getVarVal(token).toString());
				} else {
					varNotSetException(token);
					return null;
				}

			} else if (token.startsWith("\"")) {
				buffer.append(token.replaceAll("\"", ""));
			} else {
				invalidSyntaxException("Strings must be contained within quotes and variables must start with a colon.",
						token);
			}
		}
		System.out.println(buffer.toString());
		return buffer.toString();
	}

	public static boolean add( String[] line ) {
		// line.remove(0);
		float amount = 0;
		float oldAmount = 0;
		boolean flag = false;
		try {
			if (doesVarExist(line[1])) {
				if (isNumber(getVarVal(line[1]) + "")) {
					amount = Float.parseFloat(getVarVal(line[1]) + "");
					flag = true;
				}
			}
			if (!flag) {
				if (isNumber(line[1])) {
					amount = Float.parseFloat(line[1]);
					flag = true;
				}
			}

			if (!flag) {
				SULExceptions.invalidSyntaxException("A number or a number variable needs to be used in the first argument",
						line);
				return false;
			}

			Object o = getVarVal(line[3]);
			if (o == null) {
				SULExceptions.varNotSetException(line[3]);
				return false;
			} else {
				oldAmount = Float.parseFloat(o + "");
			}
		} catch (Exception e) {
			SULExceptions.invalidSyntaxException("", line);
			return false;
		}

		variables.put(line[3], oldAmount + amount);

		return true;
	}

	public static Object getVarVal( String var ) {
		return variables.get(var);
	}

	public static boolean doesVarExist( String var ) {
		return variables.containsKey(var);
	}

}