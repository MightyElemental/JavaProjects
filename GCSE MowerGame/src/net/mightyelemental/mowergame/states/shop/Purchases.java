package net.mightyelemental.mowergame.states.shop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.mightyelemental.mowergame.entities.MowerType;

public class Purchases {

	public Purchases() {
		boughtMowers.addAll(getMowers());
	}

	public List<MowerType> getMowers() {
		Field[] f = MowerType.class.getFields();
		List<MowerType> mowers = new ArrayList<MowerType>();
		for (int i = 0; i < f.length; i++) {
			if (f[i].getDeclaringClass() == MowerType.class) {
				try {
					mowers.add(((MowerType) f[i].get(null)));
				} catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
					// e.printStackTrace();
				}
			}
		}
		return mowers;
	}

	public List<MowerType> boughtMowers = new ArrayList<MowerType>();

	public boolean hasBoughtMower(MowerType mower) {
		return boughtMowers.contains(mower);
	}

	public void buyMower(MowerType mower) {
		if (!hasBoughtMower(mower)) {
			boughtMowers.add(mower);
		}
	}

}
