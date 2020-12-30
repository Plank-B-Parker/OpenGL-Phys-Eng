package Math;

public class Vect3f {
	public float x = 0 , y = 0, z = 0;
	
	public Vect3f() {
		
	}

	public Vect3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vect3f(Vect3d v) {
		x = (float) v.x;
		y = (float) v.y;
		z = (float) v.z;
	}
	
	public void set(Vect3f v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static float dot(Vect3f A, Vect3f B) {
		return A.x*B.x + A.y*B.y + A.z*B.z;
	}
	
	public float lengthSq() {
		return x*x + y*y + z*z;
	}
	
	public void normalize() {
		float _length = math.invSqrt(lengthSq());
		x = x*_length;
		y = y*_length;
		z = z*_length;
	}
	
	public static float invSqrt(float x) {
	    float xhalf = 0.5f * x;
	    int i = Float.floatToIntBits(x);
	    i = 0x5f3759df - (i >> 1);
	    x = Float.intBitsToFloat(i);
	    x *= (1.5f - xhalf * x * x);
	    return x;
	}
	
	public static void add(Vect3f result, Vect3f A, Vect3f B) {
		result.x = A.x + B.x;
		result.y = A.y + B.y;
		result.z = A.z + B.z;
	}
	public static void sub(Vect3f result, Vect3f A, Vect3f B) {
		result.x = A.x - B.x;
		result.y = A.y - B.y;
		result.z = A.z - B.z;
	}
	public static void scale(Vect3f result, Vect3f A, float k) {
		result.x = A.x*k;
		result.y = A.y*k;
		result.z = A.z*k;
	}
	public static void increment(Vect3f result, Vect3f A, float k) {
		result.x += A.x*k;
		result.y += A.y*k;
		result.z += A.z*k;
	}
	public static void cross(Vect3f result, Vect3f A, Vect3f B) {
		float tempx = A.y*B.z - A.z*B.y;
		float tempy = A.z*B.x - A.x*B.z;
		
		result.z = A.x*B.y - A.y*B.x;
		result.x = tempx;
		result.y = tempy;
	}
}
