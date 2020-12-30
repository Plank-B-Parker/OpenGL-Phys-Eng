package Math;

public class Quaternion {
	public double w = 1, i = 0, j = 0, k = 0;
	
	public Quaternion() {
		
	}
	
	public Quaternion(double w, double i, double j, double k) {
		this.w = w;
		this.i = i;
		this.j = j;
		this.k = k;
	}
	
	public Quaternion(Vect3d unitVector) {
		w = 0;
		i = unitVector.x;
		j = unitVector.y;
		k = unitVector.z;
	}
	
	public void set(double w, double i, double j, double k) {
		this.w = w;
		this.i = i;
		this.j = j;
		this.k = k;
	}
	
	public void set(Quaternion q) {
		this.w = q.w;
		this.i = q.i;
		this.j = q.j;
		this.k = q.k;
	}
	
	public void set(Vect3d Vector) {
		w = 0;
		i = Vector.x;
		j = Vector.y;
		k = Vector.z;
	}
	
	/**
	 * Returns the vector made by i,j and k.
	 * @param result
	 * @return
	 */
	public Vect3d getVector(Vect3d result) {
		result.x = i;
		result.y = j;
		result.z = k;
		return result;
	}
	
	/**
	 * Gives a quaternion that can be used to rotate another quaternion by angle around the axis given by unitDirection.
	 * unitDirection should be normalised.
	 * @param result
	 * @param angle
	 * @param unitDirection
	 * @return
	 */
	public static Quaternion getRotation(Quaternion result, double angle, Vect3d unitDirection) {
		angle = angle/2;
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		
		result.w = cos;
		result.i = unitDirection.x*sin;
		result.j = unitDirection.y*sin;
		result.k = unitDirection.z*sin;
		
		return result;
	}
	
	/**
	 * Returns a quaternion where imaginary parts are negative.
	 * @param result
	 * @return
	 */
	public Quaternion getCongecate(Quaternion result) {
		scale(result, this, -1);
		result.w *= -1;
		return result;
	}
	
	/**
	 * Returns the magnitude squared of the quaternion.
	 * @return
	 */
	public double lengthSq() {
		return w*w + i*i + j*j + k*k;
	}
	
	/**
	 * Normalises quaternion to have unit length.
	 */
	public void normalise() {
		scale(this, this, math.invSqrt(lengthSq()));
	}
	
	/**
	 * Adds q1 to q2.
	 * @param result
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static Quaternion add(Quaternion result, Quaternion q1, Quaternion q2) {
		result.w = q1.w + q2.w;
		result.i = q1.i + q2.i;
		result.j = q1.j + q2.j;
		result.k = q1.k + q2.k;
		return result;
	}
	/**
	 * Subtracts q2 from q1.
	 * @param result
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static Quaternion sub(Quaternion result, Quaternion q1, Quaternion q2) {
		result.w = q1.w - q2.w;
		result.i = q1.i - q2.i;
		result.j = q1.j - q2.j;
		result.k = q1.k - q2.k;
		return result;
	}
	
	/**
	 * Scales quaternion by k. 
	 * @param result
	 * @param q1
	 * @param k
	 * @return
	 */
	public static Quaternion scale(Quaternion result, Quaternion q1, double k) {
		result.w = q1.w * k;
		result.i = q1.i * k;
		result.j = q1.j * k;
		result.k = q1.k * k;
		return result;
	}
	
	/**
	 * Multiplies q1 to q2.
	 * @param result
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static Quaternion mult(Quaternion result, Quaternion q1, Quaternion q2) {
		//(w i j k)(w i j k)
		double w = q1.w*q2.w - q1.i*q2.i - q1.j*q2.j - q1.k*q2.k;
		double i = (q1.w*q2.i + q2.w*q1.i) + (q1.j*q2.k - q1.k*q2.j);
		double j = (q1.w*q2.j + q2.w*q1.j) + (q1.k*q2.i - q1.i*q2.k);
		double k = (q1.w*q2.k + q2.w*q1.k) + (q1.i*q2.j - q1.j*q2.i);
		
		result.set(w, i, j, k);
		
		return result;
	}
}
