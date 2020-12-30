package Objects;

import Math.Vect3d;

public class Triangle {

	public Vect3d p1,p2,p3;
	public Vect3d n1,n2,n3;
	public Vect3d midPoint = new Vect3d();
	private Vect3d normal = new Vect3d(), area = new Vect3d();
	
	public Triangle(Vect3d p1, Vect3d p2, Vect3d p3,
					Vect3d n1, Vect3d n2, Vect3d n3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		
		Vect3d.add(midPoint, p1, p2);
		Vect3d.add(midPoint, p3, midPoint);
		Vect3d.scale(midPoint, midPoint, 1d/3d);
		
		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
		
		Vect3d.add(normal, n1, n2);
		Vect3d.add(normal, normal, n3);
		normal.normalize();
		
		Vect3d d1 = new Vect3d();
		Vect3d d2 = new Vect3d();
		Vect3d.sub(d1, p3, p1);
		Vect3d.sub(d2, p2, p1);
		Vect3d.cross(area, d1, d2);
		Vect3d.scale(area, area, 1d/2d);
		
		if(Vect3d.dot(area, normal) < 0) {
			Vect3d.scale(area, area, -1);
		}
	}
	
	public Vect3d getNormal() {
		return normal;
	}
	
	public Vect3d getArea() {
		return area;
	}

}
