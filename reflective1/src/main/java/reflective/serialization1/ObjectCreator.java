package reflective.serialization1;

import serialization.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import serialization_xml.Serializer_XML;
import serialization_xml.Deserializer_XML;

public final class ObjectCreator {
	
	// important variables
	private static String json_string = " ";
	private static ObjectA objA = new ObjectA();
	private static ObjectB objB = new ObjectB();
	private static ObjectB objB2 = new ObjectB();
	private static ObjectC objC = new ObjectC();
	private static ObjectD objD = new ObjectD();
	private static ObjectE objE = new ObjectE();
	
	private static Scanner subInput = new Scanner(System.in);
	private static String subUserInput = "";
	private static String subInput2 = "";
	private static int tmpHolder = 0;
	
	private static Boolean wrongInput = false;
	
    public static String createObject(char userChar, char type) {
    	
    	// create new object per request
    	objA = new ObjectA();
    	objB = new ObjectB();
    	objB2 = new ObjectB();
    	objC = new ObjectC();
    	objD = new ObjectD();
    	objE = new ObjectE();
	    	 
    	// get object with primitive fields 
    	if (userChar == 'a') {
    		ObjectA tmpA = createPrimitiveObject();
    		
    		// user wants to serialize xml string 
    		if (type == 'x') {
    			System.out.println(prettifyXMLString(tmpA) + "\n");
    			return singleLineXMLString(tmpA);
    		}
    		
    		System.out.println(prettifyString(tmpA) + "\n");
    		return singleLineString(tmpA);
    	}
    	// get circularly referenced object
    	else if (userChar == 'b') {
    		ObjectB tmpb = createCircularRefObject();
    		
    		// user wants to serialize xml string 
    		if (type == 'x') {
    			System.out.println(prettifyXMLString(tmpb) + "\n");
    			return singleLineXMLString(tmpb);
    		}
    		
    		System.out.println(prettifyString(tmpb) + "\n");
    		return singleLineString(tmpb);
    	}
    	
    	// get object that contains an array of primitives
    	else if (userChar == 'c') {    		
    		ObjectC tmpC = createPrimitiveArray();
    		
    		// user wants to serialize xml string 
    		if (type == 'x') {
    			System.out.println(prettifyXMLString(tmpC) + "\n");
    			return singleLineXMLString(tmpC);
    		}
    		
    		System.out.println(prettifyString(tmpC) + "\n");
    		return singleLineString(tmpC);
    	}
    	// get Object that contains an array with references to other Objects
    	else if (userChar == 'd') {
    		ObjectD tmpD = createObjectRefArray();
    		
    		// user wants to serialize xml string 
    		if (type == 'x') {
    			System.out.println(prettifyXMLString(tmpD) + "\n");
    			return singleLineXMLString(tmpD);
    		}
    		
    		System.out.println(prettifyString(tmpD) + "\n");
    		return singleLineString(tmpD);
    	}
    	// get Object that contains an ArrayList with references to other Objects
    	else if (userChar == 'e') {
    		ObjectE tmpE = createObjectRefArrayList();
    		
    		// user wants to serialize xml string 
    		if (type == 'x') {
    			System.out.println(prettifyXMLString(tmpE) + "\n");
    			return singleLineXMLString(tmpE);
    		}
    		
    		System.out.println(prettifyString(tmpE) + "\n");
     		return singleLineString(tmpE);
    	}
    	
    	// invalid user input 
    	else {
    		System.out.println(" Enter in a valid option!!");
    		return "error";
    	}
    }
    
    // return serialized primitive object 
    private static ObjectA createPrimitiveObject() {
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
		
		return objA;
    }
    
    // return serialized circular referenced object
    private static ObjectB createCircularRefObject() {
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
		return objB;
    }
    
    // return serialized primitive array
    private static ObjectC createPrimitiveArray() {
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
		
		return objC;
    }
    
    // return serialized array with object references
    private static ObjectD createObjectRefArray() {
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
			    		printObjectRefMenu();
				    	
				    	subInput2 = subInput.nextLine();
				    	switch(Character.toLowerCase(subInput2.charAt(0))) {
				    		case 'a':
				    			objD.setArrayIndex(tmpHolder, createPrimitiveObject());
				    			break;
				    		case 'b':
				    			objD.setArrayIndex(tmpHolder, createCircularRefObject());
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
		return objD;
    }
    
    // return serialized ArrayList with object reference
    private static ObjectE createObjectRefArrayList() {
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
		    		printObjectRefMenu();
			    	
			    	subInput2 = subInput.nextLine();
			    	switch(Character.toLowerCase(subInput2.charAt(0))) {
			    		case 'a':
			    			objE.setArrayListIndex(tmpHolder, createPrimitiveObject());
			    			break;
			    		case 'b':
			    			//objB.setObj(new ObjectB());
			    			objE.setArrayListIndex(tmpHolder, createCircularRefObject());
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
		return objE;
    }
    
    private static void printObjectRefMenu() {
    	System.out.println("\nSelect an object to serialize by selection from the following options by entering the number in front them (e.g. 'a' or 'A'): \n");
    	System.out.println(" (a) A simple object with only primitives for instance variables");
    	System.out.println(" (b) An object that contains references to other objects");
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
    
    // return serialized object 
    public static String singleLineXMLString(Object obj) {
    	Document document;
    	StringWriter writer = new StringWriter();
		try {
			document = Serializer_XML.serializeObject(obj);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource dom_source = new DOMSource(document);
	       
	        StreamResult stream_result = new StreamResult(writer);
	        transformer.transform(dom_source, stream_result);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        return writer.toString();
    }
    
    // return serialized object 
    public static String prettifyXMLString(Object obj) {
    	Document document = null;
    	try {
			document = Serializer_XML.serializeObject(obj);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	        DOMSource domSource = new DOMSource(document);
	        StreamResult result = new StreamResult(new StringWriter());
	        transformer.transform(domSource, result);
	        return result.getWriter().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
