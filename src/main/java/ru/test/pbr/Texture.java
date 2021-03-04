package ru.test.pbr;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL45.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.apache.commons.io.IOUtils;
import org.lwjgl.system.MemoryStack;

public class Texture {
	private int width;
	private int height;
	private int id;
	private int unit;
	
	public Texture(String name, int unit) {
		try (MemoryStack stack = stackPush()) {
			this.unit = unit;
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			byte[] data = IOUtils.resourceToByteArray(name);
			ByteBuffer buf = memAlloc(data.length);
			buf.put(data).flip();
			ByteBuffer texture = stbi_load_from_memory(buf, w, h, c, STBI_rgb_alpha);
			width = w.get(0);
			height = h.get(0);
			memFree(buf);
			id = glCreateTextures(GL_TEXTURE_2D);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
			glTextureStorage2D(id, 3, GL_RGBA8, width, height);
			glTextureSubImage2D(id, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, texture);
			glGenerateTextureMipmap(id);
			glTextureParameteri(id, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTextureParameteri(id, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glTextureParameteri(id, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
			glTextureParameteri(id, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			stbi_image_free(texture);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
    
    public int getUnit() {
    	return unit;
    }

    public void bind() {
        glBindTextureUnit(unit, id);
    }
    
    public void unbind() {
    	glBindTextureUnit(unit, 0);
    }

	public void cleanUp() {
		glDeleteTextures(id);
	}
}