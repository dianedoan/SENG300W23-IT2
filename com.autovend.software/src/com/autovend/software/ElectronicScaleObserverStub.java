package com.autovend.software;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;

public class ElectronicScaleObserverStub implements  ElectronicScaleObserver{
	 public SelfCheckoutMachineLogic scLogic;
	 AbstractDevice<? extends AbstractDeviceObserver> device; 
	 
	 public ElectronicScaleObserverStub(SelfCheckoutMachineLogic callingLogic) {
		 scLogic = callingLogic;
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
	public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
		scLogic.weightChanged(weightInGrams);
		
		
	}


	@Override
	public void reactToOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reactToOutOfOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}




}
