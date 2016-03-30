package testscripts.LoginScenario;

import org.testng.annotations.Test;

import com.cognizant.framework.selenium.Browser;
import com.cognizant.framework.selenium.SeleniumTestParameters;

import supportlibraries.DriverScript;
import supportlibraries.CRAFTTestCase;


/**
 * Test for login with newly registered user
 * @author Cognizant
 */
public class TestForLoginWithNewlyRegisteredUser extends CRAFTTestCase {
	
	@Test
	public void testRunner() {
		SeleniumTestParameters testParameters =
					new SeleniumTestParameters(currentScenario, currentTestcase);
		testParameters.setCurrentTestDescription("Test for login with newly registered user");
		testParameters.setBrowser(Browser.CHROME);
		
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
		
		tearDownTestRunner(testParameters, driverScript);
	}
}