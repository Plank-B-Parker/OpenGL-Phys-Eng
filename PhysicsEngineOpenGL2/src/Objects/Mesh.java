package Objects;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Mesh {
	
	private int vaoID;
	private int vertexCount;
	private int textID;
	private ArrayList<Vector3f> vertPos = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> vertNorms = new ArrayList<Vector3f>();
	private ArrayList<Vector2f> vertText = new ArrayList<Vector2f>();
	
	
	public Mesh(ArrayList<Vector3f> pos, ArrayList<Vector3f> norms, ArrayList<Vector2f> texts, int vaoID, int vertexCount, int textID) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.textID = textID;
		
		vertPos = pos;
		vertNorms = norms;
		vertText = texts;
	}
	//TODO: Make a method to get the farthest vertex in a certain direction.
	
	public Vector3f getVertexPos(int index) {
		return vertPos.get(index);
	}
	
	public Vector3f getVertexNorm(int index) {
		return vertNorms.get(index);
	}
	
	public Vector2f getVertexText(int index) {
		return vertText.get(index);
	}
	
	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public int getTextID() {
		return textID;
	}

}
