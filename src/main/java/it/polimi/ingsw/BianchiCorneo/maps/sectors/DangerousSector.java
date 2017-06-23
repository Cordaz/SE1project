package it.polimi.ingsw.BianchiCorneo.maps.sectors;

import it.polimi.ingsw.BianchiCorneo.client.CLIConst;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Sector built as dangerous, a player that reaches this sector has to draw a sector card
 * 
 * @author Mattia Bianchi
 *
 */
public class DangerousSector extends Sector {
	private static final long serialVersionUID = 257499393378734974L;

	/**
	 * Base constructor
	 * 
	 * @param x coordinate x
	 * @param y coordinate y
	 */
	public DangerousSector(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void drawSectorCLI() {
		super.drawSectorCLI(CLIConst.ANSI_GRAY);
	}
	
	@Override
	public void drawHex(int i, int j, Graphics2D g2) {
		super.drawHex(i, j, g2, Color.GRAY);
	}
}
