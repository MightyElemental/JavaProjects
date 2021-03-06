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
import net.mightyelemental.mowergame.gui.GUIListener;
import net.mightyelemental.mowergame.gui.GUIObject;
import net.mightyelemental.mowergame.gui.ScrollBar;

public class MenuState extends BasicGameState implements GUIListener {
	
	
	public final int ID;
	
	public List<GUIObject> objects = new ArrayList<GUIObject>();
	
	private World menuWorld;
	
	public Button playButton;
	public Button shopButton;
	public Button exitButton;
	
	public Random rand;
	
	public MenuState( int ID, Random rand ) {
		this.ID = ID;
		MowerGame.buttonHandler.addListener(this);
		this.rand = rand;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		menuWorld = new World(rand, true);
		menuWorld.init(gc, sbg);
		menuWorld.deltaDividor = 2.2f;
		int buttonBase = gc.getHeight() / 2 - 25;
		playButton = new Button(gc.getWidth() / 2 - 100, buttonBase - 30, 200, 50).setText("Play", gc.getGraphics())
			.setColor(new Color(255, 255, 255, 0.9f));
		shopButton = new Button(gc.getWidth() / 2 - 100, buttonBase + 30, 200, 50).setText("Shop", gc.getGraphics())
			.setColor(new Color(255, 255, 255, 0.9f));
		exitButton = new Button(gc.getWidth() / 2 - 100, buttonBase + 90, 200, 50).setText("Exit", gc.getGraphics())
			.setColor(new Color(255, 255, 255, 0.9f));
		objects.add(playButton);
		objects.add(shopButton);
		objects.add(exitButton);
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
		drawCenterString(g, gc, gc.getHeight() / 2 - 150, "Mowve Mania");
		// g.fillRect(0, 0, gc.getWidth() / 2, 200); // y = 200 x = 640
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
	
	public void resetMenu(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		menuWorld = new World(rand, true);
		menuWorld.init(gc, sbg);
		fade.a = 0;
		menuWorld.deltaDividor = 2.2f;
		enterPlay = false;
		enterShop = false;
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (menuWorld == null) {
			resetMenu(gc, sbg, delta);
		}
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
				MowerGame.gameState.init(gc, sbg);
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
	public void onObjectPushed(GUIObject b, int button, int x, int y) {
		if (b.equals(playButton) && button == 0) {
			enterPlay = true;
		}
		if (b.equals(shopButton) && button == 0) {
			enterShop = true;
		}
		if (b.equals(exitButton) && button == 0) {
			System.exit(0);
		}
	}
	
	public void guiPush(int button, int x, int y, List<GUIObject> list) {
		for (GUIObject b : list) {
			if (b.contains(x, y)) {
				MowerGame.buttonHandler.onObjectPushed(b, button, x, y);
				continue;
			}
		}
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		guiPush(button, x, y, objects);
	}
	
	@Override
	public void onScrollBarDragged(ScrollBar sb, int x) {
		
	}
	
	@Override
	public void onObjectHovered(GUIObject b, int x, int y) {
		
	}
	
}
