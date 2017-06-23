package it.polimi.ingsw.BianchiCorneo.maps.cards;

import it.polimi.ingsw.BianchiCorneo.players.Player;

/**
 * Superclass of all object cards
 * 
 * @author Mattia Bianchi
 *
 */
public abstract class ObjectCard extends Card {
	private static final long serialVersionUID = -7637492612400184057L;

	/**
	 * Basic constructor
	 */
	public ObjectCard() {
		
	}
	
	@Override
	public abstract String toString();
	
	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}
	@Override
	public int hashCode() {
		return 0;
	}
	
	/**
	 * Applies the action done by the card
	 * 
	 * @param p player that uses the object
	 * @return an object that indicates the action done
	 */
	public abstract Object action(Player p);
	
	/**
	 * Getter method for the message of the card
	 * 
	 * @return the message of the card
	 */
	public abstract String getMsg();
	
	/**
	 * Notify that the action has been done correctly
	 * 
	 * @return a string that notifies that the action has been done correctly
	 */
	public String actionDone() {
		return "Card Used";
	}
}
