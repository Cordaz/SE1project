package it.polimi.ingsw.BianchiCorneo.maps.cards;

import it.polimi.ingsw.BianchiCorneo.players.Player;

/**
 * Object card that allows an human player to move by two sectors instead of one
 * 
 * @author Mattia Bianchi
 *
 */
public class Noise extends SectorCard {
	private static final long serialVersionUID = -1521930777725770455L;
	protected boolean real;
	
	/**
	 * Basic constructor
	 * 
	 * @param b set the kind of noise that will be done from the player
	 */
	public Noise(boolean b) {
		super();
		this.real = b;
	}
	
	/**
	 * Returns the kind of noise done by the card
	 * 
	 * @return true if noise on the current sector
	 */
	public boolean isReal() {
		return real;
	}
	
	@Override
	public boolean action(Player p) {
		if (real)
			p.makeNoise(p.getCurSector());
		return real;
	}
	
	@Override
	public void whatHappened(String s) {
		if (real)
			super.message = "Noise: You have just made noise in your current sector";
		else
			super.message = "Noise in other sectors: You can fake a noise";
	}
}
