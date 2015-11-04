package net.iridgames.munchkin.states.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.munchkin.Munchkin;
import net.iridgames.munchkin.gui.Button;

public class StateMenu extends BasicGameState {

	private final int ID;

	private List<Button> buttons = new ArrayList<Button>();

	private Random rand = new Random();

	private List<BackgroundImage> floatingImages = new ArrayList<BackgroundImage>();

	public StateMenu( int id ) {
		this.ID = id;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		buttons.add(new Button(10, 50, 100, 100).setText("Play"));
		buttons.add(new Button(110, 50, 100, 100).setText("Exit"));
		floatingImages.add(new BackgroundImage(Munchkin.loader.loadImage("menu.munchkin-original"), 10, rand));
		floatingImages.add(new BackgroundImage(Munchkin.loader.loadImage("menu.munchkin-original"), 10, rand));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		renderBackgroundImages(gc, sbg, g);
		renderButtons(gc, sbg, g);
	}

	public void renderButtons(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (Button b : buttons) {
			b.draw(g);
		}
	}

	public void renderBackgroundImages(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (BackgroundImage b : floatingImages) {
			b.draw(g);
		}
		// g.setColor(new Color(150, 150, 150, 0.5f));
		// g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		updateBackgroundImages(gc, sbg, delta);
	}

	public void updateBackgroundImages(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		System.out.println(floatingImages.size());
		for (BackgroundImage b : floatingImages) {
			b.update(gc, sbg, delta);
		}
	}

	@Override
	public int getID() {
		return ID;
	}

}
