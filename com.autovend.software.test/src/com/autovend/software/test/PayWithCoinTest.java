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

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CoinDispenserObserver;
import com.autovend.devices.observers.CoinSlotObserver;
import com.autovend.devices.observers.CoinStorageObserver;
import com.autovend.devices.observers.CoinTrayObserver;
import com.autovend.devices.observers.CoinValidatorObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.*;
import com.autovend.software.*;


public class PayWithCoinTest {
	SelfCheckoutStation station;
	SelfCheckoutMachineLogic machineLogic;
	CustomerIO customer;
	Coin coin;
	CashIO cash;
	PayWithCoin payment; 
	Currency currency;
	CoinSlotTestObserver csObserver;
	CoinDispenserTestObserver cdObserver;
	CoinTrayTestObserver ctObserver;
	CoinStorageTestObserver cstorObserver;
	CoinValidatorTestObserver cvObserver;
	
	
	
	
	@Before
	public void setup() throws DisabledException, OverloadException{
		currency = Currency.getInstance("CAD");
		int[] billDenominations = {5, 10, 20, 50, 100};
		BigDecimal[] coinDenominations = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10), BigDecimal.valueOf(0.25), BigDecimal.valueOf(1.00), BigDecimal.valueOf(2.00)};
		station = new SelfCheckoutStation(currency, billDenominations, coinDenominations, 10000, 5);
		machineLogic = new SelfCheckoutMachineLogic(station);
		customer = new CustomerIO();
		cash = new CashIO();
		payment = new PayWithCoin(station, customer);
		csObserver = new CoinSlotTestObserver();
		cdObserver = new CoinDispenserTestObserver();
		ctObserver = new CoinTrayTestObserver();
		cstorObserver = new CoinStorageTestObserver();
		cvObserver = new CoinValidatorTestObserver();
		
		
		
	
		
		
	}
	
	@After
	public void tearDown(){
		station = null;
		machineLogic = null;
		coin = null;
		payment.coinsList = null;
		payment.setTotal(BigDecimal.valueOf(0.00));
		
		
	}
	
	
	@Test
	public void testConstructor() throws DisabledException, OverloadException{
	payment = new PayWithCoin(station, customer);
	assertNotNull(payment);
	assertTrue(payment instanceof PayWithCoin);
	}
	

	
	@Test
	public void testValidCoinToCoinsList() throws DisabledException,OverloadException{
		List<BigDecimal> expectedCoinsList = new ArrayList<>();
	    expectedCoinsList.add(BigDecimal.valueOf(1.00));
	    coin  = new Coin(BigDecimal.valueOf(1.00),currency);
	    payment.coin = coin;
	    payment.setTotal(BigDecimal.valueOf(1.00));
	    payment.coinValidate(station, customer);
	    assertEquals(expectedCoinsList, payment.coinsList);
	}
	
	@Test
	public void testValidCoinsToCoinsList() throws DisabledException,OverloadException{
		List<BigDecimal> expectedCoinsList = new ArrayList<>();
	    expectedCoinsList.add(BigDecimal.valueOf(1.00));
	    coin  = new Coin(BigDecimal.valueOf(1.00),currency);
	    payment.coin = coin;
	    payment.setTotal(BigDecimal.valueOf(1.00));
	    payment.coinValidate(station, customer);
	    coin = new Coin(BigDecimal.valueOf(0.05), currency);
	    payment.coin = coin;
	    payment.coinValidate(station, customer);
	    expectedCoinsList.add(BigDecimal.valueOf(0.05));
	    assertEquals(expectedCoinsList, payment.coinsList);
	}
	
	@Test
	public void testInvalidCoinAddedToCoinsList() throws DisabledException {
	    coin = new Coin(BigDecimal.valueOf(0.17), currency);
	    List<BigDecimal> expectedCoinsList = new ArrayList<>();
	    payment.coin = coin;
	    expectedCoinsList.add(BigDecimal.valueOf(0.17));
	    payment.coinValidate(station, customer);
	    assertNotSame(expectedCoinsList, payment.coinsList);
	}
	
	@Test
	public void testInvalidAndValidCoinAddedToCoinsList() throws DisabledException {
		payment.setTotal(BigDecimal.valueOf(1.00));
	    Coin coin = new Coin(BigDecimal.valueOf(0.17), currency);
	    List<BigDecimal> dummyCoinsList = new ArrayList<>();
	    List<BigDecimal> expectedCoinsList = new ArrayList<>();
	    payment.coin = coin;
	    dummyCoinsList.add(BigDecimal.valueOf(0.17));
	    payment.coinValidate(station, customer);
	    coin = new Coin(BigDecimal.valueOf(0.25), currency);
	    payment.coin = coin;
	    payment.coinValidate(station, customer);
	    dummyCoinsList.add(BigDecimal.valueOf(0.25));
	    expectedCoinsList.add(BigDecimal.valueOf(0.25));
	    assertNotSame(dummyCoinsList, payment.coinsList);
	    assertEquals(expectedCoinsList, payment.coinsList);
	}
	
	@Test
	public void testTotalAmountAfterAddingValidCoin() throws DisabledException {
		payment.setTotal(BigDecimal.valueOf(0.00));
	    Coin coin = new Coin(BigDecimal.valueOf(1.00), currency);
	    BigDecimal expectedRemainingAmount = BigDecimal.valueOf(1.00);
	    payment.coin = coin;
	    payment.coinValidate(station, customer);
	    assertEquals(expectedRemainingAmount.negate().setScale(2), customer.getAmount());
	}
	
	
	@Test
	public void testTotalAmountAfterValidCoinsAdded() throws DisabledException {
		
		payment.coinsList.add(BigDecimal.valueOf(0.25));
		payment.coinsList.add(BigDecimal.valueOf(1.00));
		payment.coinsList.add(BigDecimal.valueOf(2.00));
		payment.setTotal(BigDecimal.valueOf(15.95));
		
	 
		payment.coin = new Coin (BigDecimal.valueOf(1.00), currency);
		payment.coinValidate(station, customer);
		BigDecimal expectedAmount = BigDecimal.valueOf(11.70).setScale(2);
	    assertEquals(expectedAmount, customer.getAmount());
	    

	}
	
	@Test
	public void testInValidCoinAdded() throws DisabledException{
		coin = new Coin(BigDecimal.valueOf(0.17), currency);
	    payment.coin = coin;
	    payment.coinValidate(station, customer);
	    assertNull(customer.getAmount());
	    
	}
	
	@Test
    public void testCheckChangeReturnsFalse() {
		payment.cash.setChange(BigDecimal.valueOf(1.00));
		assertFalse(payment.checkChange());
    }
	
	@Test
    public void testCheckChangeReturnsTrue() {
		payment.cash.setChange(BigDecimal.valueOf(0.00));
		assertTrue(payment.checkChange());
    }

	
	
	@Test
	public void SuspendSystemTest(){
		station.coinSlot.register(csObserver);
		station.coinStorage.register(cstorObserver);
		station.coinTray.register(ctObserver);
		station.coinValidator.register(cvObserver);
		station.coinDispensers.get(BigDecimal.valueOf(0.05)).register(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(0.10)).register(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(0.25)).register(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(1.00)).register(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(2.00)).register(cdObserver);
		
		payment.suspendSystem();
		
	}
	
	@Test
	public void GeneralObserversTest(){
		
		//register all observers
		station.coinSlot.register(csObserver);
		station.coinStorage.register(cstorObserver);
		station.coinTray.register(ctObserver);
		station.coinValidator.register(cvObserver);
		station.coinDispensers.get(BigDecimal.valueOf(0.05)).register(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(0.10)).register(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(0.25)).register(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(1.00)).register(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(2.00)).register(cdObserver);
		
		//disable all observers
		station.coinSlot.disable();
		station.coinStorage.disable();
		station.coinTray.disable();
		station.coinValidator.disable();
		station.coinDispensers.get(BigDecimal.valueOf(0.05)).disable();
		station.coinDispensers.get(BigDecimal.valueOf(0.10)).disable();
		station.coinDispensers.get(BigDecimal.valueOf(0.25)).disable();
		station.coinDispensers.get(BigDecimal.valueOf(1.00)).disable();
		station.coinDispensers.get(BigDecimal.valueOf(2.00)).disable();
		
		//enable all observers
		station.coinSlot.enable();
		station.coinStorage.enable();
		station.coinTray.enable();
		station.coinValidator.enable();
		station.coinDispensers.get(BigDecimal.valueOf(0.05)).enable();
		station.coinDispensers.get(BigDecimal.valueOf(0.10)).enable();
		station.coinDispensers.get(BigDecimal.valueOf(0.25)).enable();
		station.coinDispensers.get(BigDecimal.valueOf(1.00)).enable();
		station.coinDispensers.get(BigDecimal.valueOf(2.00)).enable();
		
		//deregister all observers
		station.coinSlot.deregister(csObserver);
		station.coinStorage.deregister(cstorObserver);
		station.coinTray.deregister(ctObserver);
		station.coinValidator.deregister(cvObserver);
		station.coinDispensers.get(BigDecimal.valueOf(0.05)).deregister(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(0.10)).deregister(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(0.25)).deregister(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(1.00)).deregister(cdObserver);
		station.coinDispensers.get(BigDecimal.valueOf(2.00)).deregister(cdObserver);		

		
	
	}
	
	
	
	
	
	
	

	public class CoinSlotTestObserver implements AbstractDeviceObserver, CoinSlotObserver {
		
		private AbstractDevice<? extends AbstractDeviceObserver> device;
		
		public CoinSlotTestObserver(){
			this.device = null;
			
		}
		
		public void reactToCoinInsertedEvent(CoinSlot slot){
			this.device = slot;
		}
		
		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			this.device = device;
			
		}
		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			this.device = device;
			
		}
	}
	
	public class CoinDispenserTestObserver implements AbstractDeviceObserver, CoinDispenserObserver {
		
		private AbstractDevice<? extends AbstractDeviceObserver> device;
		
		public CoinDispenserTestObserver(){
			this.device = null;
		}
	
		public void reactToCoinsFullEvent(CoinDispenser dispenser){
			this.device = dispenser;
		}

		public void reactToCoinsEmptyEvent(CoinDispenser dispenser){
			this.device = dispenser;
		}

		public void reactToCoinAddedEvent(CoinDispenser dispenser, Coin coin){
			this.device = dispenser;
		}

		public void reactToCoinRemovedEvent(CoinDispenser dispenser, Coin coin){
			this.device = dispenser;
		}

		public void reactToCoinsLoadedEvent(CoinDispenser dispenser, Coin... coins){
			this.device = dispenser;
		}
	
		public void reactToCoinsUnloadedEvent(CoinDispenser dispenser, Coin... coins){
			this.device = dispenser;
		}

		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			this.device = device;
		}

		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			this.device = device;
		}
	}
	
	
	public class CoinStorageTestObserver implements AbstractDeviceObserver, CoinStorageObserver {
		
		private AbstractDevice<? extends AbstractDeviceObserver> device;
	
		public CoinStorageTestObserver(){
			this.device = null;
		}
		
		public void reactToCoinsFullEvent(CoinStorage unit){
			this.device = unit;
		}

		void coinAddedEvent(CoinStorage unit){
			this.device = unit;
		}

	
		public void reactToCoinsLoadedEvent(CoinStorage unit){
			this.device = unit;
		}

	
		public void reactToCoinsUnloadedEvent(CoinStorage unit){
			this.device = unit;
		}


		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			this.device = device;
		}

		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			this.device = device;
		}

		@Override
		public void reactToCoinAddedEvent(CoinStorage unit) {
			// TODO Auto-generated method stub
			this.device = unit;
			
		}
	}

	public class CoinTrayTestObserver implements AbstractDeviceObserver, CoinTrayObserver {
		
		private AbstractDevice<? extends AbstractDeviceObserver> device;
		
		public CoinTrayTestObserver(){
			this.device = null;
		}
		
		/**
		 * Announces that a coin has been added to the indicated tray.
		 * 
		 * @param tray
		 *            The tray where the event occurred.
		 */
		public void reactToCoinAddedEvent(CoinTray tray){
			this.device = tray;
		}

		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			this.device = device;
		}

		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			this.device = device;
			
		}
	}
	
	public class CoinValidatorTestObserver implements AbstractDeviceObserver, CoinValidatorObserver {
		
		public void CoinValidatorObserverTest(){
			this.device = null;
		}
		
		private AbstractDevice<? extends AbstractDeviceObserver> device;
		/**
		 * An event announcing that the indicated coin has been detected and determined
		 * to be valid.
		 * 
		 * @param validator
		 *            The device on which the event occurred.
		 * @param value
		 *            The value of the coin.
		 */
		public void reactToValidCoinDetectedEvent(CoinValidator validator, BigDecimal value){
			this.device = validator;
		}

		/**
		 * An event announcing that a coin has been detected and determined to be
		 * invalid.
		 * 
		 * @param validator
		 *            The device on which the event occurred.
		 */
		public void reactToInvalidCoinDetectedEvent(CoinValidator validator){
			this.device = validator;
		}

		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			this.device = device;
			
		}

		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub
			this.device = device;
		}
	}


	    
	   

	
	

}


