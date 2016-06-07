package net.mightyelemental.mowergame.entities;

public class MowerTypes {

	private String name;
	private int durability = 100;
	private float speed = 5f;
	private int size = 110;

	private MowerTypes(String name, int size, int durability, float maxSpeed) {
		this.name = name;
		this.durability = durability;
		this.speed = maxSpeed;
		this.size = size;
	}

	public MowerTypes MowveMonster = new MowerTypes("Mowve Monster", 120, 140, 4f);

	public String getName() {
		return name;
	}

	public int getDurability() {
		return durability;
	}

	public float getSpeed() {
		return speed;
	}

	public int getSize() {
		return size;
	}

}
