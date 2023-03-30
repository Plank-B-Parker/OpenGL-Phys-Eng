package Objects;

import java.util.ArrayList;

import Math.Vect3d;

public class ObjectHandler {

	private ArrayList<Object> objects = new ArrayList<>();
	
	
	public ObjectHandler() {
	}
	
	public void initObjects() {
		
	}
	
	public void addObject(Object o) {
		objects.add(o);
	}
	
	//TODO: test this method.
	public void removeObject(Object o) {
		objects.remove(o);
	}
	
	public Object getObject(int index) {
		return objects.get(index);
	}
	
	public void cleanUp() {
		Loader.cleanUp();
		objects.clear();
	}
	
	public void updateObjects(double dt) {
		for(Object obj: objects) {
			
		}
		
	}

}
