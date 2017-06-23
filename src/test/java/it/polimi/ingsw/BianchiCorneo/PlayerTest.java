package it.polimi.ingsw.BianchiCorneo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.Table;
import it.polimi.ingsw.BianchiCorneo.maps.cards.AdrenalineCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.LightCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.ObjectCard;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.AlienBase;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.DangerousSector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.HumanBase;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.SectorList;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.ShipSector;
import it.polimi.ingsw.BianchiCorneo.moves.Controller;
import it.polimi.ingsw.BianchiCorneo.moves.ControllerImpl;
import it.polimi.ingsw.BianchiCorneo.moves.GameActions;
import it.polimi.ingsw.BianchiCorneo.players.Alien;
import it.polimi.ingsw.BianchiCorneo.players.Human;
import it.polimi.ingsw.BianchiCorneo.players.Player;
import it.polimi.ingsw.BianchiCorneo.players.PlayerList;
import it.polimi.ingsw.BianchiCorneo.supervisor.GameSupervisor;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
	Sector[][] sectorGrid;
	Sector hb;
	Sector ab;
	SectorList ships;
	Table t;
	Player h, a;
	int idGame = 0;
	Controller controller;
	GameActions gameActions;
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
		sectorGrid[4][5] = hb;
		sectorGrid[5][6] = ab;
		ships = new SectorList();
		ships.add(new ShipSector(13, 13));
		ships.add(new ShipSector(13, 14));
		t = new Table(sectorGrid, (HumanBase)hb, (AlienBase)ab, ships);
		h = new Human(0, idGame, t);
		a = new Alien(1, idGame, t);
		sv = new GameSupervisor(t);
		pl = new PlayerList();
		pl.add(h);
		pl.add(a);
		sv.setPlayerList(pl);
		sv.initGame();
		controller = new ControllerImpl();
		Map<Integer, Table> tableList = new HashMap<Integer, Table>();
		Map<Integer, GameSupervisor> svList = new HashMap<Integer, GameSupervisor>();
		tableList.put(idGame, t);
		svList.put(idGame, sv);
		gameActions = new GameActions(tableList, svList);
		((ControllerImpl)controller).addGame(idGame, t, sv);
		a.setPlaying(true);
		h.setPlaying(true);
		s1 = new DangerousSector(3, 4);
	}
	
	@Test
	public void testAddObj() {
		ObjectCard card = new AdrenalineCard();
		h.giveObj(card);
		ObjectCard objs[] = h.getObjects();
		assertEquals("Give the player a new object", card, objs[0]);
	}
	@Test
	public void testAddExcessObj() {
		ObjectCard card = new AdrenalineCard();
		ObjectCard card2 = new AdrenalineCard();
		ObjectCard card3 = new AdrenalineCard();
		ObjectCard card4 = new AdrenalineCard();
		h.giveObj(card);
		h.giveObj(card2);
		h.giveObj(card3);
		assertFalse("If the player has 3 objects already, doesn't add the fourth", h.giveObj(card4));
	}
	@Test
	public void testRmObj() {
		ObjectCard card = new AdrenalineCard();
		ObjectCard card2 = new LightCard();
		h.giveObj(card);
		h.giveObj(card2);
		h.rmObj(card2);
		ObjectCard objs[] = h.getObjects();
		int i = 0;
		for (ObjectCard curObj : objs)
			if (curObj != null)
				i++;
		assertEquals("Properly removes one object", i, 1);
	}
	
	@Test
	public void testResetHuman() throws RemoteException {
		h.resetUtilities();
		assertTrue("Resets all human flags", !h.hasAttacked() && !h.hasMoved() && !((Human)h).sedate() && !((Human)h).adrenaline() && !h.hasMadeNoise());
	}
	@Test
	public void testResetAlien() throws RemoteException {
		a.resetUtilities();
		assertTrue("Resets all alien flags", !a.hasAttacked() && !a.hasMadeNoise());
	}
	
	@Test
	public void testPossibleMovesHumanEven() throws RemoteException {
		gameActions.movePlayer(h, sectorGrid[9][10]);
		SectorList sL = h.possibleMoves();
		boolean flag = true;
		for (int i = 0; i < sL.size(); i++)
			System.out.println(sL.get(i));
		System.out.println();
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
		assertTrue("Possible moves for a normal human", flag && sL.isEmpty());
	}
	@Test
	public void testPossibleMovesHumanOdd() throws RemoteException {
		gameActions.movePlayer(h, sectorGrid[10][11]);
		SectorList setOfSectors = h.possibleMoves();
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
		assertTrue("Possible moves for a normal human", flag && setOfSectors.isEmpty());
	}
	@Test
	public void testPossibleMovesHumanAdren() throws RemoteException {
		gameActions.movePlayer(h, sectorGrid[10][11]);
		((Human)h).useAdrenaline();
		SectorList setOfSectors = h.possibleMoves();
		boolean flag = true;
		if (setOfSectors.contains(sectorGrid[8][11]))
			setOfSectors.remove(sectorGrid[8][11]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][9]))
			setOfSectors.remove(sectorGrid[9][9]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][10]))
			setOfSectors.remove(sectorGrid[9][10]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][11]))
			setOfSectors.remove(sectorGrid[9][11]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][12]))
			setOfSectors.remove(sectorGrid[9][12]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][13]))
			setOfSectors.remove(sectorGrid[9][13]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[10][9]))
			setOfSectors.remove(sectorGrid[10][9]);
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
		if (setOfSectors.contains(sectorGrid[10][13]))
			setOfSectors.remove(sectorGrid[10][13]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[11][9]))
			setOfSectors.remove(sectorGrid[11][9]);
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
		if (setOfSectors.contains(sectorGrid[11][13]))
			setOfSectors.remove(sectorGrid[11][13]);
		else
			flag = false;

		if (setOfSectors.contains(sectorGrid[12][10]))
			setOfSectors.remove(sectorGrid[12][10]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[12][11]))
			setOfSectors.remove(sectorGrid[12][11]);
		else
			flag = false;

		if (setOfSectors.contains(sectorGrid[12][12]))
			setOfSectors.remove(sectorGrid[12][12]);
		else
			flag = false;
		assertTrue("Possible moves for a human who used adrenaline", flag && setOfSectors.isEmpty());
	}
	@Test
	public void testPossibleMovesAlien() throws RemoteException {
		gameActions.movePlayer(a, sectorGrid[10][11]);
		SectorList setOfSectors = a.possibleMoves();
		boolean flag = true;
		if (setOfSectors.contains(sectorGrid[8][11]))
			setOfSectors.remove(sectorGrid[8][11]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][9]))
			setOfSectors.remove(sectorGrid[9][9]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][10]))
			setOfSectors.remove(sectorGrid[9][10]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][11]))
			setOfSectors.remove(sectorGrid[9][11]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][12]))
			setOfSectors.remove(sectorGrid[9][12]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][13]))
			setOfSectors.remove(sectorGrid[9][13]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[10][9]))
			setOfSectors.remove(sectorGrid[10][9]);
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
		if (setOfSectors.contains(sectorGrid[10][13]))
			setOfSectors.remove(sectorGrid[10][13]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[11][9]))
			setOfSectors.remove(sectorGrid[11][9]);
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
		if (setOfSectors.contains(sectorGrid[11][13]))
			setOfSectors.remove(sectorGrid[11][13]);
		else
			flag = false;

		if (setOfSectors.contains(sectorGrid[12][10]))
			setOfSectors.remove(sectorGrid[12][10]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[12][11]))
			setOfSectors.remove(sectorGrid[12][11]);
		else
			flag = false;

		if (setOfSectors.contains(sectorGrid[12][12]))
			setOfSectors.remove(sectorGrid[12][12]);
		else
			flag = false;
		assertTrue("Possible moves for a normal alien", flag && setOfSectors.isEmpty());
	}
	@Test
	public void testPossibleMovesAlienPower() throws RemoteException {
		gameActions.movePlayer(a, sectorGrid[10][11]);
		((Alien)a).becomePowerful();
		SectorList setOfSectors = a.possibleMoves();
		boolean flag = true;
		if (setOfSectors.contains(sectorGrid[7][11]))
			setOfSectors.remove(sectorGrid[7][11]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[8][9]))
			setOfSectors.remove(sectorGrid[8][9]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[8][10]))
			setOfSectors.remove(sectorGrid[8][10]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[8][11]))
			setOfSectors.remove(sectorGrid[8][11]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[8][12]))
			setOfSectors.remove(sectorGrid[8][12]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[8][13]))
			setOfSectors.remove(sectorGrid[8][13]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][8]))
			setOfSectors.remove(sectorGrid[9][8]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][9]))
			setOfSectors.remove(sectorGrid[9][9]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][10]))
			setOfSectors.remove(sectorGrid[9][10]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][11]))
			setOfSectors.remove(sectorGrid[9][11]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][12]))
			setOfSectors.remove(sectorGrid[9][12]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][13]))
			setOfSectors.remove(sectorGrid[9][13]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[9][14]))
			setOfSectors.remove(sectorGrid[9][14]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[10][8]))
			setOfSectors.remove(sectorGrid[10][8]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[10][9]))
			setOfSectors.remove(sectorGrid[10][9]);
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
		if (setOfSectors.contains(sectorGrid[10][13]))
			setOfSectors.remove(sectorGrid[10][13]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[10][14]))
			setOfSectors.remove(sectorGrid[10][14]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[11][8]))
			setOfSectors.remove(sectorGrid[11][8]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[11][9]))
			setOfSectors.remove(sectorGrid[11][9]);
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
		if (setOfSectors.contains(sectorGrid[11][13]))
			setOfSectors.remove(sectorGrid[11][13]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[11][14]))
			setOfSectors.remove(sectorGrid[11][14]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[12][8]))
			setOfSectors.remove(sectorGrid[12][8]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[12][9]))
			setOfSectors.remove(sectorGrid[12][9]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[12][10]))
			setOfSectors.remove(sectorGrid[12][10]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[12][11]))
			setOfSectors.remove(sectorGrid[12][11]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[12][12]))
			setOfSectors.remove(sectorGrid[12][12]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[12][13]))
			setOfSectors.remove(sectorGrid[12][13]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[12][14]))
			setOfSectors.remove(sectorGrid[12][14]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[13][10]))
			setOfSectors.remove(sectorGrid[13][10]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[13][11]))
			setOfSectors.remove(sectorGrid[13][11]);
		else
			flag = false;
		if (setOfSectors.contains(sectorGrid[13][12]))
				setOfSectors.remove(sectorGrid[13][12]);
			else
				flag = false;
		assertTrue("Possible moves for an alien who ate a player", flag && setOfSectors.isEmpty());
	}
}
