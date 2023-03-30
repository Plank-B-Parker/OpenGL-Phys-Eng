package Graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import Math.Mat3d;
import Math.Vect3d;
import Math.Vect3f;
import Objects.Material;
import Objects.Mesh;
import Objects.Object;
import Shaders.StaticShader;

public class objectRenderer {
	public StaticShader shader = new StaticShader();
	
	public objectRenderer() {		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	private void bindMesh(Mesh mesh) {
		GL30.glBindVertexArray(mesh.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		//Remember to initialise these.
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
	}
	
	private void bindMaterial(Material material) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, material.albedo);
	}
	
	private static Mat3d tempM1 = new Mat3d();
	public void renderObject(Mesh mesh, HashMap<Material, List<Object>> hashMap) {
		bindMesh(mesh);
		for(Material material: hashMap.keySet()) {
			bindMaterial(material);
			for(Object obj: hashMap.get(material)) {
				
				Mat3d worldTransf = tempM1;
				Mat3d.mult(worldTransf, obj.getOrientation(tempM1), obj.scale);
				
				shader.loadObject(obj.pos, worldTransf);
				GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		
		GL30.glBindVertexArray(0);
	}
}
