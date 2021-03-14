package tk.mightyelemental.sul;

public class SULExceptions {

	private SULExceptions() {
	}

	/**
	 * Prints an error message to the console
	 * 
	 * @param type the name of the exception
	 * @param message the error message
	 * @param code the line of code that caused the error
	 */
	private static void exception( String type, String message, String... code ) {
		System.err.printf("%sException\n\t%s\n\t[%s]\n", type, message, String.join(" ", code));
	}

	/**
	 * Prints an error message to the console before exiting the program
	 * 
	 * @param type the name of the exception
	 * @param message the error message
	 * @param code the line of code that caused the error
	 * @see #exception(String, String, String)
	 */
	private static void criticalException( String type, String message, String... code ) {
		exception(type, message, code);
		System.exit(1);
	}

	/**
	 * Thrown when a line of code contains invalid syntax
	 * 
	 * @param message the error message
	 * @param code the line of code that caused the error
	 */
	public static void invalidSyntaxException( String message, String... code ) {
		criticalException("InvalidSyntax", message, code);
	}

	/**
	 * Thrown when an attempt it made to use a variable that does not exist
	 * 
	 * @param code the line of code that caused the error
	 */
	public static void varNotSetException( String... code ) {
		criticalException("VariableNotFound", "The following variable has not been assigned", code);
	}

	/**
	 * Thrown when a non-recognized command is used
	 * 
	 * @param code the line of code that caused the error
	 */
	public static void commandNotRecognised( String... code ) {
		criticalException("CommandNotRecognised", "The command used does not exist!", code);
	}

	/**
	 * Thrown when there are not enough tokens in a given line of code
	 * 
	 * @param message the error message
	 * @param code the line of code that caused the error
	 */
	public static void commandIncompleteException( String message, String... code ) {
		criticalException("CommandIncomplete", message, code);
	}

}
