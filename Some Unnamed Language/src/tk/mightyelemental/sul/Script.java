package tk.mightyelemental.sul;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Contains data and methods related to Script files */
public class Script {

	/**
	 * A list of tokenized lines
	 * 
	 * @see #tokenizeScriptLine(String)
	 */
	private List<List<String>> tokenizedLines = new ArrayList<List<String>>();

	public Script( String scriptLocation ) throws IOException, FileNotFoundException {
		this(FileManager.loadLinesFromFile(scriptLocation));
	}

	/**
	 * Create a new Script object from provided program lines.<br>
	 * Tokenizes the lines and stores them locally.
	 * 
	 * @param script the program lines
	 */
	public Script( String... script ) {
		String[] scriptLines = Arrays.copyOf(script, script.length);
		for (String line : scriptLines) {
			tokenizedLines.add(tokenizeScriptLine(line));
		}
	}

	/**
	 * Split a line into individual tokens.<br>
	 * Currently, it mainly splits tokens by the spaces, however, a String is regarded as a single token (defined as being
	 * contained within two quotation marks).
	 * 
	 * @param line the line to split into tokens
	 * @return A list of tokens from the line (the first token is the instruction)
	 */
	public static List<String> tokenizeScriptLine( String line ) {
		List<String> tokenList = new ArrayList<String>();
		boolean stringFlag = false;
		char[] chars = line.toCharArray();
		StringBuilder token = new StringBuilder();
		for (char c : chars) {
			if (c == '"') {
				stringFlag = !stringFlag;
			}
			if ((c != ' ' && !stringFlag) || stringFlag) {
				token.append(c);
			}
			if (!stringFlag && c == ' ') {
				tokenList.add(token.toString());
				token.setLength(0);
			}
		}
		if (token.length() > 0) {
			tokenList.add(token.toString());
		}
		// System.err.println(tokenList);
		return tokenList;
	}

	/**
	 * Get the list of program lines from the script.
	 * 
	 * @return The tokenized lines of the script
	 */
	public List<List<String>> getLines() {
		return tokenizedLines;
	}

}
