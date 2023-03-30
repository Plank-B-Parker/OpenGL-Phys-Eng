package Shaders;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import Math.Mat3d;
import Math.Mat3f;
import Math.Vect3d;
import Math.Vect3f;

public abstract class ShaderProgram {

	public int programID;
	public int vertexShaderID;
	public int fragmentShaderID;
	
	private static FloatBuffer mat3fBuff = BufferUtils.createFloatBuffer(9);
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
		bindAttributes();
		
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteProgram(programID);
		
	}

	protected abstract void bindAttributes();
	
	protected void bindAttribute(String attName, int attribute) {
		GL20.glBindAttribLocation(programID, attribute, attName);
	}
	
	//Loading uniforms.
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadVector(int location, Vect3f v) {
		GL20.glUniform3f(location, v.x, v.y, v.z);
	}
	
	protected void loadVector(int location, Vect3d v) {
		GL20.glUniform3f(location, (float)v.x, (float)v.y, (float)v.z);
	}
	
	protected void loadBol(int location, boolean b) {
		if(b) {
			GL20.glUniform1f(location, 1);
		}else {
			GL20.glUniform1f(location, 0);
		}
	}
	
	protected void loadMatrix3f(int location, Mat3f matrix) {
		matrix.store(mat3fBuff);
		mat3fBuff.flip();
		GL20.glUniformMatrix3fv(location, false, mat3fBuff);
	}
	
	protected void loadMatrix3d(int location, Mat3d matrix) {
		matrix.store(mat3fBuff);
		mat3fBuff.flip();
		GL20.glUniformMatrix3fv(location, false, mat3fBuff);
	}
	
	
	@SuppressWarnings("deprecation")
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
			
		}catch(IOException e) {
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.print(shaderSource);
			
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader. ");
			
			System.out.println("GLSL Version: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
			System.out.println("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
			
			System.exit(-1);
		}
		return shaderID;
	}
	
	
	protected void editShader(String oldLine, String newLine, String fileName) {
		try {
			BufferedReader file = new BufferedReader(new FileReader(fileName + ".txt"));
			StringBuffer inputBuffer = new StringBuffer();
			String line;
			
			while((line = file.readLine()) != null) {
				if(line == oldLine) {
					line = newLine;
				}
				inputBuffer.append(line);
				inputBuffer.append('\n');
			}
			file.close();
			
			FileOutputStream fileOut = new FileOutputStream(fileName + ".txt");
			fileOut.write(inputBuffer.toString().getBytes());
			fileOut.close();
			
		}catch(Exception e) {
			System.err.println("Could not read file!");
		}
	}
}
