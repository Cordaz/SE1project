package it.polimi.ingsw.BianchiCorneo.client;

import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.players.Player;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface NetworkInterface {
	
	/**
	 * Establish a new connection
	 * 
	 * @return the player associated on the server
	 */
	public Player connect();
	
	/**
	 * Waits for the game to start
	 * 
	 * @param p
	 * @return an info from the server on the connection
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public String newConn(Player p) throws RemoteException, NotBoundException;

	/**
	 * Sends to the server the action to do
	 * 
	 * @param p player that wants to do the action
	 * @param s action
	 * @return an info from the server on the action done
	 * @throws RemoteException
	 */
	public String doAction(Player p, String s) throws RemoteException;

	/**
	 * Updates the infos of the player
	 * 
	 * @param p
	 * @return the player updated
	 * @throws RemoteException
	 */
	public Player updatePlayer(Player p) throws RemoteException;
	
	/**Check if the client has to be notified for the end
	 * @param gameID
	 * @return <b>true</b> if there is a notification, else <b>false</b>
	 * @throws RemoteException
	 */
	public boolean isNotifiedEnd(int gameID) throws RemoteException;

	/**Ask the server for the notification
	 * @param gameID
	 * @return Action
	 */
	public Action getNotification(int gameID) throws RemoteException;
	
	/**Get the game end message from the server
	 * @param gameID
	 * @return formatted string stating how the game ended
	 * @throws RemoteException
	 */
	public String getEnd(int gameID) throws RemoteException;

	/**Ask the server if it's player's turn
	 * @return <b>true</b> if it's player's turn, else <b>false</b>
	 */
	public boolean isMyTurn(int playerID, int gameID) throws RemoteException;

	/**Set player's name on the server
	 * @param playerID
	 * @param gameID
	 * @param name
	 * @throws RemoteException
	 */
	public void playerName(int playerID, int gameID, String name) throws RemoteException;

	/**Close the connection properly;
	 * 
	 */
	public void close();
}
