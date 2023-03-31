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
import java.util.Scanner;
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
	 * */
	public int getMembershipNumber() {
		Scanner customer_input = new Scanner(System.in);
		System.out.println("Please enter membership number:");
		String mn = customer_input.nextLine();
		return parseMembershipNumber(mn);
	}


	/**
	 * Allows user to enter their membership number
	 * @return the number entered
	 * @throws NumberFormatException if number entered was not valid.
	 * */
	public int parseMembershipNumber(String mn) {
		if (mn.equals("cancel")) {
			return 0;
		} else {
			try {
				int membershipNumber = Integer.parseInt(mn);
				return membershipNumber;
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Enter proper membership number.");
			}
		}
	}


}

		
	

//<<<<<<< HEAD
	


//>>>>>>> branch 'main' of https://github.com/dianedoan/SENG300W23-IT2.git


