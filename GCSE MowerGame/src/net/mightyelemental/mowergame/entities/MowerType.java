package net.mightyelemental.mowergame.entities;

public class MowerType {

	private String name;
	private float durability = 100;
	private float speed = 5f;
	private int size = 110;

	private MowerType(String name, int size, int durability, float maxSpeed) {
		this.name = name;
		this.durability = durability;
		this.speed = maxSpeed;
		this.size = size;
	}

	public static MowerType MowveMonster = new MowerType("Mowve Monster", 120, 140, 4f);
	public static MowerType Hacker = new MowerType("Hack", 120, 500, 6f);

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

}
