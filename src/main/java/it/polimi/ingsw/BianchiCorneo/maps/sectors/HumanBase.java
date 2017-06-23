package it.polimi.ingsw.BianchiCorneo.maps.sectors;

import it.polimi.ingsw.BianchiCorneo.client.CLIConst;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Sector built as base for human players
 * 
 * @author Mattia Bianchi
 *
 */
public class HumanBase extends Sector {
	private static final long serialVersionUID = -995862869938828721L;

	/**
	 * Base constructor
	 * 
	 * @param x coordinate x
	 * @param y coordinate y
	 */
	public HumanBase(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void drawSectorCLI() {
		super.drawSectorCLI(CLIConst.ANSI_CYAN);
	}
	
	@Override
	public void drawHex(int i, int j, Graphics2D g2) {
		super.drawHex(i, j, g2, Color.CYAN);
	}
}
