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
	 * @see #tokenizeFirstPass(String)
	 * @see #tokenizeSecondPass(String[])
	 * @see #tokenizeThirdPass(Token[])
	 */
	private List<Token[]> tokenizedLines;

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
		tokenizedLines = new ArrayList<Token[]>(scriptLines.length);
		rawLines = new ArrayList<String>(List.of(scriptLines));
		for (String line : scriptLines) {
			String[] tokens = tokenizeFirstPass(line);
			Token[] tTokens = tokenizeSecondPass(tokens);
			tTokens = tokenizeThirdPass(tTokens);
			tokenizedLines.add(tTokens);
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
	public static String[] tokenizeFirstPass( String line ) {
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
	 * Assign token types to already split tokens<br>
	 * 
	 * @param tokens the line to split into tokens
	 * @return An array of tokens from the line (the first token is the instruction)
	 * @see #tokenizeFirstPass(String)
	 */
	private Token[] tokenizeSecondPass( String[] tokens ) { // TODO: Check if these assignments are accurate in context
		List<Token> tokenList = new ArrayList<Token>();

		tokenList.add(new Token(tokens[0], Token.Type.Command));
		for (int i = 1; i < tokens.length; i++) {
			if (tokens[i].startsWith("\"")) {
				tokenList.add(new Token(tokens[i], Token.Type.String));
			} else if (Utils.isNumber(tokens[i])) {
				tokenList.add(new Token(tokens[i], Token.Type.Number));
			} else if (tokens[i].startsWith(":")) {
				tokenList.add(new Token(tokens[i], Token.Type.Variable));
			} else {
				tokenList.add(new Token(tokens[i], Token.Type.Extra));
			}
		}

		return tokenList.toArray(Token[]::new);
	}

	/**
	 * Merges tokens into a single token depending on the content.
	 * 
	 * @param tokens the tokenized line of code
	 * @return An array of tokens from the line
	 * @see #tokenizeSecondPass(String[])
	 */
	private static Token[] tokenizeThirdPass( Token[] tokens ) {
		List<Token> tokenList = new ArrayList<Token>();
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
		return tokenList.toArray(Token[]::new);
	}

	/**
	 * Merges tokens related to list references together into one complex token.
	 * 
	 * @param tokens the tokens on this line
	 * @param i the current index where 'element' is
	 * @return The complex token containing the list reference
	 */
	private static Token mergeListReferenceTokens( Token[] tokens, int i ) {
		if (tokens.length - i <= 0) { // Ensure there are at least enough tokens for the reference
			SULExceptions.commandIncompleteException("The line of code is incomplete", -1, tokens);
		}

		Token element = new Token(tokens[i].getData(), Token.Type.Extra);

		Token index = tokens[i + 1];// new Token(tokens[i + 1].getData(), Token.Type.Variable);

		if (!tokens[i + 2].equals("of")) { // Throw error if syntax is wrong
			SULExceptions.invalidSyntaxException("Invalid list reference", -1, tokens);
		}

		Token of = new Token("of", Token.Type.Extra);

		Token var = new Token(tokens[i + 3].getData(), Token.Type.Variable);

		if (!tokens[i + 3].getData().startsWith(":")) { // Throw error if syntax is wrong
			SULExceptions.invalidVariableSyntaxException(-1, tokens);
		}

		return new Token(element, index, of, var);
	}

	/**
	 * Get the list of program lines from the script.
	 * 
	 * @return The tokenized lines of the script
	 */
	public List<Token[]> getLines() {
		return tokenizedLines;
	}

	/**
	 * @return The raw lines of code
	 */
	public List<String> getRawLines() {
		return rawLines;
	}

}
