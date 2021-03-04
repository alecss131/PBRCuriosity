package ru.test.pbr;

import static org.lwjgl.opengl.GL45.*;

public class CubeMap {
	private int id;

	CubeMap(int width, int height) {
		this(width, height, 1);
	}
	
	CubeMap(int width, int height, int mips) {
		id = glCreateTextures(GL_TEXTURE_CUBE_MAP);
		glTextureStorage2D(id, mips, GL_RGB32F, width, height);
		glTextureParameteri(id, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTextureParameteri(id, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTextureParameteri(id, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTextureParameteri(id, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTextureParameteri(id, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
	}
	
	public void generateMipMaps() {
		glTextureParameteri(id, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glGenerateTextureMipmap(id);
	}

	public void bind(int unit) {
		glBindTextureUnit(unit, id);
	}

	public void unbind(int unit) {
		glBindTextureUnit(unit, 0);
	}

	public void cleanUp() {
		glDeleteTextures(id);
	}
	
	public int getId() {
		return id;
	}
}