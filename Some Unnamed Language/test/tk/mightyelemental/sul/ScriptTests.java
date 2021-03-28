package tk.mightyelemental.sul;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScriptTests {

	// see https://stackoverflow.com/a/1119559/3005475
	private final ByteArrayOutputStream	outContent	= new ByteArrayOutputStream();
	private final ByteArrayOutputStream	errContent	= new ByteArrayOutputStream();
	private final PrintStream				originalOut	= System.out;
	private final PrintStream				originalErr	= System.err;

	@Before
	public void initialize() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		SULCommands.variables.clear();
		SomeUnnamedLanguage.HALT = false;
	}

	@After
	public void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	/**
	 * Run a line of code and test against the {@code expected} output.
	 * 
	 * @param lines the lines of code
	 * @param lineNum the line of code to run
	 * @param expected the expected output
	 */
	private void testScriptOutputOut( List<Token[]> lines, int lineNum, String expected ) {
		outContent.reset();
		SomeUnnamedLanguage.interpretLine(lines.get(lineNum), lineNum);
		assertEquals(expected, outContent.toString());
	}

	/**
	 * Run a line of code and test against the {@code expected} output.
	 * 
	 * @param lines the lines of code
	 * @param lineNum the line of code to run
	 * @param expected the expected output
	 */
	private void testScriptOutputErr( List<Token[]> lines, int lineNum, String expected ) {
		errContent.reset();
		SomeUnnamedLanguage.interpretLine(lines.get(lineNum), lineNum);
		assertEquals(expected, errContent.toString());
	}

	/**
	 * Run a line of code and test that the output starts with an {@code exception}.
	 * 
	 * @param lines the lines of code
	 * @param lineNum the line of code to run
	 * @param exception the expected exception
	 */
	private void testScriptException( List<Token[]> lines, int lineNum, String exception ) {
		errContent.reset();
		SomeUnnamedLanguage.interpretLine(lines.get(lineNum), lineNum);
		assertTrue(errContent.toString().startsWith(exception));
	}

	@Test
	public void testStringDisplay() {
		String[] rawLines = { "display \"Hello, World!\" \" This is a test.\"" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		testScriptOutputOut(lines, 0, "Hello, World! This is a test.\n");
	}

	@Test
	public void testNumberDisplay() {
		String[] rawLines = { "display 1.234" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		testScriptOutputOut(lines, 0, "1.234\n");
	}

	@Test
	public void testVariableDisplay() {
		String[] rawLines = { "set :x to \"twenty = \"", "set :y to 20", "display :x :y" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		SomeUnnamedLanguage.interpretLine(lines.get(1), 1);
		testScriptOutputOut(lines, 2, "twenty = 20\n");
	}

	@Test
	public void testMixedDisplay() {
		String[] rawLines = { "set :x to \"twenty\"", "set :y to 2", "display :x \" = \" :y 0" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		SomeUnnamedLanguage.interpretLine(lines.get(1), 1);
		testScriptOutputOut(lines, 2, "twenty = 20\n");
	}

	@Test
	public void testIntegerAddition() {
		String[] rawLines = { "set :x to 5", "add 5 to :x", "display \"5 + 5 = \" :x" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		SomeUnnamedLanguage.interpretLine(lines.get(1), 1);
		testScriptOutputOut(lines, 2, "5 + 5 = 10\n");
	}

	@Test
	public void testDoubleAddition() {
		String[] rawLines = {
				"set :x to 2.12",
				"add 2.87 to :x",
				"display \"2.12 + 2.87 = \" :x",
				"add 0.01 to :x",
				"display :x" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		SomeUnnamedLanguage.interpretLine(lines.get(1), 1);
		testScriptOutputOut(lines, 2, "2.12 + 2.87 = 4.99\n");
		SomeUnnamedLanguage.interpretLine(lines.get(3), 3);
		testScriptOutputOut(lines, 4, "5\n");
	}

	@Test
	public void testDoubleAdditionWithVar() {
		String[] rawLines = { "set :x to 2.12", "set :y to 2.87", "add :y to :x", "display \"2.12 + 2.87 = \" :x" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		SomeUnnamedLanguage.interpretLine(lines.get(1), 1);
		SomeUnnamedLanguage.interpretLine(lines.get(2), 2);
		testScriptOutputOut(lines, 3, "2.12 + 2.87 = 4.99\n");
	}

	@Test
	public void testAdditionTypeError() {
		String[] rawLines = { "set :x to 2", "add \"hello\" to :x" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		testScriptException(lines, 1, "IncompatibleTypeException");
	}

	@Test
	public void testVariableTypeOverride() {
		String[] rawLines = { "set :x to \"John Doe\"", "display :x", "set :x to 5", "display :x" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		testScriptOutputOut(lines, 1, "John Doe\n");
		SomeUnnamedLanguage.interpretLine(lines.get(2), 2);
		testScriptOutputOut(lines, 3, "5\n");
	}

	@Test
	public void testListSet() {
		String[] rawLines = { "set :list to 1,2, 3" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		DataTypeList t = SULCommands.getListVar(lines.get(0)[1]);
		assertEquals(1, t.getValue(0));
		assertEquals(2, t.getValue(1));
		assertEquals(3, t.getValue(2));
	}

	@Test
	public void testDictSet() {
		String[] rawLines = { "set :dict to \"fname\":\"John\",\"lname\":\"Doe\", \"age\":25" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		DataTypeList t = SULCommands.getListVar(lines.get(0)[1]);
		assertEquals("John", t.getValue("fname"));
		assertEquals("Doe", t.getValue("lname"));
		assertEquals(25, t.getValue("age"));
	}

	@Test
	public void testListSetAndDisplay() {
		String[] rawLines = { "set :list to 1,2, 3", "display :list" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		testScriptOutputOut(lines, 1, "[1, 2, 3]\n");
	}

	@Test
	public void testDictSetAndDisplay() {
		String[] rawLines = { "set :dict to \"fname\":\"John\",\"lname\":\"Doe\", \"age\":25", "display :dict" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		SomeUnnamedLanguage.interpretLine(lines.get(1), 1);
		testScriptOutputOut(lines, 2, "{fname=John, lname=Doe, age=25}\n");
	}

	@Test
	public void testArrayOverride() {
		String[] rawLines = { "set :list to 1,2, 3", "display :list", "set element 0 of :list to 5", "display :list" };
		Script s = new Script(rawLines);
		List<Token[]> lines = s.getLines();
		SomeUnnamedLanguage.interpretLine(lines.get(0), 0);
		testScriptOutputOut(lines, 1, "[1, 2, 3]\n");
		SomeUnnamedLanguage.interpretLine(lines.get(2), 2);
		testScriptOutputOut(lines, 3, "[5, 2, 3]\n");
	}

}
