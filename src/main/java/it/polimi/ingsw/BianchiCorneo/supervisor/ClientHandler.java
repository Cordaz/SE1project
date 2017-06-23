package it.polimi.ingsw.BianchiCorneo.supervisor;

import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.moves.Controller;
import it.polimi.ingsw.BianchiCorneo.moves.ControllerImpl;
import it.polimi.ingsw.BianchiCorneo.players.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


/**This class provides all the methods necessary to handle a socket client.
 * @author Andrea Corneo
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;
	private Socket socketNotifier;
	private GameSupervisor supervisor;
	private Controller controller;
	private Player p;
	private Scanner is;
	private Scanner isNotifier;
	private ObjectOutputStream oos;
	private PrintWriter osNotifier;
	private String arg;
	private String[] args;
	private String endMessage;
	
	private ClientMonitor clientMonitor;
	
	private boolean active;
	
	private ExecutorService notifierListener;
	private ClientSocketNotificator clientSocketNotificator;
	
	private String connectionStatus;
	
	/**Constructor
	 * @param sv supervisor of the game
	 * @param s socket opened for the client
	 * @param p player assigned
	 * @param controller of the game
	 */
	public ClientHandler (GameSupervisor sv, Socket s, Socket notifier, Player p, Controller controller) {
		supervisor = sv;
		socket = s;
		socketNotifier = notifier;
		this.p = p;
		this.controller = controller;
		try {
			is = new Scanner(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			
			isNotifier = new Scanner(socketNotifier.getInputStream());
			osNotifier = new PrintWriter(socketNotifier.getOutputStream());
			osNotifier.flush();
		} catch (IOException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
		clientMonitor = sv.getMonitor();
		active = true;
		notifierListener = Executors.newSingleThreadExecutor();
		clientSocketNotificator = new ClientSocketNotificator(this, sv, p);
		connectionStatus = "wait";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		clientMonitor.subscribe(this);
		try {
			sendPlayerDetails();
			
			while (supervisor.isWaiting())
				connectionStatus();
			
			connectionStatus();
			getName();
			System.out.println(p);
			
			notifierListener.submit(clientSocketNotificator);
			
			while (active) {
				arg = readLine(is);
				args = arg.split(SVConst.SPLIT);
				
				if (args[0].equals("doAction")) {
					String toDo = "";
					for (int i=1; i<args.length; i++) {
						toDo = toDo + args[i];
						if (i<args.length-1)
							toDo = toDo + "_";
					}
					String done = ((ControllerImpl)controller).doAction(p, toDo);
					printLine(oos, done);
				} else if (args[0].equals("update")) {
					oos.writeObject(p);
					oos.flush();
				}
				
				if (!supervisor.isGameValid()) {
					active = false;
					clientSocketNotificator.setEnd(true);
				}
			}
		} catch (IOException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
		notifierListener.shutdownNow();
		
		//Game is correctly terminated
		connClose();
	}

	/**Send the client a string reporting the connection status
	 * 
	 */
	private void connectionStatus() {
		if ("ok".equals(connectionStatus) || "no".equals(connectionStatus))
			return;
		connectionStatus = connStatus();
		printLine(oos, "connectionStatus" + SVConst.SPLIT + connectionStatus);
	}

	/**create the connecton status String
	 * @return <code>"wait"</code> if the supervisor is waiting, <code>"ok"</code> if the game can start, <code>"no"</code> otherwise
	 */
	private String connStatus() {
		if (supervisor.isWaiting())
			return "wait";
		if (supervisor.isGameValid())
			return "ok";
		return "no";
	}

	/**Set player's name
	 * @param s name
	 */
	private void playerName(String s) {
		p.setName(s);
	}
	
	/**Read player's name from socket input
	 * 
	 */
	public void getName() {
		args = readLine(is).split(SVConst.SPLIT);
		if ("name".equals(args[0]))
			playerName(args[1]);
	}
	
	/**Send the player object to the client
	 * @throws IOException
	 */
	public void sendPlayerDetails() throws IOException {
		oos.writeObject(p);
		oos.flush();
		oos.reset();
	}
	
	/**Close the socket and the stream
	 * 
	 */
	private void connClose() {
		try {
			socket.close();
		} catch (IOException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
		try {
			is.close();
			oos.close();
			isNotifier.close();
			osNotifier.close();
		} catch (IOException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
		return;
	}
	
	/**Set the action and update prepare the handler to notify the client
	 * @param a action happened
	 */
	public void notifyAction(Action a) {
		clientSocketNotificator.setNotification(a);
	}
	
	/**Set the end message generated by the supervisor
	 * @param message
	 */
	public void notifyEnd(String message) {
		endMessage = message;
	}
	
	/**Standard getter for object input stream used for notifier
	 * @return oisNotifier
	 */
	public Scanner getNotifierIn() {
		return isNotifier;
	}

	/**Standard getter for object output stream used for notifier
	 * @return oosNotifier
	 */
	public PrintWriter getNotifierOut() {
		return osNotifier;
	}
	
	/**Read next socket line
	 * @return line read
	 */
	private String readLine(Scanner is) {
		return is.nextLine();
	}
	
	/**Print a String to the socket output stream
	 * @param line to print
	 */
	private void printLine(ObjectOutputStream oos, String line) {
		try {
			oos.writeObject(new Message(line));
			oos.flush();
		} catch (IOException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
	}

	/**Standard getter for <code>endMessage</code>
	 * @return the String of end
	 */
	public String getEnd() {
		return endMessage;
	}
}
