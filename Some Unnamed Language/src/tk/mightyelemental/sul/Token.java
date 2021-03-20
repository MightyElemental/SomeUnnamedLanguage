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
		this.subTokens = Arrays.copyOf(tokens, tokens.length);
		String[] strTokens = new String[tokens.length];
		for (int i = 0; i < tokens.length; i++) strTokens[i] = tokens[i].getData();
		this.data = String.join(" ", strTokens);
		this.type = Type.Complex;
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

	/** The token type helps with processing of commands */
	public static enum Type {
		Complex, Number, String, Variable, Command, Extra
	}
}
