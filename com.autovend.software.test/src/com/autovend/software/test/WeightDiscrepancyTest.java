package com.autovend.software.test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.external.ProductDatabases;
import com.autovend.software.SelfCheckoutMachineLogic;

public class WeightDiscrepancyTest {
	
	Currency currency = Currency.getInstance("CAD");
	int[] billDenominations = {5, 10, 20, 50, 100};
	BigDecimal[] coinDenominations = {BigDecimal.TEN};
	int maxScaleWeight = 50;
	int sensitivity = 1;
	
	SelfCheckoutStation station = new SelfCheckoutStation(currency, billDenominations, coinDenominations, maxScaleWeight, sensitivity);
	
	Numeral[] testNumeral = {Numeral.one, Numeral.two, Numeral.three};
    Barcode testBarcode = new Barcode(testNumeral);
    double testWeight = 10.00;
    String testDesc = "pencils";
    
    BigDecimal testPrice = BigDecimal.TWO;
    
    BarcodedUnit testUnit = new BarcodedUnit(testBarcode, testWeight);
    BarcodedProduct testProduct = new BarcodedProduct(testBarcode,testDesc, testPrice, testWeight);
    
    SelfCheckoutMachineLogic testLogic = new SelfCheckoutMachineLogic (station);
    
    

	 
    @Before
    public void setup() {
    	
    	  ProductDatabases.BARCODED_PRODUCT_DATABASE.put(testBarcode, testProduct);
    	  ProductDatabases.INVENTORY.put(testProduct, 10);
    }

	 @Test()
	 
	 public void run() {
		 
		 boolean scan1 = testLogic.addItemScan(testBarcode);
	 }
	 
	 //scan item, check expected weight
	 //plu lookup, check expected weight
	 //scan, put wrong weight, check print call
	 
	 
	 
}
