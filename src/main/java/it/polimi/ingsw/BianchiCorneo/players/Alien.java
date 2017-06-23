package it.polimi.ingsw.BianchiCorneo.players;

import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.Table;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.SectorList;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.ShipSector;

/**Represents an alien player
 * @author Andrea Corneo
 *
 */
public class Alien extends Player {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean powerful;
	
	@SuppressWarnings("unused")
	private Alien() {
		super();
		powerful = false;
	}
	
	/**Constructor
	 * @param idPlayer
	 * @param idGame
	 * @param t table which the player is playing on
	 */
	public Alien(int idPlayer, int idGame, Table t) {
		super(idPlayer, idGame, t);
		powerful = false;
	}
	
	/**This methods make the player powerful, giving him the possibilities of moving up to 3 cells
	 * 
	 */
	public void becomePowerful() {
		powerful = true;
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.players.Player#resetUtilities()
	 */
	@Override
	public void resetUtilities() {
		super.resetAttack();
		super.resetNoise();
		moved = false;
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.players.Player#placeOnMap()
	 */
	@Override
	public void placeOnMap() {
		t.put(this, t.getAlienBase());
		super.tablePath.add(t.getAlienBase());
		super.initFormattedPath();
	}
	
	/* (non-Javadoc)
	 * @see it.polimi.ingsw.BianchiCorneo.players.Player#possibleMoves()
	 */
	@Override
	public SectorList possibleMoves() {
		Sector s = tablePath.get(tablePath.size() - 1);
		SectorList setOfMoves = new SectorList();
		int distance = 2;
		if (powerful)
			distance = 3;
		if (s.getCoordX() % 2 == 0) {
			s.findAdjacents(t, s, setOfMoves, distance, MAPConst.EVENSECTOR);
		} else {
			s.findAdjacents(t, s, setOfMoves, distance, MAPConst.ODDSECTOR);
		}
		setOfMoves.remove(t.getAlienBase());
		setOfMoves.remove(t.getHumanBase());
		for (int i = 0; i < setOfMoves.size(); i++)
			if (setOfMoves.get(i) instanceof ShipSector)
				setOfMoves.remove(setOfMoves.get(i));
		return setOfMoves;
	}
}
