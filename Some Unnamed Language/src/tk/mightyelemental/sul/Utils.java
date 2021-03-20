package tk.mightyelemental.sul;

public class Utils {

	/**
	 * If a double is an integer, it removes the decimal from the resulting string.
	 * 
	 * @param num the number to display
	 * @return The number in String form
	 */
	public static String numberToString( double num ) {
		if ((num == Math.floor(num)) && !Double.isInfinite(num)) {
			return String.valueOf((int) Math.floor(num));
		}
		return String.valueOf(num);
	}

	/**
	 * Test if a String is a valid number
	 * 
	 * @param val the String to test
	 * @return {@code true} if the String is a valid number, {@code false} otherwise
	 */
	public static boolean isNumber( String val ) {
		try {
			Double.parseDouble(val);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
