package it.polimi.ingsw.BianchiCorneo.maps.sectors;

import it.polimi.ingsw.BianchiCorneo.client.CLIConst;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Sector built as base for alien players
 * 
 * @author Mattia Bianchi
 *
 */
public class AlienBase extends Sector {
	private static final long serialVersionUID = 1622742760557151743L;

	/**
	 * Base constructor
	 * 
	 * @param x coordinate x
	 * @param y coordinate y
	 */
	public AlienBase(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void drawSectorCLI() {
		super.drawSectorCLI(CLIConst.ANSI_PURPLE);
	}
	
	@Override
	public void drawHex(int i, int j, Graphics2D g2) {
		super.drawHex(i, j, g2, Color.MAGENTA);
	}
}
