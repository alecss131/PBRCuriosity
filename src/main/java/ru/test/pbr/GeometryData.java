package ru.test.pbr;

public class GeometryData {
	private static final float[] cubeVertex = {
		    -0.5f, -0.5f, -0.5f,
		     0.5f, -0.5f, -0.5f,
		     0.5f,  0.5f, -0.5f,
		     0.5f,  0.5f, -0.5f,
		    -0.5f,  0.5f, -0.5f,
		    -0.5f, -0.5f, -0.5f,

		    -0.5f, -0.5f,  0.5f,
		     0.5f, -0.5f,  0.5f,
		     0.5f,  0.5f,  0.5f,
		     0.5f,  0.5f,  0.5f,
		    -0.5f,  0.5f,  0.5f,
		    -0.5f, -0.5f,  0.5f,

		    -0.5f,  0.5f,  0.5f,
		    -0.5f,  0.5f, -0.5f,
		    -0.5f, -0.5f, -0.5f,
		    -0.5f, -0.5f, -0.5f,
		    -0.5f, -0.5f,  0.5f,
		    -0.5f,  0.5f,  0.5f,

		     0.5f,  0.5f,  0.5f,
		     0.5f,  0.5f, -0.5f,
		     0.5f, -0.5f, -0.5f,
		     0.5f, -0.5f, -0.5f,
		     0.5f, -0.5f,  0.5f,
		     0.5f,  0.5f,  0.5f,

		    -0.5f, -0.5f, -0.5f,
		     0.5f, -0.5f, -0.5f,
		     0.5f, -0.5f,  0.5f,
		     0.5f, -0.5f,  0.5f,
		    -0.5f, -0.5f,  0.5f,
		    -0.5f, -0.5f, -0.5f,

		    -0.5f,  0.5f, -0.5f,
		     0.5f,  0.5f, -0.5f,
		     0.5f,  0.5f,  0.5f,
		     0.5f,  0.5f,  0.5f,
		    -0.5f,  0.5f,  0.5f,
		    -0.5f,  0.5f, -0.5f };
	private static final float[] cubeNormals = {
	     0.0f,  0.0f, -1.0f,
	     0.0f,  0.0f, -1.0f, 
	     0.0f,  0.0f, -1.0f, 
	     0.0f,  0.0f, -1.0f, 
	     0.0f,  0.0f, -1.0f, 
	     0.0f,  0.0f, -1.0f, 

	     0.0f,  0.0f, 1.0f,
	     0.0f,  0.0f, 1.0f,
	     0.0f,  0.0f, 1.0f,
	     0.0f,  0.0f, 1.0f,
	     0.0f,  0.0f, 1.0f,
	     0.0f,  0.0f, 1.0f,

	     -1.0f,  0.0f,  0.0f,
	     -1.0f,  0.0f,  0.0f,
	     -1.0f,  0.0f,  0.0f,
	     -1.0f,  0.0f,  0.0f,
	     -1.0f,  0.0f,  0.0f,
	     -1.0f,  0.0f,  0.0f,

	     1.0f,  0.0f,  0.0f,
	     1.0f,  0.0f,  0.0f,
	     1.0f,  0.0f,  0.0f,
	     1.0f,  0.0f,  0.0f,
	     1.0f,  0.0f,  0.0f,
	     1.0f,  0.0f,  0.0f,

	     0.0f, -1.0f,  0.0f,
	     0.0f, -1.0f,  0.0f,
	     0.0f, -1.0f,  0.0f,
	     0.0f, -1.0f,  0.0f,
	     0.0f, -1.0f,  0.0f,
	     0.0f, -1.0f,  0.0f,

	     0.0f,  1.0f,  0.0f,
	     0.0f,  1.0f,  0.0f,
	     0.0f,  1.0f,  0.0f,
	     0.0f,  1.0f,  0.0f,
	     0.0f,  1.0f,  0.0f,
	     0.0f,  1.0f,  0.0f };
	
	private static final float[] cubeUV = {
			 0.0f, 0.0f,
		     1.0f, 0.0f,
		     1.0f, 1.0f,
		     1.0f, 1.0f,
		     0.0f, 1.0f,
		     0.0f, 0.0f,

		     0.0f, 0.0f,
		     1.0f, 0.0f,
		     1.0f, 1.0f,
		     1.0f, 1.0f,
		     0.0f, 1.0f,
		     0.0f, 0.0f,

		     1.0f, 0.0f,
		     1.0f, 1.0f,
		     0.0f, 1.0f,
		     0.0f, 1.0f,
		     0.0f, 0.0f,
		     1.0f, 0.0f,

		     1.0f, 0.0f,
		     1.0f, 1.0f,
		     0.0f, 1.0f,
		     0.0f, 1.0f,
		     0.0f, 0.0f,
		     1.0f, 0.0f,

		     0.0f, 1.0f,
		     1.0f, 1.0f,
		     1.0f, 0.0f,
		     1.0f, 0.0f,
		     0.0f, 0.0f,
		     0.0f, 1.0f,

		     0.0f, 1.0f,
		     1.0f, 1.0f,
		     1.0f, 0.0f,
		     1.0f, 0.0f,
		     0.0f, 0.0f,
		     0.0f, 1.0f };
	
	public static float[] getCubeVertices() {
		return cubeVertex;
	}
	
	public static float[] getCubeNormals() {
		return cubeNormals;
	}
	
	public static float[] getCubeUV() {
		return cubeUV;
	}
}