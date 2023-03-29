

package com.autovend.software;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import com.autovend.Coin;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.CoinSlot;
import com.autovend.devices.CoinStorage;
import com.autovend.devices.CoinValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CoinDispenserObserver;
import com.autovend.devices.observers.CoinSlotObserver;
import com.autovend.devices.observers.CoinStorageObserver;
import com.autovend.devices.observers.CoinValidatorObserver;

public class PayWithCoin implements CoinSlotObserver, CoinValidatorObserver, CoinDispenserObserver, CoinStorageObserver, CoinTrayObserver{
	//private SelfCheckoutStation selfCheckoutStation = null
	
			// ArrayList keeping track of (the value of) the inserted coins
			ArrayList<Integer> coinsList = new ArrayList<Integer>();
			
			private SelfCheckoutStation selfCheckoutStation;
			private CoinSlot slot;
			private CoinValidator validator;
			private Coin coin;
			private CoinDispenser dispenser;
			private BigDecimal totalCoin; // create local variable total
			
			private CustomerIO customer;
			private AttendantIO attendant;
			private CashIO cash;
			ReceiptPrinter printer;
			
			// Array of the number of each coin denomination to dispense for change
			private int[] coinsChange = new int[5];
			private CoinDispenser[] dispensers = new CoinDispenser[5];
			
			
			// Total amount of cash inserted
			double coinCount = 0;
			
			double remainingAmount;
			
			boolean inputCoin = false;
			
//			
//			/*
//			 * Setter for total
//			 */
//			public void setTotalCoin(BigDecimal totalCoin) {
//				this.totalCoin = totalCoin;
//			}
//			
//
//			/*
//			 * Getter for total
//			 */
//			public BigDecimal getTotal(BigDecimal total) {
//				return this.totalCoin;
//			}

			public PayWithCoin(SelfCheckoutStation selfCheckoutStation, CustomerIO customer) throws DisabledException, OverloadException{
				
				 inputCoin = selfCheckoutStation.coinInput.accept(coin);
				 	
				 	customer = new CustomerIO();
				 	remainingAmount = this.customer.getAmount();
					
					attendant = new AttendantIO();
					cash = new CashIO();
					//selfCheckoutStation = sc;
				
			}
			
			public void coinValidate(SelfCheckoutStation selfCheckoutStation, CustomerIO customer) throws DisabledException{
				
				coin = new Coin(coin.getValue(), coin.getCurrency());
				// The value of each coin that gets accepted (is valid) will be added to coinsList
				remainingAmount = totalCoin;
				while(selfCheckoutStation.billValidator.accept(coin)) {
					coinsList.add(coin.getValue());
					for(Integer i : coinsList) {
						// updating the total amount
						coinCount = coinCount + i;
						
						// Reduce the remaining amount due by the value of the inserted cash
						remainingAmount = remainingAmount - coinCount;
						// signals to the customer I/O the updated amount due after the insertion of each coin.
						customer.setAmount(remainingAmount);

					}
					
				}
			}
	
	
			
			private boolean coinsLoadedEvent = false;
			private boolean coinInsertedEvent = false;
			
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				// TODO Auto-generated method stub
				
			}
			
			// Implements methods from CoinValidatorObserver
			@Override
			public void reactToValidCoinDetectedEvent(CoinValidator validator, BigDecimal value) {

			}

			@Override
			public void reactToInvalidCoinDetectedEvent(CoinValidator validator) {
			}
			
			// Implements methods from CoinDispenserObserver
			
			@Override
			public void reactToCoinsFullEvent(CoinDispenser dispenser) {
				
			}
			
			@Override
			public void reactToCoinsEmptyEvent(CoinDispenser dispenser) {
				
			}
			
			@Override
			public void reactToCoinAddedEvent(CoinDispenser dispenser, Coin coin) {
				
			}
			
			@Override
			public void reactToCoinRemovedEvent(CoinDispenser dispenser, Coin coin) {
				
			}
			
			@Override
			public void reactToCoinsLoadedEvent(CoinDispenser dispenser, Coin... coins) {
				
				coinsLoadedEvent = True;
				
			}
			
			@Override
			public void reactToCoinsUnloadedEvent(CoinDispenser dispenser, Coin... coins) {
				
				coinsLoadedEvent = False;
				
			}
			
			// Implements methods from CoinStorageObserver
			
			@Override
			public void reactToCoinsFullEvent(CoinStorage unit) {
				
			}
			
			@Override 
			public void reactToCoinAddedEvent(CoinStorage unit) {
				
			}
			
			@Override
			public void reactToCoinsLoadedEvent(CoinStorage unit) {
				
			}
			
			@Override 
			public void reactToCoinsUnloadedEvent(CoinStorage unit) {
				
			}
			
			// Implements methods from CoinSlotObserver
			@Override 
			public void reactToCoinInsertedEvent(CoinSlot slot) {
				coinInsertedEvent = true;
			}
			
			// Implements methods from CoinTrayObserver
			@Override
			public void reactToCoinAddedEvent(CoinTray tray) {
				
			}
		
			
			public boolean coinsLoadedEvent() {
				return this.coinsLoadedEvent;
			}
			
			
			
			
			
			
			
			
			
			
			
			
}