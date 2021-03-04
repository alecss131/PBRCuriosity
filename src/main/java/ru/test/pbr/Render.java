package ru.test.pbr;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.stb.STBImageWrite.stbi_flip_vertically_on_write;
import static org.lwjgl.stb.STBImageWrite.stbi_write_png;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public class Render {
	private PipeLine objectPipeline;
	private PipeLine lightPipeline;
	private PipeLine backgroundPipeline;
	private PipeLine gbufPipeline;
	private SeparateShaderProgram vertex0 = new SeparateShaderProgram();
	private SeparateShaderProgram vertex1 = new SeparateShaderProgram();
	private SeparateShaderProgram vertex2 = new SeparateShaderProgram();
	private SeparateShaderProgram vertex3 = new SeparateShaderProgram();
	private SeparateShaderProgram fragment0 = new SeparateShaderProgram();
	private SeparateShaderProgram fragment1 = new SeparateShaderProgram();
	private SeparateShaderProgram fragment2 = new SeparateShaderProgram();
	private SeparateShaderProgram fragment3 = new SeparateShaderProgram();
	private final Matrix4f persp;
	private final float fov = 30.0f;
	private int w, h;
	private PreRender pre;
	
	public Render(Window window) {
		persp = new Matrix4f();
		w = window.getWidth();
		h = window.getHeight();
		persp.setPerspective((float) Math.toRadians(fov), (float) w / (float) h, 0.1f, 100.0f);
		createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		pre = new PreRender();
		try (MemoryStack stack = stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			glfwGetFramebufferSize(window.getId(), width, height);
			glViewport(0, 0, width.get(0), height.get(0));
		}
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);
		/*glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);*/
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);  
        glFrontFace(GL_CCW);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_DEBUG_OUTPUT);
		glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS);
        glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DONT_CARE, (IntBuffer)null, true);
        glDebugMessageControl(GL_DEBUG_SOURCE_API, GL_DEBUG_TYPE_OTHER, GL_DONT_CARE, 131185, false);
        glDebugMessageControl(GL_DEBUG_SOURCE_API, GL_DEBUG_TYPE_OTHER, GL_DONT_CARE, 131204, false);
        glDebugMessageCallback(new DebugCallBack(), 0);
        createShaders();
        
        vertex1.setUniform(0, persp);
        vertex2.setUniform(0, persp);
        vertex3.setUniform(0, persp);
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanUp() {
		pre.cleanUp();
		vertex0.cleanUp();
		vertex1.cleanUp();
		vertex2.cleanUp();
		vertex3.cleanUp();
		fragment0.cleanUp();
		fragment1.cleanUp();
		fragment2.cleanUp();
		fragment3.cleanUp();
		gbufPipeline.cleanUp();
		objectPipeline.cleanUp();
		lightPipeline.cleanUp();
		backgroundPipeline.cleanUp();
	}
	
	public void render(List<Item> items, Camera camera, Matrix4f model) {
		gbufPipeline.bind();
		vertex3.setUniform(1, camera.getViewMatrix());
		vertex3.setUniform(2, model);
		for (Item item : items) {
			fragment3.setUniform(0, item.getNumber());
			fragment3.setSubroutines(item.getSubs());
			item.render();
		}
		gbufPipeline.unbind();
	}
	
	public void render(Mesh mesh, Camera camera, Light light) {
		objectPipeline.bind();
		pre.bind(0, 1, 2);
		fragment0.setUniform(0, camera.getViewPos());
		fragment0.setUniform(1, light.getPosition());
		fragment0.setUniform(2, light.getColor());
		mesh.renderStrip();
		pre.unbind(0, 1, 2);
		objectPipeline.unbind();
	}
	
	public void render(Camera camera, Light light) {
		lightPipeline.bind();
		vertex1.setUniform(1, camera.getViewMatrix());
		vertex1.setUniform(2, light.getModel());
		fragment1.setUniform(0, light.getCol());
		light.render();
		lightPipeline.unbind();
	}
	
	public void render(Camera camera, Background bg) {
		glDepthFunc(GL_LEQUAL);
		backgroundPipeline.bind();
		vertex2.setUniform(1, camera.getViewMatrixSky());
		pre.bindBG(0);
		bg.render();
		pre.unbindBG(0);
		backgroundPipeline.unbind();
		glDepthFunc(GL_LESS);
	}
	
	public void saveImage() {
		ByteBuffer ib = memAlloc(w * h * 4 * 4);
		glReadnPixels(0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, ib);
		stbi_flip_vertically_on_write(true);
		stbi_write_png("screen_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd_HH-mm-ss.S")) + ".png",
				w, h, 4, ib, w*4);
		memFree(ib);
	}
	
	private void createShaders() {
		try {
			vertex0.createVertexShader(IOUtils.resourceToString("/assets/shaders/object.vert", StandardCharsets.UTF_8));
			vertex1.createVertexShader(IOUtils.resourceToString("/assets/shaders/light.vert", StandardCharsets.UTF_8));
			vertex2.createVertexShader(IOUtils.resourceToString("/assets/shaders/background.vert", StandardCharsets.UTF_8));
			vertex3.createVertexShader(IOUtils.resourceToString("/assets/shaders/gbuf.vert", StandardCharsets.UTF_8));
			fragment0.createFragmentShader(IOUtils.resourceToString("/assets/shaders/object.frag", StandardCharsets.UTF_8));
			fragment1.createFragmentShader(IOUtils.resourceToString("/assets/shaders/light.frag", StandardCharsets.UTF_8));
			fragment2.createFragmentShader(IOUtils.resourceToString("/assets/shaders/background.frag", StandardCharsets.UTF_8));
			fragment3.createFragmentShader(IOUtils.resourceToString("/assets/shaders/gbuf.frag", StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
		objectPipeline = new PipeLine();
        objectPipeline.setFragmentStage(fragment0.getId());
        objectPipeline.setVertexStage(vertex0.getId());
        objectPipeline.validate();
        lightPipeline = new PipeLine();
        lightPipeline.setFragmentStage(fragment1.getId());
        lightPipeline.setVertexStage(vertex1.getId());
        lightPipeline.validate();
        backgroundPipeline = new PipeLine();
        backgroundPipeline.setFragmentStage(fragment2.getId());
        backgroundPipeline.setVertexStage(vertex2.getId());
        backgroundPipeline.validate();
        gbufPipeline = new PipeLine();
        gbufPipeline.setFragmentStage(fragment3.getId());
        gbufPipeline.setVertexStage(vertex3.getId());
        gbufPipeline.validate();
	}
}