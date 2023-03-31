/**
 * @author: Abigia Debebe (30134608),
 * @author: Akib Hasan Aryan (),
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

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;

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
		
		// add credit card to database but not the debit card
		cardIssuer.addCardData("123456", "Adam", expiry, "123", BigDecimal.valueOf(500));
		
		// initialize cards
		pin = "0000";
		credit_card = new CreditCard("Mastercard", "123456", "Adam", "123", pin, true, true);
		debit_card = new DebitCard("VISA", "012345", "Bob", "123", pin, true, true);
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
		try {
			station.cardReader.tap(credit_card);
			machineLogic.payWithCard();
			station.cardReader.tap(credit_card);
			machineLogic.payWithCard();
			station.cardReader.tap(credit_card);
			machineLogic.payWithCard();
			station.cardReader.tap(credit_card);
			machineLogic.payWithCard();	
		}
		catch (ChipFailureException e) {
		}
		
	}
	
	/**
	 * Tests when a card is swiped four or more times.
	 */
	@Test
	public void reachMaximumAttemptSwipe() throws IOException {
		try {
			station.cardReader.swipe(credit_card, signature);
			machineLogic.payWithCard();
			station.cardReader.swipe(credit_card, signature);
			machineLogic.payWithCard();
			station.cardReader.swipe(credit_card, signature);
			machineLogic.payWithCard();
			station.cardReader.swipe(credit_card, signature);
			machineLogic.payWithCard();
		}
		catch (MagneticStripeFailureException e){
		}
	}

	/**
	 * Tests when a card is inserted and removed four or more times. 
	 */
	@Test
	public void reachMaximumAttemptInsert() throws IOException {
		try {
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
		catch (ChipFailureException e){
		}
		catch (SimulationException e){
		}
	}

	/**
	 * Tests when a valid card is tapped once.
	 */
	@Test
	public void validCardTap() throws IOException {
		try {
			machineLogic.bank = cardIssuer;
			station.cardReader.tap(credit_card);
			machineLogic.payWithCard();
		}
		catch (ChipFailureException e) {
		}
	}
	
	/**
	 * Tests when a valid card is swiped once.
	 */
	@Test
	public void validCardSwipe() throws IOException {
		try {
			machineLogic.bank = cardIssuer;
			station.cardReader.swipe(credit_card, signature);
			machineLogic.payWithCard();
		}
		catch (MagneticStripeFailureException e) {
		}
	}
	
	/**
	 * Tests when a valid card is inserted once and removed.
	 */
	@Test
	public void validCardInsert() throws IOException {
		try {
			machineLogic.bank = cardIssuer;
			station.cardReader.insert(credit_card, pin);
			machineLogic.payWithCard();
			station.cardReader.remove();
		}
		catch (ChipFailureException e) {
		}
		catch (SimulationException e) {
		}
	}
	
	/**
	 * Tests when a card that is not in the database is tapped once.
	 */
	@Test
	public void invalidCardTap() throws IOException {
		try {
			machineLogic.bank = cardIssuer;
			station.cardReader.tap(debit_card);
			machineLogic.payWithCard();
		}
		catch (ChipFailureException e) {
		}
	}
	
	/**
	 * Tests when a card that is not in the database is swiped once.
	 */
	@Test
	public void invalidCardSwipe() throws IOException {
		try {
			machineLogic.bank = cardIssuer;
			station.cardReader.swipe(debit_card, signature);
			machineLogic.payWithCard();
		}
		catch (MagneticStripeFailureException e) {
		}
	}
	
	/**
	 * Tests when a card that is not in the database is inserted once and removed.
	 */
	@Test
	public void invalidCardInsert() throws IOException {
		try {
			machineLogic.bank = cardIssuer;
			station.cardReader.insert(debit_card, pin);
			machineLogic.payWithCard();
			station.cardReader.remove();
		}
		catch (ChipFailureException e) {
		}
		catch (SimulationException e) {
		}
	}
}
