/*
 *  @author: Angeline Tran (301369846),
 *  @author: Tyson Hartley (30117135), 
 *  @author: Jeongah Lee (30137463), 
 *  @author: Tyler Nguyen (30158563), 
 *  @author: Diane Doan (30052326), 
 *  @author: Nusyba Shifa (30162709)
 */

/*
 * Observer Stub for BarcodeScannerObserver
 */
package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

public class BarcodeScannerObserverStub implements BarcodeScannerObserver{
	
	 public AbstractDevice<? extends AbstractDeviceObserver> device = null;
	 public boolean barcodeScaned;
	 public SelfCheckoutMachineLogic scLogic;
	 
	 public BarcodeScannerObserverStub(SelfCheckoutMachineLogic controllingLogic) {
		 scLogic = controllingLogic;
		 
	 }

	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.device = device;
		
	}



	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.device = device;
		
	}

	@Override
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
		BarcodedProduct bProduct = SelfCheckoutMachineLogic.getBarcodedProductFromBarcode(barcode);
		if(bProduct != null) {
			scLogic.addItemPerUnit(bProduct, bProduct.getExpectedWeight());
		}
		
		
	}







}
