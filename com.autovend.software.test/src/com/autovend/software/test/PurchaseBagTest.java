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
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
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
		
		@Test(expected = NullPointerException.class)
		public void testPurchaseBag() {
			transactionReceipt.addProduct(Bag);
			int lengthBefore = transactionReceipt.getBillLength();
			Product bag = transactionReceipt.getProductAt(1);
			SelfCheckoutMachineLogic machineLogic = null;
	        machineLogic.Purchase_bags(1);
	        int lengthAfter = transactionReceipt.getBillLength();
	        assertEquals(lengthAfter, lengthBefore + 1);
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
}
