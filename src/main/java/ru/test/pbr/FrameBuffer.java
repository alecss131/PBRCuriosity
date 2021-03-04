package ru.test.pbr;

import static org.lwjgl.opengl.GL45.*;

public class FrameBuffer {
	private int fb;
	
	FrameBuffer(int tex) {
		fb = glCreateFramebuffers();
		glNamedFramebufferTexture(fb, GL_COLOR_ATTACHMENT0, tex, 0);
		if(glCheckNamedFramebufferStatus(fb, GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("ERROR[FRAMEBUFFER] Framebuffer is not complete!");
		}
	}
	
	FrameBuffer() {
		fb = glCreateFramebuffers();
	}
	
	public void bindTexture(int tex, int mip) {
		glNamedFramebufferTexture(fb, GL_COLOR_ATTACHMENT0, tex, mip);
		if(glCheckNamedFramebufferStatus(fb, GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("ERROR[FRAMEBUFFER] Framebuffer is not complete!");
		}
	}
	
	public void bindBuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, fb);
	}
	
	public void unbindBuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void cleanUp() {
		glDeleteFramebuffers(fb);
	}
}