/**
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */

package com.autovend.software;

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
