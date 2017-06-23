package it.polimi.ingsw.BianchiCorneo.maps.cards;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * List of card used for drawing during the game
 * 
 * @author Mattia Bianchi
 *
 */
public abstract class Deck {
	protected ArrayList<Card> deckToShuff = new ArrayList<Card>();
	protected LinkedList<Card> deckReady = new LinkedList<Card>();
	
	/**
	 * Creates a new deck
	 */
	public abstract void initDeck();
	
	/**
	 * Orders the cards inside the list randomly
	 */
	public void shuffleDeck() {
		for (int i = 0; i < deckToShuff.size(); i++) {
			for (int j = deckToShuff.size(); j > 0; j--) {
				Random random = new Random();
				int rand = random.nextInt(j);
				Card c = deckToShuff.get(rand);
				deckToShuff.remove(rand);
				deckToShuff.add(c);
			}
		}
		for (int i = 0; i < deckToShuff.size(); i++) {
			deckReady.push(deckToShuff.get(i));
		}
		//Zeroing deck for saving used cards
		deckToShuff = new ArrayList<Card>();
	}
	
	/**
	 * Returns true if the deck is empty
	 * 
	 * @return true if no cards are in the deck
	 */
	public boolean isEmpty() {
		return deckReady.isEmpty();
	}
	
	/**
	 * Draws a card
	 * 
	 * @return the first card of the list
	 */
	public Card drawCard() {
		if (deckReady.isEmpty()) {
			shuffleDeck();
			if (deckReady.isEmpty()) {//Definetely terminated
				return new NoCardAvailable();
			}
		}
		
		return deckReady.pop();
	}
	
	/**
	 * Reinsert a card used in the deck
	 * 
	 * @param card
	 */
	public void insertCard(Card card) {
		deckToShuff.add(card);
		if (deckReady.isEmpty()) //was terminated
			shuffleDeck();
	}
}
