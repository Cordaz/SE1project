package it.polimi.ingsw.BianchiCorneo.maps.cards;

/**
 * List of sector cards
 * 
 * @author Mattia Bianchi
 *
 */
public class SectorDeck extends Deck {

	/**
	 * Basic constructor
	 */
	public SectorDeck() {}

	@Override
	public void initDeck() {
		for (int i = 0; i < CARDConst.SECTORSWOBJ; i++) {
			deckToShuff.add(new NoiseObj(true));
			deckToShuff.add(new NoiseObj(false));
		}
		for (int i = 0; i < CARDConst.SECTORSNOOBJ; i++) {
			deckToShuff.add(new Noise(true));
			deckToShuff.add(new Noise(false));
		}
		for (int i = 0; i < CARDConst.SILENCES; i++) {
			deckToShuff.add(new Silence());
		}
		super.shuffleDeck();
	}

}
