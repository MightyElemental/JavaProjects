package net.mightyelemental.mowergame.entities;

public class MowerType {

	private String name;
	private float durability = 100;
	private float speed = 8f;
	private int size = 110;
	private float acceleration = 0.5f;

	private MowerType(String name, int size, int durability, float maxSpeed, float acceleration) {
		this.name = name;
		this.durability = durability;
		this.speed = maxSpeed;
		this.size = size;
		this.acceleration = acceleration;
	}

	public static MowerType MowveMonster = new MowerType("Mowve Monster", 110, 120, 5f, 0.2f);
	public static MowerType Hacker = new MowerType("Hack", 120, 500, 9f, 1f);

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

}
