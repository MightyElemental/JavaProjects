package net.iridgames.munchkin.players;

import java.util.ArrayList;
import java.util.List;

import net.iridgames.munchkin.cards.Card;
import net.iridgames.munchkin.cards.ClassCard;
import net.iridgames.munchkin.cards.PowerCard;

public class Player {

	private String name = "UNDEFINED_PLAYER";

	private int level = 1;

	private int money = 0;

	private Hand hand = new Hand();

	public Player( String name ) {
		this.setName(name);
	}

	public List<PowerCard> getPowers() {
		List<PowerCard> powers = new ArrayList<PowerCard>();

		for (Card card : hand.getCardsInPlay()) {
			if (card instanceof PowerCard) {
				powers.add((PowerCard) card);
			}
		}

		return powers;
	}

	public List<ClassCard> getClasses() {
		List<ClassCard> classes = new ArrayList<ClassCard>();

		for (Card card : hand.getCardsInPlay()) {
			if (card instanceof ClassCard) {
				classes.add((ClassCard) card);
			}
		}

		return classes;
	}

	/** @return the name */
	public String getName() {
		return name;
	}

	/** @param name
	 *            the name to set */
	public void setName(String name) {
		this.name = name;
	}

	/** @return the level */
	public int getLevel() {
		return level;
	}

	/** @param level
	 *            the level to set */
	public void setLevel(int level) {
		this.level = level;
	}

	/** @return the money */
	public int getMoney() {
		return money;
	}

	/** @param money
	 *            the money to set */
	public void setMoney(int money) {
		this.money = money;
	}

	/** @return the hand */
	public Hand getHand() {
		return hand;
	}

}
