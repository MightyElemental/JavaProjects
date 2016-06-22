package net.mightyelemental.mowergame.entities;

public class MowerType {

	private String name;
	private float durability = 100;
	private float speed = 8f;
	private int size = 110;
	private float acceleration = 0.5f;
	private String imgPath = "entities.mowers.lawnMower";

	private MowerType(String name, int size, int durability, float maxSpeed, float acceleration, String imgPath) {
		this.name = name;
		this.durability = durability;
		this.speed = maxSpeed;
		this.size = size;
		this.acceleration = acceleration;
		this.imgPath = imgPath;

	}

	public static MowerType MowveMonster = new MowerType("Mowve Monster", 110, 120, 5f, 0.2f,
			"entities.mowers.mowveMonster");
	public static MowerType Hacker = new MowerType("Hack", 120, 500, 9f, 1f, "entities.mowers.mowveMonster");

	public String getName() {
		return name;
	}

	public float getDurability() {
		return durability;
	}

	public float getSpeed() {
		return speed;
	}

	public int getSize() {
		return size;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public String getImgPath() {
		return imgPath;
	}

}
