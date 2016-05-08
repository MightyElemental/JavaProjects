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

	public float health = 100f;

	public Rectangle bladeRect;

	public EntityMower(float x, float y, World worldObj) {
		super(x, y, 100, 100, worldObj);
		this.setIcon("entities.lawnMower");
		bladeRect = new Rectangle(x + width / 4, y + height / 4, width / 2.5f, height / 2.5f);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		int mouseX = gc.getInput().getMouseX();
		int mouseY = gc.getInput().getMouseY();

		// Move towards mouse
		int x = (int) this.getCenterX();
		int y = (int) this.getCenterY();

		angle = MathHelper.getAngle(new Point(x, y), new Point(mouseX, mouseY));

		float amountToMoveX = (vel / 25f * delta * (float) Math.cos(Math.toRadians(angle)));
		float amountToMoveY = (vel / 25f * delta * (float) Math.sin(Math.toRadians(angle)));

		if (Math.abs(x - mouseX) + Math.abs(y - mouseY) > 8) {
			getIcon().setRotation(angle - 90);
			this.setCenterX(this.getCenterX() - amountToMoveX);
			this.setCenterY(this.getCenterY() - amountToMoveY);
		}
		if (worldObj.getCollidingEntity(bladeRect) != null) {
			health -= worldObj.getCollidingEntity(bladeRect).damageToMower;
			if (health <= 0) {
				health = 0;
			}
			worldObj.getCollidingEntity(bladeRect).setDead();
			worldObj.animalsKilled++;
		}
		bladeRect.setCenterX(this.getCenterX());
		bladeRect.setCenterY(this.getCenterY());
		worldObj.grassCon.setMowed(bladeRect);
	}

}
