package com.autovend.software.test;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.Product;
import com.autovend.software.SelfCheckoutMachineLogic;

public class AddBagTest {

	private SelfCheckoutMachineLogic machineLogic;
    private SelfCheckoutStation selfCheckoutStation;
    public Product Bag;
    

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
    public void testAddOwnBags_whenBagIsValid() {
        Product bag = new Product(25, false);
        assertTrue(machineLogic.addOwnBags(bag));
        assertEquals(1, machineLogic.);
    }

	
}
