package it.polimi.ingsw.BianchiCorneo.supervisor;

import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.players.Player;
import it.polimi.ingsw.BianchiCorneo.players.PlayerList;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**Implementation of RMIHandler remote interface
 * @author Andrea Corneo
 *
 */
public class RMIHandlerImpl implements RMIHandler {
	private Map<Integer, GameSupervisor> svList;
	
	/**Constructor
	 * 
	 */
	public RMIHandlerImpl() {
		svList = new HashMap<Integer, GameSupervisor>();
	}

	/**Add the gameID<i>th</i> supervisor to the list
	 * @param gameID
	 * @param sv
	 */
	public void addGame(int gameID, GameSupervisor sv) {
		svList.put(gameID, sv);
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler#isGameValid(int)
	 */
	@Override
	public boolean isGameValid(int gameID) throws RemoteException {
		return svList.get(gameID).isGameValid();
	}

	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler#isWaiting(int)
	 */
	@Override
	public boolean isWaiting(int gameID) throws RemoteException {
		return svList.get(gameID).isWaiting();
	}
	
	/**Searches the player in the correct game
	 * @param idPlayer
	 * @param idGame
	 * @return correct Player object
	 */
	private Player matchPlayer(int idPlayer, int idGame) {
		return svList.get(idGame).getPlayerList().get(idPlayer);
	}

	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler#setPlaying(int, int, boolean)
	 */
	@Override
	public void setPlaying(int idPlayer, int idGame, boolean b) throws RemoteException {
		setPlaying(matchPlayer(idPlayer, idGame), b);
	}
	
	/**Set the matched player
	 * @param p
	 * @param b
	 */
	public void setPlaying(Player p, boolean b) {
		p.setPlaying(b);
		if (!b)
			synchronized(svList.get(p.getIDGame())) {svList.get(p.getIDGame()).notifyAll();}
	}

	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler#setPlayerName(int, int, java.lang.String)
	 */
	@Override
	public void setPlayerName(int idPlayer, int idGame, String name) throws RemoteException {
		setPlayerName(matchPlayer(idPlayer, idGame), name);
	}
	
	/**Set the matched player
	 * @param p
	 * @param name
	 */
	public void setPlayerName(Player p, String name) {
		p.setName(name);
	}

	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler#isSuspendedPlayer(int, int)
	 */
	@Override
	public boolean isSuspendedPlayer(int idPlayer, int idGame) throws RemoteException {
		return isSuspendedPlayer(matchPlayer(idPlayer, idGame));
	}
	
	/**Get the matched player
	 * @param p
	 * @return
	 */
	public boolean isSuspendedPlayer(Player p) {
		return p.isSuspended();
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler#updatePlayer(int, int)
	 */
	@Override
	public Player updatePlayer(int idPlayer, int idGame) throws RemoteException {
		return updatePlayer(matchPlayer(idPlayer, idGame));
	}
	
	/**Get the matched player
	 * @param p
	 * @return
	 */
	public Player updatePlayer(Player p) {
		PlayerList pl = svList.get(p.getIDGame()).getPlayerList();
		for (int i = 0; i < pl.size(); i++) {
			if (pl.get(i).equals(p))
				return pl.get(i);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler#isYourTurn(int, int)
	 */
	@Override
	public boolean isYourTurn(int idPlayer, int idGame) throws RemoteException {
		if (svList.get(idGame).isGameValid())
			return isYourTurn(matchPlayer(idPlayer, idGame));
		return false;
	}
	
	/**Get the matched player
	 * @param p
	 * @return
	 */
	public boolean isYourTurn(Player p) {
		if (svList.containsKey(p.getIDGame()))
			return svList.get(p.getIDGame()).isYourTurn(p);
		return false;
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler#isNotifiedEnd(int)
	 */
	@Override
	public boolean isNotifiedEnd(int idGame) throws RemoteException {
		return !svList.get(idGame).isGameValid();
	}

	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler#getNotification(int)
	 */
	@Override
	public Action getNotification(int idGame) throws RemoteException {
		return svList.get(idGame).getMonitor().getNotification();
	}

	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler#getEnd(int)
	 */
	@Override
	public String getEnd(int gameID) throws RemoteException {
		while (!svList.get(gameID).isRecordReady())
			;
		return svList.get(gameID).getMonitor().getEnd();
	}

	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler#playerName(int, int, java.lang.String)
	 */
	@Override
	public void playerName(int idPlayer, int idGame, String name) throws RemoteException {
		matchPlayer(idPlayer, idGame).setName(name);
	}
}
