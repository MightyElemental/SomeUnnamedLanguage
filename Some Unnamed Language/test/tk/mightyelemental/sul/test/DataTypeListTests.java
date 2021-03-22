package tk.mightyelemental.sul.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import tk.mightyelemental.sul.DataTypeList;

public class DataTypeListTests {

	Object[]						arrayValues;
	HashMap<String, Object>	mapValues;

	@Before
	public void initialize() {
		arrayValues = new String[] { "Hello", "World", "!" };

		mapValues = new HashMap<String, Object>();
		mapValues.put("first name", "John");
		mapValues.put("last name", "Doe");
		mapValues.put("age", 50);
		mapValues.put("2", 10);
	}

	@Test
	public void testArrayMode() {
		DataTypeList dtl = new DataTypeList(arrayValues);
		for (int i = 0; i < arrayValues.length; i++) assertEquals(arrayValues[i], dtl.getValue(i));
	}

	@Test
	public void testDictionaryMode() {
		DataTypeList dtl = new DataTypeList();
		dtl.putAllKeyVal(mapValues);
		for (String key : mapValues.keySet()) assertEquals(mapValues.get(key), dtl.getValue(key));
	}

	@Test
	public void testMixedMode() {
		DataTypeList dtl = new DataTypeList(arrayValues);
		for (int i = 0; i < arrayValues.length; i++) assertEquals(arrayValues[i], dtl.getValue(i));
		dtl.putAllKeyVal(mapValues);
		for (String key : mapValues.keySet()) assertEquals(mapValues.get(key), dtl.getValue(key));
		// check that the value was overwritten
		assertTrue(10 == (int) dtl.getValue(2));
	}

}
