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
import net.iridgames.munchkin.gui.ButtonListener;

public class StateMenu extends BasicGameState implements ButtonListener {

	private final int ID;

	private List<Button> buttons = new ArrayList<Button>();

	private Random rand = new Random();

	private Button	playButton;
	private Button	exitButton;

	private List<BackgroundImage> floatingImages = new ArrayList<BackgroundImage>();

	public StateMenu( int id ) {
		this.ID = id;
		Munchkin.buttonHandler.addListener(this);
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		playButton = new Button(10, 50, 150, 100).setText("Play");
		buttons.add(playButton);
		exitButton = new Button(300, 50, 150, 100).setText("Exit");
		buttons.add(exitButton);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		renderBackgroundImages(gc, sbg, g);
		renderButtons(gc, sbg, g);
	}

	public void renderButtons(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (Button b : buttons) {
			b.draw(g);
			g.setColor(Color.white);
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
		while (floatingImages.size() < 60) {
			floatingImages.add(
					new BackgroundImage(Munchkin.loader.getRandomImage(rand), rand.nextInt(10) + 5, rand, gc.getWidth(), gc.getHeight()));
		}
	}

	public void updateBackgroundImages(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// System.out.println(floatingImages.size());
		for (int i = 0; i < floatingImages.size(); i++) {
			BackgroundImage b = floatingImages.get(i);
			if (b == null) {
				continue;
			}
			b.update(gc, sbg, delta);
			if (b.isDead()) {
				floatingImages.remove(b);
			}
		}
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		super.mouseClicked(button, x, y, clickCount);
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		for (Button b : buttons) {
			if (b.contains(x, y)) {
				Munchkin.buttonHandler.onButtonPushed(b, button);
			}
		}
	}

	@Override
	public void onButtonPushed(Button b, int button) {
		if (b.equals(playButton)) {
			System.out.println("Play! " + button);
		}
		if (b.equals(exitButton)) {
			System.exit(0);
		}
	}

}
