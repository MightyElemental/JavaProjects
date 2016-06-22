package net.mightyelemental.mowergame.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.World;
import net.mightyelemental.mowergame.gui.Button;
import net.mightyelemental.mowergame.gui.CheckBox;
import net.mightyelemental.mowergame.gui.GUIListener;
import net.mightyelemental.mowergame.gui.GUIObject;
import net.mightyelemental.mowergame.gui.ScrollBar;
import net.mightyelemental.mowergame.gui.TextBox;

public class MenuState extends BasicGameState implements GUIListener {

	public final int ID;

	public List<GUIObject> objects = new ArrayList<GUIObject>();

	private World menuWorld;

	public Button playButton;
	public Button shopButton;

	public Random rand;

	public MenuState(int ID, Random rand) {
		this.ID = ID;
		MowerGame.buttonHandler.addListener(this);
		this.rand = rand;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		menuWorld = new World(rand, true);
		menuWorld.init(gc, sbg);
		menuWorld.deltaDividor = 2.5f;
		int buttonBase = gc.getHeight() / 2 - 25;
		playButton = new Button(gc.getWidth() / 2 - 100, buttonBase - 30, 200, 50).setText("Play", gc.getGraphics())
				.setColor(new Color(255, 255, 255, 0.9f));
		shopButton = new Button(gc.getWidth() / 2 - 100, buttonBase + 30, 200, 50).setText("Shop", gc.getGraphics())
				.setColor(new Color(255, 255, 255, 0.9f));
		objects.add(playButton);
		objects.add(shopButton);
	}

	private Color cloak = new Color(0, 0, 0, 0.7f);
	private Color fade = new Color(0, 0, 0, 0f);

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (menuWorld != null) {
			menuWorld.draw(gc, sbg, g);
		}
		g.setColor(cloak);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		drawCenterString(g, gc, gc.getHeight() / 2 - 150, "GCSE Lawn Mower Game");
		drawCenterString(g, gc, gc.getHeight() / 2 - 130, "\u00a92016 James Burnell");
		for (GUIObject obj : objects) {
			obj.draw(g);
		}
		g.setColor(fade);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
	}

	public void drawCenterString(Graphics g, GameContainer gc, int y, String s) {
		int wid = g.getFont().getWidth(s);
		g.setColor(Color.white);
		g.drawString(s, gc.getWidth() / 2 - wid / 2, y);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		menuWorld.update(gc, delta);
		if (enterPlay) {
			if (fade.a < 1f) {
				fade.a += (1f / 17f / 4f) * (delta / 17f);
				menuWorld.deltaDividor += 0.5f;
			} else {
				menuWorld.lawnMower.animalsKilled = 0;
				menuWorld.lawnMower = null;
				menuWorld.grassCon = null;
				menuWorld = null;
				sbg.enterState(MowerGame.STATE_GAME);
				enterPlay = false;
			}
		}
		if (enterShop) {
			sbg.enterState(MowerGame.STATE_SHOP);
			enterShop = false;
		}
	}

	@Override
	public int getID() {
		return ID;
	}

	public boolean enterPlay;
	public boolean enterShop;

	@Override
	public void onButtonPushed(Button b, int button) {
		if (b.equals(playButton) && button == 0) {
			enterPlay = true;
		}
		if (b.equals(shopButton) & button == 0) {
			enterShop = true;
		}
		System.out.println(button);
	}

	public void guiPush(int button, int x, int y, List<GUIObject> list) {
		for (GUIObject b : list) {
			if (b instanceof Button) {
				if (b.contains(x, y)) {
					MowerGame.buttonHandler.onButtonPushed((Button) b, button);
					continue;
				}
			}
			if (b instanceof CheckBox) {
				if (b.contains(x, y)) {
					MowerGame.buttonHandler.onCheckBoxClicked((CheckBox) b);
					continue;
				}
			}
			if (b instanceof ScrollBar) {
				if (b.contains(x, y)) {
					MowerGame.buttonHandler.onScrollBarClicked((ScrollBar) b, x);
					continue;
				}
			}
			if (b instanceof TextBox) {
				if (b.contains(x, y)) {
					MowerGame.buttonHandler.onTextBoxClicked((TextBox) b, x, y);
					continue;
				}
			}
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		guiPush(button, x, y, objects);
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

	@Override
	public void onTextBoxClicked(TextBox tb, int x, int y) {

	}

}
