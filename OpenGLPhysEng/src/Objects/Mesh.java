package Objects;

import java.util.ArrayList;

import Math.Mat3d;
import Math.Vect3d;
import Math.Vect3f;

public class Mesh {
	
	private int vaoID;
	
	private int vertexCount;
	private int triangleCount;
	
	private ArrayList<Vect3d> vertPos = new ArrayList<>();
	private ArrayList<Vect3d> vertNorms = new ArrayList<>();
	private ArrayList<Vect3f> vertCol = new ArrayList<>();
	private ArrayList<Triangle> tris = new ArrayList<>();
	
	public Mesh(ArrayList<Vect3d> pos, ArrayList<Vect3d> norms, ArrayList<Triangle> tris, int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		
		vertPos = pos;
		vertNorms = norms;
		this.tris = tris;
		triangleCount = tris.size();
	}
	
	public void addColor(ArrayList<Vect3f> colors) {
		vertCol = colors;
		
		float[] cols = new float[colors.size()];
		for(int i = 0; i <= cols.length - 4; i++) {
			cols[i] = colors.get(i).x;
			cols[i+1] = colors.get(i).y;
			cols[i+2] = colors.get(i).z;
		}
		
		Loader.changeColors(cols, this);
	}
	
	final private Vect3d temp1 = new Vect3d();
	final private Vect3d temp2 = new Vect3d();
	final private Vect3d temp3 = new Vect3d();
	final private Vect3d temp4 = new Vect3d();
	public double calcVolume(Mat3d scale) {
		double volume = 0;
		for(Triangle t: tris) {
			Vect3d v1 = Mat3d.mult(temp1, scale, t.p1);
			Vect3d v2 = Mat3d.mult(temp2, scale, t.p2);
			Vect3d v3 = Mat3d.mult(temp3, scale, t.p3);
			
			Vect3d cross = Vect3d.cross(temp4, v1, v2);
			volume += Vect3d.dot(cross, v3)/6d; 
		}
		return volume;
	}
	
	public Mat3d calcSpecificMomentOfInertia(Mat3d scale) {
	    Mat3d I = new Mat3d();
	    double xx = 0,yy = 0,zz = 0,xy = 0,xz = 0,yz= 0, sign = 1, determinant = 0;
	    Vect3d v1 = temp1, v2 = temp2, v3 = temp3, normal, v_avg = new Vect3d(), temp = new Vect3d();
	    for(Triangle t: tris) {
	    	v1 = Mat3d.mult(temp1, scale, t.p1);
	    	v2 = Mat3d.mult(temp2, scale, t.p2);
	    	v3 = Mat3d.mult(temp3, scale, t.p3);
	    	
	    	Vect3d.add(v_avg, v1, v2);
	    	Vect3d.add(v_avg, v_avg, v3);
	    	Vect3d.scale(v_avg, v_avg, 1d/3d);
	    	normal = t.getNormal();
	    	sign = Math.signum(Vect3d.dot(normal, v_avg));
	    
	    	Vect3d.cross(temp, v1, v2);
	    	determinant = Vect3d.dot(temp, v3);
	    	
	    	
	    	xx += determinant*sign*((v1.x*v1.x + v2.x*v2.x + v3.x*v3.x)/60d + (v1.x*v2.x + v1.x*v3.x + v3.x*v2.x)/60d);
	    	yy += determinant*sign*((v1.y*v1.y + v2.y*v2.y + v3.y*v3.y)/60d + (v1.y*v2.y + v1.y*v3.y + v3.y*v2.y)/60d);
	    	zz += determinant*sign*((v1.z*v1.z + v2.z*v2.z + v3.z*v3.z)/60d + (v1.z*v2.z + v1.z*v3.z + v3.z*v2.z)/60d);
	    	
	    	xy += determinant*sign*((v1.x*v1.y + v2.x*v2.y + v3.x*v3.y)/60d + (v1.x*v2.y + v1.y*v2.x)/120d + (v1.x*v3.y + v1.y*v3.x)/120d + (v3.x*v2.y + v3.y*v2.x)/120d);
	    	xz += determinant*sign*((v1.x*v1.z + v2.x*v2.z + v3.x*v3.z)/60d + (v1.x*v2.z + v1.z*v2.x)/120d + (v1.x*v3.z + v1.z*v3.x)/120d + (v3.x*v2.z + v3.z*v2.x)/120d);
	    	yz += determinant*sign*((v1.z*v1.y + v2.z*v2.y + v3.z*v3.y)/60d + (v1.z*v2.y + v1.y*v2.z)/120d + (v1.z*v3.y + v1.y*v3.z)/120d + (v3.z*v2.y + v3.y*v2.z)/120d);
	    }
	    
	    I.c1.x = yy + zz;
	    I.c2.y = xx + zz;
	    I.c3.z = yy + xx;
	    
	    I.c1.y = -xy;
	    I.c2.x = -xy;
	    
	    I.c1.z = -xz;
	    I.c3.x = -xz;
	    
	    I.c3.y = -yz;
	    I.c2.z = -yz;
	    
	    Mat3d.scale(I, I, 1d/calcVolume(scale));
	    
		return I;
	}
	
	public Vect3d getVertexPos(int index) {
		return vertPos.get(index);
	}
	
	public Vect3d getVertexNorm(int index) {
		return vertNorms.get(index);
	}
	
	public Triangle getTriangle(int index) {
		return tris.get(index);
	}
	
	//TODO: Make methods for getting the farthest vertex given a direction.
	
	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public int getTriangleCount() {
		return triangleCount;
	}

}
