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

public class SectorTest {
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
	public void testSectorEqualsReflexive(){
		assertTrue("Equals working properly", s1.sameAs(s1));
	}
	@Test
	public void testSectorEqualsSymmetric(){
		Sector s2 = new  Sector("D05");
		assertTrue("Simmetry of the equals method", s1.sameAs(s2) && s2.sameAs(s1));
	}
	@Test
	public void testSectorEqualsTransitive(){
		Sector s2 = new  Sector(3,4);
		Sector s3 = new Sector ("D05");
		assertTrue("Transitivity of the equals method", s1.sameAs(s2) && s2.sameAs(s3) && s1.sameAs(s3));
	}
}
