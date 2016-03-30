package com.cognizant.framework.selenium;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Enumeration to represent the mode of execution
 * @author Cognizant
 */
public enum ExecutionMode {
	/**
	 * Execute on the local machine
	 */
	LOCAL,
	
	/**
	 * Execute on a remote machine 
	 */
	REMOTE,
	
	/**
	 * Execute on an emulated device on the local machine
	 */
	LOCAL_EMULATED_DEVICE,
	
	/**
	 * Execute on an emulated device on a remote machine
	 */
	REMOTE_EMULATED_DEVICE,
	
	/**
	 * Execute on a selenium grid
	 */
	GRID,
	
	/**
	 * Execute on a Perfecto MobileCloud device using {@link RemoteWebDriver}
	 */
	PERFECTO,
	
	/**
	 * Execute on a mobile device using Appium
	 */
	MOBILE,
	/**
	 * Execute on a mobile device using SeeTest
	 */
	SEETEST,
	/**
	 * Execute on a mobile device using MobileLabs
	 */
	MOBILELABS;

}