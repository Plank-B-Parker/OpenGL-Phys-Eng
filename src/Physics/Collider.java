package Physics;

import java.util.ArrayList;
import java.util.List;

import Math.MathsSoup;
import Math.Vect3d;

import Objects.Object;

class CollisionPair{
	
	boolean confirmed;
	Object A;
	Object B;
	Vect3d mindisp;
	
	int nextPair;
	int lastPair;
	
	public CollisionPair(Object A, Object B, Vect3d mindisp, boolean confirmed) {	
		this.A = A;
		this.B = B;
		this.mindisp = mindisp;
		this.confirmed = confirmed;	
	}
}

//BroadPhase

class TreeNode{
	//Utilities
	MathsSoup vectPool = new MathsSoup("TreeNode");
	
	//Global Tree
	static TreeNode bigDaddy; //Oldest node.
	static List<TreeNode> youngestNodes = new ArrayList<>();
	static int maxGenerations;
	
	//Local Node
	List<Object> objs;
	
	//Each node is a cube with its location at the back top left corner.
	Vect3d location;
	double sideLength;
	
	//Each node split into 8 sub nodes;
	TreeNode[] subnodes = new TreeNode[8];
	int generation;
	
	TreeNode(Vect3d pos, double sideLength, int generation, List<Object> objs) {
		location = pos;
		this.sideLength = sideLength;
		this.generation = generation;
		
		if(generation == 1) bigDaddy = this;
		
		filterObjs(objs);
		
		if(generation <= maxGenerations || this.objs.size() > 2) 
			split();
		else 
			youngestNodes.add(this);
	}
	
	private void filterObjs(List<Object> objs) {
		vectPool.startOfMethod();
		/////////////////////////
		
		Vect3d diff = vectPool.getVect();
		for(Object obj: objs) {
			diff = Vect3d.sub(diff, obj.pos, location);
			
			if((diff.x >= 0 && diff.x < sideLength) &&
			   (diff.y >= 0 && diff.y < sideLength) &&
			   (diff.z >= 0 && diff.z < sideLength))
				this.objs.add(obj);
			
		}
	
		///////////////////////
		vectPool.endOfMethod();
	}
	
	void split() {
		
		double newLength = sideLength/2;
		//Simplify list below using binary numbers up to 7, each bit signifying which coordinate gets added (in a for loop).
		subnodes[0] = new TreeNode(new Vect3d(location.x, location.y, location.z), newLength, generation + 1, objs);
		subnodes[1] = new TreeNode(new Vect3d(location.x + newLength, location.y, location.z), newLength, generation + 1, objs);
		subnodes[2] = new TreeNode(new Vect3d(location.x, location.y + newLength, location.z), newLength, generation + 1, objs);
		subnodes[3] = new TreeNode(new Vect3d(location.x, location.y, location.z + newLength), newLength, generation + 1, objs);
		subnodes[4] = new TreeNode(new Vect3d(location.x + newLength, location.y + newLength, location.z), newLength, generation + 1, objs);
		subnodes[5] = new TreeNode(new Vect3d(location.x + newLength, location.y, location.z + newLength), newLength, generation + 1, objs);
		subnodes[6] = new TreeNode(new Vect3d(location.x, location.y + newLength, location.z + newLength), newLength, generation + 1, objs);
		subnodes[7] = new TreeNode(new Vect3d(location.x + newLength, location.y + newLength, location.z + newLength), newLength, generation + 1, objs);
		
	}
	
}


public class Collider {
	
	//Contains both confirmed and unconfirmed collisions.
	List<CollisionPair> collisionPairs;
	
	
	public void broadPhase(List<Object> objs) {
		double smallestX = Double.POSITIVE_INFINITY, biggestX = Double.NEGATIVE_INFINITY;
		double smallestZ = Double.POSITIVE_INFINITY, biggestZ = Double.NEGATIVE_INFINITY;
		double smallestY = Double.POSITIVE_INFINITY, biggestY = Double.NEGATIVE_INFINITY;
		
		for(Object obj: objs) {
			smallestX = Math.min(smallestX, obj.pos.x);
			smallestY = Math.min(smallestY, obj.pos.y);
			smallestZ = Math.min(smallestZ, obj.pos.z);
			
			biggestX = Math.max(biggestX, obj.pos.x);
			biggestY = Math.max(biggestY, obj.pos.y);
			biggestZ = Math.max(biggestZ, obj.pos.z);
		}
		
		
	}
	
	public void narrowPhase() {
		
	}
	
	public void resolveCollisions() {
		
	}
	
	private void addCollision(CollisionPair collision) {
		
	}

}
