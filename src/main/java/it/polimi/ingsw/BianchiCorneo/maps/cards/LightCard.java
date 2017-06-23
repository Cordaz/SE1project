package it.polimi.ingsw.BianchiCorneo.maps.cards;

import it.polimi.ingsw.BianchiCorneo.players.Player;

/**
 * Object card that allows an human player show every presence on a sector and its adjacent
 * 
 * @author Mattia Bianchi
 *
 */
public class LightCard extends ObjectCard {
	private static final long serialVersionUID = -1437683256083819588L;
	private final String message = "Choose a sector: You can reveal the position of players located in that sector and in its adjacent ones.";
	
	/**
	 * Basic constructor
	 */
	public LightCard() {}
	
	@Override
	public String toString() {
		return "Light";
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
