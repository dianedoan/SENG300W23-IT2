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

import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.Numeral;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.software.SelfCheckoutMachineLogic;
import com.autovend.software.TransactionReceipt;

	public class addBagTest {

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
    public void testAddOwnBags() {
        // Test with the bag weight less than the allowed limit
    	Numeral[] code = {Numeral.one, Numeral.five, Numeral.three, Numeral.four};
		Barcode barcode = new Barcode(code);
        Product product = new BarcodedProduct(barcode, "Product", BigDecimal.valueOf(10.00), 100);
        machineLogic.addItemPerUnit(product, 100);
        assertEquals(1, machineLogic.currentBill.getBillLength());
        assertEquals(new BigDecimal("10.0"), machineLogic.currentBill.getBillBalance());

        // Test with the bag weight more than the allowed limit
        Numeral[] code1 = {Numeral.one, Numeral.five, Numeral.three, Numeral.four};
		Barcode barcode1 = new Barcode(code1);
        Product product1 = new BarcodedProduct(barcode1, "Product", BigDecimal.valueOf(20.00), 100);
        machineLogic.addItemPerUnit(product1, 100);
        assertEquals(1, machineLogic.currentBill.getBillLength());
        assertEquals(new BigDecimal("10.0"), machineLogic.currentBill.getBillBalance());
        
    }
    
    @Test
    public void testAddOwnBags1() throws OverloadException {
      // 1. Customer I/O: Signals that the customer wants to add their own bags.
      // 2. System: Indicates that the customer should add their own bags now.
      // 3. Customer I/O: Signals that the customer has finished adding their own bags.
      // simulate customer adding bags and confirming with 'Y' response
      String response = "Y";
      assertEquals(response, "Y");
      // simulate weight change of 0.5 g
      
      ElectronicScale electronicScale = new ElectronicScale(100, 1);
  	  machineLogic.esObserver.reactToWeightChangedEvent(electronicScale, 10.0);
      double expectedWeightChange = 0.0;
      double actualWeightChange = electronicScale.getCurrentWeight();
      assertEquals(expectedWeightChange, actualWeightChange, 0.0);
      
      boolean expectedSelfCheckoutBlocked = true;
      machineLogic.setMachineLock(expectedSelfCheckoutBlocked);
      boolean actualSelfCheckoutBlocked = machineLogic.machineLocked;
      assertEquals(expectedSelfCheckoutBlocked, actualSelfCheckoutBlocked);
      
      machineLogic.attendant.informAttendant("Need approval for adding own bags");
      String expectedAttendantMessage = "Need approval for adding own bags";
      String actualAttendantMessage = machineLogic.attendant.getMostRecentMessage();
      assertEquals(expectedAttendantMessage, actualAttendantMessage);
      
      String attendantResponse = "Y";
      assertEquals(attendantResponse, "Y");
      
      expectedSelfCheckoutBlocked = false;
      machineLogic.setMachineLock(expectedSelfCheckoutBlocked);
      actualSelfCheckoutBlocked = machineLogic.machineLocked;
      assertEquals(expectedSelfCheckoutBlocked, actualSelfCheckoutBlocked);
      
    }

    @Test
    public void testAddBags2() throws OverloadException {

    	machineLogic.setResponse("Y");
    	machineLogic.addOwnBags();
    	
    	String expected = "Error: Weight not within acceptable range";
    	assertEquals(expected, machineLogic.message);
    }
    
    @Test
    public void testAddBags3() throws OverloadException {

    	machineLogic.setResponse("N");
    	machineLogic.addOwnBags();
    	
    	String expected = "Have you added the bag(s)? (Y/N)";
    	assertEquals(expected, machineLogic.message);
    }
    
    @Test
    public void testAddBags4() throws OverloadException {

    	machineLogic.setResponse("Y");
    	ElectronicScale electronicScale = new ElectronicScale(100, 1);
    	machineLogic.esObserver.reactToWeightChangedEvent(electronicScale, 10.0);
    	machineLogic.addOwnBags();
    	
    	String expected = "Error: Weight not within acceptable range";
    	assertEquals(expected, machineLogic.message);
    }
    
    @Test
    public void testAddBags5() throws OverloadException {

    	Numeral[] code = {Numeral.one, Numeral.five, Numeral.three, Numeral.four};
		Barcode barcode = new Barcode(code);
        Product product = new BarcodedProduct(barcode, "Product", BigDecimal.valueOf(10.00), 100);
        machineLogic.addItemPerUnit(product, 100);
    	
    	machineLogic.setResponse("Y");
    	ElectronicScale electronicScale = new ElectronicScale(100, 1);
    	machineLogic.esObserver.reactToWeightChangedEvent(electronicScale, 10.0);
    	machineLogic.addOwnBags();
    	
    	String expected = "Error: Weight not within acceptable range";
    	assertEquals(expected, machineLogic.message);
    }
		
}
