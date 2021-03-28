package tk.mightyelemental.sul;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DataTypeListTests {

	Object[]					arrayValues;
	Map<String, Object>	mapValues;

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

	@Test
	public void testDataTypeListToStringArrayMode() {
		DataTypeList arrayMode = new DataTypeList(1, 2, 3);
		assertEquals("[1, 2, 3]", arrayMode.toString());
	}

	@Test
	public void testDataTypeListToStringDictMode() {
		DataTypeList dictMode = new DataTypeList();
		dictMode.putKeyVal("fname", "John");
		dictMode.putKeyVal("lname", "Doe");
		dictMode.putKeyVal("age", 25);
		assertEquals("{fname=John, lname=Doe, age=25}", dictMode.toString());

		DataTypeList mixedMode = new DataTypeList(1);
		mixedMode.putKeyVal("name", "John Doe");
		assertEquals("{0=1, name=John Doe}", mixedMode.toString());
	}

}
