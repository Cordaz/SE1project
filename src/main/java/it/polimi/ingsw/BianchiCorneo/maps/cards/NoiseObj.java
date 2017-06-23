package it.polimi.ingsw.BianchiCorneo.maps.cards;

public class NoiseObj extends Noise {
	private static final long serialVersionUID = 4119203774985417180L;
	
	/**
	 * Basic constructor
	 * 
	 * @param b set the kind of noise that will be done from the player
	 */
	public NoiseObj(boolean b) {
		super(b);
	}
	
	@Override
	public void whatHappened(String s) {
		super.whatHappened(s);
		super.appendix = "\n  and you have found a new object" + s;
	}
}
