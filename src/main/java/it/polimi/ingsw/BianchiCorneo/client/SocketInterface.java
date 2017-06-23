package it.polimi.ingsw.BianchiCorneo.client;

import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.players.Player;
import it.polimi.ingsw.BianchiCorneo.supervisor.Message;
import it.polimi.ingsw.BianchiCorneo.supervisor.SVConst;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketInterface implements NetworkInterface {
	private Socket socket;
	private Socket socketNotifier;
	private ObjectInputStream ois;
	private Scanner isNotifier;
	private PrintWriter os;
	private PrintWriter osNotifier;
	private final static String ip = "127.0.0.1";
	private final static int port = 3000;
	private final static int portNotifier = 3100;
	private String[] args;
	private String arg;
	private Player p;

	@Override
	public Player connect() {
		try {
			socket = new Socket(ip, port);
			socketNotifier = new Socket(ip, portNotifier);
			
			os = new PrintWriter(socket.getOutputStream());
			os.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			
			osNotifier = new PrintWriter(socketNotifier.getOutputStream());
			osNotifier.flush();
			isNotifier = new Scanner(socketNotifier.getInputStream());
			
		} catch (IOException e) {
			socket = null;
			ois = null;
			os = null;
			socketNotifier = null;
			isNotifier = null;
			osNotifier = null;
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
		Player p = null;
		try {
			p = (Player) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
		return p;
	}

	@Override
	public String newConn(Player p) {
		return readLine(ois, "connectionStatus");
	}

	@Override
	public String doAction(Player p, String s) {
		sendServer(os, "doAction_" + s);
		return readLine(ois);
	}

	@Override
	public synchronized Player updatePlayer(Player p) {
		sendServer(os, "update");
		this.p = p;
		try {
			return (Player) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
			return null;
		}
	}

	@Override
	public boolean isNotifiedEnd(int gameID) {
		sendServer(osNotifier, "end");
		arg = readLine(isNotifier, "end");
		if ("true".equals(arg))
			return true;
		return false;
	}
	
	@Override
	public Action getNotification(int gameID) {
		sendServer(osNotifier, "notify");
		arg = readLine(isNotifier, "notify");
		if (!"null".equals(arg)) {
			Action a = new Action(p, arg);
			return a;
		}
		return null;
	}

	@Override
	public String getEnd(int gameID) {
		return readLine(isNotifier, "endMessage");
	}

	@Override
	public boolean isMyTurn(int playerID, int gameID) {
		sendServer(osNotifier, "myTurn");
		arg = readLine(isNotifier, "myTurn");
		if ("true".equals(arg))
			return true;
		return false;
	}
	
	/**Read the next line
	 * @parm ois ObjectInputStream on which read
	 * @return the message
	 */
	private String readLine(ObjectInputStream ois) {
		try {
			Object input = ois.readObject();
			arg = ((Message)input).get();
		} catch (ClassNotFoundException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		} catch (IOException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
		return arg;
	}
	private String readLine(Scanner is) {
		return is.nextLine();
	}
	
	/**Read and split the next line
	 * @param ois ObjectInputStream on which read
	 * @param param on which to split the string
	 * @return the message if the <code>param</code> is correct, <code>""</code> otherwise
	 */
	private String readLine(ObjectInputStream ois, String param) {
		args = readLine(ois).split(SVConst.SPLIT);
		if (args[0].equals(param))
			return args[1];
		return "";
	}
	private String readLine(Scanner is, String param) {
		args = readLine(is).split(SVConst.SPLIT);
		if (args[0].equals(param))
			return args[1];
		return "";
	}
	
	/**Send the <code>param</code> string to the server
	 * @param oos ObjectOutputStream on which send
	 * @param param
	 */
	private void sendServer(PrintWriter os, String param) {
		os.println(param);
		os.flush();
	}

	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.client.NetworkInterface#playerName(int, int, java.lang.String)
	 */
	@Override
	public void playerName(int playerID, int gameID, String name) {
		sendServer(os, SVConst.NAME + name);
	}

	@Override
	public void close() {
		try {
			ois.close();
			os.close();
			isNotifier.close();
			osNotifier.close();
			socket.close();
			socketNotifier.close();
		} catch (IOException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
	}
}
