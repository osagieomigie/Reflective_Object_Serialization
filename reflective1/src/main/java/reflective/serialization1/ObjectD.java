package reflective.serialization1;

public class ObjectD {
private ObjectA[] b = new ObjectA[5];
	
	ObjectD() {
		for(int i =0; i<b.length; i++) {
			b[i] = null;
		}
	}
	
	ObjectD(ObjectA firstArg, ObjectA secondArg, ObjectA thirdArg, ObjectA fourthArg, ObjectA lastArg){
		b[0] = firstArg;
		b[1] = secondArg;
		b[2] = thirdArg;
		b[3] = fourthArg;
		b[4] = lastArg;
	}
	
	public void setArrayIndex(int index, ObjectA value) {
		b[index] = value;
	}
	
	public String toString() {
		return "ObjectD";
	}

}
