package it.polimi.ingsw.BianchiCorneo.maps.cards;

/**
 * Card that notifies that the current deck is empty
 * 
 * @author Andrea Corneo
 *
 */
public class NoCardAvailable extends Card {
	private static final long serialVersionUID = 6340150546403313697L;

	/**
	 * Basic constructor
	 */
	public NoCardAvailable() {
		super();
	}
	
	@Override
	public String toString() {
		return "Deck si empty, no more card available";
	}

}
