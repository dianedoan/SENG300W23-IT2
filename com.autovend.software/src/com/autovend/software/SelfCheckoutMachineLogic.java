package com.autovend.software;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Scanner;
import java.util.ArrayList;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.external.CardIssuer;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.AttendantIO;
import com.autovend.software.BarcodeScannerObserverStub;
import com.autovend.software.BillSlotObserverStub;
import com.autovend.software.BillValidatorObserverStub;
import com.autovend.software.CashIO;
import com.autovend.software.CustomerDisplayIO;
import com.autovend.software.CustomerIO;
import com.autovend.software.ElectronicScaleObserverStub;
import com.autovend.software.PrintReceipt;
import com.autovend.software.TransactionReceipt;

public class SelfCheckoutMachineLogic{
	
	private Product bag;
	public BillSlot billSlot; // create bill slot
	public BillDispenser dispenser; // create bill dispenser
	public Bill bill; // create a bill
	public Barcode barcode; // create a barcode
	public BarcodedProduct item; // create a barcoded product
	public BigDecimal price; // create local variable price
	public BigDecimal total; // create local variable total
	public BigDecimal remainder; // create local variable remainder
	public BigDecimal change; // create local variable change
	public BigDecimal totalExpectedWeight;
	public boolean billInsertedEvent = false;
	public boolean billValidEvent = false;
	
	public TransactionReceipt currentBill;
	public boolean machineLocked = false;

	// variables for pay with card
	public String cardType;
	public CardIssuer bank;
	public Card.CardData cardData;
	public boolean cardReadEvent;
	public int attempt;
	
	public ElectronicScaleObserverStub esObserver = new ElectronicScaleObserverStub(this);
	public BarcodeScannerObserverStub bsObserver = new BarcodeScannerObserverStub(this);
	public BillSlotObserverStub listener_1 = new BillSlotObserverStub(this);
	public BillValidatorObserverStub listener_2 = new BillValidatorObserverStub(this);

	public CardReaderObserverStub card_listener = new CardReaderObserverStub(this);
	
	public PrintReceipt printReceipt; //This is the controller for printing the receipt
	public AttendantIO attendant = new AttendantIO(); //Creating an attendantIO that will receive and store calls to attendant
	public CustomerDisplayIO customerDisplay = new CustomerDisplayIO(); //Creating a display where messages to customers can go
	public CustomerIO customerIO = new CustomerIO(); // create customer i/o
	public CashIO cashIO = new CashIO(); // create cash i/o
	
	public String message = "";
	public String response = "Y";

	/**Codes for reasons the Machine is Locked
	 * -1: No Reason
	 * 0: Not Locked:
	 * 1: Locked until A change in scale weight
	 * 2: Locked until printer out of paper and/or ink is handled
	 * ...
	 * Please add any lock codes used, and why the machine is locked
	 */
	private int reasonForLock;

	private int[] listOfLockCodes;
	private int numberOfLockCodes = 3;
	public boolean billEjectedEvent;
	public boolean billRemovedEvent;
	public boolean illInvalidEvent;
	private SelfCheckoutStation station;

	private ArrayList<BarcodedUnit> scannedItems = new ArrayList<BarcodedUnit>();
	private ArrayList<PriceLookUpCodedUnit> pluItems = new ArrayList<PriceLookUpCodedUnit>();
	public int getReasonForLock() {
		return reasonForLock;
	}


	/**
	 * Set the reason the machine is being locked
	 * @param reasonForLock: Must be a value stored in listOfLockCodes 
	 * @return True: If reason was updated, false otherwise
	 */
	public boolean setReasonForLock(int reasonForLock) {
		for(int i = 0; i < numberOfLockCodes; i++) {
			if(reasonForLock == listOfLockCodes[i]) {
				this.reasonForLock = reasonForLock;
				return true;
			}
		}
		return false;
		
	}

	
	/**
	 * Constructor for Adding observers to pieces of hardware
	 */
	public SelfCheckoutMachineLogic(SelfCheckoutStation scStation) {
		
		this.station = scStation;
		listOfLockCodes = new int[numberOfLockCodes];
		for(int i = 0; i < this.numberOfLockCodes; i++) {
			listOfLockCodes[i] = i-1;
		}
		
		scStation.baggingArea.register(esObserver);
		scStation.baggingArea.disable();
		scStation.baggingArea.enable();
		
		scStation.handheldScanner.register(bsObserver);
		scStation.handheldScanner.disable();
		scStation.handheldScanner.enable();
		
		scStation.billInput.register(listener_1);

		scStation.cardReader.register(card_listener);
		scStation.cardReader.enable();
		
		printReceipt = new PrintReceipt(scStation, this, attendant);
		
		this.total = new BigDecimal(-1);
		
		bank = new CardIssuer("RBC");
		attempt = 1;
		
		this.setMachineLock(false);
	}

	
	/**
	 * 
	 * 	Adds an item that is sold by unit to the currentBill, updating the weight and total balance
	 * @param p: The product being added
	 * @param weight: the weight of p in grams
	 */
	public void addItemPerUnit(Product p, double weight) {
		
		if(!machineLocked) {
		
		//Assuming it is available		
		if(currentBill == null) {
			currentBill = new TransactionReceipt(p);
		} else {
			currentBill.addProduct(p);
		}
		
		currentBill.augmentBillBalance(p.getPrice());
		
		// Update Expected Weight
		currentBill.augmentExpectedWeight(weight);
		
 		this.askCustomerToPlaceItemGUI();
		
		
		}
	}
	
	


	public static BarcodedUnit getBarcodedUnitFromBarcode(Barcode barcode) {
		BarcodedProduct foundProduct = getBarcodedProductFromBarcode(barcode);
		
		BarcodedUnit bUnit = new BarcodedUnit(barcode, foundProduct.getExpectedWeight());
		
	
		return bUnit;
}



	/**
	 * 	Takes a barcode and returns a barcoded product if it is a valid barcode
	 * Otherwise returns null
	 * @param barcode: The barcode of the Barcoded Product being looked for
	 * @return If the barcode corossponds to one in the database, return that product otherwise return null
	 */
	public static BarcodedProduct getBarcodedProductFromBarcode(Barcode barcode) {
		BarcodedProduct foundProduct = null;
		
		if(ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode)){
			foundProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
				};
		
		return foundProduct;
	}
	
	/**
	 * Takes a plu code and returns the product registered with that code in the database; used in later methods
	 * @param plu
	 * @return
	 */
	public static PLUCodedProduct getPLUProductFromPlu(PriceLookUpCode plu) {
		PLUCodedProduct foundProduct = null;

		if(ProductDatabases.PLU_PRODUCT_DATABASE.containsKey(plu)) {
			foundProduct = ProductDatabases.PLU_PRODUCT_DATABASE.get(plu);
		};
		return foundProduct;
	}

	/**
	 * Takes a plu code, finds the associated product from the database, and creates an associated item object
	 * @param plu
	 * @param weight
	 * @return
	 */
	public static PriceLookUpCodedUnit getPLUUnitFromPLU(PriceLookUpCode plu, Double weight) {
		PLUCodedProduct foundProduct = getPLUProductFromPlu(plu);

		PriceLookUpCodedUnit pluUnit = new PriceLookUpCodedUnit(plu, weight);

		return pluUnit;
	}

	/**
	 * A getter for the total number of a product is available in stock
	 * @param product
	 * @return
	 */
	public static Integer getProductInventory(Product product) {
		return ProductDatabases.INVENTORY.get(product);
	}

	/**
	 * A method that deducts the amount of a product in inventory by one after it has been purchased by the customer
	 * @param product
	 * @param amount
	 */
	public static void deductInventory (Product product, int amount) {
		Integer current = ProductDatabases.INVENTORY.get(product);
		ProductDatabases.INVENTORY.put(product, current-amount);
	}

	/**
	 * Tells the machine to wait until the customer chnages the weight of the scale
	 */
	private void askCustomerToPlaceItemGUI() {
		
		//Prompt GUI to tell customer
		
		this.setMachineLock(true);
		this.setReasonForLock(1);
		
	}
	
	/**
	 * Sets the machines lock state to newState. If the machine is unlocked set reason for lock to 0
	 * 
	 * @param newState
	 */
	public void setMachineLock(boolean newState) {
		
		if(newState == false) {
			this.setReasonForLock(0);
		}
		this.machineLocked = newState;
	}
	
	
	/**
	 * 
	 * @return Returns a reference to the current bill the machine is processing
	 */
	public  TransactionReceipt getCurrentBill() {
		return currentBill;
	}

	/**
	 * This function is called whenever there is a weightChanged detected by the scale.
	 * If the machine is currently being locked becuase it expects a change it weight
	 * It checks if it is what the billExpects the weight to be otherwise it will lock the machine
	 * @param totalWeightInGrams: the total weight of the scale in grams.
	 */
	public void weightChanged(double totalWeightInGrams) {
		switch(this.reasonForLock) {
		
		case 1: if(totalWeightInGrams == this.currentBill.billExpectedWeight) {
			this.setMachineLock(false);
			this.setReasonForLock(0);
			
		}
			break;
		default: this.setMachineLock(true);
				this.setReasonForLock(1);
				break;
				
		}
		
	}

	/*
	 * Setter for total
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	

	/*
	 * Getter for total
	 */
	public BigDecimal getTotal(BigDecimal total) {
		return this.total;
	}
	
	/*
	 * Getter for change
	 */
	public BigDecimal getChange(BigDecimal change) {
		return this.change;
	}

	
	/*
	 * Main function for pay with cash
	 */
	public void payWithCash(){
    	remainder = total; // initialize remaining amount to pay
    	int compare = remainder.compareTo(BigDecimal.ZERO); // local variable to store comparison
    	while(compare == 1) { // comparison returns 1 if remainder > 0
    		if(billInsertedEvent) { // if event is true, continue with procedure
    			if(billValidEvent){
    				int insertedBill = bill.getValue(); // get value of the inserted bill
        		    BigDecimal updateBill = BigDecimal.valueOf(insertedBill); // convert bill to BigDecimal type
        		    remainder = total.subtract(updateBill); // reduces the remaining amount due by value of inserted bill
        		    customerIO.setAmount(remainder); // update Customer IO with amount
    			} else {
    				// Prompt again for another bill because last one was invalid
    				return;
    			}

    		}
    		else {
    			// Prompt for bill because no bill was inserted
    			return;
    		}
    		// Reset events at end of the loop
			billValidEvent = false;
			billInsertedEvent = false;
    	}
    	
    	change = remainder.abs(); // set the change to be an absolute value
    	cashIO.setChange(change); //set change using cashI/O
    	
    	// Change will then be distributed by BillStorage
	}
	
	/**
	 * Prints the receipt when signal is received that the customer has paid and the bill record is 
	 * updated with the payment details.
	 * 
	 * @param billRecord: Current bill that is printed
	 * @throws OverloadException: If the extra character would spill off the end of the line.
	 * @throws EmptyException: If there is insufficient paper or ink to print the character or
	 * 			the receipt has not been cut so unable for the customer to take it.
	 */
	public void signalToPrintReceipt(TransactionReceipt billRecord) throws OverloadException, EmptyException{
		//Check that the payment in full has been received 
		//Check that the bill record is updated with he details of payment
		
		printReceipt.printBillRecord(billRecord);
		//If the printer ran out of paper and/or ink while printing the receipt, printBillRecord() will return instead of continuing to print the receipt
		//Check if printer ran out of paper and/or ink while printing the receipt
		if(printReceipt.getObserverStub().getOutOfPaper() || printReceipt.getObserverStub().getOutOfInk()) {
			return;
		}
		
		printReceipt.takeReceipt();
		customerDisplay.informCustomer("Your session is complete. Thank you for shopping with us.");
		this.currentBill = null; //Null the current bill since the customer's session is over
		
	}
	
	/** 
	 * Use Case: Add Own Bags
	 * Allows the customer to add their own bags to the bagging area without causing a weight discrepancy
	 * 
	 * @throws OverloadException: If the extra character would spill off the end of the line.
	 */
		
	public void setResponse(String res) {
		response = res;
	}

	// Initialize message
	public void addOwnBags() throws OverloadException {
		boolean selfCheckOutBlocked = false;
		if (!selfCheckOutBlocked) {
			// 1. Customer I/O: Signals that the customer wants to add their own bags.
			//System.out.println("Please add your own bags.");
			message = "Please add your own bags.";
			
			// 2. System: Indicates that the customer should add their own bags now.
			Scanner input = new Scanner(System.in);
			//System.out.println("Have you added the bag(s)? (Y/N)");
			message = "Have you added the bag(s)? (Y/N)";
//			String response = input.nextLine();
			
			// 3. Customer I/O: Signals that the customer has finished adding their own bags.
			if (response.equalsIgnoreCase("Y")) {
				// 4. Bagging Area: Signals to the System the weight change.
				ElectronicScale electronicScale = new ElectronicScale(100, 1);
				double weightChange = electronicScale.getCurrentWeight();
				
				if (weightChange > 0.0) {
					// 5. System: Blocks the self-checkout station from further customer actions.
					selfCheckOutBlocked = true;
					setMachineLock(selfCheckOutBlocked);
					
					// 6. System: Signals to the Attendant I/O the need to approve the added bags.
					AttendantIO callAttendant = new AttendantIO();
					callAttendant.informAttendant("Need approval for adding own bags");
					//System.out.println("Waiting for attendant approval");
					message = "Waiting for attendant approval";
					
					// 7. Attendant I/O: Signals approval of the added bags
					Scanner attendantInput = new Scanner(System.in);
					//System.out.println("Approve customer bag? (Y/N)");
					message = "Approve customer bag? (Y/N)";
					String attendantResponse = input.nextLine();
					if (response.equalsIgnoreCase("Y")) {
						// 8. System: Unblocks the self-checkout station.
						selfCheckOutBlocked = false;
						setMachineLock(selfCheckOutBlocked);
						// 9. System: Signals to the Customer I/O that the customer may now continue.
						//System.out.println("You may continue with your checkout");
						message = "You may continue with your checkout";
					}
					// Exception: The attendant does not want to approve the added bags
					else if(response.equalsIgnoreCase("N")) {
						//System.out.println("Attendant did not approve the added bags. Please remove the items.");
						message = "Attendant did not approve the added bags. Please remove the items.";
					}
				}
				// Exception: The System is not ready to note weight discrepancies
				else {
					//System.out.println("Error: Weight not within acceptable range");
					message = "Error: Weight not within acceptable range";
				}
			}
		} else {
			//System.out.println("Error: Weight not within acceptable range");
			message = "Approve customer bag? (Y/N)";
		}
	}
	
	public void Purchase_bags(int number_bags) {
		customerDisplay.informCustomer("You want to purchase"+Integer.valueOf(number_bags)+" bags");

		addItemPerUnit(bag, number_bags);
		weightChanged(number_bags);
		customerDisplay.informCustomer("The operation is complete");


	}

	/**
	 * Allows customer to add an item using a price lookup instead of scanning
	 *
	 * @param plu
	 * @param weight
	 * @return
	 */
	public boolean addItemPLU(PriceLookUpCode plu, Double weight) {
		//A valid PLU code must be provided
		if (plu == null) {
			throw new  NullPointerException("PLU cannot be null!");
		}
		//Checks if the plu provided actually correlates to a product in the database by calling the getPLUProductfromPLU method
		if(getPLUProductFromPlu(plu) != null) {
			//If it exists, it gets it from the database
			PLUCodedProduct product = getPLUProductFromPlu(plu);
			//Checks that the amount of that product in stock is not 0. If it is, method returns false.
			if(getProductInventory(product) == 0) {
				return false;
			}
			else {
				//If the amount of the product in stock is not 0, deducts 1 from the amount in stock (inventory) using the deductInventory method
				deductInventory(product, 1);
				//Creates an item object
				PriceLookUpCodedUnit pluItem = getPLUUnitFromPLU(plu, weight);
				//Adds that item to the scale
				this.station.scale.add(pluItem);
				//Gets the price (per unit weight) and multiplies it by the total weight of the product the customer purchased
				BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(weight));
				//Adds that to the total price
				this.total = this.total.add(price);
				//Adds the item to Array list of purchased items by PLU (to be used for checking weight discrepancy)
				this.pluItems.add(pluItem);
				return true;
			}
		}
		//If the plu provided doesn't correlate to any product in the database, method returns false
		else {
			return false;
		}
	}

	/**
	 *
	 * Allows customer to add an item by scanning the barcode
	 *
	 * @param barcode
	 * @return
	 */
	public boolean addItemScan(Barcode barcode) {
		//The product must have a valid barcode
		if (barcode == null) {
			throw new  NullPointerException("Barcode cannot be null!");
		}

		//Checks if the barcode provided actually correlates to a product in the database by calling the getBarcodedProductfromBarcode method
		if(getBarcodedUnitFromBarcode(barcode) != null) {
			//If it exists, it gets it from the database
			BarcodedProduct product = getBarcodedProductFromBarcode(barcode);
			//Checks that the amount of that product in stock is not 0. If it is, method returns false.
			if(getProductInventory(product) == 0) {
				return false;
			}
			else {
				//If the amount of the product in stock is not 0, deducts 1 from the amount in stock (inventory) using the deductInventory method
				deductInventory(product, 1);
				//Creates an item object
				BarcodedUnit barcodeItem = getBarcodedUnitFromBarcode(barcode);
				//Invokes the station's scanner
				this.station.mainScanner.scan(barcodeItem);
				//Gets the price (per unit weight) and multiplies it by the total expected weight of the product the customer purchased
				BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(product.getExpectedWeight()));
				//Adds that to the total price
				this.total = this.total.add(price);
				//Adds the item to Array list of purchased items by scanning (to be used for checking weight discrepancy)
				this.scannedItems.add(barcodeItem);
				return true;
			}
		}
		//If the barcode provided doesn't correlate to any product in the database, method returns false
		else {
			return false;
		}
	}

	/**
	 * A method that checks whether there's a weight discrepancy after items have been added to bagging area
	 * @return
	 * @throws OverloadException
	 */
	public boolean weightDiscrepancy() throws OverloadException{
		//Loops through array list containing all scanned items and adds their expected weight to the total expected weight
		for(int i = 0; i < this.scannedItems.size(); i++) {
			this.totalExpectedWeight = this.totalExpectedWeight.add(BigDecimal.valueOf(this.scannedItems.get(i).getWeight()));
		}

		//Loops through array list containing all plu added items and adds their expected weight to the total expected weight
		for(int i = 0; i < this.pluItems.size(); i++) {
			this.totalExpectedWeight = this.totalExpectedWeight.add(BigDecimal.valueOf(this.pluItems.get(i).getWeight()));
		}

		//Gets the current weight on the station's bagging area
		double currentWeight = this.station.baggingArea.getCurrentWeight();
		//total expected weight must match the total weight on bagging area
		if(this.totalExpectedWeight != BigDecimal.valueOf(currentWeight)) {
			//If not, the attendant is notified, and the method returns true
			AttendantIO callAttendant = new AttendantIO();
			callAttendant.informAttendant("Weight Discrepancy Detected!");
			return true;
		}
		//If they match, method returns false
		return false;
	}
	public void payWithCard(){
		int holdNum;
		if (attempt > 3){
			bank.block(cardData.getNumber());
			System.out.println("Maximum attempts have been reached, please contact your bank.");
			return;
		}
		holdNum = bank.authorizeHold(cardData.getNumber(), total);
		attempt++;
		if (holdNum!=-1){
			attempt =1;
		}
		if (holdNum == -1) {
			System.out.println("The card could be blocked or insufficient balance!");
			return;
		} 
		else {
			System.out.println("Hold number: " + holdNum);
			boolean postTransactionStatus = bank.postTransaction(cardData.getNumber(), holdNum, total);
			if (postTransactionStatus){
					customerIO.setAmount(BigDecimal.valueOf(0));
			}
			else {
				System.out.println("The card could be blocked or insufficient balance!");
			}
		}
	}
}
