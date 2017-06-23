package it.polimi.ingsw.BianchiCorneo.client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains constants used by Command Line Interface, such like colors
 * 
 * @author Mattia Bianchi
 *
 */
public class CLIConst {
	public static final String ANSI_RESET = "\u001b[0m";
	public static final String ANSI_BLACK = "\u001b[30m";
	public static final String ANSI_RED = "\u001b[31m";
	public static final String ANSI_GREEN = "\u001b[32m";
	public static final String ANSI_YELLOW = "\u001b[33m";
	public static final String ANSI_BLUE = "\u001b[34m";
	public static final String ANSI_PURPLE = "\u001b[35m";
	public static final String ANSI_CYAN = "\u001b[36m";
	public static final String ANSI_GRAY= "\u001b[37m";
	
	private CLIConst() {};
	
	public final static void clearConsole() {
	    try {
	        final String os = System.getProperty("os.name");

	        if (os.contains("Windows")) {
	            Runtime.getRuntime().exec("cls");
	        }
	        else {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (final Exception e) {
	        Logger.getGlobal().log(Level.ALL, "Error.", e);
	    }
	}
}
