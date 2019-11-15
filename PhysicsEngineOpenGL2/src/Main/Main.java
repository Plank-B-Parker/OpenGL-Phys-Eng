package Main;

import org.lwjgl.opengl.Display;

import Graphics.DisplayManager;
import Graphics.Renderer;
import Objects.ObjectHandler;

public class Main implements Runnable{
	
	private boolean paused;
	private boolean running;
	
	private ObjectHandler objects;
	private Renderer renderer;
	
	public void startEngine() {
		
	}
	
	//Main loop.
	public void run() {
		
	}
	
	public static void main(String[] args) {
		DisplayManager.loadDisplay(700, 700, 60);
		while(!Display.isCloseRequested()) {
			
			DisplayManager.updateDisplay();
		}
	}

}
