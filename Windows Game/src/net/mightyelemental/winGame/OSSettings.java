package net.mightyelemental.winGame;

import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

public class OSSettings {

	private OSSettings() {
	}

	public static final int FILE_DISPLAY_SIZE = 32;
	public static TrueTypeFont FILE_FONT = new TrueTypeFont(new Font("New Times Romans", Font.PLAIN, 10), true);
}
