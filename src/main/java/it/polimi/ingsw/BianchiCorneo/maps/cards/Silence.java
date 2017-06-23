package it.polimi.ingsw.BianchiCorneo.maps.cards;

import it.polimi.ingsw.BianchiCorneo.players.Player;

/**
 * Sector card that notifies that nothing happened
 * 
 * @author Mattia Bianchi
 *
 */
public class Silence extends SectorCard {
	private static final long serialVersionUID = -9189905921547033255L;

	/**
	 * Basic constructor
	 */
	public Silence() {}
	
	@Override
	public void whatHappened(String s) {
		super.message = "Silence";
	}

	@Override
	public boolean action(Player p) {
		return false;
	}
}
