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

package com.autovend.software.test;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.Numeral;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.SelfCheckoutMachineLogic;

public class WeightTest {
	Currency currency = Currency.getInstance("CAD");
	int[] billDenominations = {5, 10, 20, 50, 100};
	BigDecimal[] coinDenominations = {BigDecimal.TEN};
	int maxScaleWeight = 50;
	int sensitivity = 1;
	
	SelfCheckoutStation station = new SelfCheckoutStation(currency, billDenominations, coinDenominations, maxScaleWeight, sensitivity);
	
	Numeral[] testNumeral = {Numeral.one, Numeral.two, Numeral.three};
	Numeral[] testNumeral2 = {Numeral.one, Numeral.two};
	Numeral[] testNumeral3 = {Numeral.one, Numeral.two};
	
    Barcode testBarcode = new Barcode(testNumeral);
    Barcode testBarcode2 = new Barcode(testNumeral2);
    Barcode testBarcode3 = new Barcode(testNumeral3);
    
    double testWeight = 10.00;
    double testWeight2 = 25.00;
    
    String testDesc = "pencils";
    String testDesc2 = "notebooks";
    
    BigDecimal testPrice = BigDecimal.TWO;
    BigDecimal testPrice2 = BigDecimal.TEN;
    

    BarcodedProduct testProduct = new BarcodedProduct(testBarcode,testDesc, testPrice, testWeight);
    BarcodedProduct testProduct2 = new BarcodedProduct(testBarcode2,testDesc2, testPrice, testWeight2);
    
    SelfCheckoutMachineLogic testLogic = new SelfCheckoutMachineLogic (station);
    
    ElectronicScale scale = new ElectronicScale(50, sensitivity);
    
  
    
    

	 
    @Before
    public void setup() {
    	
    	  ProductDatabases.BARCODED_PRODUCT_DATABASE.put(testBarcode, testProduct);
    	  ProductDatabases.INVENTORY.put(testProduct, 10);
    	  
    	  ProductDatabases.BARCODED_PRODUCT_DATABASE.put(testBarcode2, testProduct2);
    	  ProductDatabases.INVENTORY.put(testProduct2, 0);
    	  
    	  station.setBaggingArea(scale);
    	  station.baggingArea.currentWeightInGrams = 10;
    	  
    }
    
    @Test
	 public void noDisc() throws OverloadException {
		 boolean scan1 = testLogic.addItemScan(testBarcode);
		 Assert.assertEquals(true, scan1);
		 testLogic.totalExpectedWeight = 10.00;
		 boolean actual = testLogic.weightDiscrepancy();
		 Assert.assertEquals(false, actual);
	 }
    
    @Test
	 public void Disc() throws OverloadException {
		 boolean scan1 = testLogic.addItemScan(testBarcode2);
		 testLogic.totalExpectedWeight = 30.00;
		 boolean actual = testLogic.weightDiscrepancy();
		 Assert.assertEquals(true, actual);
	 }
    
    

}
