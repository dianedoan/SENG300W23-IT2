/**
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */

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
