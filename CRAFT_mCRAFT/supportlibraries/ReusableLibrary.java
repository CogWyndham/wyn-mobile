package supportlibraries;

import java.util.Properties;

import com.cognizant.framework.CraftDataTable;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Settings;
import com.cognizant.framework.selenium.CraftDriver;
import com.cognizant.framework.selenium.SeleniumReport;
import com.cognizant.framework.selenium.WebDriverUtil;



/**
 * Abstract base class for reusable libraries created by the user
 * @author Cognizant
 */
public abstract class ReusableLibrary {
	/**
	 * The {@link CraftDataTable} object (passed from the test script)
	 */
	protected CraftDataTable dataTable;
	/**
	 * The {@link SeleniumReport} object (passed from the test script)
	 */
	protected static SeleniumReport report;
	/**
	 * The {@link CraftDriver} object
	 */
	protected static CraftDriver driver;

	protected WebDriverUtil driverUtil;
	
	/**
	 * The {@link ScriptHelper} object (required for calling one reusable library from another)
	 */
	protected ScriptHelper scriptHelper;
	
	/**
	 * The {@link Properties} object with settings loaded from the framework properties file
	 */
	protected Properties properties;
	/**
	 * The {@link FrameworkParameters} object
	 */
	protected FrameworkParameters frameworkParameters;
	 
	
	/**
	 * Constructor to initialize the {@link ScriptHelper} object and in turn the objects wrapped by it
	 * @param scriptHelper The {@link ScriptHelper} object
	 */
	public ReusableLibrary(ScriptHelper scriptHelper) {
		this.scriptHelper = scriptHelper;
		this.dataTable = scriptHelper.getDataTable();
		this.report = scriptHelper.getReport();
		this.driver = scriptHelper.getcraftDriver();
		this.driverUtil = scriptHelper.getDriverUtil();
		
		properties = Settings.getInstance();
		frameworkParameters = FrameworkParameters.getInstance();
	}
}