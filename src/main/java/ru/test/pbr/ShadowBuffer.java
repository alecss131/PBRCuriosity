package ru.test.pbr;

import static org.lwjgl.opengl.GL45.*;

public class ShadowBuffer {
	private int fb;
	private int tex;
	private int w;
	private int h;

	public ShadowBuffer(int width, int height) {
		w = width;
		h = height;
		fb = glCreateFramebuffers();
		tex = glCreateTextures(GL_TEXTURE_CUBE_MAP);
		glTextureStorage2D(tex, 1, GL_DEPTH_COMPONENT32F, w, h);
		glTextureParameteri(tex, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTextureParameteri(tex, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTextureParameteri(tex, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTextureParameteri(tex, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTextureParameteri(tex, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
		glNamedFramebufferTexture(fb, GL_DEPTH_ATTACHMENT, tex, 0);
		glNamedFramebufferDrawBuffer(fb, GL_NONE);
		glNamedFramebufferReadBuffer(fb, GL_NONE);
		if (glCheckNamedFramebufferStatus(fb, GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("ERROR[FRAMEBUFFER] Framebuffer is not complete!");
		}
	}
	
	public void bindBuffer() {
		glViewport(0, 0, w, h); //texture size
		glBindFramebuffer(GL_FRAMEBUFFER, fb);
	}

	public void unbindBuffer(int sw, int sh) {
		glViewport(0, 0, sw, sh); //screen size
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void bindTexture(int unit) {
		glBindTextureUnit(unit, tex);
	}

	public void unbindTexture(int unit) {
		glBindTextureUnit(unit, 0);
	}

	public float getAspectRatio(){
		return (float)w / (float)h;
	}
	
	public void cleanUp() {
		glDeleteTextures(tex);
		glDeleteFramebuffers(fb);
	}
}