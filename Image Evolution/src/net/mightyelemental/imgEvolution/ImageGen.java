package net.mightyelemental.imgEvolution;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageGen {

	public Image img;

	public int imgSize = 64;

	public int[] pixels;

	private Graphics imgGraphics;

	public boolean imgBeenEdited;

	public ImageGen(Image in) throws SlickException {
		imgSize = in.getWidth();
		pixels = new int[imgSize * imgSize];
		this.img = in.getScaledCopy(imgSize, imgSize);
		System.out.println(this.img.getWidth());
		imgGraphics = this.img.getGraphics();
		for ( int i = 0; i < pixels.length; i++ ) {
			Color c = imgGraphics.getPixel(i % imgSize, i / imgSize);
			int grey = (c.getBlue() + c.getGreen() + c.getRed()) / 3;
			pixels[i] = grey;
		}
		imgBeenEdited = true;
	}

	public ImageGen(ImageGen ig) throws SlickException {
		this.pixels = ig.pixels.clone();
		this.img = ig.img.copy();
		this.imgSize = ig.imgSize;
		this.imgGraphics = this.img.getGraphics();
		imgBeenEdited = true;
	}

	public ImageGen(int size) throws SlickException {
		imgSize = size;
		pixels = new int[imgSize * imgSize];
		img = new Image(imgSize, imgSize);
		imgGraphics = img.getGraphics();
	}

	public boolean setPixels(int[] pix) {
		if ( pix.length == pixels.length ) {
			pixels = pix.clone();
			imgBeenEdited = true;
			return true;
		} else {
			System.err.println("ARRAYS ARE NOT THE SAME SIZE");
		}
		return false;
	}

	public void setPixel(int x, int y, int grey) {
		if ( imgGraphics == null ) return;
		imgGraphics.setColor(new Color(grey, grey, grey));
		imgGraphics.drawRect(x, y, 1, 1);
		pixels[x + y * imgSize] = grey;
		imgBeenEdited = true;
	}

	public int getSum() {
		int temp = 0;
		for ( int i : pixels ) {
			temp += i;
		}
		return temp;
	}

	public Image getImage() throws SlickException {
		if ( !imgBeenEdited ) return img;
		imgGraphics.setAntiAlias(false);
		for ( int i = 0; i < pixels.length; i++ ) {
			if ( pixels[i] != ImageEvolution.target.pixels[i] ) {
				imgGraphics.setColor(new Color(pixels[i], 0, 0));
			} else {
				imgGraphics.setColor(new Color(pixels[i], pixels[i], pixels[i]));
			}
			imgGraphics.drawRect(i % imgSize, imgSize - 1 - (i / imgSize), 1, 1);
		}
		imgBeenEdited = false;
		return img;
	}

}
