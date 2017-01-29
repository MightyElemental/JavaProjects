package net.mightyelemental.winGame.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.ResourceLoader;
import net.mightyelemental.winGame.WindowsMain;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.StartWindow;

public class StateDesktop extends BasicGameState {
	
	
	public StateDesktop() {
	}
	
	private List<GUIComponent> guiComponents = new ArrayList<GUIComponent>();
	
	private Image background, taskbar;
	private Rectangle selection;
	private StartWindow startWin;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		background = ResourceLoader.loadImage("desktop.background-bliss");
		taskbar = ResourceLoader.loadImage("desktop.taskbar");
		startWin = new StartWindow();
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		background.draw();
		g.setColor(Color.white);
		g.drawString("<Some GUI goes here>", gc.getWidth() / 2 - (g.getFont().getWidth("<Some GUI goes here>") / 2), gc.getHeight() / 2);
		taskbar.draw(0, gc.getHeight() - taskbar.getHeight());
		if (selection != null) {
			g.setColor(new Color(0f, 0f, 0.5f, 0.3f));
			g.fill(selection);
			g.setColor(new Color(0f, 0f, 0.7f, 1f));
			g.draw(selection);
		}
		g.setColor(new Color(30, 79, 178));
		//g.fill(startWin);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		startWin.update(gc, sbg, delta);
	}
	
	@Override
	public int getID() {
		return WindowsMain.STATE_DESKTOP;
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		selection = new Rectangle(this.oldx, this.oldy, newx - this.oldx, newy - this.oldy);
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		selection = null;
	}
	
	private int oldx, oldy;
	
	@Override
	public void mousePressed(int button, int x, int y) {
		oldx = x;
		oldy = y;
	}
	
}
