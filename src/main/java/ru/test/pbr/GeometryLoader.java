package ru.test.pbr;

import java.io.IOException;
import org.apache.commons.io.IOUtils;

public class GeometryLoader {
	private byte vertices[];
	private byte normals[];
	private byte textures[];
	private byte indices[];
	
	public void load(String name) {
		try {
			indices = IOUtils.resourceToByteArray(name + "-i.bin");
			vertices = IOUtils.resourceToByteArray(name + ".bin");
			normals = IOUtils.resourceToByteArray(name + "-n.bin");
			textures = IOUtils.resourceToByteArray(name + "-t0.bin");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public byte[] getVertices() {
		return vertices;
	}

	public byte[] getNormals() {
		return normals;
	}

	public byte[] getTextures() {
		return textures;
	}

	public byte[] getIndices() {
		return indices;
	}
	
}