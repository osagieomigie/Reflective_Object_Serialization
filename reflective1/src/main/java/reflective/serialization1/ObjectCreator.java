package reflective.serialization1;

import serialization.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.json.*;
import javax.json.stream.JsonGenerator;

public final class ObjectCreator {

    public static String createObject(char userChar) {
    	
    	// important variables
    	String json_string = " ";
    	ObjectA objA = new ObjectA();
    	ObjectB objB = new ObjectB();
		ObjectB objB2 = new ObjectB();
		ObjectC objC = new ObjectC();
		ObjectD objD = new ObjectD();
		ObjectE objE = new ObjectE();
		
    	Scanner subInput = new Scanner(System.in);
    	String subUserInput = "";
    	String subInput2 = "";
    	int tmpHolder = 0;
    	
    	Boolean wrongInput = false;
	    	 
    	// get object with primitive fields 
    	if (userChar == 'a') {
    		System.out.println("This object has 3 fields (x, y, z). Set their value with the following prompts: ");
    		wrongInput = false; // reset wrong input value
    		
    		while (!wrongInput) {
	    		try {
	    			System.out.println(" x is a type integer, enter in an Integer value for x:");
	        		subUserInput = subInput.nextLine();
	        		objA.setX(Integer.parseInt(subUserInput));
	        		
	        		System.out.println(" y is a type Integer, enter in an Integer value for y:");
	        		subUserInput = subInput.nextLine();
	        		objA.setY(Integer.parseInt(subUserInput));
	        		
	        		System.out.println(" z is of type Double, enter in a Double value for z:");
	        		subUserInput = subInput.nextLine();
	        		objA.setZ(Double.parseDouble(subUserInput));
	        		wrongInput = true;
	        	}catch (NumberFormatException e) {
	        		System.out.println("Don't be a clown, enter in correct values (x, y -> integers, z -> double)!");
	        	}
    		}
    		
    		System.out.println(prettifyString(objA) + "\n");
    		json_string = singleLineString(objA);
    		return json_string;
    	}
    	// get circularly referenced object
    	else if (userChar == 'b') {
    		System.out.println(" This object references it self (2 objects). Each object has an 'x' variable. Set their variables with the following prompts:");
    		wrongInput = false; // reset wrong input value
    		
    		while (!wrongInput) {
	    		try {
	    			System.out.println(" Set the 'x' field of the first object; enter in an Integer value for x:");
	        		subUserInput = subInput.nextLine();
	        		objB.setX(Integer.parseInt(subUserInput));
	        		
	        		System.out.println(" Set the 'x' field of the second object; enter in an Integer value for x:");
	        		subUserInput = subInput.nextLine();
	        		objB2.setX(Integer.parseInt(subUserInput));

	        		wrongInput = true;
	        	}catch (NumberFormatException e) {
	        		System.out.println("Don't be a clown, enter in correct values (field 'x' -> integer)!");
	        	}
    		}
    		
    		objB2.setObj(objB);
    		objB.setObj(objB2);
    		System.out.println(prettifyString(objB) + "\n");
    		json_string = singleLineString(objB);
    		return json_string;
    	}
    	
    	// get object that contains an array of primitives
    	else if (userChar == 'c') {
    		wrongInput = false; // reset wrong input value
    		System.out.println(" This object has an array of Integers. Set each index of the array:");
    		
    		while (!wrongInput) {
	    		for (int i = 0; i < 5; i++) {
	    			System.out.println(String.format(" Set the %d index of the array. Enter in an integer value:", i));
	    			
	    			subUserInput = subInput.nextLine();
	    			try {
	    				objC.setArrayIndex(i, Integer.parseInt(subUserInput));
	    				wrongInput = true;
	    			}catch (NumberFormatException e) {
	    				System.out.println("Don't be a clown, enter in correct values (indecies -> integer)!");
	    				wrongInput = false;
	    				break;
	    			}
	    		}
    		}
    		
    		System.out.println(prettifyString(objC) + "\n");
    		json_string = singleLineString(objC);
    		return json_string;
    	}
    	// get Object that contains an array with references to other Objects
    	else if (userChar == 'd') {
    		System.out.println(" This object has an array(length 5) of Object references. Set each index of the array, to the object of your choosing. Enter 'd' when finished:");
    		
    		subUserInput = "reset";
    		subInput2 = " ";
    		
    		while(subUserInput.charAt(0) != 'd') {
    			System.out.println(" Enter in the index (e.g. '1' to set 2nd index to an Object A reference). Enter 'd' when done");
    			subUserInput = subInput.nextLine();
    			
    			// error check user input
    			try {
    				tmpHolder = Integer.parseInt(subUserInput);
    				if (tmpHolder > 4 || tmpHolder < 0) {
    					System.out.println("Indecies can only be between 0-4!\n");
    				}else {
    					// select object
    			    	while(true) {
    			    		System.out.println("\nSelect an object to serialize by selection from the following options by entering the number in front them (e.g. 'a' or 'A'): \n");
    				    	System.out.println(" (a) A simple object with only primitives for instance variables");
    				    	System.out.println(" (b) An object that contains references to other objects");
    				    	
    				    	subInput2 = subInput.nextLine();
    				    	switch(Character.toLowerCase(subInput2.charAt(0))) {
    				    		case 'a':
    				    			objD.setArrayIndex(tmpHolder, new ObjectA());
    				    			break;
    				    		case 'b':
    				    			objB.setObj(new ObjectB());
    				    			objB.setX(1);
    				    			objD.setArrayIndex(tmpHolder, objB);
    				    			break;
    				    		default:
    				    			System.out.println("Don't be a clown, follow the menu!\n");
    			    				continue;
    				    	}
    				    	break; // users input is correct
    			    	}
    				}
    			}catch (NumberFormatException e) {
    				System.out.println("Don't be a clown, enter in correct values (indecies -> integer)!\n");
    				continue;
    			}
    		}
    		
    		subUserInput = ""; // reset 
    		System.out.println(prettifyString(objD) + "\n");
    		json_string = singleLineString(objD);
    		return json_string;
    	}
    	// get Object that contains an ArrayList with references to other Objects
    	else if (userChar == 'e') {
    		System.out.println(" This object has an ArrayList of object references. Set each index of the ArrayList. Enter 'd' when finished:");
    		subUserInput = "reset";
    		subInput2 = " ";
    		
    		while(subUserInput.charAt(0) != 'd') {
    			System.out.println(" Enter in the index. E.g. '1' to set 2nd index to Object A. Enter 'd' when finished");
    			subUserInput = subInput.nextLine();
    			
    			// end if user is done entering values 
    			if (subUserInput.charAt(0) == 'd')
    				break;
    			
    			tmpHolder = Integer.parseInt(subUserInput);
				if (tmpHolder < 0) {
					System.out.println("Indecies can't be less than 0!\n");
				}else {
					// select object
			    	while(true) {
			    		System.out.println("\nSelect an object to serialize by selection from the following options by entering the number in front them (e.g. 'a' or 'A'): \n");
				    	System.out.println(" (a) A simple object with only primitives for instance variables");
				    	System.out.println(" (b) An object that contains references to other objects");
				    	
				    	subInput2 = subInput.nextLine();
				    	switch(Character.toLowerCase(subInput2.charAt(0))) {
				    		case 'a':
				    			objE.setArrayListIndex(tmpHolder, new ObjectA());
				    			break;
				    		case 'b':
				    			objB.setObj(new ObjectB());
				    			objE.setArrayListIndex(tmpHolder, objB);
				    			break;
				    		default:
				    			System.out.println("Don't be a clown, follow the menu!\n");
			    				continue;
				    	}
				    	break; // users input is correct
			    	}
				}
    		}

    		subUserInput = ""; // reset 
    		System.out.println(prettifyString(objE) + "\n");
     		json_string = singleLineString(objE);
     		
    		return json_string;
    	}
	 
    	else {
    		System.out.println(" Enter in a valid option!!");
    		return "error";
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
