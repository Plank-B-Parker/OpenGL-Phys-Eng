package Objects;
import Math.Vect3d;
import Physics.MeshPhysics;

public class Box extends Object {
	private static Mesh boxMesh = Loader.generateMesh("T-Shape.obj", true);
	public double width;
	public double height;
	public double depth;
	public MeshPhysics phys;
	
	public Box() {
		super();
		mesh = boxMesh;
		phys = new MeshPhysics(mesh, 1, pos, orientation, _orientation, basis, _basis, scale);
		width = 1;
		height = 1;
		depth = 1;
	}
	
	public Box(double width, double height, double depth) {
		super();
		Vect3d.scale(scale.c1, scale.c1, width/2);
		Vect3d.scale(scale.c2, scale.c2, height/2);
		Vect3d.scale(scale.c3, scale.c3, depth/2);
		
		this.width = width;
		this.height = height;
		this.depth = depth;
		
		xl = width/2;
		yl = height/2;
		zl = depth/2;
		
		mesh = boxMesh;
		
		phys = new MeshPhysics(mesh, 1, pos, orientation, _orientation, basis, _basis, scale);
	}

}
