package net.mightyelemental.ldsracing;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;

public class ResourceLoader {

	private Map<String, Image> mapLoads = new HashMap<String, Image>();

	/** Loads an image from the 'res/assets/textures' folder
	 * 
	 * @param imagePath
	 *            the path to the image beginning with 'res/assets/textures'. Remember that you can replace slashes '/'
	 *            with dots '.'
	 * @return Image the newly loaded image */
	public Image loadImage(String imagePath) {

		Image loadedImage = LDSRacing.NULL_IMAGE;

		String location = imagePath.replaceAll("[.]", "/");
		location += ".png";
		if (mapLoads.get(location) != null) {
			return mapLoads.get(location);
		} else {

			try {
				System.out.println("res/assets/textures/" + location);
				loadedImage = new Image("res/assets/textures/" + location);
			} catch (Exception e) {
				System.out.println("CANT LOAD IMAGE '" + location + "'");
			}
			mapLoads.put(location, loadedImage);
		}

		return loadedImage;
	}

	public Image loadImageFromFile(String imagePath) {
		String location = imagePath.replaceAll("[.]", "/");
		Image temp = LDSRacing.NULL_IMAGE;
		try {
			if (mapLoads.get(location) != null) {
				// System.out.println("LOADED ORIGIONAL IMAGE");
				return mapLoads.get(location);
			} else {
				temp = new Image(location);
				// System.out.println("LOADED NEW IMAGE");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

}
