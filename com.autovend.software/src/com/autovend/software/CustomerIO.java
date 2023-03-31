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
