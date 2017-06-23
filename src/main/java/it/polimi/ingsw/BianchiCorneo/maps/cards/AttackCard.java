package it.polimi.ingsw.BianchiCorneo.maps.cards;

import it.polimi.ingsw.BianchiCorneo.players.Player;
import it.polimi.ingsw.BianchiCorneo.players.PlayerList;

/**
 * Object card that allows an human player to attack his current sector
 * 
 * @author Mattia Bianchi
 *
 */
public class AttackCard extends ObjectCard {
	private static final long serialVersionUID = 5346597423767928763L;
	private final String message = "You can attack your current sector.";
	
	/**
	 * Basic constructor
	 */
	public AttackCard() {}
	
	@Override
	public String toString() {
		return "Attack";
	}

	@Override
	public PlayerList action(Player p) {
		p.attack();
		return p.getMap().attackSector(p.getCurSector());
	}
	
	@Override
	public String getMsg() {
		return message;
	}
}
