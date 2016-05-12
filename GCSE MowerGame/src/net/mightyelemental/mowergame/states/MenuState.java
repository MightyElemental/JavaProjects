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
		playButton = new Button(gc.getWidth() / 2 - 100, gc.getHeight() / 2 - 25, 200, 50)
				.setText("Play", gc.getGraphics()).setColor(new Color(255, 255, 255, 0.9f));
		objects.add(playButton);
	}

	private Color cloak = new Color(0, 0, 0, 0.9f);
	private Color fade = new Color(0, 0, 0, 0f);

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (menuWorld != null) {
			menuWorld.draw(gc, sbg, g);
		}
		g.setColor(cloak);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		int wid = g.getFont().getWidth(":P");
		g.setColor(Color.white);
		g.drawString(":P", gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 - 150);
		for (GUIObject obj : objects) {
			obj.draw(g);
		}
		g.setColor(fade);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		menuWorld.update(gc, delta);
		if (enterPlay) {
			if (fade.a < 1f) {
				fade.a += (1f / 17f / 4f) * (delta / 17f);
				menuWorld.deltaDividor += 0.5f;
			} else {
				menuWorld.animalsKilled = 0;
				menuWorld.lawnMower = null;
				menuWorld.grassCon = null;
				menuWorld = null;
				sbg.enterState(MowerGame.STATE_GAME);
			}
		}
	}

	@Override
	public int getID() {
		return ID;
	}

	public boolean enterPlay;

	@Override
	public void onButtonPushed(Button b, int button) {
		if (b.equals(playButton) && button == 0) {
			enterPlay = true;
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
