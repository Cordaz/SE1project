package it.polimi.ingsw.BianchiCorneo.players;

import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.Table;
import it.polimi.ingsw.BianchiCorneo.maps.cards.ObjectCard;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.SectorList;

import java.io.Serializable;

/**This class represents the player of the game
 * @author Andrea Corneo
 *
 */
public class Player implements Serializable {
	private static final long serialVersionUID = -8252067105369385587L;
	private String name;
	private String charName;
	private int idPlayer;
	private int idGame;
	
	private boolean playing;
	private boolean suspended;
	private boolean madeNoise;
	private Sector noiseOn;
	protected Table t;
	protected SectorList tablePath; //path on table
	protected String[] formattedPath;
	protected boolean attack;
	protected boolean moved;
	private boolean alive;
	
	public static final int MAXCARD = 3;
	protected ObjectCard[] objectList;
	protected ObjectCard toDiscard;
	private String drawnCard;
	
	/**Constructor
	 * 
	 */
	protected Player() {
		playing = false;
		suspended = false;
		madeNoise = false;
		alive = true;
		objectList = new ObjectCard[MAXCARD];
		tablePath = new SectorList();
		formattedPath = new String[39];
		toDiscard = null;
	}
	
	/**Constructor
	 * @param idPlayer
	 * @param idGame
	 * @param t table which the player is playing on
	 */
	public Player(int idPlayer, int idGame, Table t) {
		this();
		this.idPlayer = idPlayer;
		this.idGame = idGame;
		this.t = t;
	}
	
	/**Standard getter for <code>idPlayer</code>
	 * @return player number in the game (and also his position in the corresponding PlayerList)
	 */
	public int getIDPlayer() {
		return idPlayer;
	}
	
	/**Standard getter for <code>idGame</code>
	 * @return game number and the key in the map of the supervisor/table
	 */
	public int getIDGame() {
		return idGame;
	}
	
	/**Standard getter for charName
	 * @return the character name, obtained from the constants in CharacterName
	 */
	public String getCharName() {
		return charName;
	}
	
	/**Standard setter for charName
	 * @param charName to be taken from <b>CharacterName.nextPlayerName()</b>
	 */
	public void setCharName(String charName) {
		this.charName = charName;
	}
	
	/**Return a formatted string that identifies the player
	 * @return player name and it's character name
	 */
	public String toFormattedString() {
		return name + " (" + charName + ")";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/**Standard setter for name
	 * @param s the name of the player
	 */
	public void setName(String s) {
		name = s;
	}
	
	/**Standard getter for <code>playing</code> 
	 * @return <b>true</b> if the player is currently playing, else <b>false</b>
	 */
	public boolean isPlaying() {
		return playing;
	}
	
	/**Standard setter for playing
	 * @param b current status of the player
	 */
	public void setPlaying (boolean b) {
		playing = b;
	}
	
	/**Standard getter for suspended
	 * @return <b>true</b> if the player is suspended, else <b>false</b>
	 */
	public boolean isSuspended() {
		return suspended;
	}
	
	/**Suspend a player, it sets <code>suspended=true</code> and <code>playing=false</code>
	 * 
	 */
	public void suspend() {
		suspended = true;
		playing = false;
	}
	
	/**Return the path of the player
	 * @return SectorList of the old position
	 */
	public SectorList playerPath() {
		return tablePath;
	}
	
	/**Initialize the formatted path to a default value
	 * 
	 */
	public void initFormattedPath() {
		for (int i = 0; i < 39; i++) {
			int cont = i + 1;
			String contStr = ((Integer)cont).toString();
			if (cont < 10)
				contStr = "0" + cont;
			formattedPath[i] = contStr + " = [...];   ";
		}
	}
	
	/**Standard getter for <code>formattedPath</code>
	 * @return the old position of the player
	 */
	public String[] getFormattedPath() {
		return formattedPath;
	}
	
	/**Add the Sector <b>s</b> to the player path
	 * @param s
	 */
	public void addToPath(Sector s) {
		tablePath.add(s);
		int cont = tablePath.size();
		String contStr = ((Integer)(cont - 1)).toString();
		if (cont < 10)
			contStr = "0" + contStr;
		formattedPath[cont - 2] = contStr + " = [" + s + "];   ";
	}
	
	/**Make noise in the <b>s</b> Sector, set <code>madeNoise=true</code>
	 * @param s
	 */
	public void makeNoise(Sector s) {
		madeNoise = true;
		noiseOn = s;
	}
	/**Standard getter for <code>madeNoise</made>
	 * @return <b>true</b> if player had made noise on some sector in the previous turn, else <b>false</b>
	 */
	public boolean hasMadeNoise() {
		return madeNoise;
	}
	/**Standard getter for <code>noiseOn</code>
	 * @return the Sector on which was made noise in player's previous turn
	 */
	public Sector hasMadeNoiseOn() {
		return noiseOn;
	}
	/**Reset the noise, to be used at the start of the turn
	 * 
	 */
	public void resetNoise() {
		madeNoise = false;
		noiseOn = null;
	}
	
	/**Standard getter for <code>alive</code>
	 * @return <b>true</b> if player is alive, <b>false<b> if has been killed
	 */
	public boolean isAlive() {
		return alive;
	}
	
	/**Kill the player, set <code>alive=false</code>, suspend the player e move him in a fake sector (A99)
	 * 
	 */
	public void kill() {
		alive = false;
		suspended = true;
		tablePath.add(new Sector(MAPConst.KILLEDSECTOR));
	}
	
	/**Set <code>attack=true</code> used to handle the turn in the client
	 * 
	 */
	public void attack() {
		attack = true;
	}
	/**Reset <code>attack=false</code>
	 * 
	 */
	public void resetAttack() {
		attack = false;
	}
	/**Standard getter for attack
	 * @return <b>true</b> if the player can attack, else <b>false</b>
	 */
	public boolean hasAttacked() {
		return attack;
	}
	
	/**Set <code>moved=true</code>
	 * 
	 */
	public void setMove() {
		moved = true;
	}
	/**Standard getter for <code>moved</code>
	 * @return <b>true</b> if the player has moved in the turn, else <b>false</b>
	 */
	public boolean hasMoved() {
		return moved;
	}
	
	/**Reset the turn variables to default values, like <code>moved</code>, <code>attack</code>
	 * 
	 */
	public void resetUtilities() {}
	
	/**Standard setter for <code>drawnCard</code>
	 * @param s
	 */
	public void setDrawnCard(String s) {
		drawnCard = s;
	}
	
	/**Standard getter for <code>drawnCard</code>
	 * @return the String corresponding to the drawn card
	 */
	public String getDrawnCard() {
		return drawnCard;
	}
	
	/**Reset <code>drawnCard</code> to a default value
	 * 
	 */
	public void resetDrawnCard() {
		drawnCard = "";
	}
	
	/**Insert an ObjectCard into player's list.
	 * Eventually set the new card as a candidate to be discarded (default choice)
	 * @param obj to insert
	 * @return <b>true</b> if it is possible (less than 3 objects), else <b>false</b>
	 */
	public boolean giveObj(ObjectCard obj) { //Return if object accepted
		for (int i=0; i<MAXCARD; i++)
			if (objectList[i] == null) {
				objectList[i] = obj;
				return true;
			}
		toDiscard = obj;
		return false;
	}
	
	/**Remove an object card from player's list.
	 * @ensures (*the objectcard are all consecutive*)
	 * @param obj to remove
	 */
	public void rmObj(ObjectCard obj) {
		ObjectCard tmp;
		int i, j;
		for (i = 0; i < MAXCARD; i++) {
			if (objectList[i].equals(obj)) {
				for (j = i + 1; j < MAXCARD && objectList[j] != null; j++) {
					tmp = objectList[j];
					objectList[j] = objectList[i];
					objectList[i] = tmp;
				}
				objectList[j-1] = null;
				break;
			}
		}
	}
	/**Switch the given object with the <code>toDiscard</code>
	 * @param obj to switch (to be insert in player's list)
	 */
	public void switchObj(ObjectCard obj) { 
		for (int i = 0; i < MAXCARD; i++)
			if (objectList[i].equals(obj)) {
				objectList[i] = toDiscard;
				toDiscard = obj;
			}
	}
	
	/**Return the card to be discarded
	 * @return
	 */
	public ObjectCard getToDiscard() {
		return toDiscard;
	}
	/**Reset the <code>toDiscard</code> card to a default value <b>null</b>
	 * 
	 */
	public void discard() {
		toDiscard = null;
	}
	
	/**Standard setter to <code>toDiscard</code>
	 * @param obj to be discarded
	 */
	public void setToDiscard(ObjectCard obj) {
		toDiscard = obj;
	}
	
	
	/**Generate the set of the Sector that can be reached by the player
	 * @return SectorList of possible new position
	 */
	public SectorList possibleMoves() {return null;}
	
	
	/**Place player on the base (Human or Alien)
	 * 
	 */
	public void placeOnMap() {}
	
	/**Return the table of the game
	 * @return table
	 */
	public Table getMap() {
		return t;
	}
	
	/**Getter for player's current position
	 * @return current Sector
	 */
	public Sector getCurSector() {
		return tablePath.get(tablePath.size() - 1);
	}
	
	/**Getter for player's object list
	 * @return array of ObjectCard
	 */
	public ObjectCard[] getObjects() {
		return objectList;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg) {
		if (!(arg instanceof Player))
			return false;
		return (idPlayer == ((Player)arg).getIDPlayer()) && (idGame == ((Player)arg).getIDGame());
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
	
}
