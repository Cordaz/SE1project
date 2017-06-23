package it.polimi.ingsw.BianchiCorneo.supervisor;


/**Provides constant mainly to the supervisor
 * @author Andrea Corneo
 *
 */
public class SVConst {
	public static final int MAXPLAYER = 2;
	public static final long TIMEOUTSERVER = 5*60*1000; //5 min
	public static final long TIMEOUTTURN = 2*60*1000; //2 min
	public static final long TIMECONCLUSION = 60*1000; //1 min

	//Socket protocol: communication standard string
	
	public static final String SPLIT = "_";
	public static final String INVALID = "invalid_";
	
	//Game
	public static final String WAIT = "wait_";
	public static final String TURN = "turn_";
	public static final String GAMEVALID = "gamevalid_";
	public static final String GAMESUSPENDED = "gamesuspended_";
	public static final String TIMEOUT = "timeout_";
	public static final String LASTPLAYERLEFT = "lastplayerleft_";
	public static final String HAPPENED = "happened_";
	public static final String STARTTURN = "startturn_";
	public static final String ENDTURN = "endturn_";
	public static final String CONNABORTED = "connaborted_";
	public static final String NOTIFY = "notify_";
	
	//Player
	public static final String HUMAN = "human";
	public static final String ALIEN = "alien";
	public static final String NAME = "name_";
	
	//Move
	public static final String MOVESET = "moveset_";
	public static final String MOVE = "move_";
	public static final String PATH = "path_";
	public static final String CARD = "card_";
	public static final String ATTACK = "attack_";
	public static final String NOPLAYER = "There are no sufficient player to continue play this game";
	public static final String ENDGAME = "The game is ended, here's the result:\n";
	public static final String WIN = "The following player had successfully escaped from the ship:\n";
	public static final String KILLED = "The following player had been killed:\n";
	
}
