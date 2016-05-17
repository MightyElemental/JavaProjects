package net.mightyelemental.mowergame.entities;

import java.awt.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.World;
import net.mightyelemental.mowergame.entities.avoid.EntityAvoid;
import net.mightyelemental.mowergame.entities.avoid.EntityGnome;
import net.mightyelemental.mowergame.entities.avoid.MovePath;

public class EntityMower extends Entity {

	private static final long serialVersionUID = 4112241142072508351L;

	public float maxSpeed = 5f;
	public float vel = 8f;

	public float health = 100f;

	public MovePath aiPath;

	public Rectangle bladeRect;

	public int animalsKilled;

	public int gnomesKilled;

	public boolean mowerHasAI;

	public EntityMower(float x, float y, World worldObj, boolean mowerHasAI) {
		super(x, y, 110, 110, worldObj);
		this.setIcon("entities.lawnMower");
		bladeRect = new Rectangle(x + width / 4, y + height / 4, width / 2.5f, height / 2.5f);
		this.mowerHasAI = mowerHasAI;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if (aiPath == null && mowerHasAI) {
			aiPath = new MovePath(worldObj.rand.nextInt(1280), worldObj.rand.nextInt(720));
		}
		if (aiPath != null) {
			if (this.getCenterY() > aiPath.getY() - 10 && this.getCenterY() < aiPath.getY() + 10) {
				if (this.getCenterX() > aiPath.getX() - 10 && this.getCenterX() < aiPath.getX() + 10) {
					aiPath.hasReached = true;
				}
			}
		}
		if (aiPath != null && aiPath.hasReached) {
			aiPath = null;
			aiPath = new MovePath(worldObj.rand.nextInt(1280), worldObj.rand.nextInt(720));
		}

		int mouseX = gc.getInput().getMouseX();
		int mouseY = gc.getInput().getMouseY();

		if (mowerHasAI) {
			mouseX = aiPath.getX();
			mouseY = aiPath.getY();
		}

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
		if (!mowerHasAI) {
			processHealth();
		} else {
			if (worldObj.getCollidingEntity(bladeRect) != null) {
				worldObj.getCollidingEntity(bladeRect).setDead();
			}
		}
		bladeRect.setCenterX(this.getCenterX());
		bladeRect.setCenterY(this.getCenterY());
		worldObj.grassCon.setMowed(bladeRect);
	}

	private void processHealth() {
		EntityAvoid ent = worldObj.getCollidingEntity(bladeRect);
		if (ent != null) {
			health -= ent.damageToMower;
			if (health <= 0) {
				health = 0;
			}
			worldObj.getCollidingEntity(bladeRect).setDead();
			MowerGame.gameState.timeTotalMs += ent.timeGain * 1000;
			MowerGame.gameState.timeMs += ent.timeGain * 1000;
			if (ent instanceof EntityGnome) {
				gnomesKilled++;
			} else {
				animalsKilled++;
			}
		}
	}

}
