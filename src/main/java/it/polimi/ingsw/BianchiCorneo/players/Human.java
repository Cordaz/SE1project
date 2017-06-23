package it.polimi.ingsw.BianchiCorneo.players;

import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.Table;
import it.polimi.ingsw.BianchiCorneo.maps.cards.DefenceCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.ObjectCard;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.SectorList;

/**Represents an human player
 * @author Andrea Corneo
 *
 */
public class Human extends Player {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3521611574412106860L;
	private boolean adrenaline;
	private boolean sedative;
	private boolean shield;
	private boolean lastHuman = false;
	
	@SuppressWarnings("unused")
	private Human() {
		super();
		adrenaline = false;
	}
	
	/**Constructor
	 * @param idPlayer
	 * @param idGame
	 * @param t table which the player is playing on
	 */
	public Human(int idPlayer, int idGame, Table t) {
		super(idPlayer, idGame, t);
		adrenaline = false;
	}
	
	/**Set adrenaline to <b>true</b>, so the human can move up to 2 cells
	 * 
	 */
	public void useAdrenaline() {
		adrenaline = true;
	}
	/**Standard getter for <code>adrenaline</code>
	 * @return <b>true</b> if the human can move up to 2 cells, <b>false</b>
	 */
	public boolean adrenaline() {
		return adrenaline;
	}
	/**Activate the sedative on the player, giving him the possibilities of moving without drawing any card
	 * 
	 */
	public void useSedative() {
		sedative = true;
		super.attack = true;
	}
	/**Standard getter for <code>sedative</code>
	 * @return <b>true</b> if is sedated, else <b>false</b>
	 */
	public boolean sedate() {
		return sedative;
	}
	
	/**Remove the defence card
	 * @return card removed
	 */
	public ObjectCard rmDefence() {
		ObjectCard toReturn;
		for (int i=0; i<objectList.length; i++)
			if (objectList[i] instanceof DefenceCard) {
				toReturn = objectList[i];
				rmObj(toReturn);
				return toReturn;
			}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.players.Player#resetUtilities()
	 */
	@Override
	public void resetUtilities() {
		super.resetAttack();
		super.resetNoise();
		sedative = false;
		adrenaline = false;
		moved = false;
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.players.Player#placeOnMap()
	 */
	@Override
	public void placeOnMap() {
		t.put(this, t.getHumanBase());
		super.tablePath.add(t.getHumanBase());
		super.initFormattedPath();
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.players.Player#possibleMoves()
	 */
	@Override
	public SectorList possibleMoves() {
		Sector s = tablePath.get(tablePath.size() - 1);
		SectorList setOfMoves = new SectorList();
		int distance = 1;
		if (adrenaline)
			distance = 2;
		if (s.getCoordX() % 2 == 0) {
			s.findAdjacents(t, s, setOfMoves, distance, MAPConst.EVENSECTOR);
		} else {
			s.findAdjacents(t, s, setOfMoves, distance, MAPConst.ODDSECTOR);
		}
		setOfMoves.remove(t.getAlienBase());
		setOfMoves.remove(t.getHumanBase());
		return setOfMoves;
	}
	
	/**Used to activate shield on human, it is used to activate the defence automatically when the player is attacked
	 * 
	 */
	public void setShield() {
		shield = true; 
	}
	/**Deactivate the shield
	 * 
	 */
	public void resetShield() {
		shield = false;
	}
	/**Standard getter for <code>shield</code>
	 * @return <b>true</b> if shield is on, else <b>false</b>
	 */
	public boolean hasShield() {
		return shield;
	}
	
	@Override
	public void switchObj(ObjectCard obj) { 
		for (int i = 0; i < MAXCARD; i++)
			if (objectList[i].equals(obj)) {
				if (toDiscard instanceof DefenceCard)
					setShield();
				objectList[i] = toDiscard;
				toDiscard = obj;
			}
	}
	
	@Override
	public void discard() {
		if (toDiscard instanceof DefenceCard)
			resetShield();
		toDiscard = null;
	}
	
	public void setLast() {
		lastHuman = true;
	}
	public boolean isLast() {
		return lastHuman;
	}
}
