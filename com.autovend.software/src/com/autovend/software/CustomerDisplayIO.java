package com.autovend.software;

/*
 * Simulates sending messages to the customer's display and keeping track of the most recent message.
 * Also simulates getting input from the customer
 */
public class CustomerDisplayIO {
	
	private String mostRecentMessageToCustomer;
	private int MemberNumber;
	
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
	
	public int getMembershipNumber() {
		Scanner customer_input = new Scanner(System.)
		
	}
	
	
	
	

}
