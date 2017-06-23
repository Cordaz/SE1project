package it.polimi.ingsw.BianchiCorneo.maps.sectors;

import it.polimi.ingsw.BianchiCorneo.client.CLIConst;

import java.awt.Color;
import java.awt.Graphics2D;


/**
 * Sector built as a ship, a player that reaches this sector can escape
 * 
 * @author Mattia Bianchi
 *
 */
public class ShipSector extends Sector {
	private static final long serialVersionUID = -2483108106297375936L;
	private boolean usable; //Already used
	private boolean disabled; //Value initialized
	
	/**
	 * Base constructor
	 * 
	 * @param x coordinate x
	 * @param y coordinate y
	 */
	public ShipSector(int x, int y) {
		super(x, y);
		this.usable = true;
	}
	
	/** 
	 * Check if ship is usable: the ship can be disabled by the map creation or can already be used
	 * 
	 * @return true if usable
	 */
	public boolean isUsable() {
		return disabled && usable;
	}
	/**
	 * Disable the ship
	 */
	public void disable() {
		usable = false;
	}
	
	@Override
	public void drawSectorCLI() {
		if (usable)
			super.drawSectorCLI(CLIConst.ANSI_BLUE);
		else
			super.drawSectorCLI(CLIConst.ANSI_RED);
	}
	
	@Override
	public void drawHex(int i, int j, Graphics2D g2) {
		if (usable)
			super.drawHex(i, j, g2, Color.BLUE);
		else
			super.drawHex(i, j, g2, Color.RED);
		
	}
	
	/** 
	 * Initialize the ship to disabled or not
	 * 
	 * @param bool boolean passed by the initShip() method
	 */
	public void initialize(boolean bool) {
		disabled = bool;
	}
}
