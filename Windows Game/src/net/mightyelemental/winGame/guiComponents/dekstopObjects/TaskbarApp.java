package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.guiComponents.GUIButton;

public class TaskbarApp extends GUIButton {

	private static final long	serialVersionUID	= 8086670225644581843L;
	public static final String	TASKBARAPP			= "_taskbarapp";

	// private int index = -1;

	private final float startX;

	public AppWindow linkedWindow;

	public TaskbarApp(int x, AppWindow linkedWindow, int index) {
		super(x, 720 - 43, 86, 43, linkedWindow.getTitle() + TASKBARAPP);
		startX = x;
		this.linkedWindow = linkedWindow;
		this.linkedWindow.setLinkedTaskbarApp(this);
		setIndex(index);
	}

	public void setIndex(int index) {
		// this.index = index;
		float xOffset = index * (this.getWidth() + 2);
		this.setX(startX + xOffset);
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(new Color(30, 79, 178));
		g.fillRoundRect(x, y, width, height, 5);
		g.setColor(new Color(30, 79, 178).darker());
		g.drawRoundRect(x, y, width, height, 5);
		g.setColor(Color.white);
		String temp = linkedWindow.getTitle();
		if ( linkedWindow.getTitle().length() >= 9 ) {
			temp = linkedWindow.getTitle().substring(0, 6) + "...";
		}

		g.drawString(temp, x + 2, y + 10);
	}

	@Override
	public void onMousePressed(int button, int x, int y) {
		if ( button == 0 ) {
			this.linkedWindow.toMinimise = !this.linkedWindow.toMinimise;
		} else if ( button == 1 ) {

		}
	}

}
