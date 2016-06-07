package net.mightyelemental.mowergame.entities.avoid;

import java.awt.Point;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.World;
import net.mightyelemental.mowergame.entities.Entity;
import net.mightyelemental.mowergame.entities.EntityBloodSplat;

public class EntityAvoid extends Entity {

	private static final long serialVersionUID = 3671860235445980129L;

	public Random rand;

	public EntityAvoid(float x, float y, float width, float height, World worldObj) {
		super(x, y, width, height, worldObj);
		this.rand = worldObj.rand;
	}

	protected MovePath path;

	/** The amount of money that the entity gives when killed */
	public float moneyGain = 0;

	/** The amount of time that the entity gives when killed */
	public float timeGain = 0;

	/** The amount of damage the mower takes when entity is hit */
	public int damageToMower = 50;

	public boolean dead;

	public float getMoneyGain() {
		return moneyGain;
	}

	public void setMoneyGain(float moneyGain) {
		this.moneyGain = moneyGain;
	}

	public float getTimeGain() {
		return timeGain;
	}

	public void setTimeGain(float timeGain) {
		this.timeGain = timeGain;
	}

	public int getDamageToMower() {
		return damageToMower;
	}

	public void setDamageToMower(int damageToMower) {
		this.damageToMower = damageToMower;
	}

	public void setDead() {
		worldObj.spawnEntity(new EntityBloodSplat(this.getCenterX(), this.getCenterY(), worldObj));
		this.dead = true;
	}

	public MovePath getPath() {
		return path;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if (path != null) {
			if (!path.hasReached) {
				angle = MathHelper.getAngle(new Point(path.getX(), path.getY()),
						new Point((int) getCenterX(), (int) getCenterY()));
			}
		}
		this.getIcon().setRotation(angle + 90);
	}

}
