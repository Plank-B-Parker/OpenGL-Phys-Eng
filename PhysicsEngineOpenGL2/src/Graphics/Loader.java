package Graphics;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Objects.Mesh;

public class Loader {
	ArrayList<Integer> vaoIDs = new ArrayList<Integer>();
	ArrayList<Integer> vboIDs = new ArrayList<Integer>();
	ArrayList<Integer> texureIDs = new ArrayList<Integer>();
	
	public Mesh generateMesh(float[] pos, float[] norms, float[] texts, int[] indices) {
		int vaoID = generateVAO();
		bindIndicesBuffer(indices);
		generateVBO(0, 3, pos);
		generateVBO(1, 3, norm);
		generateVBO(2, 2, texts);
		unbindVAO();
		
		return new Mesh(generateVect3fList(pos), generateVect3fList(pos), generateVect2fList(texts), vaoID, indices.length);
	}
	
	private ArrayList<Vector3f> generateVect3fList(float[] vects){
		ArrayList<Vector3f> vectList = new ArrayList<Vector3f>();
		for(int i = 0; i <= vects.length - 4; i += 3) {
			vectList.add(new Vector3f(vects[i], vects[i+1], vects[i+2]));
		}
		return vectList;
	}
	
	private ArrayList<Vector2f> generateVect2fList(float[] vects){
		ArrayList<Vector2f> vectList = new ArrayList<Vector2f>();
		for(int i = 0; i <= vects.length - 3; i += 2) {
			vectList.add(new Vector2f(vects[i], vects[i+1]));
		}
		return vectList;
	}
	
	
}
