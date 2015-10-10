package net.mightyelemental.basicengine;

import net.mightyelemental.basicengine.entities.Entity;
import net.mightyelemental.basicengine.world.World;

/** Be aware that you will have to supply this with the image objects you will be using */
@SuppressWarnings( "rawtypes" )
public abstract class BasicGame {

	public Class< ? extends Entity> playerClass;

	public BasicGame( Class< ? extends Entity> player ) {

	}

	public World world;

	/** Initialises the game */
	public abstract void init();

	/** Starts the game */
	public synchronized void startGameThread() {

	}

}
