package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.ResourceLoader;
import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;

public abstract class AppWindow extends RoundedRectangle {

	private TaskbarApp linkedTaskbarApp;

	public Image	windowButtons;
	public Image	content;
	public Graphics	contentGraphics;

	private String			displayTitle;
	private final String	baseTitle;

	protected long	lastDrawTime;
	private boolean	showFPS;
	private int		tickCount;

	public boolean toMinimise, isMinimised, fullscreen, toClose;

	public List<GUIComponent> menuButtons = new ArrayList<GUIComponent>();

	public AppWindow(float x, float y, float width, float height, String title) {
		super(x, y, width, height, 3);
		this.displayTitle = title;
		this.baseTitle = title;
		try {
			content = new Image((int) (width - 3), (int) height - 28);
			contentGraphics = content.getGraphics();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		windowButtons = ResourceLoader.loadImage("desktop.windowButtons").getScaledCopy(21f / 15f);
		menuButtons.add(new GUIButton(x + width - 85, y + 2, 21, 21, "#minimise"));
		menuButtons.add(new GUIButton(x + width - 60, y + 2, 21, 21, "#maximise"));
		menuButtons.add(new GUIButton(x + width - 35, y + 2, 21, 21, "#exit"));
	}

	private static final long serialVersionUID = 1L;

	private String getFPSText() {
		long ms = (System.nanoTime() - lastDrawTime);
		return " (" + (Math.round(10000000000f / ms) / 10f) + "fps | " + (ms / 1000000) + "ms)";
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) {

		if ( toMinimise || (isMinimised && !toMinimise) ) {
			animateMinimize(gc, sbg, g);
			return;
		}
		g.setColor(Color.lightGray);
		g.fill(this);
		g.setColor(new Color(30, 79, 178));
		g.fillRoundRect(x, y, width - 1, 25, 3);
		g.fillRect(x, y + 10, width - 1, 15);
		windowButtons.draw(x + width - 85, y + 2);
		g.setColor(Color.white);
		g.drawString(displayTitle, x + 15, y + 5);

		// for (int i = 0; i < menuButtons.size(); i++) {
		// g.draw(menuButtons.get(i));
		// }
		drawContent(contentGraphics);
		g.drawImage(content, (int) getX() + 1, (int) getY() + 26);
		lastDrawTime = System.nanoTime();
	}

	public abstract void drawContent(Graphics g);

	private float minimizeScale = 0;

	private void animateMinimize(GameContainer gc, StateBasedGame sbg, Graphics g) {
		// if ( (!isMinimised && toMinimise) || (isMinimised && !toMinimise) ) {
		g.setColor(Color.gray);
		float x = this.getX() * (1 - minimizeScale) + linkedTaskbarApp.getX() * minimizeScale;
		float y = this.getY() + Math.abs((720 - this.getY()) * minimizeScale * minimizeScale);
		float width = this.getWidth() * (1 - minimizeScale) + linkedTaskbarApp.getWidth() * minimizeScale;
		float height = this.getHeight() * (1 - minimizeScale) + linkedTaskbarApp.getHeight() * minimizeScale;
		g.fillRoundRect(x, y, width, height, (int) (5 + 10 * (1 - minimizeScale)));
		// }
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		if ( toMinimise ) {
			if ( Math.round(minimizeScale * 100) / 100f < 1 ) {
				minimizeScale += 0.02f;
			} else {
				isMinimised = true;
			}
		} else if ( isMinimised ) {
			if ( Math.round(minimizeScale * 100) / 100f > 0 ) {
				minimizeScale -= 0.02f;
			} else {
				isMinimised = false;
			}
		}
		if ( tickCount % 110 == 0 && showFPS() ) {
			displayTitle = baseTitle + getFPSText();
		}
		tickCount++;
	}

	private boolean canDrag = false;

	public void mouseDragged(int x, int y) {
		this.changeXBy(x);
		this.changeYBy(y);
	}

	public void onMouseReleased(int button) {
		canDrag = false;
	}

	public boolean onMousePressed(int button, int x, int y) {
		boolean flag = false;
		if ( isMinimised ) return false;
		for ( int i = 0; i < menuButtons.size(); i++ ) {
			if ( menuButtons.get(i).contains(x, y) ) {
				switch (menuButtons.get(i).getUID()) {
				case "#EXIT":
					if ( !toMinimise ) {
						toClose = true;
					}
					break;
				case "#MINIMISE":
					if ( !toMinimise ) {
						toMinimise = true;
					}
					System.out.println(getLinkedTaskbarApp().getUID());
					break;
				case "#MAXIMISE":
					fullscreen = true;
					break;
				}
				flag = true;
			}
		}
		if ( !flag && y < getY() + 27 ) {
			canDrag = true;
		}

		return flag;

	}

	public void changeXBy(float x) {
		super.setX(super.getX() + x);
		for ( GUIComponent c : menuButtons ) {
			c.setX(c.getX() + x);
		}
	}

	public void changeYBy(float y) {
		super.setY(super.getY() + y);
		for ( GUIComponent c : menuButtons ) {
			c.setY(c.getY() + y);
		}
	}

	public TaskbarApp getLinkedTaskbarApp() {
		return linkedTaskbarApp;
	}

	public void setLinkedTaskbarApp(TaskbarApp linkedTaskbarApp) {
		this.linkedTaskbarApp = linkedTaskbarApp;
	}

	public String getTitle() {
		return this.displayTitle;
	}

	public boolean isDraggable() {
		return canDrag;
	}

	public abstract void keyPressed(int key, char c);

	public boolean showFPS() {
		return showFPS;
	}

	public void setShowFPS(boolean showFPS) {
		this.showFPS = showFPS;
	}

}
