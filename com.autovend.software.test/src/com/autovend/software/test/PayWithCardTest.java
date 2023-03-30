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

	static Card credit_card;
	static Card debit_card;
	
	
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
		
		credit_card = new CreditCard("Mastercard", "1234567", "Adam", "123", "0000", true, true);
		debit_card = new DebitCard("VISA", "1234567", "Bob", "123", "0000", true, true);
	}

	/**
	 * Tear down after each test.
	 */
	@After
	public void tearDown() {
		station = null;
		machineLogic = null;
		
		credit_card = null;
		debit_card = null;
	}

	
	/**
	 * Tests when a credit card is attempted once to pay.
	 */
	@Test
	public void payWithCreditCard() {
		machineLogic.payWithCard();
		
	}

}
