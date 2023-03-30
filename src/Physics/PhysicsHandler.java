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
		if(physicsObjs.contains(phys)) return;
		
		physicsObjs.add(phys);
	}
	
	public void remove(Physics phys) {
		if(physicsObjs.contains(phys)) return;
		
		physicsObjs.remove(phys);
	}
	public void remove(int index) {
		if(index < 0 || index >= physicsObjs.size()) return;
		
		physicsObjs.remove(index);
	}
	
	
}
