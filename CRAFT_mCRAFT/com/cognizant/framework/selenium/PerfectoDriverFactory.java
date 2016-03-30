package com.cognizant.framework.selenium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Settings;
import com.perfectomobile.httpclient.MediaType;
import com.perfectomobile.httpclient.utils.FileUtils;
import com.perfectomobile.selenium.MobileDriver;
import com.perfectomobile.selenium.api.IMobileDevice;
import com.perfectomobile.selenium.api.IMobileWebDriver;
import com.perfectomobile.selenium.options.MobileBrowserType;

public class PerfectoDriverFactory {

	private static Properties mobileProperties;
	public static MobileDriver mobileDriverPrefecto;

	private PerfectoDriverFactory() {
		// To prevent external instantiation of this class
	}

	private static URL getUrl(String remoteUrl) {
		URL url;
		try {
			url = new URL(remoteUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"The specified remote URL is malformed");
		}
		return url;
	}

	/**
	 * Function to return the Perfecto MobileCloud {@link RemoteWebDriver}
	 * object based on the parameters passed
	 * 
	 * @param deviceId
	 *            The ID of the Perfecto MobileCloud device to be used for the
	 *            test execution
	 * @param browser
	 *            The {@link Browser} to be used for the test execution
	 * @param remoteUrl
	 *            The Perfecto MobileCloud URL to be used for the test execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getPerfectoRemoteWebDriver(String deviceId,
			Browser browser, String remoteUrl) {
		DesiredCapabilities desiredCapabilities = getPerfectoExecutionCapabilities(browser);
		desiredCapabilities.setCapability("deviceName", deviceId);

		URL url = getUrl(remoteUrl);

		return new RemoteWebDriver(url, desiredCapabilities);
	}

	private static DesiredCapabilities getPerfectoExecutionCapabilities(
			Browser browser) {
		validatePerfectoSupports(browser);

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setBrowserName(browser.getValue());
		desiredCapabilities.setPlatform(Platform.ANY);
		desiredCapabilities.setJavascriptEnabled(true); // Pre-requisite for
														// remote execution

		mobileProperties = Settings.getMobilePropertiesInstance();
		desiredCapabilities.setCapability("user",
				mobileProperties.getProperty("PerfectoUser"));
		desiredCapabilities.setCapability("password",
				mobileProperties.getProperty("PerfectoPassword"));

		return desiredCapabilities;
	}

	private static void validatePerfectoSupports(Browser browser) {
		switch (browser) {
		case INTERNET_EXPLORER:
		case FIREFOX:
		case HTML_UNIT:
		case OPERA:
			throw new FrameworkException("The browser " + browser.toString()
					+ " is not supported on the Perfecto MobileCloud");

		default:
			break;
		}
	}

	/**
	 * Function to return the Perfecto MobileCloud {@link RemoteWebDriver}
	 * object based on the parameters passed
	 * 
	 * @param platformName
	 *            The device platform to be used for the test execution (iOS,
	 *            Android, etc.)
	 * @param platformVersion
	 *            The device platform version to be used for the test execution
	 * @param browser
	 *            The {@link Browser} to be used for the test execution
	 * @param remoteUrl
	 *            The Perfecto MobileCloud URL to be used for the test execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getPerfectoRemoteWebDriverByDevicePlatform(
			String deviceId, String osVersionVersion, Browser browser,
			String remoteUrl, MobileExecutionPlatform executionPlatform) {
		String platformName = "";
		if (executionPlatform.equals("WEB_ANDROID")) {
			platformName = "Android";
		} else if (executionPlatform.equals("WEB_IOS")) {
			platformName = "ios";
		}
		DesiredCapabilities desiredCapabilities = getPerfectoExecutionCapabilities(browser);
		desiredCapabilities.setBrowserName(browser.getValue());
		desiredCapabilities.setCapability("platformName", platformName);
		desiredCapabilities.setCapability("platformVersion", osVersionVersion);

		URL url = getUrl(remoteUrl);

		return new RemoteWebDriver(url, desiredCapabilities);
	}

	/**
	 * Function to return the Perfecto MobileCloud {@link RemoteWebDriver}
	 * object based on the parameters passed
	 * 
	 * @param manufacturer
	 *            The manufacturer of the device to be used for the test
	 *            execution (Samsung, Apple, etc.)
	 * @param model
	 *            The device model to be used for the test execution (Galaxy S6,
	 *            iPad Air, etc.)
	 * @param browser
	 *            The {@link Browser} to be used for the test execution
	 * @param remoteUrl
	 *            The Perfecto MobileCloud URL to be used for the test execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getPerfectoRemoteWebDriverByDeviceModel(
			String manufacturer, String model, Browser browser, String remoteUrl) {
		DesiredCapabilities desiredCapabilities = getPerfectoExecutionCapabilities(browser);
		desiredCapabilities.setCapability("manufacturer", manufacturer);
		desiredCapabilities.setCapability("model", model);

		URL url = getUrl(remoteUrl);

		return new RemoteWebDriver(url, desiredCapabilities);
	}

	public static IMobileWebDriver getPerfectoDefaultDriver(
			MobileExecutionPlatform executionPlatform, String deviceName,
			String androidIdentifier, String iosIdentifier, Browser browser) {
		mobileProperties = Settings.getMobilePropertiesInstance();
		IMobileWebDriver perfectodriver = null;

		try {

			MobileDriver driver = launchPerfectoDevice(deviceName);
			//PerfectoDriverFactory d = new PerfectoDriverFactory();
			mobileDriverPrefecto = driver;
			switch (executionPlatform) {

			case ANDROID:

				driver.getDevice(deviceName).getNativeDriver(androidIdentifier)
						.open();
				perfectodriver = driver.getDevice(deviceName).getNativeDriver();
				break;
			case IOS:
				driver.getDevice(deviceName).getNativeDriver(iosIdentifier)
						.open();
				perfectodriver = driver.getDevice(deviceName).getNativeDriver();
				break;
			case WEB_ANDROID:
				driver.getDevice(deviceName).getDOMDriver(
						MobileBrowserType.CHROME);
				perfectodriver = driver.getDevice(deviceName).getDOMDriver();
				break;
			case WEB_IOS:
				driver.getDevice(deviceName).getDOMDriver(
						MobileBrowserType.SAFARI);
				perfectodriver = driver.getDevice(deviceName).getDOMDriver();
				break;
			default:
				break;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new FrameworkException(
					"Failed to initialize Perfecto MobileDriver, please check the perfecto details");
		}

		return perfectodriver;
	}

	public static MobileDriver launchPerfectoDevice(String deviceName) {
		MobileDriver driver = null;
		try {
			driver = new MobileDriver(mobileProperties.getProperty("PerfectoHostDefault"),
					mobileProperties.getProperty("PerfectoUser"),
					mobileProperties.getProperty("PerfectoPassword"));

			IMobileDevice device = driver.getDevice(deviceName);
			device.open();
		} catch (Exception ex) {
			throw new FrameworkException(
					"Failed to initialize Perfecto MobileDriver, please check the perfecto details");
		}
		return driver;
	}
	
	@SuppressWarnings("rawtypes")
	public static AppiumDriver getPerfectoAppiumDriver(
			MobileExecutionPlatform executionPlatform, String deviceName,
			String perfectoURL) {

		AppiumDriver driver = null;
		mobileProperties = Settings.getMobilePropertiesInstance();

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("user",
				mobileProperties.getProperty("PerfectoUser"));
		desiredCapabilities.setCapability("password",
				mobileProperties.getProperty("PerfectoPassword"));
		try {
			switch (executionPlatform) {

			case ANDROID:

				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("deviceName", deviceName);
				desiredCapabilities.setCapability("appPackage",
						mobileProperties.getProperty("Application_Package_Name"));
				desiredCapabilities
						.setCapability("appActivity", mobileProperties
								.getProperty("Application_MainActivity_Name"));
				// desiredCapabilities.setCapability("app",
				// "PUBLIC:appium/apiDemos.apk");
				try {
					driver = new AndroidDriver(new URL(perfectoURL),
							desiredCapabilities);
				} catch (MalformedURLException e) {
					throw new FrameworkException(
							"The android driver invokation has problem, please re-check the capabilities and check the perfecto details URL, Username and Password ");
				}

				break;

			case IOS:

				desiredCapabilities.setCapability("platformName", "ios");
				desiredCapabilities.setCapability("deviceName", deviceName);
				desiredCapabilities.setCapability("newCommandTimeout", 120);
				desiredCapabilities.setCapability("bundleId", mobileProperties.getProperty("PerfecttoIosBundleID"));
//				 desiredCapabilities.setCapability("app",
//				 "PUBLIC:appium/apiDemos.ipa");

				try {
					driver = new IOSDriver(new URL(perfectoURL),
							desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The IOS driver invokation has problem, please re-check the capabilities and check the perfecto details URL, Username and Password ");
				}
				break;

			case WEB_ANDROID:

				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("deviceName", deviceName);
				//desiredCapabilities.setCapability("automationName", "Appium");
				desiredCapabilities.setCapability("browserName", "Chrome");

				try {
					driver = new AndroidDriver(new URL(perfectoURL),
							desiredCapabilities);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The android driver/browser invokation has problem, please re-check the capabilities and check the perfecto details URL, Username and Password ");
				}
				break;

			case WEB_IOS:

				desiredCapabilities.setCapability("platformName", "ios");
				desiredCapabilities.setCapability("deviceName", deviceName);
				desiredCapabilities.setCapability("automationName", "Appium");
				desiredCapabilities.setCapability("browserName", "Safari");

				try {
					driver = new IOSDriver(new URL(perfectoURL),
							desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The IOS driver invokation/browser has problem, please re-check the capabilities and check the perfecto details URL, Username and Password ");
				}
				break;

			default:
				throw new FrameworkException("Unhandled ExecutionMode!");

			}
		} catch (Exception ex) {
			throw new FrameworkException(
					"The perfecto appium driver invocation created a problem , please check the capabilities");
		}
		return driver;

	}

	public static WebDriver getPerfectoRemoteWebDriver(
			MobileExecutionPlatform executionPlatform, String deviceName,
			String perfectoURL, Browser browser) {

		WebDriver driver = null;
		mobileProperties = Settings.getMobilePropertiesInstance();

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("user",
				mobileProperties.getProperty("PerfectoUser"));
		desiredCapabilities.setCapability("password",
				mobileProperties.getProperty("PerfectoPassword"));
		try {
			switch (executionPlatform) {

			case ANDROID:

				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("deviceName", deviceName);
				desiredCapabilities.setCapability("appPackage",
						mobileProperties.getProperty("Application_Package_Name"));
				desiredCapabilities
						.setCapability("appActivity", mobileProperties
								.getProperty("Application_MainActivity_Name"));
				try {
					driver = new RemoteWebDriver(new URL(perfectoURL),
							desiredCapabilities);
				} catch (MalformedURLException e) {
				
					e.printStackTrace();
					throw new FrameworkException(
							"The android driver invokation has problem, please re-check the capabilities");
				}

				break;

			case IOS:

				desiredCapabilities.setCapability("platformName", "ios");
				desiredCapabilities.setCapability("deviceName", deviceName);
				desiredCapabilities.setCapability("newCommandTimeout", 120);

				try {
					driver = new RemoteWebDriver(new URL(perfectoURL),
							desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The IOS driver invokation has problem, please re-check the capabilities");
				}
				break;

			case WEB_ANDROID:

				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("deviceName", deviceName);
				desiredCapabilities.setCapability("browserName", "Chrome");
				try {
					driver = new RemoteWebDriver(new URL(perfectoURL),
							desiredCapabilities);
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
					throw new FrameworkException(
							"The android driver/browser invokation has problem, please check the capabilities");
				}
				break;

			case WEB_IOS:

				desiredCapabilities.setCapability("platformName", "ios");
				desiredCapabilities.setCapability("deviceName", deviceName);
				desiredCapabilities.setCapability("automationName", "Appium");
				desiredCapabilities.setCapability("browserName", "Safari");

				try {
					driver = new RemoteWebDriver(new URL(perfectoURL),
							desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The IOS driver/browser invokation has problem, please check the capabilities");
				}
				break;

			default:
				throw new FrameworkException("Unhandled ExecutionMode!");

			}
		} catch (Exception ex) {
			throw new FrameworkException(
					"Failed to launch RemoteWebDriver for Perfecto, please check the Perfecto details");
		}

		return driver;

	}

	public static void uploadMedia(String localPath, String perfectoPath,
			MobileExecutionPlatform executionPlatform) {
		try {
			MobileDriver driver = new MobileDriver(
					mobileProperties.getProperty("PerfectoHost"),
					mobileProperties.getProperty("PerfectoUser"),
					mobileProperties.getProperty("PerfectoPassword"));
			File file = new File(localPath);
			if (executionPlatform.toString() == "IOS") {
				String repositoryKeyApple = perfectoPath;
				driver.uploadMedia(repositoryKeyApple, file);

			} else if (executionPlatform.toString() == "ANDROID") {
				// Upload APK file from my folder to repository
				String repositoryKeyAndroid = perfectoPath;
				driver.uploadMedia(repositoryKeyAndroid, file);

			}
		} catch (Exception ex) {
			throw new FrameworkException("Failed to upload file in Perfecto");
		}
	}

	public static void downloadReport(MobileDriver driver, String pdfReport) {
		try{
		InputStream reportStream = driver.downloadReport(MediaType.PDF);
		if (reportStream != null) {
			File reportFile = new File(pdfReport);
			try {
				FileUtils.write(reportStream, reportFile);
			} catch (IOException e) {
		
				e.printStackTrace();
			}

		}
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw new FrameworkException(
					"Failed to download PDF report for Perfecto");
		}
	}

}
