package tk.mightyelemental.sul;

import static tk.mightyelemental.sul.SULCommands.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class SomeUnnamedLanguage {

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
		for (List<String> line : script.getLines()) {
			interpretLine(line);
		}
	}

	/**
	 * Interprets and executes a line of code
	 * 
	 * @param line the line to execute
	 */
	static void interpretLine( List<String> line ) {
		// System.out.println(line);
		switch (line.get(0)) {
			case "set":
				set(line);
				break;
			case "display":
				display(line);
				break;
			case "add":
				add(line);
				break;
			default:
				SULExceptions.commandNotRecognised(line);
		}
	}

}