package ru.test.pbr;

import static org.lwjgl.opengl.GL45.*;

public class GBuffer {
	private int w;
	private int h;
	private int fb;
	private int albedo;
	private int normal;
	private int positions;
	private int rb;
	
	public GBuffer(int width, int height) {
		w = width;
		h = height;
		fb = glCreateFramebuffers();
		albedo = glCreateTextures(GL_TEXTURE_2D);
		glTextureStorage2D(albedo, 1, GL_RGBA32F, w, h);
		glTextureParameteri(albedo, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTextureParameteri(albedo, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTextureParameteri(albedo, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTextureParameteri(albedo, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glNamedFramebufferTexture(fb, GL_COLOR_ATTACHMENT0, albedo, 0);
		normal = glCreateTextures(GL_TEXTURE_2D);
		glTextureStorage2D(normal, 1, GL_RGBA16F, w, h);
		glTextureParameteri(normal, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTextureParameteri(normal, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTextureParameteri(normal, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTextureParameteri(normal, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glNamedFramebufferTexture(fb, GL_COLOR_ATTACHMENT1, normal, 0);
		positions = glCreateTextures(GL_TEXTURE_2D);
		glTextureStorage2D(positions, 1, GL_RGBA32F, w, h);
		glTextureParameteri(positions, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTextureParameteri(positions, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTextureParameteri(positions, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTextureParameteri(positions, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glNamedFramebufferTexture(fb, GL_COLOR_ATTACHMENT2, positions, 0);
		int attachments[] = { GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2};
		glNamedFramebufferDrawBuffers(fb, attachments);
		rb = glCreateRenderbuffers();
		glNamedRenderbufferStorage(rb, GL_DEPTH_COMPONENT, w, h);
		glNamedFramebufferRenderbuffer(fb, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rb);
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
	
	public void bindTexture(int a, int b, int c) {
		glBindTextureUnit(a, albedo);
		glBindTextureUnit(b, normal);
		glBindTextureUnit(c, positions);
    }
    
    public void unbindTexture(int a, int b, int c) {
    	glBindTextureUnit(a, 0);
    	glBindTextureUnit(b, 0);
    	glBindTextureUnit(c, 0);
    }
    
    public int getId() {
    	return fb;
    }
    
    public void copyZBuffer() {
    	glBindFramebuffer(GL_READ_FRAMEBUFFER, fb);
    	glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    	glBlitFramebuffer(0, 0, w, h, 0, 0, w, h, GL_DEPTH_BUFFER_BIT, GL_NEAREST);
    	glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
	
	public void cleanUp() {
		glDeleteTextures(albedo);
		glDeleteTextures(normal);
		glDeleteTextures(positions);
		glDeleteRenderbuffers(rb);
		glDeleteFramebuffers(fb);
	}
}