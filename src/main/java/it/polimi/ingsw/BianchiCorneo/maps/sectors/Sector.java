package it.polimi.ingsw.BianchiCorneo.maps.sectors;

import it.polimi.ingsw.BianchiCorneo.client.CLIConst;
import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.Table;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.io.Serializable;
/**
 * Component of the map, defined by a couple of coordinates, which can be expressed in the format "A00" or numerically
 * 
 * @author Mattia Bianchi
 *
 */
public class Sector implements Serializable {
	private static final long serialVersionUID = 232021549809414584L;
	private int coordX;
	private int coordY;
	private String name; //Sector name
	private boolean noise;
	
	/**
	 * Constructor called by numeric coordinates
	 * 
	 * @param x coordinate x
	 * @param y coordinate y
	 */
	public Sector(int x, int y) {
		this.coordX = x;
		this.coordY = y;
		this.noise = false;
		convert();
	}
	
	/**
	 * Constructor called by the name of the sector, such as "A00"
	 * 
	 * @param s name of the sector
	 */
	public Sector(String s) {
		name = s;
		noise = false;
		inverseConvert();
	}
	
	/**
	 * Getter method for coordinate x
	 * 
	 * @return coordinate x
	 */
	public int getCoordX() {
		return coordX;
	}
	/**
	 * Getter method for coordinate y
	 * 
	 * @return coordinate y
	 */
	public int getCoordY() {
		return coordY;
	}
	
	/**
	 * Shows if someone has made noise on the sector 
	 * 
	 * @return the state of noise of the sector
	 */
	public boolean isNoisy() {
	return noise;
	}
	
	/**
	 * Set the sector as "noisy"
	 */
	public void makeNoise() {
		this.noise = true;
	}
	
	/**
	 * Set the sector as "quiet"
	 */
	public void stopNoise() {
		this.noise = false;
	}
	
	/**
	 * Shows if the sector is reachable
	 * 
	 * @return 
	 */
	public boolean isValidSector() {
		if (this instanceof NullSector)
			return false;
		return true;
	}
	
	/**
	 * Converts the format of the coordinates from string to numeric
	 */
	private void inverseConvert() {
		char[] charCoord = MAPConst.CHAR.toCharArray();
		coordY = (Character.getNumericValue(name.charAt(1)) * 10 + Character.getNumericValue(name.charAt(2))) - 1;
		for (int i = 0; i < charCoord.length; i++ ) {
			if (charCoord[i] == name.charAt(0)) {
				coordX = i;
			}
		} 
	}
	
	/**
	 * Converts the format from numeric to string
	 */
	private void convert() { 
		char[] charCoord = MAPConst.CHAR.toCharArray();
		String y = Integer.toString(coordY + 1);
		if  (coordY + 1 < 10) 
			y = "0" + y;
		name = Character.toString(charCoord[coordX]) + y;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Overload of equals function 
	 * 
	 * @param s sector to match
	 * @return true if the coordinates are the same
	 */
	public boolean sameAs(Sector s) {
		if (this.coordX == s.getCoordX() && this.coordY == s.getCoordY())
			return true;
		return false;
	}
	
	/**
	 * Recursive function that finds all the adjacent sectors
	 * 
	 * @param t map
	 * @param sInit starting sector
	 * @param setOfSectors set of sectors where new adjacent sectors are stored
	 * @param d distance of recursion
	 * @param toIgnore indicate if the current sector is in an odd or in an even column
	 */
	public void findAdjacents(Table t, Sector sInit, SectorList setOfSectors, int d, int toIgnore) {
		Sector sectorToCheck = null;
		if (d == 0) {
			return;
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((i != toIgnore || (i == toIgnore && j == 0)) && 
						(!(coordY + i < 0 || coordY + i >= MAPConst.DIMY) && !(coordX + j < 0 || coordX + j >= MAPConst.DIMX))) {
					sectorToCheck = t.getSector(coordX + j, coordY + i);
					if (sectorToCheck.isValidSector()) {
						if (!sectorToCheck.sameAs(sInit) && !setOfSectors.contains(sectorToCheck))
							setOfSectors.add(sectorToCheck);
						if (sectorToCheck.getCoordX() % 2 == 0)
							sectorToCheck.findAdjacents(t, sInit, setOfSectors, d - 1, MAPConst.EVENSECTOR);
						else
							sectorToCheck.findAdjacents(t, sInit, setOfSectors, d - 1, MAPConst.ODDSECTOR);
					}
				}
			}
		}
	}
	
	/**
	 * Draws the single sector on the shell
	 */
	public void drawSectorCLI() {
		String str = "[" + name + "]     ";
		System.out.printf(str);
	}
	/**
	 * Draws the colored version of the sector (shell)
	 * 
	 * @param color
	 */
	public void drawSectorCLI(String color) {
		String str = color + "[" + name + "]     " + CLIConst.ANSI_RESET;
		System.out.printf(str);
	}
	
	//GUI Constants
	public final static boolean orFLAT= true;
	public final static boolean orPOINT= false;
 
	private static boolean XYVertex=true;	//true: x,y are the co-ords of the first vertex.
	//false: x, y are the co-ords of the top left rect. co-ord.
 
	private static int BORDERS=50;	//default number of pixels for the border.
 
	private static int s=0;	// length of one side
	private static int t=0;	// short side of 30o triangle outside of each hex
	private static int r=0;	// radius of inscribed circle (centre to middle of each side). r= h/2
	private static int h=0;	// height. Distance between centres of two adjacent hexes. Distance between two opposite sides in a hex.
 
	public void setXYasVertex(boolean b) {
		XYVertex=b;
	}
	public void setBorders(int b){
		BORDERS=b;
	}
 
	/** This functions takes the Side length in pixels and uses that as the basic dimension of the hex.
     *       It calculates all other needed constants from this dimension.
     */
	public void setSide(int side) {
		s=side;
		t =  (int) (s / 2);			//t = s sin(30) = (int) CalculateH(s);
		r =  (int) (s * 0.8660254037844);	//r = s cos(30) = (int) CalculateR(s); 
		h=2*r;
	}
	public void setHeight(int height) {
		h = height;			// h = basic dimension: height (distance between two adj centresr aka size)
		r = h/2;			// r = radius of inscribed circle
		s = (int) (h / 1.73205);	// s = (h/2)/cos(30)= (h/2) / (sqrt(3)/2) = h / sqrt(3)
		t = (int) (r / 1.73205);	// t = (h/2) tan30 = (h/2) 1/sqrt(3) = h / (2 sqrt(3)) = r / sqrt(3)
	}
 
	/*********************************************************
	Name: hex()
	Parameters: (x0,y0) This point is normally the top left corner 
	    of the rectangle enclosing the hexagon. 
	    However, if XYVertex is true then (x0,y0) is the vertex of the 
	    top left corner of the hexagon. 
	Returns: a polygon containing the six points.
	Called from: drawHex(), fillhex()
	Purpose: This function takes two points that describe a hexagon
	and calculates all six of the points in the hexagon.
	*********************************************************/
	public Polygon hex (int x0, int y0) {
 
		int y = y0 + BORDERS;
		int x = x0 + BORDERS; // + (XYVertex ? t : 0); //Fix added for XYVertex = true. 
				      // NO! Done below in cx= section
		if (s == 0  || h == 0) {
			System.out.println("ERROR: size of hex has not been set");
			return new Polygon();
		}
 
		int[] cx, cy;
 
		//I think that this XYvertex stuff is taken care of in the int x line above. Why is it here twice?
		if (XYVertex) 
			cx = new int[] {x, x + s, x + s + t, x + s, x, x - t};  //this is for the top left vertex being at x,y. Which means that some of the hex is cutoff.
		else
			cx = new int[] {x + t, x + s + t, x + s + t + t, x + s + t, x + t, x};	//this is for the whole hexagon to be below and to the right of this point
 
		cy = new int[] {y, y, y + r, y + r + r, y + r + r, y + r};
		return new Polygon(cx, cy, 6);
	}
 
	/********************************************************************
	Name: drawHex()
	Parameters: (i,j) : the x,y coordinates of the inital point of the hexagon
		    g2: the Graphics2D object to draw on.
	Returns: void
	Calls: hex() 
	Purpose: This function draws a hexagon based on the initial point (x,y).
	The hexagon is drawn in the colour specified in hexgame.COLOURELL.
	*********************************************************************/
	public void drawHex(int i, int j, Graphics2D g2) {
		int x = i * (s + t);
		int y = j * h + (i % 2) * h / 2;
		Polygon poly = hex(x, y);
		g2.setColor(Color.WHITE);
		//g2.fillPolygon(hexmech.hex(x,y));
		g2.fillPolygon(poly);
		g2.setColor(Color.BLACK);
		g2.drawPolygon(poly);
		
		String c;
		x = i * (s + t) - 9;
		y = j * h + (i % 2) * h / 2;
		g2.setColor(Color.BLACK);
		c = this.toString();
		g2.drawString("" + c, x + r + BORDERS, y + r + BORDERS + 4); //FIXME: handle XYVertex
		//g2.drawString(x+","+y, x+r+BORDERS, y+r+BORDERS+4);
	}
	public void drawHex(int i, int j, Graphics2D g2, Color color) {
		int x = i * (s + t);
		int y = j * h + (i % 2) * h / 2;
		Polygon poly = hex(x, y);
		g2.setColor(color);
		//g2.fillPolygon(hexmech.hex(x,y));
		g2.fillPolygon(poly);
		g2.setColor(Color.BLACK);
		g2.drawPolygon(poly);
		
		String c;
		x = i * (s + t) - 10;
		y = j * h + (i % 2) * h / 2;
		g2.setColor(Color.BLACK);
		c = this.toString();
		g2.drawString("" + c, x + r + BORDERS, y + r + BORDERS + 4); //FIXME: handle XYVertex
		//g2.drawString(x+","+y, x+r+BORDERS, y+r+BORDERS+4);
	}
 
	//This function changes pixel location from a mouse click to a hex grid location
	/*****************************************************************************
	* Name: pxtoHex (pixel to hex)
	* Parameters: mx, my. These are the co-ordinates of mouse click.
	* Returns: point. A point containing the coordinates of the hex that is clicked in.
	           If the point clicked is not a valid hex (ie. on the borders of the board, (-1,-1) is returned.
	* Function: This only works for hexes in the FLAT orientation. The POINTY orientation would require
	            a whole other function (different math).
	            It takes into account the size of borders.
	            It also works with XYVertex being True or False.
	*****************************************************************************/
	public static Point pxtoHex(int mx, int my) {
		Point p = new Point(-1, -1);
 
		//correction for BORDERS and XYVertex
		mx -= BORDERS;
		my -= BORDERS;
		if (XYVertex) mx += t;
 
		int x = (int) (mx / (s + t)); //this gives a quick value for x. It works only on odd cols and doesn't handle the triangle sections. It assumes that the hexagon is a rectangle with width s+t (=1.5*s).
		int y = (int) ((my - (x % 2) * r) / h); //this gives the row easily. It needs to be offset by h/2 (=r)if it is in an even column
 
		/******FIX for clicking in the triangle spaces (on the left side only)*******/
		//dx,dy are the number of pixels from the hex boundary. (ie. relative to the hex clicked in)
		int dx = mx - x * (s + t);
		int dy = my - y * h;
 
		if (my - (x % 2) * r < 0) return p; // prevent clicking in the open halfhexes at the top of the screen
 
		//System.out.println("dx=" + dx + " dy=" + dy + "  > " + dx*r/t + " <");
		
		//even columns
		if (x%2==0) {
			if (dy > r) {	//bottom half of hexes
				if (dx * r / t < dy - r) {
					x--;
				}
			}
			if (dy < r) {	//top half of hexes
				if ((t - dx) * r / t > dy) {
					x--;
					y--;
				}
			}
		} else {  // odd columns
			if (dy > h) {	//bottom half of hexes
				if (dx * r / t < dy - h) {
					x--;
					y++;
				}
			}
			if (dy < h) {	//top half of hexes
				//System.out.println("" + (t- dx)*r/t +  " " + (dy - r));
				if ((t - dx) * r / t > dy - r) {
					x--;
				}
			}
		}
		p.x = x;
		p.y = y;
		return p;
	}
}
