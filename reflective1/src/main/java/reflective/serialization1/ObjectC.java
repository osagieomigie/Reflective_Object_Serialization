package reflective.serialization1;

public class ObjectC {
	private int[] a = new int[5];
	
	ObjectC() {
		for(int i =0; i<a.length; i++) {
			a[i] = 0;
		}
	}
	
	ObjectC(int b, int c, int d, int e, int f){
		a[0] = b;
		a[1] = c;
		a[2] = d;
		a[3] = e;
		a[4] = f;
	}
	
	public void setArrayIndex(int index, int value) {
		a[index] = value;
	}
	
	public String toString() {
		return "ObjectC";
	}
}
