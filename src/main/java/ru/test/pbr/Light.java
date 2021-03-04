package ru.test.pbr;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Light {
	private Matrix4f model;
	private Vector3f color;
	private Vector3f pos;
	private Vector3f col;
	private Mesh mesh;
	
	public Light() {
		pos = new Vector3f();
		color = new Vector3f();
		col = new Vector3f();
		model = new Matrix4f();
		model.identity().translate(pos);
		mesh = new Mesh(GeometryData.getCubeVertices());
	}
	
	public void setPosition(float x, float y, float z) {
		pos.set(x, y, z);
	}
	
	public void setColor(float r, float g, float b) {
		color.set(r, g, b);
	}
	
	public void setCol(float r, float g, float b) {
		col.set(r, g, b);
	}
	
	public Matrix4f getModel() {
		return model;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public Vector3f getCol() {
		return col;
	}
	
	public void render() {
		mesh.render();
	}
	
	public void cleanUp() {
		mesh.cleanUp();
	}

	public void setScale(float scale) {
		model.identity().translate(pos).scale(scale);
	}
	
	public Vector3f getPosition() {
		return pos;
	}
	
	public float[] getData() {
		return new float[] {pos.x, pos.y, pos.z, 0.0f, color.x, color.y, color.z, 0.0f};
	}
}