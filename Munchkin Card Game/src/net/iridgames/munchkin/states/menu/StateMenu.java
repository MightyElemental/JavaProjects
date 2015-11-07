package net.iridgames.munchkin.states.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.munchkin.Munchkin;
import net.iridgames.munchkin.gui.Button;
import net.iridgames.munchkin.gui.CheckBox;
import net.iridgames.munchkin.gui.GUIListener;
import net.iridgames.munchkin.gui.GUIObject;

public class StateMenu extends BasicGameState implements GUIListener {

	private final int ID;

	private List<GUIObject>	mainButtons	= new ArrayList<GUIObject>();
	private List<GUIObject>	playButtons	= new ArrayList<GUIObject>();
	private List<GUIObject>	hostButtons	= new ArrayList<GUIObject>();

	private Random rand = new Random();

	private static final int	MAIN_MODE	= 0;
	private static final int	PLAY_MODE	= 1;
	private static final int	HOST_MODE	= 2;

	private int mode = MAIN_MODE;

	// main buttons
	private Button	playButton;
	private Button	exitButton;

	// play buttons
	private Button	hostButton;
	private Button	joinButton;
	private Button	backButton;

	// host buttons
	private CheckBox test;

	// join

	private Image background;

	private List<BackgroundImage> floatingImages = new ArrayList<BackgroundImage>();

	public StateMenu( int id ) {
		this.ID = id;
		Munchkin.buttonHandler.addListener(this);
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		initButtons(gc, sbg);

		background = Munchkin.loader.loadImage("menu.background");
	}

	private void initButtons(GameContainer gc, StateBasedGame sbg) {
		playButton = new Button(200, 250, 180 * 3, 130).setText("Play");
		mainButtons.add(playButton);
		exitButton = new Button(200, 450, 180 * 3, 130).setText("Exit");
		mainButtons.add(exitButton);

		hostButton = new Button(200, 250, 180 * 3, 130).setText("Host");
		playButtons.add(hostButton);
		joinButton = new Button(200, 450, 180 * 3, 130).setText("Join");
		playButtons.add(joinButton);
		backButton = new Button(200, 650, 180 * 3, 130).setText("Back");
		playButtons.add(backButton);

		test = new CheckBox(200, 250).setText("Click to check the box!");
		hostButtons.add(test);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setFont(Munchkin.font);
		background.draw(0, 0, gc.getWidth(), gc.getHeight());
		renderBackgroundImages(gc, sbg, g);
		renderButtons(gc, sbg, g);
	}

	public void renderButtons(GameContainer gc, StateBasedGame sbg, Graphics g) {
		if (mode == MAIN_MODE) {
			for (GUIObject b : mainButtons) {
				b.draw(g);
			}
		} else if (mode == PLAY_MODE) {
			for (GUIObject b : playButtons) {
				b.draw(g);
			}
		} else if (mode == HOST_MODE) {
			for (GUIObject b : hostButtons) {
				b.draw(g);
			}
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
		// System.out.println(floatingImages.size());
		while (floatingImages.size() < 30) {
			floatingImages.add(
					new BackgroundImage(Munchkin.loader.getRandomImage(rand), rand.nextInt(10) + 8, rand, gc.getWidth(), gc.getHeight()));
		}
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
		if (mode == MAIN_MODE) {
			buttonPush(button, x, y, mainButtons);
		} else if (mode == PLAY_MODE) {
			buttonPush(button, x, y, playButtons);
		} else if (mode == HOST_MODE) {
			buttonPush(button, x, y, hostButtons);
		}
	}

	public void buttonPush(int button, int x, int y, List<GUIObject> list) {
		for (GUIObject b : list) {
			if (b instanceof Button) {
				if (b.contains(x, y)) {
					Munchkin.buttonHandler.onButtonPushed((Button) b, button);
					continue;
				}
			}
			if (b instanceof CheckBox) {
				if (b.contains(x, y)) {
					Munchkin.buttonHandler.onCheckBoxClicked((CheckBox) b);
					continue;
				}
			}
		}
	}

	@Override
	public void onButtonPushed(Button b, int button) {
		if (b.equals(playButton)) {
			mode = PLAY_MODE;
		}
		if (b.equals(exitButton)) {
			System.exit(0);
		}
		if (b.equals(backButton)) {
			mode = MAIN_MODE;
		}
		if (b.equals(hostButton)) {
			mode = HOST_MODE;
		}
	}

	@Override
	public void onCheckBoxClicked(CheckBox cb) {

	}

}
