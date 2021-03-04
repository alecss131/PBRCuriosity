package ru.test.pbr;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

public class PBR {

	public static void main(String[] args) {
		GameLoop game = new GameLoop("Light pbr 3", 800, 600, true, false);
		//GameLoop game = new GameLoop("Light", 1920, 1080, true, true);
		game.run();
	}
}