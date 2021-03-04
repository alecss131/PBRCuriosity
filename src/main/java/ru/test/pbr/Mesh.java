package ru.test.pbr;

import static org.lwjgl.opengl.GL45.*;
import java.nio.ByteBuffer;
import static org.lwjgl.system.MemoryUtil.*;

public class Mesh {
	private int vao;
	private int vbo;
	private int len;
	private final static long SIZE = 4;
	private boolean ind = true;
	
	public Mesh(float vertex[]) {
		len = vertex.length / 3;
		ind = false;
		vbo = glCreateBuffers();
		glNamedBufferStorage(vbo, vertex, GL_DYNAMIC_STORAGE_BIT);
		vao = glCreateVertexArrays();
		glEnableVertexArrayAttrib(vao, 0);
		glVertexArrayAttribFormat(vao, 0, 3, GL_FLOAT, false, 0);
		glVertexArrayVertexBuffer(vao, 0, vbo, 0, 3 * (int) SIZE);
		glVertexArrayAttribBinding(vao, 0, 0);
	}
	
	public Mesh(float vertex[], float tex[]) {
		len = vertex.length / 3;
		ind = false;
		vbo = glCreateBuffers();
		glNamedBufferStorage(vbo, (vertex.length + tex.length) * SIZE, GL_DYNAMIC_STORAGE_BIT);
		glNamedBufferSubData(vbo, 0, vertex);
		glNamedBufferSubData(vbo, vertex.length * SIZE, tex);
		vao = glCreateVertexArrays();
		glEnableVertexArrayAttrib(vao, 0);
		glEnableVertexArrayAttrib(vao, 1);
		glVertexArrayAttribFormat(vao, 0, 3, GL_FLOAT, false, 0);
		glVertexArrayAttribFormat(vao, 1, 2, GL_FLOAT, false, 0);
		glVertexArrayVertexBuffer(vao, 0, vbo, 0, 3 * (int) SIZE);
		glVertexArrayVertexBuffer(vao, 1, vbo, vertex.length * SIZE, 2 * (int) SIZE);
		glVertexArrayAttribBinding(vao, 0, 0);
		glVertexArrayAttribBinding(vao, 1, 1);
	}
	
	public Mesh(float vertex[], short indices[]) {
		len = indices.length;
		vbo = glCreateBuffers();
		glNamedBufferStorage(vbo, (indices.length / 2 + vertex.length) * SIZE, GL_DYNAMIC_STORAGE_BIT);
		glNamedBufferSubData(vbo, 0, indices);
		glNamedBufferSubData(vbo, indices.length * SIZE / 2, vertex);
		vao = glCreateVertexArrays();
		glEnableVertexArrayAttrib(vao, 0);
		glVertexArrayAttribFormat(vao, 0, 3, GL_FLOAT, false, 0);
		glVertexArrayVertexBuffer(vao, 0, vbo, indices.length * SIZE / 2, 3 * (int) SIZE);
		glVertexArrayAttribBinding(vao, 0, 0);
		glVertexArrayElementBuffer(vao, vbo);
	}
	
	public Mesh(byte vertex[], byte texcolor[], byte normals[], byte indices[], int len) {
		this.len = len;
		int tcSize =  texcolor.length / (vertex.length / 3);
		vbo = glCreateBuffers();
		glNamedBufferStorage(vbo, indices.length + vertex.length + texcolor.length + normals.length,
				GL_DYNAMIC_STORAGE_BIT);
		ByteBuffer bb = memAlloc(indices.length);
		bb.put(indices).flip();
		glNamedBufferSubData(vbo, 0, bb);
		memFree(bb);
		bb = memAlloc(vertex.length);
		bb.put(vertex).flip();
		glNamedBufferSubData(vbo, indices.length, bb);
		memFree(bb);
		bb = memAlloc(texcolor.length);
		bb.put(texcolor).flip();
		glNamedBufferSubData(vbo, indices.length + vertex.length, bb);
		memFree(bb);
		bb = memAlloc(normals.length);
		bb.put(normals).flip();
		glNamedBufferSubData(vbo, indices.length + vertex.length + texcolor.length, bb);
		memFree(bb);
		vao = glCreateVertexArrays();
		glEnableVertexArrayAttrib(vao, 0);
		glEnableVertexArrayAttrib(vao, 1);
		glEnableVertexArrayAttrib(vao, 2);
		glVertexArrayAttribFormat(vao, 0, 3, GL_FLOAT, false, 0);
		glVertexArrayAttribFormat(vao, 1, tcSize, GL_FLOAT, false, 0);
		glVertexArrayAttribFormat(vao, 2, 3, GL_FLOAT, false, 0);
		glVertexArrayVertexBuffer(vao, 0, vbo, indices.length, 3 * (int) SIZE);
		glVertexArrayVertexBuffer(vao, 1, vbo, indices.length + vertex.length, tcSize * (int) SIZE);
		glVertexArrayVertexBuffer(vao, 2, vbo, indices.length + vertex.length + texcolor.length, 3 * (int) SIZE);
		glVertexArrayAttribBinding(vao, 0, 0);
		glVertexArrayAttribBinding(vao, 1, 1);
		glVertexArrayAttribBinding(vao, 2, 2);
		glVertexArrayElementBuffer(vao, vbo);
	}
	
	public void render() {
		glBindVertexArray(vao);
		if (ind) {
			glDrawElements(GL_TRIANGLES, len, GL_UNSIGNED_SHORT, 0);
		} else {
			glDrawArrays(GL_TRIANGLES, 0, len);
		}
		glBindVertexArray(0);
	}
	
	public void renderStrip() {
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, len);
		glBindVertexArray(0);
	}
	
	public void cleanUp() {
		glDeleteBuffers(vbo);
		glDeleteVertexArrays(vao);
	}
}