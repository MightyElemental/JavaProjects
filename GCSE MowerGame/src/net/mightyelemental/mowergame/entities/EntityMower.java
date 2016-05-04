package net.mightyelemental.mowergame.entities;

import java.awt.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.World;

public class EntityMower extends Entity {

	private static final long serialVersionUID = 4112241142072508351L;

	public float maxSpeed = 5f;
	public float vel = 5f;

	public EntityMower(float x, float y, World worldObj) {
		super(x, y, 50, 50, worldObj);
		this.setIcon("entities.lawnMower");
		displayIcon = displayIcon.getScaledCopy((int) width, (int) height);
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
		worldObj.grassCon.setMowed(x, y);
	}

	public float getAngle() {
		return angle;
	}

}
