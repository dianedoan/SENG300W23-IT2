package com.autovend.software.test;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.external.CardIssuer;
import com.autovend.software.*;

public class PayWithCardTest {
	private SelfCheckoutStation station;
	private SelfCheckoutMachineLogic machineLogic;

	static CardIssuer cardIssuer;
	static Calendar expiry;

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
		
		// initialize card issuer
		cardIssuer = new CardIssuer("RBC");
		
		// initialize card expire date
		expiry = Calendar.getInstance();
		expiry.set(Calendar.YEAR, 2030);
		expiry.set(Calendar.MONTH, 10);
		
		// add cards to database
		cardIssuer.addCardData("123456", "Adam", expiry, "123", BigDecimal.valueOf(500));
		cardIssuer.addCardData("012345", "Bob", expiry, "123", BigDecimal.valueOf(100));
		
		// initialize cards
		pin = "0000";
		credit_card = new CreditCard("Mastercard", "123456", "Adam", "123", pin, true, true);
		debit_card = new DebitCard("VISA", "0123456", "Bob", "123", pin, true, true);
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
	 * Tests when a card is tapped four or more times.
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
	 * Tests when a card is swiped four or more times.
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
	 * Tests when a card is inserted and removed four or more times. 
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
	 * Tests when a card is tapped once.
	 */
	@Test
	public void CardTap() throws IOException {
		machineLogic.bank = cardIssuer;
		station.cardReader.tap(credit_card);
		machineLogic.payWithCard();	
	}
	
	/**
	 * Tests when a card is swiped once.
	 */
	@Test
	public void CardSwipe() throws IOException {
		machineLogic.bank = cardIssuer;
		station.cardReader.swipe(credit_card, signature);
		machineLogic.payWithCard();
	}
	
	/**
	 * Tests when a card is inserted once and removed.
	 */
	@Test (expected = SimulationException.class)
	public void CardInsert() throws IOException {
		machineLogic.bank = cardIssuer;
		station.cardReader.insert(credit_card, pin);
		machineLogic.payWithCard();
		station.cardReader.remove();
	}
	
}
