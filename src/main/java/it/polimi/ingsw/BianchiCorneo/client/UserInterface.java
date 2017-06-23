package it.polimi.ingsw.BianchiCorneo.client;

import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.players.Player;

/**
 * Methods that allow a graphical or a shell interaction with the user
 * 
 * @author Mattia Bianchi
 *
 */
public interface UserInterface {
	/**
	 * Draws a shell version of the map
	 * 
	 * @param t grid of sectors to draw
	 * @param sl list of reachable sectors for a player that can move
	 * @param s current sector of the player
	 * @param myTurn ability to move of the player
	 */
	public void drawMap(Player p);
	
	/**
	 * Shows connection status
	 */
	public void reqConn();
	
	/**
	 * Asks for the player's name
	 * 
	 * @return player's name
	 */
	public String reqName();
	
	/**
	 * Notifies that the client is waiting for his turn
	 * 
	 * @param p player
	 */
	public void waitTurn(Player p);
	
	/**
	 * Asks for the input of a sector
	 * 
	 * @param p player
	 * @return the sector chosen by the user
	 */
	public String getSector(Player p);
	
	/**
	 * Shows a message
	 * 
	 * @param p player
	 * @param s message to show
	 */
	public void viewMessage(String s);
	
	/**
	 * Asks to select the card to discard
	 * 
	 * @return the number of the card to discard
	 */
	public String selectToDiscard(String s);
	
	public String takeAction(Player p);
	
	/**
	 * Notifies the lack of players to begin a new game
	 */
	public void lackOfPlayers();
	
	/**
	 * Notifies the action done to all clients
	 * 
	 * @param notification action done
	 */
	public void serverNotifies(Action notification);
	
	/**
	 * Notifies user that the current session has ended.
	 */
	public void exitGame();

	/**
	 * Notifies that the player has been suspended due to inactivity
	 */
	public void banned();

	/**
	 * Notifies the start of your turn
	 */
	public void startTurn();
	
	/**
	 * Unlocks the waiting user interface to end the game
	 */
	public void unlock();
}
