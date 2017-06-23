package it.polimi.ingsw.BianchiCorneo.supervisor;


import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.players.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**This class exposes the RMI methods useful to the game logic, not included in the move controller.
 * For example this provides all the methods used for the turn alternation
 * @author Andrea Corneo
 *
 */
public interface RMIHandler extends Remote {
	
	/**Used to check if the player can play
	 * @param idPlayer
	 * @param idGame
	 * @return <b>true</b> if it is player's turn else <b>false</b>
	 * @throws RemoteException
	 */
	public boolean isYourTurn(int idPlayer, int idGame) throws RemoteException;

	/**Used to check if the game is still valid (not terminated)
	 * @param gameID
	 * @return <b>true</b> if the game is playable, else <b>false</b>
	 * @throws RemoteException
	 */
	public boolean isGameValid(int gameID) throws RemoteException;

	/**Used to check if the game hasn't already started
	 * @param gameID
	 * @return <b>true</b> if the supervisor hasn't started the game, else <b>false</b>
	 * @throws RemoteException
	 */
	public boolean isWaiting(int gameID) throws RemoteException;
	
	/**Signals that a player has started/finished is turn
	 * @param idPlayer
	 * @param idGame
	 * @param b <b>true</b> if started, <b>false</b> if ended
	 * @throws RemoteException
	 */
	public void setPlaying(int idPlayer, int idGame, boolean b) throws RemoteException;
	
	/**Setter for player's name
	 * @param idPlayer
	 * @param idGame
	 * @param name
	 * @throws RemoteException
	 */
	public void setPlayerName(int idPlayer, int idGame, String name) throws RemoteException;
	
	/**Used to check if the player has been suspended due to inactivity or for other reasons (killed...)
	 * @param idPlayer
	 * @param idGame
	 * @return <b>true</b> if suspended, else <b>false</b>
	 * @throws RemoteException
	 */
	public boolean isSuspendedPlayer(int idPlayer, int idGame) throws RemoteException;
	
	/**Update the player, sending it to the client
	 * @param idPlayer
	 * @param idGame
	 * @return current player object
	 * @throws RemoteException
	 */
	public Player updatePlayer(int idPlayer, int idGame) throws RemoteException;
	
	/**Check if the game has ended and if there is the end message to read
	 * @param idGame
	 * @return <b>true</b> if the end message is ready, else <b>false</b>
	 * @throws RemoteException
	 */
	public boolean isNotifiedEnd(int idGame) throws RemoteException;
	
	/**Remote getter for the notification
	 * @param idGame
	 * @return action happened
	 * @throws RemoteException
	 */
	public Action getNotification(int idGame) throws RemoteException;

	/**Remote getter for end message
	 * @param gameID
	 * @return end message
	 * @throws RemoteException
	 */
	public String getEnd(int gameID) throws RemoteException;

	/**Remote setter for player's name
	 * @param idPlayer
	 * @param idGame
	 * @param name
	 * @throws RemoteException
	 */
	public void playerName(int idPlayer, int idGame, String name) throws RemoteException;

}
