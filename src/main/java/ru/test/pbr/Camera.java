package ru.test.pbr;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private final Matrix4f viewMatrix;
    private float r;
    private float scale = 1.0f;
    private float y0 = 0.0f;
    private Vector3f pos;
    private Vector3f rot;
    
    public Camera(float r) {
        viewMatrix = new Matrix4f();
		pos = new Vector3f();
		rot = new Vector3f();
    	this.r = r;
    }
    
    public void setScale(float scale) {
    	this.scale = scale;
    }
    
    public void move(float y) {
    	y0 += y;
    }
    
	public void setPosition(float x, float y) {
		float t = (float) Math.toRadians(x);
		float p = (float) Math.toRadians(y);
		rot.set(t, -p, 0.0f);
		pos.z = (float) (r * Math.cos(t) * Math.cos(p));
		pos.x = (float) (r * Math.cos(t) * Math.sin(p));
		pos.y = y0 + (float) (r * Math.sin(t));
	}
    
    public Matrix4f getViewMatrix() {
    	viewMatrix.identity().scale(scale).rotateXYZ(rot).translateLocal(0, y0, -r);
    	return viewMatrix;
    }
    
    public Matrix4f getViewMatrixSky() {
    	viewMatrix.identity().rotateXYZ(rot);
    	return viewMatrix;
    }
    
    public Vector3f getViewPos() {
    	return pos;
    }
}