package net.mightyelemental.mowergame.states.shop;

import java.util.ArrayList;
import java.util.List;

import net.mightyelemental.mowergame.entities.MowerType;

public class Purchases {

	public Purchases() {

	}

	public int durabilityLevel = 0;
	public int speedLevel = 0;

	public List<MowerType> boughtMowers = new ArrayList<MowerType>();

	public boolean hasBoughtMower(MowerType mower) {
		return boughtMowers.contains(mower);
	}
	
	

}
