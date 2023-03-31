package src.com.autovend.software;

/*
 * Simulates sending messages to the attendant and keeping track of the most recent message.
 */
public class AttendantIO {
	
	private String mostRecentMessageToAttendant;
	
	/**
	 * Sets the most recent message as the one given as a parameter in this method.
	 * 
	 * @param message: String of the message sent to the attendant
	 */
	public void informAttendant(String message) {
		mostRecentMessageToAttendant = message;
	}
	
	/**
	 * Getter for the most recent message to the attendant
	 * 
	 * @return: String of the most recent message
	 */
	public String getMostRecentMessage() {
		return mostRecentMessageToAttendant;
	}

}
