package com.autovend.software;

import java.util.Currency;

import com.autovend.Bill;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillValidator;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillValidatorObserver;

/*
 * Stub class to get observer methods
 */
public class BillValidatorObserverStub implements BillValidatorObserver{

	public BillValidator billValidator;

	public boolean billValidEvent = false;
	public boolean billInvalidEvent = false;
	public AbstractDevice<? extends AbstractDeviceObserver> device = null;
	public SelfCheckoutMachineLogic	scMachine;
	
	public BillValidatorObserverStub (SelfCheckoutMachineLogic scm) {
		this.scMachine = scm;
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.device = device;

	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.device = device;

	}

	@Override
	public void reactToValidBillDetectedEvent(BillValidator billValidator, Currency currency, int value) {
		this.billValidator = billValidator;
		scMachine.billValidEvent = true;
		scMachine.bill = new Bill(value, currency);
	}

	@Override
	public void reactToInvalidBillDetectedEvent(BillValidator billValidator){
		this.billValidator = billValidator;
		scMachine.illInvalidEvent = true;
	}
}
