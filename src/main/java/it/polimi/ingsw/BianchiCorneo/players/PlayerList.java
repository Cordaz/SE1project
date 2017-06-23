package it.polimi.ingsw.BianchiCorneo.players;

import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;

import java.util.ArrayList;
import java.util.List;

/**This class is a list of players, with methods that are extension of Java List methods and method useful for game handling
 * @author Andrea Corneo
 *
 */
public class PlayerList {
	private List<Player> pL;
	private String winner = "";
	private String killed = "";
	
	/**Constructor
	 * 
	 */
	public PlayerList() {
		pL = new ArrayList<Player>();
	}
	
	/**Add the Player <b>p</b> to the list
	 * @param p
	 */
	public void add(Player p) {
		pL.add(p);
	}
	
	/**List get(int index)
	 * @param index of player needed (<b>playerID</b>)
	 * @return Player in index position
	 */
	public Player get(int index) {
		return pL.get(index);
	}
	
	/**List size()
	 * @return size of the player list
	 */
	public int size() {
		return pL.size();
	}
	
	/**List remove(int index)
	 * @param index playerID to remove
	 */
	public void remove(int index) {
		pL.remove(index);
	}
	
	/**Remove the indicated player
	 * @param p player to remove
	 */
	public void remove(Player p) {
		for (int i=0; i<pL.size(); i++)
			if(pL.get(i).equals(p)) {
				remove(i);
				break;
			}
	}
	
	/**List contains(Player p)
	 * @param p player to check
	 * @return <b>true</b> if p is contained, else <b>false</b>
	 */
	public boolean contains(Player p) {
		return pL.contains(p);
	}
	
	/**List isEmpty()
	 * @return <b>true</b> if no player contained, else <b>false</b>
	 */
	public boolean isEmpty() {
		return pL.isEmpty();
	}
	
	
	/**Check if there are at least one Human and one Alien in the player list
	 * @return <b>true</b> if the game is still playable
	 */
	public boolean validGame() {
		boolean alien = false;
		boolean human = false;
		//At least one human
		for (Player p : pL)
			if (p instanceof Human) {
				human = true;
				break;
			}
		//At least one alien
		for (Player p1 : pL)
			if (p1 instanceof Alien) {
				alien = true;
				break;
			}
		return alien && human;
	}

	/**Formats a String with the player that had successfully escaped
	 * @return formatted player string if someone has escaped, <b>""</b> otherwise
	 */
	public String giveWinner() {
		return winner;
	}

	/**Formats a String with the player that had been killed
	 * @return formatted player string if someone had been killed, <b>""</b> otherwise
	 */
	public String giveKilled() {
		return killed;
	}
	
	/**Copy the contents of the player list from param to this
	 * @param player list to copy
	 */
	public void copy(PlayerList pl) {
		for (Player p : pl.pL)
			this.add(p);
	}
	
	public void addEndInfo(Player p) {
		if (p.getCurSector().sameAs(new Sector(MAPConst.WINNINGSECTOR)))
			winner = winner + "  " + p.toFormattedString() + "\n";
		else if (!p.isAlive())
			killed = killed + "  " + p.toFormattedString() + "\n";
	}
}
