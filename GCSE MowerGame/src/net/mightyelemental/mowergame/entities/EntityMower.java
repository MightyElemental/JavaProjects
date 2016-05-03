package net.mightyelemental.mowergame.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.mightyelemental.mowergame.World;

public class EntityMower extends Entity {

	private static final long serialVersionUID = 4112241142072508351L;

	public float maxSpeed = 5f;

	public EntityMower(float x, float y, World worldObj) {
		super(x, y, 50, 50, worldObj);
		this.setIcon("entities.lawnMower");
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		int mouseX = gc.getInput().getMouseX();
		int mouseY = gc.getInput().getMouseY();

		// Move towards mouse
		int x = (int) this.getX();
		int y = (int) this.getY();
		int amountToMoveX = x - mouseX;
		int amountToMoveY = y - mouseY;
		this.setX(x - amountToMoveX / maxSpeed);
		this.setY(y - amountToMoveY / maxSpeed);
		x = (int) this.getX();
		y = (int) this.getY();

		worldObj.grassCon.setMowed(x, y);
	}

}
