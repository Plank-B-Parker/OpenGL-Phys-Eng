package Main;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

public class DisplayManager {
	
	private static int WIDTH;
	private static int HEIGHT;
	private static float aspectRatio;
	private static int FPS_CAP;
	
	private static long window;
	
//	public DisplayManager(int width, int height, int fps) {
//		WIDTH = width;
//		HEIGHT = height;
//		FPS_CAP = fps;
//		
//		aspectRatio = (float)(WIDTH)/(float)(HEIGHT);
//		glfwDefaultWindowHints();
//		glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
//		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
//		
//		windowID = glfwCreateWindow(width, height, "Your mother is a great woman", NULL, NULL);
//		if(windowID == NULL) 
//			throw new RuntimeException("YOU SUCK! YOUR WINDOW CANT LOAD AND YOU SUCK!");
//		
//	}

	
	public static void loadDisplay(int width, int height, int fps) {
		WIDTH = width;
		HEIGHT = height;
		FPS_CAP = fps;
		aspectRatio = (float)width/(float)height;
		
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		//glfwDefaultWindowHints(); // optional, the current window hints are already the default
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		
		System.out.println("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current.
		glfwMakeContextCurrent(window);
		//For multiple displays, you need one display per thread, with each window made the on main thread. Then you call this method in each thread.
		//Also Polling events in main thread.
		
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

//	public static void loadDisplay(int width, int height, int fps) {
//		WIDTH = width;
//		HEIGHT = height;
//		FPS_CAP = fps;
//		aspectRatio = (float)width/(float)height;
//		
//		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
//		
//		try {
//			Display.setDisplayMode(new DisplayMode(width, height));
//			Display.create(new PixelFormat(), attribs);
//			Display.setTitle("Engine boiis");
//		} catch (LWJGLException e) {
//			e.printStackTrace();
//		}
//		
//		GL11.glViewport(0, 0, width, height);
//	}
	
	public static boolean isCloseRequested() {
		return !glfwWindowShouldClose(window);
	}
	
	public static void closeDisplay() {
		//?DisplayManager.destroy();
	}
	
	public static void updateDisplay() {
		glfwSwapBuffers(window);
		//?Display.sync(FPS_CAP);
		//?Display.update();
	}
	
	public static float getAspectRatio() {
		return aspectRatio;
	}
}
