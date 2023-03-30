package com.autovend.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.external.ProductDatabases;
import com.autovend.products.*;
import com.autovend.software.*;

public class PayWithCardTest {
	private SelfCheckoutStation station;
	private SelfCheckoutMachineLogic machineLogic;
	
	/**
	 * Set up before each test.
	 */
	@Before
	public void setUp() {
		Currency currency = Currency.getInstance("CAD");
		int[] billDenominations = {5, 10, 20};
		BigDecimal[] coinDenominations = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10), BigDecimal.valueOf(0.25)};
		station = new SelfCheckoutStation(currency, billDenominations, coinDenominations, 10000, 5);
		machineLogic = new SelfCheckoutMachineLogic(station);
	}

	/**
	 * Tear down after each test.
	 */
	@After
	public void tearDown() {
		station = null;
		machineLogic = null;	
	}

	
	/**
	 * Tests when ...
	 */
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
