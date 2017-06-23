package it.polimi.ingsw.BianchiCorneo.maps.cards;

import it.polimi.ingsw.BianchiCorneo.maps.Table;
import it.polimi.ingsw.BianchiCorneo.players.Player;

/**
 * Object card that allows an human player to move instantly back to the human base
 * 
 * @author Mattia Bianchi
 *
 */
public class TeleportCard extends ObjectCard {
	private static final long serialVersionUID = 3318395570387218386L;
	private final String message = "You can return to your base.";
	
	/**
	 * Basic constructor
	 */
	public TeleportCard() {}
	
	@Override
	public String toString() {
		return "Teleport";
	}

	@Override
	public String action(Player p) {
		Table t = p.getMap();
		t.updatePos(p, t.getHumanBase());
		p.addToPath(t.getHumanBase());
		return null;
	}
	
	@Override
	public String getMsg() {
		return message;
	}
}
