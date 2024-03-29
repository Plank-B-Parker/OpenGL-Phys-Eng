package Objects;

import Math.Mat3d;
import Math.Quaternion;
import Math.Vect3d;
import Math.math;

public abstract class Object {
	
	public Vect3d pos;
	public Mat3d basis;
	protected Quaternion orientation = new Quaternion();
	///
	public Mat3d scale;
	///
	protected double xl = 1, yl = 1, zl = 1;
	protected Mesh mesh;
	private static Material whiteTexture = new Material();
	public Material material = whiteTexture;
	
	private final static Vect3d tempV1 = new Vect3d();
	private final static Vect3d tempV2 = new Vect3d();
	private final static Vect3d tempV3 = new Vect3d();
	private final static Vect3d tempV4 = new Vect3d();
	
	public Object() {
		pos = new Vect3d();
		basis = new Mat3d();
		scale = new Mat3d();
	}
	
	public Object(Vect3d pos, Mat3d basis, Mat3d scale) {
		this.pos = pos;
		this.basis = basis;
		this.scale = scale;
		
		xl = scale.c1.lengthSq();
		yl = scale.c2.lengthSq();
		zl = scale.c3.lengthSq();
	}

	public void translate(Vect3d d) {
		Vect3d.add(pos, pos, d);
	}
	
	protected void rotate(Vect3d center, Vect3d normal, double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		Vect3d.rotate(pos, pos, center, normal, cos, sin);
		basis.rotate(normal, cos, sin);
	}
	
	//Add quaternion method for rotating.
	
	public void rotate2(Vect3d center, Vect3d normal, double angle) {
		Vect3d.rotate(pos, pos, center, normal, angle);
		
	}
	
	public void lookAt(Vect3d thingYouWannaLookAt) {
		Vect3d direction = tempV1;
		Vect3d.sub(direction, thingYouWannaLookAt, pos);
		direction.normalize();
		
		//Gets information for rotation.
		double cos = Vect3d.dot(direction, basis.c3)/zl;
		Vect3d nsin = tempV2;
		Vect3d.cross(nsin, basis.c3, direction);
		Vect3d.scale(nsin, nsin, 1.0/zl);
		double sinSq = nsin.lengthSq();
		
		if(sinSq == 0)
			return;
		
		//Applies rotation formula.
		//Change basis.c1
		Vect3d x = basis.c1;
		double dot = Vect3d.dot(x, nsin);
		
		Vect3d xCos = tempV3;
		Vect3d.scale(xCos, x, cos);
		
		Vect3d xCosPlusNSinCrossX = tempV4;
		Vect3d.cross(xCosPlusNSinCrossX, nsin, x);
		Vect3d.increment(xCosPlusNSinCrossX, xCos, 1);
		
		Vect3d.increment(xCosPlusNSinCrossX, nsin, (dot)*(1-cos)/sinSq);
		basis.c1.set(xCosPlusNSinCrossX);
		
		//Change basis.c2
		x = basis.c2;
		dot = Vect3d.dot(x, nsin);
		
		xCos = tempV3;
		Vect3d.scale(xCos, x, cos);
		
		xCosPlusNSinCrossX = tempV4;
		Vect3d.cross(xCosPlusNSinCrossX, nsin, x);
		Vect3d.increment(xCosPlusNSinCrossX, xCos, 1);
		
		Vect3d.increment(xCosPlusNSinCrossX, nsin, (dot)*(1-cos)/sinSq);
		basis.c2.set(xCosPlusNSinCrossX);
		
		basis.c3 = direction;
	}
	
	private double tolerance = 0.001;
	public void orthogonormaliseBasis() {
		if(Vect3d.dot(basis.c1, basis.c2) > tolerance || Vect3d.dot(basis.c1, basis.c3) > tolerance) {
			Vect3d.cross(basis.c1, basis.c2, basis.c3);
		}
		if(Vect3d.dot(basis.c3, basis.c1) > tolerance || Vect3d.dot(basis.c3, basis.c2) > tolerance) {
			Vect3d.cross(basis.c3, basis.c1, basis.c2);
		}
		
		
		if(Math.abs(basis.c1.lengthSq() - xl) > tolerance) {
			Vect3d.scale(basis.c1, basis.c1, math.invSqrt(basis.c1.lengthSq()/xl));
		}
		if(Math.abs(basis.c2.lengthSq() - yl) > tolerance) {
			Vect3d.scale(basis.c2, basis.c2, math.invSqrt(basis.c2.lengthSq()/yl));
		}
		if(Math.abs(basis.c3.lengthSq() - zl) > tolerance) {
			Vect3d.scale(basis.c3, basis.c3, math.invSqrt(basis.c3.lengthSq()/zl));
		}
	}

	public Mat3d getOrientation(Mat3d result) {
		basis.copyTo(result);
		result.rotate(orientation);
		return result;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
	public void setMaterial(Material mat) {
		material = mat;
	}
}