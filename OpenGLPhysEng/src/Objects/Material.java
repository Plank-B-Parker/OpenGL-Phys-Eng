package Objects;

public class Material {
	public int albedo = Loader.loadTexture("white");
	public int normal = Loader.loadTexture("flat");
	public int height = Loader.loadTexture("white");
	public int roughness = Loader.loadTexture("white");
	public int metalic = Loader.loadTexture("black");
	public int AO = Loader.loadTexture("white");
	private int restituion;
	private int friction;
	private int stiffness;
	
	public void setTexture(String fileName) {
		Loader.loadTexture(fileName);
	}
	public void setNormals(String fileName) {
		Loader.loadTexture(fileName);
	}
	public void setHeightMap(String fileName) {
		Loader.loadTexture(fileName);
	}
	public void setRoughness(String fileName) {
		Loader.loadTexture(fileName);
	}
	public void setMetalness(String fileName) {
		Loader.loadTexture(fileName);
	}
	public void setAmbient(String fileName) {
		Loader.loadTexture(fileName);
	}
}
