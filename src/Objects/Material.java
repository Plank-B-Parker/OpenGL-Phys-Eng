package Objects;

public class Material {
	public String albedoPath = "checkerboard-pattern.png";
	public int albedo = Loader.loadTexture("checkerboard-pattern.png");
	public int normal = Loader.loadTexture("flat.png");
	public int height = Loader.loadTexture("white.png");
	public int roughness = Loader.loadTexture("white.png");
	public int metalic = Loader.loadTexture("black.png");
	public int AO = Loader.loadTexture("white.png");
	private int restituion;
	private int friction;
	private int stiffness;
	
	public void setTexture(String fileName) {
		albedo = Loader.loadTexture(fileName);
		albedoPath = fileName;
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
