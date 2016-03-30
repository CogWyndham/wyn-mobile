package testscripts.LoginScenario;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.selenium.Browser;
import com.cognizant.framework.selenium.ExecutionMode;
import com.cognizant.framework.selenium.SeleniumTestParameters;

import supportlibraries.DriverScript;
import supportlibraries.CRAFTTestCase;

/**
 * Test for login with invalid user credentials
 * 
 * @author Cognizant
 */
public class TestForInvalidLogin extends CRAFTTestCase {

	@Test(dataProvider = "InvalidLoginTestConfigurations")
	public void testRunner(String testInstance, ExecutionMode executionMode,
	Browser browser, int startIteration, int endIteration) {
		SeleniumTestParameters testParameters = new SeleniumTestParameters(
				currentScenario, currentTestcase);
		testParameters
				.setCurrentTestDescription("Test for login with invalid user credentials");
		testParameters.setCurrentTestInstance(testInstance);
		testParameters.setExecutionMode(executionMode);
		testParameters.setBrowser(browser);
		testParameters
				.setIterationMode(IterationOptions.RUN_ONE_ITERATION_ONLY);
		testParameters.setStartIteration(startIteration);
		testParameters.setEndIteration(endIteration);

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

	@DataProvider(name = "InvalidLoginTestConfigurations", parallel = true)
	public Object[][] dataTC2() {
		return new Object[][] {
		// { "Instance1", ExecutionMode.LOCAL, Browser.CHROME, 1, 1 },
		{ "Instance2", ExecutionMode.LOCAL, Browser.FIREFOX, 1, 1 }, };
	}
}