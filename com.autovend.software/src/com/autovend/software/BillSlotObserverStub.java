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
