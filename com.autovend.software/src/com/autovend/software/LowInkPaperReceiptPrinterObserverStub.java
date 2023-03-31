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
