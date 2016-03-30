package businesscomponents;

import java.util.concurrent.TimeUnit;

import supportlibraries.*;
import uimap.*;

import com.cognizant.framework.Status;

import org.openqa.selenium.*;


/**
 * Class for storing business components related to the flight reservation functionality
 * @author Cognizant
 */
public class FlightReservationComponents extends ReusableLibrary {
	private static final String FLIGHTS_DATA = "Flights_Data";
	private static final String PASSENGER_DATA = "Passenger_Data";
	
	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper The {@link ScriptHelper} object passed from the {@link DriverScript}
	 */
	public FlightReservationComponents(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}
	
	
	public void findFlights() {
		report.updateTestLog("Enter Search Criteria", "Enter the search criteria to search for flights", Status.DONE);
		
		driver.findElement(FlightFinderPage.cmbPassengerCount).sendKeys((dataTable.getData(PASSENGER_DATA,"PassengerCount")));
		driver.findElement(FlightFinderPage.cmbDepartFrom).sendKeys((dataTable.getData(FLIGHTS_DATA,"FromPort")));
		driver.findElement(FlightFinderPage.cmbDepartMonth).sendKeys((dataTable.getData(FLIGHTS_DATA,"FromMonth")));
		driver.findElement(FlightFinderPage.cmbDepartDate).sendKeys((dataTable.getData(FLIGHTS_DATA,"FromDay")));
		driver.findElement(FlightFinderPage.cmbArriveAt).sendKeys((dataTable.getData(FLIGHTS_DATA,"ToPort")));
		driver.findElement(FlightFinderPage.cmbArriveMonth).sendKeys((dataTable.getData(FLIGHTS_DATA,"ToMonth")));
		driver.findElement(FlightFinderPage.cmbArriveDate).sendKeys((dataTable.getData(FLIGHTS_DATA,"ToDay")));
		driver.findElement(FlightFinderPage.cmbAirline).sendKeys((dataTable.getData(FLIGHTS_DATA,"Airline")));
		
		report.updateTestLog("Find Flights", "Click on find flights", Status.SCREENSHOT);
		driver.findElement(FlightFinderPage.btnContinue).click();
	}
	
	public void selectFlights() {
		report.updateTestLog("Select Flights", "Select the first available flights", Status.DONE);
		driver.findElement(SelectFlightPage.btnContinue).click();
	}
	
	public void bookFlights() {
		report.updateTestLog("Enter Passenger Details", "Enter passenger details", Status.DONE);
		
		String[] passengerFirstNames = dataTable.getData(PASSENGER_DATA,"PassengerFirstNames").split(",");
		String[] passengerLastNames = dataTable.getData(PASSENGER_DATA,"PassengerLastNames").split(",");
		int passengerCount = Integer.parseInt(dataTable.getData(PASSENGER_DATA,"PassengerCount"));
		
		for(int i=0; i<passengerCount; i++) {
			driver.findElement(By.name(BookFlightPage.txtFirstName + i)).sendKeys(passengerFirstNames[i]);
			driver.findElement(By.name(BookFlightPage.txtLastName + i)).sendKeys(passengerLastNames[i]);
		}
		driver.findElement(BookFlightPage.cmbCreditCard).sendKeys(dataTable.getData(PASSENGER_DATA,"CreditCard"));
		driver.findElement(BookFlightPage.txtCardNo).sendKeys(dataTable.getData(PASSENGER_DATA,"CreditNumber"));
		
		report.updateTestLog("Book Tickets", "Click on book tickets", Status.SCREENSHOT);
		driver.findElement(BookFlightPage.btnSecurePurchase).click();
		driverUtil.waitFor(500);	// to avoid StaleElementException (no idea why it occurs!)
	}
	
	public void verifyBooking() {
		if(driverUtil.isTextPresent("^[\\s\\S]*Your itinerary has been booked![\\s\\S]*$")) {
			report.updateTestLog("Verify Booking", "Tickets booked successfully", Status.PASS);
			
			WebElement flightConfirmation =
					driver.findElement(FlightConfirmationPage.lblConfirmationMessage);
			
			String flightConfirmationNumber = flightConfirmation.getText();
			flightConfirmationNumber = flightConfirmationNumber.split("#")[1].trim();
			dataTable.putData(FLIGHTS_DATA, "FlightConfirmationNumber", flightConfirmationNumber);
			report.updateTestLog("Flight Confirmation",
					"The flight confirmation number is " + flightConfirmationNumber,
					Status.SCREENSHOT);
		} else {
			report.updateTestLog("Verify Booking", "Tickets booking failed", Status.FAIL);
		}
	}
	
	public void backToFlights() {
		driver.findElement(FlightConfirmationPage.imgFlights).click();
	}
	
	public void testAppium(){
		System.out.println("inside BC");
		driver.get("http://google.com");
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		report.updateTestLog("test", "stepDescription", Status.SCREENSHOT);
	}
}