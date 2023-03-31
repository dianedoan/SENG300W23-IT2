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
import java.util.ArrayList;
import com.autovend.*;
import com.autovend.products.Product;

/**
 * @author Tyson
 *
 */
public class TransactionReceipt {
	
	/**
	 * Creates a new Bill of Sale
	 */
	
	ArrayList<Product> currentBill;
	BigDecimal billBalance;




	double billExpectedWeight;
	
	public TransactionReceipt() {
		
		currentBill = new ArrayList<>();
		this.billBalance = BigDecimal.valueOf(0);
	
	}
	
	/**
	 * Creates a new Bill of Sale with the product p in it
	 */
	
	public TransactionReceipt(Product p) {
		
		currentBill = new ArrayList<Product>();
		this.billBalance = BigDecimal.valueOf(0);
		this.currentBill.add(p);
	
	}
	

	
	/**
	 * Gets the product at index i. First product is stored at i = 1
	 */
	
	public Product getProductAt(int i) {
		return this.currentBill.get(i);
			
	}

	
	/**
	 * Returns the number of items in the bill
	 * @return int = number of items in bill
	 */
	public int getBillLength() {
		return currentBill.size();
	}
	


	public void addProduct(Product p) {
		currentBill.add(p);
		p.getPrice();
		
	}
	
	public BigDecimal getBillBalance() {
		return this.billBalance;
	}

	public void setBillBalance(BigDecimal billBalance) {


		this.billBalance = billBalance;
	}
	
	/**
	 * Used to increase or decrease Balance by a Big Decimal
	 * 
	 * @param BigDecimal addend
	 * @return 
	 */
	public BigDecimal augmentBillBalance(BigDecimal addend) {
		this.billBalance = this.billBalance.add(addend);
		return this.billBalance;
	}

	public double getBillExpectedWeight() {
		return billExpectedWeight;
	}

	
	
	public void setBillExpectedWeight(double billExpectedWeight) {
		this.billExpectedWeight = billExpectedWeight;
	}
	
	/**
	 * Used to increase or decrease the expected weight by a double
	 * 
	 * @param BigDecimal double
	 */
	public void augmentExpectedWeight(double addend) {
		this.billExpectedWeight += addend;
	}
	
}
