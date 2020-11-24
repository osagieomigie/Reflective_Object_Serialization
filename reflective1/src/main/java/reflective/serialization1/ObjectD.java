package reflective.serialization1;

public class ObjectD {
private Object[] b = new Object[5];
	
	ObjectD() {
		for(int i =0; i<b.length; i++) {
			b[i] = null;
		}
	}
	
	ObjectD(Object firstArg, Object secondArg, Object thirdArg, Object fourthArg, Object lastArg){
		b[0] = firstArg;
		b[1] = secondArg;
		b[2] = thirdArg;
		b[3] = fourthArg;
		b[4] = lastArg;
	}
	
	public void setArrayIndex(int index, Object value) {
		b[index] = value;
	}
	
	public String toString() {
		return "ObjectD";
	}

}
