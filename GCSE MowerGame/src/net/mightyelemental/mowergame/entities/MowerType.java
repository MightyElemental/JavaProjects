package net.mightyelemental.mowergame.entities;

import org.newdawn.slick.Image;

import net.mightyelemental.mowergame.MowerGame;

public class MowerType {

	private String name;
	private float durabilityBase = 100, durability = durabilityBase, durabilityLevel;
	private float speedBase = 8f, speed = speedBase, speedLevel;
	private int size = 110;
	private float accelerationBase = 0.5f, acceleration = accelerationBase;
	private String imgPath = "entities.mowers.lawnMower";
	private String description = "";

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

	public static MowerType MowveMonster = new MowerType("Mowve Monster", 110, 100, 5f, 0.2f,
			"entities.mowers.mowveMonster2")
					.setDescription("This powerful mower will slice the grass as if it were a hot knife to butter");
	public static MowerType Hacker = new MowerType("Hack", 120, 500, 9f, 1f, "entities.trump2");
	public static MowerType DonaldMower = new MowerType("Donald Mower", 100, 1, 6f, 1f, "entities.trump");
	public static MowerType SteveMower = new MowerType("SteveTope123", 200, 1200, 6f, 1f, "entities.mowers.steve");

	public String getName() {
		return name;
	}

	public float getBaseDurability() {
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

	public void setDurability(float durability) {
		this.durability = durability;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	public float getDurability() {
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

	public float getDurabilityLevel() {
		return durabilityLevel;
	}

	public void setDurabilityLevel(float durabilityLevel) {
		this.durabilityLevel = durabilityLevel;
	}

	public float getSpeedLevel() {
		return speedLevel;
	}

	public void setSpeedLevel(float speedLevel) {
		this.speedLevel = speedLevel;
	}

}
