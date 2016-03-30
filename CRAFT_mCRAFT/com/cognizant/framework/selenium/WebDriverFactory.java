package com.cognizant.framework.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.*;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Settings;


/**
 * Factory class for creating the {@link WebDriver} object as required
 * @author Cognizant
 */
public class WebDriverFactory {
	private static Properties properties;
	
	private WebDriverFactory() {
		// To prevent external instantiation of this class
	}
	/**
	 * Function to return the appropriate {@link WebDriver} object based on the parameters passed
	 * @param browser The {@link Browser} to be used for the test execution
	 * @return The corresponding {@link WebDriver} object
	 */
	public static WebDriver getWebDriver(Browser browser) {
		WebDriver driver = null;
		properties = Settings.getInstance();
		boolean proxyRequired =
				Boolean.parseBoolean(properties.getProperty("ProxyRequired"));
		
		switch(browser) {
		case CHROME:
			// Takes the system proxy settings automatically
			
			System.setProperty("webdriver.chrome.driver",
									properties.getProperty("ChromeDriverPath"));
			driver = new ChromeDriver();
			break;
			
		case FIREFOX:
			// Takes the system proxy settings automatically
			
			driver = new FirefoxDriver();
			break;
			
		case GHOST_DRIVER:
			// Takes the system proxy settings automatically (I think!)
			
			System.setProperty("phantomjs.binary.path",
									properties.getProperty("PhantomJSPath"));
			driver = new PhantomJSDriver();
			break;
			
		case HTML_UNIT:
			// Does not take the system proxy settings automatically!
			
			if (proxyRequired) {
				driver = getHtmlUnitDriverWithProxy();
			} else {
				driver = new HtmlUnitDriver(true);
			}
			
			break;
			
		case INTERNET_EXPLORER:
			// Takes the system proxy settings automatically
			
			System.setProperty("webdriver.ie.driver",
									properties.getProperty("InternetExplorerDriverPath"));
			driver = new InternetExplorerDriver();
			break;
			
		case OPERA:
			// Does not take the system proxy settings automatically!
			// NTLM authentication for proxy NOT supported
			
			if (proxyRequired) {
				DesiredCapabilities desiredCapabilities = getProxyCapabilities();
				driver = new OperaDriver(desiredCapabilities);
			} else {
				driver = new OperaDriver();
			}
			
			break;
			
		case SAFARI:
			// Takes the system proxy settings automatically
			
			driver = new SafariDriver();
			break;
			
		default:
			throw new FrameworkException("Unhandled browser!");
		}
		
		return driver;
	}
	
	private static WebDriver getHtmlUnitDriverWithProxy() {
		WebDriver driver;
		boolean proxyAuthenticationRequired =
				Boolean.parseBoolean(properties.getProperty("ProxyAuthenticationRequired"));
		
		if(proxyAuthenticationRequired) {
			// NTLM authentication for proxy supported
			
			driver = new HtmlUnitDriver(true) {
			@Override
			protected WebClient modifyWebClient(WebClient client) {
				DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
				credentialsProvider.addNTLMCredentials(properties.getProperty("Username"),
														properties.getProperty("Password"),
														properties.getProperty("ProxyHost"),
														Integer.parseInt(properties.getProperty("ProxyPort")),
														"", properties.getProperty("Domain"));
				client.setCredentialsProvider(credentialsProvider);
				return client;
				}
			};
		} else {
			driver = new HtmlUnitDriver(true);
		}
		
		((HtmlUnitDriver) driver).setProxy(properties.getProperty("ProxyHost"),
									Integer.parseInt(properties.getProperty("ProxyPort")));
		return driver;
	}
	
	private static DesiredCapabilities getProxyCapabilities() {
		properties = Settings.getInstance();
		String proxyUrl = properties.getProperty("ProxyHost") + ":" +
									properties.getProperty("ProxyPort");
		
		Proxy proxy = new Proxy();
		proxy.setProxyType(ProxyType.MANUAL);
		proxy.setHttpProxy(proxyUrl);
		proxy.setFtpProxy(proxyUrl);
		proxy.setSslProxy(proxyUrl);
		
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);
		
		return desiredCapabilities;
	}
	
	/**
	 * Function to return the {@link RemoteWebDriver} object based on the parameters passed
	 * @param browser The {@link Browser} to be used for the test execution
	 * @param browserVersion The browser version to be used for the test execution
	 * @param platform The {@link Platform} to be used for the test execution
	 * @param remoteUrl The URL of the remote machine to be used for the test execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getRemoteWebDriver(Browser browser, String browserVersion,
												Platform platform, String remoteUrl) {
		// For running RemoteWebDriver tests in Chrome and IE:
		// The ChromeDriver and IEDriver executables needs to be in the PATH of the remote machine
		// To set the executable path manually, use:
		// java -Dwebdriver.chrome.driver=/path/to/driver -jar selenium-server-standalone.jar
		// java -Dwebdriver.ie.driver=/path/to/driver -jar selenium-server-standalone.jar
		
		properties = Settings.getInstance();
		boolean proxyRequired =
				Boolean.parseBoolean(properties.getProperty("ProxyRequired"));
		
		DesiredCapabilities desiredCapabilities = null;
		if ((browser.equals(Browser.HTML_UNIT) || browser.equals(Browser.OPERA))
																&& proxyRequired) {
			desiredCapabilities = getProxyCapabilities();
		} else {
			desiredCapabilities = new DesiredCapabilities();
		}
		
		desiredCapabilities.setBrowserName(browser.getValue());
		
		if (browserVersion != null) {
			desiredCapabilities.setVersion(browserVersion);
		}
		if (platform != null) {
			desiredCapabilities.setPlatform(platform);
		}
		
		desiredCapabilities.setJavascriptEnabled(true);	// Pre-requisite for remote execution
		
		URL url = getUrl(remoteUrl);
		
		return new RemoteWebDriver(url, desiredCapabilities);
	}
	
	private static URL getUrl(String remoteUrl) {
		URL url;
		try {
			url = new URL(remoteUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified remote URL is malformed");
		}
		return url;
	}
	
	/**
	 * Function to return the {@link RemoteWebDriver} object based on the parameters passed
	 * @param browser The {@link Browser} to be used for the test execution
	 * @param remoteUrl The URL of the remote machine to be used for the test execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getRemoteWebDriver(Browser browser, String remoteUrl) {
		return getRemoteWebDriver(browser, null, null, remoteUrl);
	}
	
	/**
	 * Function to return the {@link ChromeDriver} object emulating the device specified by the user
	 * @param deviceName The name of the device to be emulated (check Chrome Dev Tools for a list of available devices)
	 * @return The corresponding {@link ChromeDriver} object
	 */
	public static WebDriver getEmulatedWebDriver(String deviceName) {
		DesiredCapabilities desiredCapabilities = getEmulatedChromeDriverCapabilities(deviceName);
		
		properties = Settings.getInstance();
		System.setProperty("webdriver.chrome.driver",
											properties.getProperty("ChromeDriverPath"));
		
		return new ChromeDriver(desiredCapabilities);
	}
	
	private static DesiredCapabilities getEmulatedChromeDriverCapabilities(String deviceName) {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", deviceName);
		
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		
		return desiredCapabilities;
	}
	
	/**
	 * Function to return the {@link RemoteWebDriver} object emulating the device specified by the user
	 * @param deviceName The name of the device to be emulated (check Chrome Dev Tools for a list of available devices)
	 * @param remoteUrl The URL of the remote machine to be used for the test execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getEmulatedRemoteWebDriver(String deviceName, String remoteUrl) {
		DesiredCapabilities desiredCapabilities = getEmulatedChromeDriverCapabilities(deviceName);
		desiredCapabilities.setJavascriptEnabled(true);	// Pre-requisite for remote execution
		
		URL url = getUrl(remoteUrl);
		
		return new RemoteWebDriver(url, desiredCapabilities);
	}
	
	/**
	 * Function to return the {@link ChromeDriver} object emulating the device attributes specified by the user
	 * @param deviceWidth The width of the device to be emulated (in pixels)
	 * @param deviceHeight The height of the device to be emulated (in pixels)
	 * @param devicePixelRatio The device's pixel ratio
	 * @param userAgent The user agent string
	 * @return The corresponding {@link ChromeDriver} object
	 */
	public static WebDriver getEmulatedWebDriver(int deviceWidth, int deviceHeight,
											float devicePixelRatio, String userAgent) {
		DesiredCapabilities desiredCapabilities =
						getEmulatedChromeDriverCapabilities(deviceWidth, deviceHeight,
															devicePixelRatio, userAgent);
		
		properties = Settings.getInstance();
		System.setProperty("webdriver.chrome.driver",
											properties.getProperty("ChromeDriverPath"));
		
		return new ChromeDriver(desiredCapabilities);
	}
	
	private static DesiredCapabilities getEmulatedChromeDriverCapabilities(
			int deviceWidth, int deviceHeight, float devicePixelRatio, String userAgent) {
		Map<String, Object> deviceMetrics = new HashMap<String, Object>();
		deviceMetrics.put("width", deviceWidth);
		deviceMetrics.put("height", deviceHeight);
		deviceMetrics.put("pixelRatio", devicePixelRatio);
		
		Map<String, Object> mobileEmulation = new HashMap<String, Object>();
		mobileEmulation.put("deviceMetrics", deviceMetrics);
		//mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
		mobileEmulation.put("userAgent", userAgent);
		
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		return desiredCapabilities;
	}
	
	/**
	 * Function to return the {@link RemoteWebDriver} object emulating the device attributes specified by the user
	 * @param deviceWidth The width of the device to be emulated (in pixels)
	 * @param deviceHeight The height of the device to be emulated (in pixels)
	 * @param devicePixelRatio The device's pixel ratio
	 * @param userAgent The user agent string
	 * @param remoteUrl The URL of the remote machine to be used for the test execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getEmulatedRemoteWebDriver(int deviceWidth, int deviceHeight,
								float devicePixelRatio, String userAgent, String remoteUrl) {
		DesiredCapabilities desiredCapabilities =
				getEmulatedChromeDriverCapabilities(deviceWidth, deviceHeight,
													devicePixelRatio, userAgent);
		desiredCapabilities.setJavascriptEnabled(true);	// Pre-requisite for remote execution
		
		URL url = getUrl(remoteUrl);
		
		return new RemoteWebDriver(url, desiredCapabilities);
	}
	
}