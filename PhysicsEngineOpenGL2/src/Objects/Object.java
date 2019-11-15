package Objects;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Object {

	private int ID;
	private Vector3f pos;
	private Matrix3f basis;
	private Mesh mesh;
	
	public Object(int ID) {
		this.ID = ID;
		pos = new Vector3f(0,0,0);
		basis = new Matrix3f();
	}
	
	public Object(int ID, Vector3f pos, Matrix3f basis) {
		this.ID = ID;
		this.pos = new Vector3f(0,0,0);
		this.basis = new Matrix3f();
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public Matrix3f getBasis() {
		return basis;
	}

	public void setBasis(Matrix3f basis) {
		this.basis = basis;
	}

	public Mesh getMesh() {
		return mesh;
	}
	

}
