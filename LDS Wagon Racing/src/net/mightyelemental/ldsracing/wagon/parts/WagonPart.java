package net.mightyelemental.ldsracing.wagon.parts;

import org.newdawn.slick.Image;

import net.mightyelemental.ldsracing.LDSRacing;

public class WagonPart {

	protected Image	displayImage	= LDSRacing.NULL_IMAGE;
	protected int	type, xRelativeToWagon, yRelativeToWagon;

	public Image getDisplayImage() {
		return displayImage;
	}

	public WagonPart setDisplayImage(String location) {
		this.displayImage = LDSRacing.resLoader.loadImage(location);
		return this;
	}

	public int getType() {
		return type;
	}

	public WagonPart setType(int type) {
		this.type = type;
		return this;
	}

	public int getxRelativeToWagon() {
		return xRelativeToWagon;
	}

	public float getScaledXToWagon(float scale) {
		return xRelativeToWagon * scale;
	}

	public float getScaledYToWagon(float scale) {
		return yRelativeToWagon * scale;
	}

	public WagonPart setxRelativeToWagon(int xRelativeToWagon) {
		this.xRelativeToWagon = xRelativeToWagon;
		return this;
	}

	public int getyRelativeToWagon() {
		return yRelativeToWagon;
	}

	public WagonPart setyRelativeToWagon(int yRelativeToWagon) {
		this.yRelativeToWagon = yRelativeToWagon;
		return this;
	}

}
