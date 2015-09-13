package net.mightyelemental.ldsracing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;

/** @author MightyElemental
 * @since 28/10/2014 */
public class ResourceLoader {

	private Map<String, Image>	mapLoads	= new HashMap<String, Image>();
	private Map<String, Sound>	soundLoads	= new HashMap<String, Sound>();
	private Map<String, Music>	musicLoads	= new HashMap<String, Music>();

	/** Loads an image from the 'assets/textures' package
	 * 
	 * @param imagePath
	 *            the path to the image beginning with 'assets/textures'. Remember that you can replace slashes '/' with
	 *            dots '.'
	 * @return Image the newly loaded image */
	public Image loadImage(String imagePath) {

		Image loadedImage = LDSRacing.NULL_IMAGE;

		String location = imagePath.replaceAll("[.]", "/");
		location += ".png";
		location = "assets/textures/" + location;
		if (mapLoads.get(location) != null) {
			return mapLoads.get(location);
		} else {
			try {
				// loadedImage = new Image(location);
				File temp = new File(this.getClass().getClassLoader().getResource(location).toURI());
				if (temp.exists()) {
					loadedImage = new Image(this.getClass().getClassLoader().getResourceAsStream(location), location, false,
							Image.FILTER_NEAREST);
					System.out.println("Added texture\t'" + location + "'");
				} else {
					throw new Exception("Missing texture\t'" + location + "'");
				}
			} catch (Exception e) {
				System.err.println("Missing texture\t'" + location + "'");
			}
			mapLoads.put(location, loadedImage);
		}

		return loadedImage;
	}

	/** Loads a music file from the 'assets/sounds/music' package
	 * 
	 * @param musicPath
	 *            the path to the sound file beginning with 'assets/sounds/music'. Remember that you can replace slashes
	 *            '/' with dots '.'
	 * @return Music the newly loaded music file */
	public Music loadMusic(String musicPath) {

		Music loadedMusic = null;

		String location = musicPath.replaceAll("[.]", "/");
		location += ".ogg";
		location = "assets/sounds/music/" + location;
		if (mapLoads.get(location) != null) {
			return musicLoads.get(location);
		} else {
			try {

				File temp = new File(this.getClass().getClassLoader().getResource(location).toURI());
				if (temp.exists()) {
					loadedMusic = new Music(this.getClass().getClassLoader().getResourceAsStream(location), location);
					System.out.println("Added music\t'" + location + "'");
				} else {
					throw new Exception("Missing music\t'" + location + "'");
				}
			} catch (Exception e) {
				System.err.println("Missing music\t'" + location + "'");
			}
			musicLoads.put(location, loadedMusic);
		}

		return loadedMusic;
	}

	/** Loads a sound file from the 'assets/sounds' package
	 * 
	 * @param soundPath
	 *            the path to the sound file beginning with 'assets/sounds'. Remember that you can replace slashes '/'
	 *            with dots '.'
	 * @return Sound the newly loaded sound */
	public Sound loadSound(String soundPath) {

		Sound loadedSound = null;

		String location = soundPath.replaceAll("[.]", "/");
		location += ".ogg";
		location = "assets/sounds/" + location;
		if (mapLoads.get(location) != null) {
			return soundLoads.get(location);
		} else {
			try {
				File temp = new File(this.getClass().getClassLoader().getResource(location).toURI());
				if (temp.exists()) {
					loadedSound = new Sound(this.getClass().getClassLoader().getResourceAsStream(location), location);
					System.out.println("Added sound\t'" + location + "'");
				} else {
					throw new Exception("Missing sound\t'" + location + "'");
				}
			} catch (Exception e) {
				System.out.println("Missing sound\t'" + location + "'");
			}
			soundLoads.put(location, loadedSound);
		}

		return loadedSound;
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
