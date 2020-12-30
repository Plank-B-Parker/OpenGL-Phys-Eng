package Shaders;

import org.lwjgl.opengl.GL20;

import Math.Mat3d;
import Math.Vect3d;
import Math.Vect3f;

public class StaticShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/Shaders/staticVertexShader"; 
	private static final String FRAGMENT_FILE = "src/Shaders/staticFragmentShader";
	
	private int obj_basis;
	private int obj_pos;
	
	private int[] lightPos;
	private int[] lightForward;
	private int[] lightCol;
	private int[] lightFOV;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		//name of matrix in shader.
		obj_basis = super.getUniformLocation("objBasis");
		obj_pos = super.getUniformLocation("objPos");		
		
	}
	
	public void loadObject(Vect3d pos, Mat3d matrix) {
		super.loadMatrix3d(obj_basis, matrix);
		super.loadVector(obj_pos, pos);
	}
	
	public void loadLights(Vect3d[] pos, Vect3d[] forward, Vect3f[] col, float[] FOV) {
		for(int i = 0; i < pos.length; i++) {
			super.loadVector(lightPos[i], pos[i]);
			super.loadVector(lightForward[i], forward[i]);
			super.loadVector(lightCol[i], col[i]);
			super.loadFloat(lightFOV[i], FOV[i]);
		}
		if(pos.length<100) {
			for(int i = pos.length-1; i<100; i++) {
				super.loadVector(lightPos[i], Vect3d.O);
				super.loadVector(lightForward[i], Vect3d.O);
				super.loadVector(lightCol[i], Vect3d.O);
				super.loadFloat(lightFOV[i], 0);
			}
		}
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute("Pos", 0);
		super.bindAttribute("Norm", 1);
		super.bindAttribute("UV", 2);
		super.bindAttribute("Col", 3);
		
	}

}
