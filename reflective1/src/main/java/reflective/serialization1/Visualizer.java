package reflective.serialization1;
import java.lang.reflect.*;

/**
 * CPSC 501
 * Used for inspecting an object
 *
 * @author Jonathan Hudson, Osa Omigie 
 */

public class Visualizer {

    public void inspect(Object obj, boolean recursive) {
        Class<?> c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
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
    
    private void inspectClass(Class<?> c, Object obj, boolean recursive, int depth) {
    	// print declaring class
    	StringBuilder tabDepth = calculateTabDepth(depth);
    	
    	System.out.println(tabDepth + "CLASS");
    	System.out.println(tabDepth + String.format("Class: %s", c.getName()));
    	
    	if (c.isArray()) {
    		inspectArray(c, obj, recursive, depth);
    	}else {
	    	inspectFields(c, obj, recursive, depth);
    	}
    }
    
    public void printModifiers(int ref, StringBuilder tabDepth) {
    	if ( Modifier.toString(ref).isEmpty()) {
        	System.out.println(tabDepth +"  Modifiers: NONE");
        }else {
        	System.out.println(tabDepth + "  Modifiers: " + Modifier.toString(ref));
        }
    }
    
    private void inspectFields(Class<?> c, Object obj, boolean recursive, int depth) {
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
    			inspectField(f, obj, recursive, depth);
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
    
    private void inspectField(Field f, Object obj, boolean recursive, int depth) {
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
    		inspectFieldArray(f, obj, recursive, depth);
    	}else { 
    		// inspect object
    		Object value = getValue(f, obj);
    		if (value != null) {
	    		System.out.println(tabDepth + "  Value (ref): " + value.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(value)) );
				
				// inspect class if recursive true 
				if (recursive) {
					System.out.println(tabDepth + "    -> Recursively inspect");
					inspectClass(value.getClass(), value, recursive, depth+1);
				}
    		}else {
    			System.out.println(tabDepth + "  Value: null"); 
    		}
    	}
    	
    }
    
    private void inspectFieldArray(Field f, Object obj, boolean recursive, int depth) {
    	// calculate depth 
    	StringBuilder tabDepth = calculateTabDepth(depth);
    	
    	// array object
    	Object array = getValue(f, obj);
    	
    	// inspect array
    	inspectArray(f.getType(), array, recursive, depth);
    }
    
    private void inspectArray(Class<?> c, Object obj, boolean recursive, int depth) {
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
    					inspectClass(arrValue.getClass(), arrValue, recursive, depth+1);
    				}
    			}
    				
    		}else {
    			System.out.println(tabDepth + "   Value: null"); 
    		}
    		
    	}
    }
}
