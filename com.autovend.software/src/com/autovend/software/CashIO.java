package com.autovend.software;

import java.math.BigDecimal;

/*
 * Temporary class for CashIO to set and get the change due
 */
public class CashIO {

	private BigDecimal change;
	
	// Setter
	public void setChange(BigDecimal change) {
		this.change = change;
	}
	
	// Getter
	public BigDecimal getChange() {
		return this.change;
	}
}
