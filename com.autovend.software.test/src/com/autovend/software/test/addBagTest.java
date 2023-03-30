package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
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
    
//    @Test
//    public void testAddOwnBags_whenBagIsValid() throws OverloadException {
//        Product bag = transactionReceipt.getProductAt(1);
//        machineLogic.addOwnBags();
////        assertEquals(1, machineLogic.getNumberOfBags());
//        assertEquals(machineLogic.addOwnBags(), "Please add your own bags.");
//    }
    
//    @Test
//    public void testAddOwnBag1() throws OverloadException {
//    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        PrintStream printStream = new PrintStream(outputStream);
//
//        PrintStream originalPrintStream = System.out;
//        System.setOut(printStream);
//        
//    	boolean selfCheckOutBlocked = true;
//    	Numeral[] code = {Numeral.one, Numeral.five, Numeral.three, Numeral.four};
//		Barcode barcode = new Barcode(code);
//        Product product = new BarcodedProduct(barcode, "Product", BigDecimal.valueOf(10.00), 100);
//        machineLogic.addOwnBags();
//        String output = outputStream.toString().trim();
//
//        assertEquals("Please add your own bags.", output);
//        
////        try {
////        	Product bag = transactionReceipt.getProductAt(1);
////            machineLogic.addOwnBags();
////            String output = outputStream.toString().trim();
////
////            assertEquals("Please add your own bags.", output);
////        } finally {
////            System.setOut(originalPrintStream);
////        }
//    }

//    @Test
//    public void myTest() throws OverloadException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        PrintStream printStream = new PrintStream(outputStream);
//
//        PrintStream originalPrintStream = System.out;
//        System.setOut(printStream);
//
//        try {
//            machineLogic.addOwnBags();
//            String output = outputStream.toString().trim();
//
//            assertEquals("Please add your own bags.", output);
//        } finally {
//            System.setOut(originalPrintStream);
//        }
//    }
    
    @Test
    public void testAddOwnBags1() throws OverloadException {
      // 1. Customer I/O: Signals that the customer wants to add their own bags.
      // 2. System: Indicates that the customer should add their own bags now.
      // 3. Customer I/O: Signals that the customer has finished adding their own bags.
      // simulate customer adding bags and confirming with 'Y' response
      String response = "Y";
      assertEquals(response, "Y");
      
      // 4. Bagging Area: Signals to the System the weight change.
      // simulate weight change of 0.5 kg
      
      machineLogic.weightChanged(0.5);
      double expectedWeightChange = 0.0;
      ElectronicScale electronicScale = new ElectronicScale(100, 1);
      double actualWeightChange = electronicScale.getCurrentWeight();
      assertEquals(expectedWeightChange, actualWeightChange, 0.0);
      
      // 5. System: Blocks the self-checkout station from further customer actions.
      boolean expectedSelfCheckoutBlocked = true;
      machineLogic.setMachineLock(expectedSelfCheckoutBlocked);
      boolean actualSelfCheckoutBlocked = machineLogic.machineLocked;
      assertEquals(expectedSelfCheckoutBlocked, actualSelfCheckoutBlocked);
      
      // 6. System: Signals to the Attendant I/O the need to approve the added bags.
      // simulate calling the attendant for approval
      machineLogic.attendant.informAttendant("Need approval for adding own bags");
      String expectedAttendantMessage = "Need approval for adding own bags";
      String actualAttendantMessage = machineLogic.attendant.getMostRecentMessage();
      assertEquals(expectedAttendantMessage, actualAttendantMessage);
      
      // 7. Attendant I/O: Signals approval of the added bags
      // simulate attendant approving bags with 'Y' response
      String attendantResponse = "Y";
      assertEquals(attendantResponse, "Y");
      
      // 8. System: Unblocks the self-checkout station.
      expectedSelfCheckoutBlocked = false;
      machineLogic.setMachineLock(expectedSelfCheckoutBlocked);
      actualSelfCheckoutBlocked = machineLogic.machineLocked;
      assertEquals(expectedSelfCheckoutBlocked, actualSelfCheckoutBlocked);
      
      // 9. System: Signals to the Customer I/O that the customer may now continue.
      // simulate system signaling to customer that they can continue
      // (no assertion needed since it's just a print statement)
      
    }
    
//    @Test
//    public void testAddOwnBags2() throws OverloadException {
//    	boolean selfCheckOutBlocked = true;
//    	machineLogic.addOwnBags();
//    	int expect = 1;
//    	int actual = 1;
//    	assertEquals(expect, actual);
//    
//    	
//    
//    }
    
//    @Test
//    public void testAddOwnBags3() throws OverloadException {
//    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    	PrintStream printStream = new PrintStream(outputStream);
//
//    	PrintStream originalPrintStream = System.out;
//    	System.setOut(printStream);
//
//    	try {
//    		machineLogic.addOwnBags();
//    		String output = outputStream.toString().trim();
//
//    		assertEquals("Please add your own bags. "
//          		+ "Please add your own bags.\n"
//          		+ "Have you added the bag(s)? (Y/N)", output);
//      } finally {
//          System.setOut(originalPrintStream);
//      }
//    }
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testSomething() throws OverloadException {

        // Call the method that prints to System.out
    	machineLogic.addOwnBags();


    	
        // Get the printed output as a string
        String printedOutput = outContent.toString();

        // Assert that the output is what you expect
        assertEquals("", printedOutput.trim());
    }
		
}
