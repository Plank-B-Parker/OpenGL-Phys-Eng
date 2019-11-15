package Objects;

import java.util.ArrayList;

public class ObjectHandler {

	private ArrayList<Object> objects = new ArrayList<Object>();
	
	public ObjectHandler() {
		
	}
	
	public void addObject(Object o) {
		objects.add(o);
	}
	
	public void removeObject(Object o) {
		objects.remove(o);
	}
	
	public Object getObject(int index) {
		return objects.get(index);
	}
	
	public void renderObjects() {
		
	}
	
	public void updateObjects() {
		
	}

}
