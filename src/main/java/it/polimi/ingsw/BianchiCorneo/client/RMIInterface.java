package it.polimi.ingsw.BianchiCorneo.client;

import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.moves.Controller;
import it.polimi.ingsw.BianchiCorneo.players.Player;
import it.polimi.ingsw.BianchiCorneo.server.RMIServer;
import it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler;

import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIInterface implements NetworkInterface {
	protected Registry registry;
	protected RMIServer rmiServer;
	protected RMIHandler rmiHandler;
	protected Controller controller;
	
	@Override
	public Player connect() {
		try {
			Registry registry = LocateRegistry.getRegistry(2020);
			rmiServer = (RMIServer)registry.lookup("RMIServer");
		
			Player p = rmiServer.requestConn();
			
			rmiHandler = (RMIHandler)registry.lookup("RMIHandler");
			controller = (Controller)registry.lookup("Move");
			
			return p;
			
		} catch (ConnectException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		} catch (AccessException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		} catch (RemoteException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		} catch (NotBoundException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
		return null;
	}

	@Override
	public String newConn(Player p) throws RemoteException, NotBoundException {
		return rmiServer.waitingForConn(p.getIDGame());
	}

	@Override
	public String doAction(Player p, String s) throws RemoteException {
		return controller.doAction(p.getIDPlayer(), p.getIDGame(), s);
	}
	@Override
	public Player updatePlayer(Player p) throws RemoteException {
		return rmiHandler.updatePlayer(p.getIDPlayer(), p.getIDGame());
	}

	@Override
	public boolean isNotifiedEnd(int gameID) throws RemoteException {
		return rmiHandler.isNotifiedEnd(gameID);
	}

	@Override
	public Action getNotification(int gameID) throws RemoteException {
		return rmiHandler.getNotification(gameID);
	}

	@Override
	public String getEnd(int gameID) throws RemoteException {
		return rmiHandler.getEnd(gameID);
	}

	@Override
	public boolean isMyTurn(int playerID, int gameID) throws RemoteException {
		return rmiHandler.isYourTurn(playerID, gameID);
	}

	@Override
	public void playerName(int playerID, int gameID, String name) throws RemoteException {
		rmiHandler.playerName(playerID, gameID, name);
	}

	@Override
	public void close() {}
}
