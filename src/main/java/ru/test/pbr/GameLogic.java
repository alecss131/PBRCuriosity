package ru.test.pbr;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameLogic {
	private final List<Item> items;
	private final Camera camera;
	private float rot1 = 25.1f;
	private float rot2 = -50.0f;
	private float scale = 1.0f;
	private Light light;
	private Timer timer;
	private boolean rotate = false;
	private float r = 0.0f;
	private Vector3f pos;
	private Matrix4f model = new Matrix4f();
	private UniformBuffer ub;
	private Background bg;
	private Mesh quad;
	private Window window;
	private GBuffer gbuf;
	
	public GameLogic(Window window) {
		this.window = window;
		ub = new UniformBuffer(0, 32 * 10);
		light = new Light();
		light.setPosition(0.0f, 5.0f, 5.0f);
		light.setScale(0.2f);
		light.setCol(1.0f, 1.0f, 1.0f);
		light.setColor(150.0f, 150.0f, 150.0f);
		ub.addData(light.getData());
		items = new ArrayList<>();
		pos = new Vector3f(1.062891960144043f, 0.24909445643424988f, 1.0950909852981567f);
		model.identity().translate(pos);
		String tnames[] = { "tex_03.png", "tex_02.jpg", "tex_01.jpg", "parts_AO.jpg", "tex_03.png", "tex_05.jpg",
				"tex_04.jpg", "tex_03.png" };
		float mf[] = {1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f};
		float rf[] = {0.5f, 0.5f, 0.5f, 0.75f, 0.7f, 0.5f, 0.75f, 1.0f, 0.5f};
		for (int i = 0; i < 9; i++) {
			Item item = new Item(i);
			Material materialItem = new Material();
			if (i < 8) {
				Texture dTexture = new Texture("/assets/textures/" + tnames[i], 0);
				materialItem.setDiffuse(dTexture);
			} else {
				materialItem.setColor(0.8040874f, 0.7515147f, 0.6060708f);
			}
			if (i == 0 || i == 4 || i == 7) {
				materialItem.setNormal(new Texture("/assets/textures/tex_03_n.jpg", 1));
			}
			materialItem.setMetallic(mf[i]);
			materialItem.setRoughness(rf[i]);
			item.setMaterial(materialItem);
			items.add(item);
			ub.addData(materialItem.getData());
		}
		camera = new Camera(15.0f);
		bg = new Background();
		gbuf = new GBuffer(window.getWidth(), window.getHeight());
		float v[] = { -1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f };
		float t[] = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f };
		quad = new Mesh(v, t);
		timer = new Timer();
	}
	
	public void update (Render render) {
		input();
		if (rotate) {
			model.identity().rotate(r, 0.0f, 1.0f, 0.0f).translate(pos);
			r += 0.01f;
		}
		if (window.isKeySinglePressed(InputManager.getScreenShot())) {
			render.saveImage();
		}
		camera.setPosition(rot1, rot2);
		render.clear(); //clear main buffer
		gbuf.bindBuffer();
		render.clear(); //clear gbuffer
		render.render(items, camera, model); //gbuff pass
		gbuf.unbindBuffer();
		gbuf.bindTexture(3, 4, 5);
		render.render(quad, camera, light); //render scene from gbuffer
		gbuf.unbindTexture(3, 4, 5);
		gbuf.copyZBuffer();
		render.render(camera, light); //render light source
		render.render(camera, bg); //render skybox
		window.setFPSTitle((int) (1000.0f/timer.getElapsedTime()));
	}
	
	private void input() {
		if (window.isKeyPressed(InputManager.getLeft())) {
			rot2 += 5f;
		}
		if (window.isKeyPressed(InputManager.getRight())) {
			rot2 -= 5f;
		}
		if (window.isKeyPressed(InputManager.getUp())) {
			rot1 -= 5f;
		}
		if (window.isKeyPressed(InputManager.getDown())) {
			rot1 += 5f;
		}
		if (window.isKeyPressed(InputManager.getLower())) {
			camera.move(0.1f);
		}
		if (window.isKeyPressed(InputManager.getUpper())) {
			camera.move(-0.1f);
		}
		if (window.isKeyPressed(InputManager.getReduce()) && scale > 0.1f) {
			scale -= 0.05f;
			camera.setScale(scale);
		}
		if (window.isKeyPressed(InputManager.getIncrease()) && scale < 5.0f) {
			scale += 0.05f;
			camera.setScale(scale);
		}
		if (window.isKeySinglePressed(InputManager.getSystem())) {
			rotate ^= true;
		}
		if (window.isKeySinglePressed(InputManager.getExit())) {
			window.setWindowShouldClose();
		}
	}
	
	public void cleanUp() {
		for (Item item : items) {
			item.cleanUp();
		}
		quad.cleanUp();
		light.cleanUp();
		ub.cleanUp();
		bg.cleanUp();
		gbuf.cleanUp();
	}
}