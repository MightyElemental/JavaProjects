package net.iridgames.munchkin.cards;

public class MonsterCard extends Card {

	public MonsterCard( String title, int level ) {
		super(title, Card.TYPE_DOOR);
		this.level = level;
	}

	private int level = 1;

	/** @return the level */
	public int getLevel() {
		return level;
	}

	/** @param level
	 *            the level to set */
	public void setLevel(int level) {
		this.level = level;
	}

}
