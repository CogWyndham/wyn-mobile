package Miscellaneous;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.selenium.Browser;
import com.cognizant.framework.selenium.ExecutionMode;
import com.cognizant.framework.selenium.MobileExecutionPlatform;
import com.cognizant.framework.selenium.MobileToolName;
import com.cognizant.framework.selenium.SeleniumTestParameters;

import supportlibraries.CRAFTLiteTestCase;
import supportlibraries.DriverScript;

/**
 * Test for book flight tickets and verify booking
 * 
 * @author Cognizant
 */
public class CraftLiteTestCaseTemplate extends CRAFTLiteTestCase {

	@Test(dataProvider = "DataProviderName")
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

	@Override
	public void setUp() {
		report.addTestLogSection("Setup");

		// Setup logic

	}

	@Override
	public void executeTest() {
		// main logic
	}

	@Override
	public void tearDown() {
		report.addTestLogSection("Teardown");

		// Tear Down Logic
	}

	@DataProvider(name = "DataProviderName", parallel = true)
	public Object[][] dataTC2() {
		return new Object[][] {
				{ "Instance1", ExecutionMode.MOBILE, MobileToolName.APPIUM,
						MobileExecutionPlatform.ANDROID, "", "DeviceName",
						 Browser.CHROME, 1, 1 } };
	}
}