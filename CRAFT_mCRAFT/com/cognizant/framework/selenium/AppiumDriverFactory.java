package com.cognizant.framework.selenium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Settings;

public class AppiumDriverFactory {

	private static Properties mobileProperties;

	private AppiumDriverFactory() {
		// To prevent external instantiation of this class
	}

	@SuppressWarnings("rawtypes")
	public static AppiumDriver getAppiumDriver(
			MobileExecutionPlatform executionPlatform, String deviceName,
			String version, Boolean installApp, String appiumURL) {

		AppiumDriver driver = null;
		mobileProperties = Settings.getMobilePropertiesInstance();
		String appPath = installApplication(installApp);
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		try {
			switch (executionPlatform) {

			case ANDROID:

				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("deviceName", deviceName);
				// desiredCapabilities.setCapability("udid",deviceName);
				desiredCapabilities.setCapability("platformVersion", version);
				desiredCapabilities.setCapability("app", appPath);
				desiredCapabilities.setCapability("appPackage",
						mobileProperties.getProperty("Application_Package_Name"));
				desiredCapabilities
						.setCapability("appActivity", mobileProperties
								.getProperty("Application_MainActivity_Name"));
				try {
					driver = new AndroidDriver(new URL(appiumURL),
							desiredCapabilities);
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The android driver invokation has problem, please re-check the capabilities or Start Appium");
				}
				break;

			case IOS:

				desiredCapabilities.setCapability("platformName", "ios");
				desiredCapabilities.setCapability("platformVersion", version);
				desiredCapabilities.setCapability("deviceName", deviceName);
				// desiredCapabilities.setCapability("udid",deviceName);
//				desiredCapabilities.setCapability("app",
//						properties.getProperty("iPhoneApplicationPath"));
				desiredCapabilities.setCapability("bundleId", mobileProperties.getProperty("iPhoneBundleID"));
				desiredCapabilities.setCapability("newCommandTimeout", 120);
				try {
					driver = new IOSDriver(new URL(appiumURL),
							desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The IOS driver invokation has problem, please re-check the capabilities or Start Appium");
				}
				break;

			case WEB_ANDROID:

				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("deviceName", deviceName);
				// desiredCapabilities.setCapability("udid",deviceName);
				desiredCapabilities.setCapability("platformVersion", version);
				desiredCapabilities.setCapability("browserName", "chrome");
				try {
					driver = new AndroidDriver(new URL(appiumURL),
							desiredCapabilities);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The android driver invokation has problem, please check the capabilities or Start Appium");
				}
				break;

			case WEB_IOS:

				desiredCapabilities.setCapability("platformName", "ios");
				desiredCapabilities.setCapability("platformVersion", version);
				desiredCapabilities.setCapability("deviceName", deviceName);
				// desiredCapabilities.setCapability("udid",deviceName);
				desiredCapabilities.setCapability("automationName", "Appium");
				desiredCapabilities.setCapability("browserName", "Safari");
				try {
					driver = new IOSDriver(new URL(appiumURL),
							desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The IOS driver invokation has problem, please check the capabilities or Start Appium");
				}
				break;

			default:
				throw new FrameworkException("Unhandled ExecutionMode!");

			}
		} catch (Exception ex) {
			throw new FrameworkException(
					"The appium driver invocation created a problem , please check the capabilities");
		}

		return driver;

	}
	
	
	public static WebDriver getAppiumRemoteWebDriver(
			MobileExecutionPlatform executionPlatform, String deviceName,
			String version, Boolean installApp, String appiumURL) {

		WebDriver driver = null;
		mobileProperties = Settings.getMobilePropertiesInstance();
		String appPath = installApplication(installApp);
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		try {
			switch (executionPlatform) {

			case ANDROID:

				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("deviceName", deviceName);
				// desiredCapabilities.setCapability("udid",deviceName);
				desiredCapabilities.setCapability("platformVersion", version);
				desiredCapabilities.setCapability("app", appPath);
				desiredCapabilities.setCapability("appPackage",
						mobileProperties.getProperty("Application_Package_Name"));
				desiredCapabilities
						.setCapability("appActivity", mobileProperties
								.getProperty("Application_MainActivity_Name"));
				try {
					driver = new RemoteWebDriver(new URL(appiumURL),
							desiredCapabilities);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The android driver invokation has problem, please re-check the capabilities or Start Appium");
				}
				break;

			case IOS:

				desiredCapabilities.setCapability("platformName", "ios");
				desiredCapabilities.setCapability("platformVersion", version);
				desiredCapabilities.setCapability("deviceName", deviceName);
				// desiredCapabilities.setCapability("udid",deviceName);
//				desiredCapabilities.setCapability("app",
//						properties.getProperty("iPhoneApplicationPath"));
				desiredCapabilities.setCapability("bundleId", mobileProperties.getProperty("iPhoneBundleID"));
				desiredCapabilities.setCapability("newCommandTimeout", 120);

				try {
					driver = new RemoteWebDriver(new URL(appiumURL),
							desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The IOS driver invokation has problem, please re-check the capabilities or Start Appium");
				}
				break;

			case WEB_ANDROID:

				desiredCapabilities.setCapability("platformName", "Android");
				desiredCapabilities.setCapability("deviceName", deviceName);
				// desiredCapabilities.setCapability("udid",deviceName);
				desiredCapabilities.setCapability("platformVersion", version);
				desiredCapabilities.setCapability("browserName", "Chrome");

				try {
					driver = new RemoteWebDriver(new URL(appiumURL),
							desiredCapabilities);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The android driver invokation has problem, please check the capabilities or Start Appium");
				}
				break;

			case WEB_IOS:

				desiredCapabilities.setCapability("platformName", "ios");
				desiredCapabilities.setCapability("platformVersion", version);
				desiredCapabilities.setCapability("deviceName", deviceName);
				// desiredCapabilities.setCapability("udid",deviceName);
				desiredCapabilities.setCapability("automationName", "Appium");
				desiredCapabilities.setCapability("browserName", "Safari");
				try {
					driver = new RemoteWebDriver(new URL(appiumURL),
							desiredCapabilities);

				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"The IOS driver invokation has problem, please check the capabilities or Start Appium");
				}
				break;

			default:
				throw new FrameworkException("Unhandled ExecutionMode!");

			}
		} catch (Exception ex) {
			throw new FrameworkException(
					"The appium driver invocation created a problem , please check the capabilities");
		}

		return driver;

	}

	private static String installApplication(Boolean installApp) {
		String appPath = "";

		try {
			if (installApp) {
				File path = new File(
						mobileProperties.getProperty("AndroidApplicationPath"));
				appPath = path.getAbsolutePath();
			}
		} catch (Exception ex) {
			throw new FrameworkException(
					"Pleach the Android Application Path in Global Setting.Properties");
		}
		return appPath;
	}
}
