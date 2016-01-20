package net.mighty.control;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Control {

	public Control() {
		try {
			img = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("net/mighty/control/teddy_bear.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean connected = true;

	public static int fps = 1;

	public static Input	in		= new Input();
	public static Frame	frame	= new Frame();

	public static void main(String[] args) {
		new Control();
	}

	public static BufferedImage capture;

	public transient static BufferedImage img;

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	public static byte[] imgToBytes(BufferedImage img) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, "jpg", baos);
		baos.flush();
		byte[] buffer = baos.toByteArray();
		return buffer;
	}

	public static int[] imgToRGB(BufferedImage img) throws IOException {
		int[] buffer = new int[img.getHeight() * img.getWidth() + 2];
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				buffer[(y * img.getWidth()) + x + 2] = img.getRGB(x, y);
			}
		}
		buffer[0] = img.getWidth();
		buffer[1] = img.getHeight();
		return buffer;
	}

	public static BufferedImage bytesToImg(byte[] bytes) throws IOException {
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = ImageIO.read(in);
		return img;
	}

	/** The first two ints in the array must be the width and height */
	public static BufferedImage rgbToImg(int[] ints) throws IOException {
		if (ints.length < 3) { return null; }
		BufferedImage bi = new BufferedImage(ints[0], ints[1], BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < ints[0]; x++) {
			for (int y = 0; y < ints[1]; y++) {
				bi.setRGB(x, y, ints[(y * ints[0]) + x + 1]);
			}
		}
		return img;
	}

}
