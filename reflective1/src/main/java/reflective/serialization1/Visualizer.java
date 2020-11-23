package reflective.serialization1;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * CPSC 501
 * Used for inspecting an object
 *
 * @author Jonathan Hudson, Osa Omigie 
 */

public class Visualizer {

    public void inspect(Object obj, boolean recursive) {
        Class<?> c = obj.getClass();
        Map <Integer, String> searchedObjects = new HashMap<Integer, String>(); // track previously explored objects 
        inspectClass(c, obj, recursive, 0, searchedObjects);
    }
    
    public StringBuilder calculateTabDepth(int depth) {
    	StringBuilder tmpTab = new StringBuilder("");
    	if (depth > 0) { 
	    	for(int i = 0; i < depth; i++) {
	    		tmpTab.append("\t");
	    	}
    	}
    	
    	return tmpTab;
    }
    
    private void inspectClass(Class<?> c, Object obj, boolean recursive, int depth, Map searchedObjects) {
    	// print declaring class
    	StringBuilder tabDepth = calculateTabDepth(depth);
    	
    	System.out.println(tabDepth + "CLASS");
    	System.out.println(tabDepth + String.format("Class: %s", c.getName()));
    	
    	// only inspect objects that have not yet been explored 
    	if (!searchedObjects.containsKey(obj.hashCode()) || searchedObjects.get(obj.hashCode()) != c.getName()) {
    		searchedObjects.put(obj.hashCode(), c.getName());
	    	if (c.isArray()) {
	    		inspectArray(c, obj, recursive, depth, searchedObjects);
	    	}else {
	    	
		    	Class<?> superClass = c.getSuperclass();
		  
		    	// print super class 
		    	if(superClass != null) {
//		    		if (!searchedObjects.containsKey(obj.hashCode())) {
		    			System.out.println(tabDepth + "SUPERCLASS -> Recursively Inspect");
		    			System.out.println(tabDepth + String.format("SuperClass: %s", superClass.getName()));
		    			inspectClass(superClass, obj, recursive, depth+1, searchedObjects);
//		    		}
		    	}else {
		    		System.out.println(tabDepth + "SuperClass: NONE");
		    	}
		    	inspectFields(c, obj, recursive, depth, searchedObjects);
	    	}
    	}
    }
    
    public void printModifiers(int ref, StringBuilder tabDepth) {
    	if ( Modifier.toString(ref).isEmpty()) {
        	System.out.println(tabDepth +"  Modifiers: NONE");
        }else {
        	System.out.println(tabDepth + "  Modifiers: " + Modifier.toString(ref));
        }
    }
    
    private void inspectFields(Class<?> c, Object obj, boolean recursive, int depth, Map searchedClasses) {
    	// calculate depth 
    	StringBuilder tabDepth = calculateTabDepth(depth);
    	
    	System.out.println(tabDepth + String.format("FIELDS( %s )", c.getName()));
    	
    	Field [] field = c.getDeclaredFields();
    	
    	if(field.length > 0) {
    		System.out.println(tabDepth + "Fields-> ");
    		
    		// inspect each field 
    		for (Field f : field) {
    			System.out.println(tabDepth + " FIELD");
    			f.setAccessible(true);
    			inspectField(f, obj, recursive, depth, searchedClasses);
    		}
    	}else
    		System.out.println(tabDepth + "Fields-> NONE");
    }
    
    private Object getValue(Field f, Object obj) {
    	try {
    		return f.get(obj);
    	}catch (IllegalArgumentException | IllegalAccessException e){
    		System.err.println("Can't access field " + obj);
    		
    	}
    	
    	return null;
    }
    
    private void inspectField(Field f, Object obj, boolean recursive, int depth, Map searchedObjects) {
    	// calculate depth 
    	StringBuilder tabDepth = calculateTabDepth(depth);
    	
    	// print field name
    	System.out.println(tabDepth + "  Name: " + f.getName());
    	
    	// print field type
    	Class<?> type = f.getType();
    	System.out.println(tabDepth + "  Type: " + type);
    	
    	// print field modifiers 
    	printModifiers(f.getModifiers(), tabDepth);
    	
    	// determine fields type, and print current value(s)
    	if(type.isPrimitive()) { 
    		// print fields value 
    		Object value = getValue(f, obj);
    		if(value != null) {
    			System.out.println(tabDepth + "  Value: " + value); 
    		}else {
    			System.out.println(tabDepth + "  Value: null"); 
    		}
    	}else if (type.isArray()) {
    		// inspect array
    		inspectFieldArray(f, obj, recursive, depth, searchedObjects);
    	}else { 
    		// inspect object
    		Object value = getValue(f, obj);
    		if (value != null) {
	    		System.out.println(tabDepth + "  Value (ref): " + value.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(value)) );
				
				// inspect class if recursive true 
				if (recursive && !searchedObjects.containsKey(value.hashCode())) {
					System.out.println(tabDepth + "    -> Recursively inspect");
					inspectClass(value.getClass(), value, recursive, depth+1, searchedObjects);
				}else {
					System.out.println(tabDepth + "     -> Already recursively inspected");
				}
    		}else {
    			System.out.println(tabDepth + "  Value: null"); 
    		}
    	}
    	
    }
    
    private void inspectFieldArray(Field f, Object obj, boolean recursive, int depth, Map searchedObjects) {
    	// calculate depth 
    	StringBuilder tabDepth = calculateTabDepth(depth);
    	
    	// array object
    	Object array = getValue(f, obj);
    	
    	// inspect array
    	inspectArray(f.getType(), array, recursive, depth, searchedObjects);
    }
    
    private void inspectArray(Class<?> c, Object obj, boolean recursive, int depth, Map searchedObjects) {
    	// calculate depth 
    	StringBuilder tabDepth = calculateTabDepth(depth);
    	
    	// print component type
    	System.out.println(tabDepth + "  Component Type: " + c.getComponentType());
    	
    	// print array length
    	int arrLength = Array.getLength(obj);
    	System.out.println(tabDepth + "  Length: " + arrLength);
    	
    	// inspect array elements
    	System.out.println(tabDepth + "  Entries-> ");
    	for (int i = 0; i < arrLength; i++) {
    		Object arrValue = Array.get(obj, i);
    		if(arrValue != null) {
    			// primitive object
    			if (c.getComponentType().isPrimitive())
    				System.out.println(tabDepth + "   Value: " + arrValue);
    			// class object
    			else {
    				System.out.println(tabDepth + "   Value (ref): " + arrValue.getClass().getName() + "@" + Integer.toHexString(arrValue.hashCode()) );
    				
    				// inspect class if recursive true 
    				if (recursive) {
    					System.out.println(tabDepth + "    -> Recursively inspect");
    					inspectClass(arrValue.getClass(), arrValue, recursive, depth+1, searchedObjects);
    				}
    			}
    				
    		}else {
    			System.out.println(tabDepth + "   Value: null"); 
    		}
    		
    	}
    }
}
