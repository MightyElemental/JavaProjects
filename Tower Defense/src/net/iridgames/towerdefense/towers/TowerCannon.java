package net.iridgames.towerdefense.towers;

import java.util.List;

import org.newdawn.slick.geom.Point;

import net.iridgames.towerdefense.MathHelper;
import net.iridgames.towerdefense.monsters.Monster;
import net.iridgames.towerdefense.world.World;

public class TowerCannon extends Tower {

	private static final long serialVersionUID = -7467369134509181775L;

	public TowerCannon(World worldObj, float x, float y) {
		super(worldObj, x, y, 7.5f);
		this.setTime(1500);
		this.setTopLayer("sniper");
	}

	@Override
	public void attackMonsters(List<Monster> monsters) {
		float angle = MathHelper.getAngle(new Point(this.getCenterX(), this.getCenterY()),
				new Point(target.getCenterX(), target.getCenterY())) - 180;
		if ( !monsters.isEmpty() ) {
			worldObj.getProjectileList().add(new Object[] { (x + 0.5f) * i, (y + 0.5f) * i, angle, 5, 50, false });
			// monsters.get(0).dead = true;
		}
	}

}
