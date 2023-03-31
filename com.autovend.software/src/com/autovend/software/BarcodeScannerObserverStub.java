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
