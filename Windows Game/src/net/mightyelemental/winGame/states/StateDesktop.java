package net.mightyelemental.winGame.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.ResourceLoader;
import net.mightyelemental.winGame.WindowsMain;
import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.StartWindow;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.TaskbarApp;

public class StateDesktop extends BasicGameState {
	
	
	public StateDesktop() {
	}
	
	private List<GUIComponent> guiComponents = new ArrayList<GUIComponent>();
	private List<AppWindow> windowList = new ArrayList<AppWindow>();
	
	private Image background, taskbar;
	private Rectangle selection;
	private StartWindow startWin;
	
	private String selectedUID = "";
	
	@Override
	public void init(GameContainer gc, StateBasedGame delta) throws SlickException {
		background = ResourceLoader.loadImage("desktop.background-bliss");
		taskbar = ResourceLoader.loadImage("desktop.taskbar");
		
		startWin = new StartWindow();
		guiComponents.add(new GUIButton(0, gc.getHeight() - 43, 105, 43, "START"));
		startWin.init(gc, delta);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		background.draw();
		g.setColor(Color.white);
		// g.drawString("<Some GUI goes here>", gc.getWidth() / 2 - (g.getFont().getWidth("<Some GUI goes here>") / 2),
		// gc.getHeight() / 2);
		taskbar.draw(0, gc.getHeight() - taskbar.getHeight());
		if (selection != null) {
			g.setColor(new Color(0f, 0f, 0.5f, 0.3f));
			g.fill(selection);
			g.setColor(new Color(0f, 0f, 0.7f, 1f));
			g.draw(selection);
		}
		for (GUIComponent c : guiComponents) {
			c.draw(gc, sbg, g);
		}
		drawWindows(gc, sbg, g);
	}
	
	public void drawWindows(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (int i = 0; i < windowList.size(); i++) {
			windowList.get(i).draw(gc, sbg, g);
		}
		if (getComponent("START") != null && getComponent("START").isSelected()) {
			startWin.draw(gc, sbg, g);
		}
	}
	
	public GUIComponent getComponent(String uid) {
		for (int i = 0; i < guiComponents.size(); i++) {
			if (guiComponents.get(i).getUID().equals(uid.toUpperCase())) return guiComponents.get(i);
		}
		return null;
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		startWin.update(gc, sbg, delta);
		updateWindows(gc, sbg, delta);
	}
	
	public void updateWindows(GameContainer gc, StateBasedGame sbg, int delta) {
		for (int i = 0; i < windowList.size(); i++) {
			windowList.get(i).update(gc, sbg, delta);
			if (windowList.get(i).toClose) {
				windowList.remove(i);
			}
		}
		for (int i = 0; i < guiComponents.size(); i++) {
			if (guiComponents.get(i) instanceof TaskbarApp) {
				if (((TaskbarApp) guiComponents.get(i)).linkedWindow == null || ((TaskbarApp) guiComponents.get(i)).linkedWindow.toClose) {
					guiComponents.remove(i);
				}
			}
		}
	}
	
	@Override
	public int getID() {
		return WindowsMain.STATE_DESKTOP;
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (selectedUID.equals("")) {
			selection = new Rectangle(this.oldx, this.oldy, newx - this.oldx, newy - this.oldy);
		}
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
		selectedUID = "";
		for (GUIComponent c : guiComponents) {
			if (!c.getUID().equals("START")) {
				c.setSelected(false);
			}
			if (c.contains(x, y)) {
				c.onMousePressed(button, x, y);
				onComponentPressed(button, c);
				selectedUID = c.getUID();
				if (c.getUID().equals("START")) {
					boolean sel = c.isSelected();
					c.setSelected(!sel);
				} else {
					c.setSelected(true);
				}
			}
		}
		for (int i = 0; i < windowList.size(); i++) {
			if (windowList.get(i).contains(x, y)) {
				windowList.get(i).onMousePressed(button, x, y);
			}
		}
	}
	
	public void onComponentPressed(int button, GUIComponent c) {
		
	}
	
	@Override
	public void keyPressed(int key, char c) {
		for (GUIComponent g : guiComponents) {
			if (g.getUID().equals(selectedUID)) {
				g.onKeyPressed(key, c);
			}
		}
		if (key == Input.KEY_I) {
			int x = 1280 / 2 - 800 / 2;
			int y = 720 / 2 - 600 / 2;
			AppWindow wa = new AppWindow(x, y, 800, 600, "Test");
			windowList.add(wa);
			guiComponents.add(new TaskbarApp(110, "test", wa));
		}
	}
	
}
