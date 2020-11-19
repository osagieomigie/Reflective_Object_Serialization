package reflective.serialization1;

public class ObjectA {
	private int x;
	private int y;
	private double z; 
	
	ObjectA(){
		x = 1;
		y = 3;
		z = 1.0; 
	}
	
	ObjectA(int x, int y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	void setX(int x) {
		this.x = x;
	}
	
	void setY(int y) {
		this.y = y;
	}
	
	void setZ(double z) {
		this.z = z;
	}
	
	public String toString() {
        return "ObjectA";
    }
}
