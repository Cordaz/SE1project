package it.polimi.ingsw.BianchiCorneo.client;


public class NetworkInterfaceFactory {
	
	private NetworkInterfaceFactory() {
	}
	public static NetworkInterface getInterface(String param) {
		if ("1".equals(param)) 
			return new RMIInterface();
		else 
			return new SocketInterface();
	}
}
