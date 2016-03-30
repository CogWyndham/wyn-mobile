package uimap;

import org.openqa.selenium.By;

/**
 * UI Map for FlightFinderPage 
 */
public class FlightFinderPage {
	// Combo boxes
	public static final By cmbPassengerCount = By.name("passCount");
	public static final By cmbDepartFrom = By.name("fromPort");
	public static final By cmbDepartMonth = By.name("fromMonth");
	public static final By cmbDepartDate = By.name("fromDay");
	public static final By cmbArriveAt = By.name("toPort");
	public static final By cmbArriveMonth = By.name("toMonth");
	public static final By cmbArriveDate = By.name("toDay");
	public static final By cmbAirline = By.name("airline");
	
	// Buttons
	public static final By btnContinue = By.name("findFlights");
}