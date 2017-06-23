package it.polimi.ingsw.BianchiCorneo;

import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.Table;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.AlienBase;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.DangerousSector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.HumanBase;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.SectorList;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.ShipSector;
import it.polimi.ingsw.BianchiCorneo.moves.Controller;
import it.polimi.ingsw.BianchiCorneo.moves.ControllerImpl;
import it.polimi.ingsw.BianchiCorneo.players.Alien;
import it.polimi.ingsw.BianchiCorneo.players.Human;
import it.polimi.ingsw.BianchiCorneo.players.Player;
import it.polimi.ingsw.BianchiCorneo.players.PlayerList;
import it.polimi.ingsw.BianchiCorneo.supervisor.GameSupervisor;

import org.junit.Before;
import org.junit.Test;

public class TableTest {
	Sector[][] sectorGrid;
	Sector hb;
	Sector ab;
	SectorList ships;
	Table t;
	Player h, a;
	int idGame = 0;
	Controller move;
	GameSupervisor sv;
	PlayerList pl;
	
	Sector s1;

	@Before
	public void setUp() throws Exception {
		sectorGrid = new Sector[MAPConst.DIMY][MAPConst.DIMX];
		for (int j = 0; j < MAPConst.DIMY; j++) {
			for (int i = 0; i < MAPConst.DIMX; i++) { 
				sectorGrid[j][i] = new DangerousSector(i, j);
			}
		}
		hb = new HumanBase(4, 5);
		ab = new AlienBase(5, 6);
		ships = new SectorList();
		ships.add(new ShipSector(13, 13));
		ships.add(new ShipSector(13, 14));
		t = new Table(sectorGrid, (HumanBase)hb, (AlienBase)ab, ships);
		h = new Human(0, idGame, t);
		h.placeOnMap();
		a = new Alien(1, idGame, t);
		a.placeOnMap();
		sv = new GameSupervisor(t);
		pl = new PlayerList();
		pl.add(h);
		pl.add(a);
		sv.setPlayerList(pl);
		sv.initGame();
		move = new ControllerImpl();
		((ControllerImpl)move).addGame(idGame, t, sv);
		
		s1 = new DangerousSector(3, 4);
	}
	
	@Test
	public void testAdjacentsEven() {
		SectorList sL = new SectorList();
		Sector s = sectorGrid[9][10];
		s.findAdjacents(t, s, sL, 1, MAPConst.EVENSECTOR);
		boolean flag = true;
		if (sL.contains(sectorGrid[8][9]))
			sL.remove(sectorGrid[8][9]);
		else
			flag = false;
		if (sL.contains(sectorGrid[8][10]))
			sL.remove(sectorGrid[8][10]);
		else
			flag = false;
		if (sL.contains(sectorGrid[8][11]))
			sL.remove(sectorGrid[8][11]);
		else
			flag = false;
		if (sL.contains(sectorGrid[9][9]))
			sL.remove(sectorGrid[9][9]);
		else
			flag = false;
		if (sL.contains(sectorGrid[9][11]))
			sL.remove(sectorGrid[9][11]);
		else
			flag = false;
		if (sL.contains(sectorGrid[10][10]))
			sL.remove(sectorGrid[10][10]);
		else
			flag = false;
		assertTrue("Finds adjacents of a sector", flag && sL.isEmpty());
	}
	
	@Test
	public void testAdjacentsOdd() {
		SectorList setOfSectors = new SectorList();
		Sector s = sectorGrid[10][11];
		s.findAdjacents(t, s, setOfSectors, 1, MAPConst.ODDSECTOR);
		boolean flag = true;
		if (setOfSectors.contains(sectorGrid[9][11]))
			setOfSectors.remove(sectorGrid[9][11]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[10][10]))
			setOfSectors.remove(sectorGrid[10][10]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[10][12]))
			setOfSectors.remove(sectorGrid[10][12]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[11][10]))
			setOfSectors.remove(sectorGrid[11][10]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[11][11]))
			setOfSectors.remove(sectorGrid[11][11]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[11][12]))
			setOfSectors.remove(sectorGrid[11][12]);
		else
			flag = false;
		assertTrue("Finds adjacents of a sector", flag && setOfSectors.isEmpty());
	}

}
