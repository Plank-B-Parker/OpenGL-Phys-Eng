package Main;



import Graphics.lightSource;
import Math.Mat3d;
import Math.Vect3d;
import Math.Vect3f;
import Objects.Box;
import Objects.Cylinder;
import Objects.Sphere;

public class UserMain extends Main{

	public UserMain() {
		
	}

	Box box;
	Cylinder omega;
	Cylinder momentum;
	@Override
	protected void initialiseWorld() {
		//Testing//////////
		//box = new Box(0.75,2,0.25);
		//box = new Box(4, 6, 0.5);
		box = new Box(0.2,0.2,0.2);
		//box.rotate(Vect3d.O, Vect3d.k, -0.5*Math.PI/16);
		box.phys.rotation.setAngVel(5/3d, 25d/3d, 0);
		box.pos.y = 0;
		renderer.addToScene(box);
		physics.add(box.phys);
		
		lightSource l = new lightSource(new Vect3f(200,200,200), 0);
		l.pos.set(0,0,5);
		renderer.addToScene(l);
		
		renderer.getCamera().pos.z = 2;
		renderer.getCamera().pos.y = 0;
		renderer.getCamera().lookAt(box.pos);
		//renderer.getCamera().setFOV((float)(Math.PI/10));
		///////////////////

		omega = new Cylinder(0.005, 10);
		renderer.addToScene(omega);
		omega.material.setTexture("red.png");
		box.material.setTexture("checkerboard-pattern.png");
		System.out.println("omega: " + omega.material.albedoPath);
		System.out.println("box: " + box.material.albedoPath);


	}
	
	@Override
	protected void onPhysicsUpdate(double dt) {
		//omega.lookAt(box.phys.rotation.w);
		
		Vect3d direction = box.phys.rotation.w.copy();
		Vect3d y = omega.basis.c2.copy();
		direction.normalize();
		y.normalize();

		Vect3d axis = Vect3d.cross(y, direction);
		double sin = Math.sqrt(axis.lengthSq());
		double cos = Vect3d.dot(y, direction);

		if(sin != 0)
			Vect3d.scale(axis, axis, 1.0/sin);
		

		omega.basis.rotate(axis, cos, sin);

		// Vect3d w = box.phys.rotation.w;
		// Vect3d x = Vect3d.cross(new Vect3d(), w, omega.basis.c2);
		// x.normalize();
		// Vect3d y = w.copy();
		// y.normalize();

		// Vect3d z = Vect3d.cross(new Vect3d(), x, y);

		// omega.basis.c1 = x;
		// omega.basis.c2 = y;
		// omega.basis.c3 = z;

		//omega.basis = box.basis;

		
	}
	
	@Override
	protected void onRenderUpdate() {
		Vect3d L = box.phys.rotation.getApproxAngularMomentum(new Vect3d());
		Vect3d w = box.phys.rotation.getAngVel(new Vect3d());
		Vect3d dw = Vect3d.cross(L, w);
		Mat3d.mult(dw, box.phys.rotation.getInvMomentOfInertia(new Mat3d()), dw);
//		double dot1 = Vect3d.dot(L,dw);
//		double dot2 = box.phys.rotation.getApproxE();
		
//		energyGain += dot1/600d;
//		
//		energyRate = (dot2 - oldEnergy)*600;
//		oldEnergy = dot2;
		double approxEnergy = box .phys.rotation.getApproxE();
		double targetEnergy = box.phys.rotation.getRotationalEnergy();
		
		// System.out.println("actual energy: " + approxEnergy);
		// System.out.println("target energy: " + targetEnergy);
		// System.out.println("Energy error: " + (targetEnergy - approxEnergy));
		// System.out.println();
		
		//  Logger.printVect(w, "w");
		// Logger.printVect(w, "w");
		// Logger.printVect(box.phys.rotation.angMomentum, "rL");

		//Logger.printMatrix(box.phys.rotation.getMomentOfInertia(new Mat3d()), "I");
		
//		Vect3d diff = Vect3d.sub(new Vect3d(), box.phys.rotation.w, box.phys.rotation.k);
//		System.out.println("dx : " + diff.x);
//		System.out.println("dy : " + diff.y);
//		System.out.println("dz : " + diff.z);
//		System.out.println();
		
		//print to check if basis vectors are still orthonormal.
		
	}
	
	
	
	Vect3d[] sphericalSpawn(double radius, int num) {
		Vect3d[] positions = new Vect3d[num];
		
		for(int i = 1; i <= num; i++) {
			double theta = 2*Math.PI*Math.random();
			double phi = 2*Math.PI*Math.random();
			
			double cosT = Math.cos(theta);
			double cosP = Math.cos(phi);
			
			double sinT = Math.sin(theta);
			double sinP = Math.sin(phi);
			
			positions[i-1] = new Vect3d(radius*cosT*cosP, radius*sinP, radius*sinT*cosP);
		}
		
		return positions;
	}
	
}
