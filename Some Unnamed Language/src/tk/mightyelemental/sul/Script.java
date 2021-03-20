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
	 * @see #tokenizeScriptLineSecondary(String[])
	 */
	private List<String[]> tokenizedLines;

	/** A list of unprocessed lines of code */
	private List<String> rawLines;

	/**
	 * Create a new Script object from a provided file location.<br>
	 * Tokenizes the lines and stores them locally.
	 * 
	 * @param scriptLocation the location of the script file
	 * @throws FileNotFoundException If the file is not found at the location
	 * @throws IOException If an I/O error occurs
	 * @see #Script(String[])
	 */
	public Script( String scriptLocation ) throws IOException, FileNotFoundException {
		this(FileManager.loadLinesFromFile(scriptLocation));
	}

	/**
	 * Create a new Script object from provided program lines.<br>
	 * Tokenizes the lines and stores them locally.
	 * 
	 * @param script the program lines
	 */
	public Script( String[] script ) {
		String[] scriptLines = Arrays.copyOf(script, script.length);
		tokenizedLines = new ArrayList<String[]>(scriptLines.length);
		rawLines = new ArrayList<String>(List.of(scriptLines));
		for (String line : scriptLines) {
			String[] tokens = tokenizeScriptLine(line);
			tokens = tokenizeScriptLineSecondary(tokens);
			tokenizedLines.add(tokens);
		}
	}

	/**
	 * Split a line into individual tokens.<br>
	 * Currently, it mainly splits tokens by the spaces, however, a String is regarded as a single token (defined as
	 * being contained within two quotation marks).
	 * 
	 * @param line the line to split into tokens
	 * @return An array of tokens from the line (the first token is the instruction)
	 */
	public static String[] tokenizeScriptLine( String line ) {
		List<String> tokenList = new ArrayList<String>();
		boolean stringFlag = false;
		char[] chars = line.toCharArray();
		StringBuilder token = new StringBuilder();
		for (char c : chars) {
			// If char is quote, toggle the string mode
			if (c == '"') {
				stringFlag = !stringFlag;
			}
			if (c != ' ' || stringFlag) {
				token.append(c);
			} else if (token.length() > 0) {
				tokenList.add(token.toString());
				token.setLength(0);
			}
		}
		// If there is content remaining in the buffer, add buffer to token list
		if (token.length() > 0) {
			tokenList.add(token.toString());
		}
		// System.err.println(tokenList);
		return tokenList.toArray(String[]::new);
	}

	/**
	 * Merges tokens into a single token depending on the content.
	 * 
	 * @param tokens the tokenized line of code
	 * @return An array of tokens from the line
	 * @see #tokenizeScriptLine(String)
	 */
	public static String[] tokenizeScriptLineSecondary( String[] tokens ) {
		List<String> tokenList = new ArrayList<String>();
		for (int i = 0; i < tokens.length; i++) {
			// This means there is a list variable
			// 4 tokens - 'element x of :var'
			if (tokens[i].equals("element")) {
				tokenList.add(mergeListReferenceTokens(tokens, i));
				i += 3;
			} else {
				tokenList.add(tokens[i]);
			}
		}
		return tokenList.toArray(String[]::new);
	}

	private static String mergeListReferenceTokens( String[] tokens, int i ) {
		StringBuilder result = new StringBuilder("element ");

		if (tokens.length - i <= 0) {
			SULExceptions.commandIncompleteException("The line of code is incomplete", -1, tokens);
		}

		result.append(tokens[i + 1]);

		if (tokens[i + 2].equals("of")) {
			result.append(" of ");
		} else {
			SULExceptions.invalidSyntaxException("Invalid list reference", -1, tokens);
		}

		if (tokens[i + 3].startsWith(":")) {
			result.append(tokens[i + 3]);
		} else {
			SULExceptions.invalidSyntaxException("Variable names must start with a ':'", -1, tokens);
		}

		return result.toString();
	}

	/**
	 * Get the list of program lines from the script.
	 * 
	 * @return The tokenized lines of the script
	 */
	public List<String[]> getLines() {
		return tokenizedLines;
	}

	/**
	 * @return The raw lines of code
	 */
	public List<String> getRawLines() {
		return rawLines;
	}

}
