package net.iridgames.munchkin.cards;

public abstract class ItemCard extends Card {

	private int value = 0;

	public ItemCard( String title, int value ) {
		super(title, Card.TYPE_TREASURE);
		this.setValue(value);
	}

	/** @return the value */
	public int getValue() {
		return value;
	}

	/** @param value
	 *            the value to set */
	public ItemCard setValue(int value) {
		this.value = value;
		return this;
	}

}
