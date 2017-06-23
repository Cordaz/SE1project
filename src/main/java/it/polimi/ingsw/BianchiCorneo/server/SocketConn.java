package it.polimi.ingsw.BianchiCorneo.server;

import it.polimi.ingsw.BianchiCorneo.players.Alien;
import it.polimi.ingsw.BianchiCorneo.players.CharacterName;
import it.polimi.ingsw.BianchiCorneo.players.Human;
import it.polimi.ingsw.BianchiCorneo.players.Player;
import it.polimi.ingsw.BianchiCorneo.supervisor.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**This class accept socket connection and instantiate the corresponding player and ClientHandler
 * @author Andrea Corneo
 *
 */
public class SocketConn implements Runnable {
	private ServerSocket serverSocket;
	private ServerSocket serverSocketNotifier;
	private Server server;
	
	private ExecutorService executor;
	private boolean stop = false;
	
	/**Constructor
	 * @param server
	 * @param ss
	 */
	public SocketConn(Server server, ServerSocket ss, ServerSocket notifier) {
		this.server = server;
		this.serverSocket = ss;
		this.serverSocketNotifier = notifier;
	}
	
	/**Wait for socket request of connection
	 * @throws IOException
	 */
	public void waitSocketConn() throws IOException {
		while (!stop) {
			Socket socket = serverSocket.accept();
			Socket socketNotifier = serverSocketNotifier.accept();
			//Connection requested
			synchronized (server) {
				Player p;
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
				
				System.out.println("Socket-player added");
				ClientHandler clientHandler = new ClientHandler(server.getSupervisor(), socket, socketNotifier, p, server.getController());
				executor.submit(clientHandler);
			}
		}
	}
	
	/**Stop listening
	 * 
	 */
	public synchronized void stop() {
		stop = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			executor = Executors.newCachedThreadPool();
			waitSocketConn();
		} catch (IOException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
	}

}
