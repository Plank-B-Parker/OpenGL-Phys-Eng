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

import Graphics.DisplayManager;
import Graphics.Renderer;
import Graphics.lightSource;
import Math.Mat3d;
import Math.MathsSoup;
import Math.Quaternion;
import Math.Vect3d;
import Math.Vect3f;
import Objects.Box;
import Objects.ObjectHandler;
import Physics.PhysicsHandler;

public abstract class Main implements Runnable{
	
	private boolean paused;
	private boolean running;
	private boolean debugMode = true;
	
	protected ObjectHandler objects = new ObjectHandler();
	protected PhysicsHandler physics = new PhysicsHandler();
	protected Renderer renderer;
	protected static boolean Logging = true;
	
	private double timeStep;
	private double FPS;
	private double UPS;
	private double speed = 1;
	
	private Thread loop;
	
	public Main() {
		startEngine();
	}
	
	public void startEngine() {
		loop = new Thread();
		start();
	}
	
	//Starting thread.
	public synchronized void start() {
		loop = new Thread(this);
		loop.start();
		running = true;
		loop();
		
	}
	//Stopping thread
	public synchronized void stop() {
		try{
			loop.join();
			running= false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	void initOpenGL() {
		GLFWErrorCallback.createPrint(System.err).set();
		if(!glfwInit()) 
			throw new IllegalStateException("glfw setup fail");
		
	}
	
	//Main loop.
	public void loop() {
		
		DisplayManager.loadDisplay(850, 850, 60);
		GL.createCapabilities();
		System.out.println("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
		
		renderer = new Renderer();
		initialiseWorld();
		
		double LastTime = System.nanoTime();
		double AmountOfTicks = 600;
		double AmountOfFrames = 60;
		
		//ns is time between each frame.
		double ns = 1000000000/AmountOfTicks;
		
		//change in time between now and then.
		double delta = 0;
		
		long timer = System.currentTimeMillis();
		int frames = 0;
		int updates = 0;
		
		//Current time and last time, for timeStep.
		double t1 = 0, t2 = 0;
		
		
		double time = 0;
		
		while(!DisplayManager.isCloseRequested()) {
			long now = System.nanoTime();
			delta += (now - LastTime)/ns;
			LastTime = now;
			
			//find out if time that has passed is longer than or equal to time between frames.
			while(AmountOfFrames*delta/AmountOfTicks >= 1) {
				
				render();
				onRenderUpdate();
				frames++;
				
				//find out if time passed is longer than time between updates
				while(delta >= 1) {
					t2 = System.nanoTime();
					//Update
					timeStep = (t2-t1)/1000000000;
					if(!paused) {
						if(debugMode) {
							physics.update(speed/AmountOfTicks);
							onPhysicsUpdate(speed/AmountOfTicks);
							time += 1/AmountOfTicks;
							//renderer.getCamera().lookAt(box.pos);
						}
						else
							physics.update(timeStep);
							onPhysicsUpdate(timeStep);
					
						time += timeStep;
					}
					t1 = t2;
					
					updates++;
					delta--;
				}
			}
			
			//for FPS and UPS
		  if(System.currentTimeMillis() - timer > 1000){
	            timer += 1000;
	            FPS = frames;
	            UPS = updates;
//	            System.out.println("fps: "+FPS);
	          //  System.out.println("ups: "+UPS);
		        frames = 0;
		        updates = 0; 
	        }
		}
		
		cleanUp();
	}
	
	private void cleanUp() {
		objects.cleanUp();
		renderer.cleanUp();
	}
		
	public void render() {
		renderer.prepare();
		renderer.renderScene();
		DisplayManager.updateDisplay();
		
		glfwPollEvents();
	}

	public static boolean Logging(){
		return Logging;
	} 
	
	protected abstract void initialiseWorld();
	
	abstract protected void onPhysicsUpdate(double dt);
	
	abstract protected void onRenderUpdate();
	
	public void run() {
		
	}
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.library.path"));
		
		UserMain main = new UserMain();
//		
//		Vect3d v = new Vect3d(1,0,0);
//		Vect3d n = new Vect3d(0,1,0);
//		double angle = Math.PI/2;
//		
//		Quaternion q = Quaternion.getRotation(new Quaternion(), angle, n);
//		q.normalise();
//		Quaternion.mult(q,q,q);
//		q.normalise();
//		Vect3d.rotate(v, v, q);
	}

}
