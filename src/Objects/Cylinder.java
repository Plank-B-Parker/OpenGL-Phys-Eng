package Objects;

import Math.Vect3d;
import Physics.MeshPhysics;

public class Cylinder extends Object{
    private static Mesh cylinderMesh = Loader.generateMesh("cylinder.obj", false);
    double radius, height;
    public MeshPhysics phys;
    
    public Cylinder(double radius, double height){
        super();      
        this.radius = radius;
        this.height = height;
        mesh = cylinderMesh;
        Vect3d.scale(scale.c1, scale.c1, radius);
		Vect3d.scale(scale.c2, scale.c2, height/2);
		Vect3d.scale(scale.c3, scale.c3, radius);

        phys = new MeshPhysics(mesh, 1, pos, orientation, _orientation, basis, _basis, scale);
    }
}
