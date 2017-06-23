package it.polimi.ingsw.BianchiCorneo.client;

import it.polimi.ingsw.BianchiCorneo.actions.Action;

import java.rmi.RemoteException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a thread to catch and show notification
 * 
 * @author Andrea Corneo
 *
 */
public class Notifier extends TimerTask {
	private Client client; //for synch lock
	private NetworkInterface ni;
	private UserInterface ui;
	private int gameID;
	private int playerID;
	private boolean myTurn;
	private boolean playing;
	private boolean go;
	
	private Action notification;
	private Action lastNotification;

	/**Constructor
	 * @param client 
	 * @param gameID
	 * @param ni NetworkInterface of the client
	 * @param ui UserInterface of the client
	 */
	public Notifier(Client client, int playerID, int gameID, NetworkInterface ni, UserInterface ui) {
		this.client = client;
		this.playerID = playerID;
		this.gameID = gameID;
		this.ni = ni;
		this.ui = ui;
		playing = false;
		go = true;
		notification = null;
		lastNotification = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while(go) {
				if (ni.isNotifiedEnd(gameID)) {
					go = false;
					synchronized(client) {
						client.setEnd(true);
					}
					ui.viewMessage(ni.getEnd(gameID));
					ui.unlock();
				}
				
				myTurn = ni.isMyTurn(playerID, gameID);
				if (!playing) {
					if (myTurn) {
						synchronized(client) {
							ui.startTurn();
						}
						playing = true;
					}
				} else {
					if (!myTurn)
						playing = false;
				}
				
				notification = ni.getNotification(gameID);
				if (notification != null)
					if(!checkAlreadyRead()) {
						synchronized(client) {
							ui.serverNotifies(notification);
							lastNotification = notification;
						}
					}
				
			}
		} catch (RemoteException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
	}
	
	/**Check if the notification ha already been read.
	 * @return <b>false</b> if it has to be read, <b>true</b> else
	 */
	private boolean checkAlreadyRead() {
		return notification.equals(lastNotification);
	}
}
