package com.autovend.software;
import java.util.*;
import com.autovend.devices

import com.autovend.IllegalDigitException;
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
	
	/**
	 * Allows user to enter their membership number
	 * @return the number entered
	 * @throws IllegalArgumentException if not a number entered.
	 */
	
	public int getMembershipNumber() throws IllegalArgumentException{
		try {
		Scanner customer_input = new Scanner(System.in);
		System.out.println("Please enter membership number:");
		String mn = customer_input.nextLine();
		if(mn.equals("cancel")) {
			return 0;
		}else {
		int membershipNumber = Integer.parseInt(mn);
		return membershipNumber;
		}}catch(IllegalArgumentException e) {
			System.out.println("Enter proper membership number.");
		}

		
		
	}
	
	
	
	

}
