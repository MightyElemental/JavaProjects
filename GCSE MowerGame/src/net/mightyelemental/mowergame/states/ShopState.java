package net.mightyelemental.mowergame.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.MowerGame;

public class ShopState extends BasicGameState {

	public Image background;
	public Image trump;
	public Image sign;

	public final int ID;

	public ShopState(int ID) {
		this.ID = ID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		background = MowerGame.resLoader.loadImage("shop.background").getScaledCopy(gc.getWidth(), gc.getHeight());
		trump = MowerGame.resLoader.loadImage("shop.trumpApproved").getScaledCopy(0.8f);
		sign = MowerGame.resLoader.loadImage("shop.sign").getScaledCopy(0.5f);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(background, 0, 0);
		g.drawImage(trump, 0, gc.getHeight() - trump.getHeight());
		g.drawImage(sign, 0, -10);
		g.setColor(Color.white);
		g.fillRect(0, gc.getHeight() - trump.getHeight() / 4,
				g.getFont().getWidth("\"My father gave me a small loan of $1,000,000\"") + 5, 20);
		g.setColor(Color.black);
		g.drawString("\"My father gave me a small loan of $1,000,000\"", 0, gc.getHeight() - trump.getHeight() / 4);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

	}

	@Override
	public int getID() {
		return ID;
	}

}
