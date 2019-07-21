package net.mightyelemental.maven.RayTraceTest2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ResourceLoader {

	private static Map<String, BufferedImage> cache = new HashMap<String, BufferedImage>();

	public static BufferedImage loadImage(String loc) {
		if ( cache.containsKey(loc) ) return cache.get(loc);
		BufferedImage img = nullImage();
		File f = new File(loc);
		if ( f.exists() ) {
			try {
				img = ImageIO.read(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		cache.put(loc, img);

		return img;
	}

	public static BufferedImage nullImage() {
		if ( cache.containsKey("null") ) return cache.get("null");
		BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.MAGENTA);
		g.fillRect(0, 0, 1, 1);
		g.fillRect(1, 1, 1, 1);
		g.setColor(Color.BLACK);
		g.fillRect(0, 1, 1, 1);
		g.fillRect(1, 0, 1, 1);
		g.dispose();
		cache.put("null", img);
		return img;
	}

}
