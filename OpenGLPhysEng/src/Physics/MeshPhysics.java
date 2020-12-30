package Physics;

import Math.Mat3d;
import Math.Quaternion;
import Math.Vect3d;
import Objects.Mesh;
import Objects.Triangle;

public class MeshPhysics extends Physics{
	
	public Mesh mesh;
	public Axis rotation;
	public Mat3d basis;

	public MeshPhysics(Mesh mesh, double mass, Vect3d pos, Quaternion orientation, Mat3d basis, Mat3d scale) {
		super(mass, pos);
		this.mesh = mesh;
		this.basis = basis;
		
		Mat3d I = new Mat3d();
		
		Mat3d.scale(I, mesh.calcSpecificMomentOfInertia(scale), mass);
		
//		Mat3d _scale = new Mat3d();
//		scale.inverse(_scale);
//		Mat3d.mult(I, I, _scale);
//		Mat3d.mult(I, scale, I);
		
		rotation = new Axis(pos, orientation, basis, I);
		attributes.add(rotation);
	}

	double energyGain = 0;
	double oldEnergy = 0;
	double energyRate = 0;
	//Since you did quick and dirty springs. Try a quick and dirty floor.
	protected void computeForces() {
		
		Vect3d L = rotation.getApproxAngularMomentum(new Vect3d());
		Vect3d w = rotation.getAngVel(new Vect3d());
		Vect3d dw = Vect3d.cross(new Vect3d(), L, w);
		Mat3d.mult(dw, rotation.getInvMomentOfInertia(new Mat3d()), dw);
		double dot1 = Vect3d.dot(L,dw);
		double dot2 = rotation.getApproxE();
		
		energyGain += dot1/600d;
		
		energyRate = (dot2 - oldEnergy)*600;
		oldEnergy = dot2;
		
		System.out.println("actual energy: " + oldEnergy);
		System.out.println("target energy: " + rotation.getRotationalEnergy());		
		
		System.out.println();
		
		System.out.println(" Lx : " + L.x);
		System.out.println(" Ly : " + L.y);
		System.out.println(" Lz : " + L.z);
		System.out.println();
		
		System.out.println("rLx : " + rotation.angMomentum.x);
		System.out.println("rLy : " + rotation.angMomentum.y);
		System.out.println("rLz : " + rotation.angMomentum.z);
		System.out.println();
		
//		Vect3d diff = Vect3d.sub(new Vect3d(), rotation.w, rotation.k);
//		System.out.println("dx : " + diff.x);
//		System.out.println("dy : " + diff.y);
//		System.out.println("dz : " + diff.z);
//		System.out.println();
		
		//print to check if basis vectors are still orthonormal.
		//addDrag();
	}
	
	//Integrate over curved surface with interpolated normals.
	private void addDrag() {
		Vect3d rotVel = new Vect3d();
		Vect3d totVel = new Vect3d();
		Vect3d force = new Vect3d();
		Vect3d totForce = new Vect3d();
		Vect3d midPoint = new Vect3d();
		Vect3d normal = new Vect3d();
		Vect3d Area = new Vect3d();
		
		for(int i = 0; i < mesh.getTriangleCount(); i++) {
			Triangle t = mesh.getTriangle(i);
			
			t.midPoint.copyTo(midPoint);
			Mat3d.mult(midPoint, basis, midPoint);
			Vect3d.add(midPoint, pos, midPoint);
			
			rotVel = rotation.getVel(rotVel, midPoint);
			Vect3d.add(totVel, rotVel, vel);
			double speed = Math.sqrt(totVel.lengthSq());
			
			t.getArea().copyTo(Area);
			Mat3d.mult(Area, basis, Area);
			
			double dot = Vect3d.dot(Area, totVel);
			if(dot <= 0)
				continue;
			
			t.getNormal().copyTo(normal);
			Mat3d.mult(normal, basis, normal);
			Vect3d.scale(force, normal, -0.01*dot*speed);
			
			rotation.addTorque(force, midPoint);
			Vect3d.add(totForce, totForce, force);
		}
		
		totForce.copyTo(this.force);
	}
	
//	Vect3d L = rotation.getApproxAngularMomentum(new Vect3d());
//	Vect3d w = rotation.getAngVel(new Vect3d());
//	Vect3d dw = Vect3d.cross(new Vect3d(), L, w);
//	Mat3d.mult(dw, rotation.getInvMomentOfInertia(new Mat3d()), dw);
//	double dot1 = Vect3d.dot(L,dw);
//	double dot2 = rotation.getApproxE();
//	
//	energyGain += dot1/600d;
//	
//	energyRate = (dot2 - oldEnergy)*600;
//	oldEnergy = dot2;
//	
//	System.out.println("actual energy: " + oldEnergy);
//	System.out.println("target energy: " + rotation.getRotationalEnergy());		
//	
//	System.out.println();
//	
//	System.out.println(" Lx : " + L.x);
//	System.out.println(" Ly : " + L.y);
//	System.out.println(" Lz : " + L.z);
//	System.out.println();
//	
//	System.out.println("rLx : " + rotation.angMomentum.x);
//	System.out.println("rLy : " + rotation.angMomentum.y);
//	System.out.println("rLz : " + rotation.angMomentum.z);
//	System.out.println();

}