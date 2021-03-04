package ru.test.pbr;

public class Item {
	private Mesh mesh;
	private Material material;
	private GeometryLoader gl = new GeometryLoader();
	private int n;
	private int subs[];
	
	public Item(int i) {
		n = i;
		gl.load("/assets/models/Cylinder.015-" + i);
		int n[] = {34416, 23379, 18336, 8811, 26454, 6426, 34287, 156, 1650};
		mesh = new Mesh(gl.getVertices(), gl.getTextures(), gl.getNormals(), gl.getIndices(), n[i]);
	}
	
	public void setMaterial (Material material) {
		this.material = material;
		subs = material.getSubs();
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void render() {
		material.bindTextures();
		mesh.render();
		material.unbindTextures();
	}
	
	public void cleanUp() {
		mesh.cleanUp();
		material.cleanUp();
	}
	
	public int getNumber() {
		return n;
	}
	
	public int[] getSubs() {
		return subs;
	}
}