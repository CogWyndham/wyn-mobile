package uimap;

import org.openqa.selenium.By;

/**
 * UI Map for UserRegistrationPage 
 */
public class UserRegistrationPage {
	// Text boxes
	public static final By txtFirstName = By.name("firstName");
	public static final By txtLastName = By.name("lastName");
	public static final By txtPhone = By.name("phone");
	public static final By txtEmail = By.name("userName");
	public static final By txtAddressLine1 = By.name("address1");
	public static final By txtCity = By.name("city");
	public static final By txtState = By.name("state");
	public static final By txtPostalCode = By.name("postalCode");
	public static final By txtUsername = By.name("email");
	public static final By txtPassword = By.name("password");
	public static final By txtConfirmPassword = By.name("confirmPassword");
	
	//Buttons
	public static final By btnRegister = By.name("register");
}