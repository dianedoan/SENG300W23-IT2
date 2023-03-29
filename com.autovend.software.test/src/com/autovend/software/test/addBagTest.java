package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
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
        machineLogic.addOwnBags(2, 0.15);
        assertEquals(2, machineLogic.currentBill.getBagsCount());
        assertEquals(new BigDecimal("0.30"), machineLogic.currentBill.getBagsPrice());

        // Test with the bag weight more than the allowed limit
        machineLogic.addOwnBags(1, 1.5);
        assertEquals(3, machineLogic.currentBill.getBagsCount());
        assertEquals(new BigDecimal("0.50"), machineLogic.currentBill.getBagsPrice());
        
    }
    
    @Test
    public void testAddOwnBags_whenBagIsValid() throws OverloadException {
        Product bag = transactionReceipt.getProductAt(1);
        machineLogic.addOwnBags();
//        assertEquals(1, machineLogic.getNumberOfBags());
        assertEquals(machineLogic.addOwnBags(), "Please add your own bags.");
    }

    @Test
    public void myTest() throws OverloadException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        // Replace System.out with a mocked PrintStream that writes to the ByteArrayOutputStream
        PrintStream originalPrintStream = System.out;
        System.setOut(printStream);

        try {
            machineLogic.addOwnBags();
            String output = outputStream.toString().trim();

            assertEquals("Please add your own bags.", output);
        } finally {
            System.setOut(originalPrintStream);
        }
    }
	
}
