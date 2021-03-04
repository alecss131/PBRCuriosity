package ru.test.pbr;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static org.lwjgl.opengl.GL46.*;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;

public class PreRender {
	private PipeLine equirectangularToCubemapPipeline;
	private PipeLine irradiancePipeline;
	private PipeLine prefilterPipeline;
	private PipeLine brdfPipeline;
	private SeparateShaderProgram vertex0;
	private SeparateShaderProgram vertex1;
	private SeparateShaderProgram geometry0;
	private SeparateShaderProgram fragment0;
	private SeparateShaderProgram fragment1;
	private SeparateShaderProgram fragment2;
	private SeparateShaderProgram fragment3;
	private Background bg;
	private CubeMap hdrc;
	private CubeMap hdrir;
	private CubeMap hdrpf;
	private HdrTexture brdf;
	private int w = 512;
	private int h = 512;
	private int s = 16;
	private int sp = 4;
	private int maxMipLevels = 5;
	private Matrix4f proj;

	public PreRender() {
		bg = new Background();
		proj = new Matrix4f();
		createShaders();
		hdrc = new CubeMap(w, h);
		hdrir = new CubeMap(w / s, h / s);
		hdrpf = new CubeMap(w / sp, h / sp, maxMipLevels);
		brdf = new HdrTexture(w, h);
		render();
	}
	
	public void render() {
		UniformBuffer ub = new UniformBuffer(1, 384);
		Matrix4f views[] = {
				new Matrix4f().lookAt(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f),
				new Matrix4f().lookAt(0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f),
				new Matrix4f().lookAt(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f),
				new Matrix4f().lookAt(0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f),
				new Matrix4f().lookAt(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f),
				new Matrix4f().lookAt(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, -1.0f, 0.0f)};
		for (Matrix4f m : views) {
			ub.addData(m);
		}
		proj.perspective((float) Math.toRadians(90.0f), 1.0f, 0.1f, 10.0f);
		step0();
		step1();
		step2();
		ub.cleanUp();
		bg.cleanUp();
		step3();
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	private void step0() {
		HdrTexture hdr = new HdrTexture("/assets/hdr/MonValley_A_LookoutPoint_2k.hdr");
		FrameBuffer hdrfb = new FrameBuffer(hdrc.getId());
		glViewport(0, 0, w, h);
		hdrfb.bindBuffer();
		glClear(GL_COLOR_BUFFER_BIT);
		equirectangularToCubemapPipeline.bind();
		hdr.bind(0);
		geometry0.setUniform(0, proj);
		bg.render();
		hdr.unbind(0);
		equirectangularToCubemapPipeline.unbind();
		hdrfb.cleanUp();
		hdr.cleanUp();
	}
	
	private void step1() {
		FrameBuffer hdrfb = new FrameBuffer(hdrir.getId());
		glViewport(0, 0, w / s, h / s);
		hdrfb.bindBuffer();
		glClear(GL_COLOR_BUFFER_BIT);
		irradiancePipeline.bind();
		hdrc.bind(0);
		geometry0.setUniform(0, proj);
		bg.render();
		hdrc.unbind(0);
		irradiancePipeline.unbind();
		hdrfb.cleanUp();
	}
	
	private void step2() {
		hdrpf.generateMipMaps();
		FrameBuffer hdrfb = new FrameBuffer();
		int size = w / sp;
		for (int mip = 0; mip < maxMipLevels; ++mip) {
			int mipWidth = (int) (size * Math.pow(0.5, mip));
	        int mipHeight = (int) (size * Math.pow(0.5, mip));
	        hdrfb.bindTexture(hdrpf.getId(), mip);
	        hdrfb.bindBuffer();
			glViewport(0, 0, mipWidth, mipHeight);
	        float roughness = (float) mip / (float) (maxMipLevels - 1);
			glClear(GL_COLOR_BUFFER_BIT);
			prefilterPipeline.bind();
			hdrc.bind(0);
			geometry0.setUniform(0, proj);
			fragment2.setUniform(0, roughness);
			bg.render();
			hdrc.unbind(0);
			prefilterPipeline.unbind();
		}
		hdrfb.cleanUp();
	}
	
	private void step3() {
		float v[] = { -1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f };
		float t[] = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f };
		Mesh quad = new Mesh(v, t);
		FrameBuffer hdrfb = new FrameBuffer(brdf.getId());
		hdrfb.bindBuffer();
		glViewport(0, 0, w, h);
		glClear(GL_COLOR_BUFFER_BIT);
		brdfPipeline.bind();
		quad.renderStrip();
		brdfPipeline.unbind();
		quad.cleanUp();
		hdrfb.cleanUp();
	}

	public void bind(int u0, int u1, int u2) {
		hdrir.bind(u0);
		hdrpf.bind(u1);
		brdf.bind(u2);
	}

	public void unbind(int u0, int u1, int u2) {
		hdrir.unbind(u0);
		hdrpf.unbind(u1);
		brdf.unbind(u2);
	}

	public void bindBG(int unit) {
		hdrc.bind(unit);
	}

	public void unbindBG(int unit) {
		hdrc.unbind(unit);
	}
	
	/*public void save() {
		int id = hdrc.getId();
		for (int i = 0; i < 6; i++) {
			FloatBuffer ib = memAllocFloat(w * h * 3);
			glGetTextureSubImage(id, 0, 0, 0, i, w, h, 1, GL_RGB, GL_FLOAT, ib);
			stbi_write_hdr("cubemap" + i + ".hdr", w, h, 3, ib);
			memFree(ib);
		}
	}*/

	private void createShaders() {
		vertex0 = new SeparateShaderProgram();
		vertex1 = new SeparateShaderProgram();
		geometry0 = new SeparateShaderProgram();
		fragment0 = new SeparateShaderProgram();
		fragment1 = new SeparateShaderProgram();
		fragment2 = new SeparateShaderProgram();
		fragment3 = new SeparateShaderProgram();
		try {
			vertex0.createVertexShader(
					IOUtils.resourceToString("/assets/shaders/cubemap.vert", StandardCharsets.UTF_8));
			vertex1.createVertexShader(
					IOUtils.resourceToString("/assets/shaders/brdf.vert", StandardCharsets.UTF_8));
			geometry0.createGeometryShader(
					IOUtils.resourceToString("/assets/shaders/cubemap.geom", StandardCharsets.UTF_8));
			fragment0.createFragmentShader(IOUtils
					.resourceToString("/assets/shaders/equirectangular_to_cubemap.frag", StandardCharsets.UTF_8));
			fragment1.createFragmentShader(IOUtils.resourceToString("/assets/shaders/irradiance_convolution.frag",
					StandardCharsets.UTF_8));
			fragment2.createFragmentShader(
					IOUtils.resourceToString("/assets/shaders/prefilter.frag", StandardCharsets.UTF_8));
			fragment3.createFragmentShader(
					IOUtils.resourceToString("/assets/shaders/brdf.frag", StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
		equirectangularToCubemapPipeline = new PipeLine();
		irradiancePipeline = new PipeLine();
		prefilterPipeline = new PipeLine();
		brdfPipeline = new PipeLine();
		equirectangularToCubemapPipeline.setVertexStage(vertex0.getId());
		irradiancePipeline.setVertexStage(vertex0.getId());
		prefilterPipeline.setVertexStage(vertex0.getId());
		brdfPipeline.setVertexStage(vertex1.getId());
		equirectangularToCubemapPipeline.setGeometryStage(geometry0.getId());
		irradiancePipeline.setGeometryStage(geometry0.getId());
		prefilterPipeline.setGeometryStage(geometry0.getId());
		equirectangularToCubemapPipeline.setFragmentStage(fragment0.getId());
		irradiancePipeline.setFragmentStage(fragment1.getId());
		prefilterPipeline.setFragmentStage(fragment2.getId());
		brdfPipeline.setFragmentStage(fragment3.getId());
		equirectangularToCubemapPipeline.validate();
		irradiancePipeline.validate();
		prefilterPipeline.validate();
		brdfPipeline.validate();
	}

	public void cleanUp() {
		vertex0.cleanUp();
		vertex1.cleanUp();
		geometry0.cleanUp();
		fragment0.cleanUp();
		fragment1.cleanUp();
		fragment2.cleanUp();
		fragment3.cleanUp();
		equirectangularToCubemapPipeline.cleanUp();
		irradiancePipeline.cleanUp();
		prefilterPipeline.cleanUp();
		brdfPipeline.cleanUp();
		hdrc.cleanUp();
		hdrir.cleanUp();
		hdrpf.cleanUp();
		brdf.cleanUp();
	}
}