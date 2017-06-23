package it.polimi.ingsw.BianchiCorneo.maps.cards;

import it.polimi.ingsw.BianchiCorneo.players.Human;
import it.polimi.ingsw.BianchiCorneo.players.Player;

/**
 * Object card that allows an human player to move by two sectors instead of one
 * 
 * @author Mattia Bianchi
 *
 */
public class AdrenalineCard extends ObjectCard {
	private static final long serialVersionUID = 7929353512727180740L;
	private final String message = "During this turn, you can move by two sectors instead of one.";
	
	/**
	 * Basic constructor
	 */
	public AdrenalineCard() {}
	
	@Override
	public String toString() {
		return "Adrenaline";
	}
	
	@Override
	public String action(Player p) {
		if (!p.hasMoved()) {
			((Human)p).useAdrenaline();
			return null;
		}
		return "You have already made your move this turn.";
	}
	
	@Override
	public String getMsg() {
		return message;
	}
}
