package chatBase.model;
/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server. 
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no 
 * need to count bytes or to wait for a line feed at the end of the frame
 */
public class ChatMessageMessage extends ChatMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6244303134585142647L;
	
	/**
	 * 
	 */
	//protected static final long serialVersionUID = 1112122200L;

	
	// The different types of message sent by the Client
	// WHOISIN to receive the list of the users connected
	// MESSAGE an ordinary message
	// LOGOUT to disconnect from the Server
	//public static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2, ADVERTISEMENT = 3;
	//private int type;
	private String message;
	
	// constructor
	public ChatMessageMessage(int type, String message) {
		super(type);
		this.message = message;
	}
	
	// getters
	public String getMessage() {
		return message;
	}
}
