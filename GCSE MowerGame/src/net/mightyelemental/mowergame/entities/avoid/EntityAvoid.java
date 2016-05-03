package net.mightyelemental.mowergame.entities.avoid;

import net.mightyelemental.mowergame.World;
import net.mightyelemental.mowergame.entities.Entity;

public abstract class EntityAvoid extends Entity {

	private static final long serialVersionUID = 3671860235445980129L;

	public EntityAvoid(float x, float y, float width, float height, World worldObj) {
		super(x, y, width, height, worldObj);
	}

	/** The amount of money that the entity gives when killed */
	public float moneyGain = 0;

	/** The amount of time that the entity gives when killed */
	public float timeGain = 0;

	/** The amount of damage the mower takes when entity is hit */
	public int damageToMower = 0;

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

}
