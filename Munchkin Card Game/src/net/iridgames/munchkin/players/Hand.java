package net.iridgames.munchkin.players;

import java.util.ArrayList;
import java.util.List;

import net.iridgames.munchkin.cards.Card;

public class Hand {

	private List<Card>	cardsInPlay		= new ArrayList<Card>();
	private List<Card>	cardsInHand		= new ArrayList<Card>();
	private List<Card>	cardsToTheSide	= new ArrayList<Card>();

	public int getCardsInHand() {
		return cardsInHand.size();
	}

	/** @return the cardsInPlay */
	public List<Card> getCardsInPlay() {
		return cardsInPlay;
	}

	/** @return the cardsToTheSide */
	public List<Card> getCardsToTheSide() {
		return cardsToTheSide;
	}

}
