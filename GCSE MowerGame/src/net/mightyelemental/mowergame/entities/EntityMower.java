package net.mightyelemental.mowergame.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.mightyelemental.mowergame.World;

public class EntityMower extends Entity {
	
	
	private static final long serialVersionUID = 4112241142072508351L;
	
	public float maxSpeed = 5f;
	
	public EntityMower( float x, float y, World worldObj ) {
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
		float amountToMoveX = x - mouseX;
		float amountToMoveY = y - mouseY;
		
		float xRatio = amountToMoveX / (amountToMoveX + amountToMoveY);
		float yRatio = amountToMoveY / (amountToMoveX + amountToMoveY);
		amountToMoveX = (xRatio * 25);
		amountToMoveY = (yRatio * 25);
		System.out.println("X:" + amountToMoveX + " Y:" + amountToMoveY);
		
		float shiftX = amountToMoveX / 10;
		float shiftY = amountToMoveY / 10;
		if (amountToMoveX / maxSpeed > maxSpeed) {
			shiftX = maxSpeed;
		} else if (amountToMoveX / maxSpeed < -maxSpeed) {
			shiftX = -maxSpeed;
		}
		if (amountToMoveY / maxSpeed > maxSpeed) {
			shiftY = maxSpeed;
		} else if (amountToMoveY / maxSpeed < -maxSpeed) {
			shiftY = -maxSpeed;
		}
		this.setX(x - shiftX);
		this.setY(y - shiftY);
		x = (int) this.getX();
		y = (int) this.getY();
		
		worldObj.grassCon.setMowed(x, y);
	}
	
}
