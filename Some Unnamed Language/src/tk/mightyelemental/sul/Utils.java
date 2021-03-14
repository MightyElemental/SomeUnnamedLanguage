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

}
