package reflective.serialization1;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.PrintStream;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.Before;

import reflective.serialization1.*;
import serialization.*;

/**
 * Unit test for correct serialization of objects.
 */
public class AppTest {
	private ObjectA testA = new ObjectA();
	private ObjectB testB = new ObjectB();
	private ObjectB testB2 = new ObjectB();
	private ObjectC testC = new ObjectC();
	private ObjectE testE = new ObjectE();
	private JsonReader json_reader;
	private JsonObject json_object;
	
	@Before
	public void setUp() {
		// set field for field for first object
		testA.setX(2);
		testA.setY(15);
		testA.setZ(100.2);
		
		// set fields of circularly referenced object
		testB2.setX(4);
		testB2.setObj(testB);
		testB.setX(2);
		testB.setObj(testB2);
		
		// set array index of objectC 
		testC.setArrayIndex(1, 2);
		
		// set ArrayList index of objectE
		testE.setArrayListIndex(1, new ObjectA());
	}
	
    @Test
    public void testPrimitiveSerialization() {
    	String result = ObjectCreator.singleLineString(testA);
    	String expected = "{\"objects\":[{\"class\":\"reflective.serialization1.ObjectA\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"x\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"2\"},{\"name\":\"y\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"15\"},{\"name\":\"z\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"100.2\"}]}]}";
    	assertEquals(expected, result);
    }
    
    @Test
    public void testPrimitiveDeserialization() throws Exception {
    	// serialized object string
    	String objectA = "{\"objects\":[{\"class\":\"reflective.serialization1.ObjectA\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"x\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"2\"},{\"name\":\"y\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"15\"},{\"name\":\"z\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"100.2\"}]}]}";
    	
    	// convert to json
    	json_reader = Json.createReader(new StringReader(objectA));
		json_object = json_reader.readObject();
		
		// deserialize object
    	Object returnedObject = Deserializer.deserializeObject(json_object);
    	
    	// serialize object again
    	String result = ObjectCreator.singleLineString(returnedObject);
    	
    	// test if result is similar
    	assertEquals(objectA, result);
    }
    
    @Test
    public void testCircularSerialization() {
    	String result = ObjectCreator.singleLineString(testB);
    	String expected = "{\"objects\":[{\"class\":\"reflective.serialization1.ObjectB\",\"id\":\"1\",\"type\":\"object\",\"fields\":[{\"name\":\"obj\",\"declaringClass\":\"reflective.serialization1.ObjectB\",\"reference\":\"0\"},{\"name\":\"x\",\"declaringClass\":\"reflective.serialization1.ObjectB\",\"value\":\"4\"}]},{\"class\":\"reflective.serialization1.ObjectB\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"obj\",\"declaringClass\":\"reflective.serialization1.ObjectB\",\"reference\":\"1\"},{\"name\":\"x\",\"declaringClass\":\"reflective.serialization1.ObjectB\",\"value\":\"2\"}]}]}"; 
    	assertEquals(expected, result);
    }
    
    @Test
    public void testCircularDeserialization() throws Exception {
    	// serialized object string
    	String objectB = "{\"objects\":[{\"class\":\"reflective.serialization1.ObjectB\",\"id\":\"1\",\"type\":\"object\",\"fields\":[{\"name\":\"obj\",\"declaringClass\":\"reflective.serialization1.ObjectB\",\"reference\":\"0\"},{\"name\":\"x\",\"declaringClass\":\"reflective.serialization1.ObjectB\",\"value\":\"4\"}]},{\"class\":\"reflective.serialization1.ObjectB\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"obj\",\"declaringClass\":\"reflective.serialization1.ObjectB\",\"reference\":\"1\"},{\"name\":\"x\",\"declaringClass\":\"reflective.serialization1.ObjectB\",\"value\":\"2\"}]}]}"; 
    	
    	// convert to json
    	json_reader = Json.createReader(new StringReader(objectB));
		json_object = json_reader.readObject();
		
		// deserialize object
    	Object returnedObject = Deserializer.deserializeObject(json_object);
    	
    	// serialize object again
    	String result = ObjectCreator.singleLineString(returnedObject);
    	
    	// test if result is similar
    	assertEquals(objectB, result);
    }
    
    @Test
    public void testArraySerialization() {
    	String result = ObjectCreator.singleLineString(testC);
    	String expected = "{\"objects\":[{\"class\":\"[I\",\"id\":\"1\",\"type\":\"array\",\"length\":\"5\",\"entries\":[{\"value\":\"0\"},{\"value\":\"2\"},{\"value\":\"0\"},{\"value\":\"0\"},{\"value\":\"0\"}]},{\"class\":\"reflective.serialization1.ObjectC\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"a\",\"declaringClass\":\"reflective.serialization1.ObjectC\",\"reference\":\"1\"}]}]}";
    	assertEquals(expected, result);
    }
    
    @Test
    public void testArrayDeserialization() throws Exception {
    	// serialized object string
    	String objectC = "{\"objects\":[{\"class\":\"[I\",\"id\":\"1\",\"type\":\"array\",\"length\":\"5\",\"entries\":[{\"value\":\"0\"},{\"value\":\"2\"},{\"value\":\"0\"},{\"value\":\"0\"},{\"value\":\"0\"}]},{\"class\":\"reflective.serialization1.ObjectC\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"a\",\"declaringClass\":\"reflective.serialization1.ObjectC\",\"reference\":\"1\"}]}]}";
    	
    	// convert to json
    	json_reader = Json.createReader(new StringReader(objectC));
		json_object = json_reader.readObject();
		
		// deserialize object
    	Object returnedObject = Deserializer.deserializeObject(json_object);
    	
    	// serialize object again
    	String result = ObjectCreator.singleLineString(returnedObject);
    	
    	// test if result is similar
    	assertEquals(objectC, result);
    }
    
    @Test
    public void testArrayListSerialization() {
    	String result = ObjectCreator.singleLineString(testE);
    	String expected = "{\"objects\":[{\"class\":\"reflective.serialization1.ObjectA\",\"id\":\"3\",\"type\":\"object\",\"fields\":[{\"name\":\"x\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"1\"},{\"name\":\"y\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"3\"},{\"name\":\"z\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"1.0\"}]},{\"class\":\"[Ljava.lang.Object;\",\"id\":\"2\",\"type\":\"array\",\"length\":\"4\",\"entries\":[{\"value\":\"null\"},{\"reference\":\"3\"},{\"value\":\"null\"},{\"value\":\"null\"}]},{\"class\":\"java.util.ArrayList\",\"id\":\"1\",\"type\":\"object\",\"fields\":[{\"name\":\"elementData\",\"declaringClass\":\"java.util.ArrayList\",\"reference\":\"2\"},{\"name\":\"size\",\"declaringClass\":\"java.util.ArrayList\",\"value\":\"4\"},{\"name\":\"modCount\",\"declaringClass\":\"java.util.AbstractList\",\"value\":\"4\"}]},{\"class\":\"reflective.serialization1.ObjectE\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"b\",\"declaringClass\":\"reflective.serialization1.ObjectE\",\"reference\":\"1\"}]}]}";
    	assertEquals(expected, result);
    }
    
    @Test
    public void testArrayListDeserialization() throws Exception {
    	// serialized object string
    	String objectE = "{\"objects\":[{\"class\":\"reflective.serialization1.ObjectA\",\"id\":\"3\",\"type\":\"object\",\"fields\":[{\"name\":\"x\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"1\"},{\"name\":\"y\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"3\"},{\"name\":\"z\",\"declaringClass\":\"reflective.serialization1.ObjectA\",\"value\":\"1.0\"}]},{\"class\":\"[Ljava.lang.Object;\",\"id\":\"2\",\"type\":\"array\",\"length\":\"4\",\"entries\":[{\"value\":\"null\"},{\"reference\":\"3\"},{\"value\":\"null\"},{\"value\":\"null\"}]},{\"class\":\"java.util.ArrayList\",\"id\":\"1\",\"type\":\"object\",\"fields\":[{\"name\":\"elementData\",\"declaringClass\":\"java.util.ArrayList\",\"reference\":\"2\"},{\"name\":\"size\",\"declaringClass\":\"java.util.ArrayList\",\"value\":\"4\"},{\"name\":\"modCount\",\"declaringClass\":\"java.util.AbstractList\",\"value\":\"4\"}]},{\"class\":\"reflective.serialization1.ObjectE\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"b\",\"declaringClass\":\"reflective.serialization1.ObjectE\",\"reference\":\"1\"}]}]}";
    	
    	// convert to json
    	json_reader = Json.createReader(new StringReader(objectE));
		json_object = json_reader.readObject();
		
		// deserialize object
    	Object returnedObject = Deserializer.deserializeObject(json_object);
    	
    	// serialize object again
    	String result = ObjectCreator.singleLineString(returnedObject);
    	
    	// test if result is similar
    	assertEquals(objectE, result);
    }
}
