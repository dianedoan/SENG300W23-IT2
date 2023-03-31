/**
 * @author: Abigia Debebe (30134608),
 * @author: Akib Hasan Aryan (30141456),
 * @author: Andy Tran (30125341),
 * @author: Delaram Bahreini Esfahani (30133864),
 * @author: Diane Doan (30052326),
 * @author: Faiyaz Altaf Pranto (30162576),
 * @author: Ishita Chandra (30159580),
 * @author: Nam Nguyen Vu (30154892),
 * @author: River Sanoy (30129508),
 * @author: Ryan Haoping Zheng (30072318),
 * @author: Xinzhou Li (30066080)
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
