package tk.mightyelemental.sul;

import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.stream.Collectors.joining;

public class DataTypeList {

	/** Where the data for the list is stored. */
	private Map<String, Object> valueMap;

	/**
	 * The next available index in the list. This allows the {@code DataTypeList} to act as an array.
	 */
	private int index = 0;

	/** Defines if the list is running in array mode or mixed/dictionary mode. */
	private boolean arrayOnly = true;

	/**
	 * Create a new list object.
	 * 
	 * @param objects a list of objects to store starting at {@linkplain #index} 0.
	 */
	public DataTypeList( Object... objects ) {
		valueMap = new LinkedHashMap<String, Object>();
		for (Object o : objects) append(o);
	}

	/**
	 * Get the value of a specific key.
	 * 
	 * @param key the key of the data
	 * @return The data stored at the key
	 */
	public Object getValue( String key ) {
		if (!valueMap.containsKey(key)) SULExceptions.unknownListIndexException(-1, (Token[]) null);
		return valueMap.get(key);
	}

	/**
	 * Get the value at a specific index.
	 * 
	 * @param index the index of the data
	 * @return The data found at the index
	 * @see #getValue(String)
	 */
	public Object getValue( int index ) {
		return getValue(Integer.toString(index));
	}

	/**
	 * Adds an object to the list at the next available {@linkplain #index}.<br>
	 * Can overwrite data if a value was previously set in {@linkplain #putKeyVal(String, Object)}.
	 * 
	 * @param o the object to add to the list
	 */
	public void append( Object o ) {
		valueMap.put(Integer.toString(index++), o);
	}

	/**
	 * Assign a value to a specific key.
	 * 
	 * @param key the name of the index
	 * @param o the object to assign to the index
	 */
	public void putKeyVal( String key, Object o ) {
		valueMap.put(key, o);
		arrayOnly = false;
	}

	/**
	 * Assign a value found at an index to a new value. This will ignore the {@linkplain #index}.
	 * 
	 * @param index the index to put the value
	 * @param o the object to place at the index
	 * @see #putKeyVal(String, Object)
	 */
	public void putKeyVal( int index, Object o ) {
		putKeyVal(Integer.toString(index), o);
	}

	/**
	 * Add a pre-made map of keys and values to the {@linkplain #valueMap}.
	 * 
	 * @param values the key/value map to add to the data set
	 */
	public void putAllKeyVal( Map<String, Object> values ) {
		valueMap.putAll(values);
	}

	@Override
	public String toString() {
		if (arrayOnly) {
			String values = valueMap.entrySet().stream().map(e -> e.getValue().toString()).collect(joining(", "));
			return String.format("[%s]", values);
		} else {
			String values = valueMap.entrySet().stream().map(Object::toString).collect(joining(", "));
			return String.format("{%s}", values);
		}
	}

}
