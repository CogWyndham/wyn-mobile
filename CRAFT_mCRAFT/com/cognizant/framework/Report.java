package com.cognizant.framework;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Class to encapsulate all the reporting features of the framework
 * 
 * @author Cognizant
 */
public class Report {
	private static final String EXCEL_RESULTS = "Excel Results";
	private static final String HTML_RESULTS = "HTML Results";
	private static final String SCREENSHOTS = "Screenshots";
	private static final String PERFECTO_RESULTS = "Perfecto Results";
	private static final String SEETEST_RESULTS = "SeeTest Results";

	private ReportSettings reportSettings;
	private ReportTheme reportTheme;

	private int stepNumber;
	private int nStepsPassed, nStepsFailed;
	private int nTestsPassed, nTestsFailed;

	private List<ReportType> reportTypes = new ArrayList<ReportType>();

	private String testStatus;
	private String failureDescription;

	/**
	 * Constructor to initialize the Report
	 * 
	 * @param reportSettings
	 *            The {@link ReportSettings} object
	 * @param reportTheme
	 *            The {@link ReportTheme} object
	 */
	public Report(ReportSettings reportSettings, ReportTheme reportTheme) {
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;

		nStepsPassed = 0;
		nStepsFailed = 0;
		testStatus = "Passed";
	}

	/**
	 * Function to get the current status of the test being executed
	 * 
	 * @return the current status of the test being executed
	 */
	public String getTestStatus() {
		return testStatus;
	}

	/**
	 * Function to get the description of any failure that may occur during the
	 * script execution
	 * 
	 * @return The failure description (relevant only if the test fails)
	 */
	public String getFailureDescription() {
		return failureDescription;
	}

	public void initialize() {

		if (reportSettings.shouldGenerateExcelReports()) {
			new File(reportSettings.getReportPath() + Util.getFileSeparator()
					+ EXCEL_RESULTS).mkdir();

			ExcelReport excelReport = new ExcelReport(reportSettings,
					reportTheme);
			reportTypes.add(excelReport);
		}

		if (reportSettings.shouldGenerateHtmlReports()) {
			new File(reportSettings.getReportPath() + Util.getFileSeparator()
					+ HTML_RESULTS).mkdir();

			HtmlReport htmlReport = new HtmlReport(reportSettings, reportTheme);
			reportTypes.add(htmlReport);
		}

		if (reportSettings.shouldGeneratePerfectoReports()) {
			new File(reportSettings.getReportPath() + Util.getFileSeparator()
					+ PERFECTO_RESULTS).mkdir();
		}

		if (reportSettings.shouldGenerateSeeTestReports()) {
			new File(reportSettings.getReportPath() + Util.getFileSeparator()
					+ SEETEST_RESULTS).mkdir();
		}

		new File(reportSettings.getReportPath() + Util.getFileSeparator()
				+ SCREENSHOTS).mkdir();
	}

	/**
	 * Function to create a sub-folder within the Results folder
	 * 
	 * @param subFolderName
	 *            The name of the sub-folder to be created
	 * @return The {@link File} object representing the newly created sub-folder
	 */
	public File createResultsSubFolder(String subFolderName) {
		File resultsSubFolder = new File(reportSettings.getReportPath()
				+ Util.getFileSeparator() + subFolderName);
		resultsSubFolder.mkdir();
		return resultsSubFolder;
	}

	/* TEST LOG FUNCTIONS */

	/**
	 * Function to initialize the test log
	 */
	public void initializeTestLog() {
		if ("".equals(reportSettings.getReportName())) {
			throw new FrameworkException("The report name cannot be empty!");
		}

		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).initializeTestLog();
		}
	}

	/**
	 * Function to add a heading to the test log
	 * 
	 * @param heading
	 *            The heading to be added
	 */
	public void addTestLogHeading(String heading) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogHeading(heading);
		}
	}

	/**
	 * Function to add sub-headings to the test log (4 sub-headings present per
	 * test log row)
	 * 
	 * @param subHeading1
	 *            The first sub-heading to be added
	 * @param subHeading2
	 *            The second sub-heading to be added
	 * @param subHeading3
	 *            The third sub-heading to be added
	 * @param subHeading4
	 *            The fourth sub-heading to be added
	 */
	public void addTestLogSubHeading(String subHeading1, String subHeading2,
			String subHeading3, String subHeading4) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSubHeading(subHeading1, subHeading2,
					subHeading3, subHeading4);
		}
	}

	/**
	 * Function to add the overall table headings to the test log (should be
	 * called first before adding the actual content into the test log; headings
	 * and sub-heading should be added before this)
	 */
	public void addTestLogTableHeadings() {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogTableHeadings();
		}
	}

	/**
	 * Function to add a section to the test log
	 * 
	 * @param section
	 *            The section to be added
	 */
	public void addTestLogSection(String section) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSection(section);
		}

		stepNumber = 1;
	}

	/**
	 * Function to add a sub-section to the test log (should be called only
	 * within a previously created section)
	 * 
	 * @param subSection
	 *            The sub-section to be added
	 */
	public void addTestLogSubSection(String subSection) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSubSection(subSection);
		}
	}

	/**
	 * Function to update the test log with the details of a particular test
	 * step
	 * 
	 * @param stepName
	 *            The test step name
	 * @param stepDescription
	 *            The description of what the test step does
	 * @param stepStatus
	 *            The status of the test step
	 */
	public void updateTestLog(String stepName, String stepDescription,
			Status stepStatus) {
		handleStepInvolvingPassOrFail(stepDescription, stepStatus);

		if (stepStatus.ordinal() <= reportSettings.getLogLevel()) {
			String screenshotName = handleStepInvolvingScreenshot(stepName,
					stepStatus);

			for (int i = 0; i < reportTypes.size(); i++) {
				reportTypes.get(i).updateTestLog(Integer.toString(stepNumber),
						stepName, stepDescription, stepStatus, screenshotName);
			}

			stepNumber++;
		}
	}

	private void handleStepInvolvingPassOrFail(String stepDescription,
			Status stepStatus) {
		if (stepStatus.equals(Status.FAIL)) {
			testStatus = "Failed";

			if (failureDescription == null) {
				failureDescription = stepDescription;
			} else {
				failureDescription = failureDescription + "; "
						+ stepDescription;
			}

			nStepsFailed++;
		} else if (stepStatus.equals(Status.PASS)) {
			nStepsPassed++;
		}
	}

	private String handleStepInvolvingScreenshot(String stepName,
			Status stepStatus) {
		String screenshotName = reportSettings.getReportName()
				+ "_"
				+ Util.getCurrentFormattedTime(
						reportSettings.getDateFormatString()).replace(" ", "_")
						.replace(":", "-") + "_" + stepName.replace(" ", "_")
				+ ".png";

		if ((stepStatus.equals(Status.FAIL) && reportSettings
				.shouldTakeScreenshotFailedStep())
				|| (stepStatus.equals(Status.PASS) && reportSettings
						.shouldTakeScreenshotPassedStep())
				|| stepStatus.equals(Status.SCREENSHOT)) {

			String screenshotPath = reportSettings.getReportPath()
					+ Util.getFileSeparator() + SCREENSHOTS
					+ Util.getFileSeparator() + screenshotName;
			if (screenshotPath.length() > 256) { // Max char limit for Windows
													// filenames
				screenshotPath = screenshotPath.substring(0, 256);
			}

			takeScreenshot(screenshotPath);
		}

		return screenshotName;
	}

	/**
	 * Function to take a screenshot
	 * 
	 * @param screenshotPath
	 *            The path where the screenshot should be saved
	 */
	protected void takeScreenshot(String screenshotPath) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Rectangle rectangle = new Rectangle(0, 0, screenSize.width,
				screenSize.height);
		Robot robot;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while creating Robot object (for taking screenshot)");
		}

		BufferedImage screenshotImage = robot.createScreenCapture(rectangle);
		File screenshotFile = new File(screenshotPath);

		try {
			ImageIO.write(screenshotImage, "jpg", screenshotFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while writing screenshot to .jpg file");
		}
	}

	/**
	 * Function to add a footer to the test log (The footer format is
	 * pre-defined - it contains the execution time and the number of
	 * passed/failed steps)
	 * 
	 * @param executionTime
	 *            The time taken to execute the test case
	 */
	public void addTestLogFooter(String executionTime) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogFooter(executionTime, nStepsPassed,
					nStepsFailed);
		}
	}

	/**
	 * Function to consolidate all screenshots into a Word document
	 */
	public void consolidateScreenshotsInWordDoc() {
		String screenshotsConsolidatedFolderPath = reportSettings
				.getReportPath()
				+ Util.getFileSeparator()
				+ "Screenshots (Consolidated)";
		new File(screenshotsConsolidatedFolderPath).mkdir();

		WordDocumentManager documentManager = new WordDocumentManager(
				screenshotsConsolidatedFolderPath,
				reportSettings.getReportName());

		String screenshotsFolderPath = reportSettings.getReportPath()
				+ Util.getFileSeparator() + SCREENSHOTS;
		File screenshotsFolder = new File(screenshotsFolderPath);

		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String fileName) {
				return fileName.contains(reportSettings.getReportName());
			}
		};

		File[] screenshots = screenshotsFolder.listFiles(filenameFilter);
		if (screenshots != null && screenshots.length > 0) {
			documentManager.createDocument();

			for (File screenshot : screenshots) {
				documentManager.addPicture(screenshot);
			}
		}
	}

	/**
	 * Function to consolidate all screenshots into a PDF document
	 * 
	 * @param isMobile
	 */
	public void consolidateScreenshotsInPDF(boolean isMobile) {
		String screenshotsConsolidatedFolderPath = reportSettings
				.getReportPath()
				+ Util.getFileSeparator()
				+ "Screenshots (Consolidated)";
		new File(screenshotsConsolidatedFolderPath).mkdir();

		PDFDocumentManager pdfDocument = new PDFDocumentManager(
				screenshotsConsolidatedFolderPath,
				reportSettings.getReportName());

		String screenshotsFolderPath = reportSettings.getReportPath()
				+ Util.getFileSeparator() + SCREENSHOTS;
		File screenshotsFolder = new File(screenshotsFolderPath);

		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String fileName) {
				return fileName.contains(reportSettings.getReportName());
			}
		};

		File[] screenshots = screenshotsFolder.listFiles(filenameFilter);
		if (screenshots != null && screenshots.length > 0) {
			pdfDocument.createDocument(screenshots, isMobile);
		}

	}

	/* RESULT SUMMARY FUNCTIONS */

	/**
	 * Function to initialize the result summary
	 */
	public void initializeResultSummary() {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).initializeResultSummary();
		}
	}

	/**
	 * Function to add a heading to the result summary
	 * 
	 * @param heading
	 *            The heading to be added
	 */
	public void addResultSummaryHeading(String heading) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryHeading(heading);
		}
	}

	/**
	 * Function to add sub-headings to the result summary (4 sub-headings
	 * present per result summary row)
	 * 
	 * @param subHeading1
	 *            The first sub-heading to be added
	 * @param subHeading2
	 *            The second sub-heading to be added
	 * @param subHeading3
	 *            The third sub-heading to be added
	 * @param subHeading4
	 *            The fourth sub-heading to be added
	 */
	public void addResultSummarySubHeading(String subHeading1,
			String subHeading2, String subHeading3, String subHeading4) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummarySubHeading(subHeading1,
					subHeading2, subHeading3, subHeading4);
		}
	}

	/**
	 * Function to add the overall table headings to the result summary
	 */
	public void addResultSummaryTableHeadings() {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryTableHeadings();
		}
	}

	/**
	 * Function to update the results summary with the status of the test
	 * instance which was executed
	 * 
	 * @param testParameters
	 *            The {@link TestParameters} object containing the details of
	 *            the test instance which was executed
	 * @param testReportName
	 *            The name of the test report file corresponding to the test
	 *            instance
	 * @param executionTime
	 *            The time taken to execute the test instance
	 * @param testStatus
	 *            The Pass/Fail status of the test instance
	 */
	public synchronized void updateResultSummary(TestParameters testParameters,
			String testReportName, String executionTime, String testStatus) {
		if ("failed".equalsIgnoreCase(testStatus)) {
			nTestsFailed++;
		} else if ("passed".equalsIgnoreCase(testStatus)) {
			nTestsPassed++;
		} else if ("aborted".equalsIgnoreCase(testStatus)) {
			reportSettings.setLinkTestLogsToSummary(false);
		}

		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).updateResultSummary(testParameters,
					testReportName, executionTime, testStatus);
		}
	}

	/**
	 * Function to add a footer to the result summary (The footer format is
	 * pre-defined - it contains the total execution time and the number of
	 * passed/failed tests)
	 * 
	 * @param totalExecutionTime
	 *            The total time taken to execute all the test cases
	 */
	public void addResultSummaryFooter(String totalExecutionTime) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryFooter(totalExecutionTime,
					nTestsPassed, nTestsFailed);
		}
	}
}