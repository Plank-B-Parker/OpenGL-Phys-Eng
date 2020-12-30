package Math;

import java.nio.FloatBuffer;

public class Mat3d {
	
	public Vect3d c1, c2, c3;
	
	private final static Mat3d tempM1 = new Mat3d();
	private final static Mat3d tempM2 = new Mat3d();
	private final static Mat3d tempM3 = new Mat3d();
	public final static Mat3d I = new Mat3d();
	
	private final double tolerance = 0.00;
	
	public Mat3d() {
		c1 = new Vect3d(1,0,0);
		c2 = new Vect3d(0,1,0);
		c3 = new Vect3d(0,0,1);
	}
	public Mat3d(Vect3d c1, Vect3d c2, Vect3d c3) {
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
	}
	
	public void copyTo(Mat3d copyTo) {
		c1.copyTo(copyTo.c1);
		c2.copyTo(copyTo.c2);
		c3.copyTo(copyTo.c3);
	}

	public static void add(Mat3d result ,Mat3d A, Mat3d B) {
		Vect3d.add(result.c1, A.c1, B.c1);
		Vect3d.add(result.c2, A.c2, B.c2);
		Vect3d.add(result.c3, A.c3, B.c3);
	}
	
	public static void sub(Mat3d result ,Mat3d A, Mat3d B) {
		Vect3d.sub(result.c1, A.c1, B.c1);
		Vect3d.sub(result.c2, A.c2, B.c2);
		Vect3d.sub(result.c3, A.c3, B.c3);
	}
	
	public static void scale(Mat3d result, Mat3d M, double k) {
		Vect3d.scale(result.c1, M.c1, k);
		Vect3d.scale(result.c2, M.c2, k);
		Vect3d.scale(result.c3, M.c3, k);
	}

	public static Vect3d mult(Vect3d result, Mat3d M, Vect3d v) {
		double x = v.x;
		double y = v.y;
		double z = v.z;
		Vect3d.scale(result, M.c1, x);
		Vect3d.increment(result, M.c2, y);
		Vect3d.increment(result, M.c3, z);
		return result;
	}
	
	public static void mult(Mat3d result, Mat3d A, Mat3d B) {
		Mat3d temp = tempM1;
		
		mult(temp.c1, A, B.c1);
		mult(temp.c2, A, B.c2);
		mult(temp.c3, A, B.c3);
		
		temp.copyTo(result);
	}
	
	public double det() {
		return c1.x*(c2.y*c3.z - c2.z*c3.y) + c1.y*(c2.z*c3.x - c2.x*c3.z) + c1.z*(c2.x*c3.y - c2.y*c3.x);
		//Vect3d.cross(temp, c2, c3);
		//return Vect3d.dot(c1, temp);
	}
	
	public void transpose(Mat3d result) {
		if(result == this) 
			transpose();
		else {
			result.c1.x = c1.x;
			result.c1.y = c2.x;
			result.c1.z = c3.x;

			result.c2.x = c1.y;
			result.c2.y = c2.y;
			result.c2.z = c3.y;

			result.c3.x = c1.z;
			result.c3.y = c2.z;
			result.c3.z = c3.z;
		}
	}
	
	public void transpose() {
		double temp = c1.y;
		c1.y = c2.x;
		c2.x = temp;
		
		temp = c1.z;
		c1.z = c3.x;
		c3.x = temp;
		
		temp = c2.z;
		c2.z = c3.y;
		c3.y = temp;
	}
	
	public void inverse(Mat3d mat) {
		Mat3d temp = tempM1;
		mat.copyTo(temp);
		
		if(isOrthogonal()) {
			transpose(temp);
			Vect3d.scale(temp.c1, temp.c1, 1/temp.c1.lengthSq());
			Vect3d.scale(temp.c2, temp.c2, 1/temp.c2.lengthSq());
			Vect3d.scale(temp.c3, temp.c3, 1/temp.c3.lengthSq());
			temp.copyTo(mat);
			return;
		}
		double det = det();
		Vect3d.cross(temp.c1, c2, c3);
		Vect3d.cross(temp.c2, c3, c1);
		Vect3d.cross(temp.c3, c1, c2);
		
		temp.transpose();
		
		scale(mat, temp, 1/det);
	}
	
	public static void crossTransform(Mat3d result, Vect3d v, Mat3d M) {
		Vect3d.cross(result.c1, v, M.c1);
		Vect3d.cross(result.c2, v, M.c2);
		Vect3d.cross(result.c3, v, M.c3);
	}
	
	public void store(FloatBuffer buffer) {
		buffer.put((float) c1.x);
		buffer.put((float) c1.y);
		buffer.put((float) c1.z);
		
		buffer.put((float) c2.x);
		buffer.put((float) c2.y);
		buffer.put((float) c2.z);
		
		buffer.put((float) c3.x);
		buffer.put((float) c3.y);
		buffer.put((float) c3.z);
	}
	
	public void rotate(Vect3d norm, double ang) {
		double sin = Math.sin(ang);
		double cos = Math.cos(ang);
		
		Vect3d.rotate(c1, c1, Vect3d.O, norm, cos, sin);
		Vect3d.rotate(c2, c2, Vect3d.O, norm, cos, sin);
		Vect3d.rotate(c3, c3, Vect3d.O, norm, cos, sin);
	}
	
	public void rotate(Quaternion q) {
		Vect3d.rotate(c1, c1, q);
		Vect3d.rotate(c2, c2, q);
		Vect3d.rotate(c3, c3, q);
	}
	
	public static void rotate(Mat3d result, Quaternion q, Mat3d M, Quaternion _q) {
		Vect3d.rotate(result.c1, q, M.c1, _q);
		Vect3d.rotate(result.c2, q, M.c2, _q);
		Vect3d.rotate(result.c3, q, M.c3, _q);
	}
	
	public void rotate(Vect3d norm, double cos, double sin) {
		Vect3d.rotate(c1, c1, Vect3d.O, norm, cos, sin);
		Vect3d.rotate(c2, c2, Vect3d.O, norm, cos, sin);
		Vect3d.rotate(c3, c3, Vect3d.O, norm, cos, sin);
	}
	
	public boolean isOrthogonal() {
		if(Math.abs(Vect3d.dot(c1,c2)) <= tolerance && Math.abs(Vect3d.dot(c1,c3)) <= tolerance && Math.abs(Vect3d.dot(c2,c3)) <= tolerance)
			return true;
		return false;
	}
	
}