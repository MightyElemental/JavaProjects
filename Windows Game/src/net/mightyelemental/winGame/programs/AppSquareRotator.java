package net.mightyelemental.winGame.programs;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class AppSquareRotator extends AppWindow {

	private static final long	serialVersionUID	= -5362114223313401429L;
	private Rectangle			rect				= new Rectangle(50, 50, 50, 50);
	private Shape				transformedRect		= rect;
	private float				angle				= 0;

	private float	xLoc	= 50, yLoc = 50;
	private float	yVel	= 0.0f, xVel = 0.0f;

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

	private float	gravity	= 0.05f;
	private boolean	onGround;

	@Override
	public void updateContent(int delta) {
		if ( on[0] || on[2] ) {
			angle -= delta / 5f;
			if ( onGround ) {
				xVel -= delta / 50f;
			} else {
				xVel -= delta / 200f;
			}
		} else if ( on[1] || on[3] ) {
			angle += delta / 5f;
			if ( onGround ) {
				xVel += delta / 50f;
			} else {
				xVel += delta / 200f;
			}
		}
		if ( xVel > 2.6f ) xVel = 2.6f;
		if ( xVel < -2.6f ) xVel = -2.6f;
		if ( onGround ) {
			xVel /= 1.1f;
		} else {
			xVel /= 1.005f;
		}
		if ( transformedRect.getMaxY() + transformedRect.getHeight() / 2 < height ) {
			yVel += gravity;
			onGround = false;
		} else if ( transformedRect.getMaxY() + transformedRect.getHeight() / 2 >= height ) {
			yLoc = height - transformedRect.getHeight();
			onGround = true;
		} else {
			yVel = 0;
		}
		for ( float i = 0; i < yVel; i += gravity ) {
			if ( transformedRect.getMaxY() + i + transformedRect.getHeight() / 2 >= height ) {
				yVel = i + 5;
				break;
			}
		}
		yLoc += yVel;
		xLoc += xVel;
		if ( on[0] || on[1] || on[2] || on[3] ) {
			transformedRect = rect.transform(Transform.createRotateTransform((float) Math.toRadians(angle)));
		}
		transformedRect.setCenterX(xLoc);
		transformedRect.setCenterY(yLoc);
	}

	private boolean[] on = new boolean[4];

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_Q:
			on[0] = true;
			break;
		case Input.KEY_E:
			on[1] = true;
			break;
		case Input.KEY_A:
			on[2] = true;
			break;
		case Input.KEY_D:
			on[3] = true;
			break;
		}
		if ( onGround && key == Input.KEY_SPACE ) {
			yVel = -2f;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		switch (key) {
		case Input.KEY_Q:
			on[0] = false;
			break;
		case Input.KEY_E:
			on[1] = false;
			break;
		case Input.KEY_A:
			on[2] = false;
			break;
		case Input.KEY_D:
			on[3] = false;
			break;
		}

	}

}
