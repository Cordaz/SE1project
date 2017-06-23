package it.polimi.ingsw.BianchiCorneo.actions;

import it.polimi.ingsw.BianchiCorneo.players.Player;

/**This class represent an action notifying the killing of a player
 * @author Andrea Corneo
 *
 */
public class Killed extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7035686971756192973L;

	/**Constructor
	 * @param player killed
	 * @param message standard message
	 */
	public Killed(Player player, String message) {
		super(player, message);
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.actions.Action#toString()
	 */
	@Override
	public String toString() {
		return super.player.toFormattedString() + ": " + super.message;
	}

}
