package com.cognizant.framework.selenium;

//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Settings;

public class MobileLabsDriverFactory {

	protected static Properties mobileProperties;
	static DesiredCapabilities desiredCapabilities;
	public static String forwardport, uploadcommand, connectcommand, FilePath,
			sendport, AppName, ReleaseCommand, DeviceName = null;

	private MobileLabsDriverFactory() {

	}

	@SuppressWarnings("rawtypes")
	public static WebDriver getMobileLabsDriver(
			MobileExecutionPlatform executionPlatform, String deviceName,
			String appiumURL,String version) {

		AppiumDriver driver = null;
		mobileProperties = Settings.getMobilePropertiesInstance();
		switch (executionPlatform) {

		case IOS:

			desiredCapabilities.setCapability("platformName", "ios");
			desiredCapabilities.setCapability("platformVersion", version);
			desiredCapabilities.setCapability("deviceName", deviceName);
			// desiredCapabilities.setCapability("udid",deviceName);
//			desiredCapabilities.setCapability("app",
//					properties.getProperty("iPhoneApplicationPath"));
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
		case WEB_ANDROID:
		case ANDROID:
			System.out.println("Not Compatible for the selected Platform "
					+ executionPlatform);
			// Runtime runtime = Runtime.getRuntime();
			// URL url;
			// if
			// (executionPlatform.equals(MobileExecutionPlatform.WEB_ANDROID)) {
			// DeviceName = "\"" + deviceName + "\"";
			// sendport = mobileProperties.getProperty("AndroidPort");
			// FilePath = mobileProperties.getProperty("ApplicationPath");
			// AppName = "\"" + "WebDriver" + "\""; // Name of the application
			// // displayed on DC Web
			// // UI
			// desiredCapabilities = DesiredCapabilities.android();
			// } else {
			//
			// DeviceName = "\"" + deviceName + "\"";
			// sendport = mobileProperties.getProperty("AndroidPort");
			// FilePath = mobileProperties.getProperty("ApplicationPath");
			// AppName = "\"" + "iWebDriver" + "\""; // Name of the application
			// // displayed on DC Web
			// // UI
			// desiredCapabilities = DesiredCapabilities.ipad();
			// }
			//
			// forwardport = mobileProperties.getProperty("DownloadPath")
			// + "MobileLabs.DeviceConnect.Cli.exe "
			// + mobileProperties.getProperty("HostIp") + " "
			// + mobileProperties.getProperty("UseraName") + " "
			// + mobileProperties.getProperty("Password") + " -device "
			// + DeviceName + " -forward " + availablePort + " "
			// + sendport;
			//
			// // /**********************************upload command to upload
			// the
			// // OS specific
			// // application***********************************************///
			// uploadcommand = mobileProperties.getProperty("DownloadPath")
			// + "MobileLabs.DeviceConnect.Cli.exe "
			// + mobileProperties.getProperty("HostIp") + " "
			// + mobileProperties.getProperty("UseraName") + " "
			// + mobileProperties.getProperty("Password") + " -upload "
			// + FilePath;
			// // Runtime runtime = Runtime.getRuntime();
			// try {
			// runtime.exec("cmd /c " + uploadcommand).getInputStream();
			//
			// runtime.exec("cmd /c " + forwardport).getInputStream();
			//
			// // /**********************************Connect to device with
			// // uploaded
			// // application***********************************************/
			// connectcommand = mobileProperties.getProperty("DownloadPath")
			// + "MobileLabs.DeviceConnect.Cli.exe " + ""
			// + mobileProperties.getProperty("HostIp") + " "
			// + mobileProperties.getProperty("UseraName") + " "
			// + mobileProperties.getProperty("Password")
			// + " -device " + DeviceName + " -orientation "
			// + mobileProperties.getProperty("Oreintation")
			// + " -scale 25 -retain -install " + AppName
			// + " -autoconnect " + AppName;
			// runtime.exec("cmd /c " + connectcommand).getInputStream();
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// }
			// try {
			// Thread.sleep(50000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			//
			// // URL url;
			// try {
			// url = new URL("http://localhost:" + availablePort + "/wd/hub");
			//
			// driver = new RemoteWebDriver(url, desiredCapabilities);
			// } catch (MalformedURLException e) {
			// e.printStackTrace();
			// }

			break;

		default:
			break;
		}

		return driver;

	}
}
