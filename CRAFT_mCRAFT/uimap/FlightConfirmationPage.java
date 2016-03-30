package uimap;

import org.openqa.selenium.By;

/**
 * UI Map for FlightConfirmationPage 
 */
public class FlightConfirmationPage {
	// Labels
	public static final By lblConfirmationMessage =
									By.cssSelector("font > font > b > font");
									// By.xpath("//font/font/b/font");
	
	// Images
	public static final By imgFlights = By.xpath("//a/img");
}