package it.polimi.ingsw.BianchiCorneo.maps.cards;

import it.polimi.ingsw.BianchiCorneo.players.Human;
import it.polimi.ingsw.BianchiCorneo.players.Player;

/**
 * Object card that allows an human player to avoid to draw a card for the current turn
 * 
 * @author Mattia Bianchi
 *
 */
public class SedativeCard extends ObjectCard {
	private static final long serialVersionUID = -8812061970523485432L;
	private final String message = "In this turn you don't have to draw any sector card.";
	
	/**
	 * Basic constructor
	 */
	public SedativeCard() {}
	
	@Override
	public String toString() {
		return "Sedative";
	}

	@Override
	public String action(Player p) {
		((Human)p).useSedative();
		return null;
	}
	
	@Override
	public String getMsg() {
		return message;
	}
}
