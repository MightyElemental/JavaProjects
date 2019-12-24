package net.mightyelemental.maven.RayTraceTest2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mightyelemental.maven.RayTraceTest2.objects.BoundingBox;
import net.mightyelemental.maven.RayTraceTest2.objects.Polyhedron;
import net.mightyelemental.maven.RayTraceTest2.objects.Renderable;
import net.mightyelemental.maven.RayTraceTest2.objects.PolyPoint;
import net.mightyelemental.maven.RayTraceTest2.objects.Triangle;
import net.mightyelemental.maven.RayTraceTest2.thread.RenderChunk;

public class Utils {

	public static Vec3f lambertainShade(Vec3f normal, Vec3f vecToLight, float diffuseCoeff,
			Vec3f color) {
		return color.mul( diffuseCoeff * Math.max( 0, normal.dot( vecToLight ) ) );
	}

	public static Vec3f specularShade(Vec3f rayDir, Vec3f vecToLight, Vec3f normal, float specCoeff,
			float shininess, Vec3f color) {
		Vec3f h = Vec3f.bisect( rayDir, vecToLight );
		float gamma = (int) Math.pow( 2, 2 );
		float lambda = 1 - vecToLight.getReflectedVector( normal ).dot( rayDir );
		float beta = shininess / gamma;
		float max = Math.max( 0, 1 - beta * lambda );
		return color.mul( specCoeff * (float) Math.pow( max, gamma ) );
	}

	public static TexData convertVecToCubeUV(String skyboxImg, Vec3f vec) {
		TexData td = new TexData();
		float absX = Math.abs( vec.x );
		float absY = Math.abs( vec.y );
		float absZ = Math.abs( vec.z );

		boolean isXPositive = vec.x > 0;
		boolean isYPositive = vec.y > 0;
		boolean isZPositive = vec.z > 0;

		float maxAxis = 1, uc = 0, vc = 0;

		if (isXPositive && absX >= absY && absX >= absZ) {
			maxAxis = absX;
			uc = -vec.z;
			vc = vec.y;
			td.img = ResourceLoader.loadImage( skyboxImg + "side_0.png", skyboxImg + "posx.jpg" );
		}
		if (!isXPositive && absX >= absY && absX >= absZ) {
			maxAxis = absX;
			uc = vec.z;
			vc = vec.y;
			td.img = ResourceLoader.loadImage( skyboxImg + "side_1.png", skyboxImg + "negx.jpg" );
		}
		if (isYPositive && absY >= absX && absY >= absZ) {
			maxAxis = absY;
			uc = vec.x;
			vc = -vec.z;
			td.img = ResourceLoader.loadImage( skyboxImg + "side_2.png", skyboxImg + "posy.jpg" );
		}
		if (!isYPositive && absY >= absX && absY >= absZ) {
			maxAxis = absY;
			uc = vec.x;
			vc = vec.z;
			td.img = ResourceLoader.loadImage( skyboxImg + "side_3.png", skyboxImg + "negy.jpg" );
		}
		if (isZPositive && absZ >= absX && absZ >= absY) {
			maxAxis = absZ;
			uc = vec.x;
			vc = vec.y;
			td.img = ResourceLoader.loadImage( skyboxImg + "side_4.png", skyboxImg + "posz.jpg" );
		}
		if (!isZPositive && absZ >= absX && absZ >= absY) {
			maxAxis = absZ;
			uc = -vec.x;
			vc = vec.y;
			td.img = ResourceLoader.loadImage( skyboxImg + "side_5.png", skyboxImg + "negz.jpg" );
		}
		td.uvX = 0.5f * (uc / maxAxis + 1f);
		td.uvY = 0.5f * (vc / maxAxis + 1.0f);
		return td;
	}

	/**
	 * Opens a file as a buffered reader. Remember to close the reader after use!
	 */
	public static BufferedReader fileReader(String f) throws FileNotFoundException {
		return new BufferedReader( new FileReader( f ) );
	}

	public static Optional<Polyhedron> getRenderableFromObjFile(String file) {
		File objFile = new File( "./objs/" + file );
		if (!objFile.exists()) return Optional.empty();
		System.out.println( "Found obj ./objs/" + file );
		List<Vec3f> vecs = new ArrayList<Vec3f>();
		List<Vec3f> uvVecs = new ArrayList<Vec3f>();
		List<Vec3f> norms = new ArrayList<Vec3f>();
		List<Triangle> tris = new ArrayList<Triangle>();
		try {
			BufferedReader br = fileReader( objFile.getAbsolutePath() );
			String line = "";
			while ((line = br.readLine()) != null) {
				line = line.trim().replaceAll( "[ ]{2,}", " " );
				if (line.startsWith( "v " )) {
					vecs.add( getVecFromObjLine( line ) );
				} else if (line.startsWith( "vt " )) {
					uvVecs.add( getUVVecFromObjLine( line ) );
				} else if (line.startsWith( "vn " )) {
					norms.add( getNormFromObjLine( line ) );
				} else if (line.startsWith( "f " )) {
					tris.addAll( getFacesFromObjLine( line, vecs, uvVecs, norms ) );
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.of( new Polyhedron( tris ) );
	}

	private static Pattern p = Pattern.compile( "(\\d+)\\/?(\\d+)?\\/?(\\d+)?" );

	private static PolyPoint getPointFromArg(String arg, List<Vec3f> vecs, List<Vec3f> uvVecs,
			List<Vec3f> norms) {
		if (arg.length() < 1) return null;
		Matcher matcher = p.matcher( arg );
		matcher.find();
		String point = matcher.group( 1 );
		String tex = matcher.group( 2 );
		String norm = matcher.group( 3 );
		// System.out.println(point + "|" + tex + "|" + norm);
		Vec3f p = vecs.get( Integer.parseInt( point ) - 1 );
		Vec3f t = tex == null ? null : getTex( tex, uvVecs );
		Vec3f n = norm == null ? null : norms.get( Integer.parseInt( norm ) - 1 );
		return new PolyPoint( p, t, n );
	}

	private static Vec3f getTex(String tex, List<Vec3f> uvVecs) {
		if (tex == null) return null;
		return uvVecs.get( Integer.parseInt( tex ) - 1 );
	}

	private static List<Triangle> getFacesFromObjLine(String line, List<Vec3f> vecs,
			List<Vec3f> uvVecs, List<Vec3f> norms) {
		String[] a = line.split( " " );
		if (a.length < 4) {
			System.err.println( "not enough values were provided for a face!" );
			System.err.println( "\t" + line );
			return null;
		}
		PolyPoint[] points = new PolyPoint[a.length - 1];
		for (int i = 1; i < a.length; i++) {
			points[i - 1] = getPointFromArg( a[i], vecs, uvVecs, norms );
		}
		return Triangle.getTrisFromPoints( points );
	}

	private static Vec3f getNormFromObjLine(String line) {
		String[] a = line.split( " " );
		if (a.length < 4) {
			System.err.println( "not enough values were provided for a normal!" );
			System.err.println( "\t" + line );
			return null;
		}
		return new Vec3f( Float.parseFloat( a[1] ), Float.parseFloat( a[2] ),
				Float.parseFloat( a[3] ) );
	}

	private static Vec3f getUVVecFromObjLine(String line) {
		String[] a = line.split( " " );
		if (a.length < 2) {
			System.err.println( "no values were provided for a uv point!" );
			System.err.println( "\t" + line );
			return null;
		}
		switch (a.length) {
		default:
		case 2:
			return new Vec3f( Float.parseFloat( a[1] ), 0, 0 );
		case 3:
			return new Vec3f( Float.parseFloat( a[1] ), Float.parseFloat( a[2] ), 0 );
		case 4:
			return new Vec3f( Float.parseFloat( a[1] ), Float.parseFloat( a[2] ),
					Float.parseFloat( a[3] ) );
		}
	}

	private static Vec3f getVecFromObjLine(String line) {
		String[] a = line.split( "\\s+" );
		if (a.length < 4) {
			System.err.println( "Less than three values were provided for a vector point!" );
			System.err.println( "\t" + line );
			return null;
		}
		try {
			return new Vec3f( Float.parseFloat( a[1] ), Float.parseFloat( a[2] ),
					Float.parseFloat( a[3] ) );
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.err.println( Arrays.toString( a ) );
		}
		return null;
	}

	public static Vector<RenderChunk> generateRenderChunks(int width, int height) {
		Vector<RenderChunk> result = new Vector<RenderChunk>(
				width * height / (RenderChunk.CHUNK_SIZE * RenderChunk.CHUNK_SIZE) );
		for (int y = 0; y < height; y += RenderChunk.CHUNK_SIZE) {
			for (int x = 0; x < width; x += RenderChunk.CHUNK_SIZE) {
				RenderChunk rc = new RenderChunk( x, y );
				result.add( rc );
			}
		}
		return result;
	}

	public static BoundingBox generateBoundingBox(Renderable rend) {
		return rend.generateBoundingBox();
	}

//	public static Vec3f minVec(Vec3f v1, Vec3f v2) {
//		float x = v1.x < v2.x ? v1.x : v2.x;
//		float y = v1.y < v2.y ? v1.y : v2.y;
//		float z = v1.z < v2.z ? v1.z : v2.z;
//		return new Vec3f( x, y, z );
//	}

	public static Vec3f minVec(Vec3f... vecs) {
		float x = 0;
		float y = 0;
		float z = 0;
		for (Vec3f v : vecs) {
			if (v.x < x) x = v.x;
			if (v.y < y) y = v.y;
			if (v.z < z) z = v.z;
		}
		return new Vec3f( x, y, z );
	}

//	public static Vec3f maxVec(Vec3f v1, Vec3f v2) {
//		float x = v1.x > v2.x ? v1.x : v2.x;
//		float y = v1.y > v2.y ? v1.y : v2.y;
//		float z = v1.z > v2.z ? v1.z : v2.z;
//		return new Vec3f( x, y, z );
//	}

	public static Vec3f maxVec(Vec3f... vecs) {
		float x = 0;
		float y = 0;
		float z = 0;
		for (Vec3f v : vecs) {
			if (v.x > x) x = v.x;
			if (v.y > y) y = v.y;
			if (v.z > z) z = v.z;
		}
		return new Vec3f( x, y, z );
	}

	public static BoundingBox mergeBoxes(BoundingBox b1, BoundingBox b2) {
		return new BoundingBox( minVec( b1.min, b2.min ), maxVec( b1.max, b2.max ) );
	}

	public static int getTriNumber(int base) { return base * (base + 1) / 2; }

	public static double log(double x, double b) { return (Math.log( x ) / Math.log( b )); }

	/**
	 * @param minNodeCount - The minimum number of nodes in the binary tree
	 * @return the number of nodes in a full binary tree that holds the minimum number of nodes given by the param
	 */
	public static int getBinTreeNodeCount(int minNodeCount) {
		int height = (int) Math.floor( log( minNodeCount, 2 ) );
		return (int) (Math.pow( 2, height + 1 ) - 1);
	}

	public static double round(double value, int precision) {
		double mul = Math.pow( 10, precision );
		return Math.round( value * mul ) / mul;
	}

	/** Used to get the top half of an array */
	public static <T> T[] getTopHalf(T[] arr) {
		if (arr == null) return null;
		if (arr.length < 1) return arr;
		int size = Math.floorDiv( arr.length, 2 );
		T[] newT = Arrays.copyOfRange( arr, size, arr.length );
		return newT;
	}

	/** Used to get the bottom half of an array */
	public static <T> T[] getBottomHalf(T[] arr) {
		if (arr == null) return null;
		if (arr.length < 1) return arr;
		int size = Math.floorDiv( arr.length, 2 );
		T[] newT = Arrays.copyOfRange( arr, 0, size );
		return newT;
	}

}
