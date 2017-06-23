package it.polimi.ingsw.BianchiCorneo.maps.cards;

import it.polimi.ingsw.BianchiCorneo.players.Player;

/**
 * Object card that allows an human player to protect himself from the next attack
 * 
 * @author Mattia Bianchi
 *
 */
public class DefenceCard extends ObjectCard {
	private static final long serialVersionUID = 2243724075696003878L;
	private final String message = "Your sheild is on, you can survive an attack.";

	/**
	 * Basic constructor
	 */
	public DefenceCard() {}
	
	@Override
	public String toString() {
		return "Defense";
	}

	@Override
	public String action(Player p) {
		return null;
	}
	
	@Override
	public String getMsg() {
		return message;
	}
}
