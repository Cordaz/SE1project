package it.polimi.ingsw.BianchiCorneo.moves;

import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.actions.ObjectUsed;
import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.Table;
import it.polimi.ingsw.BianchiCorneo.maps.cards.Card;
import it.polimi.ingsw.BianchiCorneo.maps.cards.DefenceCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.ObjectCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.SectorCard;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.ShipSector;
import it.polimi.ingsw.BianchiCorneo.players.Human;
import it.polimi.ingsw.BianchiCorneo.players.Player;
import it.polimi.ingsw.BianchiCorneo.supervisor.GameSupervisor;

import java.util.Map;

/**
 * Class of methods that make actions on the game
 * 
 * @author Mattia Bianchi
 *
 */
public class GameActions {
	private Map<Integer, Table> tableList;
	private Map<Integer, GameSupervisor> svList;
	
	/**
	 * Basic constructor
	 * 
	 * @param tableList
	 * @param svList
	 */
	public GameActions(Map<Integer, Table> tableList, Map<Integer, GameSupervisor> svList) {
		this.tableList = tableList;
		this.svList = svList;
	}
	
	/**
	 * Move a player on the selected sector
	 * 
	 * @param p player to move
	 * @param s sector chosen for the move
	 */
	public void movePlayer(Player p, Sector s) {
			tableList.get(p.getIDGame()).updatePos(p, s);
			p.addToPath(s);
			p.setMove();
	}
	
	/**
	 * Attack the sector where a player is located
	 * 
	 * @param p player that is attacking
	 */
	public void attackSector(Player p) {
		Action a = new Action(p, "attacked on sector " + p.getCurSector());
		p.attack();
		svList.get(p.getIDGame()).notifySupervisor(a, tableList.get(p.getIDGame()).attackSector(p.getCurSector()));
	}
	
	/**
	 * Activates the effect of a sector card used by a human player
	 * 
	 * @param p player that used the card
	 * @param obj object card used
	 * @return a string which indicates the result of the action done 
	 */
	public String useCard(Player p, ObjectCard obj) {
		ObjectUsed ou = new ObjectUsed(p, obj);
		Object act = obj.action(p);
		if (!(act instanceof String)) {
			p.rmObj(obj);
			svList.get(p.getIDGame()).reinsertCard(obj);
			svList.get(p.getIDGame()).notifySupervisor(ou, act);
		} else
			return (String)act;
		return obj.actionDone();
	}
	
	/**
	 * Specifically activates the effect of the light card, which reveals the players located on a sector and on the adjacent ones.
	 * The use of a proper method for this object is due to the need of the card to receive the sector to light, action that has to be 
	 * asked to the user
	 * 
	 * @param p player that uses the card
	 * @param s sector to light
	 * @param obj object card used
	 * @return a string that indicates the result of the action
	 */
	public String lightOn(Player p, Sector s, ObjectCard obj) {
		ObjectUsed ou = new ObjectUsed(p, obj); 
		p.rmObj(obj);
		svList.get(p.getIDGame()).reinsertCard(obj);
		svList.get(p.getIDGame()).notifySupervisor(ou, tableList.get(p.getIDGame()).lightOn(s));
		return obj.actionDone();
	}
	
	/**
	 * Draws a sector card, and eventually add an object card to the list of cards held by the player
	 * 
	 * @param p player that draws the card
	 * @return the sector card drawn
	 */
	public SectorCard drawCard(Player p) {
		Card[] cards = svList.get(p.getIDGame()).drawCard();
		
		String actionMsg = "";
		if (((SectorCard)cards[0]).action(p))
			actionMsg = "has made noise on " + p.getCurSector();
		
		if (cards[1] != null) {
			((SectorCard)cards[0]).whatHappened(": " + cards[1].toString());
			if (!p.giveObj((ObjectCard)cards[1])) {
				p.setToDiscard((ObjectCard) cards[1]);
			} else {
				if (cards[1] instanceof DefenceCard && p instanceof Human)
					((Human)p).setShield();
			}
			if (!"".equals(actionMsg))
				actionMsg = "drew a new object\n> " + p + ": " + actionMsg;
			else
				actionMsg = actionMsg + "drew a new object";
		} else {
			((SectorCard)cards[0]).whatHappened("");
		}
		
		if (!"".equals(actionMsg))
			svList.get(p.getIDGame()).notifySupervisor(new Action(p, actionMsg), null);
		
		svList.get(p.getIDGame()).reinsertCard(cards[0]);
		return (SectorCard)cards[0];
	}
	
	/**
	 * Switch the current card that will be discarded with one of the cards held by the player
	 * 
	 * @param p player that wants to switch his cards
	 * @param obj object card to switch
	 */
	public void switchObj(Player p, ObjectCard obj) {
		p.switchObj(obj);
	}
	
	/**
	 * Discards the fourth card held by the player
	 * 
	 * @param p player
	 */
	public void discardCard(Player p) {
		svList.get(p.getIDGame()).reinsertCard(p.getToDiscard());
		p.discard();
	}
	
	/**
	 * Make a noise in a selected sector and not in the sector where the player is located
	 * 
	 * @param p player that makes the noise
	 * @param s sector where the player makes the noise
	 * @return a string that notifies the result of the action
	 */
	public String fakeNoise(Player p, Sector s) {
		Action a = new Action(p, "has made noise on " + s);
		p.makeNoise(s);
		svList.get(p.getIDGame()).notifySupervisor(a, null);
		return "You have just made noise in the sector " + s;
	}
	
	/**
	 * Use the ship reached by a player
	 * 
	 * @param p player that reaches a ship
	 */
	public void useShip(Player p) {
		((ShipSector)p.getCurSector()).disable();
		svList.get(p.getIDGame()).getActivePlayerList().remove(p);
		svList.get(p.getIDGame()).removeFromPlayerTurn();
		svList.get(p.getIDGame()).notifySupervisor(new Action(p, "has escaped"), null);
		movePlayer(p, new Sector(MAPConst.WINNINGSECTOR));
		svList.get(p.getIDGame()).getPlayerList().addEndInfo(p);
	}
}
