package net.wolfgangts.render;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.ldsracing.LDSRacing;

public class GUIToolTip {
	private static boolean showHint = false;
	private static String hint = "";
	private static long time = 0;

	public static void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		if (showHint) {
			TrueTypeFont currentFont = LDSRacing.font;

			int x = gc.getInput().getMouseX();
			int y = gc.getInput().getMouseY();
			int width = currentFont.getWidth(hint);
			int height = currentFont.getHeight();

			g.setFont(currentFont);
			g.setColor(new Color(255, 255, 255, 255));
			g.fillRect(x - 5, y - height - 10, width + 10, height + 10);
			g.setColor(new Color(0, 0, 0, 255));
			g.drawString(hint, x, y - height - 5);
		}

	}

	public static void setHint(String hint2) {
		showHint = true;
		hint = hint2;
		time = System.currentTimeMillis();
	}

	private static float timeDelay = 0.125f;

	public static void update() {
		if (System.currentTimeMillis() - time >= timeDelay * 1000) {
			showHint = false;
		}
	}
}
