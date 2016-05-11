package s9.itba;

public class Vector {

	public double x, y;
	
	public Vector(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public String toString(){
		return "[" + x + "," + y + "]"; 
	}
}
