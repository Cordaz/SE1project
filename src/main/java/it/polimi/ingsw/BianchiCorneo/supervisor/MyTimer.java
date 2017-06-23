package it.polimi.ingsw.BianchiCorneo.supervisor;

import java.util.TimerTask;

/**This class provides a timer task
 * @author Andrea Corneo
 *
 */
public class MyTimer extends TimerTask {
	private TimerInterface caller;
	
	/**Constructor
	 * @param caller of the timer
	 */
	public MyTimer(TimerInterface caller) {
		this.caller = caller;
	}
	
	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		synchronized(caller) {
			caller.setTimeExceeded();
			caller.notify();
		}
	}
}
