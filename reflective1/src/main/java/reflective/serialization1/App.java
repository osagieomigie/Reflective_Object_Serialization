package reflective.serialization1;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

import javax.json.JsonObject;

public final class App {

    public static void main(String[] args) {
    	try {
    		// get object with primitive fields 
    		 ObjectA objA = new ObjectA();
    		
    		// get circularly referenced object
//    		ObjectB objB = new ObjectB();
//    		ObjectB objB2 = new ObjectB();
//    		objB2.setObj(objB);
//    		objB.setObj(objB2);
    		
    		// write serialized object to file 
    		FileWriter fw = new FileWriter("ObjectA.json");
    		//FileWriter fw = new FileWriter("ObjectB.json");
			fw.write(singleLineString(objA));
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
}