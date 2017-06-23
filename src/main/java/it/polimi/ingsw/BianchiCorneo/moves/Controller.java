package it.polimi.ingsw.BianchiCorneo.moves;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Controller extends Remote {
	
	/**
	 * Method that receives a command from the client, checks it and returns the result of the action.
	 * 
	 * @param idPlayer player's id
	 * @param idGame game's id
	 * @param s command sent
	 * @return the result of the action
	 * @throws RemoteException
	 */
	public String doAction(int idPlayer, int idGame, String s) throws RemoteException;
}