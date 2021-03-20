package tk.mightyelemental.sul;

import java.util.Arrays;

public class Token {

	/** The segment of code contained by this token */
	private String data;

	/** Array of subtokens contained within this token */
	private Token[] subTokens;

	/** The type of data contained within this token */
	private Type type;

	public Token( String data, Type type ) {
		this.data = data;
		this.type = type;
	}

	/**
	 * Create a new Token comprised of subtokens.<br>
	 * The {@link Type} of this token will be {@link Type#Complex}.
	 * 
	 * @param tokens an array of subtokens
	 */
	public Token( Token... tokens ) {
		this(joinTokens(tokens), Type.Complex);
		this.subTokens = Arrays.copyOf(tokens, tokens.length);
	}

	/**
	 * Test if the token contains more tokens
	 * 
	 * @return {@code true} if {@link #subTokens} is not null
	 */
	public boolean containsSubTokens() {
		return subTokens != null;
	}

	/** @return The data contained within this token */
	public String getData() {
		return data;
	}

	/** @return The array of subtokens */
	public Token[] getSubTokens() {
		return subTokens;
	}

	/** @return The type of data contained by this token */
	public Type getType() {
		return type;
	}

	public String toString() {
		return data;
	}

	/**
	 * Check if object is equal or String is equal to the contents.
	 * 
	 * @param o the object to test equivalency
	 * @return {@code true} if the objects are the same
	 */
	public boolean equals( Object o ) {
		return super.equals(o) || this.data.equals(o);
	}

	/**
	 * Join tokens together to form a single String.
	 * 
	 * @param tokens the tokens to join
	 * @return A String of tokens separated by spaces
	 */
	public static String joinTokens( Token... tokens ) {
		String[] strTokens = new String[tokens.length];
		for (int i = 0; i < tokens.length; i++) strTokens[i] = tokens[i].getData();
		return String.join(" ", strTokens);
	}

	/** The token type helps with processing of commands */
	public static enum Type {
		Complex, Number, String, Variable, Command, Extra
	}
}
