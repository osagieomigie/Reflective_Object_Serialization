package serialization_xml;

import java.lang.reflect. *;
import java.util. *;
import org.w3c.dom. *;
/**
 * CPSC 501 
 * XML deserializer
 *
 * @author Jonathan Hudson, Osa Omigie 
 */

public class Deserializer_XML {

	public static Object deserializeObject (Document document) throws Exception {
        NodeList object_node_list = document.getDocumentElement().getChildNodes();
        Map object_tracking_map = new HashMap ();
        createInstances(object_tracking_map, object_node_list);
        assignFieldValues(object_tracking_map, object_node_list);
        return object_tracking_map.get ("0");
    }

    private static void createInstances(Map object_tracking_map, NodeList object_list) throws Exception {
        
    	for (int i = 0; i < object_list.getLength(); i++) {
    		Node object_node = object_list.item(i);

            Object object_instance = null;
            if (object_node instanceof Element) {
                Element object_info = (Element) object_node;
                Class object_class = Class.forName(object_info.getAttribute("class"));
                
                // element is an array
                if (object_info.getAttribute("type").equals("array")) {
                	int [] arrayLength = {Integer.valueOf(object_info.getAttribute("length"))};
                	object_instance = Array.newInstance(object_class.getComponentType(), arrayLength);
                }else {
                	Constructor constructor = object_class.getDeclaredConstructor();
    	            if (! Modifier.isPublic(constructor.getModifiers())) {
    	                constructor.setAccessible(true);
    	            }
    	            object_instance = constructor.newInstance();
                }
                
                // Make object
                object_tracking_map.put(object_info.getAttribute("id"), object_instance);
            }
        }
    }

    private static void assignFieldValues(Map object_tracking_map, NodeList object_list) throws Exception {
    	
    	for (int i = 0; i < object_list.getLength(); i++) {
    		Node object_node = object_list.item(i);
    		
    		if (object_node instanceof Element) {
                Element object_info = (Element) object_node;
                Object object_instance = object_tracking_map.get(object_info.getAttribute("id"));
                
                
                if (object_info.getAttribute("type").equals("array")) {
                	// get entries 
                	for (int j = 0; j < object_node.getChildNodes().getLength(); j++) {
                		Node child_node = object_node.getChildNodes().item(j);
	        			Element array_entry = (Element) child_node;
	        			
	        			// Entry either a null value, or a primitive type
            			if (array_entry.getTagName().equals("value")) {
            				if (array_entry.getTextContent().equals("null")) {
            					Array.set(object_instance, j, null);
            				}else {
            					Array.setInt(object_instance, j, Integer.valueOf(array_entry.getTextContent()));
            				}
            			}else { // entry an object
            				Array.set(object_instance, j, object_tracking_map.get(array_entry.getTextContent()));
            			}
                	}
                }else {
	                // get fields 
	        		for (int j = 0; j < object_node.getChildNodes().getLength(); j++) {
	        			Node child_node = object_node.getChildNodes().item(j);
	        			Element field_info = (Element) child_node;
	        			
	        			// get declaring class 
	        			String field_deClass = field_info.getAttribute("declaringClass");
	        			Class fieldClass = Class.forName(field_deClass);
	        			
	        			String field_name = field_info.getAttribute("name");
	        			Field f = fieldClass.getDeclaredField(field_name);
	        			
	        			// get field value 
	        			Node value_node = field_info.getFirstChild();
	        			Element field_value = (Element) value_node;
	        			
	        			f.setAccessible(true);
	        			
	        			// primitive 
	        			if (f.getType().isPrimitive()) {
	        				if (f.getType().equals(int.class))
	        					f.set(object_instance, Integer.valueOf(field_value.getTextContent()));
	        				else if (f.getType().equals(double.class))
	        					f.set(object_instance, Double.valueOf(field_value.getTextContent()));
	        			}
	        			// object 
	        			else {
	        				f.set(object_instance, object_tracking_map.get(field_value.getTextContent()));
	        			}
	        		}
                }
    		}
    	}
    }

}