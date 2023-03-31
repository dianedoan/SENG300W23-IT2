package src.com.autovend.software;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;

/*
 * Stub class that implements the ReceiptPrinterObserver
 */
public class ReceiptPrinterObserverStub implements ReceiptPrinterObserver, LowInkPaperReceiptPrinterObserverStub {
	
	private boolean outOfPaper = false;
	private boolean outOfInk = false;
	private boolean lowOnPaper = false;
	private boolean lowOnInk = false;
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

	// If low paper event occurs, notifies attendant
	@Override
	public void reactToLowPaperEvent(ReceiptPrinter printer) {
		attendant.informAttendant("Paper low in printer. Replace printer paper soon.");
		lowOnPaper = true;
	}
	
	
	// If low ink event occurs, notifies attendant
	@Override
	public void reactToLowInkEvent(ReceiptPrinter printer) {
		attendant.informAttendant("Ink low in printer. Replace printer ink soon.");
		lowOnInk = true;
		
	}
	
	/**
	 * Return value of outOfPaper boolean.
	 * 
	 * @return: Boolean of if the printer is out of paper or not.
	 */
	public boolean getLowPaper() {
		return lowOnPaper;
	}
	
	/**
	 * Return value of outOfInk boolean.
	 * 
	 * @return: Boolean of if the printer is out of ink or not.
	 */
	public boolean getLowInk() {
		return lowOnInk;
	}

}
