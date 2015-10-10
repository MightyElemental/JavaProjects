package net.mightyelemental.basicengine.items;

public class Item<T> {

	public T displayIcon;

	public String	name	= "UNSPECIFIED";
	public double	weightTotal;

	public Item( String name, double weightPerCM3, int size ) {
		this.name = name;
		this.weightTotal = weightPerCM3 * size;
	}

	public Item<T> setImage(T image) {
		displayIcon = image;
		return this;
	}

	public T getImage() {
		return this.displayIcon;
	}

}
