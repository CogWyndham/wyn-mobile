package supportlibraries;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.cognizant.framework.*;
import com.cognizant.framework.ReportThemeFactory.Theme;
import com.cognizant.framework.selenium.*;
import com.experitest.client.Client;
import com.perfectomobile.selenium.MobileDriver;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.*;

/**
 * Driver script class which encapsulates the core logic of the framework
 * 
 * @author Cognizant
 */
public class DriverScript {
	private List<String> businessFlowData;
	private int currentIteration, currentSubIteration;
	private Date startTime, endTime;
	private String executionTime;

	private CraftDataTable dataTable;
	private ReportSettings reportSettings;
	private SeleniumReport report;

	private CraftDriver driver;

	private WebDriverUtil driverUtil;
	private ScriptHelper scriptHelper;
	private MobileDriver mobileDriver;
	private Client client;

	private Properties properties;
	private Properties mobileProperties;
	private final FrameworkParameters frameworkParameters = FrameworkParameters
			.getInstance();

	private Boolean linkScreenshotsToTestLog = true;

	private final SeleniumTestParameters testParameters;
	private String reportPath;

	private CRAFTLiteTestCase testCase;
	private String seeTesResultPath;

	/**
	 * DriverScript constructor
	 * 
	 * @param testParameters
	 *            A {@link SeleniumTestParameters} object
	 */
	public DriverScript(SeleniumTestParameters testParameters) {
		this.testParameters = testParameters;
	}

	/**
	 * Function to configure the linking of screenshots to the corresponding
	 * test log
	 * 
	 * @param linkScreenshotsToTestLog
	 *            Boolean variable indicating whether screenshots should be
	 *            linked to the corresponding test log
	 */
	public void setLinkScreenshotsToTestLog(Boolean linkScreenshotsToTestLog) {
		this.linkScreenshotsToTestLog = linkScreenshotsToTestLog;
	}

	/**
	 * Function to get the name of the test report
	 * 
	 * @return The test report name
	 */
	public String getReportName() {
		return reportSettings.getReportName();
	}

	/**
	 * Function to get the status of the test case executed
	 * 
	 * @return The test status
	 */
	public String getTestStatus() {
		return report.getTestStatus();
	}

	/**
	 * Function to get the decription of any failure that may occur during the
	 * script execution
	 * 
	 * @return The failure description (relevant only if the test fails)
	 */
	public String getFailureDescription() {
		return report.getFailureDescription();
	}

	/**
	 * Function to get the execution time for the test case
	 * 
	 * @return The test execution time
	 */
	public String getExecutionTime() {
		return executionTime;
	}

	/**
	 * Function to execute the given test case
	 */
	public void driveTestExecution() {
		startUp();
		initializeTestIterations();
		initializeWebDriver();
		initializeTestReport();
		initializeDatatable();
		executeCraftOrCraftLite();

		quitWebDriver();
		wrapUp();
	}

	private void executeCraftOrCraftLite() {
		if (properties.getProperty("Approach")
				.equalsIgnoreCase("KeywordDriven")) {
			initializeTestScript();
			executeCRAFTTestIterations();
		} else {
			initializeTestCase();

			try {
				testCase.setUp();
				executeCRAFTLiteTestIterations();
			} catch (FrameworkException fx) {
				exceptionHandler(fx, fx.getErrorName());
			} catch (Exception ex) {
				exceptionHandler(ex, "Error");
			} finally {
				testCase.tearDown(); // tearDown will ALWAYS be called
			}
		}

	}

	private void startUp() {
		startTime = Util.getCurrentTime();

		properties = Settings.getInstance();
		mobileProperties = Settings.getMobilePropertiesInstance();

		setDefaultTestParameters();
	}

	private void setDefaultTestParameters() {
		if (testParameters.getIterationMode() == null) {
			testParameters
					.setIterationMode(IterationOptions.RUN_ALL_ITERATIONS);
		}

		if (testParameters.getExecutionMode() == null) {
			testParameters.setExecutionMode(ExecutionMode.valueOf(properties
					.getProperty("DefaultExecutionMode")));
		}

		if (testParameters.getMobileExecutionPlatform() == null) {
			testParameters.setMobileExecutionPlatform(MobileExecutionPlatform
					.valueOf(mobileProperties
							.getProperty("DefaultMobileExecutionPlatform")));
		}

		if (testParameters.getMobileToolName() == null) {
			testParameters.setMobileToolName(MobileToolName
					.valueOf(mobileProperties
							.getProperty("DefaultMobileToolName")));
		}

		if (testParameters.getDeviceName() == null) {
			testParameters.setDeviceName(mobileProperties
					.getProperty("DefaultDevice"));
		}

		if (testParameters.getBrowser() == null) {
			testParameters.setBrowser(Browser.valueOf(properties
					.getProperty("DefaultBrowser")));
		}

		if (testParameters.getPlatform() == null) {
			testParameters.setPlatform(Platform.valueOf(properties
					.getProperty("DefaultPlatform")));
		}

		testParameters.setInstallApplication(Boolean
				.parseBoolean(mobileProperties
						.getProperty("InstallApplicationInDevice")));

	}

	private void initializeTestIterations() {
		switch (testParameters.getIterationMode()) {
		case RUN_ALL_ITERATIONS:
			int nIterations = getNumberOfIterations();
			testParameters.setEndIteration(nIterations);

			currentIteration = 1;
			break;

		case RUN_ONE_ITERATION_ONLY:
			currentIteration = 1;
			break;

		case RUN_RANGE_OF_ITERATIONS:
			if (testParameters.getStartIteration() > testParameters
					.getEndIteration()) {
				throw new FrameworkException("Error",
						"StartIteration cannot be greater than EndIteration!");
			}
			currentIteration = testParameters.getStartIteration();
			break;

		default:
			throw new FrameworkException("Unhandled Iteration Mode!");
		}
	}

	private int getNumberOfIterations() {
		String datatablePath = frameworkParameters.getRelativePath()
				+ Util.getFileSeparator() + "Datatables";
		ExcelDataAccess testDataAccess = new ExcelDataAccess(datatablePath,
				testParameters.getCurrentScenario());
		testDataAccess.setDatasheetName(properties
				.getProperty("DefaultDataSheet"));
		if (properties.getProperty("Approach")
				.equalsIgnoreCase("KeywordDriven")) {
			int startRowNum = testDataAccess.getRowNum(
					testParameters.getCurrentTestcase(), 0);
			int nTestcaseRows = testDataAccess.getRowCount(
					testParameters.getCurrentTestcase(), 0, startRowNum);
			int nSubIterations = testDataAccess
					.getRowCount("1", 1, startRowNum); // Assumption:
														// Every
														// test
														// case
														// will
														// have
														// at
														// least
														// one
														// iteration
			return nTestcaseRows / nSubIterations;
		} else {
			return testDataAccess.getRowCount(
					testParameters.getCurrentTestcase(), 0);
		}

	}

	private void initializeWebDriver() {

		switch (testParameters.getExecutionMode()) {
		case LOCAL:
			WebDriver webDriver = WebDriverFactory.getWebDriver(testParameters
					.getBrowser());
			driver = new CraftDriver(webDriver);
			driver.setTestParameters(testParameters);
			WaitPageLoad();
			break;

		case REMOTE:
			WebDriver remoteWebDriver = WebDriverFactory.getRemoteWebDriver(
					testParameters.getBrowser(),
					properties.getProperty("RemoteUrl"));
			driver = new CraftDriver(remoteWebDriver);
			driver.setTestParameters(testParameters);
			WaitPageLoad();
			break;

		case LOCAL_EMULATED_DEVICE:
			testParameters.setBrowser(Browser.CHROME); // Mobile emulation
														// supported only on
														// Chrome
			WebDriver localEmulatedDriver = WebDriverFactory
					.getEmulatedWebDriver(testParameters.getDeviceName());
			driver = new CraftDriver(localEmulatedDriver);
			driver.setTestParameters(testParameters);
			WaitPageLoad();
			break;

		case REMOTE_EMULATED_DEVICE:
			testParameters.setBrowser(Browser.CHROME); // Mobile emulation
														// supported only on
														// Chrome
			WebDriver remoteEmulatedDriver = WebDriverFactory
					.getEmulatedRemoteWebDriver(testParameters.getDeviceName(),
							properties.getProperty("RemoteUrl"));
			driver = new CraftDriver(remoteEmulatedDriver);
			driver.setTestParameters(testParameters);
			break;

		case GRID:
			WebDriver remoteGridDriver = WebDriverFactory.getRemoteWebDriver(
					testParameters.getBrowser(),
					testParameters.getBrowserVersion(),
					testParameters.getPlatform(),
					properties.getProperty("RemoteUrl"));
			driver = new CraftDriver(remoteGridDriver);
			driver.setTestParameters(testParameters);
			WaitPageLoad();
			break;

		case MOBILE:
			if ((testParameters.getMobileToolName()
					.equals(MobileToolName.DEFAULT))
					|| (testParameters.getMobileToolName()
							.equals(MobileToolName.APPIUM))) {
				WebDriver appiumDriver = AppiumDriverFactory.getAppiumDriver(
						testParameters.getMobileExecutionPlatform(),
						testParameters.getDeviceName(),
						testParameters.getMobileOSVersion(),
						testParameters.shouldInstallApplication(),
						mobileProperties.getProperty("AppiumURL"));
				driver = new CraftDriver(appiumDriver);
				driver.setTestParameters(testParameters);
			} else if (testParameters.getMobileToolName().equals(
					MobileToolName.REMOTE_WEBDRIVER)) {
				WebDriver remoteAppiumDriver = AppiumDriverFactory
						.getAppiumRemoteWebDriver(
								testParameters.getMobileExecutionPlatform(),
								testParameters.getDeviceName(),
								testParameters.getMobileOSVersion(),
								testParameters.shouldInstallApplication(),
								mobileProperties.getProperty("AppiumURL"));
				driver = new CraftDriver(remoteAppiumDriver);
				driver.setTestParameters(testParameters);
			}

			break;

		case PERFECTO:

			// IMobileWebDriver - Perfecto default selenium Implementation

			if (testParameters.getMobileToolName().equals(
					MobileToolName.DEFAULT)) {
				WebDriver perfectoDriver = PerfectoDriverFactory
						.getPerfectoDefaultDriver(testParameters
								.getMobileExecutionPlatform(), testParameters
								.getDeviceName(), mobileProperties
								.getProperty("PerfectoAndroidIdentifier"),
								mobileProperties
										.getProperty("PerfecttoIosIdentifier"),
								testParameters.getBrowser());
				driver = new CraftDriver(perfectoDriver);
				mobileDriver = PerfectoDriverFactory.mobileDriverPrefecto;
				driver.setTestParameters(testParameters);

			} else if (testParameters.getMobileToolName().equals(
					MobileToolName.APPIUM)) {
				WebDriver appiumPerfectoDriver = PerfectoDriverFactory
						.getPerfectoAppiumDriver(
								testParameters.getMobileExecutionPlatform(),
								testParameters.getDeviceName(),
								mobileProperties.getProperty("PerfectoHost"));
				driver = new CraftDriver(appiumPerfectoDriver);
				driver.setTestParameters(testParameters);

			} else if (testParameters.getMobileToolName().equals(
					MobileToolName.REMOTE_WEBDRIVER)) {
				WebDriver remotePerfectoDriver = PerfectoDriverFactory
						.getPerfectoRemoteWebDriver(
								testParameters.getMobileExecutionPlatform(),
								testParameters.getDeviceName(),
								mobileProperties.getProperty("PerfectoHost"),
								testParameters.getBrowser());
				driver = new CraftDriver(remotePerfectoDriver);
				driver.setTestParameters(testParameters);
			}

			break;
		case SEETEST:

			testParameters.setMobileToolName(MobileToolName.DEFAULT);
			WebDriver seeTestDriver = SeeTestDriverFactory
					.getMobileLabsDriver(
							mobileProperties.getProperty("SeeTestHost", "localhost"),
							Integer.parseInt(mobileProperties
									.getProperty("SeeTestDefaultPort")),
							mobileProperties
									.getProperty("SeeTestProjectBaseDirectory"),
							mobileProperties.getProperty("SeeTestReportType",
									"xml"),
							"report",
							"Test Name from Driver Init",
							testParameters.getMobileExecutionPlatform(),
							mobileProperties
									.getProperty("SeeTestAndroidApplicationName"),
							mobileProperties
									.getProperty("SeeTestiOSApplicationName"),
							mobileProperties
									.getProperty("SeeTestAndroidWebApplicationName"),
							mobileProperties
									.getProperty("SeeTestiOSWebApplicationName"),
							testParameters.getDeviceName());
			driver = new CraftDriver(seeTestDriver);
			client = SeeTestDriverFactory.client;
			driver.setTestParameters(testParameters);

			break;

		case MOBILELABS:

			testParameters.setMobileToolName(MobileToolName.REMOTE_WEBDRIVER);
			WebDriver mobilelabsDriver = MobileLabsDriverFactory
					.getMobileLabsDriver(
							testParameters.getMobileExecutionPlatform(),
							testParameters.getDeviceName(),
							mobileProperties.getProperty("AppiumURL"),
							testParameters.getMobileOSVersion());
			driver = new CraftDriver(mobilelabsDriver);
			driver.setTestParameters(testParameters);
			break;

		default:
			throw new FrameworkException("Unhandled Execution Mode!");
		}
		implicitWaitForDriver();

	}

	private void implicitWaitForDriver() {
		long objectSyncTimeout = Long.parseLong(properties.get(
				"ObjectSyncTimeout").toString());
		driver.manage().timeouts()
				.implicitlyWait(objectSyncTimeout, TimeUnit.SECONDS);
	}

	private void WaitPageLoad() {
		long pageLoadTimeout = Long.parseLong(properties.get("PageLoadTimeout")
				.toString());
		driver.manage().timeouts()
				.pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);
		driver.manage().window().maximize();

	}

	private void initializeTestReport() {
		initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory.getReportsTheme(Theme
				.valueOf(properties.getProperty("ReportsTheme")));

		report = new SeleniumReport(reportSettings, reportTheme, testParameters);

		report.initialize();
		report.setDriver(driver);
		if (testParameters.getExecutionMode().equals(ExecutionMode.PERFECTO)
				&& testParameters.getMobileToolName().equals(
						MobileToolName.DEFAULT)) {
			report.setMobileDriver(mobileDriver);
		}
		if(testParameters.getExecutionMode().equals(ExecutionMode.SEETEST)){
			report.setClient(client);
		}
		report.initializeTestLog();
		createTestLogHeader();
	}

	private void initializeReportSettings() {
		if (System.getProperty("ReportPath") != null) {
			reportPath = System.getProperty("ReportPath");
		} else {
			reportPath = TimeStamp.getInstance();
		}

		reportSettings = new ReportSettings(reportPath,
				testParameters.getCurrentScenario() + "_"
						+ testParameters.getCurrentTestcase() + "_"
						+ testParameters.getCurrentTestInstance());

		reportSettings.setDateFormatString(properties
				.getProperty("DateFormatString"));
		reportSettings.setLogLevel(Integer.parseInt(properties
				.getProperty("LogLevel")));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.setGenerateExcelReports(Boolean.parseBoolean(properties
				.getProperty("ExcelReport")));
		reportSettings.setGenerateHtmlReports(Boolean.parseBoolean(properties
				.getProperty("HtmlReport")));
		reportSettings.setGenerateSeeTestReports(Boolean
				.parseBoolean(mobileProperties
						.getProperty("SeeTestReportGeneration")));
		reportSettings.setGeneratePerfectoReports(Boolean
				.parseBoolean(mobileProperties
						.getProperty("PerfectoReportGeneration")));
		reportSettings.setTakeScreenshotFailedStep(Boolean
				.parseBoolean(properties
						.getProperty("TakeScreenshotFailedStep")));
		reportSettings.setTakeScreenshotPassedStep(Boolean
				.parseBoolean(properties
						.getProperty("TakeScreenshotPassedStep")));
		reportSettings.setConsolidateScreenshotsInWordDoc(Boolean
				.parseBoolean(properties
						.getProperty("ConsolidateScreenshotsInWordDoc")));
		reportSettings.setConsolidateScreenshotsInPDF(Boolean
				.parseBoolean(properties
						.getProperty("ConsolidateScreenshotsInPDF")));
		reportSettings.setisMobileExecution(isMobileAutomation());
		if (testParameters.getBrowser().equals(Browser.HTML_UNIT)) {
			// Screenshots not supported in headless mode
			reportSettings.setLinkScreenshotsToTestLog(false);
		} else {
			reportSettings
					.setLinkScreenshotsToTestLog(this.linkScreenshotsToTestLog);
		}
	}

	private void createTestLogHeader() {
		report.addTestLogHeading(reportSettings.getProjectName() + " - "
				+ reportSettings.getReportName()
				+ " Automation Execution Results");
		report.addTestLogSubHeading(
				"Date & Time",
				": "
						+ Util.getFormattedTime(startTime,
								properties.getProperty("DateFormatString")),
				"Iteration Mode", ": " + testParameters.getIterationMode());
		report.addTestLogSubHeading("Start Iteration",
				": " + testParameters.getStartIteration(), "End Iteration",
				": " + testParameters.getEndIteration());

		switch (testParameters.getExecutionMode()) {
		case LOCAL:
			report.addTestLogSubHeading("Browser/Platform", ": "
					+ testParameters.getBrowserAndPlatform(), "Executed on",
					": " + "Local Machine");
			break;

		case REMOTE:
			report.addTestLogSubHeading(
					"Browser/Platform",
					": " + testParameters.getBrowserAndPlatform(),
					"Executed on",
					": " + "Remote Machine @ "
							+ properties.getProperty("RemoteUrl"));
			break;

		case LOCAL_EMULATED_DEVICE:
			report.addTestLogSubHeading("Browser/Platform", ": "
					+ testParameters.getBrowserAndPlatform(), "Executed on",
					": " + "Emulated Mobile Device on Local Machine");
			report.addTestLogSubHeading("Emulated Device Name", ": "
					+ testParameters.getDeviceName(), "", "");
			break;

		case REMOTE_EMULATED_DEVICE:
			report.addTestLogSubHeading("Browser/Platform", ": "
					+ testParameters.getBrowserAndPlatform(), "Executed on",
					": " + "Emulated Mobile Device on Remote Machine @ "
							+ properties.getProperty("RemoteUrl"));
			report.addTestLogSubHeading("Emulated Device Name", ": "
					+ testParameters.getDeviceName(), "", "");
			break;

		case GRID:
			report.addTestLogSubHeading("Browser/Platform", ": "
					+ testParameters.getBrowserAndPlatform(), "Executed on",
					": " + "Grid @ " + properties.getProperty("RemoteUrl"));
			break;

		case MOBILE:
			report.addTestLogSubHeading("ExecutionPlatform", ": "
					+ testParameters.getExecutionMode(), "Executed on", ": "
					+ testParameters.getMobileExecutionPlatform());
			report.addTestLogSubHeading("Tool Used",
					": " + testParameters.getMobileToolName(),
					"Device Name/ID", ": " + testParameters.getDeviceName());
			break;

		case PERFECTO:
			report.addTestLogSubHeading(
					"Execution Mode",
					": " + testParameters.getExecutionMode(),
					"Executed on",
					": " + "Perfecto MobileCloud @ "
							+ mobileProperties.getProperty("PerfectoHost"));
			report.addTestLogSubHeading(
					"Device Name/ID",
					": " + testParameters.getDeviceName() + " ("
							+ testParameters.getMobileExecutionPlatform() + ")",
					"Perfecto User",
					": " + mobileProperties.getProperty("PerfectoUser"));
			break;
		case SEETEST:
			report.addTestLogSubHeading("ExecutionPlatform", ": "
					+ testParameters.getExecutionMode(), "Executed on", ": "
					+ testParameters.getMobileExecutionPlatform());
			report.addTestLogSubHeading("Tool Used",
					": " + testParameters.getMobileToolName(),
					"Device Name/ID", ": " + testParameters.getDeviceName());
			break;
		case MOBILELABS:
			report.addTestLogSubHeading(
					"Execution Mode",
					": " + testParameters.getExecutionMode(),
					"Executed on",
					": " + "MobileLabs Cloud @ "
							+ mobileProperties.getProperty("HostIP"));
			report.addTestLogSubHeading(
					"Device Name/ID",
					": " + testParameters.getDeviceName() + " ("
							+ testParameters.getMobileExecutionPlatform() + ")",
					"MobileLabs User",
					": " + mobileProperties.getProperty("UserName"));

			break;

		default:
			throw new FrameworkException("Unhandled Execution Mode!");
		}

		report.addTestLogTableHeadings();
	}

	private synchronized void initializeDatatable() {
		String datatablePath = frameworkParameters.getRelativePath()
				+ Util.getFileSeparator() + "Datatables";

		String runTimeDatatablePath;
		Boolean includeTestDataInReport = Boolean.parseBoolean(properties
				.getProperty("IncludeTestDataInReport"));
		if (includeTestDataInReport) {
			runTimeDatatablePath = reportPath + Util.getFileSeparator()
					+ "Datatables";

			File runTimeDatatable = new File(runTimeDatatablePath
					+ Util.getFileSeparator()
					+ testParameters.getCurrentScenario() + ".xls");
			if (!runTimeDatatable.exists()) {
				File datatable = new File(datatablePath
						+ Util.getFileSeparator()
						+ testParameters.getCurrentScenario() + ".xls");

				try {
					FileUtils.copyFile(datatable, runTimeDatatable);
				} catch (IOException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"Error in creating run-time datatable: Copying the datatable failed...");
				}
			}

			File runTimeCommonDatatable = new File(runTimeDatatablePath
					+ Util.getFileSeparator() + "Common Testdata.xls");
			if (!runTimeCommonDatatable.exists()) {
				File commonDatatable = new File(datatablePath
						+ Util.getFileSeparator() + "Common Testdata.xls");

				try {
					FileUtils.copyFile(commonDatatable, runTimeCommonDatatable);
				} catch (IOException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"Error in creating run-time datatable: Copying the common datatable failed...");
				}
			}
		} else {
			runTimeDatatablePath = datatablePath;
		}

		dataTable = new CraftDataTable(runTimeDatatablePath,
				testParameters.getCurrentScenario());
		dataTable.setDataReferenceIdentifier(properties
				.getProperty("DataReferenceIdentifier"));
		// CRAFTLite Change
		if (properties.getProperty("Approach")
				.equalsIgnoreCase("ModularDriven")) {
			// Initialize the datatable row in case test data is required during
			// the setUp()
			dataTable.setCurrentRow(testParameters.getCurrentTestcase(),
					currentIteration, 0);
		}
	}

	private void initializeTestScript() {

		scriptHelper = new ScriptHelper(dataTable, report, driver, driverUtil);
		driver.setRport(report);
		initializeBusinessFlow();
	}

	private void initializeBusinessFlow() {
		ExcelDataAccess businessFlowAccess = new ExcelDataAccess(
				frameworkParameters.getRelativePath() + Util.getFileSeparator()
						+ "Datatables", testParameters.getCurrentScenario());
		businessFlowAccess.setDatasheetName("Business_Flow");

		int rowNum = businessFlowAccess.getRowNum(
				testParameters.getCurrentTestcase(), 0);
		if (rowNum == -1) {
			throw new FrameworkException("The test case \""
					+ testParameters.getCurrentTestcase()
					+ "\" is not found in the Business Flow sheet!");
		}

		String dataValue;
		businessFlowData = new ArrayList<String>();
		int currentColumnNum = 1;
		while (true) {
			dataValue = businessFlowAccess.getValue(rowNum, currentColumnNum);
			if ("".equals(dataValue)) {
				break;
			}
			businessFlowData.add(dataValue);
			currentColumnNum++;
		}

		if (businessFlowData.isEmpty()) {
			throw new FrameworkException(
					"No business flow found against the test case \""
							+ testParameters.getCurrentTestcase() + "\"");
		}
	}

	private void executeCRAFTTestIterations() {
		while (currentIteration <= testParameters.getEndIteration()) {
			report.addTestLogSection("Iteration: "
					+ Integer.toString(currentIteration));

			// Evaluate each test iteration for any errors
			try {
				executeTestcase(businessFlowData);
			} catch (FrameworkException fx) {
				exceptionHandler(fx, fx.getErrorName());
			} catch (InvocationTargetException ix) {
				exceptionHandler((Exception) ix.getCause(), "Error");
			} catch (Exception ex) {
				exceptionHandler(ex, "Error");
			}

			currentIteration++;
		}
	}

	private void executeCRAFTLiteTestIterations() {
		while (currentIteration <= testParameters.getEndIteration()) {
			report.addTestLogSection("Iteration: "
					+ Integer.toString(currentIteration));

			// Evaluate each test iteration for any errors
			try {
				testCase.executeTest();
			} catch (FrameworkException fx) {
				exceptionHandler(fx, fx.getErrorName());
			} catch (Exception ex) {
				exceptionHandler(ex, "Error");
			}

			currentIteration++;
			dataTable.setCurrentRow(testParameters.getCurrentTestcase(),
					currentIteration, 0);
		}
	}

	private void initializeTestCase() {
		driverUtil = new WebDriverUtil(driver);
		scriptHelper = new ScriptHelper(dataTable, report, driver, driverUtil);
		// testCase = new TC1();
		testCase = getTestCaseInstance();
		testCase.initialize(scriptHelper);
	}

	private CRAFTLiteTestCase getTestCaseInstance() {
		Class<?> testScriptClass;
		try {
			testScriptClass = Class.forName("testscripts."
					+ testParameters.getCurrentScenario() + "."
					+ testParameters.getCurrentTestcase());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"The specified test case is not found!");
		}

		try {
			return (CRAFTLiteTestCase) testScriptClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while instantiating the specified test script");
		}
	}

	private void executeTestcase(List<String> businessFlowData)
			throws IllegalAccessException, InvocationTargetException,
			ClassNotFoundException, InstantiationException {
		Map<String, Integer> keywordDirectory = new HashMap<String, Integer>();

		for (int currentKeywordNum = 0; currentKeywordNum < businessFlowData
				.size(); currentKeywordNum++) {
			String[] currentFlowData = businessFlowData.get(currentKeywordNum)
					.split(",");
			String currentKeyword = currentFlowData[0];

			int nKeywordIterations;
			if (currentFlowData.length > 1) {
				nKeywordIterations = Integer.parseInt(currentFlowData[1]);
			} else {
				nKeywordIterations = 1;
			}

			for (int currentKeywordIteration = 0; currentKeywordIteration < nKeywordIterations; currentKeywordIteration++) {
				if (keywordDirectory.containsKey(currentKeyword)) {
					keywordDirectory.put(currentKeyword,
							keywordDirectory.get(currentKeyword) + 1);
				} else {
					keywordDirectory.put(currentKeyword, 1);
				}
				currentSubIteration = keywordDirectory.get(currentKeyword);

				dataTable.setCurrentRow(testParameters.getCurrentTestcase(),
						currentIteration, currentSubIteration);

				if (currentSubIteration > 1) {
					report.addTestLogSubSection(currentKeyword
							+ " (Sub-Iteration: " + currentSubIteration + ")");
				} else {
					report.addTestLogSubSection(currentKeyword);
				}

				invokeBusinessComponent(currentKeyword);
			}
		}
	}

	private void invokeBusinessComponent(String currentKeyword)
			throws IllegalAccessException, InvocationTargetException,
			ClassNotFoundException, InstantiationException {
		Boolean isMethodFound = false;
		final String CLASS_FILE_EXTENSION = ".class";
		File[] packageDirectories = {
				new File(frameworkParameters.getRelativePath()
						+ Util.getFileSeparator() + "businesscomponents"),
				new File(frameworkParameters.getRelativePath()
						+ Util.getFileSeparator() + "componentgroups") };

		for (File packageDirectory : packageDirectories) {
			File[] packageFiles = packageDirectory.listFiles();
			String packageName = packageDirectory.getName();

			for (int i = 0; i < packageFiles.length; i++) {
				File packageFile = packageFiles[i];
				String fileName = packageFile.getName();

				// We only want the .class files
				if (fileName.endsWith(CLASS_FILE_EXTENSION)) {
					// Remove the .class extension to get the class name
					String className = fileName.substring(0, fileName.length()
							- CLASS_FILE_EXTENSION.length());

					Class<?> reusableComponents = Class.forName(packageName
							+ "." + className);
					Method executeComponent;

					try {
						// Convert the first letter of the method to lowercase
						// (in line with java naming conventions)
						currentKeyword = currentKeyword.substring(0, 1)
								.toLowerCase() + currentKeyword.substring(1);
						executeComponent = reusableComponents.getMethod(
								currentKeyword, (Class<?>[]) null);
					} catch (NoSuchMethodException ex) {
						// If the method is not found in this class, search the
						// next class
						continue;
					}

					isMethodFound = true;

					Constructor<?> ctor = reusableComponents
							.getDeclaredConstructors()[0];
					Object businessComponent = ctor.newInstance(scriptHelper);

					executeComponent.invoke(businessComponent, (Object[]) null);

					break;
				}
			}
		}

		if (!isMethodFound) {
			throw new FrameworkException("Keyword " + currentKeyword
					+ " not found within any class "
					+ "inside the businesscomponents package");
		}
	}

	private void exceptionHandler(Exception ex, String exceptionName) {
		// Error reporting
		String exceptionDescription = ex.getMessage();
		if (exceptionDescription == null) {
			exceptionDescription = ex.toString();
		}

		if (ex.getCause() != null) {
			report.updateTestLog(exceptionName, exceptionDescription
					+ " <b>Caused by: </b>" + ex.getCause(), Status.FAIL);
		} else {
			report.updateTestLog(exceptionName, exceptionDescription,
					Status.FAIL);
		}

		// Print stack trace for detailed debug information
		StringWriter stringWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(stringWriter));
		String stackTrace = stringWriter.toString();
		report.updateTestLog("Exception stack trace", stackTrace, Status.DEBUG);

		// Error response
		if (frameworkParameters.getStopExecution()) {
			report.updateTestLog(
					"CRAFT Info",
					"Test execution terminated by user! All subsequent tests aborted...",
					Status.DONE);
			currentIteration = testParameters.getEndIteration();
		} else {
			OnError onError = OnError
					.valueOf(properties.getProperty("OnError"));
			switch (onError) {
			// Stop option is not relevant when run from QC
			case NEXT_ITERATION:
				report.updateTestLog(
						"CRAFT Info",
						"Test case iteration terminated by user! Proceeding to next iteration (if applicable)...",
						Status.DONE);
				break;

			case NEXT_TESTCASE:
				report.updateTestLog(
						"CRAFT Info",
						"Test case terminated by user! Proceeding to next test case (if applicable)...",
						Status.DONE);
				currentIteration = testParameters.getEndIteration();
				break;

			case STOP:
				frameworkParameters.setStopExecution(true);
				report.updateTestLog(
						"CRAFT Info",
						"Test execution terminated by user! All subsequent tests aborted...",
						Status.DONE);
				currentIteration = testParameters.getEndIteration();
				break;

			default:
				throw new FrameworkException("Unhandled OnError option!");
			}
		}
	}

	private void quitWebDriver() {
		switch (testParameters.getExecutionMode()) {
		case LOCAL:
		case REMOTE:
		case LOCAL_EMULATED_DEVICE:
		case REMOTE_EMULATED_DEVICE:
		case GRID:
		case MOBILE:
		case MOBILELABS:
			driver.quit();
			break;
		case PERFECTO:
			if (testParameters.getMobileToolName().equals(
					MobileToolName.DEFAULT)) {
				mobileDriver.quit();
			} else {
				driver.quit();
			}
			break;
		case SEETEST:
			// client.applicationClose(properties.getProperty("SeeTestAndroidApplicationName"));
			 client.releaseDevice("*", true, false, true);
			seeTesResultPath = client.generateReport(true);
			break;
		default:
			throw new FrameworkException("Unhandled Execution Mode!");
		}

	}

	private void wrapUp() {
		endTime = Util.getCurrentTime();
		closeTestReport();
		downloadAddtionalReport();
	}

	private void closeTestReport() {
		executionTime = Util.getTimeDifference(startTime, endTime);
		report.addTestLogFooter(executionTime);

		if (reportSettings.shouldConsolidateScreenshotsInWordDoc()) {
			report.consolidateScreenshotsInWordDoc();
		}

		if (reportSettings.shouldConsolidateScreenshotsInPDF()) {
			report.consolidateScreenshotsInPDF(isMobileAutomation());
		}
	}

	private void downloadAddtionalReport() {
		if (testParameters.getExecutionMode().equals(ExecutionMode.PERFECTO)
				&& reportSettings.shouldGeneratePerfectoReports()
				&& testParameters.getMobileToolName().equals(
						MobileToolName.DEFAULT)) {
			PerfectoDriverFactory.downloadReport(
					mobileDriver,
					reportPath + Util.getFileSeparator() + "Perfecto Results"
							+ Util.getFileSeparator()+testParameters.getCurrentScenario()+"_"
							+ testParameters.getCurrentTestcase() + "_"
							+ testParameters.getCurrentTestInstance() + ".pdf");
		}
		if (testParameters.getExecutionMode().equals(ExecutionMode.SEETEST)
				&& reportSettings.shouldGenerateSeeTestReports()) {
			new File(reportPath + Util.getFileSeparator() + "SeeTest Results"
					+ Util.getFileSeparator()
					+ testParameters.getCurrentTestcase() + "_"
					+ testParameters.getCurrentTestInstance()).mkdir();
			File source = new File(seeTesResultPath);
			File dest = new File(reportPath + Util.getFileSeparator()
					+ "SeeTest Results" + Util.getFileSeparator()
					+ testParameters.getCurrentTestcase() + "_"
					+ testParameters.getCurrentTestInstance());

			try {
				FileUtils.copyDirectoryToDirectory(source, dest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isMobileAutomation() {
		boolean isMobileAutomation = false;
		if (testParameters.getExecutionMode().equals(ExecutionMode.MOBILE)
				|| testParameters.getExecutionMode().equals(
						ExecutionMode.PERFECTO)
				|| testParameters.getExecutionMode().equals(
						ExecutionMode.SEETEST)
				|| testParameters.getExecutionMode().equals(
						ExecutionMode.MOBILELABS)) {
			isMobileAutomation = true;
		}
		return isMobileAutomation;

	}
}