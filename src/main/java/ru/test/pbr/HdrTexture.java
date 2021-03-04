package ru.test.pbr;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL45.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.apache.commons.io.IOUtils;
import org.lwjgl.system.MemoryStack;

public class HdrTexture {
	private int width;
	private int height;
	private int id;
	
	public HdrTexture(String name) {
		try (MemoryStack stack = stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			byte[] data = IOUtils.resourceToByteArray(name);
			ByteBuffer buf = memAlloc(data.length);
			buf.put(data).flip();
			stbi_set_flip_vertically_on_load(true);
			FloatBuffer texture = stbi_loadf_from_memory(buf, w, h, c, STBI_rgb);
			stbi_set_flip_vertically_on_load(false);
			width = w.get(0);
			height = h.get(0);
			memFree(buf);
			id = glCreateTextures(GL_TEXTURE_2D);
			glTextureStorage2D(id, 1, GL_RGB32F, width, height);
			glTextureSubImage2D(id, 0, 0, 0, width, height, GL_RGB, GL_FLOAT, texture);
			glTextureParameteri(id, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTextureParameteri(id, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glTextureParameteri(id, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTextureParameteri(id, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			stbi_image_free(texture);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public HdrTexture(int width, int height) {
		this.width = width;
		this.height = height;
		id = glCreateTextures(GL_TEXTURE_2D);
		glTextureStorage2D(id, 1, GL_RG32F, width, height);
		glTextureParameteri(id, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTextureParameteri(id, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTextureParameteri(id, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTextureParameteri(id, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	}
	
    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
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