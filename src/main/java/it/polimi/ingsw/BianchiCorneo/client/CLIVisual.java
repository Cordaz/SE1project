package it.polimi.ingsw.BianchiCorneo.client;

import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;
import it.polimi.ingsw.BianchiCorneo.players.Player;

import java.util.Scanner;

/**
 * Command Line Interface for user interface, implements UserInterface
 * 
 * @author Mattia Bianchi
 *
 */
public class CLIVisual implements UserInterface {
	private Scanner in = new Scanner(System.in);
	
	@Override
	public void drawMap(Player p) {			 
		Sector curSector;
		for (int j = 0; j < MAPConst.DIMY; j++) {
			for (int i = 0; i < MAPConst.DIMX; i = i + 2) {
				curSector = p.getMap().getSector(i, j);
				drawSector(curSector, p);
			}
			System.out.println();
			System.out.printf("     ");
			for (int i = 1; i < MAPConst.DIMX; i = i + 2) {
				curSector = p.getMap().getSector(i, j);
				drawSector(curSector, p);
			}
			System.out.println();
		}
	}
	
	/**
	 * Draw the shell version of a single sector
	 * 
	 * @param curSector sector to draw
	 * @param sl list of reachable sectors for a player that can move
	 * @param s position of the player
	 * @param myTurn ability to move of the player
	 */
	private void drawSector(Sector curSector, Player p){
		if (curSector.sameAs(p.getCurSector())) {
			String str = CLIConst.ANSI_GREEN + "[" + curSector + "]     " + CLIConst.ANSI_RESET;
			System.out.printf(str);
		}
		else if (p.possibleMoves().contains(curSector)) {
			String str = CLIConst.ANSI_YELLOW + "[" + curSector + "]     " + CLIConst.ANSI_RESET;
			System.out.printf(str);
		}
		else
			curSector.drawSectorCLI();
	}
	
	@Override
	public void reqConn() {
		System.out.println("Connection established");
	}
	@Override
	public String reqName() {
		System.out.println("Insert your name");
		String name = in.nextLine();
		System.out.println("> Waiting for game to start...");
		return name;
	}
	@Override
	public void lackOfPlayers() {
		System.out.println("> Not enough players for game to start");
	}
	@Override
	public void exitGame() {
		System.out.println("> See you next time!");
	}
	@Override
	public void waitTurn(Player p) {
		//Not used in CLI
	}
	
	@Override
	public String getSector(Player p) {
		System.out.print("> Type the name of the sector chosen in the right format: ");
		return in.nextLine();
	}
	@Override
	public void viewMessage(String s) {
		System.out.println(s);
	}
	@Override
	public String selectToDiscard(String s) {
		System.out.println();
		System.out.print("> Write the number of the object you want to discard: ");
		return in.nextLine();
	}
	//TODO add showcards command

	@Override
	public String takeAction(Player p) {
		String choice;
		System.out.println();
		System.out.print("> ");
		choice = in.nextLine();
		if ("move".equals(choice))
			return choice + "_" + getSector(p);
		if ("usecard".equals(choice))
			return choice + "_" + whichCard(p);
		return choice + "_";
	}
	
	/**
	 * Prints cards owned and asks which one the player wants to use.
	 * 
	 * @param p player
	 * @return the number of the card used
	 */
	private String whichCard(Player p) {
		if (p.getObjects()[0] == null) {
			System.out.println("> You don't have any object to use");
			return "";
		} else {
			System.out.println("> Your objects are: ");
			for (int i = 0, cont = 0; i < Player.MAXCARD; i++) {
				cont = i + 1;
				if (p.getObjects()[i] != null)
					System.out.println("  " + cont + ": " + p.getObjects()[i].getMsg());
			}
			System.out.println();
			System.out.print("> Write the number of the object you want to use: ");
			return in.nextLine();
		}
	}

	@Override
	public void serverNotifies(Action notification) {
		System.out.println("> " + notification);
	}

	@Override
	public void banned() {
		System.out.println("> You have been suspended due to inactivity,\n  please restart the client to begin a new game.");
	}

	@Override
	public void startTurn() {
		System.out.println("> It's your turn!");
	}

	@Override
	public void unlock() {		
	}
}
