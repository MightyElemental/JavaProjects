package net.mightyelemental.ldsracing.wagon.parts;

import org.newdawn.slick.Image;

public class Cart extends WagonPart {

	@Override
	public Image getDisplayImage() {
		float x = 250;
		float y = x * (128.0f / 364.0f);
		return displayImage.getScaledCopy((int) x, (int) y);
	}

}
