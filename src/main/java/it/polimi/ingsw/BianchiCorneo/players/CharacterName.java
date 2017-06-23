package it.polimi.ingsw.BianchiCorneo.players;

public class CharacterName {	
	private static final String[] charName = {"Piero Ceccarella, 1st alien", "Cpt. Ennio Maria Dominoni",
		"Vittorio Martana, 2nd alien", "Julia \"Cabal\" Niguloti, pilot", "Maria Galbani, 3rd alien", 
		"Silvano Porpora, psychologist", "Paolo Landon, 4th alien", "Tuccio \"Piri\" Brendon, private"};
	private static int next = 0;
	
	private CharacterName() {
	}
	
	public static String nextPlayerName() {
		String name = charName[next];
		next++;
		return name;
	}
	
	public static void reset() {
		next = 0;
	}
}
