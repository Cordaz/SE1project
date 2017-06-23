package it.polimi.ingsw.BianchiCorneo.server;

import it.polimi.ingsw.BianchiCorneo.maps.CreateMap;
import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.Table;
import it.polimi.ingsw.BianchiCorneo.moves.Controller;
import it.polimi.ingsw.BianchiCorneo.moves.ControllerImpl;
import it.polimi.ingsw.BianchiCorneo.players.CharacterName;
import it.polimi.ingsw.BianchiCorneo.players.PlayerList;
import it.polimi.ingsw.BianchiCorneo.supervisor.MyTimer;
import it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandler;
import it.polimi.ingsw.BianchiCorneo.supervisor.RMIHandlerImpl;
import it.polimi.ingsw.BianchiCorneo.supervisor.SVConst;
import it.polimi.ingsw.BianchiCorneo.supervisor.GameSupervisor;
import it.polimi.ingsw.BianchiCorneo.supervisor.TimerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**This class contains the main of the server side. It opens to client connection and start to instantiate the game
 * @author Andrea Corneo
 *
 */
public class Server implements TimerInterface {
	private static Server instance;
	
	private ServerSocket serverSocket;
	private ServerSocket serverSocketNotifier;
	private final static int port = 3000;
	private final static int portNotifier = 3100;

	private PlayerList playerList;
	private Table table;
	private GameSupervisor sv;
	private ControllerImpl controller;
	private RMIHandlerImpl rmiHandler;
	private RMIServerImpl rmiServer;
	private int numPlayer;
	private boolean firstConn;
	private int gameID;

	private boolean timeExceeded;
	
	private boolean gameValid;
	
	private Server() {
		gameValid = false;
		numPlayer = 0;
		firstConn = false;
		gameID = 0;
	}
	
	/**Singleton method for this class
	 * @return
	 */
	public static Server factory() {
		if (instance == null)
			instance = new Server();
		return instance;
	}
	
	/** Initialize a new game and the supervisor.
	 * This will also create the map.
	 * 
	 */
	private void initGame() {
		playerList = new PlayerList();
		numPlayer = 0;
		firstConn = false;
		timeExceeded = false;
		gameValid = true;
		table = CreateMap.createMap(MAPConst.MAPNAME);
		sv = new GameSupervisor(table);
		
		try {
			controller.addGame(gameID, table, sv);
			ExecutorService svExec = Executors.newSingleThreadExecutor();
			ExecutorService socketConnExec = Executors.newSingleThreadExecutor();
			
			controller.addGame(gameID, table, sv);
			rmiHandler.addGame(gameID, sv);
			
			SocketConn socketConn = new SocketConn(this, serverSocket, serverSocketNotifier);
			socketConnExec.submit(socketConn);
			//Wait for new socket-players
			//Also waiting for new RMI-players
			
			System.out.println("Waiting the first player");
			while(!firstConn)
				try {
					synchronized(this) {this.wait();}
				} catch (InterruptedException e) {
					Logger.getGlobal().log(Level.ALL, "Error.", e);
				}
			
			System.out.println("First player added");
			
			System.out.println("Starting Timer");
			Timer timer = new Timer();
			timer.schedule(new MyTimer(this), SVConst.TIMEOUTSERVER);
			
			System.out.println("Waiting for other players");
			while (!timeExceeded) {
				Thread.sleep(1000);
				if(numPlayer==SVConst.MAXPLAYER) {
					System.out.println("Game full");
					synchronized(this) {socketConn.stop();}
					socketConnExec.shutdownNow();
					break;
				}
			}
			timer.cancel();
			//Check while exit condition
			
			//Timeout exceeded - check if there are sufficient player
			if (timeExceeded && numPlayer < 2) {
				System.out.println("Timeout server");
				synchronized(sv) {
					sv.suspend();
				}
				gameValid=false;
				return;
			} else if (timeExceeded) {
				System.out.println("Timeout, no other player connected");
				return;
			}
			rmiServer.setSupervisor(sv);
			if (gameValid) {
				synchronized(sv) {
					sv.setPlayerList(playerList);
					sv.initGame();
					sv.start();
					svExec.submit(sv);
				}
			}
			
			Thread.sleep(3000);
			
		}catch (InterruptedException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.supervisor.TimerInterface#setTimeExceeded()
	 */
	@Override
	public void setTimeExceeded() {
		timeExceeded = true;
	}
	
	/**Standard getter for <code>numPlayer</code>
	 * @return number of player
	 */
	public int numPlayer() {
		return numPlayer;
	}
	
	/**Increments the number of the player
	 * 
	 */
	public void incNumPlayer() {
		numPlayer++;
	}
	
	/**Invoked if first player (Socket/RMI connected)
	 * 
	 */
	public void firstConn() {
		firstConn = true;
	}
	
	/**Start the server, publishing the registry and initializing the ServerSocket
	 * 
	 */
	private void startServer() {
		System.out.println("Server starting");
		try {
			Registry registry = LocateRegistry.createRegistry(2020);
			controller = new ControllerImpl();
			Controller moveStub = (Controller) UnicastRemoteObject.exportObject(controller, 0);
			rmiHandler = new RMIHandlerImpl();
			RMIHandler rmiHandlerStub = (RMIHandler) UnicastRemoteObject.exportObject(rmiHandler, 0);
			rmiServer = new RMIServerImpl(this);
			RMIServer rmiServerStub = (RMIServer) UnicastRemoteObject.exportObject(rmiServer, 0);
			
			registry.bind("RMIServer", rmiServerStub);
			registry.bind("RMIHandler", rmiHandlerStub);
			registry.bind("Move", moveStub);
			
			System.out.println("Registry bound");
			
		} catch (RemoteException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		} catch (AlreadyBoundException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
		try {
			serverSocket = new ServerSocket(port);
			serverSocketNotifier = new ServerSocket(portNotifier);
		} catch (IOException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
		System.out.println("Server ready");
		while (true) {
			CharacterName.reset();
			initGame();
			gameID++;
		}
	}

	/**Main
	 * @param args
	 */
	public static void main (String[] args) {
		Server server = Server.factory();
		try {
			Logger.getGlobal().addHandler(new FileHandler("./ServerLog.log"));
			Logger.getGlobal().setLevel(Level.ALL);
		} catch (SecurityException | IOException e1) {
			System.out.println("Error in initializing log file");
		}
		server.startServer();
	}

	/**Standard getter for <code>gameID</code>
	 * @return gameID
	 */
	public int getGame() {
		return gameID;
	}

	/**Standard getter for <code>table</code>
	 * @return table
	 */
	public Table getTable() {
		return table;
	}

	/**Standard getter for <code>playerList</code>
	 * @return playerList
	 */
	public PlayerList getPlayerList() {
		return playerList;
	}

	/**Standard getter for <code>sv</code>
	 * @return supervisor
	 */
	public GameSupervisor getSupervisor() {
		return sv;
	}

	/**Standard getter for <code>controller</code>
	 * @return controller
	 */
	public Controller getController() {
		return controller;
	}
}
