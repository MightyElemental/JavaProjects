package net.iridgames.munchkin.cards;

public class BonusCard extends ItemCard {

	private int bonus = 0;

	public BonusCard( String title, int value, int bonus ) {
		super(title, value);
		this.setBonus(bonus);
	}

	/** @return the bonus */
	public int getBonus() {
		return bonus;
	}

	/** @param bonus
	 *            the bonus to set */
	public BonusCard setBonus(int bonus) {
		this.bonus = bonus;
		return this;
	}

}
