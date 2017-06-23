package it.polimi.ingsw.BianchiCorneo.maps.cards;

/**
 * Constants for cards and decks
 * 
 * @author Mattia Bianchi
 *
 */
public class CARDConst {
	public static final int SECTORSNOOBJ = 0;//6
	public static final int SECTORSWOBJ = 15;//4
	public static final int SILENCES = 0;//5
	public static final int SECTORCARDS = SECTORSNOOBJ + SECTORSWOBJ + SILENCES;
	
	public static final int ATTACKS = 2;
	public static final int SEDATIVES = 3;
	public static final int TELEPORTS = 2;
	public static final int DEFENCES = 1;
	public static final int LIGHTS = 2;
	public static final int ADRENALINES = 2;
	public static final int OBJECTCARDS = ATTACKS + SEDATIVES + TELEPORTS + DEFENCES + LIGHTS + ADRENALINES;
	
	/**
	 * Hided constructor
	 */
	private CARDConst() {
	}
}
