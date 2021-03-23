package tk.mightyelemental.sul;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TokenizerTests {

	Token		complexToken;
	String[]	codeLines;

	@Before
	public void initialize() {
		codeLines = new String[] {
				"set :x to 5",
				"set :y to 10",
				"add :x to :y",
				"display \"Hello, World!\" \" This is a test.\" :x",
				"add 5 to :x",
				"display \":x = 10\"" };
	}

	/** Strings should have quotes at this stage. */
	@Test
	public void testStringTokenizerFP() {
		String[] tokens = Script.tokenizeFirstPass(codeLines[3]);
		assertEquals("\"Hello, World!\"", tokens[1]);
		assertEquals("\" This is a test.\"", tokens[2]);
	}

	/**
	 * Strings should NOT have quotes at this stage. The tokenizer system should already know they are Strings.<br>
	 * This tests for the correct data and the correct data type for Strings
	 */
	@Test
	public void testStringTokenizerSP() {
		String[] tokens = Script.tokenizeFirstPass(codeLines[3]);
		Token[] tokens2 = Script.tokenizeSecondPass(tokens);
		assertEquals("Hello, World!", tokens2[1].getData());
		assertEquals(" This is a test.", tokens2[2].getData());
		assertEquals(Token.Type.String, tokens2[1].getType());
		assertEquals(Token.Type.String, tokens2[2].getType());
	}

	@Test
	public void testStringTokenizerTP() {
		String[] tokens = Script.tokenizeFirstPass(codeLines[3]);
		Token[] tokens2 = Script.tokenizeSecondPass(tokens);
		tokens2 = Script.tokenizeThirdPass(tokens2);
		assertEquals("Hello, World!", tokens2[1].getData());
		assertEquals(" This is a test.", tokens2[2].getData());
		assertEquals(Token.Type.String, tokens2[1].getType());
		assertEquals(Token.Type.String, tokens2[2].getType());
	}

	/** Test that if a String contains a variable that it is still recognized as a string */
	@Test
	public void testStringTokenizerWithVar() {
		String[] tokens = Script.tokenizeFirstPass(codeLines[5]);
		Token[] tokens2 = Script.tokenizeSecondPass(tokens);
		assertEquals(":x = 10", tokens2[1].getData());
		assertEquals(Token.Type.String, tokens2[1].getType());
	}

	@Test
	public void testListTokenize() {
		String[] tokens = Script.tokenizeFirstPass("set element 0 of :x to 10");
		Token[] tokens2 = Script.tokenizeSecondPass(tokens);
		tokens2 = Script.tokenizeThirdPass(tokens2);
		assertEquals("element 0 of :x", tokens2[1].getData());
		assertEquals(Token.Type.ListRef, tokens2[1].getType());
	}

}
