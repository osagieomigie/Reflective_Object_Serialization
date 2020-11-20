import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.*;
import javax.json.*;

public class Serializer {
	
    public static JsonObject serializeObject(Object object) throws Exception {
        JsonArrayBuilder object_list = Json.createArrayBuilder();
        serializeHelper(object, object_list, new IdentityHashMap());
        JsonObjectBuilder json_base_object = Json.createObjectBuilder();
        json_base_object.add("objects", object_list);
        return json_base_object.build();
    }

    private static void serializeHelper(Object source, JsonArrayBuilder object_list, Map object_tracking_map) throws Exception {
    	// unique object id 
        String object_id = Integer.toString(object_tracking_map.size());
        
        object_tracking_map.put(source, object_id);
        
        Class object_class = source.getClass();
        JsonObjectBuilder object_info = Json.createObjectBuilder();
        
        object_info.add("class", object_class.getName());
        object_info.add("id", object_id);   // stops at object id   
        
        // list of fields 
        JsonArrayBuilder field_list = Json.createArrayBuilder();
        for (Field f : object_class.getDeclaredFields()) {
        	f.setAccessible(true);
        	
        	// list of field info
        	JsonObjectBuilder field_info = Json.createObjectBuilder();
            Class fieldDeclareClass = f.getDeclaringClass(); // field declaring class 
            Object field_obj = f.get(source); // get field value 
            
            object_info.add("type", "object"); // field type
                        
        	field_info.add("name", f.getName()); // field name 
        	field_info.add("declaringClass", fieldDeclareClass.getName()); // field declaring class name 
        	
        	// determine field type 
        	if (f.getType().isPrimitive()) {
        		field_info.add("value", field_obj.toString()); // primitive field 
        	}else {// object field 
        		if (field_obj == null) {
        			field_info.add("reference", "null"); // null object
        		}
        		// Field is an array 
        		else if (f.getType().isArray()) {
        	        // add array to map 
        			String array_id = Integer.toString(object_tracking_map.size());
        	        object_tracking_map.put(field_obj, array_id);
        	        field_info.add("reference", array_id);
        			inspectArray(field_obj.getClass(), array_id, field_obj, object_list, object_tracking_map);
        		}else {
        			if (!object_tracking_map.containsKey(field_obj)) { // add new object to tracking list 
        				field_info.add("reference", Integer.toString(object_tracking_map.size()));
        				serializeHelper(field_obj, object_list, object_tracking_map);
        			}else {
        				field_info.add("reference", object_tracking_map.get(field_obj).toString());
        			}
        		}
        	}
        	
        	field_list.add(field_info);
            
        }
        
        // add fields
        object_info.add("fields", field_list);
        object_list.add(object_info);
    }
    
    private static void inspectArray(Class<?> c, String object_id, Object obj, JsonArrayBuilder object_list, Map object_tracking_map) {
    	
        Class object_class = obj.getClass();
        JsonObjectBuilder object_info = Json.createObjectBuilder();
        
        object_info.add("class", object_class.getName());
        object_info.add("id", object_id);   // stops at object id   
        object_info.add("type", "array"); // add type
        
    	// add array length
    	int arrLength = Array.getLength(obj);
    	object_info.add("length", String.valueOf(arrLength));
    	
    	JsonArrayBuilder array_list = Json.createArrayBuilder();
    
    	// inspect array elements
    	for (int i = 0; i < arrLength; i++) {
    		JsonObjectBuilder array_info = Json.createObjectBuilder();
    		Object arrValue = Array.get(obj, i);
    		if(arrValue != null) {
    			// primitive object
    			if (c.getComponentType().isPrimitive())
    				array_info.add("value", arrValue.toString());
    			// class object
    			else {
    				array_info.add("reference", Integer.toString(object_tracking_map.size()));
    				
    				// serialize class object
    				try {
						serializeHelper(arrValue, object_list, object_tracking_map);
					} catch (Exception e) {
						e.printStackTrace();
					}
    			}	
    		}else {
    			array_info.add("value", "null");
    		}
    		
    		array_list.add(array_info);
    	}
    	
    	// add entries
        object_info.add("entries", array_list);
        object_list.add(object_info);
    }

}