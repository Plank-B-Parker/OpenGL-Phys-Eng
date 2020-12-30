package Objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import Math.Vect2f;
import Math.Vect3d;
import Math.Vect3i;

public class Loader {
	static ArrayList<Integer> vaoIDs = new ArrayList<Integer>();
	static ArrayList<Integer> vboIDs = new ArrayList<Integer>();
	static ArrayList<Integer> textureIDs = new ArrayList<Integer>();
	
	public static Mesh generateMesh(float[] pos, float[] norms, float[] texts, int[] indices) {
		int vaoID = generateVAO();
		bindIndicesBuffer(indices);
		generateVBO(0, 3, pos);
		generateVBO(1, 3, norms);
		generateVBO(2, 2, texts);
		//Color VBO.
		generateVBO(3, 3, new float[pos.length]);
		//Check if VBOs can be changed or if are zero if not initialised.
		
		unbindVAO();
		
		ArrayList<Vect3d> positions = generateVect3dList(pos);
		ArrayList<Vect3d> normals = generateVect3dList(norms);
		
		return new Mesh(positions, normals, generateTris(positions, normals, indices), vaoID, indices.length);
	}
	public static Mesh generateMesh(String fileName, boolean flatFaced) {
		return loadMeshFromFile(fileName, flatFaced);
	}
	
	public static int loadTexture(String fileName) {
		Texture texture = null;
		
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/textures/" + fileName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textureIDs.add(textureID);
		return textureID;
	}
	
	//Changing VBOs.
	public static void changeColors(float[] colors, Mesh m) {
		int index = 4*vaoIDs.indexOf(m.getVaoID()) + 3;
		changeVBO(vboIDs.get(index), colors);
	}
	public static void changePositions(float[] positions, Mesh m) {
		int index = 4*vaoIDs.indexOf(m.getVaoID());
		changeVBO(vboIDs.get(index), positions);
	}
	public static void changeUVs(float[] uvs, Mesh m) {
		int index = 4*vaoIDs.indexOf(m.getVaoID()) + 2;
		changeVBO(vboIDs.get(index), uvs);
	}
	public static void changeNorms(float[] norms, Mesh m) {
		int index = 4*vaoIDs.indexOf(m.getVaoID()) + 1;
		changeVBO(vboIDs.get(index), norms);
	}
	
	private static void changeVBO(int vboID, float[] data) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer vbo = storeInFloatBuffer(data);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	
	public static void cleanUp() {
		for(int vbo: vboIDs) {
			GL30.glDeleteFramebuffers(vbo);
		}
		for(int vao: vaoIDs) {
			GL30.glDeleteVertexArrays(vao);
		}
		for(int texture: textureIDs) {
			GL11.glDeleteTextures(texture);
		}
	}
	
	private static int generateVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaoIDs.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private static void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vboIDs.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer vbo = storeInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo, GL15.GL_STATIC_DRAW);
	}
	
	private static void generateVBO(int attribIndex, int dimension, float[] data) {
		int vboID = GL15.glGenBuffers();
		vboIDs.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer vbo = storeInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vbo, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribIndex, dimension, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	
	//Generates data to store for physics.
	private static ArrayList<Vect3d> generateVect3dList(float[] vects){
		ArrayList<Vect3d> vectList = new ArrayList<Vect3d>();
		for(int i = 0; i <= vects.length - 4; i += 3) {
			vectList.add(new Vect3d(vects[i], vects[i+1], vects[i+2]));
		}
		return vectList;
	}
	
	private static ArrayList<Vector2f> generateVect2fList(float[] vects){
		ArrayList<Vector2f> vectList = new ArrayList<Vector2f>();
		for(int i = 0; i <= vects.length - 3; i += 2) {
			vectList.add(new Vector2f(vects[i], vects[i+1]));
		}
		return vectList;
	}
	
	private static ArrayList<Triangle> generateTris(ArrayList<Vect3d> pos, ArrayList<Vect3d> norms, int[] indices){
		ArrayList<Triangle> Tris = new ArrayList<Triangle>();
		
		for(int i = 0; i <= indices.length - 4; i += 3) {
			int index1 = indices[i];
			int index2 = indices[i+1];
			int index3 = indices[i+2];
			Triangle newTri = new Triangle(pos.get(index1), pos.get(index2), pos.get(index3),
											norms.get(index1), norms.get(index2), norms.get(index3));
			Tris.add(newTri);
		}
		
		
		return Tris;
	}
	
	//Storing data in buffers.
	private static FloatBuffer storeInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private static IntBuffer storeInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	//TODO: Change method to handle objects without texture and normal info
	private static Mesh loadMeshFromFile(String fileName, boolean flatFaced) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/3d models/"+fileName+".obj"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		ArrayList<Vect3d> positions = new ArrayList<Vect3d>();
		ArrayList<Vect2f> textures = new ArrayList<Vect2f>();
		ArrayList<Vect3d> normals = new ArrayList<Vect3d>();
		ArrayList<Vect3i> indices = new ArrayList<Vect3i>();
		ArrayList<Triangle> triangles =  new ArrayList<Triangle>();
		
		float[] posArray = null;
		float[] normsArray = null;
		float[] texArray = null;
		int[] iArray = null;
		
		try {
			
			//Store vertex data into lists.
			line = reader.readLine();
			while(line != null) {
				String[] currentLine = line.split(" ");
				if(line.startsWith("v ")) {
					Vect3d position = new Vect3d(Float.parseFloat(currentLine[1]),
												Float.parseFloat(currentLine[2]),
												Float.parseFloat(currentLine[3])
												);
					positions.add(position);
					
				}else if(line.startsWith("vt ")) {
					Vect2f texture = new Vect2f(Float.parseFloat(currentLine[1]),
												Float.parseFloat(currentLine[2])
												);
					textures.add(texture);
					
				}else if(line.startsWith("vn ")) {
					Vect3d normal = new Vect3d(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3])
							);
					normals.add(normal);
					
				}else if(line.startsWith("f ")) {
					
					break;
				}	
				line = reader.readLine();
			}
			
			//Store the indices of the faces and fill triangle list.
			while(line != null) {
				if(!line.startsWith("f")) {
					line = reader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				String[] v1 = currentLine[1].split("/");
				String[] v2 = currentLine[2].split("/");
				String[] v3 = currentLine[3].split("/");
				
				int v1x = Integer.parseInt(v1[0]) - 1;
				int v1y = Integer.parseInt(v1[1]) - 1;
				int v1z = Integer.parseInt(v1[2]) - 1;
				
				int v2x = Integer.parseInt(v2[0]) - 1;
				int v2y = Integer.parseInt(v2[1]) - 1;
				int v2z = Integer.parseInt(v2[2]) - 1;
				
				int v3x = Integer.parseInt(v3[0]) - 1;
				int v3y = Integer.parseInt(v3[1]) - 1;
				int v3z = Integer.parseInt(v3[2]) - 1;
				
				
				indices.add(new Vect3i(v1x, v1y, v1z));
				
				indices.add(new Vect3i(v2x, v2y, v2z));
				
				indices.add(new Vect3i(v3x, v3y, v3z));
				
				triangles.add(generateTri(v1, v2, v3, positions, normals));
				
				line = reader.readLine();
			}
			
			reader.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//Find centre of mass. Uniform density for now.
		double totVol = 0;
		Vect3d centre = new Vect3d();
		for(Triangle tri: triangles) {
			Vect3d v2v1 = Vect3d.sub(new Vect3d(), tri.p2, tri.p1);
			Vect3d v3v1 = Vect3d.sub(new Vect3d(), tri.p3, tri.p1);
			Vect3d v2Xv3 = Vect3d.cross(new Vect3d(), v2v1, v3v1);
			
			Vect3d centreTet = Vect3d.add(new Vect3d(), tri.p1, tri.p2);
			Vect3d.add(centreTet, centreTet, tri.p3);
			Vect3d.scale(centreTet, centreTet, 0.25);
			
			double volTet = Vect3d.dot(v2Xv3, tri.p1);
			totVol += volTet;
			
			Vect3d.scale(centreTet, centreTet, volTet);
			Vect3d.add(centre, centre, centreTet);
		}
		
		Vect3d.scale(centre,centre, 1d/totVol);
		///////////////////////////////////////
		
		for(Vect3d pos: positions) {
			Vect3d.sub(pos, pos, centre);
		}
		/////////////////////////////////////////
		if(flatFaced)
			duplicateVertices(positions, indices);
		
		normsArray = new float[3*positions.size()];
		texArray = new float[2*positions.size()];		
		posArray = new float[3*positions.size()];
		iArray = new int[indices.size()];
		
		//Store data into arrays.
		int i = 0;
		for(Vect3i index: indices) {
			int posIndex = index.x;
			int texIndex = index.y;
			int normIndex = index.z;
			
			Vect2f tex = textures.get(texIndex);
			Vect3d norm = normals.get(normIndex);
			
			texArray[2*posIndex] = tex.x;
			texArray[2*posIndex + 1] = 1-tex.y;
			
			normsArray[3*posIndex] = (float) norm.x;
			normsArray[3*posIndex + 1] = (float) norm.y;
			normsArray[3*posIndex + 2] = (float) norm.z;
			
			iArray[i] = posIndex;
			i++;
		}
		i = 0;
		for(Vect3d pos: positions) {
			posArray[3*i] = (float) pos.x;
			posArray[3*i + 1] = (float) pos.y;
			posArray[3*i + 2] = (float) pos.z;
			i++;
		}
		
		//Generate VBOs with arrays and store in VAO.
		int vaoID = generateVAO();
		bindIndicesBuffer(iArray);
		generateVBO(0, 3, posArray);
		generateVBO(1, 3, normsArray);
		generateVBO(2, 2, texArray);
		generateVBO(3, 3, new float[posArray.length]);
		
		unbindVAO();
		
		return new Mesh(positions, normals, triangles, vaoID, indices.size());
		
	}
	
	private static void duplicateVertices(ArrayList<Vect3d> positions, ArrayList<Vect3i> indices) {
		int[] repeatArray = new int[indices.size()];
		ArrayList<Integer> skipList = new ArrayList<>();
		int repeatNumber = 0;
		for(int i = 0; i < indices.size()-1; i ++) {
			
			Vect3i indexI = indices.get(i);
			if(skipList.contains(i))
				continue;
			
			for(int j = i; j < indices.size(); j ++) {
				
				Vect3i indexJ = indices.get(j);
				if(indexI.equals(indexJ)) {
					repeatArray[j] = repeatNumber;
					skipList.add(j);
				}
			}
			repeatNumber++;
		}
		
		for(int i = 0; i < indices.size()-1; i ++) {
			
			Vect3i indexI = indices.get(i);
			for(int j = i+1; j < indices.size(); j ++) {
				
				Vect3i indexJ = indices.get(j);
				
				if(indexI.x == indexJ.x && !indexI.equals(indexJ)) {
					repeatNumber = repeatArray[j];
					Vect3d pos = positions.get(indices.get(i).x);
					positions.add(pos);
					
					int k=0;
					for(int number: repeatArray) {
						if (number == repeatNumber) {
							Vect3i index = indices.get(k);
							index.x = positions.size() - 1;
						}
						k++;
					}
				}
				
			}
		}
	}
	
	private static Triangle generateTri(String[] v1, String[] v2, String[] v3, 
										List<Vect3d> pos, List<Vect3d> norms) {
		int pos1 = Integer.parseInt(v1[0]) - 1;
		int norm1 = Integer.parseInt(v1[2]) - 1;
		
		int pos2 = Integer.parseInt(v2[0]) - 1;
		int norm2 = Integer.parseInt(v2[2]) - 1;

		int pos3 = Integer.parseInt(v3[0]) - 1;
		int norm3 = Integer.parseInt(v3[2]) - 1;
		
		return new Triangle(pos.get(pos1), pos.get(pos2), pos.get(pos3),
							norms.get(norm1), norms.get(norm2), norms.get(norm3));
	}
	
	
}
