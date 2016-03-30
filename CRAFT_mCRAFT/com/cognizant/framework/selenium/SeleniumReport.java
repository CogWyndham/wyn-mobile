package com.cognizant.framework.selenium;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Report;
import com.cognizant.framework.ReportSettings;
import com.cognizant.framework.ReportTheme;
import com.experitest.client.Client;
import com.perfectomobile.selenium.MobileDriver;
import com.perfectomobile.selenium.options.MobileScreenshotOptions;

/**
 * Class which extends the {@link Report} class with a Selenium specific
 * override for taking screenshots
 * 
 * @author Cognizant
 */
public class SeleniumReport extends Report {

	private CraftDriver driver;
	private MobileDriver mobileDriver;
	private final SeleniumTestParameters testParameters;
	private Client client;

	/**
	 * Constructor to initialize the Report object
	 * 
	 * @param reportSettings
	 *            The {@link ReportSettings} object
	 * @param reportTheme
	 *            The {@link ReportTheme} object
	 * @param testParameters
	 */
	public SeleniumReport(ReportSettings reportSettings,
			ReportTheme reportTheme, SeleniumTestParameters testParameters) {
		super(reportSettings, reportTheme);
		this.testParameters = testParameters;
	}

	/**
	 * Function to set the {@link CraftDriver} object
	 * 
	 * @param driver
	 *            The {@link CraftDriver} object
	 */

	public void setDriver(CraftDriver driver) {
		this.driver = driver;
	}

	public void setMobileDriver(MobileDriver mobileDriver) {
		this.mobileDriver = mobileDriver;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	protected void takeScreenshot(String screenshotPath) {
		if (driver == null) {
			throw new FrameworkException("Report.driver is not initialized!");
		}
		File scrFile = null;
		switch (testParameters.getExecutionMode()) {
		case LOCAL:
		case REMOTE:
		case LOCAL_EMULATED_DEVICE:
		case REMOTE_EMULATED_DEVICE:
		case GRID:
		case MOBILE:
		case MOBILELABS:

			try {

				if ("HtmlUnitDriver".equals(driver.getWebDriver().getClass()
						.getSimpleName())
						|| "class org.openqa.selenium.htmlunit.HtmlUnitDriver"
								.equals(driver.getWebDriver().getClass()
										.getGenericSuperclass().toString())) {
					return; // Screenshots not supported in headless mode
				}

				if ("RemoteWebDriver".equals(driver.getWebDriver().getClass()
						.getSimpleName())) {
					Capabilities capabilities = ((RemoteWebDriver) driver
							.getWebDriver()).getCapabilities();
					if ("htmlunit".equals(capabilities.getBrowserName())) {
						return; // Screenshots not supported in headless mode
					}
					WebDriver augmentedDriver = new Augmenter().augment(driver
							.getWebDriver());
					scrFile = ((TakesScreenshot) augmentedDriver)
							.getScreenshotAs(OutputType.FILE);
				} else {
					scrFile = ((TakesScreenshot) driver.getWebDriver())
							.getScreenshotAs(OutputType.FILE);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new FrameworkException(
						"Error while capturing the screenshot");
			}

			break;
		case PERFECTO:
			try {

				if (testParameters.getMobileToolName().toString()
						.equalsIgnoreCase("DEFAULT")) {
					String deviceName = testParameters.getDeviceName();
					MobileScreenshotOptions screenshotOptions = new MobileScreenshotOptions();
					scrFile = mobileDriver
							.getDevice(deviceName)
							.getScreenshotAs(OutputType.FILE, screenshotOptions);

				} else {
					if ("HtmlUnitDriver".equals(driver.getWebDriver()
							.getClass().getSimpleName())
							|| "class org.openqa.selenium.htmlunit.HtmlUnitDriver"
									.equals(driver.getWebDriver().getClass()
											.getGenericSuperclass().toString())) {
						return; // Screenshots not supported in headless mode
					}

					if ("RemoteWebDriver".equals(driver.getWebDriver()
							.getClass().getSimpleName())) {
						Capabilities capabilities = ((RemoteWebDriver) driver
								.getWebDriver()).getCapabilities();
						if ("htmlunit".equals(capabilities.getBrowserName())) {
							return; // Screenshots not supported in headless
									// mode
						}
						WebDriver augmentedDriver = new Augmenter()
								.augment(driver.getWebDriver());
						scrFile = ((TakesScreenshot) augmentedDriver)
								.getScreenshotAs(OutputType.FILE);
					} else {
						scrFile = ((TakesScreenshot) driver.getWebDriver())
								.getScreenshotAs(OutputType.FILE);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			break;
		case SEETEST:
			scrFile = new File(client.capture());
			int i = 0;
			while (!scrFile.exists()) {
				try {
					Thread.sleep(1000);
					i++;
					if (i > 30) {
						break;
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}

			break;
		}

		try {
			FileUtils.copyFile(scrFile, new File(screenshotPath), true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while writing screenshot to file");
		}

	}

}