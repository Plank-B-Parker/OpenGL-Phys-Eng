package Math;

import java.nio.FloatBuffer;

public class Mat3f {
	
	public Vect3f c1, c2, c3;
	
	public Mat3f() {
		c1 = new Vect3f(1,0,0);
		c2 = new Vect3f(0,1,0);
		c3 = new Vect3f(0,0,1);
	}
	public Mat3f(Vect3f c1, Vect3f c2, Vect3f c3) {
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
	}

	public static Mat3f add(Mat3f result ,Mat3f A, Mat3f B) {
		Vect3f.add(result.c1, A.c1, B.c1);
		Vect3f.add(result.c2, A.c2, B.c2);
		Vect3f.add(result.c3, A.c3, B.c3);

		return result;
	}
	
	public static Mat3f sub(Mat3f result ,Mat3f A, Mat3f B) {
		Vect3f.sub(result.c1, A.c1, B.c1);
		Vect3f.sub(result.c2, A.c2, B.c2);
		Vect3f.sub(result.c3, A.c3, B.c3);

		return result;
	}
	
	public static Mat3f scale(Mat3f result, Mat3f M, float k) {
		Vect3f.scale(result.c1, M.c1, k);
		Vect3f.scale(result.c2, M.c2, k);
		Vect3f.scale(result.c3, M.c3, k);

		return result;
	}

	public static Vect3f mult(Vect3f result, Mat3f M, Vect3f v) {
		Vect3f.scale(result, M.c1, v.x);
		Vect3f.increment(result, M.c2, v.y);
		Vect3f.increment(result, M.c3, v.z);

		return result;
	}
	
	public static Mat3f mult(Mat3f result, Mat3f A, Mat3f B) {
		mult(result.c1, A, B.c1);
		mult(result.c2, A, B.c2);
		mult(result.c3, A, B.c3);

		return result;
	}
	
	public float det() {
		return c1.x*(c2.y*c3.z - c2.z*c3.y) + c1.y*(c2.z*c3.x - c2.x*c3.z) + c1.z*(c2.x*c3.y - c2.y*c3.x);
		//Vect3f.cross(temp, c2, c3);
		//return Vect3f.dot(c1, temp);
	}
	
	public void transpose() {
		float temp = c1.y;
		c1.y = c2.x;
		c2.x = temp;
		
		temp = c1.z;
		c1.z = c3.x;
		c3.x = temp;
		
		temp = c2.z;
		c2.z = c3.y;
		c3.y = temp;
	}
	
	public void store(FloatBuffer buffer) {
		buffer.put(c1.x);
		buffer.put(c1.y);
		buffer.put(c1.z);
		
		buffer.put(c2.x);
		buffer.put(c2.y);
		buffer.put(c2.z);
		
		buffer.put(c3.x);
		buffer.put(c3.y);
		buffer.put(c3.z);
	}
	
//	public Mat3f getInv() {
//		return null;
//	}

	public static Mat3f add(Mat3f A, Mat3f B){
		return add(new Mat3f(), A, B);
	}
	public static Mat3f sub(Mat3f A, Mat3f B){
		return sub(new Mat3f(), A, B);
	}
	public static Mat3f scale(Mat3f A, float scale){
		return scale(new Mat3f(), A, scale); 
	}
	public static Vect3f mult(Mat3f M, Vect3f v) {
		return mult(new Vect3f(), M, v);
	}
	public static Mat3f mult(Mat3f A, Mat3f B) {
		return mult(new Mat3f(), A, B);
	}
}
