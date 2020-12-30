package Math;

public class Vect2f {

	public float x = 0;
	public float y = 0;
	
	public Vect2f() {
	}
	
	public Vect2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void normalize() {
		float _length = math.invSqrt(lengthSq());
		x = x*_length;
		y = y*_length;
	}
	
	public float lengthSq() {
		return x*x + y*y;
	}
	
	public static float dot(Vect2f A, Vect2f B) {
		return A.x*B.x + A.y*B.y;
	}
	
	public static float cross(Vect2f A, Vect2f B) {
		return A.x*B.y - A.y*B.x;
	}

	public static void add(Vect2f result, Vect2f A, Vect2f B) {
		result.x = A.x + B.x;
		result.y = A.y + B.y;
	}
	
	public static void sub(Vect2f result, Vect2f A, Vect2f B) {
		result.x = A.x - B.x;
		result.y = A.y - B.y;
	}
	
	public static void scale(Vect2f result, Vect2f A, float k) {
		result.x = A.x*k;
		result.y = A.y*k;
	}
	
	public static void increment(Vect2f result, Vect2f A, float k) {
		result.x += A.x*k;
		result.y += A.y*k;
	}
}
