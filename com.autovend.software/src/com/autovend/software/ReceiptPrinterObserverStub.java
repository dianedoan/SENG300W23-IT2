/**
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */
package com.autovend.software;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;

/*
 * Stub class that implements the ReceiptPrinterObserver
 */
public class ReceiptPrinterObserverStub implements ReceiptPrinterObserver{
	
	private boolean outOfPaper = false;
	private boolean outOfInk = false;
	private AttendantIO attendant;
	
	public ReceiptPrinterObserverStub (AttendantIO attendant) {
		this.attendant = attendant;
	}
	
	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		
	}

	@Override
	public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
		
		attendant.informAttendant("Paper out in printer. Duplicate receipt must be printed and station needs maintenance.");
		outOfPaper = true;
		
	}

	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		
		attendant.informAttendant("Ink out in printer. Duplicate receipt must be printed and station needs maintenance.");
		outOfInk = true;
	}

	@Override
	public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		
		
	}

	@Override
	public void reactToInkAddedEvent(ReceiptPrinter printer) {
		
		
	}
	
	/**
	 * Return value of outOfPaper boolean.
	 * 
	 * @return: Boolean of if the printer is out of paper or not.
	 */
	public boolean getOutOfPaper() {
		return outOfPaper;
	}
	
	/**
	 * Return value of outOfInk boolean.
	 * 
	 * @return: Boolean of if the printer is out of ink or not.
	 */
	public boolean getOutOfInk() {
		return outOfInk;
	}

}
