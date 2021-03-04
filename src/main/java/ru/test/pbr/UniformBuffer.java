package ru.test.pbr;

import static org.lwjgl.opengl.GL46.*;

import org.joml.Matrix4f;

public class UniformBuffer {
	private int ubo;
	private int offset = 0;
	
	UniformBuffer(int binding, int size) {
		ubo = glCreateBuffers();
		glNamedBufferStorage(ubo, size, GL_DYNAMIC_STORAGE_BIT);
		glBindBufferBase(GL_UNIFORM_BUFFER, binding, ubo);
	}
	
	public void addData(int offset, float data[]) {
		glNamedBufferSubData(ubo, offset, data);
	}
	
	public void addData(int offset, int data) {
		glNamedBufferSubData(ubo, offset, new int[] {data});
	}
	
	public void addData(float data[]) {
		glNamedBufferSubData(ubo, offset, data);
		offset += data.length * 4;
	}
	
	public void addData(Matrix4f data) {
		glNamedBufferSubData(ubo, offset, data.get(new float[16]));
		offset += 64;
	}
	
	public void addData(int data) {
		glNamedBufferSubData(ubo, offset, new int[] {data});
		offset += 4;
	}
	
	public void cleanUp() {
		glDeleteBuffers(ubo);
	}
}