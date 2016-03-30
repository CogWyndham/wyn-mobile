package supportlibraries;

import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.selenium.*;

import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;


/**
 * Abstract base class for all the test cases to be automated
 * @author Cognizant
 */
public abstract class CRAFTTestCase {
	/**
	 * The current scenario
	 */
	protected String currentScenario;
	/**
	 * The current test case
	 */
	protected String currentTestcase;
	
	private ResultSummaryManager resultSummaryManager =
										ResultSummaryManager.getInstance();
	
	
	/**
	 * Function to do the required framework setup activities before executing the overall test suite
	 * @param testContext The TestNG {@link ITestContext} of the current test suite 
	 */
	@BeforeSuite
	public void setUpTestSuite(ITestContext testContext) {
		resultSummaryManager.setRelativePath();
		resultSummaryManager.initializeTestBatch(testContext.getSuite().getName());
		
		int nThreads;
		if ("false".equalsIgnoreCase(testContext.getSuite().getParallel())) {
			nThreads = 1;
		} else {
			nThreads = testContext.getCurrentXmlTest().getThreadCount();
		}
		
		// Note: Separate threads may be spawned through usage of DataProvider
		// testContext.getSuite().getXmlSuite().getDataProviderThreadCount();
		// This will be at test case level (multiple instances on same test case in parallel)
		// This level of threading will not be reflected in the summary report
		
		resultSummaryManager.initializeSummaryReport(nThreads);
		resultSummaryManager.setupErrorLog();
	}
	
	/**
	 * Function to do the required framework setup activities before executing each test case
	 */
	@BeforeMethod
	public void setUpTestRunner() {
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		if(frameworkParameters.getStopExecution()) {
			tearDownTestSuite();
			
			// Throwing TestNG SkipException within a configuration method causes all subsequent test methods to be skipped/aborted
			throw new SkipException("Test execution terminated by user! All subsequent tests aborted...");
		} else {
			currentScenario = this.getClass().getPackage().getName().substring(12);
			currentTestcase = this.getClass().getSimpleName();
		}
	}
	
	/**
	 * Function to do the required framework teardown activities after executing each test case
	 * @param testParameters The {@link SeleniumTestParameters} object passed from the test case
	 * @param driverScript The {@link DriverScript} object passed from the test case
	 */
	protected synchronized void tearDownTestRunner(SeleniumTestParameters testParameters,
													DriverScript driverScript) {
		String testReportName = driverScript.getReportName();
		String executionTime = driverScript.getExecutionTime();
		String testStatus = driverScript.getTestStatus();
		
		resultSummaryManager.updateResultSummary(testParameters, testReportName,
														executionTime, testStatus);
		
		if("Failed".equalsIgnoreCase(testStatus)) {
			Assert.fail(driverScript.getFailureDescription());
		}
	}
	
	/**
	 * Function to do the required framework teardown activities after executing the overall test suite
	 */
	@AfterSuite
	public void tearDownTestSuite() {
		resultSummaryManager.wrapUp(true);
		//resultSummaryManager.launchResultSummary();
	}
	
	@DataProvider(name = "GlobalTestConfigurations", parallel = true)
	public Object[][] dataGlobal() {
		return new Object[][] {
			//{ "Instance4", ExecutionMode.PERFECTO_REMOTEWEBDRIVER, "Apple_iPad_Air", Browser.PERFECTO_MOBILE_SAFARI, Platform.ANY },
			//{ "Instance5", ExecutionMode.PERFECTO_REMOTEWEBDRIVER, "Samsung_Galaxy_S6", Browser.PERFECTO_MOBILE_CHROME, Platform.ANDROID },
			//{ "Instance6", ExecutionMode.PERFECTO_REMOTEWEBDRIVER, "Samsung_Galaxy_S5", Browser.PERFECTO_MOBILE, Platform.ANDROID },
			{ "Instance4", ExecutionMode.LOCAL, MobileToolName.DEFAULT,MobileExecutionPlatform.ANDROID,"4.4","N/A", Browser.FIREFOX, Platform.WINDOWS }
		};
	}
}