package net.mightyelemental.basicengine.entities;

import java.awt.Rectangle;

import net.mightyelemental.basicengine.world.World;

@SuppressWarnings( "rawtypes" )
public abstract class Entity<imageType, inputClass> {

	public imageType displayImage;

	public boolean beingHurt;

	public float maximumWeightCap;

	public World worldObj;

	public int	sizeX;
	public int	sizeY;

	public float	xPos;
	public float	yPos;

	private boolean isSolid;

	public double rotation = 0;

	private Rectangle hitbox;

	public static String UNLOCALISED_NAME = "ENTITY";

	public String name;

	public Entity( int x, int y, World worldObj ) {
		this.worldObj = worldObj;
		this.xPos = x * 50;
		this.yPos = y * 50;
		this.worldObj = worldObj;
		this.sizeX = 50;
		this.sizeY = 50;
	}

	public int getCenterX() {
		return this.getXInt() + this.sizeX / 2;
	}

	public int getXInt() {
		return (int) xPos;
	}

	public float getX() {
		return xPos;
	}

	public void setX(float x) {
		this.xPos = x;
	}

	public void setY(float y) {
		this.yPos = y;
	}

	public float getY() {
		return yPos;
	}

	public int getCenterY() {
		return this.getYInt() + this.sizeY / 2;
	}

	public int getYInt() {
		return (int) yPos;
	}

	public String getName() {
		return name;
	}

	public Entity<imageType, inputClass> setSizeY(int size) {
		this.sizeY = size;
		return this;
	}

	public Entity<imageType, inputClass> setSizeX(int size) {
		this.sizeX = size;
		return this;
	}

	public int getSizeX() {
		return this.sizeX;
	}

	public int getSizeY() {
		return this.sizeY;
	}

	public Rectangle getHitbox() {
		return this.hitbox;
	}

	/** Updates the entity This should always call the super method
	 * 
	 * @param input
	 *            just in case the entity requires input from keyboard or mouse
	 * @param delta
	 *            used to make entities move at the correct speed no matter the amount of lag */
	public void update(inputClass input, int delta) {
		hitbox = new Rectangle(this.getXInt(), this.getYInt(), sizeX, sizeY);
	}

	public Entity<imageType, inputClass> setName(String name) {
		this.name = name;
		return this;
	}

	public Entity<imageType, inputClass> setImage(imageType loc) {
		displayImage = loc;
		// System.out.println(this.getName()+" size X: "+getSizeX()+ " Image X: "+displayImage.getTileWidth());
		return this;
	}

	public imageType getImage() {
		return displayImage;
	}

	public String getUnlocalisedName() {
		return Entity.UNLOCALISED_NAME;
	}

	public boolean isSolid() {
		return isSolid;
	}

	public Entity<imageType, inputClass> setSolid(boolean isSolid) {
		this.isSolid = isSolid;
		return this;
	}

}
