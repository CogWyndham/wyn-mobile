package com.cognizant.framework.selenium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.ReportSettings;
import com.cognizant.framework.ReportTheme;
import com.cognizant.framework.ReportThemeFactory;
import com.cognizant.framework.Settings;
import com.cognizant.framework.TimeStamp;
import com.cognizant.framework.Util;
import com.cognizant.framework.ReportThemeFactory.Theme;

/**
 * Singleton class that manages the result summary creation during a batch
 * execution
 * 
 * @author Cognizant
 */
public class ResultSummaryManager {
	private SeleniumReport summaryReport;

	private ReportSettings reportSettings;
	private String reportPath;

	private Date overallStartTime, overallEndTime;

	private Properties properties;
	private FrameworkParameters frameworkParameters = FrameworkParameters
			.getInstance();

	private static final ResultSummaryManager RESULT_SUMMARY_MANAGER = new ResultSummaryManager();
	private SeleniumTestParameters testParameters;

	private ResultSummaryManager() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the singleton instance of the
	 * {@link ResultSummaryManager} object
	 * 
	 * @return Instance of the {@link ResultSummaryManager} object
	 */
	public static ResultSummaryManager getInstance() {
		return RESULT_SUMMARY_MANAGER;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * Function to set the absolute path of the framework (to be used as a
	 * relative path)
	 */
	public void setRelativePath() {
		String relativePath = new File(System.getProperty("user.dir"))
				.getAbsolutePath();
		if (relativePath.contains("supportlibraries")) {
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		frameworkParameters.setRelativePath(relativePath);
	}

	/**
	 * Function to initialize the test batch execution
	 * 
	 * @param runConfiguration
	 *            The run configuration to be executed
	 */
	public void initializeTestBatch(String runConfiguration) {
		overallStartTime = Util.getCurrentTime();

		properties = Settings.getInstance();

		frameworkParameters.setRunConfiguration(runConfiguration);
	}

	/**
	 * Function to initialize the summary report
	 * 
	 * @param nThreads
	 *            The number of parallel threads configured for the test batch
	 *            execution
	 */
	public void initializeSummaryReport(int nThreads) {
		initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory.getReportsTheme(Theme
				.valueOf(properties.getProperty("ReportsTheme")));

		summaryReport = new SeleniumReport(reportSettings, reportTheme,
				testParameters);

		summaryReport.initialize();
		summaryReport.initializeResultSummary();

		createResultSummaryHeader(nThreads);
	}

	private void initializeReportSettings() {
		if (System.getProperty("ReportPath") != null) {
			reportPath = System.getProperty("ReportPath");
		} else {
			reportPath = TimeStamp.getInstance();
		}

		reportSettings = new ReportSettings(reportPath, "");

		reportSettings.setDateFormatString(properties
				.getProperty("DateFormatString"));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.setGenerateExcelReports(Boolean.parseBoolean(properties
				.getProperty("ExcelReport")));
		reportSettings.setGenerateHtmlReports(Boolean.parseBoolean(properties
				.getProperty("HtmlReport")));
		reportSettings.setLinkTestLogsToSummary(true);
	}

	private void createResultSummaryHeader(int nThreads) {
		summaryReport.addResultSummaryHeading(reportSettings.getProjectName()
				+ " - Automation Execution Results Summary");
		summaryReport.addResultSummarySubHeading(
				"Date & Time",
				": "
						+ Util.getFormattedTime(overallStartTime,
								properties.getProperty("DateFormatString")),
				"OnError", ": " + properties.getProperty("OnError"));
		summaryReport.addResultSummarySubHeading("Run Configuration", ": "
				+ frameworkParameters.getRunConfiguration(), "No. of threads",
				": " + nThreads);

		summaryReport.addResultSummaryTableHeadings();
	}

	/**
	 * Function to set up the error log file within the test report
	 */
	public void setupErrorLog() {
		String errorLogFile = reportPath + Util.getFileSeparator()
				+ "ErrorLog.txt";
		try {
			System.setErr(new PrintStream(new FileOutputStream(errorLogFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while setting up the Error log!");
		}
	}

	/**
	 * Function to update the results summary with the status of the test
	 * instance which was executed
	 * 
	 * @param testParameters
	 *            The {@link SeleniumTestParameters} object containing the
	 *            details of the test instance which was executed
	 * @param testReportName
	 *            The name of the test report file corresponding to the test
	 *            instance
	 * @param executionTime
	 *            The time taken to execute the test instance
	 * @param testStatus
	 *            The Pass/Fail status of the test instance
	 */
	public void updateResultSummary(SeleniumTestParameters testParameters,
			String testReportName, String executionTime, String testStatus) {
		summaryReport.updateResultSummary(testParameters, testReportName,
				executionTime, testStatus);
	}

	/**
	 * Function to do the required wrap-up activities after completing the test
	 * batch execution
	 * 
	 * @param testExecutedInUnitTestFramework
	 *            Boolean variable indicating whether the test is executed in
	 *            JUnit/TestNG
	 */
	public void wrapUp(Boolean testExecutedInUnitTestFramework) {
		overallEndTime = Util.getCurrentTime();
		String totalExecutionTime = Util.getTimeDifference(overallStartTime,
				overallEndTime);
		summaryReport.addResultSummaryFooter(totalExecutionTime);

		if (testExecutedInUnitTestFramework
				&& System.getProperty("ReportPath") == null) {
			File testNgResultSrc = new File(
					frameworkParameters.getRelativePath()
							+ Util.getFileSeparator()
							+ properties.getProperty("TestNgReportPath")
							+ Util.getFileSeparator()
							+ frameworkParameters.getRunConfiguration());
			File testNgResultCssFile = new File(
					frameworkParameters.getRelativePath()
							+ Util.getFileSeparator()
							+ properties.getProperty("TestNgReportPath")
							+ Util.getFileSeparator() + "testng.css");
			File testNgResultDest = summaryReport
					.createResultsSubFolder("TestNG Results");

			try {
				FileUtils.copyDirectoryToDirectory(testNgResultSrc,
						testNgResultDest);
				FileUtils.copyFileToDirectory(testNgResultCssFile,
						testNgResultDest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Function to launch the summary report at the end of the test batch
	 * execution
	 */
	public void launchResultSummary() {
		if (reportSettings.shouldGenerateHtmlReports()) {
			try {
				/**
				 * below logic is used for sending mails
				 */
				// sendReport(reportPath + "\\HTML Results\\Summary.Html");
				Runtime.getRuntime().exec(
						"RunDLL32.EXE shell32.dll,ShellExec_RunDLL "
								+ reportPath + "\\HTML Results\\Summary.Html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (reportSettings.shouldGenerateExcelReports()) {
			try {
				/**
				 * below logic is used for sending mails
				 */
				// sendReport(reportPath + "\\Excel Results\\Summary.xls");
				Runtime.getRuntime().exec(
						"RunDLL32.EXE shell32.dll,ShellExec_RunDLL "
								+ reportPath + "\\Excel Results\\Summary.xls");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// /**
	// * Function to that will send the summary report to specified mail after
	// * execution
	// * */
	//
	// private void sendReport(String reportPath) {
	// }
	

}