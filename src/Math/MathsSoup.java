package Math;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class MathsSoup{
	
	//Keep track of all soups in case any are not being used properly.s
	String className;
	static LinkedList<MathsSoup> allSoups = new LinkedList<>();
	
	private ArrayList<Vect3d> vects = new ArrayList<>();
	private int vP = 0;//Pointer to first inactive vector.
	private Stack<Integer> vectsToClear = new Stack<Integer>();
	
	private ArrayList<Mat3d> mats = new ArrayList<>();
	private int mP = 0;//Pointer to first inactive matrix.
	private Stack<Integer> matsToClear = new Stack<Integer>();
	
	private ArrayList<Quaternion> quats = new ArrayList<>();
	private int qP = 0;//Pointer to first inactive quaternion.
	private Stack<Integer> quatsToClear = new Stack<Integer>();
	
	public MathsSoup(String className) {
		this.className = className;
		allSoups.add(this);
	}
	
	public void startOfMethod() {
		vectsToClear.push(0);
		matsToClear.push(0);
		quatsToClear.push(0);
	}
	
	public void endOfMethod() {
		cleanVects();
		cleanMats();
		cleanQuats();
	}
	
	////////////////////////////////////////////

	public Vect3d getVect() {
		Vect3d vect = null;
		
		if(vects.size() == vP) 
			vects.add(vect = new Vect3d());
		else 
			vect = vects.get(vP);
		vP++;
		
		int numVectsToClear = vectsToClear.pop() + 1;
		vectsToClear.push(numVectsToClear);
		
		return vect;
	}
	
	/**
	 * Clear the last num vects.
	 * @param num
	 */
	private void clearVects(int num) {
		if(vP > 0)
			vP -= num;
	}
	
	private void cleanVects() {
		clearVects(vectsToClear.pop());
	}
	
	/////////////////////////////////////////////
	
	public Mat3d getMat() {
		Mat3d mat = null;
		
		if(mats.size() == mP) 
			mats.add(mat = new Mat3d());
		else 
			mat = mats.get(mP);
		mP++;
		
		int numMatsToClear = matsToClear.pop() + 1;
		matsToClear.push(numMatsToClear);
		
		return mat;
	}
	
	/**
	 * Clear the last num mats.
	 * @param num
	 */
	private void clearMats(int num) {
		if(mP > 0)
			mP -= num;
	}
	
	private void cleanMats() {
		clearMats(matsToClear.pop());
	}
	
	/////////////////////////////////////////////
	
	public Quaternion getQuaternion() {
		Quaternion quat = null;
		
		if(quats.size() == qP) 
			quats.add(quat = new Quaternion());
		else 
			quat = quats.get(qP);
		qP++;
		
		int numQuatsToClear = quatsToClear.pop() + 1;
		quatsToClear.push(numQuatsToClear);
		
		return quat;
	}
	
	/**
	 * Clear the last num quats.
	 * @param num
	 */
	private void clearQuats(int num) {
		if(qP > 0)
			qP -= num;
	}
	
	private void cleanQuats() {
		clearQuats(quatsToClear.pop());
	}
	
}
