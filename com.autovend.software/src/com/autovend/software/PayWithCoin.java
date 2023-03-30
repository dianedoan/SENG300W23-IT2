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
import com.autovend.devices.CoinTray;
import com.autovend.devices.CoinValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CoinDispenserObserver;
import com.autovend.devices.observers.CoinSlotObserver;
import com.autovend.devices.observers.CoinStorageObserver;
import com.autovend.devices.observers.CoinTrayObserver;
import com.autovend.devices.observers.CoinValidatorObserver;

public class PayWithCoin implements CoinSlotObserver, CoinValidatorObserver, CoinDispenserObserver, CoinStorageObserver, CoinTrayObserver{
	//private SelfCheckoutStation selfCheckoutStation = null
	
			// ArrayList keeping track of (the value of) the inserted coins
			ArrayList<BigDecimal> coinsList = new ArrayList<BigDecimal>();
			
			private SelfCheckoutStation selfCheckoutStation;
			private CoinSlot slot;
			private CoinValidator validator;
			private Coin coin;
			private CoinDispenser dispenser;
			private BigDecimal totalCoin; // create local variable total
			
			private CustomerIO customer;
			private AttendantIO attendant;
			private CashIO cash;
			private ReceiptPrinter printer;
			private String message;
			
			// Array of the number of each coin denomination to dispense for change
			private int[] coinsChange = new int[5];
			private CoinDispenser[] dispensers = new CoinDispenser[5];
			
			//initialize coin dispensers:
			private CoinDispenser dispenser5;
			private CoinDispenser dispenser10;
			private CoinDispenser dispenser25;
			private CoinDispenser dispenser100;
			private CoinDispenser dispenser200;
			
			
			
			// Total amount of cash inserted
			int coinCount = 0;
			
			BigDecimal remainingAmount;
			
			boolean inputCoin = false;
			
			
			/*
			 * Setter for total
			 */
			public void setTotal(BigDecimal totalCoin) {
				this.totalCoin = totalCoin;
			}
			

			/*
			 * Getter for total
			 */
			public BigDecimal getTotal(BigDecimal total) {
				return this.totalCoin;
			}
			

			public PayWithCoin(SelfCheckoutStation selfCheckoutStation, CustomerIO customer) throws DisabledException, OverloadException{
				
				 inputCoin = selfCheckoutStation.coinSlot.accept(coin);
				 	
				 	customer = new CustomerIO();
				 	remainingAmount = this.customer.getAmount();
					
					attendant = new AttendantIO();
					cash = new CashIO();

					selfCheckoutStation.coinValidator.register(this);
					selfCheckoutStation.coinSlot.register(this);
					selfCheckoutStation.coinDispensers.get(5).register(this);
					selfCheckoutStation.coinDispensers.get(10).register(this);
					selfCheckoutStation.coinDispensers.get(25).register(this);
					selfCheckoutStation.coinDispensers.get(100).register(this);
					selfCheckoutStation.coinDispensers.get(200).register(this);
					
					// Initialize the five dispensers for each denomination being used in the machine
					this.dispenser5 = selfCheckoutStation.coinDispensers.get(5);
					this.dispenser10 = selfCheckoutStation.coinDispensers.get(10);
					this.dispenser25 = selfCheckoutStation.coinDispensers.get(25);
					this.dispenser100 = selfCheckoutStation.coinDispensers.get(100);
					this.dispenser200 = selfCheckoutStation.coinDispensers.get(200);
					
					dispensers[0] = this.dispenser5;
					dispensers[1] = this.dispenser10;
					dispensers[2] = this.dispenser25;
					dispensers[3] = this.dispenser100;
					dispensers[4] = this.dispenser200;
				
			}
			
			public void coinValidate(SelfCheckoutStation selfCheckoutStation, CustomerIO customer) throws DisabledException{
				
				coin = new Coin(coin.getValue(), coin.getCurrency());
				// The value of each coin that gets accepted (is valid) will be added to coinsList
				remainingAmount = totalCoin;
				while(selfCheckoutStation.coinValidator.accept(coin)) {
					coinsList.add(coin.getValue());
					for(BigDecimal i : coinsList) {
						// updating the total amount
						coinCount = coinCount+ i.intValue();
						
						// Reduce the remaining amount due by the value of the inserted cash
						
						remainingAmount = remainingAmount.subtract(BigDecimal.valueOf(coinCount));
						// signals to the customer I/O the updated amount due after the insertion of each coin.
						customer.setAmount(remainingAmount);

					}
					
				}
			}
			
			public boolean changeHandle() throws DisabledException, EmptyException, OverloadException {
				
				// check if the remaining amount is greater than zero
				if(remainingAmount.intValue() > 0) {
					// Return false as coin not fully paid and thus no change given
					return false;
				}
				
				// If the remaining amount is less than 0 the change has to be returned to the customer
				else if (remainingAmount.intValue() < 0) {
					// signal to CASHIO the amount of change due
					cash.getChange();
					this.cash.setChange(remainingAmount.negate());
//					this.cash.setChange(Double.toString(-remainingAmount));
					this.customer.setAmount(BigDecimal.ZERO);
					
					if (checkChange()) {
					//dispense the change to the customer
					CoinDispenser emitter;
					for (int i = 0; i < coinsChange.length; i++) {
						for (int j = 0; j < coinsChange[i]; j++) {
							emitter = dispensers[i];
							emitter.emit();
			
						}
					}
					// Return true as there was sufficient change and it was dispensed
					return true;
					}
					else {
						//if insufficient change signal attendant and suspendSystem
						//attendant.informAttendant();
						
						suspendSystem();
						// Return false as there was not sufficient change to dispense
						return false;
					}
				}
				else {
					
					// see PrintReceipt
					// Return false if no change given
					return false;
				}	
			}
	
			/**
			 * When the attendant is called the system has to be blocked from any further transactions
			 */
			public void suspendSystem() {
				selfCheckoutStation.mainScanner.disable();
				selfCheckoutStation.handheldScanner.disable();
				selfCheckoutStation.scale.disable();
				selfCheckoutStation.printer.disable();
				selfCheckoutStation.billInput.disable();
				selfCheckoutStation.billOutput.disable();
				selfCheckoutStation.billStorage.disable();
				selfCheckoutStation.billValidator.disable();
			}
			
			public boolean checkChange() {
				
				// Get the amount of change, converted to integer as coins not yet supported
				int changeDue = this.cash.getChange().intValue();
				
				// Get the number of coins in each dispenser
				int size5 = dispenser5.size();
				int size10 = dispenser10.size();
				int size25 = dispenser25.size();
				int size100 = dispenser100.size();
				int size200 = dispenser200.size();
				
				// Check if all are empty, and if so notify listeners
				if ((size5 + size10 + size25 + size100 + size200) == 0) {
					this.reactToCoinsEmptyEvent(dispenser5);
					this.reactToCoinsEmptyEvent(dispenser10);
					this.reactToCoinsEmptyEvent(dispenser25);
					this.reactToCoinsEmptyEvent(dispenser100);
					this.reactToCoinsEmptyEvent(dispenser200);
				}
				
				// Calculate number of each coin denomination in decreasing order that can be given
				int change200 = Math.min((changeDue / 200),size200);
				changeDue -= (change200 * 200);
				int change100 = Math.min((changeDue / 100),size100);
				changeDue -= (change100 * 100);
				int change25 = Math.min((changeDue / 25),size25);
				changeDue -= (change25 * 25);
				int change10 = Math.min((changeDue / 10),size10);
				changeDue -= (change10 * 10);
				int change5 = Math.min((changeDue / 5),size5);
				changeDue -= (change5 * 5);
				
				// Check if change due at the end equals zero
				if (changeDue == 0) {
					// If so, update the array for number of each bill to dispense and return true
					coinsChange[0] = change5;
					coinsChange[1] = change10;
					coinsChange[2] = change25;
					coinsChange[3] = change100;
					coinsChange[4] = change200;
					return true;
				}
				// If not, there are not enough bills or change cannot be made with just bills, return false
				else {
					return false;
				}
				
			}
			
			/**
			 * Getter to retrieve information for testing on which bills dispensed as change
			 * @return billsChange array of number of each bill given as change
			 */
			public int[]  getCoinsChange() {
				return coinsChange;
			}
			
			private boolean coinsLoadedEvent = false;
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
				
				coinsLoadedEvent = true;
				
			}
			
			@Override
			public void reactToCoinsUnloadedEvent(CoinDispenser dispenser, Coin... coins) {
				
				coinsLoadedEvent = false;
				
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
			}
			
			// Implements methods from CoinTrayObserver
			@Override
			public void reactToCoinAddedEvent(CoinTray tray) {
				
			}
		
			
			public boolean coinsLoadedEvent() {
				return this.coinsLoadedEvent;
			}


		
			
			
			
			
			
			
			
			
			
			
			
}