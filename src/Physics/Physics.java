package Physics;

import java.util.ArrayList;
import java.util.List;

import Math.Vect3d;
/*
 	TODO:	Find a way to generalise this class to not use meshes. Make this a subclass for rigid bodies?
 	
 	TODO:	Find a way to implement different integration schemes. 
 			Perhaps also store momentum instead of velocity. 
 			Research more on Hamiltonians to extend above statement for more physics objects than just
 			rigid bodies and particles. Find out about conjugate variables (e.g. position-momentum, entropy-temperature and pressure volume)
 			
 			Idea:
 			
 			//Physics Handler has to sort out order of computing forces.
 	
			 	updateVerlet(dt){
			 		
			 		momentum += F(position)*dt/2;
			 		position += _Mass*momentum*dt;
			 		momentum += F(position)*dt/2;
			 	
			 	}
			 	
			 	updateBackWardEuler(dt){
			 		
			 		momentum += F(position)*dt;
			 		position += _Mass*momentum*dt;
			 		
			 	}
 */

public abstract class Physics {
	
	public double mass;
	protected double invMass;
	public Vect3d pos;
	public Vect3d vel = new Vect3d();
	public Vect3d acc = new Vect3d();
	public Vect3d force = new Vect3d();
	
	//Physics attributes;
	List<PhysicsAttrib> attributes = new ArrayList<>();
	
	//Uniform fields.
	private static Vect3d gravField = new Vect3d();
	private static Vect3d elecField = new Vect3d();
	
	private static double G;
	private static double E;
	
	public Physics(double mass, Vect3d pos) {
		this.mass = mass;
		invMass = 1d/mass;
		this.pos = pos;
	}
	
	public void update(double dt) {
		acc = Vect3d.scale(acc, force, invMass);
		vel = Vect3d.increment(vel, acc, dt);
		pos = Vect3d.increment(pos, vel, dt);
		for(PhysicsAttrib attrib: attributes) {
			attrib.update(dt);
		}
	}
	
	public void calcForces() {
		force.set(0,0,0);
		for(PhysicsAttrib attrib: attributes) {
			attrib.resetForces();
		}
		computeForces();
	}
	
	protected abstract void computeForces();
	
	
}
