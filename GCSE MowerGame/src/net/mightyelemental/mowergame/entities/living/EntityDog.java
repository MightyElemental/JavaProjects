package net.mightyelemental.mowergame.entities.living;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.mightyelemental.mowergame.World;

public class EntityDog extends EntityLiving {
	
	
	private static final long serialVersionUID = -9006257819132425251L;
	
	public EntityDog( float x, float y, World worldObj ) {
		super(x, y, 70, 70, worldObj);
		this.damageToMower = 50;
		this.timeGain = -10;
		this.moneyGain = 10;
		this.setIcon("entities.dog");
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		super.update(gc, delta);
		if (path == null) {
			path = new MovePath(rand.nextInt(1280), rand.nextInt(720));
		}
		if (path.hasReached) {
			if (rand.nextInt(1000) < 15) {
				path = null;
				int x = (int) (rand.nextInt(1280));
				if (x <= 0) {
					x = 0;
				}
				if (x >= gc.getWidth()) {
					x = gc.getWidth();
				}
				path = new MovePath(x, rand.nextInt(720));
			}
		}
		path.update(gc, delta, this);
	}
	
}
