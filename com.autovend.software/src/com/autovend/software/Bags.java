package com.autovend.software;

import java.util.Scanner;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;

public class Bags {

	public boolean selfCheckOutBlocked = false;
	
	public void addOwnBags() throws OverloadException {
		if (!selfCheckOutBlocked) {
			System.out.println("Please add your own bags.");
			
			Scanner input = new Scanner(System.in);
			System.out.println("Have you added the bag(s)? (Y/N)");
			String response = input.nextLine();
			if (response.equalsIgnoreCase("Y")) {
				ElectronicScale electronicScale = new ElectronicScale(100, 1);
				double weightChange = electronicScale.getCurrentWeight();
				if (weightChange > 0.0) {
					selfCheckOutBlocked = true;
				}
			}
		}
	}
}
