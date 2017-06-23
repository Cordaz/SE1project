package it.polimi.ingsw.BianchiCorneo.maps.sectors;

import java.awt.Graphics2D;


/**
 * Sector built as null, no one can reach this sector
 * 
 * @author Mattia Bianchi
 *
 */
public class NullSector extends Sector {
	private static final long serialVersionUID = -8269174506952413300L;

	/**
	 * Base constructor
	 * 
	 * @param x coordinate x
	 * @param y coordinate y
	 */
	public NullSector(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void drawSectorCLI() {
		System.out.printf("          ");
	}
	
	@Override
	public void drawHex(int i, int j, Graphics2D g2) {
	}
}
