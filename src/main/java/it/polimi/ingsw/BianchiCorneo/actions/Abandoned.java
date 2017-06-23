package it.polimi.ingsw.BianchiCorneo.actions;

import it.polimi.ingsw.BianchiCorneo.players.Player;

/**This class represent the action notifying that a player has left the game
 * @author Andrea Corneo
 *
 */
public class Abandoned extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 911342537572879964L;

	/**Constructor
	 * @param player that left the game
	 * @param message to print in the client
	 */
	public Abandoned(Player player, String message) {
		super(player, message);
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.actions.Action#toString()
	 * @return player formatted string
	 */
	@Override
	public String toString() {
		return super.player.toFormattedString() + ": " + super.message;
	}

}
