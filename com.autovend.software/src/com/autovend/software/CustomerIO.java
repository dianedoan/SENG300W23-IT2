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
