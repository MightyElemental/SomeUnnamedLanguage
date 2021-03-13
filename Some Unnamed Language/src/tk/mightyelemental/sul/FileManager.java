package tk.mightyelemental.sul;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {

	/**
	 * Loads the lines from a specified file.
	 * 
	 * @param location the location of the file
	 * @return An array containing the lines from the file
	 * @throws FileNotFoundException If the file does not exist, is a directory rather than a regular file, or for some
	 *         other reason cannot be opened for reading
	 * @throws IOException If an I/O error occurs
	 */
	public static String[] loadLinesFromFile( String location ) throws FileNotFoundException, IOException {
		File f = new File(location);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String[] result = br.lines().toArray(String[]::new);
		br.close();
		return result;
	}

	/**
	 * Loads a script from a specified file.
	 * 
	 * @param location the location of the script file
	 * @return A new {@link Script} object containing the data from the file
	 * @throws FileNotFoundException If the file does not exist, is a directory rather than a regular file, or for some
	 *         other reason cannot be opened for reading
	 * @throws IOException If an I/O error occurs
	 * @see #loadLinesFromFile(String)
	 */
	public static Script loadScriptFromFile( String location ) throws FileNotFoundException, IOException {
		return new Script(loadLinesFromFile(location));
	}

}
