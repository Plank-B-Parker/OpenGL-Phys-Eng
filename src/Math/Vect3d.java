package Math;

public class Vect3d {
	public double x = 0 , y = 0, z = 0;
	public static final Vect3d O = new Vect3d();
	public static final Vect3d i = new Vect3d(1,0,0);
	public static final Vect3d j = new Vect3d(0,1,0);
	public static final Vect3d k = new Vect3d(0,0,1);
	
	public Vect3d() {
		
	}
	
	public Vect3d(Vect3f v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}

	public Vect3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vect3d copy() {
		return new Vect3d(x,y,z);
	}
	
	public void copy(Vect3d v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public void copyTo(Vect3d copyTo) {
		copyTo.x = x;
		copyTo.y = y;
		copyTo.z = z;
	}
	
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static double dot(Vect3d A, Vect3d B) {
		return A.x*B.x + A.y*B.y + A.z*B.z;
	}
	
	public double lengthSq() {
		return x*x + y*y + z*z;
	}
	
	public void normalize() {
		double length = Math.sqrt(lengthSq());
		x = x/length;
		y = y/length;
		z = z/length;
	}
	
	public static Vect3d add(Vect3d result, Vect3d A, Vect3d B) {
		result.x = A.x + B.x;
		result.y = A.y + B.y;
		result.z = A.z + B.z;
		return result;
	}
	public static Vect3d sub(Vect3d result, Vect3d A, Vect3d B) {
		result.x = A.x - B.x;
		result.y = A.y - B.y;
		result.z = A.z - B.z;
		return result;
	}
	public static Vect3d scale(Vect3d result, Vect3d A, double k) {
		result.x = A.x*k;
		result.y = A.y*k;
		result.z = A.z*k;
		return result;
	}
	public static Vect3d increment(Vect3d result, Vect3d A, double k) {
		result.x += A.x*k;
		result.y += A.y*k;
		result.z += A.z*k;
		return result;
	}
	public static Vect3d cross(Vect3d result, Vect3d A, Vect3d B) {
		double tempx = A.y*B.z - A.z*B.y;
		double tempy = A.z*B.x - A.x*B.z;
		
		result.z = A.x*B.y - A.y*B.x;
		result.x = tempx;
		result.y = tempy;
		return result;
	}
	
	public static Vect3d crossIncrement(Vect3d result, Vect3d A, Vect3d B, double k) {
		double tempx = A.y*B.z - A.z*B.y;
		double tempy = A.z*B.x - A.x*B.z;
		
		result.z += k*(A.x*B.y - A.y*B.x);
		result.x += k*tempx;
		result.y += k*tempy;
		return result;
	}
	
	public static Vect3d rotate(Vect3d result, Vect3d v, Vect3d c, Vect3d n, double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		
		if(v == c) {
			result = v;
			return result;
		}
		
		Vect3d.sub(result, v, c);
		double dot = Vect3d.dot(result, n);
		Vect3d.scale(result, result, cos);
		Vect3d.crossIncrement(result, n, result, sin/cos);
		Vect3d.increment(result, n, (dot)*(1-cos));
		
		Vect3d.add(result, result, c);
		return result;
	}

	public static Vect3d rotate(Vect3d result, Vect3d v, Vect3d c, Vect3d n, double cos, double sin) {
		if(v == c) {
			result = v;
			return result;
		}
		
		Vect3d.sub(result, v, c);
		double dot = Vect3d.dot(result, n);
		Vect3d.scale(result, result, cos);
		Vect3d.crossIncrement(result, n, result, sin/cos);
		Vect3d.increment(result, n, (dot)*(1-cos));
		
		Vect3d.add(result, result, c);
		return result;
	}
	
	private static final Quaternion tempQ1 = new Quaternion();
	private static final Quaternion tempQ2 = new Quaternion();
	public static Vect3d rotate(Vect3d result, Vect3d v, Quaternion q) {
		Quaternion vAsQuat = tempQ1;
		
		vAsQuat.set(v);
		Quaternion _q = q.getCongecate(tempQ2);
		
		//q * v * _q
		Quaternion.mult(vAsQuat, vAsQuat, _q);
		Quaternion.mult(vAsQuat, q, vAsQuat);
		
		return vAsQuat.getVector(result);
	}

	public static Vect3d rotate(Vect3d result, Quaternion q, Vect3d v, Quaternion _q) {
		Quaternion vAsQuat = tempQ1;
		
		vAsQuat.set(v);
		
		//q * v * _q
		Quaternion.mult(vAsQuat, vAsQuat, _q);
		Quaternion.mult(vAsQuat, q, vAsQuat);
		
		return vAsQuat.getVector(result);
	}

	public static Vect3d add(Vect3d A, Vect3d B){
		return add(new Vect3d(), A, B);
	}
	public static Vect3d sub(Vect3d A, Vect3d B){
		return sub(new Vect3d(), A, B);
	}
	public static Vect3d scale(Vect3d A, double k){
		return scale(new Vect3d(), A, k);
	}
	public static Vect3d cross(Vect3d A, Vect3d B) {
		return cross(new Vect3d(), A, B);
	}
	public static Vect3d rotate(Vect3d v, Vect3d c, Vect3d n, double angle){
		return rotate(new Vect3d(), v, c, n, angle);
	}
	public static Vect3d rotate(Vect3d v, Vect3d c, Vect3d n, double cos, double sin){
		return rotate(new Vect3d(), v, c, n, cos, sin);
	}
	public static Vect3d rotate(Vect3d v, Quaternion q){
		return rotate(new Vect3d(), v, q);
	}
	public static Vect3d rotate(Quaternion q, Vect3d v, Quaternion _q){
		return rotate(q,v,_q);
	}
}
