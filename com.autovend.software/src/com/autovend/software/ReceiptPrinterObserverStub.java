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
