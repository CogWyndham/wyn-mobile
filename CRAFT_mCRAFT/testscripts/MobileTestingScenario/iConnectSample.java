package testscripts.MobileTestingScenario;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.selenium.Browser;
import com.cognizant.framework.selenium.ExecutionMode;
import com.cognizant.framework.selenium.MobileExecutionPlatform;
import com.cognizant.framework.selenium.MobileToolName;
import com.cognizant.framework.selenium.SeleniumTestParameters;

import supportlibraries.DriverScript;
import supportlibraries.CRAFTTestCase;

/**
 * Test for login with invalid user credentials
 * 
 * @author Cognizant
 */
public class iConnectSample extends CRAFTTestCase {

	@Test(dataProvider = "TestConfigurations")
	public void testRunner(String testInstance, ExecutionMode executionMode,
			MobileToolName mobileToolName,
			MobileExecutionPlatform executionPlatform, String osVersion,
			String deviceName, Browser browser, int startIteration,
			int endIteration) {
		SeleniumTestParameters testParameters = new SeleniumTestParameters(
				currentScenario, currentTestcase);
		testParameters
				.setCurrentTestDescription("Test for login with invalid user credentials");
		testParameters.setCurrentTestInstance(testInstance);
		testParameters.setExecutionMode(executionMode);
		testParameters.setMobileExecutionPlatform(executionPlatform);
		testParameters.setMobileToolName(mobileToolName);
		testParameters.setmobileOSVersion(osVersion);
		testParameters.setDeviceName(deviceName);
		testParameters.setBrowser(browser);
		testParameters
				.setIterationMode(IterationOptions.RUN_ONE_ITERATION_ONLY);
		testParameters.setStartIteration(startIteration);
		testParameters.setEndIteration(endIteration);

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}

	@DataProvider(name = "TestConfigurations", parallel = true)
	public Object[][] dataTC2() {
		return new Object[][] {

		{ "Instance3", ExecutionMode.MOBILE, MobileToolName.APPIUM,
				MobileExecutionPlatform.IOS, "9.2", "00E6FB9156B3C267",
				Browser.CHROME, 2, 2 } };
	}
}