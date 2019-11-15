package Graphics;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	private static int WIDTH;
	private static int HEIGHT;
	private static int FPS_CAP;
	
	public static void loadDisplay(int width, int height, int fps) {
		WIDTH = width;
		HEIGHT = height;
		FPS_CAP = fps;
		
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Engine boiis");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, width, height);
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
	}
}
