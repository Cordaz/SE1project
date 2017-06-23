package it.polimi.ingsw.BianchiCorneo.maps.cards;

/**
 * List of object cards
 * 
 * @author Mattia Bianchi
 *
 */
public class ObjDeck extends Deck {

	/**
	 * Basic constructor
	 */
	public ObjDeck() {}

	@Override
	public void initDeck() {
		for (int i = 0; i < CARDConst.ATTACKS; i++) {
			deckToShuff.add(new AttackCard());
		}
		for (int i = 0; i < CARDConst.ADRENALINES; i++) {
			deckToShuff.add(new AdrenalineCard());
		}
		for (int i = 0; i < CARDConst.LIGHTS; i++) {
			deckToShuff.add(new LightCard());
		}
		for (int i = 0; i < CARDConst.TELEPORTS; i++) {
			deckToShuff.add(new TeleportCard());
		}
		for (int i = 0; i < CARDConst.DEFENCES; i++) {
			deckToShuff.add(new DefenceCard());
		}
		for (int i = 0; i < CARDConst.SEDATIVES; i++) {
			deckToShuff.add(new SedativeCard());
		}
		super.shuffleDeck();
	}

}
