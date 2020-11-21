import java.lang.reflect.*;
import java.util.*;
import javax.json.*;

/**
 * CPSC 501 
 * Incomplete JSON deserializer
 *
 * @author Jonathan Hudson, Osa Omigie 
 */

public class Deserializer {

    public static Object deserializeObject(JsonObject json_object) throws Exception {
        JsonArray object_list = json_object.getJsonArray("objects");
        Map object_tracking_map = new HashMap();
        createInstances(object_tracking_map, object_list);
        assignFieldValues(object_tracking_map, object_list);
        return object_tracking_map.get("0");
    }

    private static void createInstances(Map object_tracking_map, JsonArray object_list) throws Exception {
        
    	for (int i = 0; i < object_list.size(); i++) {
            JsonObject object_info = object_list.getJsonObject(i);
            Class object_class = Class.forName(object_info.getString("class"));
            Constructor constructor = object_class.getDeclaredConstructor();
            if (!Modifier.isPublic(constructor.getModifiers())) {
                constructor.setAccessible(true);
            }
            Object object_instance = constructor.newInstance();
            //Make object
            object_tracking_map.put(object_info.getString("id"), object_instance);
        }
    }

    private static void assignFieldValues(Map object_tracking_map, JsonArray object_list) throws Exception {
    	
    	for (int i = 0; i < object_list.size(); i++) {
    		JsonObject object_info = object_list.getJsonObject(i);
    		
    		// get object instance 
    		Object object_instance = object_tracking_map.get(object_info.getString("id"));
    		
    		// get fields 
    		JsonArray object_fields = object_info.getJsonArray("fields");
    		
    		for (int j = 0; j < object_fields.size(); j++) {
    			JsonObject field_info = object_fields.getJsonObject(j);
    			
    			// get declaring class 
    			String field_deClass = field_info.getString("declaringClass");
    			Class fieldClass = Class.forName(field_deClass);
    			
    			String field_name = field_info.getString("name");
    			Field f = fieldClass.getDeclaredField(field_name);
    			
    			f.setAccessible(true);
    			
    			// primitive 
    			if (f.getType().isPrimitive()) {
    				if (f.getType().equals(int.class))
    					f.set(object_instance, Integer.valueOf(field_info.getString("value")));
    				else if (f.getType().equals(double.class))
    					f.set(object_instance, Double.valueOf(field_info.getString("value")));
    			}
    			// object 
    			else {
    				f.set(object_instance, object_tracking_map.get(field_info.getString("reference")));
    			}
    		}
    	}
    }

}