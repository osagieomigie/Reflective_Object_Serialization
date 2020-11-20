package reflective.serialization1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.*;
import java.util.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

public final class App {

    public static void main(String[] args) {
    	try {
    		// get object with primitive fields 
    		ObjectA objA = new ObjectA();
    		
    		// get circularly referenced object
    		ObjectB objB = new ObjectB();
    		ObjectB objB2 = new ObjectB();
    		objB2.setObj(objB);
    		objB.setObj(objB2);
    		
    		// get object that contains an array of primitives  
    		ObjectC objC = new ObjectC();
    		
    		// get Object that contains an array with references to other Objects
    		ObjectD objD = new ObjectD();
    		objD.setArrayIndex(3, objA);
    		 
    		// get Object that contains an ArrayList with references to other Objects
    		ObjectE objE = new ObjectE();
     		objE.setArrayListIndex(0, objA);
    		// write serialized object to file 
    		FileWriter fw = new FileWriter("ObjectE.json");

//			fw.write(singleLineString(objE));
			fw.write(prettifyString(objE));
			fw.close();
		} catch (IOException | SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}
    }
    
    // return serialized object 
    public static String singleLineString(Object obj) {
		JsonObject json_object = null;
		try {
			json_object = Serializer.serializeObject(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(json_object.toString());
		return json_object.toString();
	}
    
    public static String prettifyString(Object obj) {
    	JsonObject json_object = null;
		try {
			json_object = Serializer.serializeObject(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	Map<String, Object> settings_map = new HashMap<>();
    	StringWriter string_writer = new StringWriter();
    	settings_map.put(JsonGenerator.PRETTY_PRINTING, true);
    	JsonWriterFactory writer_factory = Json.createWriterFactory(settings_map);
    	JsonWriter json_writer = writer_factory.createWriter(string_writer);
    	
    	json_writer.writeObject(json_object);
    	json_writer.close();
    	
    	String prettyPrint = string_writer.toString();
    	
    	return prettyPrint;
    }
}