package it.polimi.ingsw.BianchiCorneo.maps;

import it.polimi.ingsw.BianchiCorneo.maps.sectors.AlienBase;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.DangerousSector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.HumanBase;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.NullSector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.SectorList;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.ShipSector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create the map from an input .txt file
 * 
 * @author Mattia Bianchi
 *
 */
public class CreateMap {
	
	/**
	 * Hided constructor
	 */
	private CreateMap() {
	}
	
	/**
	 * 
	 * @param name name of the .txt file
	 * @return the map based on the input file
	 */
	public static Table createMap(String name) {
		Sector[][] sectorGrid;
		String[][] s;
		HumanBase humanBase = null;
		AlienBase alienBase = null;
		
		File file;
		Scanner fileIn;
		try {
			file = new File(name + ".txt");
			fileIn = new Scanner(file);
			
			//Initialize map sectors and her auxiliary variable
			sectorGrid = new Sector[MAPConst.DIMY][MAPConst.DIMX];
			s = new String[MAPConst.DIMY][MAPConst.DIMX];
			
			//Assign appropriated sectors to map reading them from file
			for (int j = 0; j < MAPConst.DIMY; j++) {
				s[j] = fileIn.nextLine().split(MAPConst.SPLITTER);
				for (int i = 0; i < MAPConst.DIMX; i++) { 
					if (s[j][i].compareTo("0") == 0) {
						sectorGrid[j][i] = new NullSector(i, j);
					}
					else if (s[j][i].compareTo("1") == 0) {
						sectorGrid[j][i] = new Sector(i, j);
					}
					else if (s[j][i].compareTo("2") == 0) {
						sectorGrid[j][i] = new DangerousSector(i, j);
					}
				}
			}
			fileIn.close();
			
			//Receive information on bases and ships from file
			file = new File(name + "Init.txt");
			fileIn = new Scanner(file);
			int posY = fileIn.nextInt();
			int posX = fileIn.nextInt();
			sectorGrid[posY][posX] = new HumanBase(posX, posY);
			humanBase = (HumanBase)sectorGrid[posY][posX];
			posY = fileIn.nextInt();
			posX = fileIn.nextInt();
			sectorGrid[posY][posX] = new AlienBase(posX, posY);
			alienBase = (AlienBase)sectorGrid[posY][posX];
			int numOfShips = fileIn.nextInt();
			SectorList ships = new SectorList();
			for (int i = 0; i < numOfShips; i++) {
				posY = fileIn.nextInt();
				posX = fileIn.nextInt();
				sectorGrid[posY][posX] = new ShipSector(posX, posY);
				ships.add((ShipSector) sectorGrid[posY][posX]);
			}
			fileIn.close();
			return new Table(sectorGrid, humanBase, alienBase, ships);
		}
		catch (FileNotFoundException e) {
			Logger.getGlobal().log(Level.ALL, "Error.", e);
		}
		return null;
	}
}
