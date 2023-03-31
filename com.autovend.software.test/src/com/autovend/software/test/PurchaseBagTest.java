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

package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
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
	    public void testAddItemPerUnit() {
			Numeral[] code = {Numeral.one, Numeral.five, Numeral.three, Numeral.four};
			Barcode barcode = new Barcode(code);
	        Product product = new BarcodedProduct(barcode, "Product", BigDecimal.valueOf(10.00), 100);
	        machineLogic.addItemPerUnit(product, 200.0);
	        Assert.assertNotNull(machineLogic.currentBill);
	        Assert.assertEquals(1, machineLogic.currentBill.getBillLength());
	        Assert.assertEquals(BigDecimal.valueOf(10.00), machineLogic.currentBill.getBillBalance());
	        assertEquals(200.0, machineLogic.currentBill.getBillExpectedWeight(), 0.0001);
	    }

	    @Test(expected = NullPointerException.class)
	    public void testGetBarcodedUnitFromBarcode() {
	    	Numeral[] code = {Numeral.one, Numeral.five, Numeral.three, Numeral.four};
			Barcode barcode = new Barcode(code);
	        BarcodedUnit barcodedUnit = SelfCheckoutMachineLogic.getBarcodedUnitFromBarcode(barcode);
	        Assert.assertNotNull(barcodedUnit);
	        Assert.assertEquals(barcode, barcodedUnit.getBarcode());
	        Assert.assertEquals(BigDecimal.valueOf(100), barcodedUnit.getWeight());
	    }
	    
	    @Test
	    public void testPurchaseBag1() throws OverloadException {
	    	machineLogic.addOwnBags(); // call this to set the machine lock to false
	        
	    	Numeral[] code = {Numeral.one, Numeral.five, Numeral.three, Numeral.four};
			Barcode barcode = new Barcode(code);
	        Product product = new BarcodedProduct(barcode, "Product", BigDecimal.valueOf(10.00), 100);
	        machineLogic.addItemPerUnit(product, 200.0);
	        // test with valid number of bags
	    	machineLogic.Purchase_bags(2);
	        assertEquals("Error: Weight not within acceptable range", machineLogic.message);
	        assertEquals("Expected response did not match", "Y", machineLogic.response);
	        
	    }
	    
	    @Test
	    public void testPurchaseBag2() throws OverloadException {
	    	machineLogic.addOwnBags(); // call this to set the machine lock to false
	        
	    	Numeral[] code = {Numeral.one, Numeral.five, Numeral.three, Numeral.four};
			Barcode barcode = new Barcode(code);
	        Product product = new BarcodedProduct(barcode, "Product", BigDecimal.valueOf(10.00), 100);
	        machineLogic.addItemPerUnit(product, 200.0);
	        // test with valid number of bags
	    	machineLogic.Purchase_bags(0);
	        assertEquals("Error: Weight not within acceptable range", machineLogic.message);
	        
	    }
}
