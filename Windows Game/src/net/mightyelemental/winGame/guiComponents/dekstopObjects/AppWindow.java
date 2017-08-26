package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.ResourceLoader;
import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;

public class AppWindow extends RoundedRectangle {

	public Image windowButtons;

	public String title;

	public boolean toMinimise, fullscreen, toClose;

	public List<GUIComponent> menuButtons = new ArrayList<GUIComponent>();

	public AppWindow(float x, float y, float width, float height, String title) {
		super(x, y, width, height, 15);
		this.title = title;
		windowButtons = ResourceLoader.loadImage("desktop.windowButtons").getScaledCopy(21f / 15f);
		menuButtons.add(new GUIButton(x + width - 85, y + 2, 21, 21, "minimise"));
		menuButtons.add(new GUIButton(x + width - 60, y + 2, 21, 21, "maximise"));
		menuButtons.add(new GUIButton(x + width - 35, y + 2, 21, 21, "exit"));
	}

	private static final long serialVersionUID = 1L;

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) {
		if ( toMinimise ) {
			animateMinimize(gc, sbg, g);
			return;
		}
		g.setColor(Color.lightGray);
		g.fill(this);
		g.setColor(new Color(30, 79, 178));
		g.fillRoundRect(x, y, width - 1, 25, 15);
		g.fillRect(x, y + 10, width - 1, 15);
		windowButtons.draw(x + width - 85, y + 2);
		g.setColor(Color.white);
		g.drawString(title, x + 15, y + 5);
		// for (int i = 0; i < menuButtons.size(); i++) {
		// g.draw(menuButtons.get(i));
		// }
	}

	private float minimizeX, minimizeY, minimizeScale = 1;

	private void animateMinimize(GameContainer gc, StateBasedGame sbg, Graphics g) {
		if ( minimizeY < 680 ) {
			g.setColor(Color.gray);
			g.fillRoundRect(minimizeX, minimizeY, getWidth() / minimizeScale, getHeight() / minimizeScale,
					15);
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		if ( toMinimise ) {
			minimizeScale += 0.1f;
			// if ( minimizeX >= 100 ) minimizeX--;
			if ( minimizeY < 680 ) minimizeY += 18;
		} else {
			minimizeX = getX();
			minimizeY = getY();
		}
	}

	public void onMousePressed(int button, int x, int y) {
		for ( int i = 0; i < menuButtons.size(); i++ ) {
			if ( menuButtons.get(i).contains(x, y) ) {
				switch (menuButtons.get(i).getUID()) {
				case "EXIT":
					toClose = true;
					break;
				case "MINIMISE":
					toMinimise = true;
					break;
				case "MAXIMISE":
					fullscreen = true;
					break;
				}
			}
		}
	}

}
