package tk.mightyelemental.sul;

import java.util.HashMap;

public class DataTypeList {

	private HashMap<String, Object> valueMap;

	/**
	 * The next available index in the list.<br>
	 * This allows the {@code DataTypeList} to act as an array.
	 */
	int index = 0;

	public DataTypeList( Object... objects ) {
		valueMap = new HashMap<String, Object>();
		for (Object o : objects) append(o);
	}

	public Object getValue( String key ) {
		return valueMap.get(key);
	}

	public Object getValue( int index ) {
		String key = Integer.toString(index);
		if (!valueMap.containsKey(key)) SULExceptions.unknownListIndexException(-1, (Token[]) null);
		return getValue(key);
	}

	/**
	 * Adds an object to the list at the next available {@linkplain #index}.<br>
	 * Can overwrite data if a value was previously set in {@linkplain #setKeyVal(String, Object)}.
	 * 
	 * @param o the object to add to the list
	 */
	public void append( Object o ) {
		valueMap.put(Integer.toString(index++), o);
	}

	/**
	 * Assign a value to a specific key.<br>
	 * 
	 * @param key the name of the index
	 * @param o the object to assign to the index
	 */
	public void setKeyVal( String key, Object o ) {
		valueMap.put(key, o);
	}

	public void setKeyVal( int index, Object o ) {
		setKeyVal(Integer.toString(index), o);
	}

}
