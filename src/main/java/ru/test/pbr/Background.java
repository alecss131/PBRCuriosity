package ru.test.pbr;

public class Background {
	private Mesh mesh;

	public Background() {
		GenGeom gg = new GenGeom();
		mesh = new Mesh(gg.getVertex(), gg.getIndixes());
	}
	
	public void render() {
		mesh.render();
	}

	public void cleanUp() {
		mesh.cleanUp();
	}
}