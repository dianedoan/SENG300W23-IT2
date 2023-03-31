package com.autovend.software.test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.external.ProductDatabases;
import com.autovend.software.SelfCheckoutMachineLogic;



public class AddItemTest {
	
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
    
    

	 
    @Before
    public void setup() {
    	
    	  ProductDatabases.BARCODED_PRODUCT_DATABASE.put(testBarcode, testProduct);
    	  ProductDatabases.INVENTORY.put(testProduct, 10);
    	  
    	  ProductDatabases.BARCODED_PRODUCT_DATABASE.put(testBarcode2, testProduct2);
    	  ProductDatabases.INVENTORY.put(testProduct2, 0);
    }
    

	 @Test(expected = NullPointerException.class)
	 
	 public void BarcodeIsNull() throws OverloadException {	 
	 boolean scan1 = testLogic.addItemScan(null); 
	 }
	 
	 @Test
	 public void BarcodeNotNull() {
		 boolean scan2 = testLogic.addItemScan(testBarcode);
		 Assert.assertEquals(true, scan2);
	 }
	 
	 @Test()
	 public void ItemUnavailable() {
		 boolean scan3 = testLogic.addItemScan(testBarcode2);
		 Assert.assertEquals(false, scan3);
		 
}
	 @Test()
	 public void ItemNotInDataBase() {
		 boolean scan4 = testLogic.addItemScan(testBarcode3);
		 Assert.assertEquals(false, scan4);
	 }
	 }

