package it.polimi.ingsw.BianchiCorneo.actions;

import it.polimi.ingsw.BianchiCorneo.maps.cards.ObjectCard;
import it.polimi.ingsw.BianchiCorneo.players.Player;

/**This class saves also the object used (that caused the action)
 * @author Andrea Corneo
 *
 */
public class ObjectUsed extends Action {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5690517148973943280L;
	private ObjectCard obj;
	private final static String MESSAGE = "Object used";

	public ObjectUsed(Player player, ObjectCard obj) {
		super(player, MESSAGE);
		this.obj = obj;
	}
	
	/**Set the object used
	 * @param obj
	 */
	public void setObject (ObjectCard obj) {
		this.obj = obj;
	}
	
	/**Return the object used
	 * @return object used
	 */
	public ObjectCard getObject () {
		return obj;
	}
	
	@Override
	public String toString() {
		return super.toString() + ": " + obj;
	}

}
