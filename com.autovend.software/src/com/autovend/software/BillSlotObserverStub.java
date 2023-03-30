/*
 *  @author: Angeline Tran (301369846),
 *  @author: Tyson Hartley (30117135), 
 *  @author: Jeongah Lee (30137463), 
 *  @author: Tyler Nguyen (30158563), 
 *  @author: Diane Doan (30052326), 
 *  @author: Nusyba Shifa (30162709)
 */

package com.autovend.software;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillSlot;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillSlotObserver;

public class BillSlotObserverStub implements BillSlotObserver{

	public BillSlot billSlot; 
	public SelfCheckoutMachineLogic scMachine;
	
	public boolean billInsertedEvent = false;
    public boolean billEjectedEvent = false;
    public boolean billRemovedEvent = false;
    public AbstractDevice<? extends AbstractDeviceObserver> device = null;
    
    public BillSlotObserverStub (SelfCheckoutMachineLogic scMachine) {
    	this.scMachine = scMachine;
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
	public void reactToBillInsertedEvent(BillSlot slot) {
		scMachine.billInsertedEvent = true;
		
	}

	@Override
	public void reactToBillEjectedEvent(BillSlot slot) {
		scMachine.billEjectedEvent = true;
		
	}

	@Override
	public void reactToBillRemovedEvent(BillSlot slot) {
		scMachine.billRemovedEvent = true;
		
	}
	
	public boolean getInsertedEvent() {
		return billInsertedEvent;
	}
	
	public void waitForBill() {
		this.billInsertedEvent = false;
	}

}
