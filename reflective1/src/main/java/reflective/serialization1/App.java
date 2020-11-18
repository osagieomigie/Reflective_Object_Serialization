package reflective.serialization1;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

import javax.json.JsonObject;

public final class App {

    public static void main(String[] args) {
    	try {
    		// get class with primitive fields 
    		Class aClass = Class.forName("reflective.serialization1.ObjectA");
    		Object obj = aClass.newInstance();
    		
    		// write serialized object to file 
    		FileWriter fw = new FileWriter("ObjectA.json");
			fw.write(singleLineString(obj));
			fw.close();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException e) {
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
}