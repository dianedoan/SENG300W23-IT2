package src.com.autovend.software;

import com.autovend.devices.ReceiptPrinter;

public interface LowInkPaperReceiptPrinterObserverStub {
	/**
	 * Announces that the indicated printer is low on paper.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	void reactToLowPaperEvent(ReceiptPrinter printer);

	/**
	 * Announces that the indicated printer is low on ink.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	void reactToLowInkEvent(ReceiptPrinter printer);

}
