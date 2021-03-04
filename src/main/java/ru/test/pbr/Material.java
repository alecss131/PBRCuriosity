package ru.test.pbr;

import org.joml.Vector3f;

public class Material {
	private Texture diffuse;
	private Texture normal;
	private float metallic = 0.0f;
	private float roughness = 0.0f;
	private float ao = 1.0f;
	private boolean hasTexture = false;
	private boolean hasNormal = false;
	private Vector3f color = new Vector3f();

	public void setDiffuse(Texture texture) {
		diffuse = texture;
		hasTexture = true;
	}
	
	public void setNormal(Texture texture) {
		normal = texture;
		hasNormal = true;
	}
	
	public void setColor(float r, float g, float b) {
		color = new Vector3f(r, g, b);
		hasTexture = false;
	}
	
	public Vector3f getColor() {
		return color;
	}

	public Texture getDiffuse() {
		return diffuse;
	}

	public void bindTextures() {
		if (hasTexture) {
			diffuse.bind();
		}
		if (hasNormal) {
			normal.bind();
		}
	}

	public void unbindTextures() {
		if (hasTexture) {
			diffuse.unbind();
		}
		if (hasNormal) {
			normal.unbind();
		}
	}

	public void cleanUp() {
		if (hasTexture) {
			diffuse.cleanUp();
		}
		if (hasNormal) {
			normal.cleanUp();
		}
	}
	
	public float[] getData() {
		return new float[] {color.x, color.y, color.z, 0.0f, metallic, roughness, ao, 0.0f};
	}
	
	public int hasTexture() {
		return (hasTexture ? 1 : 0) + (hasNormal ? 2 : 0);
	}

	public float getMetallic() {
		return metallic;
	}

	public float getRoughness() {
		return roughness;
	}

	public void setMetallic(float metallic) {
		this.metallic = metallic;
	}

	public void setRoughness(float roughness) {
		this.roughness = roughness;
	}

	public float getAo() {
		return ao;
	}

	public void setAo(float ao) {
		this.ao = ao;
	}
	
	public int[] getSubs() {
		return new int[]{hasTexture ? 0 : 1, hasNormal ? 2 : 3, 5, 7, 9};
	}
}