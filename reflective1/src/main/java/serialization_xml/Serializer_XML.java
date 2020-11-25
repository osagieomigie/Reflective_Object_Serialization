package serialization_xml;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.xml.parsers.*;
import org.w3c.dom.*;


public class Serializer_XML {
	public static Document serializeObject(Object object_instance) throws Exception {
	    DocumentBuilderFactory document_factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder document_builder = document_factory.newDocumentBuilder();
	    Document document = document_builder.newDocument();
	    document.appendChild(document.createElement("serialized"));
	    return serializeHelper(object_instance, document, new IdentityHashMap());
	}

	    private static Document serializeHelper(Object source, Document document, Map object_tracking_map) throws Exception {
	    	// unique object id 
	        String object_id = Integer.toString(object_tracking_map.size());
	        
	        object_tracking_map.put(source, object_id);
	        
	        Class object_class = source.getClass();
	        Element object_info = document.createElement("object");
	        
	        object_info.setAttribute("class", object_class.getName());  //.add("class", object_class.getName());
	        object_info.setAttribute("id", object_id);  //.add("id", object_id);
	        
	        Field [] requiredFields = null;
	        
	        // Determine the fields we need to explore 
	        if (!source.getClass().getSuperclass().getName().equals("java.util.AbstractList")) {
	        	requiredFields = object_class.getDeclaredFields();
	        }else {
	        	requiredFields = new Field[3];
	        	requiredFields[0] = object_class.getDeclaredField("elementData");
	        	requiredFields[1] = object_class.getDeclaredField("size");
	        	requiredFields[2] = object_class.getSuperclass().getDeclaredField("modCount");
	        }
	        
	        // list of fields 
	        //JsonArrayBuilder field_list = Json.createArrayBuilder();
	        for (Field f : requiredFields) {
	        	f.setAccessible(true);
	        	
	        	// list of field info
	        	//JsonObjectBuilder field_info = Json.createObjectBuilder();
	        	Element field = document.createElement("field");
	        	
	            Class fieldDeclareClass = f.getDeclaringClass(); // field declaring class 
	            Object field_obj = f.get(source); // get field value 
	            
	            //object_info.add("type", "object"); // field type
	                        
	        	//field_info.add("name", f.getName()); // field name 
	            field.setAttribute("name", f.getName());
	        	//field_info.add("declaringClass", fieldDeclareClass.getName()); // field declaring class name 
	            field.setAttribute("declaringClass", fieldDeclareClass.getName());
	            Element value = document.createElement("value");
	            Element reference = document.createElement("reference");
	        	
	        	// determine field type 
	        	if (f.getType().isPrimitive()) {
	        		//field_info.add("value", field_obj.toString()); // primitive field 
	        		value.appendChild(document.createTextNode(field_obj.toString()));
	        		field.appendChild(value);
	        		object_info.appendChild(field);
	        	}else {// object field 
	        		if (field_obj == null) {
	        			//field_info.add("reference", "null"); // null object
	        			reference.appendChild(document.createTextNode("null"));
		        		field.appendChild(reference);
		        		object_info.appendChild(field);
	        		}
	        		// Field is an array 
	        		else if (f.getType().isArray()) {
	        	        // add array to map 
	        			String array_id = Integer.toString(object_tracking_map.size());
	        	        object_tracking_map.put(field_obj, array_id);
	        	        //field_info.add("reference", array_id);
	        	        reference.appendChild(document.createTextNode(array_id));
		        		field.appendChild(reference);
		        		object_info.appendChild(field);
	        	        //object_info = serializeArray(field_obj.getClass(), array_id, field_obj, document, object_tracking_map);
	        	        document.getDocumentElement().appendChild(serializeArray(field_obj.getClass(), array_id, field_obj, document, object_tracking_map));
	        		}
	        		// Field is an Object 
	        		else {
	        			if (!object_tracking_map.containsKey(field_obj)) { // add new object to tracking list 
	        				//field_info.add("reference", Integer.toString(object_tracking_map.size()));
	        				reference.appendChild(document.createTextNode(Integer.toString(object_tracking_map.size())));
			        		field.appendChild(reference);
			        		object_info.appendChild(field);
	        				serializeHelper(field_obj, document, object_tracking_map);
	        			}else {
	        				//field_info.add("reference", object_tracking_map.get(field_obj).toString());
	        				reference.appendChild(document.createTextNode(object_tracking_map.get(field_obj).toString()));
			        		field.appendChild(reference);
			        		object_info.appendChild(field);
	        			}
	        		}
	        	}
	        	
	        	//field_list.add(field_info);
	        }
	        
	        // add fields
	        //object_info.add("fields", field_list);
	        
	        document.getDocumentElement().appendChild(object_info);
	        return document;
	        //object_list.add(object_info);
	    }
	    
	    private static Element serializeArray(Class<?> c, String object_id, Object obj, Document document, Map object_tracking_map) {
	    	
	        Class object_class = obj.getClass();
	        //JsonObjectBuilder object_info = Json.createObjectBuilder();
	        Element object_info = document.createElement("object");
	        
	        object_info.setAttribute("class", object_class.getName());  //.add("class", object_class.getName()); // add class name
	        object_info.setAttribute("id", object_id);  //.add("id", object_id);   // add object id   
	        object_info.setAttribute("type", "array");  //.add("type", "array"); // add type

	    	// add array length
	    	int arrLength = Array.getLength(obj);
	    	object_info.setAttribute("length", String.valueOf(arrLength));  //.add("length", String.valueOf(arrLength));
	    	
	    	//JsonArrayBuilder array_list = Json.createArrayBuilder();  
	    	// inspect array elements
	    	for (int i = 0; i < arrLength; i++) {
	    		Element value = document.createElement("value");
		    	Element reference = document.createElement("reference");
	    		Object arrValue = Array.get(obj, i);
	    		if(arrValue != null) {
	    			// primitive object
	    			if (c.getComponentType().isPrimitive()) {
	    				//array_info.add("value", arrValue.toString());
	    				value.appendChild(document.createTextNode(arrValue.toString()));
	    				object_info.appendChild(value);
	    			}
	    			// class object
	    			else {
	    				//array_info.add("reference", Integer.toString(object_tracking_map.size()));
	    				reference.appendChild(document.createTextNode(Integer.toString(object_tracking_map.size())));
	    				object_info.appendChild(reference);
	    				// serialize class object
	    				try {
							serializeHelper(arrValue, document, object_tracking_map);
						} catch (Exception e) {
							e.printStackTrace();
						}
	    			}	
	    		}else {
	    			//array_info.add("value", "null");
	    			value.appendChild(document.createTextNode("null"));
    				object_info.appendChild(value);
	    		}
	    		
	    		//array_list.add(array_info);
	    	}
	    	
	    	// add entries
	        //object_info.add("entries", array_list);
//	    	document.getDocumentElement().appendChild(object_info);
	    	return object_info;
	    }
}
