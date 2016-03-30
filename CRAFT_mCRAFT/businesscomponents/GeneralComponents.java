package businesscomponents;


import supportlibraries.*;
import uimap.*;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;


/**
 * Class for storing general purpose business components
 * @author Cognizant
 */
public class GeneralComponents extends ReusableLibrary {
	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper The {@link ScriptHelper} object passed from the {@link DriverScript}
	 */
	public GeneralComponents(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}
	
	
	public void invokeApplication() {
		report.updateTestLog("Invoke Application", "Invoke the application under test @ " +
									properties.getProperty("ApplicationUrl"), Status.DONE);
		
		driver.get(properties.getProperty("ApplicationUrl"));
	}
	
	public void login() {
		String userName = dataTable.getData("General_Data", "Username");
		String password = dataTable.getData("General_Data", "Password");
		
		report.updateTestLog("Enter user credentials", "Specify " +
									"username = " + userName + ", " +
									"password = " + password, Status.PASS);
		driver.findElement(SignOnPage.txtUsername).sendKeys(userName);
		driver.findElement(SignOnPage.txtPassword).sendKeys(password);
		
		report.updateTestLog("Login", "Click the sign-in button", Status.SCREENSHOT);
		driver.findElement(SignOnPage.btnLogin).click();
	}
	
	public void verifyLoginSuccessful() {
		if(driverUtil.objectExists(MasterPage.lnkSignOff)&&
				driver.findElement(MasterPage.lnkSignOff).isDisplayed()) {
			report.updateTestLog("Verify Login", "Login succeeded for valid user", Status.PASS);
		} else {
			frameworkParameters.setStopExecution(true);
			throw new FrameworkException("Verify Login", "Login failed for valid user");
		}
	}
	
	public void verifyLoginFailed() {
		if(!driverUtil.objectExists(MasterPage.lnkSignOff)) {
			report.updateTestLog("Verify Login", "Login failed for invalid user", Status.PASS);
		} else {
			report.updateTestLog("Verify Login", "Login succeeded for invalid user", Status.FAIL);
		}
	}
	
	public void logout() {
		report.updateTestLog("Logout", "Click the sign-off link", Status.DONE);
		driver.findElement(MasterPage.lnkSignOff).click();
	}
}