package net.mightyelemental.maven.RayTraceTest2;

import java.awt.image.BufferedImage;

public class TexData {

	public BufferedImage	img;
	public float			uvX, uvY;

	public Vector3f getColor() {
		if ( img == null ) return new Vector3f(0, 0, 0);
		if ( uvX < 0 || uvX > 1 ) return new Vector3f(0, 0, 0);
		if ( uvY < 0 || uvY > 1 ) return new Vector3f(0, 0, 0);
		int col = img.getRGB((int) ((uvX) * (img.getWidth() - 1)), (int) ((1-uvY) * (img.getHeight() - 1)));
		return App.rgbIntToVec(col);
	}

}
