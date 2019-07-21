package net.mightyelemental.maven.RayTraceTest2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.mightyelemental.maven.RayTraceTest2.objects.CameraModel;
import net.mightyelemental.maven.RayTraceTest2.objects.Light;
import net.mightyelemental.maven.RayTraceTest2.objects.Plane;
import net.mightyelemental.maven.RayTraceTest2.objects.Renderable;
import net.mightyelemental.maven.RayTraceTest2.objects.Sphere;

/**
 * Hello world!
 *
 */
public class App implements KeyListener, MouseWheelListener, Runnable {

	public Thread thread = new Thread(this);

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
		}
	}

	Sphere		s5		= new Sphere(new Vector3f(7, 4, 2), 1);
	CameraModel	camS	= new CameraModel(new Vector3f(0, 0, 0));

	public void setupScene() {
		// objects.add(new Sphere(0.1f));
		Sphere s = new Sphere(new Vector3f(0, 0, 0), 6);
		s.col = new Vector3f(1, 1, 1);
		s.reflectivity = 1;
		objects.add(s);

		Sphere s2 = new Sphere(new Vector3f(0, 5, 12), 4);
		s2.col = new Vector3f(1, 0, 1);
		s2.reflectivity = 0.2f;
		objects.add(s2);

		Sphere s3 = new Sphere(new Vector3f(8, 7.5f, -15), 7);
		s3.col = new Vector3f(1, 0, 0);
		// s3.reflectivity = 0f;
		objects.add(s3);

		Sphere s4 = new Sphere(new Vector3f(-13, 3, -20), 5);
		s4.col = new Vector3f(0.5f, 0, 1);
		s4.reflectivity = 0.8f;
		objects.add(s4);

		Sphere earth = new Sphere(new Vector3f(0, -6371000, 0), 6371000);
		earth.col = new Vector3f(67, 109, 7).mul(1f / 255f);

		objects.add(earth);

		Plane p = new Plane(new Vector3f(0, 1, 0), new Vector3f(0, -2, 0));
		p.reflectivity = 0;
		p.col = new Vector3f(255, 109, 0).mul(1f / 255f);
		// objects.add(p);

		// objects.add(new Plane(new Vector3f(1, 0, 0), new Vector3f(20, 0, 0)));

		s5.col = new Vector3f(0, 1, 0);
		objects.add(s5);

		objects.add(camS);
		cam.setCamObj(camS);

		// objects.add(new Tube(new Vector3f(0, 1, 0), new Vector3f(0, 2, 4), 0, 2));

		lights.add(new Light(1000, 500, 400));
		// lights.add(new Light(10, 20, 3));
		// Sphere ls = new Sphere(lights.get(0).pos, 9f);
		// ls.col = new Vector3f(1, 1, 0f);
		// objects.add(ls);
	}

	private List<Long> frametimes = new ArrayList<Long>();

	public static final int FPS_TARGET = 30;

	public static int MAX_RAY_DEPTH = 3;

	public JFrame	window	= new JFrame();
	public JPanel	pan		= new JPanel() {
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
									if ( gravity ) g.drawString("GRAV ON", 5, 40);
								}
							};

	public BufferedImage screen = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);

	public Camera cam = new Camera(new Vector3f(3, 100, 25), 95, screen.getWidth(), screen.getHeight());

	public List<Renderable>	objects	= new ArrayList<Renderable>();
	public List<Light>		lights	= new ArrayList<Light>();

	public Vector3f	backgroundColor	= new Vector3f(119 / 255f, 181 / 255f, 254 / 255f);
	public String	skyboxLocation	= "./imgs/skybox/";

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

	public void render() throws InterruptedException {
		if ( getAvgFPS() < FPS_TARGET * 2f ) {
			dynamicRender();
		} else {
			Thread t1 = new Thread("t1") {
				public void run() {
					t1Done = false;
					for ( int x = 1; x < screen.getWidth(); x += 2 ) {
						for ( int y = 0; y < screen.getHeight(); y++ ) {
							Ray r = cam.createRay(x, y);
							int c = getIntFromVector(trace(r, 0));
							setPixel(x, y, c);
							// System.out.println(c);
						}
					}
					t1Done = true;
				}
			};
			t1.start();
			for ( int x = 0; x < screen.getWidth(); x += 2 ) {
				for ( int y = 0; y < screen.getHeight(); y++ ) {
					Ray r = cam.createRay(x, y);
					int c = getIntFromVector(trace(r, 0));
					setPixel(x, y, c);
					// System.out.println(c);
				}
			}
			t1.join();
			while (!t1Done) {
				Thread.sleep(1);
			}
		}
		// antiAllias();
		Thread.sleep(1);
		pan.repaint();
		// window.repaint();
	}

	int toggle = 0;

	public void fourWayInterlaceRender() {
		int xs = 0, ys = 0;
		if ( toggle == 0 ) {
			xs = 0;
			ys = 0;
		}
		if ( toggle == 1 ) {
			xs = 1;
			ys = 0;
		}
		if ( toggle == 2 ) {
			xs = 1;
			ys = 1;
		}
		if ( toggle == 3 ) {
			xs = 0;
			ys = 1;
		}
		for ( int x = xs; x < screen.getWidth(); x += 2 ) {
			for ( int y = ys; y < screen.getHeight(); y += 2 ) {
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
		int step = FPS_TARGET - fps > 1 ? FPS_TARGET - fps : 1;

		int width = screen.getWidth();
		int height = screen.getHeight();

		Thread t1 = new Thread("t1") {
			public void run() {
				t1Done = false;
				for ( int x = 0; x < width; x += step ) {
					for ( int y = 0; y < height / 2; y += step ) {
						Ray r = cam.createRay(x, y);
						int c = getIntFromVector(trace(r, 0));
						for ( int i = 0; i < step; i++ ) {
							for ( int j = 0; j < step; j++ ) {
								setPixel(x + i, y + j, c);
							}
						}
					}
				}
				t1Done = true;
			}
		};
		t1.start();

		for ( int x = 0; x < width; x += step ) {
			for ( int y = height / 2; y < height; y += step ) {
				Ray r = cam.createRay(x, y);
				int c = getIntFromVector(trace(r, 0));
				for ( int i = 0; i < step; i++ ) {
					for ( int j = 0; j < step; j++ ) {
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
		if ( width == screen.getWidth() && height == screen.getHeight() ) return;
		screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		cam.setDim(width, height);
		pixelGrid = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
	}

	public Vector3f trace(Ray r, int depth) {
		Renderable rend = r.trace(objects, depth);
		if ( rend == null ) return getBackground(r.getDirection());
		Vector3f hit = r.getHitPoint();
		Vector3f color = new Vector3f(0, 0, 0);
		if ( rend.getReflectivity() > 0 ) {
			if ( depth < MAX_RAY_DEPTH ) {
				Vector3f dir = r.getReflectedVector(rend.getNormal(hit)).normalize();
				Ray refr = new Ray(dir, hit.sum(dir.mul(0.01f)));
				Vector3f ref = trace(refr, depth + 1);
				Vector3f base = getDiffuse(rend, hit, depth).mul(0.3f * (1 - rend.getReflectivity()));
				Vector3f adjustedRef = ref.mul((1 - 0.3f) * rend.getReflectivity());
				color = base.sum(adjustedRef);
			}
		} else {
			color = getDiffuse(rend, hit, depth);
		}
		return color;
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

		if ( isXPositive && absX >= absY && absX >= absZ ) {
			maxAxis = absX;
			uc = -vec.z;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(skyboxLocation + "side_0.png");
		}
		if ( !isXPositive && absX >= absY && absX >= absZ ) {
			maxAxis = absX;
			uc = vec.z;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(skyboxLocation + "side_1.png");
		}
		if ( isYPositive && absY >= absX && absY >= absZ ) {
			maxAxis = absY;
			uc = vec.x;
			vc = -vec.z;
			td.img = ResourceLoader.loadImage(skyboxLocation + "side_2.png");
		}
		if ( !isYPositive && absY >= absX && absY >= absZ ) {
			maxAxis = absY;
			uc = vec.x;
			vc = vec.z;
			td.img = ResourceLoader.loadImage(skyboxLocation + "side_3.png");
		}
		if ( isZPositive && absZ >= absX && absZ >= absY ) {
			maxAxis = absZ;
			uc = vec.x;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(skyboxLocation + "side_4.png");
		}
		if ( !isZPositive && absZ >= absX && absZ >= absY ) {
			maxAxis = absZ;
			uc = -vec.x;
			vc = vec.y;
			td.img = ResourceLoader.loadImage(skyboxLocation + "side_5.png");
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
		for ( Light l : lights ) {
			Vector3f lvec = hit.vecTo(l.pos).normalize();
			Ray shadowRay = new Ray(lvec, hit.sum(lvec.mul(0.01f)));
			Renderable obj = shadowRay.trace(objects, depth);
			if ( obj == null || (obj instanceof Sphere && ((Sphere) obj).center.equals(l.pos)) ) {// TODO: fix sun shadow
				float dot = rend.getNormal(hit).getUnitVec().dot(lvec);
				if ( dot > 0 ) lightSamples = lightSamples.sum(l.color.mul(l.brightness * dot));
				// System.out.println("asd");
				break;
			}
		}
		return color.sum(lightSamples.mul((1 - ambientCoeff) / lights.size()));
	}

	private int getIntFromVector(Vector3f col) {
		return getIntFromColor((int) (col.x * 255), (int) (col.y * 255), (int) (col.z * 255));
	}

	public void setPixel(int x, int y, int col) {
		if ( x >= screen.getWidth() || y >= screen.getHeight() ) return;
		pixelGrid[(int) (x + y * screen.getWidth())] = col;
	}

	private void setPixel(int x, int y, Vector3f vec) {
		setPixel(x, y, getIntFromVector(vec));
	}

	public int getPixel(int x, int y) {
		return pixelGrid[(int) (x + y * screen.getWidth())];
	}

	public double round(double value, int precision) {
		double mul = Math.pow(10, precision);
		return Math.round(value * mul) / mul;
	}

	public void antiAllias() {// blurs screen
		for ( int x = 0; x < screen.getWidth(); x++ ) {
			for ( int y = 0; y < screen.getHeight(); y++ ) {
				int samples = 0;
				Vector3f colTot = Vector3f.origin();
				if ( x > 1 ) {
					samples += 1;
					colTot = colTot.sum(rgbIntToVec(getPixel(x - 1, y)));
				}
				if ( y > 1 ) {
					samples += 1;
					colTot = colTot.sum(rgbIntToVec(getPixel(x, y - 1)));
				}
				if ( x < screen.getWidth() - 1 ) {
					samples += 1;
					colTot = colTot.sum(rgbIntToVec(getPixel(x + 1, y)));
				}
				if ( y < screen.getHeight() - 1 ) {
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
		if ( event.getKeyCode() == KeyEvent.VK_G ) {
			gravity = !gravity;
		}
	}

	public boolean gravity = false;

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

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
			if ( gravity ) {
				cam.cameraPos.y -= fallSpeed;
				if ( cam.cameraPos.y > camHeightTarget ) {
					fallSpeed += grav / 20f;
				}
				if ( cam.cameraPos.y < camHeightTarget ) {
					cam.cameraPos.setY(camHeightTarget);
					fallSpeed = 0;
				}
			}
			if ( keys.contains(KeyEvent.VK_UP) ) {
				cam.cameraAngle.addX(2.5f);
			}
			if ( keys.contains(KeyEvent.VK_DOWN) ) {
				cam.cameraAngle.addX(-2.5f);
			}
			if ( keys.contains(KeyEvent.VK_RIGHT) ) {
				cam.cameraAngle.addY(-2.5f);
			}
			if ( keys.contains(KeyEvent.VK_LEFT) ) {
				cam.cameraAngle.addY(2.5f);
			}
			if ( keys.contains(KeyEvent.VK_W) ) {
				Vector3f dir = cam.rotMat.multiply(new Vector3f(0, 0, -speed));
				if ( gravity ) dir.removeY();
				cam.cameraPos = cam.cameraPos.sum(dir);
			}
			if ( keys.contains(KeyEvent.VK_S) ) {
				Vector3f dir = cam.rotMat.multiply(new Vector3f(0, 0, speed));
				if ( gravity ) dir.removeY();
				cam.cameraPos = cam.cameraPos.sum(dir);
			}
			if ( keys.contains(KeyEvent.VK_A) ) {
				cam.cameraPos = cam.cameraPos.sum(cam.rotMat.multiply(new Vector3f(-speed, 0, 0)).removeY());
			}
			if ( keys.contains(KeyEvent.VK_D) ) {
				cam.cameraPos = cam.cameraPos.sum(cam.rotMat.multiply(new Vector3f(speed, 0, 0)).removeY());
			}
			if ( keys.contains(KeyEvent.VK_SPACE) && cam.cameraPos.y == camHeightTarget ) {
				fallSpeed = -2;
			}
			if ( keys.contains(KeyEvent.VK_CONTROL) ) {
				camHeightTarget = 3;
				speed = 0.2f;
			} else {
				camHeightTarget = 5;
				speed = 1f;
			}
			if ( keys.contains(KeyEvent.VK_SHIFT) ) {
				speed = 2.5f;
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
