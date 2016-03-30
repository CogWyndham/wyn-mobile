package testscripts.LoginScenario;

import org.testng.annotations.Test;

import com.cognizant.framework.selenium.SeleniumTestParameters;

import supportlibraries.DriverScript;
import supportlibraries.CRAFTTestCase;


/**
 * Test for login with valid user credentials
 * @author Cognizant
 */
public class TestForValidLogin extends CRAFTTestCase {
	
	@Test
	public void testRunner() {
		SeleniumTestParameters testParameters =
				new SeleniumTestParameters(currentScenario, currentTestcase);
		testParameters.setCurrentTestDescription("Test for login with valid user credentials");
		
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
		
		tearDownTestRunner(testParameters, driverScript);
	}
}