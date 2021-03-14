package tk.mightyelemental.sul;

import static tk.mightyelemental.sul.SULCommands.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class SomeUnnamedLanguage {
	
	/*
	 * -=DATA TYPE TODOS=-
	 * TODO: Add array type (more of a dictionary)
	 * TODO: Add boolean type
	 * 
	 * -=MATH FUNCTION TODOS=-
	 * TODO: Add subtraction
	 * TODO: Add multiplication
	 * TODO: Add division
	 * TODO: Add indices
	 * TODO: Add increment/decrement
	 * 
	 * -=INSTRUCTION TODOS=-
	 * TODO: Add if statements
	 * TODO: Add for loops
	 * TODO: Add while loops
	 */

	// Prevent instantiation
	private SomeUnnamedLanguage() {
	}

	public static void main( String[] args ) {
		Script script = null;
		for (String arg : args) {
			if (!arg.startsWith("--")) {
				try {
					script = FileManager.loadScriptFromFile(arg);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (script != null) {
			runScript(script);
		}

	}

	/**
	 * Executes a script
	 * 
	 * @param script the script to execute
	 * @see #interpretLine(List)
	 */
	static void runScript( Script script ) {
		
		for (int i = 0; i < script.getLines().size(); i++) {
			String[] line = script.getLines().get(i);
			interpretLine(line, i);
		}
	}

	/**
	 * Interprets and executes a line of code
	 * 
	 * @param line the line to execute
	 */
	static void interpretLine( String[] line, int lineNum ) {
		// System.out.println(line);
		switch (line[0]) {
			case "set":
				set(line, lineNum);
				break;
			case "display":
				display(line, lineNum);
				break;
			case "add":
				add(line, lineNum);
				break;
			default:
				SULExceptions.commandNotRecognised(lineNum, line);
		}
	}

}