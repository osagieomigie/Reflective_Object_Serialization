package reflective.serialization1;

import java.util.ArrayList;

public class ObjectE {
private ArrayList<ObjectA> b = new ArrayList<>(3);
	
	ObjectE() {
		for(int i =0; i<3; i++) {
			b.add(null);
		}
	}
	
	ObjectE(ObjectA firstArg, ObjectA secondArg, ObjectA thirdArg){
		b.add(firstArg);
		b.add(secondArg);
		b.add(thirdArg);
	}
	
	public void setArrayListIndex(int index, ObjectA value) {
		b.add(index, value); 
	}
	
	public String toString() {
		return "ObjectE";
	}

}
