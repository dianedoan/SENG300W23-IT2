package com.autovend.software;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.CoinSlot;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CoinSlotObserver;

public class CoinSlotObserverStub implements CoinSlotObserver{

	public CoinSlot coinSlot; 
	public SelfCheckoutMachineLogic scMachine;

}
