package it.polimi.ingsw.BianchiCorneo.server;

import it.polimi.ingsw.BianchiCorneo.players.Alien;
import it.polimi.ingsw.BianchiCorneo.players.CharacterName;
import it.polimi.ingsw.BianchiCorneo.players.Human;
import it.polimi.ingsw.BianchiCorneo.players.Player;
import it.polimi.ingsw.BianchiCorneo.supervisor.GameSupervisor;

import java.rmi.RemoteException;

public class RMIServerImpl implements RMIServer {
	private Server server;
	private GameSupervisor sv;
	
	/**Constructor
	 * @param server
	 * @throws RemoteException
	 */
	public RMIServerImpl(Server server) throws RemoteException {
		this.server = server;
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.server.RMIServer#requestConn()
	 */
	@Override
	public Player requestConn() throws RemoteException {
		Player p;
		synchronized (server) {
			if (server.numPlayer() % 2 == 0)
				p = new Alien(server.numPlayer(), server.getGame(), server.getTable());
			else
				p = new Human(server.numPlayer(), server.getGame(), server.getTable());
			server.getPlayerList().add(p);
			p.setCharName(CharacterName.nextPlayerName());
			server.incNumPlayer();
			if (server.numPlayer() == 1) {
				server.firstConn();
				server.notifyAll();
			}
		}
		System.out.println("RMI-player added");
		return p;
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.server.RMIServer#waitingForConn(int, java.lang.String)
	 */
	@Override
	public String waitingForConn(int idGame) throws RemoteException {
		String value;
		if(server.getGame() == idGame)
			value = checkGame(server.getSupervisor());
		else
			value = checkGame(sv);
		
		return value;
	}
	
	private String checkGame(GameSupervisor sv) {
		if (sv.isWaiting())
			return "wait";
		if (!sv.isGameValid())
			return "no";
		return "ok";
	}
	
	public void setSupervisor(GameSupervisor sv) {
		this.sv = sv;
	}
}
