package net.mightyelemental.winGame.programs;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class AppSquareRotator extends AppWindow {

	private static final long serialVersionUID = -5362114223313401429L;
	private Rectangle rect = new Rectangle(50, 50, 50, 50);
	private Shape transformedRect = rect;
	private float angle = 0;

	private float xLoc = 50, yLoc = 50;
	private float yVel = 0;

	public AppSquareRotator(float x, float y, float width, float height, String title) {
		super(x, y, width, height, title);
	}

	@Override
	protected void drawContent(Graphics g, int width, int height) {
		g.setAntiAlias(true);
		this.clearScreen();
		g.fill(transformedRect);
		g.drawLine(0, transformedRect.getMaxY(), width, transformedRect.getMaxY());
		g.drawLine(0, height - 1, width, height - 1);
	}

	private float gravity = 0.01f;

	@Override
	public void updateContent(int delta) {
		if (on[0]) {
			angle -= delta / 5f;
			xLoc -= delta / 5f;
		} else if (on[1]) {
			angle += delta / 5f;
			xLoc += delta / 5f;
		}
		if (transformedRect.getMaxY() + transformedRect.getHeight() / 2 < height) {
			yVel += gravity;
		} else if (transformedRect.getMaxY() + transformedRect.getHeight() / 2 >= height) {
			yLoc = height - transformedRect.getHeight();
		} else {
			yVel = 0;
		}
		for (float i = 0; i < yVel; i += gravity) {
			if (transformedRect.getMaxY() + i + transformedRect.getHeight() / 2 >= height) {
				yVel = i + 5;
				break;
			}
		}
		yLoc += yVel;
		if (on[0] || on[1]) {
			transformedRect = rect.transform(Transform.createRotateTransform((float) Math.toRadians(angle)));
		}
		transformedRect.setCenterX(xLoc);
		transformedRect.setCenterY(yLoc);
	}

	private boolean[] on = new boolean[4];

	@Override
	public void keyPressed(int key, char c) {
		on[0] = key == Input.KEY_Q;
		on[1] = key == Input.KEY_E;
		on[2] = key == Input.KEY_A;
		on[3] = key == Input.KEY_D;
	}

	@Override
	public void keyReleased(int key, char c) {
		for (int i = 0; i < on.length; i++)
			on[i] = false;

	}

}
