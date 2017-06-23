/**
 * 
 */
package it.polimi.ingsw.BianchiCorneo.supervisor;

import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.players.Player;

import java.io.PrintWriter;
import java.util.Scanner;

/**This class provides a runnable listener for the notification of socket client
 * @author Andrea Corneo
 *
 */
public class ClientSocketNotificator implements Runnable {
	private ClientHandler clientHandler;
	private GameSupervisor sv;
	private Player player;
	private String arg;
	private String[] args;
	
	private Scanner is;
	private PrintWriter os;
	
	private Action notification;
	private boolean myTurn;
	private boolean end;
	
	/**Constructor
	 * @param clientHandler
	 */
	public ClientSocketNotificator(ClientHandler clientHandler, GameSupervisor sv, Player p) {
		this.clientHandler = clientHandler;
		this.sv = sv;
		player = p;
		end = false;
		notification = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		is = clientHandler.getNotifierIn();
		os = clientHandler.getNotifierOut();
		
		while(!end) {
			arg = readLine(is);
			args = arg.split(SVConst.SPLIT);
			
			switch(args[0]) {
			case "end":
				printLine(os, "end_" + printBool(end));
				if (end)
					printLine(os, "endMessage_" + clientHandler.getEnd());
				break;
			case "myTurn":
				myTurn = sv.isYourTurn(player);
				printLine(os, "myTurn_" + printBool(myTurn));
				break;
			case "notify":
				printLine(os, "notify_" + notification);
				break;
			}
		}
	}
	
	/**Standard setter for <code>notification</code>
	 * @param notification of Action happened
	 */
	public void setNotification(Action notification) {
		this.notification = notification;
	}
	
	/**Standard setter for <code>end</code>
	 * @param <b>true</b> if the game is ended, <b>false</b> otherwise
	 */
	public void setEnd(boolean b) {
		end = b;
	}
	
	/**Convert a boolean value into a string
	 * @param b boolean to convert
	 * @return <code>"true"</code> if b is <b>true</b>, else <code>"false"</code>
	 */
	private String printBool(boolean b) {
		return b ? "true" : "false";
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
	private void printLine(PrintWriter os, String line) {
		os.println(line);
		os.flush();
	}
}
