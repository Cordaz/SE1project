package it.polimi.ingsw.BianchiCorneo.maps.cards;

import it.polimi.ingsw.BianchiCorneo.players.Player;

/**
 * Superclass of all sector cards
 * 
 * @author Mattia Bianchi
 *
 */
public abstract class SectorCard extends Card {
	private static final long serialVersionUID = 2320603907788096175L;
	protected String message;
	protected String appendix = "";
	
	/**
	 * Basic constructor
	 */
	public SectorCard() {}
	
	/**
	 * Applies the effect of the drawn card
	 * 
	 * @param p player that is using the card
	 * @return true if the  action of the card is done
	 */
	public abstract boolean action(Player p);
	
	/**
	 * Create the message of the card based on its effect
	 * 
	 * @param s string to append to the message
	 */
	public abstract void whatHappened(String s);
	
	@Override
	public String toString() {
		return message + appendix;
	}
}
