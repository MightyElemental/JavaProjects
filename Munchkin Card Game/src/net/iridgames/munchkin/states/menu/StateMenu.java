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
import net.iridgames.munchkin.gui.ScrollBar;

public class StateMenu extends BasicGameState implements GUIListener {

	private final int ID;

	private List<GUIObject>	mainButtons	= new ArrayList<GUIObject>();
	private List<GUIObject>	playButtons	= new ArrayList<GUIObject>();
	private List<GUIObject>	hostButtons	= new ArrayList<GUIObject>();
	private List<GUIObject>	optiButtons	= new ArrayList<GUIObject>();

	private Random rand = new Random();

	private static final int	MAIN_MODE		= 0;
	private static final int	PLAY_MODE		= 1;
	private static final int	HOST_MODE		= 2;
	private static final int	OPTIONS_MODE	= 3;

	private int mode = MAIN_MODE;

	// main buttons
	private Button	playButton;
	private Button	exitButton;
	private Button	optionsButton;

	// play buttons
	private Button	hostButton;
	private Button	joinButton;
	private Button	backButton;

	// host buttons
	private CheckBox useAddons;
	// private TextBox portBox;

	// option buttons
	private Button		credits;
	private ScrollBar	musicVolume;
	private ScrollBar	soundVolume;
	private Button		backButton2;

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
		// Main menu
		playButton = new Button(200, 250, 180 * 3, 130).setText("Play");
		optionsButton = new Button(200, 400, 180 * 3, 130).setText("Options");
		exitButton = new Button(200, 550, 180 * 3, 130).setText("Exit");
		mainButtons.add(playButton);
		mainButtons.add(optionsButton);
		mainButtons.add(exitButton);

		// Play menu
		hostButton = new Button(200, 250, 180 * 3, 130).setText("Host");
		joinButton = new Button(200, 400, 180 * 3, 130).setText("Join");
		backButton = new Button(200, 550, 180 * 3, 130).setText("Back");
		playButtons.add(hostButton);
		playButtons.add(joinButton);
		playButtons.add(backButton);

		// Host menu
		useAddons = new CheckBox(200, 250).setText("Use addons");
		hostButtons.add(useAddons);
		hostButtons.add(backButton);

		// Join menu

		// Options menu
		credits = new Button(200, 250, 180 * 3, 130).setText("Credits");
		musicVolume = new ScrollBar(200, 400, 180 * 3, 130, 0, 100).setText("Music Volume");
		soundVolume = new ScrollBar(200, 550, 180 * 3, 130, 0, 100).setText("Sound Volume");
		backButton2 = new Button(200, 700, 180 * 3, 130).setText("Back");
		optiButtons.add(credits);
		optiButtons.add(musicVolume);
		optiButtons.add(soundVolume);
		optiButtons.add(backButton2);
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
		} else if (mode == OPTIONS_MODE) {
			for (GUIObject b : optiButtons) {
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
			guiPush(button, x, y, mainButtons);
		} else if (mode == PLAY_MODE) {
			guiPush(button, x, y, playButtons);
		} else if (mode == HOST_MODE) {
			guiPush(button, x, y, hostButtons);
		} else if (mode == OPTIONS_MODE) {
			guiPush(button, x, y, optiButtons);
		}
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (mode == MAIN_MODE) {
			guiDrag(newx, newy, mainButtons);
		} else if (mode == PLAY_MODE) {
			guiDrag(newx, newy, playButtons);
		} else if (mode == HOST_MODE) {
			guiDrag(newx, newy, hostButtons);
		} else if (mode == OPTIONS_MODE) {
			guiDrag(newx, newy, optiButtons);
		}
	}

	public void guiDrag(int x, int y, List<GUIObject> list) {
		for (GUIObject b : list) {
			if (b instanceof ScrollBar) {
				if (b.contains(x, y)) {
					Munchkin.buttonHandler.onScrollBarDragged((ScrollBar) b, x);
					continue;
				}
			}
		}
	}

	public void guiPush(int button, int x, int y, List<GUIObject> list) {
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
			if (b instanceof ScrollBar) {
				if (b.contains(x, y)) {
					Munchkin.buttonHandler.onScrollBarClicked((ScrollBar) b, x);
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
		if (b.equals(backButton) || b.equals(backButton2)) {
			mode = MAIN_MODE;
		}
		if (b.equals(hostButton)) {
			mode = HOST_MODE;
		}
		if (b.equals(optionsButton)) {
			mode = OPTIONS_MODE;
		}
	}

	@Override
	public void onCheckBoxClicked(CheckBox cb) {

	}

	@Override
	public void onScrollBarClicked(ScrollBar sb, float x) {

	}

	@Override
	public void onScrollBarDragged(ScrollBar sb, int x) {

	}

}
