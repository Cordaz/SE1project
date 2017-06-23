package it.polimi.ingsw.BianchiCorneo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.Table;
import it.polimi.ingsw.BianchiCorneo.maps.cards.AdrenalineCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.AttackCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.LightCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.ObjectCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.SectorCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.SedativeCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.TeleportCard;
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
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MoveTest {
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
	public void testMovePl() throws RemoteException {
		controller.doAction(0, 0, "move_D06");
		assertEquals("The target sector and the current player sector has to be the same", h.getCurSector(), sectorGrid[5][3]);
	}
	
	@Test
	public void testMovePl1() throws RemoteException {
		controller.doAction(0, 0, "move_D06");
		assertTrue("Player's flag has to be set after the move", h.hasMoved());
	}
	
	@Test
	public void testAttack() throws RemoteException {
		PlayerList pl1;
		
		gameActions.movePlayer(a, sectorGrid[5][7]);
		gameActions.movePlayer(h, sectorGrid[5][7]);
		gameActions.attackSector(a);
		pl1 = t.attackSector(sectorGrid[5][7]);
		pl1.remove(a);
		boolean flag = true;
		if (pl1.contains(h))
			pl1.remove(h);
		else
			flag = false;
		assertTrue("The target player, and only him, has to be returned from the function", a.hasAttacked() && flag && pl1.isEmpty());
	}
	
	@Test
	public void testDiscardCard() throws RemoteException {
		ObjectCard card = new AdrenalineCard();
		ObjectCard card2 = new AdrenalineCard();
		ObjectCard card3 = new AdrenalineCard();
		ObjectCard card4 = new AdrenalineCard();
		h.giveObj(card);
		h.giveObj(card2);
		h.giveObj(card3);
		h.giveObj(card4);
		controller.doAction(0, 0, "discard_4");
		assertNull("The fourth card is replaced with 'null' after the discard action", h.getToDiscard());
	}
	
	@Test
	public void testDiscardCard2() throws RemoteException {
		ObjectCard card = new AdrenalineCard();
		ObjectCard card2 = new LightCard();
		ObjectCard card3 = new AdrenalineCard();
		ObjectCard card4 = new AttackCard();
		h.giveObj(card);
		h.giveObj(card2);
		h.giveObj(card3);
		h.giveObj(card4);
		controller.doAction(0, 0, "discard_2");
		assertTrue("Testing the switching function of the card to discard", h.getToDiscard() == null && h.getObjects()[1] instanceof AttackCard);
	}
	
	@Test
	public void testFakeNoise() throws RemoteException {
		Sector s = new Sector("J12");
		gameActions.fakeNoise(h, s);
		assertTrue("Noise in other sectors", h.hasMadeNoise() && h.hasMadeNoiseOn().sameAs(s));
	}
	
	@Test
	public void testSwitchObj() throws RemoteException {
		ObjectCard card = new AdrenalineCard();
		ObjectCard card2 = new AdrenalineCard();
		ObjectCard card3 = new AdrenalineCard();
		ObjectCard card4 = new AdrenalineCard();
		h.giveObj(card);
		h.giveObj(card2);
		h.giveObj(card3);
		h.giveObj(card4);
		gameActions.switchObj(h, card2);
		ObjectCard objs[] = h.getObjects();
		assertEquals("Switch selected object with the object to discard", objs[1], card4);
		assertEquals(h.getToDiscard(), card2);
	}
	
	@Test
	public void testLightOnEven() throws RemoteException {
		Sector s = sectorGrid[4][5];
		ObjectCard card = new LightCard();
		h.giveObj(card);
		gameActions.movePlayer(a, sectorGrid[5][5]);
		gameActions.lightOn(h, s, card);
		boolean flag = true;
		Map<Player, Sector> plLighted = t.lightOn(s);
		Set<Player> players = plLighted.keySet();
		while(players.iterator().hasNext()) {
			Player key = players.iterator().next();
			if (key.equals(h) && plLighted.get(key).sameAs(hb))
				plLighted.remove(key);
			else if (key.equals(a) && plLighted.get(key).sameAs(sectorGrid[5][5]))
				plLighted.remove(key);
			else
				flag = false;
		}
		ObjectCard[] objs = h.getObjects();
		assertTrue("Light the players near the target sector, and only them", flag && plLighted.isEmpty() && objs[0] == null);
	}
	
	@Test
	public void testLightOnOdd() throws RemoteException {
		Sector s = sectorGrid[5][5];
		ObjectCard card = new LightCard();
		h.giveObj(card);
		gameActions.movePlayer(a, sectorGrid[5][5]);
		gameActions.lightOn(h, s, card);
		boolean flag = true;
		Map<Player, Sector> plLighted = t.lightOn(s);
		Set<Player> players = plLighted.keySet();
		while(players.iterator().hasNext()) {
			Player key = players.iterator().next();
			if (key.equals(h) && plLighted.get(key).sameAs(hb))
				plLighted.remove(key);
			else if (key.equals(a) && plLighted.get(key).sameAs(sectorGrid[5][5]))
				plLighted.remove(key);
			else
				flag = false;
		}
		ObjectCard[] objs = h.getObjects();
		assertTrue("Light the players near the target sector, and only them", flag && plLighted.isEmpty() && objs[0] == null);
	}
	@Test
	public void testUseCardAttack() throws RemoteException {
		ObjectCard card = new AttackCard();
		h.giveObj(card);
		String result = gameActions.useCard(h, card);
		ObjectCard[] objs = h.getObjects();
		assertTrue("Use the attack card, setting the attack flag to 'true'", result.equals("Card Used") && ((Human)h).hasAttacked() && objs[0] == null);
	}
	@Test
	public void testUseCardTeleport() throws RemoteException {
		ObjectCard card = new TeleportCard();
		gameActions.movePlayer(h, sectorGrid[10][10]);
		h.giveObj(card);
		String result = gameActions.useCard(h, card);
		ObjectCard[] objs = h.getObjects();
		assertTrue("Teleports the human to his base", result.equals("Card Used") && h.getCurSector().sameAs(hb) && objs[0] == null);
	}
	@Test
	public void testUseCardSetative() throws RemoteException {
		ObjectCard card = new SedativeCard();
		h.giveObj(card);
		String result = gameActions.useCard(h, card);
		ObjectCard[] objs = h.getObjects();
		assertTrue("Sets the sedate flag to 'true'", result.equals("Card Used") && ((Human)h).sedate() && objs[0] == null);
	}
	@Test
	public void testUseCardAdrenaline() throws RemoteException {
		ObjectCard card = new AdrenalineCard();
		h.giveObj(card);
		String result = gameActions.useCard(h, card);
		ObjectCard[] objs = h.getObjects();
		assertTrue("Allows the player to have a moving range of two", result.equals("Card Used") && ((Human)h).adrenaline() && objs[0] == null);
	}
	@Test
	public void testUseCardAdrenalineMoved() throws RemoteException {
		ObjectCard card = new AdrenalineCard();
		h.giveObj(card);
		gameActions.movePlayer(h, sectorGrid[10][10]);
		String result = gameActions.useCard(h, card);
		ObjectCard[] objs = h.getObjects();
		assertTrue("The adrenaline card can't be used after moving", result.equals("You have already made your move this turn.") && objs[0] == card);
	}
	@Test
	public void testDrawCard() {
		SectorCard sc = gameActions.drawCard(h);
		boolean flag = false;
		if (sc.toString().contains("new object"))
			flag = true;
		assertTrue("Can draw a sector card with or without object", (flag && h.getObjects()[0] != null) || (!flag && h.getObjects()[0] == null));
		
	}
}
