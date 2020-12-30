package Math;

public class Vect3i {
	public int x = 0,y = 0,z = 0;
	
	public Vect3i() {}
	public Vect3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public boolean equals(Vect3i i) {
		if(x == i.x && y == i.y && z == i.z) 
			return true;
		else
			return false;
	}
}
