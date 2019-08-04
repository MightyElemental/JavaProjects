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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.mightyelemental.maven.RayTraceTest2.objects.CameraModel;
import net.mightyelemental.maven.RayTraceTest2.objects.Circle;
import net.mightyelemental.maven.RayTraceTest2.objects.ComplexRenderable;
import net.mightyelemental.maven.RayTraceTest2.objects.Light;
import net.mightyelemental.maven.RayTraceTest2.objects.Plane;
import net.mightyelemental.maven.RayTraceTest2.objects.Renderable;
import net.mightyelemental.maven.RayTraceTest2.objects.Scene;
import net.mightyelemental.maven.RayTraceTest2.objects.Sphere;
import net.mightyelemental.maven.RayTraceTest2.objects.Triangle;

/**
 * Hello world!
 *
 */
public class App implements KeyListener, MouseWheelListener, Runnable {

	public Thread thread = new Thread(this);

	boolean pause = false;

	public App() {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("A ray tracer");
		window.setSize(screen.getWidth(), screen.getHeight());
		window.add(pan);
		window.setVisible(true);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.addKeyListener(this);
		setupScene();
		// lights.get(0).color = new Vector3f(1,0.5f,1);
		int ticks = 0;
		long timeOff = 0;
		thread.start();
		while (true) {
			long t1 = System.currentTimeMillis();
			// cam.moveTo((float) Math.sin(ticks / 80f) * 10 + 3, 10, 10);
			// lights.get(0).pos.setX((float) Math.sin(ticks / 60f) * 500);
			// lights.get(0).pos.setY((float) Math.cos(ticks / 60f) * 500);
			s5.center.setX((float) Math.sin(ticks / 30f) * 10);
			s5.center.setZ((float) Math.cos(ticks / 30f) * 10);

			try {
				render();
				Thread.sleep(5 - timeOff > 0 ? Math.min(timeOff, 5) : 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ticks++;
			timeOff += System.currentTimeMillis() - t1 - 5;
			// if ( ticks % 20 == 0 ) {
			// System.out.println(timeOff / 1000f);
			// }
			frametimes.add(System.currentTimeMillis() - t1);
			while (pause) {
				try {
					render();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	Sphere s5 = new Sphere(new Vector3f(7, 4, 2), 1);
	CameraModel camS = new CameraModel(new Vector3f(0, 0, 0));

	public void setupScene() {
		// objects.add(new Sphere(0.1f));
		Sphere s = new Sphere(new Vector3f(0, 0, 0), 6);
		s.col = new Vector3f(1, 0, 1);
		s.setMaterial(0f, 0.3f, 1.52f);
//		s.opacity = 0.3f;
//		s.reflectivity = 0f;
//		s.ior = 1.52f;//4f / 3f;
		worldScene.add(s);

		Sphere s2 = new Sphere(new Vector3f(0, 5, 12), 4);
		s2.col = new Vector3f(1, 0, 1);
		s2.setMaterial(0.2f, 1, 1);
		// worldScene.add(s2);

		Sphere s3 = new Sphere(new Vector3f(8, 7.5f, -15), 7);
		s3.col = new Vector3f(1, 0, 0);
		// s3.reflectivity = 0f;
		// worldScene.add(s3);

		Sphere s4 = new Sphere(new Vector3f(-13, 3, -20), 5);
		s4.col = new Vector3f(0.5f, 0, 1);
		s4.setMaterial(0.8f, 1, 1);
		// worldScene.add(s4);

		Sphere earth = new Sphere(new Vector3f(0, -6371000, 0), 6371000);
		earth.col = new Vector3f(67, 109, 7).mul(1f / 255f);

		// worldScene.add(earth);

		Plane p = new Plane(new Vector3f(0, 1, 0), new Vector3f(0, -2, 0));
		p.reflectivity = 0;
		p.col = new Vector3f(255, 109, 0).mul(1f / 255f);
		worldScene.add(p);

		Circle c = new Circle(new Vector3f(0.5f, 1, 0).normalize(), new Vector3f(0, 40, 0), 20);
		worldScene.add(c);

		Triangle t = new Triangle(new Vector3f(5, 0, 0), new Vector3f(5, 10, 0), new Vector3f(10, 10, 0));
		Triangle t2 = new Triangle(new Vector3f(5, 0, 0), new Vector3f(10, 0, 0), new Vector3f(10, 10, 0));
		List<Renderable> vec = new ArrayList<Renderable>();
		vec.add(t);
		vec.add(t2);
		ComplexRenderable comp = new ComplexRenderable(vec);
		comp.setMaterial(0f,0.1f,5f);
		worldScene.add(comp);

		// worldScene.add(new Plane(new Vector3f(1, 0, 0), new Vector3f(20, 0, 0)));

		s5.col = new Vector3f(0, 1, 0);
		worldScene.add(s5);

		worldScene.add(camS);
		cam.setCamObj(camS);

		// worldScene.add(new Tube(new Vector3f(0, 1, 0), new Vector3f(0, 2, 4), 0, 2));

		worldScene.add(new Light(1000, 500, 400));
		// lights.add(new Light(10, 20, 3));
		// Sphere ls = new Sphere(lights.get(0).pos, 9f);
		// ls.col = new Vector3f(1, 1, 0f);
		// worldScene.add(ls);
	}

	private List<Long> frametimes = new ArrayList<Long>();

	public static final int FPS_TARGET = 30;

	public static int MAX_RAY_DEPTH = 3;

	public JFrame window = new JFrame();
	public JPanel pan = new JPanel() {
		private static final long serialVersionUID = -3107887887785696282L;

		public void paint(Graphics g) {
			// ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			// RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(screen, 0, 0, 1280, 720, null);
			String text = "FPS: " + round(getAvgFPS(), 2);
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 72, 50);
			g.setColor(Color.BLACK);
			g.drawString(text, 5, 20);

			if (pause) {
				g.drawString("PAUSE", 5, 40);
			} else if (gravity) {
				g.drawString("GRAV ON", 5, 40);
			}
		}
	};

	public BufferedImage screen = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);

	public Camera cam = new Camera(new Vector3f(3, 10, 25), 95, screen.getWidth(), screen.getHeight());

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

	boolean t1Done = false;

	private void hqRender() throws InterruptedException {
		// cam.width = 3840;
		// cam.height = 2160;
		pause = true;
		Thread.sleep(1);
		BufferedImage img = new BufferedImage(3840, 2160, BufferedImage.TYPE_INT_RGB);
		twoThreadRender(img);
		try {
			String date = (new Date()).toString().replaceAll(" ", "_").replaceAll(":", ".");
			new File("./imgs/renders/").mkdirs();
//			File outputfile = new File("./imgs/renders/render_" + date + ".jpg");
//			ImageIO.write(img, "jpg", outputfile);
			saveImage(img, "./imgs/renders/render_" + date + ".jpg", 0.9f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		pause = false;
	}

	public void saveImage(BufferedImage img, String location, float quality) throws FileNotFoundException, IOException {
		JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
		jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpegParams.setCompressionQuality(quality);

		final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
		// specifies where the jpg image has to be written
		writer.setOutput(new FileImageOutputStream(new File(location)));

		// writes the file with given compression level
		// from your JPEGImageWriteParam instance
		writer.write(null, new IIOImage(img, null, null), jpegParams);
	}

	public void render() throws InterruptedException {
		if (getAvgFPS() < FPS_TARGET * 2f) {
			dynamicRender();
		} else {
			twoThreadRender(screen);
		}
		// antiAllias();
		Thread.sleep(1);
		pan.repaint();
		// window.repaint();
	}

	public BufferedImage twoThreadRender(BufferedImage target) throws InterruptedException {
		float oldWid = cam.width;
		float oldHeight = cam.height;
		cam.width = target.getWidth();
		cam.height = target.getHeight();

		int[] pixels = ((DataBufferInt) target.getRaster().getDataBuffer()).getData();

		Thread t1 = new Thread("t1") {
			public void run() {
				t1Done = false;
				for (int x = 1; x < target.getWidth(); x += 2) {
					for (int y = 0; y < target.getHeight(); y++) {
						Ray r = cam.createRay(x, y);
						int c = getIntFromVector(trace(r, 0));
						setPixel(pixels, target.getWidth(), x, y, c);
						// System.out.println(c);
					}
				}
				t1Done = true;
			}
		};
		t1.start();
		for (int x = 0; x < target.getWidth(); x += 2) {
			for (int y = 0; y < target.getHeight(); y++) {
				Ray r = cam.createRay(x, y);
				int c = getIntFromVector(trace(r, 0));
				setPixel(pixels, target.getWidth(), x, y, c);
				// System.out.println(c);
			}
		}
		t1.join();
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
				Ray r = cam.createRay(x, y);
				int c = getIntFromVector(trace(r, 0));
				setPixel(x, y, c);
				// System.out.println(c);
			}
		}
		toggle = (++toggle) % 4;
	}

	private void dynamicRender() {
		int fps = (int) Math.round(getAvgFPS());
		int step = pause ? 1 : (FPS_TARGET - fps > 1 ? FPS_TARGET - fps : 1);

		int width = screen.getWidth();
		int height = screen.getHeight();

		Thread t1 = new Thread("t1") {
			public void run() {
				t1Done = false;
				for (int x = 0; x < width; x += step) {
					for (int y = 0; y < height / 2; y += step) {
						Ray r = cam.createRay(x, y);
						int c = getIntFromVector(trace(r, 0));
						for (int i = 0; i < step; i++) {
							for (int j = 0; j < step; j++) {
								setPixel(x + i, y + j, c);
							}
						}
					}
				}
				t1Done = true;
			}
		};
		t1.start();

		for (int x = 0; x < width; x += step) {
			for (int y = height / 2; y < height; y += step) {
				Ray r = cam.createRay(x, y);
				int c = getIntFromVector(trace(r, 0));
				for (int i = 0; i < step; i++) {
					for (int j = 0; j < step; j++) {
						setPixel(x + i, y + j, c);
					}
				}
				// System.out.println(c);
			}
		}
		try {
			t1.join();
			while (!t1Done) {
				Thread.sleep(1);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void updateScreenSize(int width, int height) {
		if (width == screen.getWidth() && height == screen.getHeight())
			return;
		screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		cam.setDim(width, height);
		pixelGrid = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
	}

	public Vector3f trace(Ray r, int depth) {
		Renderable rend = r.trace(worldScene.objectList, depth);
		if (rend == null)
			return getBackground(r.getDirection());
		Vector3f hit = r.getHitPoint();
		Vector3f color = new Vector3f(0, 0, 0);
		Vector3f base = getDiffuse(rend, hit, depth);
		if (depth < MAX_RAY_DEPTH && rend.getMaterial().getReflectivity() > 0 && rend.getMaterial().getOpacity() < 1) {
			Vector3f ref = traceReflection(r, rend, hit, depth);
			Vector3f adjustedRef = ref
					.mul(rend.getMaterial().getOpacity() * (1 - ambientCoeff) * rend.getMaterial().getReflectivity());
			// refraction
			Vector3f refractionCol = traceRefraction(r, rend, hit, depth);
			Vector3f adjustedRefrac = refractionCol.mul((1 - rend.getMaterial().getOpacity()) * (1 - ambientCoeff));

			color = base
					.mul(ambientCoeff * rend.getMaterial().getOpacity() * (1 - rend.getMaterial().getReflectivity()))
					.sum(adjustedRef).sum(adjustedRefrac);
		} else if (depth < MAX_RAY_DEPTH && rend.getMaterial().getReflectivity() > 0) { // reflection
			Vector3f ref = traceReflection(r, rend, hit, depth);
			Vector3f adjustedRef = ref.mul((1 - ambientCoeff) * rend.getMaterial().getReflectivity());

			color = base.mul(ambientCoeff * (1 - rend.getMaterial().getReflectivity())).sum(adjustedRef);
		} else if (depth < MAX_RAY_DEPTH && rend.getMaterial().getOpacity() < 1) { // refraction
			Vector3f refractionCol = traceRefraction(r, rend, hit, depth);
			Vector3f adjustedRefrac = refractionCol.mul((1 - rend.getMaterial().getOpacity()) * (1 - ambientCoeff));

			color = base.mul(ambientCoeff * rend.getMaterial().getOpacity()).sum(adjustedRefrac);
		} else {
			color = getDiffuse(rend, hit, depth);
		}
		return color;
	}

	public Vector3f traceReflection(Ray r, Renderable rend, Vector3f hit, int depth) {
		Vector3f dir = r.getReflectedVector(rend.getNormal(hit)).normalize();
		Ray refr = new Ray(dir, hit.sum(dir.mul(0.01f)));
		return trace(refr, depth + 1);
	}

	public Vector3f traceRefraction(Ray r, Renderable rend, Vector3f hit, int depth) {
		Vector3f refracDir = r.getRefractionVector(1f / rend.getMaterial().getIOR(), rend.getNormal(hit).normalize())
				.normalize();
		Ray refractionRay = new Ray(refracDir, hit.sum(refracDir.mul(0.01f)));
		return trace(refractionRay, depth + 1);
	}

	private Vector3f getBackground(Vector3f frag) {
		return convertVecToCubeUV(frag).getColor();
	}

	public TexData convertVecToCubeUV(Vector3f vec) {
		TexData td = new TexData();
		float absX = Math.abs(vec.x);
		float absY = Math.abs(vec.y);
		float absZ = Math.abs(vec.z);

		boolean isXPositive = vec.x > 0;
		boolean isYPositive = vec.y > 0;
		boolean isZPositive = vec.z > 0;

		float maxAxis = 1, uc = 0, vc = 0;

		if (isXPositive && absX >= absY && absX >= absZ) {
			maxAxis = absX;
			uc = -vec.z;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(worldScene.getSkybox() + "side_0.png");
		}
		if (!isXPositive && absX >= absY && absX >= absZ) {
			maxAxis = absX;
			uc = vec.z;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(worldScene.getSkybox() + "side_1.png");
		}
		if (isYPositive && absY >= absX && absY >= absZ) {
			maxAxis = absY;
			uc = vec.x;
			vc = -vec.z;
			td.img = ResourceLoader.loadImage(worldScene.getSkybox() + "side_2.png");
		}
		if (!isYPositive && absY >= absX && absY >= absZ) {
			maxAxis = absY;
			uc = vec.x;
			vc = vec.z;
			td.img = ResourceLoader.loadImage(worldScene.getSkybox() + "side_3.png");
		}
		if (isZPositive && absZ >= absX && absZ >= absY) {
			maxAxis = absZ;
			uc = vec.x;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(worldScene.getSkybox() + "side_4.png");
		}
		if (!isZPositive && absZ >= absX && absZ >= absY) {
			maxAxis = absZ;
			uc = -vec.x;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(worldScene.getSkybox() + "side_5.png");
		}
		td.uvX = 0.5f * (uc / maxAxis + 1f);
		td.uvY = 0.5f * (vc / maxAxis + 1.0f);
		return td;
	}

	public static Vector3f rgbIntToVec(int col) {
		Color c = new Color(col);
		float r = c.getRed() / 255f;
		float g = c.getGreen() / 255f;
		float b = c.getBlue() / 255f;
		return new Vector3f(r, g, b);
	}

	private Vector3f getDiffuse(Renderable rend, Vector3f hit, int depth) {
		Vector3f color = rend.getColor().mul(ambientCoeff);
		Vector3f lightSamples = new Vector3f(0, 0, 0);
		for (Light l : worldScene.lightList) {
			Vector3f lvec = hit.vecTo(l.pos).normalize();
			Ray shadowRay = new Ray(lvec, hit.sum(lvec.mul(0.01f)));
			Renderable obj = shadowRay.trace(worldScene.objectList, depth);
			if (obj == null || (obj instanceof Sphere && ((Sphere) obj).center.equals(l.pos))) {// TODO: fix sun shadow
				float dot = rend.getNormal(hit).getUnitVec().dot(lvec);
				if (dot > 0)
					lightSamples = lightSamples.sum(l.color.mul(l.brightness * dot));
				// System.out.println("asd");
				break;
			}
		}
		return color.sum(lightSamples.mul((1 - ambientCoeff) / worldScene.lightList.size()));
	}

	private int getIntFromVector(Vector3f col) {
		return getIntFromColor((int) (col.x * 255), (int) (col.y * 255), (int) (col.z * 255));
	}

	public void setPixel(int x, int y, int col) {
		if (x >= screen.getWidth() || y >= screen.getHeight())
			return;
		pixelGrid[(int) (x + y * screen.getWidth())] = col;
	}

	private void setPixel(int x, int y, Vector3f vec) {
		setPixel(x, y, getIntFromVector(vec));
	}

	private void setPixel(int[] pixels, int width, int x, int y, int c) {
		pixels[(int) (x + y * width)] = c;
	}

	public int getPixel(int x, int y) {
		return pixelGrid[(int) (x + y * screen.getWidth())];
	}

	public double round(double value, int precision) {
		double mul = Math.pow(10, precision);
		return Math.round(value * mul) / mul;
	}

	public void antiAllias() {// blurs screen
		for (int x = 0; x < screen.getWidth(); x++) {
			for (int y = 0; y < screen.getHeight(); y++) {
				int samples = 0;
				Vector3f colTot = Vector3f.origin();
				if (x > 1) {
					samples += 1;
					colTot = colTot.sum(rgbIntToVec(getPixel(x - 1, y)));
				}
				if (y > 1) {
					samples += 1;
					colTot = colTot.sum(rgbIntToVec(getPixel(x, y - 1)));
				}
				if (x < screen.getWidth() - 1) {
					samples += 1;
					colTot = colTot.sum(rgbIntToVec(getPixel(x + 1, y)));
				}
				if (y < screen.getHeight() - 1) {
					samples += 1;
					colTot = colTot.sum(rgbIntToVec(getPixel(x, y + 1)));
				}
				colTot = colTot.sum(rgbIntToVec(getPixel(x, y)));
				setPixel(x, y, colTot.mul(1f / samples));
			}
		}
	}

	public synchronized double getAvgFPS() {
		OptionalDouble od = OptionalDouble.empty();
		try {
			od = frametimes.stream().mapToLong(Long::longValue).average();
		} catch (NullPointerException | ConcurrentModificationException e) {
			// ignore all
		}
		return 1000f / od.orElse(FPS_TARGET + 1);
	}

	Set<Integer> keys = new HashSet<Integer>();

	@Override
	public void keyPressed(KeyEvent e) {
		keys.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent event) {
		keys.removeIf(e -> e == event.getKeyCode());
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

	public boolean gravity = false;

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void run() {
		float fallSpeed = 0;
		float grav = 9.8f;
		float camHeightTarget = 5;
		float speed = 1;
		while (true) {
			while (frametimes.size() > 200) {
				frametimes.remove(0);
			}
			if (!pause) {
				if (gravity) {
					cam.cameraPos.y -= fallSpeed;
					if (cam.cameraPos.y > camHeightTarget) {
						fallSpeed += grav / 20f;
					}
					if (cam.cameraPos.y < camHeightTarget) {
						cam.cameraPos.setY(camHeightTarget);
						fallSpeed = 0;
					}
				}
				if (keys.contains(KeyEvent.VK_UP)) {
					cam.cameraAngle.addX(2.5f);
				}
				if (keys.contains(KeyEvent.VK_DOWN)) {
					cam.cameraAngle.addX(-2.5f);
				}
				if (keys.contains(KeyEvent.VK_RIGHT)) {
					cam.cameraAngle.addY(-2.5f);
				}
				if (keys.contains(KeyEvent.VK_LEFT)) {
					cam.cameraAngle.addY(2.5f);
				}
				if (keys.contains(KeyEvent.VK_W)) {
					Vector3f dir = cam.rotMat.multiply(new Vector3f(0, 0, -speed));
					if (gravity)
						dir.removeY();
					cam.cameraPos = cam.cameraPos.sum(dir);
				}
				if (keys.contains(KeyEvent.VK_S)) {
					Vector3f dir = cam.rotMat.multiply(new Vector3f(0, 0, speed));
					if (gravity)
						dir.removeY();
					cam.cameraPos = cam.cameraPos.sum(dir);
				}
				if (keys.contains(KeyEvent.VK_A)) {
					cam.cameraPos = cam.cameraPos.sum(cam.rotMat.multiply(new Vector3f(-speed, 0, 0)).removeY());
				}
				if (keys.contains(KeyEvent.VK_D)) {
					cam.cameraPos = cam.cameraPos.sum(cam.rotMat.multiply(new Vector3f(speed, 0, 0)).removeY());
				}
				if (keys.contains(KeyEvent.VK_SPACE)) {
					if (gravity && cam.cameraPos.y == camHeightTarget) {
						fallSpeed = -2;
					} else {
						cam.cameraPos.y += speed;
					}
				}
				if (keys.contains(KeyEvent.VK_CONTROL)) {
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
				if (keys.contains(KeyEvent.VK_SHIFT)) {
					speed = 2.5f;
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

	}
}
