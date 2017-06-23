package it.polimi.ingsw.BianchiCorneo.server;

import it.polimi.ingsw.BianchiCorneo.players.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**Interface exposed by the server to permit the connection at the client
 * @author Andrea Corneo
 *
 */
public interface RMIServer extends Remote {
	/**Create the connection to server, instantiate the player
	 * @return Player
	 * @throws RemoteException
	 */
	public Player requestConn() throws RemoteException;
	
	/**
	 * @param idGame
	 * @return
	 * @throws RemoteException
	 */
	public String waitingForConn(int idGame) throws RemoteException;
}
