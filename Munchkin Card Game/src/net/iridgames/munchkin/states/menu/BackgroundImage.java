package net.iridgames.munchkin.states.menu;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class BackgroundImage {

	private Image image;

	private long time;

	private float	posX	= 0;
	private float	posY	= 0;

	private float	width;
	private float	height;

	private boolean dead = false;

	private float	alpha	= 0.1f;
	private Color	tint	= new Color(150, 150, 150, alpha);

	private int direction = -1;

	private boolean alphaPeaked;

	public BackgroundImage( Image img, int time, Random rand, int screenWidth, int screenHeight ) {
		if (img == null) {
			dead = true;
			return;
		}
		this.posX = rand.nextInt(screenWidth);
		this.posY = rand.nextInt(screenHeight);
		this.image = img;
		this.width = img.getWidth() * 1f;
		this.height = img.getHeight() * 1f;
		this.time = time;
		direction = rand.nextInt(7);
	}

	/** @param g
	 *            the graphics instance */
	public void draw(Graphics g) {
		if (dead == true) { return; }
		image.draw(posX, posY, width, height, tint);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (dead == true) { return; }
		posX += 0.7f * (delta / 17);
		if (alpha >= 0.2) {
			alphaPeaked = true;
		}
		if (alphaPeaked) {
			alpha -= ((1f / 52f) / (time / 2)) * (delta / 17);
		} else {
			alpha += ((1f / 30f) / (time / 2)) * (delta / 17);
		}
		tint.a = alpha;
		switch (direction) {
			case 0:
				posX += 0.7f * (delta / 17);
				break;
			case 1:
				posX += 0.7f * (delta / 17);
				posY += 0.7f * (delta / 17);
				break;
			case 2:
				posY += 0.7f * (delta / 17);
				break;
			case 3:
				posY += 0.7f * (delta / 17);
				posX -= 0.7f * (delta / 17);
				break;
			case 4:
				posX -= 0.7f * (delta / 17);
				break;
			case 5:
				posX -= 0.7f * (delta / 17);
				posY -= 0.7f * (delta / 17);
				break;
			case 6:
				posY -= 0.7f * (delta / 17);
				break;
			case 7:
				posX += 0.7f * (delta / 17);
				posY -= 0.7f * (delta / 17);
				break;
			default:
				posX -= 0.7f * (delta / 17);
				break;
		}
		if (posX + width < 0 | posX > gc.getWidth()) {
			dead = true;
		}
		if (posY + height < 0 | posY > gc.getHeight()) {
			dead = true;
		}

		if (alpha <= 0) {
			dead = true;
		}
	}

	/** @return the dead */
	public boolean isDead() {
		return dead;
	}

}
