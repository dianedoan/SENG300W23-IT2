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

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.software.*;
import com.autovend.devices.SelfCheckoutStation;


public class lowInkAndPaperTest {

    private SelfCheckoutStation scs;
    private SelfCheckoutMachineLogic machineLogic;
    private AttendantIO attendant = new AttendantIO();
    private ReceiptPrinterObserverStub observerStub;

    @Before
    public void setUp() {
        Currency currency = Currency.getInstance("CAD");
        int[] billDenominations = {5, 10, 20};
        BigDecimal[] coinDenominations = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10), BigDecimal.valueOf(0.25)};
        scs = new SelfCheckoutStation(currency, billDenominations, coinDenominations, 10000, 5);
        machineLogic = new SelfCheckoutMachineLogic(scs);
        PrintReceipt lowInkPaper = new PrintReceipt(scs, machineLogic, attendant);

    }
    @After
    public void tearDown(){
        scs = null;
        machineLogic = null;
    }



    @Test
    public void testLowInkWithinThreshold(){
        PrintReceipt lowInk = new PrintReceipt(scs, machineLogic, attendant);
        lowInk.setMaxInk(10);
        boolean expect = false;
        assertEquals(expect,lowInk.isLowInk());
    }
    @Test
    public void testLowInkOverThreshold(){
        PrintReceipt lowInk = new PrintReceipt(scs, machineLogic, attendant);
        lowInk.setMaxInk(1048500);
        boolean expect = true;
        assertEquals(expect,lowInk.isLowInk());
    }

    @Test
    public void testLowPaperWithinThreshold(){
        PrintReceipt lowPaper = new PrintReceipt(scs, machineLogic, attendant);
        lowPaper.setMaxPaper(10);
        boolean expect = false;
        assertEquals(expect,lowPaper.isLowPaper());
    }
    @Test
    public void testLowPaperOverThreshold(){
        PrintReceipt lowPaper = new PrintReceipt(scs, machineLogic, attendant);
        lowPaper.setMaxPaper(1023);
        boolean expect = true;
        assertEquals(expect,lowPaper.isLowPaper());
    }




}
