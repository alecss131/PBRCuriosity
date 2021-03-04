package ru.test.pbr;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.IntBuffer;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;

public class Window {
	private String title;
	private int width;
	private int height;
	private long window;
	private final KeyCallBack keyCallback;
	private long monitor = NULL;

	public Window(String title, int width, int height, boolean vSync, boolean fullScreen) {
		keyCallback = new KeyCallBack();
		this.title = title;
		this.width = width;
		this.height = height;
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE); 
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_SAMPLES, 4);
		if (fullScreen) {
			monitor = glfwGetPrimaryMonitor();
		}
		window = glfwCreateWindow(width, height, title, monitor, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		setCenterWindowPos();
		glfwSetKeyCallback(window, keyCallback);
		glfwMakeContextCurrent(window);
		if (vSync) {
			glfwSwapInterval(1);
		}
	}

	public boolean isKeySinglePressed(int keyCode) {
		return keyCallback.isKeyPressed(keyCode);
	}
	
	public void setFPSTitle(int fps) {
		glfwSetWindowTitle(window, title + " " + fps + " fps");
	}
	
	public void setDecorated(boolean decorated) {
		glfwSetWindowAttrib(window, GLFW_DECORATED, decorated ? GLFW_TRUE : GLFW_FALSE);
	}

	public boolean isKeyPressed(int keyCode) {
		return (glfwGetKey(window, keyCode) == GLFW_PRESS);
	}

	public void update() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	public void cleanUp() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public boolean windowShouldClose() {
		return glfwWindowShouldClose(window);
	}

	public void setWindowShouldClose() {
		glfwSetWindowShouldClose(window, true);
	}

	public void show() {
		glfwShowWindow(window);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public long getId() {
		return window;
	}

	private void setCenterWindowPos() {
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		}
	}
}