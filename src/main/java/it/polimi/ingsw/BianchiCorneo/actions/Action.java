package it.polimi.ingsw.BianchiCorneo.actions;

import it.polimi.ingsw.BianchiCorneo.players.Player;

import java.io.Serializable;

/** This class offers method to registry any action happened during the game (i.e. when a player use an object)
 * @author Andrea Corneo
 *
 */
public class Action implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3661198712159232073L;
	protected String message;
	protected Player player;
	
	/**Constructor
	 * @param player that made the action
	 * @param message representing the action
	 */
	public Action(Player player, String message) {
		this.player = player;
		this.message = message;
	}
	
	/**Set the message of the action: the string that states what happened.
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return player + ": " + message;
	}
	
	public String getMessage() {
		return message;
	}
	
	/**Return the player that had done the action
	 * @return player
	 */
	public Player who() {
		return player;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Action) || arg0 == null)
			return false;
		return ((Action)arg0).toString().equals(this.toString());
	}
	
	@Override
	public int hashCode() {
		return -1;
	}
}
