package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.Product;
import com.autovend.software.SelfCheckoutMachineLogic;
import com.autovend.software.TransactionReceipt;

	public class PurchaseBagTest {
		private SelfCheckoutMachineLogic machineLogic;
	    private SelfCheckoutStation selfCheckoutStation;
	    public Product Bag;
	    private TransactionReceipt transactionReceipt;
		
	    @Before
	    public void setUp() throws Exception {
	        selfCheckoutStation = new SelfCheckoutStation(Currency.getInstance("USD"), new int[] {2, 5, 10}, 
	        		new BigDecimal[] {new BigDecimal ("2.0"), new BigDecimal ("5.0"), new BigDecimal ("10.0")}, 
	        		5, 2);
	        machineLogic = new SelfCheckoutMachineLogic(selfCheckoutStation);
	    }
		
		@Test
		public void testPurchaseBag() {
			transactionReceipt.addProduct(Bag);
			int lengthBefore = transactionReceipt.getBillLength();
			Product bag = transactionReceipt.getProductAt(1);
			SelfCheckoutMachineLogic machineLogic = null;
	        machineLogic.Purchase_bags(1);
	        int lengthAfter = transactionReceipt.getBillLength();
	        assertEquals(lengthAfter, lengthBefore + 1);
	}
}
