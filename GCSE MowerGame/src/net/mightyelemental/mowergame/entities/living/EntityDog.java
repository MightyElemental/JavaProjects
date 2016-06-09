package net.mightyelemental.mowergame.entities.living;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.mightyelemental.mowergame.World;

public class EntityDog extends EntityLiving {

	private static final long serialVersionUID = -9006257819132425251L;

	public EntityDog(float x, float y, World worldObj) {
		super(x, y, 70, 70, worldObj);
		this.damageToMower = 60;
		this.timeGain = -10;
		this.moneyGain = 10;
		this.setIcon("entities.dog");
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		super.update(gc, delta);
		if (path == null) {
			path = new MovePath(worldObj.lawnMower, 2f);
		}
		path.update(gc, delta, this);
	}

}
