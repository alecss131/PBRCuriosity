package ru.test.pbr;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager {
	private static int up = GLFW_KEY_W;
	private static int down = GLFW_KEY_S;
	private static int left = GLFW_KEY_A;
	private static int right = GLFW_KEY_D;
	private static int reduce = GLFW_KEY_Q;
	private static int increase = GLFW_KEY_E;
	private static int screenShot = GLFW_KEY_F;
	private static int system = GLFW_KEY_SPACE;
	private static int exit = GLFW_KEY_ESCAPE;
	private static int lower = GLFW_KEY_Z;
	private static int upper = GLFW_KEY_X;
	
	public static int getUp() {
		return up;
	}

	public static int getDown() {
		return down;
	}

	public static int getLeft() {
		return left;
	}

	public static int getRight() {
		return right;
	}

	public static int getReduce() {
		return reduce;
	}

	public static int getIncrease() {
		return increase;
	}
	
	public static int getScreenShot() {
		return screenShot;
	}
	
	public static int getSystem() {
		return system;
	}
	
	public static int getExit() {
		return exit;
	}

	public static int getLower() {
		return lower;
	}

	public static int getUpper() {
		return upper;
	}
}