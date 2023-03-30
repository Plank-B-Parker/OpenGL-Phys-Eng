package Graphics;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Texture {
	
	private int texID;
	private String filePath;
	

	public Texture(String path) {
		filePath = path;
		
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		
		//Texture boundary settings:
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		//Pixelate when stretching:
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		
		ByteBuffer image = stbi_load(filePath, width, height, channels, 0);
		
		if(image != null) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		}else {
			System.out.println("YOUR TEXTURES ARE BROKEN FOOL");
		}
		
		stbi_image_free(image);
		
	}
	
	public int getID() {
		return texID;
	}

}
