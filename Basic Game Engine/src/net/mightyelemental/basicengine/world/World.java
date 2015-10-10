package net.mightyelemental.basicengine.world;

import net.mightyelemental.basicengine.BasicGame;
import net.mightyelemental.basicengine.entities.Entity;

@SuppressWarnings( "rawtypes" )
public abstract class World<inputClass> {

	public Entity player;

	public World( BasicGame bg ) {
		try {
			this.player = bg.playerClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/** Used to refresh the world
	 * 
	 * @param input
	 *            just in case the world object requires input from keyboard or mouse
	 * @param delta
	 *            used to make world update at the correct speed no matter the amount of lag */
	public abstract void update(inputClass input, int delta);

}
