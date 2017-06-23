package it.polimi.ingsw.BianchiCorneo;

import static org.junit.Assert.*;
import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.maps.*;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.*;
import it.polimi.ingsw.BianchiCorneo.players.*;
import it.polimi.ingsw.BianchiCorneo.supervisor.*;

import org.junit.*;

public class SupervisorTest {
	GameSupervisor sv;
	Table t;
	PlayerList pl;

	@Before
	public void setUp() {
		t = CreateMap.createMap(MAPConst.MAPNAME);
		sv = new GameSupervisor(t);
		pl = new PlayerList();
		for(int i=0; i<4; i++)
			if (i%2 == 0)
				pl.add(new Alien(i, 1, t));
			else
				pl.add(new Human(i, 1, t));
		sv.setPlayerList(pl);
		sv.initGame();
		
		//Place player
		pl.get(0).addToPath(new Sector("M06"));
		pl.get(1).addToPath(new Sector("M06"));
		pl.get(2).addToPath(new Sector("M06"));
		pl.get(3).addToPath(new Sector("M06"));
		
		((Human)pl.get(3)).setShield();
	}
	
	@Test
	public void killTest() {
		PlayerList attacked = new PlayerList();
		attacked.copy(pl);
		sv.notifySupervisor(new Action(pl.get(0), "attacked"), attacked);
		assertTrue(pl.get(0).isAlive());
		//assertFalse(pl.get(1).isAlive());
		//assertFalse(pl.get(2).isAlive());
		assertTrue(pl.get(3).isAlive());
	}
	
	
}
