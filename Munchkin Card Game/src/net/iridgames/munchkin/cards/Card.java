package net.iridgames.munchkin.cards;

import org.newdawn.slick.Image;

public abstract class Card {

	private String	title	= "UNDEFINED_CARD";
	private String	type	= "UNDEFINED_TYPE";

	public static final String	TYPE_DOOR		= "DOOR";
	public static final String	TYPE_TREASURE	= "TREASURE";

	private Image image;

	public Card( String title, String type ) {
		this.title = title;
		this.type = type;
	}

	/** @return the image */
	public Image getImage() {
		return image;
	}

	/** @param image
	 *            the image to set */
	public Card setImage(Image image) {
		this.image = image;
		return this;
	}

	/** @return the title */
	public String getTitle() {
		return title;
	}

	/** @param title
	 *            the title to set */
	public void setTitle(String title) {
		this.title = title;
	}

	/** @return the type */
	public String getType() {
		return type;
	}

	/** @param type
	 *            the type to set */
	public void setType(String type) {
		this.type = type;
	}

}
