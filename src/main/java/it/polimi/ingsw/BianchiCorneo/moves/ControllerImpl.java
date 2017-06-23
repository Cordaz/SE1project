package it.polimi.ingsw.BianchiCorneo.moves;

import it.polimi.ingsw.BianchiCorneo.maps.Table;
import it.polimi.ingsw.BianchiCorneo.maps.cards.DefenceCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.LightCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.Noise;
import it.polimi.ingsw.BianchiCorneo.maps.cards.ObjectCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.SectorCard;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.DangerousSector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.ShipSector;
import it.polimi.ingsw.BianchiCorneo.players.Alien;
import it.polimi.ingsw.BianchiCorneo.players.Human;
import it.polimi.ingsw.BianchiCorneo.players.Player;
import it.polimi.ingsw.BianchiCorneo.supervisor.GameSupervisor;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * Set of actions that can be done during a player's turn
 * 
 * @author Mattia Bianchi
 *
 */
public class ControllerImpl implements Controller {
	
	private Map<Integer, Table> tableList;
	private Map<Integer, GameSupervisor> svList;
	private GameActions gameActions;
	
	/**
	 * Basic constructor for MoveImpl
	 */
	public ControllerImpl() {
		tableList = new HashMap<Integer, Table>();
		svList = new HashMap<Integer, GameSupervisor>();
		gameActions = new GameActions(tableList, svList);
	}
	
	/**
	 * Create the references for a new supervisor and a new map
	 * 
	 * @param idGame id of the current game
	 * @param t map used in the current game
	 * @param sv supervisor related to the current game
	 */
	public void addGame(int idGame, Table t, GameSupervisor sv) {
		tableList.put(idGame, t);
		svList.put(idGame, sv);
	}
	
	/**
	 * Match an idPlayer with the related player
	 * 
	 * @param idPlayer id of the player
	 * @param idGame id of the game
	 * @return the player selected
	 */
	private Player matchPlayer(int idPlayer, int idGame) {
		return svList.get(idGame).getPlayerList().get(idPlayer);
	}
	
	@Override
	public String doAction(int idPlayer, int idGame, String s) throws RemoteException {		
		return doAction(matchPlayer(idPlayer, idGame), s);
	} 
	
	/**
	 * Method that receives a command from the client, checks it and returns the result of the action.
	 * 
	 * @param p player
	 * @param s command sent
	 * @return the result of the action
	 */
	public String doAction(Player p, String s) {
		String[] args;
		if (p.isSuspended())
			return "inactivity_You have been suspended due to inactivity,\n  restart the client to begin a new game";
		if (!svList.get(p.getIDGame()).isYourTurn(p))
			return "warning_Is not your turn to play";
		args = s.split("_");
		switch (args[0]) {
			case "move":
				if (args.length != 2)
					return "warning_Invalid attribute, please insert a valid attribute.";
				return checkMove(p, args[1]);
			case "usecard":
				if (args.length != 2)
					return "warning_Invalid attribute, please insert a valid attribute.";
				return checkUseCard(p, args[1]);
			case "attack":
				return checkAttack(p);
			case "lighton":
				if (args.length != 2)
					return "lighton_Invalid attribute, please insert a valid attribute.";
				return checkLightOn(p, args[1]);
			case "endturn":
				return checkEndTurn(p);
			case "discard":
				if (args.length != 2)
					return "which_Invalid attribute, please insert a valid attribute.";
				return checkDiscard(p, args[1]);
			case "noise":
				if (args.length != 2)
					return "where_Invalid attribute, please insert a valid attribute.";
				return checkNoise(p, args[1]);
			default:
				return "warning_Unknown command";
		}	
	}
	
	/**
	 * Check if the player is able to move and, if it is, moves it.
	 * 
	 * @param p player
	 * @param s sector to move
	 * @return the result of the action
	 */
	private String checkMove(Player p, String s) {
		if (p.getMap().getSector(s) == null)
			return "warning_The sector chosen does not exist, please select a valid one.";
		Sector sec = p.getMap().getSector(s);
		if (p.hasMoved())
			return "warning_You already moved this turn";
		else if (!sec.isValidSector() || !p.possibleMoves().contains(sec))
			return "warning_Sector not valid for moving, please select a valid sector";
		else {
			gameActions.movePlayer(p, sec);
			if (p.getCurSector() instanceof ShipSector) {
				if (((ShipSector)p.getCurSector()).isUsable()) {
					gameActions.useShip(p);
					return "win_You moved on the selected sector\n  and you successfully escaped from the map";
				}
				return "info_You moved on the selected sector\n  but the hatch is not usable";
			}
			return "info_You moved on the selected sector";
		}
	}
	
	/**
	 * Check if the player is able to use a card and, if it is, uses the card.
	 * 
	 * @param p player
	 * @param s number of the card to use
	 * @return the result of the action
	 */
	private String checkUseCard(Player p, String s) {
		int attr;
		try {
			attr = Integer.parseInt(s) - 1;
		} catch (NumberFormatException e) {
			return "warning_Invalid attribute, please insert a valid attribute.";
		}
		if (p instanceof Alien)
			return "warning_You are an alien, you can't use object cards";
		if (attr > Player.MAXCARD || p.getObjects()[attr] == null)
			return "warning_Invalid choice";
		if (p.getObjects()[attr] instanceof LightCard) {
			return "lighton_Select the sector you want to light";
		}
		if (p.getObjects()[attr] instanceof DefenceCard) {
			return "warning_You can't use your defense card,\n  it will active automatically when you will be attacked";
		}
		String infoDone = gameActions.useCard(p, p.getObjects()[attr]);
		if (!"Card Used".equals(infoDone)) {
			return "warning_" + infoDone;
		}
		return "info_" + infoDone;
	}
	
	/**
	 * Check if the player is able to attack and, if it is, attack its current sector.
	 * 
	 * @param p player
	 * @return the result of the action
	 */
	private String checkAttack(Player p) {
		if (p instanceof Human)
			return "warning_You are an human, you can attack only using the proper card";
		if (p.hasAttacked())
			return "warning_You already attacked this turn";
		if (!p.hasMoved())
			return "warning_You have to move before attack";
		gameActions.attackSector(p);
		return "info_You attacked your current sector";
	}
	
	/**
	 * Check if the player is able to use the light card and, if it is, uses the card.
	 * 
	 * @param p player
	 * @param s sector to light
	 * @return the result of the action
	 */
	private String checkLightOn(Player p, String s) {
		if (p.getMap().getSector(s) == null)
			return "lighton_The sector chosen does not exist, please select a valid one.";
		Sector secLight = p.getMap().getSector(s);
		if (!secLight.isValidSector())
			return "lighton_Sector not valid, please select a valid sector";
		ObjectCard obj = new LightCard();
		return "info_" + gameActions.lightOn(p, secLight, obj);
	}
	
	/**
	 * Check if the player is able to end its turn and, if it is, ends the turn.
	 * 
	 * @param p player
	 * @return the result of the action
	 */
	private String checkEndTurn(Player p) {
		SectorCard sc;
		if (!p.hasMoved()) 
			return "warning_You have to make your move first";
		if (!p.hasAttacked() && p.getCurSector() instanceof DangerousSector) {
			sc = gameActions.drawCard(p);
			if (sc instanceof Noise) {
				if (!((Noise)sc).isReal() && p.getToDiscard() != null)
					return "both_" + sc.toString();
				if (!((Noise)sc).isReal())
					return "where_" + sc.toString();
				if (p.getToDiscard() != null)
					return "which_" + sc.toString();
				p.resetUtilities();
				p.setPlaying(false);
				synchronized(svList.get(p.getIDGame())) {svList.get(p.getIDGame()).notifyAll();}
				return "info_" + sc.toString() + "\n\n  Your turn is ended\n";
			} else {
				p.resetUtilities();
				p.setPlaying(false);
				synchronized(svList.get(p.getIDGame())) {svList.get(p.getIDGame()).notifyAll();}
				return "info_" + sc.toString() + "\n\n  Your turn is ended\n";
			}
		}
		p.resetUtilities();
		p.setPlaying(false);
		synchronized(svList.get(p.getIDGame())) {svList.get(p.getIDGame()).notifyAll();}
		return "info_Your turn is ended\n";
	}
	
	/**
	 * Discards the card chosen by the user.
	 * 
	 * @param p player
	 * @param s card to discard
	 * @return the result of the action
	 */
	private String checkDiscard(Player p, String s) {
		int attr;
		try {
			attr = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return "which_Invalid attribute, please insert a valid attribute.";
		}
		if (attr > 4 || attr < 1)
			return "which_Invalid choice";
		if (attr != 4)
			p.switchObj(p.getObjects()[attr - 1]);
		gameActions.discardCard(p);
		p.resetUtilities();
		p.setPlaying(false);
		synchronized(svList.get(p.getIDGame())) {svList.get(p.getIDGame()).notifyAll();}
		return "info_You discarded the selected card\n\n  Your turn is ended\n";
	}
	
	/**
	 * Makes a fake noise in the sector chosen by the user.
	 * 
	 * @param p player
	 * @param s sector chosen
	 * @return the result of the action
	 */
	private String checkNoise(Player p, String s) {
		if (p.getMap().getSector(s) == null)
			return "where_The sector chosen does not exist, please select a valid one.";
		Sector secNoise = p.getMap().getSector(s);
		if (!secNoise.isValidSector())
			return "where_Sector not valid, please select a valid sector";
		if (p.getToDiscard() != null)
			return "info_" + gameActions.fakeNoise(p, secNoise);
		String msg = gameActions.fakeNoise(p, secNoise);
		p.resetUtilities();
		p.setPlaying(false);
		synchronized(svList.get(p.getIDGame())) {svList.get(p.getIDGame()).notifyAll();}
		return "info_" + msg + "\n\n  Your turn is ended\n";
	}
}
