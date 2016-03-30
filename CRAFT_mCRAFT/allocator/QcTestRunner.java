package allocator;

import java.io.File;
import java.util.Properties;

import org.openqa.selenium.Platform;

import supportlibraries.*;

import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.Settings;
import com.cognizant.framework.selenium.*;

/**
 * Class to manage the test execution from HP Quality Center
 * 
 * @author Cognizant
 */
public class QcTestRunner {
	private static FrameworkParameters frameworkParameters = FrameworkParameters
			.getInstance();
	private static SeleniumTestParameters testParameters;

	private QcTestRunner() {
		// To prevent external instantiation of this class
	}

	/**
	 * The entry point of the test execution from HP ALM/QC <br>
	 * Exits with a value of 0 if the test passes and 1 if the test fails
	 * 
	 * @param args
	 *            Command line arguments to control the test parameters (details
	 *            below):<br>
	 *            <b>Argument 1 :</b> The absolute path where the test report is
	 *            to be stored (Mandatory)<br>
	 *            <b>Argument 2 :</b> The name of the scenario which contains
	 *            the test case to be executed (Mandatory)<br>
	 *            <b>Argument 3 :</b> The name of the test case to be executed
	 *            (Mandatory)<br>
	 *            <b>Argument 4 :</b> The name of the test instance to be
	 *            executed (Mandatory)<br>
	 *            <b>Argument 5 :</b> The description of the test case to be
	 *            executed (Optional - Specify SKIP if not required)<br>
	 *            <b>Argument 6 :</b> The iteration mode (Optional - Specify
	 *            SKIP if not required)<br>
	 *            <b>Argument 7 :</b> The start iteration - applicable only for
	 *            RUN_RANGE_OF_ITERATIONS mode (Optional - Specify SKIP if not
	 *            required)<br>
	 *            <b>Argument 8 :</b> The end iteration - applicable only for
	 *            RUN_RANGE_OF_ITERATIONS mode (Optional - Specify SKIP if not
	 *            required)<br>
	 *            <b>Argument 9 :</b> The execution mode (Optional - Specify
	 *            SKIP if not required)<br>
	 *            <b>Argument 10 :</b> The Mobile ToolName(Optional - Specify
	 *            SKIP if not required)<br>
	 *            <b>Argument 11 :</b> The Mobile Execution Platform(Optional -
	 *            Specify SKIP if not required)<br>
	 *            <b>Argument 12 :</b> The device name - applicable only for
	 *            PERFECTO or EMULATED_DEVICE execution modes (Optional -
	 *            Specify SKIP if not required)<br>
	 *            <b>Argument 13 :</b> The browser on which the test is to be
	 *            executed (Optional - Specify SKIP if not required)<br>
	 *            <b>Argument 14 :</b> The browser version (Optional - Specify
	 *            SKIP if not required)<br>
	 *            <b>Argument 15 :</b> The platform on which the test is to be
	 *            executed (Optional - Specify SKIP if not required)
	 */
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("\nError: Insufficient parameters!"
					+ "\nUsage: java allocator.QcTestRunner "
					+ "<report-path> "
					+ "<scenario-name> <test-name> <test-instance> "
					+ "<test-description*> "
					+ "<iteration-mode*> <start-iteration*> <end-iteration*> "
					+ "<execution-mode*> <device-name*> "
					+ "<browser*> <browser-version*> <platform*> "
					+ "\n\n * - Optional (specify SKIP if not required)");
			return;
		}

		setRelativePath();
		initializeTestParameters(args);

		String testStatus = driveExecutionFromQc();
		if ("passed".equalsIgnoreCase(testStatus)) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}

	private static void setRelativePath() {
		String relativePath = new File(System.getProperty("user.dir"))
				.getAbsolutePath();
		if (relativePath.contains("allocator")) {
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		frameworkParameters.setRelativePath(relativePath);
	}

	private static void initializeTestParameters(String[] args) {
		System.setProperty("ReportPath", args[0]);
		Properties properties = Settings.getInstance();
		Properties mobileProperties = Settings.getMobilePropertiesInstance();
		testParameters = new SeleniumTestParameters(args[1], args[2]);
		testParameters.setCurrentTestInstance(args[3]);
		if (args.length >= 5 && !"SKIP".equalsIgnoreCase(args[4])) {
			testParameters.setCurrentTestDescription(args[4]);
		}

		if (args.length >= 6 && !"SKIP".equalsIgnoreCase(args[5])) {
			testParameters.setIterationMode(IterationOptions.valueOf(args[5]));
		} else {
			testParameters
					.setIterationMode(IterationOptions.RUN_ALL_ITERATIONS);
		}
		if (args.length >= 7 && !"SKIP".equalsIgnoreCase(args[6])) {
			testParameters.setStartIteration(Integer.parseInt(args[6]));
		}
		if (args.length >= 8 && !"SKIP".equalsIgnoreCase(args[7])) {
			testParameters.setEndIteration(Integer.parseInt(args[7]));
		}
		if (args.length >= 9 && !"SKIP".equalsIgnoreCase(args[8])) {
			testParameters.setExecutionMode(ExecutionMode.valueOf(args[8]));
		} else {
			testParameters.setExecutionMode(ExecutionMode.valueOf(properties
					.getProperty("DefaultExecutionMode")));
		}

		if (args.length >= 10 && !"SKIP".equalsIgnoreCase(args[9])) {
			testParameters.setMobileToolName((MobileToolName.valueOf(args[9])));
		} else {
			testParameters.setMobileToolName(MobileToolName.valueOf(mobileProperties
					.getProperty("DefaultMobileToolName")));
		}

		if (args.length >= 11 && !"SKIP".equalsIgnoreCase(args[10])) {
			testParameters.setMobileExecutionPlatform((MobileExecutionPlatform
					.valueOf(args[10])));
		} else {
			testParameters.setMobileExecutionPlatform(MobileExecutionPlatform
					.valueOf(mobileProperties
							.getProperty("DefaultMobileExecutionPlatform")));
		}

		if (args.length >= 12 && !"SKIP".equalsIgnoreCase(args[11])) {
			testParameters.setDeviceName(args[11]);
		} else {
			testParameters.setDeviceName(mobileProperties
					.getProperty("DefaultDevice"));
		}

		if (args.length >= 13 && !"SKIP".equalsIgnoreCase(args[12])) {
			testParameters.setBrowser(Browser.valueOf(args[12]));
		} else {
			testParameters.setBrowser(Browser.valueOf(properties
					.getProperty("DefaultBrowser")));
		}
		
		if (args.length >= 14 && !"SKIP".equalsIgnoreCase(args[13])) {
			testParameters.setBrowserVersion(args[14]);
		}
		if (args.length >= 15 && !"SKIP".equalsIgnoreCase(args[14])) {
			testParameters.setPlatform(Platform.valueOf(args[14]));
		} else {
			testParameters.setPlatform(Platform.valueOf(properties
					.getProperty("DefaultPlatform")));
		}
	}

	private static String driveExecutionFromQc() {
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.setLinkScreenshotsToTestLog(false);
		driverScript.driveTestExecution();

		return driverScript.getTestStatus();
	}
}