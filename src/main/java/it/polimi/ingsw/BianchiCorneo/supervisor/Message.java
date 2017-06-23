package it.polimi.ingsw.BianchiCorneo.supervisor;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2333617636720981865L;
	private String message;

	public Message(String message) {
		this.message = message;
	}
	
	public String get() {
		return message;
	}

}
