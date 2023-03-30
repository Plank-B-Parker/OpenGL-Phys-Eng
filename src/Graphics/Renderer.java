package Graphics;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL41;

import Math.Mat3d;
import Math.Vect3d;
import Math.Vect3f;
import Objects.Material;
import Objects.Mesh;
import Objects.Object;

public class Renderer {

	/*
	 TODO: Add option to render to a quad with array of pixels for software rendering. 
			Can add methods like draw triangle and draw line, or draw circle.
			
	 TODO: Add instanced rendering for object renderer. 
	 		Maybe use texture atlas so many textures binded at once.
	 		
	 TODO: Add debug rendering.
	 
	 TODO: Store render commands in some sort of list 
	 		and compile the list into efficient draw call.
	 		So that user can say drawLine(...) and renderer sorts it out.
	*/
	
	private Camera Camera = new Camera((float)Math.PI/2);
	private int cameraUBOID;
	
	private ArrayList<lightSource> lights = new ArrayList<>();
	private int lightsUBOID;
	private int max_num_lights = 100;
	
	private objectRenderer objRenderer = new objectRenderer();
	
	private Map<Mesh, HashMap<Material, List<Object>>> meshes = new HashMap<>();
	
	public Renderer() {
		genCamera();
		genLights();	//Prepare lights.
		System.out.println("max vertex = " + GL11.glGetInteger(GL20.GL_MAX_VERTEX_UNIFORM_COMPONENTS)/4);
		System.out.println("max fragment = " + GL11.glGetInteger(GL20.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS)/4);
		System.out.println("max vertex = " + GL11.glGetInteger(GL41.GL_MAX_VERTEX_UNIFORM_VECTORS));
		System.out.println("max varying = " + GL11.glGetInteger(GL20.GL_MAX_VARYING_FLOATS)/4);
		System.out.println("max varying = " + GL11.glGetInteger(GL30.GL_MAX_VARYING_COMPONENTS));
		
	}
	
	public void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0,0,0,0);
	}
	
	public void renderScene() {
		renderObjects();
		//renderGUI();
	}
	
	private void renderObjects() {
		objRenderer.shader.start();
		loadCamera();
		loadLights();
		//objRenderer.loadLightsSources(lights);
		for(Entry<Mesh, HashMap<Material, List<Object>>> entry: meshes.entrySet()) {
			objRenderer.renderObject(entry.getKey(), entry.getValue());
		}
		objRenderer.shader.stop();
	}
	
	//TODO: Perhaps make a genUBO method.
	private void genCamera() {
		cameraUBOID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, cameraUBOID);
		ByteBuffer bytes = BufferUtils.createByteBuffer(5*16);
		GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, bytes, GL15.GL_STATIC_DRAW);
		GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 0, cameraUBOID);
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
		
		int cameraBlockIndex = GL31.glGetUniformBlockIndex(objRenderer.shader.programID, "Camera");
		GL31.glUniformBlockBinding(objRenderer.shader.programID, cameraBlockIndex, 0);
	}
	
	private void  genLights() {
		lightsUBOID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, lightsUBOID);
		ByteBuffer bytes = BufferUtils.createByteBuffer(4*16*max_num_lights);
		GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, bytes, GL15.GL_STATIC_DRAW);
		GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 1, lightsUBOID);
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
		
		int lightsBlockIndex = GL31.glGetUniformBlockIndex(objRenderer.shader.programID, "Lights");
		GL31.glUniformBlockBinding(objRenderer.shader.programID, lightsBlockIndex, 1);
	}
	
	private void loadCamera()
	{
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, cameraUBOID);
		ByteBuffer mappedBuffer = GL15.glMapBuffer(GL31.GL_UNIFORM_BUFFER, GL15.GL_READ_WRITE, 5*16, null);
		
		///
		storeVect3inMappedBuffer(Camera.pos, mappedBuffer);
		storeMat3inMappedBuffer(Camera.basis, mappedBuffer);
		
		float near = Camera.getzNear();
		float far = Camera.getzFar();
		
		float zs = (near+far)/(far-near);
		float zp = 2*near*far/(far - near);
		
		mappedBuffer.putFloat(zp);
		mappedBuffer.putFloat(zs);
		mappedBuffer.putFloat(Camera.getFOV());
		mappedBuffer.putFloat(1.0f/DisplayManager.getAspectRatio());
		///
		
		GL15.glUnmapBuffer(GL31.GL_UNIFORM_BUFFER);
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
	}
	
	private void loadLights() {
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, lightsUBOID);
		ByteBuffer mappedBuffer = GL15.glMapBuffer(GL31.GL_UNIFORM_BUFFER, GL15.GL_READ_WRITE, null);
		
		///
		int size = lights.size();
		int i = 0;
		for(lightSource light: lights) {
			storeVect3inMappedBuffer(light.pos, 16*i, mappedBuffer);
			storeVect3inMappedBuffer(light.basis.c3, 16*(i + max_num_lights), mappedBuffer);
			storeVect3inMappedBuffer(light.color, 16*(i + 2*max_num_lights), mappedBuffer);
			mappedBuffer.putFloat(16*(i + 3*max_num_lights), light.FOV);
			mappedBuffer.putFloat(16*(i + 3*max_num_lights) + 4, 0.0f);
			mappedBuffer.putFloat(16*(i + 3*max_num_lights) + 8, 0.0f);
			mappedBuffer.putFloat(16*(i + 3*max_num_lights) + 12, 0.0f);
			i++;
		}
//		if(size < max_num_lights) {
//			for(i = size; i < max_num_lights; i++) {
//				storeVect3inMappedBuffer(Vect3d.O, 16*i, mappedBuffer);
//				storeVect3inMappedBuffer(Vect3d.O, 16*(i + max_num_lights), mappedBuffer);
//				storeVect3inMappedBuffer(Vect3d.O, 16*(i + 2*max_num_lights), mappedBuffer);
//				mappedBuffer.putFloat(16*(i + 3*max_num_lights), 0.0f);
//				mappedBuffer.putFloat(16*(i + 3*max_num_lights) + 4, 0.0f);
//				mappedBuffer.putFloat(16*(i + 3*max_num_lights) + 8, 0.0f);
//				mappedBuffer.putFloat(16*(i + 3*max_num_lights) + 12, 0.0f);
//			}
//		}
		///
		
		GL15.glUnmapBuffer(GL31.GL_UNIFORM_BUFFER);
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
	}
	
	private void storeMat3inMappedBuffer(Mat3d m, ByteBuffer buffer) {
		buffer.putFloat((float) m.c1.x);
		buffer.putFloat((float) m.c1.y);
		buffer.putFloat((float) m.c1.z);
		buffer.putFloat(0.0f);
		
		buffer.putFloat((float) m.c2.x);
		buffer.putFloat((float) m.c2.y);
		buffer.putFloat((float) m.c2.z);
		buffer.putFloat(0.0f);
		
		buffer.putFloat((float) m.c3.x);
		buffer.putFloat((float) m.c3.y);
		buffer.putFloat((float) m.c3.z);
		buffer.putFloat(0.0f);
	}
	
	private void storeVect3inMappedBuffer(Vect3d v, ByteBuffer buffer) {
		buffer.putFloat((float)v.x);
		buffer.putFloat((float)v.y);
		buffer.putFloat((float)v.z);
		buffer.putFloat(0.0f);
	}
	
	private void storeVect3inMappedBuffer(Vect3d v, int index, ByteBuffer buffer) {
		buffer.putFloat(index, (float)v.x);
		buffer.putFloat(index + 4, (float)v.y);
		buffer.putFloat(index + 8, (float)v.z);
		buffer.putFloat(index + 12, 0.0f);
	}
	
	private void storeVect3inMappedBuffer(Vect3f v, ByteBuffer buffer) {
		buffer.putFloat(v.x);
		buffer.putFloat(v.y);
		buffer.putFloat(v.z);
		buffer.putFloat(0.0f);
	}
	
	private void storeVect3inMappedBuffer(Vect3f v, int index, ByteBuffer buffer) {
		buffer.putFloat(index, v.x);
		buffer.putFloat(index + 4, v.y);
		buffer.putFloat(index + 8, v.z);
		buffer.putFloat(index + 12, 0.0f);
	}
	
	public void addToScene(Object o) {
		HashMap<Material, List<Object>> map;
		List<Object> list;
		
		if(!meshes.containsKey(o.getMesh())) {
			map = new HashMap<>();
			list = new ArrayList<>();
			list.add(o);
			map.put(o.material, list);
			meshes.put(o.getMesh(), map);
			return;
		}
		
		map = meshes.get(o.getMesh());
		
		if(map.containsKey(o.material)) {
			list = map.get(o.material);
			if(list.contains(o)) return;
		}
		else {
			list = new ArrayList<>();
			map.put(o.material, list);
		}
		list.add(o);
	}
	
	/**Returns false if max number of light sources reached.
	 * Returns true otherwise.
	 */
	public boolean addToScene(lightSource L) {
		if(lights.size() >= 100) {
			return false;
		}
		lights.add(L);
		return true;
	}
	
	public void addToScene(Camera C) {
		Camera = C;
	}
	
	public Camera getCamera() {
		return Camera;
	}
	
	public void removeFromScene(Object o) {
		HashMap<Material, List<Object>> map = meshes.get(o.getMesh());
		List<Object> list = map.get(o.material);
		list.remove(o);
	}
	
	public void removeFromScene(lightSource L) {
		lights.remove(L);
	}
	
	public void removeFromScene(Camera C) {
		Camera = new Camera((float)Math.PI/2);
	}
	
	public void cleanUp() {
		objRenderer.shader.cleanUp();
	}
	
}