package uimap;

import org.openqa.selenium.By;

/**
 * UI Map for BookFlightPage 
 */
public class BookFlightPage {
	// Text boxes
	public static final String txtFirstName = "passFirst";
	public static final String txtLastName = "passLast";
	public static final By txtCardNo = By.name("creditnumber");
	
	// Combo boxes
	public static final By cmbCreditCard = By.name("creditCard");
	
	// Buttons
	public static final By btnSecurePurchase = By.name("buyFlights");
}