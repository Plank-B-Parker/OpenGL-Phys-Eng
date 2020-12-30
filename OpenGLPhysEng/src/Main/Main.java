package Main;

import org.lwjgl.opengl.Display;

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

public class Main implements Runnable{
	
	private boolean paused;
	private boolean running;
	private boolean debugMode = true;
	
	private ObjectHandler objects;
	private PhysicsHandler physics = new PhysicsHandler();
	private Renderer renderer;
	
	private double timeStep;
	private double FPS;
	private double UPS;
	private double speed = 1;
	
	private Thread loop;
	
	public void startEngine() {
		loop = new Thread();
		start();
	}
	
	//Main loop.
	public void run() {
		
		DisplayManager.loadDisplay(900, 900, 60);
		initialiseWorld();
		
		double LastTime = System.nanoTime();
		double AmountOfTicks = 60;
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
		while(!Display.isCloseRequested()) {
			long now = System.nanoTime();
			delta += (now - LastTime)/ns;
			LastTime = now;
			
			//find out if time that has passed is longer than or equal to time between frames.
			while(AmountOfFrames*delta/AmountOfTicks >= 1) {
				
				render();
				frames++;
				
				//find out if time passed is longer than time between updates
				while(delta >= 1) {
					t2 = System.nanoTime();
					//Update
					timeStep = (t2-t1)/1000000000;
					if(!paused) {
						if(debugMode) {
							physics.update(speed/AmountOfTicks);
							time += 1/AmountOfTicks;
							//renderer.getCamera().lookAt(box.pos);
						}
						else
							physics.update(timeStep);
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
	
	//Starting thread.
	public synchronized void start() {
		loop = new Thread(this);
		loop.start();
		running = true;
		
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
		
	public void render() {
		renderer.prepare();
		renderer.renderScene();
		DisplayManager.updateDisplay();
	}
	
	Box box;
	public void initialiseWorld() {
		objects = new ObjectHandler();
		renderer = new Renderer();
		
		//Testing//////////
		//box = new Box(0.75,2,0.25);
		box = new Box(1,1,1);
		//box.rotate(Vect3d.O, Vect3d.i, -Math.PI/2);
		box.phys.rotation.setAngVel(0, 20, 0);
		box.pos.y = 0;
		objects.addObject(box);
		renderer.addToScene(box);
		physics.add(box.phys);
		
		lightSource l = new lightSource(new Vect3f(200,200,200), 0);
		l.pos.set(0,10,3);
		renderer.addToScene(l);
		
		for(int i = 1; i <= 0; i++) {
			double theta = 2*Math.PI*Math.random();
			double theta2 = 2*Math.PI*Math.random();
			double cos = Math.cos(theta);
			double cos2 = Math.cos(theta2);
			double sin = Math.sin(theta);
			double sin2 = Math.sin(theta2);
			lightSource L = new lightSource(new Vect3d(3*cos*cos2, 3*sin2, 3*sin*cos2) , new Vect3f(10,10,10), 0);
			renderer.addToScene(L);
		}
		renderer.getCamera().pos.z = 7;
		//renderer.getCamera().setFOV((float)(Math.PI/10));
		///////////////////
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.startEngine();
		
		Vect3d v = new Vect3d(1,0,0);
		Vect3d n = new Vect3d(0,1,0);
		double angle = Math.PI/2;
		
		Quaternion q = Quaternion.getRotation(new Quaternion(), angle, n);
		q.normalise();
		Quaternion.mult(q,q,q);
		q.normalise();
		Vect3d.rotate(v, v, q);
	}

}
