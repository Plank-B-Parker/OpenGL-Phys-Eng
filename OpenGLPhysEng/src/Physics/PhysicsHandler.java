package Physics;

import java.util.ArrayList;
import java.util.List;

public class PhysicsHandler {
	List<Physics> physicsObjs = new ArrayList<Physics>();
	
	public void update(double dt) {
		for(Physics physObj: physicsObjs) {
			physObj.calcForces();
		}
		for(Physics physObj: physicsObjs) {
			physObj.update(dt);
		}
	}
	
	public void add(Physics phys) {
		physicsObjs.add(phys);
	}
	
	public void remove(Physics phys) {
		physicsObjs.remove(phys);
	}
	public void remove(int index) {
		physicsObjs.remove(index);
	}
	
	
}
