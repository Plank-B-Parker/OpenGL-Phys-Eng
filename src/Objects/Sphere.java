package Objects;

public class Sphere extends Object{
	private static Mesh sphereMesh = Loader.generateMesh("sphere", false);
	public double radius;
	
	public Sphere() {
		super();
		mesh = sphereMesh;
		radius = 1;
	}
	
	public Sphere(double radius) {
		super();
		mesh = sphereMesh;
		this.radius = radius;
	}
}
