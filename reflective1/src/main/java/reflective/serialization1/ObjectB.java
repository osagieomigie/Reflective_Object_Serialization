package reflective.serialization1;

public class ObjectB {
	
	private ObjectB obj;
	private int x;
	
	ObjectB(){
		obj = null;
		x = 1;
	}
	
	ObjectB(int x){
		obj = null;
		this.x = x;
	}
	
	void setX(int x) {
		this.x = x;
	}
	
	void setObj(ObjectB obj) {
		this.obj = obj;
	}
	
	public String toString() {
        return "ObjectB";
    }
	
}
