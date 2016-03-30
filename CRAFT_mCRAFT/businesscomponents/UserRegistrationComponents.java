package businesscomponents;

import supportlibraries.*;
import uimap.*;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;


/**
 * Class for storing business components related to the user registration functionality
 * @author Cognizant
 */
public class UserRegistrationComponents extends ReusableLibrary {
	private static final String GENERAL_DATA = "General_Data";
	private static final String REGISTER_USER_DATA = "RegisterUser_Data";
	
	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper The {@link ScriptHelper} object passed from the {@link DriverScript}
	 */
	public UserRegistrationComponents(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}
	
	
	public void clickRegister() {
		report.updateTestLog("Click Register", "Click on the REGISTER link", Status.DONE);
		driver.findElement(MasterPage.lnkRegister).click();
	}
	
	public void registerUser() {
		report.updateTestLog("Enter user details", "Enter new user details for registration", Status.DONE);
		driver.findElement(UserRegistrationPage.txtFirstName).sendKeys(dataTable.getData(REGISTER_USER_DATA,"FirstName"));
		driver.findElement(UserRegistrationPage.txtLastName).sendKeys(dataTable.getData(REGISTER_USER_DATA,"LastName"));		
		driver.findElement(UserRegistrationPage.txtPhone).sendKeys(dataTable.getData(REGISTER_USER_DATA,"Phone"));		
		driver.findElement(UserRegistrationPage.txtEmail).sendKeys(dataTable.getData(REGISTER_USER_DATA,"Email"));	
		driver.findElement(UserRegistrationPage.txtAddressLine1).sendKeys(dataTable.getData(REGISTER_USER_DATA,"Address"));
		driver.findElement(UserRegistrationPage.txtCity).sendKeys(dataTable.getData(REGISTER_USER_DATA,"City"));
		driver.findElement(UserRegistrationPage.txtState).sendKeys(dataTable.getData(REGISTER_USER_DATA,"State"));
		driver.findElement(UserRegistrationPage.txtPostalCode).sendKeys(dataTable.getData(REGISTER_USER_DATA,"PostalCode"));
		driver.findElement(UserRegistrationPage.txtUsername).sendKeys(dataTable.getData(GENERAL_DATA,"Username"));
		String password = dataTable.getData(GENERAL_DATA, "Password");
		driver.findElement(UserRegistrationPage.txtPassword).sendKeys(password);
		driver.findElement(UserRegistrationPage.txtConfirmPassword).sendKeys(password);
		
		report.updateTestLog("Register", "Click on Register User", Status.SCREENSHOT);
		driver.findElement(UserRegistrationPage.btnRegister).click();
	}
	
	public void verifyRegistration() {
		String userName = dataTable.getData(GENERAL_DATA, "Username");
		
		if(driverUtil.isTextPresent("^[\\s\\S]*Dear " +
					dataTable.getExpectedResult("FirstName") + " " +
					dataTable.getExpectedResult("LastName") + "[\\s\\S]*$")) {
			report.updateTestLog("Verify Registration",
										"User " + userName + " registered successfully", Status.PASS);
		} else {
			throw new FrameworkException("Verify Registration",
											"User " + userName + " registration failed");
		}
	}
	
	public void clickSignIn() {
		report.updateTestLog("Click sign-in", "Click the sign-in link", Status.DONE);
		driver.findElement(UserRegistrationConfirmationPage.lnkSignIn).click();
	}
}