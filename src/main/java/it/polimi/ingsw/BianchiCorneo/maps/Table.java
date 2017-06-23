package it.polimi.ingsw.BianchiCorneo.maps;

import it.polimi.ingsw.BianchiCorneo.maps.sectors.AlienBase;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.HumanBase;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.SectorList;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.ShipSector;
import it.polimi.ingsw.BianchiCorneo.players.Player;
import it.polimi.ingsw.BianchiCorneo.players.PlayerList;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class initiate the map used for the game, it's called by CreateMap
 * 
 * @author Mattia Bianchi
 *
 */
public class Table implements Serializable {
	
	private static final long serialVersionUID = 6707609883869172561L;
	private Sector[][] tableMap;
	private transient Map<Player, Sector> positions;
	private HumanBase humanBase;
	private AlienBase alienBase;
	private SectorList ships;
	
	/**
	 * Basic constructor for table class.
	 * 
	 * @param s list of sectors
	 * @param hb human base sector
	 * @param ab alien base sector
	 * @param ships list
	 */
	public Table(Sector[][] s, HumanBase hb, AlienBase ab, SectorList ships) {
		positions = new HashMap<Player, Sector>();
		this.tableMap = s;
		this.humanBase = hb;
		this.alienBase = ab;
		this.ships = ships;
	}
	
	/**
	 * 
	 * @return Human base sector
	 */
	public HumanBase getHumanBase() {
		return humanBase;
	}
	
	/**
	 * 
	 * @return Alien baseb sector
	 */
	public AlienBase getAlienBase() {
		return alienBase;
	}
	
	/**
	 * 
	 * @param x coordinate x
	 * @param y coordinate y
	 * @return the sector related to the given coordinates
	 */
	public Sector getSector(int x, int y) {
		return tableMap[y][x];
	}
	
	/**
	 * 
	 * @param s name of the sector
	 * @return the sector related to the given coordinates
	 */
	public Sector getSector(String s) {
		Sector sec = new Sector(s);
		return tableMap[sec.getCoordY()][sec.getCoordX()];
	}
	
	/**
	 * 
	 * @return the sector grid of the map
	 */
	public Sector[][] getSectors() {
		return tableMap;
	}
	
	/**
	 * Insert a playerand its position in the list of players
	 * 
	 * @param p player
	 * @param s current sector of the player
	 */
	public void put(Player p, Sector s) {
		positions.put(p, s);
	}
	
	/**
	 * Called by move, refresh the position of the player playing the turn
	 * 
	 * @param p player
	 * @param s destination sector
	 */
	public void updatePos(Player p, Sector s) {
		positions.remove(p);
		positions.put(p, s);
	}
	
	/**
	 * 
	 * @param p player
	 * @return the position of the player on map
	 */
	public Sector getPos(Player p) {
		return positions.get(p);
	}
	
	/**
	 * 
	 * @param s sector to attack
	 * @return the list of players on the given sector
	 */
	public PlayerList attackSector(Sector s) {
		return this.getPresencesOnSector(s);
	}
	
	/**
	 * Light a sector and its adjacent sectors 
	 * 
	 * @param s sector to light
	 * @return a map of each player and its sector
	 */
	public Map<Player, Sector> lightOn(Sector s) {
		Map<Player, Sector> playersLighted = new HashMap<Player, Sector>();
		SectorList sL = new SectorList();
		if (s.getCoordX() % 2 == 0) 
			s.findAdjacents(this, s, sL, 1, MAPConst.EVENSECTOR);
		else 
			s.findAdjacents(this, s, sL, 1, MAPConst.ODDSECTOR);
		sL.add(s);
		for (int i = 0; i < sL.size(); i++) {
			PlayerList pL = getPresencesOnSector(sL.get(i));
			for (int k = 0; k < pL.size(); k++)
				playersLighted.put(pL.get(k), sL.get(i));
		}
		return playersLighted;
	}
	
	/**
	 * Reveals players on a given sector
	 * 
	 * @param s sector to light
	 * @return the list of player on that sector
	 */
	public PlayerList getPresencesOnSector(Sector s) { 
		PlayerList presences = new PlayerList();
		for(Player p : positions.keySet()) {
			if (positions.get(p).sameAs(s)) {
				presences.add(p);
			}
		}
		return presences;
	}
	
	/**
	 * Instantiate sector ship to usable or not
	 */
	public void initShip() {
		//Creating boolean values
		int dim = ships.size();
		boolean[] bool = new boolean[(dim-1)*2];
		bool[0] = true;
		for (int i=1; i<(dim-1)*2; i++)
			if (bool[i-1])
				bool[i] = false;
			else
				bool[i] = true;
		
		//Shuffle
		int a, b;
		boolean tmp;
		for (int j=0; j<50; j++) { //statistic
			a = (int) (Math.random()*(dim-1)*2);
			b = (int) (Math.random()*(dim-1)*2);
			tmp = bool[a];
			bool[a] = bool[b];
			bool[b] = tmp;
		}
		
		//Check if in dim+1 (number of ships) there is at least 1 true
		boolean ok = false;
		for (int i=0; i<dim && !ok; i++) {
			((ShipSector)ships.get(i)).initialize(bool[i]);
			ok = bool[i];
		}
		//No true, re-do
		if (!ok)
			initShip();
	}
}
