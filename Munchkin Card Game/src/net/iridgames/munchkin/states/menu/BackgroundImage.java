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

	private float	posX	= 200;
	private float	posY	= 200;

	private Color tint = new Color(150, 150, 150, 0.5f);

	private int direction = -1;

	public BackgroundImage( Image img, int time, Random rand ) {
		this.image = img;
		this.time = System.currentTimeMillis() + time * 1000;
		direction = rand.nextInt(7);
	}

	/** @param g
	 *            the graphics instance */
	public void draw(Graphics g) {
		if (System.currentTimeMillis() < time) {
			image.draw(posX, posY, tint);
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		posX += 0.7f * (delta / 17);
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
		}
	}

}
