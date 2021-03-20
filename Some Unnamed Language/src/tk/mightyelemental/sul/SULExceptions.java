package tk.mightyelemental.sul;

public class SULExceptions {

	private SULExceptions() {
	}

	/**
	 * Prints an error message to the console
	 * 
	 * @param type the name of the exception
	 * @param message the error message
	 * @param lineNum the line number the code belongs to
	 * @param code the line of code that caused the error
	 */
	private static void exception( String type, String message, int lineNum, String... code ) {
		System.err.printf("%sException\n\t%s\n\tline %d: [%s]\n", type, message, lineNum + 1, String.join(" ", code));
	}

	/**
	 * Prints an error message to the console before exiting the program
	 * 
	 * @param type the name of the exception
	 * @param message the error message
	 * @param lineNum the line number the code belongs to
	 * @param code the line of code that caused the error
	 * @see #exception(String, String, String)
	 */
	private static void criticalException( String type, String message, int lineNum, String... code ) {
		exception(type, message, lineNum, code);
		System.exit(1);
	}

	/**
	 * Thrown when a line of code contains invalid syntax
	 * 
	 * @param message the error message
	 * @param lineNum the line number the code belongs to
	 * @param code the line of code that caused the error
	 */
	public static void invalidSyntaxException( String message, int lineNum, String... code ) {
		criticalException("InvalidSyntax", message, lineNum, code);
	}

	/**
	 * Thrown when a line of code contains a variable that does not start with a colon.
	 * 
	 * @param lineNum the line number the code belongs to
	 * @param code the line of code that caused the error
	 */
	public static void invalidVariableSyntaxException( int lineNum, String... code ) {
		invalidSyntaxException("Variable names must start with a ':'", lineNum, code);
	}

	/**
	 * Thrown when an attempt it made to use a variable that does not exist
	 * 
	 * @param lineNum the line number the code belongs to
	 * @param code the line of code that caused the error
	 */
	public static void varNotSetException( int lineNum, String... code ) {
		criticalException("VariableNotFound", "The following variable has not been assigned", lineNum, code);
	}

	/**
	 * Thrown when a non-recognized command is used
	 * 
	 * @param lineNum the line number the code belongs to
	 * @param code the line of code that caused the error
	 */
	public static void commandNotRecognised( int lineNum, String... code ) {
		criticalException("CommandNotRecognised", "The command used does not exist!", lineNum, code);
	}

	/**
	 * Thrown when there are not enough tokens in a given line of code
	 * 
	 * @param message the error message
	 * @param lineNum the line number the code belongs to
	 * @param code the line of code that caused the error
	 */
	public static void commandIncompleteException( String message, int lineNum, String... code ) {
		criticalException("CommandIncomplete", message, lineNum, code);
	}

	/**
	 * Thrown when the code attempts to use an incompatible type of variable.<br>
	 * Example: Attempting to add a string to a number will throw this error
	 * 
	 * @param message the error message
	 * @param lineNum the line number the code belongs to
	 * @param code the line of code that caused the error
	 */
	public static void incompatibleTypeException( String message, int lineNum, String... code ) {
		criticalException("IncompatibleType", message, lineNum, code);
	}

}
