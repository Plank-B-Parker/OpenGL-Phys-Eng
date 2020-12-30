package Math;

public class Matd {
	private double elements[][];
	
	public Matd(int c, int r) {
		elements = new double[c][r];
		
		if(c == r) {
			for(int i = 0; i < r; i++) {
				elements[i][i] = 1;
			}
		}
	}
	
	public void transpose() {
		int colomns = elements[0].length;
		int rows = elements.length;
		
		double[][] newElements = new double[colomns][rows];
		for(int c = 0; c < colomns; c++) {
			for(int r = 0; r < rows; r++) {
				newElements[c][r] = elements[r][c];
			}
		}
		elements = newElements;
	}
}
