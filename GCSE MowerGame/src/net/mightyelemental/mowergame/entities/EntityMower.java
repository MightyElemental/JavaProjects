package net.mightyelemental.mowergame.entities;

import java.awt.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.World;

public class EntityMower extends Entity {

	private static final long serialVersionUID = 4112241142072508351L;

	public float maxSpeed = 5f;
	public float vel = 8f;

	public Rectangle bladeRect;

	public EntityMower(float x, float y, World worldObj) {
		super(x, y, 70, 70, worldObj);
		this.setIcon("entities.lawnMower");
		bladeRect = new Rectangle(x + width / 4, y + height / 4, width / 2.5f, height / 2.5f);
	}

	public float angle = 0;

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		int mouseX = gc.getInput().getMouseX();
		int mouseY = gc.getInput().getMouseY();

		// Move towards mouse
		int x = (int) this.getCenterX();
		int y = (int) this.getCenterY();

		angle = MathHelper.getAngle(new Point(x, y), new Point(mouseX, mouseY));

		float amountToMoveX = (vel * (float) Math.cos(Math.toRadians(angle)));
		float amountToMoveY = (vel * (float) Math.sin(Math.toRadians(angle)));

		if (Math.abs(x - mouseX) + Math.abs(y - mouseY) > 5) {
			getIcon().setRotation(angle - 90);
			this.setCenterX(this.getCenterX() - amountToMoveX);
			this.setCenterY(this.getCenterY() - amountToMoveY);
		}
		bladeRect.setCenterX(this.getCenterX());
		bladeRect.setCenterY(this.getCenterY());
		worldObj.grassCon.setMowed(bladeRect);
	}

	public float getAngle() {
		return angle;
	}

}
