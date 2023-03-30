package com.autovend.software.test;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.software.*;

public class PayWithCardTest {
	private SelfCheckoutStation station;
	private SelfCheckoutMachineLogic machineLogic;

	static Card credit_card;
	static Card debit_card;
	
	static BufferedImage signature;
	
	static String pin;
	
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
		
		pin = "0000";
		credit_card = new CreditCard("Mastercard", "1234567", "Adam", "123", pin, true, true);
		debit_card = new DebitCard("VISA", "1234567", "Bob", "123", pin, true, true);
		
		signature = new BufferedImage(24, 24, 13);
		
		station.cardReader.enable();
	}

	/**
	 * Tear down after each test.
	 */
	@After
	public void tearDown() {
		station.cardReader.disable();
		
		station = null;
		machineLogic = null;
		
		credit_card = null;
		debit_card = null;
	}
	
	/**
	 * Tests when a credit card is tapped more than four times.
	 */
	@Test
	public void reachMaximumAttemptTap() throws IOException {
		station.cardReader.tap(credit_card);
		machineLogic.payWithCard();
		station.cardReader.tap(credit_card);
		machineLogic.payWithCard();
		station.cardReader.tap(credit_card);
		machineLogic.payWithCard();
		station.cardReader.tap(credit_card);
		machineLogic.payWithCard();
	}
	
	/**
	 * Tests when a credit card is swiped more than four times.
	 */
	@Test
	public void reachMaximumAttemptSwipe() throws IOException {
		station.cardReader.swipe(credit_card, signature);
		machineLogic.payWithCard();
		station.cardReader.swipe(credit_card, signature);
		machineLogic.payWithCard();
		station.cardReader.swipe(credit_card, signature);
		machineLogic.payWithCard();
		station.cardReader.swipe(credit_card, signature);
		machineLogic.payWithCard();
	}

	/**
	 * Tests when a credit card is inserted and removed more than four times. 
	 */
	@Test (expected = SimulationException.class)
	public void reachMaximumAttemptInsert() throws IOException {
		station.cardReader.insert(credit_card, pin);
		machineLogic.payWithCard();
		station.cardReader.remove();
		station.cardReader.insert(credit_card, pin);
		machineLogic.payWithCard();
		station.cardReader.remove();
		station.cardReader.insert(credit_card, pin);
		machineLogic.payWithCard();
		station.cardReader.remove();
		station.cardReader.insert(credit_card, pin);
		machineLogic.payWithCard();
		station.cardReader.remove();
	}
	
	/**
	 * Tests when a credit card is tapped once.
	 */
	@Test
	public void cardTap() throws IOException {
		station.cardReader.tap(credit_card);
		machineLogic.payWithCard();

	}
}
