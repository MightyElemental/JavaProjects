package net.mightyelemental.mowergame.entities;

import org.newdawn.slick.Image;

import net.mightyelemental.mowergame.MowerGame;

public class MowerType {

	private String name;
	private int durabilityBase = 100, durability = durabilityBase, durabilityLevel = 1;
	private float speedBase = 8f, speed = speedBase, speedLevel = 1;
	private int size = 110;
	private float accelerationBase = 0.5f, acceleration = accelerationBase;
	private String imgPath = "entities.mowers.lawnMower";
	private String description = "";
	private int price = 100;
	private boolean hidden = false;
	private int turnAngle = 2;

	private MowerType(String name, int size, int durability, float maxSpeed, float acceleration, String imgPath) {
		this.name = name;
		this.durabilityBase = durability;
		this.durability = durabilityBase;
		this.speedBase = maxSpeed;
		this.speed = speedBase;
		this.size = size;
		this.accelerationBase = acceleration;
		this.acceleration = accelerationBase;
		this.imgPath = imgPath;
	}

	public static MowerType MowveMonster = new MowerType("Mowve Monster", 140, 100, 5f, 0.2f,
			"entities.mowers.mowveMonster")
					.setDescription(
							"The most expensive mower but the most powerful. This mower is for the professionals.")
					.setPrice(10000);
	public static MowerType Hacker = new MowerType("Hack", 120, 500, 9f, 1f, "entities.trump2")
			.setDescription("Don't you dare try and cheat your way through the game ;)").setPrice(1000000000)
			.setHidden(true).setTurnAngle(5);
	public static MowerType DonaldMower = new MowerType("Donald Mower", 100, 1, 6f, 1f, "entities.trump")
			.setDescription("Use this mower at the cost of a small $1,000,000 dollars").setPrice(1000000)
			.setHidden(false).setTurnAngle(10);

	public String getName() {
		return name;
	}

	public int getBaseDurability() {
		return durabilityBase;
	}

	public float getBaseSpeed() {
		return speedBase;
	}

	public int getSize() {
		return size;
	}

	public float getBaseAcceleration() {
		return accelerationBase;
	}

	public String getImgPath() {
		return imgPath;
	}

	public Image getDisplayIcon() {
		return MowerGame.resLoader.loadImage(getImgPath());
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	public int getDurability() {
		return durability;
	}

	public float getSpeed() {
		return speed;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public String getDescription() {
		return description;
	}

	public MowerType setDescription(String description) {
		this.description = description;
		return this;
	}

	public int getDurabilityLevel() {
		return durabilityLevel;
	}

	public void setDurabilityLevel(int durabilityLevel) {
		this.durabilityLevel = durabilityLevel;
	}

	public float getSpeedLevel() {
		return speedLevel;
	}

	public void setSpeedLevel(float speedLevel) {
		this.speedLevel = speedLevel;
	}

	public float getPrice() {
		return price;
	}

	public MowerType setPrice(int price) {
		this.price = price;
		return this;
	}

	public boolean isHidden() {
		return hidden;
	}

	public MowerType setHidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	public int getTurnAngle() {
		return turnAngle;
	}

	/** The larger the turnAngle the smaller the turn radius */
	public MowerType setTurnAngle(int turnAngle) {
		this.turnAngle = turnAngle;
		return this;
	}

}
