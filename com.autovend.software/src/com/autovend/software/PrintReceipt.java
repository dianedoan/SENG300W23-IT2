package src.com.autovend.software;

import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.devices.ReceiptPrinter;


/*
 * Controls the logic of the receipt printer.
 */
public class PrintReceipt extends ReceiptPrinter {
	
	private SelfCheckoutStation scs;
	private SelfCheckoutMachineLogic machineLogic;
	private ReceiptPrinterObserverStub observerStub;

	int maxInk = ReceiptPrinter.MAXIMUM_INK;
	int maxPaper = ReceiptPrinter.MAXIMUM_PAPER;
	
	int inkThreshold = (int) (0.1 * ReceiptPrinter.MAXIMUM_INK);
	int paperThreshold = (int) (0.1 * ReceiptPrinter.MAXIMUM_PAPER);
	
	
	public PrintReceipt(SelfCheckoutStation selfcheckoutstation, SelfCheckoutMachineLogic machineLogic, AttendantIO attendant) {
		this.scs = selfcheckoutstation;
		this.machineLogic = machineLogic;
		observerStub = new ReceiptPrinterObserverStub(attendant);
		scs.printer.register(observerStub);
				
	}
	
	
	/**
	 * Prints the products, their price, and payment details of the bill record. 
	 * 
	 * @param billRecord: The bill record to print a receipt for.
	 * @throws OverloadException: If the extra character would spill off the end of the line.
	 * @throws EmptyException: If there is insufficient paper or ink to print the character.
	 */
	public void printBillRecord(TransactionReceipt billRecord) throws OverloadException, EmptyException{
		
		//Print each item on the bill and its price
		for(int i = 0; i < billRecord.getBillLength(); i++) {
			BarcodedProduct productToPrint = (BarcodedProduct) billRecord.getProductAt(i); //Get one product at a time from the bill record
			char[] productDescription = productToPrint.getDescription().toCharArray(); //Get the product description and store it in a char array
			
			//Print each char in the product description
			for (int j = 0; j < productDescription.length; j++) {
				scs.printer.print(productDescription[j]);
				if (observerStub.getOutOfInk()) { //If the printer is out of ink after printing a char, abort printing the receipt
					machineLogic.setMachineLock(true);
					machineLogic.setReasonForLock(2);
					return;
				}
				isLowInk();		//checks if the printer is low on ink
				
			}
			
			scs.printer.print(' '); //space
			
			scs.printer.print('$');//$ to go before printing the price
			isLowInk(); //checks if the printer is low on ink
			
			if (observerStub.getOutOfInk()) { //If the printer is out of ink after printing a char, abort printing the receipt
				machineLogic.setMachineLock(true);
				machineLogic.setReasonForLock(2);
				return;
			}
			
			char[] productPrice = productToPrint.getPrice().toString().toCharArray();
			//Print each number in the price
			for (int p = 0; p < productPrice.length; p++) {
				scs.printer.print(productPrice[p]);
				if (observerStub.getOutOfInk()) { //If the printer is out of ink after printing a char, abort printing the receipt
					machineLogic.setMachineLock(true);
					machineLogic.setReasonForLock(2);
					return;
				}
				
				isLowInk(); //checks if the printer is low on ink
			}
			
			scs.printer.print('\n');//Newline before printing the next product
			isLowPaper(); //checks if the printer is low on paper
			if (observerStub.getOutOfPaper()) { //If the printer is out of paper after a new line, abort printing
				machineLogic.setMachineLock(true);
				machineLogic.setReasonForLock(2);
				return;
			}
			
		}
		
		//Print details of the payment
		String totalTextString = "TOTAL:$";
		char[] totalText = totalTextString.toCharArray();
		for (int u = 0; u < totalText.length; u++) {
			scs.printer.print(totalText[u]);
			if (observerStub.getOutOfInk()) { //If the printer is out of ink after printing a char, abort printing the receipt
				machineLogic.setMachineLock(true);
				machineLogic.setReasonForLock(2);
				return;
			}
			isLowInk(); //checks if the printer is low on ink
		}
		
		char[] totalPrice = machineLogic.total.toString().toCharArray();
		for (int k = 0; k < totalPrice.length; k++) {
			scs.printer.print(totalPrice[k]);
			if (observerStub.getOutOfInk()) { //If the printer is out of ink after printing a char, abort printing the receipt
				machineLogic.setMachineLock(true);
				machineLogic.setReasonForLock(2);
				return;
			}
		}
		isLowInk(); //checks if the printer is low on ink
		
	}
	
	/**
	 * Cuts the receipt. Simulate the customer taking their receipt.
	 * 
	 */
	public String takeReceipt(){
		scs.printer.cutPaper(); //Cut the receipt from the receipt printer
		String receipt = scs.printer.removeReceipt(); //Simulate a customer removing and taking their receipt
		return receipt;
	}
	
	//This method cheeks if the paper is low using a threshold value (10 percent)
	public boolean isLowPaper() {
	    int currentPaper = ReceiptPrinter.MAXIMUM_PAPER - maxPaper;
	    boolean isLowPaper = currentPaper <= paperThreshold;
	    if (isLowPaper) {
	        observerStub.reactToLowPaperEvent(this);
	    }
	   
        maxPaper--; //decrements max paper each time it is checked
	    
	    return isLowPaper;
	}
	
	//This method cheeks if the paper is low using a threshold value (10 percent)
	public boolean isLowInk() {
	    int currentInk = ReceiptPrinter.MAXIMUM_INK - maxInk;
	    boolean isLowInk = currentInk <= inkThreshold;
	    if (isLowInk) {
	        observerStub.reactToLowInkEvent(this);
	    }
	    
	    maxInk--;   //decrements max ink each time it is checked
	    
	    return isLowInk;
	}
	
	/**
	 * Getter for the receipt printer observer stub
	 * @return: observerStub
	 */
	public ReceiptPrinterObserverStub getObserverStub() {
		return observerStub;
	}
	

}
