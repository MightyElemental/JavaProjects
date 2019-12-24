package net.mightyelemental.maven.RayTraceTest2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.mightyelemental.maven.RayTraceTest2.BVH.BVH;
import net.mightyelemental.maven.RayTraceTest2.materials.Material;
import net.mightyelemental.maven.RayTraceTest2.objects.Box;
import net.mightyelemental.maven.RayTraceTest2.objects.CameraModel;
import net.mightyelemental.maven.RayTraceTest2.objects.Light;
import net.mightyelemental.maven.RayTraceTest2.objects.Plane;
import net.mightyelemental.maven.RayTraceTest2.objects.Polyhedron;
import net.mightyelemental.maven.RayTraceTest2.objects.Renderable;
import net.mightyelemental.maven.RayTraceTest2.objects.Scene;
import net.mightyelemental.maven.RayTraceTest2.objects.Sphere;
import net.mightyelemental.maven.RayTraceTest2.objects.Triangle;
import net.mightyelemental.maven.RayTraceTest2.thread.RenderChunk;
import net.mightyelemental.maven.RayTraceTest2.thread.ThreadHandler;

public class App implements KeyListener, MouseWheelListener {

	boolean pause = false;

	int frameCount = 0;

	/** The threadpool that handles the rendering */
	ExecutorService threadPool;

	/** Used to render the screen while the rendering is still occurring */
	Thread starting = new Thread() {

		public void run() {
			while (frameCount <= 0) {
				pan.repaint();
				try {
					sleep( 10 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public App() {
		System.out.println( "Thread count: " + Properties.threads );
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setTitle( "A ray tracer" );
		window.setSize( screen.getWidth(), screen.getHeight() );
		window.add( pan );
		window.setVisible( true );
		window.setLocationRelativeTo( null );
		window.setResizable( false );
		window.addKeyListener( this );
		setupScene();
		threadPool = Executors.newFixedThreadPool( Properties.threads );
		// lights.get(0).color = new Vector3f(1,0.5f,1);
		int ticks = 0;
		long timeOff = 0;
		starting.start();
		while (true) {
			long t1 = System.currentTimeMillis();
			// cam.moveTo((float) Math.sin(ticks / 80f) * 10 + 3, 10, 10);
			// lights.get(0).pos.setX((float) Math.sin(ticks / 60f) * 500);
			// lights.get(0).pos.setY((float) Math.cos(ticks / 60f) * 500);
			s5.center.setX( (float) Math.sin( ticks / 30f ) * 10 );
			s5.center.setZ( (float) Math.cos( ticks / 30f ) * 10 );
			s5.radius = (float) Math.sin( ticks / 60f ) * 4 + 5;

			BVH.generateBVHTree( worldScene );
			updateControls();

			try {
				render();
				// Thread.sleep( 1 );
				// Thread.sleep( 5 - timeOff > 0 ? Math.min( timeOff, 5 ) : 0 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ticks++;
			timeOff += System.currentTimeMillis() - t1 - 5;
			// if ( ticks % 20 == 0 ) {
			// System.out.println(timeOff / 1000f);
			// }
			frametimes.add( System.currentTimeMillis() - t1 );
			while (pause) {
				try {
					render();
					Thread.sleep( 500 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	Sphere      s5   = new Sphere( new Vec3f( 7, 4, 2 ), 1 );
	CameraModel camS = new CameraModel( new Vec3f( 0, 0, 0 ) );

	@Deprecated
	public List<Thread> setupRenderingThreads(int[] pixels, int width, int height, int pixelSize) {
		List<Thread> renderingThreads = new ArrayList<Thread>();
		for (int i = 0; i < Properties.threads - 1; i++) {
			final int threadNum = i;
			Thread rt = new Thread( "t" + threadNum ) {

				public void run() {
					for (int x = width / Properties.threads * threadNum; x < width / Properties.threads
							* (threadNum + 1); x += pixelSize) {
						for (int y = 0; y < height; y += pixelSize) {
							Ray r = cam.createRay( x, y );
							int c = getIntFromVector( trace( r, 0 ) );
							for (int i = 0; i < pixelSize && x + i < width; i++) {
								for (int j = 0; j < pixelSize && y + j < height; j++) {
									setPixel( pixels, width, x + i, y + j, c );
								}
							}
						}
					}
				}
			};
			renderingThreads.add( rt );
		}
		return renderingThreads;
	}

	public void renderLoop(RenderChunk rc, int screenWidth, int screenHeight, int pixelSize,
			int[] pixels) {

		for (int x = rc.x; x < screenWidth && x < RenderChunk.CHUNK_SIZE + rc.x; x += pixelSize) {
			for (int y = 0; y < screenHeight && y < RenderChunk.CHUNK_SIZE + rc.y; y += pixelSize) {
				Ray r = cam.createRay( x, y );
				int c = getIntFromVector( trace( r, 0 ) );
				for (int i = 0; i < pixelSize && x + i < screenWidth; i++) {
					for (int j = 0; j < pixelSize && y + j < screenHeight; j++) {
						setPixel( pixels, screenWidth, x + i, y + j, c );
					}
				}
			}
		}

	}

	public static final int FPS_TARGET = 30;

	public void setupScene() {

//		for (int i = 0; i < 300; i++) {
//			// if (ticks % 5 == 0) {
//			float x = (float) Math.abs( Math.random() - Math.random() ) * 300 - 150;
//			float z = (float) Math.abs( Math.random() - Math.random() ) * 300 - 150;
//			float y = (float) Math.abs( Math.random() - Math.random() ) * 30;
//			Sphere newSphere = new Sphere( new Vec3f( x, y, z ), 5 );
//			float r = (x + 150) / 300;
//			newSphere.col = new Vec3f( r, 1 - r, (z + 150) / 300 );
//			newSphere.setMaterial( 0f, 1f, 1f );
//			worldScene.add( newSphere );
//			// }
//		}

		// objects.add(new Sphere(0.1f));
		Sphere s = new Sphere( new Vec3f( 0, 0, 0 ), 6 );
		s.col = new Vec3f( 0, 0, 1 );
		s.setMaterial( 0f, 0.3f, 1.52f );
		// s.setMaterial(0f, 0f, 1f);
//		s.opacity = 0.3f;
//		s.reflectivity = 0f;
//		s.ior = 1.52f;//4f / 3f;
		// worldScene.add( s );

		Sphere s2 = new Sphere( new Vec3f( 0, 5, 12 ), 4 );
		s2.col = new Vec3f( 1, 0, 1 );
		s2.setMaterial( 0f, 0.3f, 2f );
		// worldScene.add( s2 );

		Sphere s3 = new Sphere( new Vec3f( 8, 7.5f, -15 ), 7 );
		s3.col = new Vec3f( 1, 0, 0 );
		// s3.reflectivity = 0f;
		// worldScene.add( s3 );

		Sphere s4 = new Sphere( new Vec3f( -13, 3, -20 ), 5 );
		s4.col = new Vec3f( 0.5f, 0, 1 );
		s4.setMaterial( 0.8f, 1, 1 );
		// worldScene.add( s4 );

		Sphere earth = new Sphere( new Vec3f( 0, -6371000, 0 ), 6371000 );
		earth.col = new Vec3f( 67, 109, 7 ).mul( 1f / 255f );
		// worldScene.add(earth);

		Plane p = new Plane( new Vec3f( 0, 1, 0 ), new Vec3f( 0, -2, 0 ) );
		p.reflectivity = 0;
		p.col = new Vec3f( 255, 109, 0 ).mul( 1f / 255f );
		// worldScene.add(p);// Floor

		// Circle c = new Circle( new Vec3f( 0, 0, 1 ).normalize(), new Vec3f( -10, 40, 0 ), 20 );
		// worldScene.add( c );

		Triangle t = new Triangle( new Vec3f( 5, 0, 0 ), new Vec3f( 5, 10, 0 ),
				new Vec3f( 10, 10, 0 ) );
		Triangle t2 = new Triangle( new Vec3f( 5, 0, 0 ), new Vec3f( 10, 0, 0 ),
				new Vec3f( 10, 10, 0 ) );
		List<Triangle> vec = new ArrayList<Triangle>();
		vec.add( t );
		vec.add( t2 );
		Polyhedron comp = new Polyhedron( vec );
		comp.setMaterial( 1f, 1f, 1f );
		// worldScene.add( comp );

		Box box = new Box( new Vec3f( 10, 10, 10 ), 15, 5, 8 );
		box.setMaterial( 0.5f, 0.5f, 1f );
		box.setColor( new Vec3f( 0.5f, 0.1f, 1f ) );
		// worldScene.add( box );

		// worldScene.add(new Plane(new Vector3f(1, 0, 0), new Vector3f(20, 0, 0)));

		s5.col = new Vec3f( 0, 1, 0 );
		//worldScene.add( s5 );

		worldScene.add( camS );
		cam.setCamObj( camS );

		Polyhedron ammo = Utils.getRenderableFromObjFile( "pikachu.obj" ).get();
		ammo.translate( new Vec3f( 50, -10, -10 ) );
		ammo.setMaterial( 0f, 1f, 1f );
		ammo.setColor( new Vec3f( 44 / 255f, 205 / 255f, 138 / 255f ) );
		ammo.rotate( new Vec3f( -90, 0, 0 ) );
		worldScene.add( ammo );

		Polyhedron pika = Utils.getRenderableFromObjFile( "pikachu.obj" ).get();
		pika.translate( new Vec3f( -10, 20, -10 ) );
		pika.setMaterial( 0.8f, 1f, 1f );
		pika.setColor( new Vec3f( 1, 0, 0 ) );
		// pika.useRandomColors();
		pika.rotate( new Vec3f( -90, 0, 0 ) );
		worldScene.add( pika );

		// worldScene.add(new Tube(new Vector3f(0, 1, 0), new Vector3f(0, 2, 4), 0, 2));

		worldScene.add( new Light( 1000, 500, 400 ) );
		// lights.add(new Light(10, 20, 3));
		// Sphere ls = new Sphere(lights.get(0).pos, 9f);
		// ls.col = new Vector3f(1, 1, 0f);
		// worldScene.add(ls);
	}

	private List<Long> frametimes = new ArrayList<Long>();

	public static int MAX_RAY_DEPTH = 5;

	public JFrame window = new JFrame();

	public JPanel pan = new JPanel() {

		private static final long serialVersionUID = -3107887887785696282L;

		public void paint(Graphics g) {
			// ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			// RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage( screen, 0, 0, 1280, 720, null );
			String text = "FPS: " + Utils.round( getAvgFPS(), 2 );
			g.setColor( Color.WHITE );
			g.fillRect( 0, 0, 72, 50 );
			g.setColor( Color.BLACK );
			g.drawString( text, 5, 20 );

			if (pause) {
				g.drawString( "PAUSE", 5, 40 );
			} else if (gravity) { g.drawString( "GRAV ON", 5, 40 ); }
		}
	};

	public BufferedImage screen = new BufferedImage( 1280, 720, BufferedImage.TYPE_INT_RGB );

	public Camera cam = new Camera( new Vec3f( 3, 10, 25 ), 95, screen.getWidth(),
			screen.getHeight() );// TODO: add multiple rays per pixel

	public Scene worldScene = new Scene();

	public static final float ambientCoeff = 0.3f;

	public int[] pixelGrid;

	{
		pixelGrid = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
	}

	public static void main(String[] args) {
		// Vector3f vec = new Vector3f(1,2,3);
		// Mat3f n = Mat3f.getIdentity();
		// System.out.println(n.multiply(vec));
		new App();
	}

	public static int getIntFromColor(int Red, int Green, int Blue) {
		Red = (Red << 16) & 0x00FF0000; // Shift red 16-bits and mask out other stuff
		Green = (Green << 8) & 0x0000FF00; // Shift Green 8-bits and mask out other stuff
		Blue = Blue & 0x000000FF; // Mask out anything not blue.

		return 0xFF000000 | Red | Green | Blue; // 0xFF000000 for 100% Alpha. Bitwise OR everything together.
	}

	private void hqRender() throws InterruptedException {
		// cam.width = 3840;
		// cam.height = 2160;
		int oldMaxRayDepth = MAX_RAY_DEPTH;
		MAX_RAY_DEPTH = Math.max( MAX_RAY_DEPTH, 10 );// if the default is higher, don't reduce it.
		pause = true;
		Thread.sleep( 1 );
		BufferedImage img = new BufferedImage( 3840, 2160, BufferedImage.TYPE_INT_RGB );
		long t1 = System.currentTimeMillis();
		renderToTarget( img );
		System.out.printf( "Time taken to render: %dms\n", System.currentTimeMillis() - t1 );
		MAX_RAY_DEPTH = oldMaxRayDepth;
		try {
			String date = (new Date()).toString().replaceAll( " ", "_" ).replaceAll( ":", "." );
			new File( "./imgs/renders/" ).mkdirs();
//			File outputfile = new File("./imgs/renders/render_" + date + ".jpg");
//			ImageIO.write(img, "jpg", outputfile);
			String path = "./imgs/renders/render_" + date.toLowerCase() + ".jpg";
			saveImage( img, path, 0.95f );
			showFileInExplorer( path );
		} catch (IOException e) {
			e.printStackTrace();
		}
		pause = false;
	}

	public void showFileInExplorer(String path) throws IOException {
		Properties.OSTYPE os = Properties.getOSType();
		switch (os) {
		case LINUX:
			Runtime.getRuntime().exec( "xdg-open " + path );
			break;
		case MAC:
		case OTHER:
			System.out.println( "Could not open file explorer" );
			break;
		case WINDOWS:
			Runtime.getRuntime().exec( "explorer.exe /select," + path );
			break;
		default:
			break;
		}
	}

	public void saveImage(BufferedImage img, String location, float quality)
			throws FileNotFoundException, IOException {
		JPEGImageWriteParam jpegParams = new JPEGImageWriteParam( null );
		jpegParams.setCompressionMode( ImageWriteParam.MODE_EXPLICIT );
		jpegParams.setCompressionQuality( quality );

		final ImageWriter writer = ImageIO.getImageWritersByFormatName( "jpg" ).next();
		// specifies where the jpg image has to be written
		writer.setOutput( new FileImageOutputStream( new File( location ) ) );

		// writes the file with given compression level
		// from your JPEGImageWriteParam instance
		writer.write( null, new IIOImage( img, null, null ), jpegParams );
	}

	public void render() throws InterruptedException {
		cam.calculateStartingMaterial( worldScene.objectList );
		dynamicRender();
		// antiAllias();
		Thread.sleep( 1 );
		pan.repaint();
		// window.repaint();
	}

	public BufferedImage renderToTarget(BufferedImage target) throws InterruptedException {
		float oldWid = cam.width;
		float oldHeight = cam.height;
		cam.width = target.getWidth();
		cam.height = target.getHeight();

		int[] pixels = ((DataBufferInt) target.getRaster().getDataBuffer()).getData();

		// jjj

		ThreadHandler thrHnd = new ThreadHandler( target.getWidth(), target.getHeight() );

		int chunkCount = thrHnd.chunks.size();
		CountDownLatch latch = new CountDownLatch( chunkCount );
		System.out.println( "HQREND: Starting HQ Render..." );

		thrHnd.chunks.forEach( c -> threadPool.submit( () -> {
			renderLoop( c, target.getWidth(), target.getHeight(), 1, pixels );
			latch.countDown();
			System.out.printf( "HQREND: Completed %d/%d chunks\n", chunkCount - latch.getCount(),
					chunkCount );
		} ) );

		try {
			latch.await();
			Thread.sleep( 1 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.printf( "Completed rendering to the %dx%d canvas\n", target.getWidth(),
				target.getHeight() );

		// jjj

//		List<Thread> renderingThreads = setupRenderingThreads( pixels, target.getWidth(),
//				target.getHeight(), 1 );
//
//		for (Thread t : renderingThreads) { t.start(); }
//
//		int cores = Properties.cores;
//
//		for (int x = target.getWidth() / cores * (cores - 1); x < target.getWidth(); x++) {
//			for (int y = 0; y < target.getHeight(); y++) {
//				Ray r = cam.createRay( x, y );
//				int c = getIntFromVector( trace( r, 0 ) );
//				setPixel( pixels, target.getWidth(), x, y, c );
//				// System.out.println(c);
//			}
//		}
//		for (Thread t : renderingThreads) { t.join(); }
		cam.width = oldWid;
		cam.height = oldHeight;
		return target;
	}

	int toggle = 0;

	public void fourWayInterlaceRender() {
		int xs = 0, ys = 0;
		if (toggle == 0) {
			xs = 0;
			ys = 0;
		}
		if (toggle == 1) {
			xs = 1;
			ys = 0;
		}
		if (toggle == 2) {
			xs = 1;
			ys = 1;
		}
		if (toggle == 3) {
			xs = 0;
			ys = 1;
		}
		for (int x = xs; x < screen.getWidth(); x += 2) {
			for (int y = ys; y < screen.getHeight(); y += 2) {
				Ray r = cam.createRay( x, y );
				int c = getIntFromVector( trace( r, 0 ) );
				setPixel( x, y, c );
				// System.out.println(c);
			}
		}
		toggle = (++toggle) % 4;
	}

	/** The chunk size can be found here: {@link RenderChunk#CHUNK_SIZE} */
	ThreadHandler thrHnd = new ThreadHandler( screen.getWidth(), screen.getHeight() );

	private void dynamicRender() {
		int fps = (int) Math.ceil( getAvgFPS() );
		int step = Math.min( pause ? 1 : (FPS_TARGET - fps > 1 ? FPS_TARGET - fps : 1), 40 );

		int width = screen.getWidth();
		int height = screen.getHeight();

		// System.out.println( step );

		CountDownLatch latch = new CountDownLatch( thrHnd.chunks.size() );

		thrHnd.chunks.forEach( c -> threadPool.submit( () -> {
			renderLoop( c, width, height, step, pixelGrid );
			latch.countDown();
		} ) );

		// threadPool.shutdown();

		try {
			latch.await();
			Thread.sleep( 1 );
			// threadPool.awaitTermination( 60, TimeUnit.MINUTES );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// threadPool = Executors.newFixedThreadPool( Properties.cores );

//		List<Thread> renderingThreads = thrHnd.setupRenderingThreads( this,
//				pixelGrid, step );

//		for (Thread t : renderingThreads) { t.start(); }
//
//		boolean completed = false;
//		while (!completed) {
//			try {
//				Thread.sleep( 5 );
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			completed = true;
//			for (RenderChunk c : thrHnd.chunks) {
//				if (!c.finishedRender) {
//					completed = false;
//					continue;
//				}
//			}
//		}

//		List<Thread> renderingThreads = setupRenderingThreads(pixelGrid, width, height, step);
//
//		for (Thread t : renderingThreads) {
//			t.start();
//		}
//
//		for (int x = width / Properties.cores * (Properties.cores - 1); x < width; x += step) {
//			for (int y = 0; y < height; y += step) {
//				Ray r = cam.createRay(x, y);
//				int c = getIntFromVector(trace(r, 0));
//				for (int i = 0; i < step; i++) {
//					for (int j = 0; j < step; j++) {
//						setPixel(x + i, y + j, c);
//					}
//				}
//				// System.out.println(c);
//			}
//		}
//		try {
//			for (Thread t : renderingThreads) { t.join(); }
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	public void updateScreenSize(int width, int height) {
		if (width == screen.getWidth() && height == screen.getHeight()) return;
		screen = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
		cam.setDim( width, height );
		pixelGrid = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
	}

	public Vec3f trace(Ray r, int depth) {// TODO: make shadows depend on transparency
//		long t1 = System.nanoTime();
		List<Renderable> objects = BVH.intersects( r );
//		System.out.printf( "It took %dns to call the BVH and found %d objs to test\n",
//				System.nanoTime() - t1, objects.size() );
		// System.out.println( objects.size() );
		if (objects.isEmpty()) return getBackground( r.getDirection() );

		Renderable rend = r.trace( objects, depth );
		if (rend == null) return getBackground( r.getDirection() );
		Vec3f hit = r.getHitPoint();
		Vec3f color = new Vec3f( 0, 0, 0 );
		Vec3f base = getDiffuse( r.getDirection(), rend, hit, depth );
		if (depth < MAX_RAY_DEPTH && rend.getMaterial().getReflectivity() > 0
				&& rend.getMaterial().getOpacity() < 1) {
			Vec3f ref = traceReflection( r, rend, hit, depth );
			Vec3f adjustedRef = ref.mul( rend.getMaterial().getOpacity() * (1 - ambientCoeff)
					* rend.getMaterial().getReflectivity() );
			// refraction
			Vec3f refractionCol = traceRefraction( r, rend, hit, depth );
			Vec3f adjustedRefrac = refractionCol
					.mul( (1 - rend.getMaterial().getOpacity()) * (1 - ambientCoeff) );

			color = base
					.mul( ambientCoeff * rend.getMaterial().getOpacity()
							* (1 - rend.getMaterial().getReflectivity()) )
					.sum( adjustedRef ).sum( adjustedRefrac );
		} else if (depth < MAX_RAY_DEPTH && rend.getMaterial().getReflectivity() > 0) { // reflection

			Vec3f ref = traceReflection( r, rend, hit, depth );
			Vec3f adjustedRef = ref.mul( (1 - ambientCoeff) * rend.getMaterial().getReflectivity() );

			// VERIFY removing the world ambient coeff does not break the system
			color = base.mul( (1 - rend.getMaterial().getReflectivity()) ).sum( adjustedRef );
		} else if (depth < MAX_RAY_DEPTH && rend.getMaterial().getOpacity() < 1) { // refraction
			Vec3f refractionCol = traceRefraction( r, rend, hit, depth );
			Vec3f adjustedRefrac = refractionCol
					.mul( (1 - rend.getMaterial().getOpacity() * (1 - ambientCoeff)) );

			color = base.mul( ambientCoeff * rend.getMaterial().getOpacity() ).sum( adjustedRefrac );
		} else {
			color = getDiffuse( r.getDirection(), rend, hit, depth );
		}
		return color;
	}

	public Vec3f traceReflection(Ray r, Renderable rend, Vec3f hit, int depth) {
		Vec3f dir = r.getReflectedVector( rend.getNormal( hit, r.getDirection() ) ).normalize();
		Ray refr = new Ray( dir, hit.sum( dir.mul( 0.01f ) ), rend.getMaterial() );
		return trace( refr, depth + 1 );
	}

	public Vec3f traceRefraction(Ray r, Renderable rend, Vec3f hit, int depth) {
		// System.out.println(depth);
		Vec3f norm = rend.getNormal( hit, r.getDirection() ).normalize();
		Material rayStartMat = r.getStartingMaterial();
		// TODO: calculate the material better.
		Vec3f refracDir = r.getRefractionVector( rayStartMat.getIOR() / rend.getMaterial().getIOR(),
				norm );
		if (refracDir == null)// TODO: replace with TIR
			return Vec3f.origin();
		// TODO: make sure this is the correct material
		Ray refractionRay = new Ray( refracDir, hit.sub( norm.mul( 0.01f ) ), rend.getMaterial() );
		return trace( refractionRay, depth + 1 );
	}

	private Vec3f getBackground(Vec3f frag) {
		return Utils.convertVecToCubeUV( worldScene.getSkybox(), frag ).getColor();
	}

	public static Vec3f rgbIntToVec(int col) {
		Color c = new Color( col );
		float r = c.getRed() / 255f;
		float g = c.getGreen() / 255f;
		float b = c.getBlue() / 255f;
		return new Vec3f( r, g, b );
	}

	private Vec3f getDiffuse(Vec3f rayDir, Renderable rend, Vec3f hit, int depth) {
		Vec3f color = rend.getColor().mul( ambientCoeff );
		Vec3f lightSamples = new Vec3f( 0, 0, 0 );
		for (Light l : worldScene.lightList) {
			Vec3f lvec = hit.vecTo( l.pos ).normalize();
			Ray shadowRay = new Ray( lvec, hit.sum( lvec.mul( 0.01f ) ) );
			Renderable obj = shadowRay.trace( worldScene.objectList, depth );
			if (obj == null || (obj instanceof Sphere && ((Sphere) obj).center.equals( l.pos ))) {// TODO: fix sun shadow
				float dot = rend.getNormal( hit, rayDir ).getUnitVec().dot( lvec );
				if (dot > 0) lightSamples = lightSamples.sum( l.color.mul( l.brightness * dot ) );
				// System.out.println("asd");
				break;
			}
		}
		return color.sum( lightSamples.mul( (1 - ambientCoeff) / worldScene.lightList.size() ) );
	}

	public int getIntFromVector(Vec3f col) {
		return getIntFromColor( (int) (col.x * 255), (int) (col.y * 255), (int) (col.z * 255) );
	}

	public void setPixel(int x, int y, int col) {
		if (x >= screen.getWidth() || y >= screen.getHeight()) return;
		pixelGrid[(int) (x + y * screen.getWidth())] = col;
	}

	private void setPixel(int x, int y, Vec3f vec) { setPixel( x, y, getIntFromVector( vec ) ); }

	public void setPixel(int[] pixels, int width, int x, int y, int c) {
		if (x > width) return;
		pixels[(int) (x + y * width)] = c;
	}

	public int getPixel(int x, int y) { return pixelGrid[(int) (x + y * screen.getWidth())]; }

	public void antiAllias() {// blurs screen
		for (int x = 0; x < screen.getWidth(); x++) {
			for (int y = 0; y < screen.getHeight(); y++) {
				int samples = 0;
				Vec3f colTot = Vec3f.origin();
				if (x > 1) {
					samples += 1;
					colTot = colTot.sum( rgbIntToVec( getPixel( x - 1, y ) ) );
				}
				if (y > 1) {
					samples += 1;
					colTot = colTot.sum( rgbIntToVec( getPixel( x, y - 1 ) ) );
				}
				if (x < screen.getWidth() - 1) {
					samples += 1;
					colTot = colTot.sum( rgbIntToVec( getPixel( x + 1, y ) ) );
				}
				if (y < screen.getHeight() - 1) {
					samples += 1;
					colTot = colTot.sum( rgbIntToVec( getPixel( x, y + 1 ) ) );
				}
				colTot = colTot.sum( rgbIntToVec( getPixel( x, y ) ) );
				setPixel( x, y, colTot.mul( 1f / samples ) );
			}
		}
	}

	public synchronized double getAvgFPS() {
		OptionalDouble od = OptionalDouble.empty();
		try {
			od = frametimes.stream().mapToLong( Long::longValue ).average();
		} catch (NullPointerException | ConcurrentModificationException e) {
			// ignore all
		}
		return 1000f / od.orElse( 1000 );
	}

	Set<Integer> keys = new HashSet<Integer>();

	@Override
	public void keyPressed(KeyEvent e) { keys.add( e.getKeyCode() ); }

	@Override
	public void keyReleased(KeyEvent event) {
		keys.removeIf( e -> e == event.getKeyCode() );
		if (event.getKeyCode() == KeyEvent.VK_G) {
			gravity = !gravity;
		} else if (event.getKeyCode() == KeyEvent.VK_P) {
			pause = !pause;
		} else if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				hqRender();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean gravity   = false;
	public float   fallSpeed = 0;

	@Override
	public void keyTyped(KeyEvent e) {}

	public void updateControls() {
		float speed = 1;
		float camHeightTarget = 5;

		while (frametimes.size() > 200) { frametimes.remove( 0 ); }
		if (!pause) {
			if (gravity) {
				cam.cameraPos.y -= fallSpeed;
				if (cam.cameraPos.y > camHeightTarget) { fallSpeed += worldScene.gravity / 20f; }
				if (cam.cameraPos.y < camHeightTarget) {
					cam.cameraPos.setY( camHeightTarget );
					fallSpeed = 0;
				}
			}
			if (keys.contains( KeyEvent.VK_UP )) { cam.cameraAngle.addX( 2.5f ); }
			if (keys.contains( KeyEvent.VK_DOWN )) { cam.cameraAngle.addX( -2.5f ); }
			if (keys.contains( KeyEvent.VK_RIGHT )) { cam.cameraAngle.addY( -2.5f ); }
			if (keys.contains( KeyEvent.VK_LEFT )) { cam.cameraAngle.addY( 2.5f ); }
			if (keys.contains( KeyEvent.VK_W )) {
				Vec3f dir = cam.rotMat.multiply( new Vec3f( 0, 0, -speed ) );
				if (gravity) dir.removeY();
				cam.cameraPos = cam.cameraPos.sum( dir );
			}
			if (keys.contains( KeyEvent.VK_S )) {
				Vec3f dir = cam.rotMat.multiply( new Vec3f( 0, 0, speed ) );
				if (gravity) dir.removeY();
				cam.cameraPos = cam.cameraPos.sum( dir );
			}
			if (keys.contains( KeyEvent.VK_A )) {
				cam.cameraPos = cam.cameraPos
						.sum( cam.rotMat.multiply( new Vec3f( -speed, 0, 0 ) ).removeY() );
			}
			if (keys.contains( KeyEvent.VK_D )) {
				cam.cameraPos = cam.cameraPos
						.sum( cam.rotMat.multiply( new Vec3f( speed, 0, 0 ) ).removeY() );
			}
			if (keys.contains( KeyEvent.VK_SPACE )) {
				if (gravity && cam.cameraPos.y == camHeightTarget) {
					fallSpeed = -2;
				} else {
					cam.cameraPos.y += speed;
				}
			}
			if (keys.contains( KeyEvent.VK_CONTROL )) {
				camHeightTarget = 3;
				if (gravity) {
					speed = 0.2f;
				} else {
					cam.cameraPos.y -= speed;
				}
			} else {
				camHeightTarget = 5;
				speed = 1f;
			}
			if (keys.contains( KeyEvent.VK_SHIFT )) { speed = 2.5f; }
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

	}
}
