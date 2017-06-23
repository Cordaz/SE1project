package it.polimi.ingsw.BianchiCorneo.client;

import it.polimi.ingsw.BianchiCorneo.players.Player;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client of the game
 * 
 * @author Mattia Bianchi
 *
 */
public class Client {
	private UserInterface ui;
	private NetworkInterface ni;
	private Player p;
	private String serverMsg = "";
	private Timer notifier;
	private boolean end;
	
	private Client() {}
	
	public static void main(String[] args) {
		Client client = new Client();
		try {
			Logger.getGlobal().addHandler(new FileHandler("./GameLog.log"));
			Logger.getGlobal().setLevel(Level.ALL);
		} catch (SecurityException | IOException e1) {
			System.out.println("Error in initializing log file");
		}
		try {
			client.start();
		} catch (RemoteException | NotBoundException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
	}
	
	/**
	 * Asks for connection, starts and handle the game
	 * 
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private void start() throws RemoteException, NotBoundException {
		System.out.println("Welcome to the online version of Escape From The Aliens In The Outer Space!");
		System.out.println();
		System.out.println();
		ni = factoryClient();
		
		ui.reqConn();
		p = ni.connect();
		
		//Start the client notifier
		notifier = new Timer(true); //as daemon
		
		//waiting for others players
		String name = ui.reqName();
		String start;
		do {
			start = ni.newConn(p);
		} while ("wait".equals(start));
		
		if ("no".equals(start)) {
			ui.lackOfPlayers();
			ui.exitGame();
			close();
			return;
		}
		
		ni.playerName(p.getIDPlayer(), p.getIDGame(), name);
		
		p = ni.updatePlayer(p);
		ui.viewMessage("Playing as: " + p.getCharName() + "\n");
		
		notifier.scheduleAtFixedRate(new Notifier(this, p.getIDPlayer(), p.getIDGame(), ni, ui), 0, 500);
		
		while (!end) {
			if ("inactivity".equals(serverMsg)) {
				ui.banned();
				break;
			}
			
			p = ni.updatePlayer(p);
			ui.waitTurn(p);
			synchronized(this) {
				ui.drawMap(p);
			}
			
			if ("where".equals(serverMsg))
				serverMsg = ni.doAction(p, "noise_" + ui.getSector(p));
			else if ("which".equals(serverMsg))
				serverMsg = ni.doAction(p, "discard_" + ui.selectToDiscard(p.getToDiscard().toString()));
			else if ("lighton".equals(serverMsg))
				serverMsg = ni.doAction(p, "lighton_" + ui.getSector(p));
			else {
				String act = ui.takeAction(p);
				if (end)
					break;
				serverMsg = ni.doAction(p, act);
			}
			
			String[] splitter;
			splitter = serverMsg.split("_");
			serverMsg = splitter[0];
			ui.viewMessage(splitter[1]);
			
			if ("both".equals(serverMsg)) {
				p = ni.updatePlayer(p);
				serverMsg = ni.doAction(p, "noise_" + ui.getSector(p));
				splitter = serverMsg.split("_");
				ui.viewMessage(splitter[1]);
				serverMsg = "which";
			}
		}
		notifier.cancel();
		
		ui.exitGame();
		
		close();
	}
	
	/**
	 * Notify the close of the client
	 */
	private void close() {
		ni.close();
	}

	/**
	 * Initialize the client with the connection and user interface chosen
	 * 
	 * @return the client
	 */
	public NetworkInterface factoryClient() {	
		switch (choose()) {
			case 1:
				try {
					ni = NetworkInterfaceFactory.getInterface("1");
					ui = new CLIVisual();
					return ni;
				} catch (Exception e) {
					Logger.getGlobal().log(Level.ALL, "Error.", e);
				}
			case 2:
				ni = NetworkInterfaceFactory.getInterface("2");
				ui = new CLIVisual();
				return ni;
			case 3:
				try {
					ni = NetworkInterfaceFactory.getInterface("1");
					ui = new GUI();
					return ni;
				} catch (Exception e) {
					Logger.getGlobal().log(Level.ALL, "Error.", e);
				}
			case 4:
				ni = NetworkInterfaceFactory.getInterface("2");
				ui = new GUI();
				return ni;
			default:
				return null;
		}
	}
	
	/**
	 * Method that asks for the kind of client that the player wants to use
	 * @return
	 */
	public int choose() {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		String choiceStr;
		int choice;
		printMenu();
		do {
			choiceStr = in.nextLine();
			try {
				choice = Integer.parseInt(choiceStr);
			} catch (NumberFormatException e) {
				choice = 0;
			}
			if (choice > 0 && choice < 5) {
				return choice;
			}
			else {
				System.out.println("Choice not valid, please insert one of the shown numbers\n");
				choose();
			}
		} while (choice > 0 && choice < 5);
		return choice;
	}
	
	/**
	 * List of choices
	 */
	public void printMenu() {
		System.out.println("Select the type of interface and the connection that you want to use:");
		System.out.println();
		System.out.println("1- Command Line Interface, connection via Remote Method Invocation");
		System.out.println("2- Command Line Interface, connection via Socket");
		System.out.println("3- Graphical User Interface, connection via Remote method Invocation");
		System.out.println("4- Graphical User Interface, connection via Socket");
	}

	/**Standard setter for <code>end</code>
	 * @param <b>true</b> if game is ended, <b>false</b> otherwise
	 */
	public void setEnd(boolean b) {
		end = b;
	}
}
