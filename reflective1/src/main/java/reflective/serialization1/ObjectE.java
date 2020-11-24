package reflective.serialization1;

import java.util.ArrayList;

public class ObjectE {
private ArrayList<Object> b = new ArrayList<>(3);
	
	ObjectE() {
		for(int i =0; i<3; i++) {
			b.add(null);
		}
	}
	
	ObjectE(Object firstArg, Object secondArg, Object thirdArg){
		b.add(firstArg);
		b.add(secondArg);
		b.add(thirdArg);
	}
	
	public void setArrayListIndex(int index, Object value) {
		b.add(index, value); 
	}
	
	public String toString() {
		return "ObjectE";
	}

}
