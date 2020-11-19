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
        object_info.add("type", "object");
        
        // list of fields 
        JsonArrayBuilder field_list = Json.createArrayBuilder();
        for (Field f : object_class.getDeclaredFields()) {
        	f.setAccessible(true);
        	
        	// list of field info
        	JsonObjectBuilder field_info = Json.createObjectBuilder();
            Class fieldDeclareClass = f.getDeclaringClass();
            Object field_obj = f.get(source);
            
        	field_info.add("name", f.getName()); // field name 
        	field_info.add("declaringClass", fieldDeclareClass.getName()); // field declaring class name 
        	
        	// determine field type 
        	if (f.getType().isPrimitive()) {
        		field_info.add("value", field_obj.toString()); // primitive field 
        	}else {// object field 
        		if (field_obj == null) {
        			field_info.add("reference", "null"); // null object
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

}