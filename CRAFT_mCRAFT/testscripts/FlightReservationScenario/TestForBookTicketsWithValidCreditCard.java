package testscripts.FlightReservationScenario;

import org.openqa.selenium.Platform;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.selenium.Browser;
import com.cognizant.framework.selenium.ExecutionMode;
import com.cognizant.framework.selenium.SeleniumTestParameters;

import supportlibraries.DriverScript;
import supportlibraries.CRAFTTestCase;

/**
 * Test for book flight tickets and verify booking
 * 
 * @author Cognizant
 */
public class TestForBookTicketsWithValidCreditCard extends CRAFTTestCase {

	@Test(dataProvider = "TicketsWithValidCreditCard")
	public void testRunner(String testInstance, ExecutionMode executionMode,
			Browser browser, Platform platform) {
		SeleniumTestParameters testParameters = new SeleniumTestParameters(
				currentScenario, currentTestcase);
		testParameters
				.setCurrentTestDescription("Test for book flight tickets and verify booking");
		testParameters
				.setIterationMode(IterationOptions.RUN_ONE_ITERATION_ONLY);

		testParameters.setCurrentTestInstance(testInstance);
		testParameters.setExecutionMode(executionMode);
		testParameters.setBrowser(browser);
		testParameters.setPlatform(platform);

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

	@DataProvider(name = "TicketsWithValidCreditCard", parallel = true)
	public Object[][] dataTC2() {
		return new Object[][] {
		// { "Instance1", ExecutionMode.LOCAL, Browser.CHROME, 1, 1 },
		{ "Instance2", ExecutionMode.LOCAL, Browser.FIREFOX, 1, 1 }, };
	}
}