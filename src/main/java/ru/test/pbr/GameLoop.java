package ru.test.pbr;

public class GameLoop implements Runnable {

	private int TARGET_FPS = 60;
	private final Window window;
	private final Timer timer;
	private final Render render;
	private boolean vSync;
	private final GameLogic game;

	public GameLoop(String windowTitle, int width, int height, boolean vSync, boolean fullScreen) {
		window = new Window(windowTitle, width, height, vSync, fullScreen);
		render = new Render(window);
		game = new GameLogic(window);
		this.vSync = vSync;
		timer = new Timer();
	}

	@Override
	public void run() {
		try {
			gameLoop();
			cleanup();
		} catch (Exception excp) {
			excp.printStackTrace();
		}
	}

	private void gameLoop() {
		window.show();
		while (!window.windowShouldClose()) {
			game.update(render);
			window.update();
			if (!vSync) {
				sync();
			}
		}
	}

	private void cleanup() {
		render.cleanUp();
		window.cleanUp();
		game.cleanUp();
	}

	private void sync() {
		int loopSlot = 1000 / TARGET_FPS;
		long endTime = timer.getLastLoopTime() + loopSlot;
		while (timer.getTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}