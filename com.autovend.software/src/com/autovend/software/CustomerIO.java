/**
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */

package com.autovend.software;

import java.math.BigDecimal;

/*
 * Temporary class for the CustomerIO to get and set the amount of money due
 */
public class CustomerIO {

	BigDecimal amount;
	
	// Setter
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	// Getter
	public BigDecimal getAmount() {
		return this.amount;
	}
}
