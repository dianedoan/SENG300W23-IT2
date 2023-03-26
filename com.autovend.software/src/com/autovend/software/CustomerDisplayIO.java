package com.autovend.software;

/*
 * Simulates sending messages to the customer's display and keeping track of the most recent message.
 */
public class CustomerDisplayIO {
	
	private String mostRecentMessageToCustomer;
	
	/**
	 * Sets the most recent message as the one given as a parameter in this method.
	 * 
	 * @param message: String of the message sent to the customer display
	 */
	public void informCustomer(String message) {
		mostRecentMessageToCustomer = message;
	}
	
	/**
	 * Getter for the most recent message to the customer's display
	 * 
	 * @return: String of the most recent message
	 */
	public String getMostRecentMessage() {
		return mostRecentMessageToCustomer;
	}

}
