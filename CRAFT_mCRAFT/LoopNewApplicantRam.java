/*package businesscomponents;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.cognizant.framework.Status;
//import ru.yandex.qatools.allure.annotations.Step;
import com.thoughtworks.selenium.webdriven.commands.KeyEvent;

//import ru.yandex.qatools.allure.annotations.Attachment;
//import ru.yandex.qatools.allure.annotations.Step;
import supportlibraries.*;
//import test.java.Constants;

import org.openqa.selenium.remote.RemoteWebDriver;
*//**
 * Class for storing general purpose business components
 * 
 * @author Cognizant
 *//* 

public class LoopNewApplicant extends ReusableLibrary {
	*//**
	 * Constructor to initialize the component library
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object passed from the
	 *            {@link DriverScript}
	 *//*
	//protected RemoteWebDriver driver1;
	public LoopNewApplicant(ScriptHelper scriptHelper) {
		super(scriptHelper);
		}
	
	//Copied
	private Map<String, Object> perfectoCommand = new HashMap<>();
	protected SoftAssert softAssert = new SoftAssert();
	private boolean rotate;
	private boolean isMobile;
		
	@Test
	public void runLoopTest() throws Exception {
		try {
			createProfile();
			privacyAgreement();
			uploadResumeFromLocal();
			//uploadResumeFromGoogleDrive();
			dataVerification();
		} catch (Exception e) {e.printStackTrace(); }
		finally{testTearDown();}  }
	
	
	//@Step("Create Profile")
	public void createProfile() throws MalformedURLException, InterruptedException {
		

		//driver.get("http://wynexternalstage.loop.jobs");
		driver.get("https://wynstaging.loopapply.com/apply/70/1184765");
		
		driver.findElementnTakescreenShot(By.xpath("//span[text()='Create an account']"));
		driver.findElement(By.xpath("//span[text()='Create an account']")).click();
		
		String output;
		output = new String(new Timestamp(System.currentTimeMillis()).toString());
		output = output.replace("-", "");
		output = output.replace(" ", "_");
		output = output.replace(":", "");
		output = "wyn.ram+" + output + "@gmail.com";
		
		driver.findElement(By.xpath("//input[@id='Email']")).sendKeys(output);
		driver.findElement(By.xpath("//input[@id='Password']")).sendKeys("Wyndham1");
		driver.findElement(By.xpath("//input[@id='ConfirmPassword']")).sendKeys("Wyndham1");
		
		//driver.capture();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@value='Create Account']")).click();
		Thread.sleep(1000);
		
		report.updateTestLog("createProfile", "createProfile Success", Status.PASS);
		
	}
	
	//	@Step("Privacy Agreement")
	private void privacyAgreement() throws InterruptedException {
		Thread.sleep(1000);
		//driver.findElementnTakescreenShot(By.xpath("//input[@name='ContinueBtn']"));
		driver.findElement(By.xpath("//input[@name='ContinueBtn']")).click();
		Thread.sleep(1000);
		isElementDisplayed("//span[contains(text(),'Please accept our privacy agreement if you wish to continue')]");
		
		driver.findElement(By.xpath("//span[text()='I accept the privacy agreement']")).click();
		driver.findElement(By.xpath("//span[contains(text(),'Yes!')]")).click();
		driver.findElement(By.xpath("//input[@name='ContinueBtn']")).click();
		Thread.sleep(1000);
		report.updateTestLog("privacyAgreement", "privacyAgreement selection success", Status.PASS);
	}
	
	//@Step("Upload Resume")
	
	private void uploadResumeFromLocal() throws InterruptedException, IOException{
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[text()='Upload local file']")).click();
		Thread.sleep(1000);
		//driver.findElement(By.xpath("//span[text()='Browse']")).click();
		driver.findElement(By.xpath("//input[@data-buttontext='Choose file']")).click();
		
		
		
         Runtime.getRuntime().exec("C:/Mobile testing/FileUpload1.exe");
		
		Thread.sleep(5000);
		
		driver.findElement(By.xpath("//input[@value ='Upload']")).click();
		Thread.sleep(10000);
		
		
		//isElementDisplayed("//p[contains(text(),'Success')]");
		//verifyValue("//p[contains(text(),'Success')]", "Success");
		
		driver.findElement(By.xpath("//span[text()='Continue']")).click();
		
		report.updateTestLog("Upload Resume from Local", "Resume uploaded succesfully from local machine", Status.PASS);
	}
	private void uploadResumeFromGoogleDrive() throws InterruptedException{
		
		driver.findElement(By.xpath("//span[text()='Google Drive']")).click();
		
		Thread.sleep(5000);
		if (isElementPresent(By.xpath("//a[text()='" + "ramanna.satyaph@gmail.com" + "']"))) {System.out.println("Logged In");}
		else if(isElementPresent(By.xpath("//span[@id='reauthEmail']"))) {
			System.out.println("Partially logged in");
			
			driver.findElement(By.xpath("//input[@id='Passwd']")).sendKeys("KAmol1218");
			driver.findElement(By.xpath("//input[@id='signIn']")).click();
			
			}
		else {
			System.out.println("Not logged in");
			//driver.findElement(By.xpath("//input[@id='Email']")).sendKeys(Constants.GMAIL_EMAIL);
			driver.findElement(By.xpath("//input[@id='Email']")).sendKeys("ramanna.satyaph@gmail.com");
			driver.findElement(By.xpath("//input[@id='next']")).click();
			//driver.findElement(By.xpath("//input[@id='Passwd']")).sendKeys(Constants.GMAIL_PASSWORD);
			driver.findElement(By.xpath("//input[@id='Passwd']")).sendKeys("KAmol1218");
			driver.findElement(By.xpath("//input[@id='signIn']")).click();
		}
		
		driver.findElement(By.xpath("//button[@id='submit_approve_access']")).click();
		driver.findElement(By.xpath("//div[text()='Mike Smith.doc']")).click();
		isElementDisplayed("//p[contains(text(),'Success')]");
		//takeScreenshot();
		driver.findElement(By.xpath("//span[text()='Click to continue']")).click();
		softAssert.assertAll();		
		report.updateTestLog("uploadResume from GoogleDrive", "uploadResume from GoogleDrive Success", Status.PASS);
		
		
	}
	    
	
	private void dataVerification(){
		
		verifyValue("//input[@name='FirstName']", "Mike");
		verifyValue("//input[@name='LastName']", "Smith");
		verifyValue("//input[@name='Address']", "1 Smith Road");
		verifyValue("//input[@name='City']","Parsippany");
		verifyValue("//input[@name='ZipCode']","07054");
		
		//verifyValue("//label[contains(text(),'Country')]/following-sibling::div/div/select","United States","//label[contains(text(),'Country')]/following-sibling::div/div/select/option[contains(text(),'United States')]");
		WebElement elment1= driver.findElement(By.xpath("//label[contains(text(),'Country')]/following-sibling::div/div/select"));
		WebElement elemnt2 =elment1.findElement(By.xpath("//label[contains(text(),'Country')]/following-sibling::div/div/select/option[contains(text(),'United States')]"));
		elemnt2.click();
		
		verifyValue("//label[contains(text(),'State')]/following-sibling::div/div/select","New Jersey","//label[contains(text(),'State')]/following-sibling::div/div/select/option[contains(text(),'New Jersey')]");
		verifyValue("//label[contains(text(),'Nearest Metro Area')]/following-sibling::div/div/select", "Parsippany", "//label[contains(text(),'Nearest Metro Area')]/following-sibling::div/div/select/option[contains(text(),'Parsippany')]");
		verifyValue("//label[contains(text(),'Primary Number')]/following-sibling::div/div/select","Home Phone", "//label[contains(text(),'Primary Number')]/following-sibling::div/div/select/option[contains(text(),'Home Phone')]");
		verifyValue("//input[@name='HomePhone']","+1 973-753-6772");
				
		//verifyValue("//label[contains(text(),'Source Type')]/following-sibling::div/div/select","Other","//label[contains(text(),'Source Type')]/following-sibling::div/div/select/option[contains(text(),'Other)]");
		
		verifyValue("//label[contains(text(),'Source Type')]/following-sibling::div/div/select","Agent","//label[contains(text(),'Source Type')]/following-sibling::div/div/select/option[contains(text(),'Agent')]");
		driver.findElement(By.xpath("//input[@value='Next']")).click();
		
		verifyValue("//input[@placeholder='Employer*']","Doc's Irish Inn");
		verifyValue("//input[@placeholder='Job Title*']","Bartender");
		verifyValue("//input[@name='BeginDate.month']","01/2011");
		//safeClick("//a[@title='Close']");
		verifyValue("//input[@name='EndDate.month']","03/2016");
		//safeClick("//a[@title='Close']");
		//driver.findElement(By.xpath("(//a[child::span/span[contains(text(),'Work Experience')]])[2]")).click();
		//safeClick("//a[@title='Close']");
		//takeScreenshot();
		driver.findElement(By.xpath("//input[@value='Next']")).click();
		
		//textCheckpoint("Education", 30);
		
		verifyValue("//input[@placeholder='Institution*']","Mansfield University of Pennsylvania");
		verifyValue("//input[@placeholder='Program*']","Bachelors");
		//verifyValue("//input[@name='graduationDate.month']","05/2011");
		driver.findElement(By.xpath("//input[@name='graduationDate.month']")).click();
		safeClick("//a[@title='Close']");
		verifyValue("//select[@name='StudyLevel']","Bachelor's Degree (±16 years)","//label[contains(text(),'Education Level')]/following-sibling::div/div/select/option[contains(text(),'Bachelor')]");
		verifyValue("//select[@name='OBTAINED']","Yes", "//label[contains(text(),'Degree')]/following-sibling::div/div/select/option[contains(text(),'Yes')]");
		//takeScreenshot();
		driver.findElement(By.xpath("//input[@value='Next']")).click();
		
		
		
	}

	
	private void questionsDataVerification(){
		
		// Please complete Important Questions from here 
		
	
	}
	
		
	//@Step("Tear Down and Download Report")
	public void testTearDown() throws Exception {
		if (driver != null) {
			driver.close();
			if(isMobile) { downloadReport("html"); }
			driver.quit();
		}
	}
	
	//@Attachment
	public byte[] saveImage(byte[] imageToSave) {
        return imageToSave;
    }
			
	@AfterTest
	public void closeWebDriver () throws SessionNotFoundException, IOException {
		// make sure web driver is closed
		try{
			if (driver.getSessionId() != null) {
				driver.close();
				}
			driver.quit();
			}	
		catch (SessionNotFoundException e) {}
	}
	
	//@Attachment
	private byte[] downloadReport(String type) throws IOException
	{	
		String command = "mobile:report:download";
		Map<String, String> params = new HashMap<>();
		params.put("type", type);
		String report = (String)driver.executeScript(command, params);
		byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report);
		return reportBytes;
	}
	
//	public static void downloadReportToLocal(String type, String fileName) throws IOException {
//        try { 
//    // call executeScript command to get the report
//            String command = "mobile:report:download"; 
//            Map<String, Object> params = new HashMap<>(); 
//            params.put("type", type); 
//            String report = (String)driver.executeScript(command, params);
//    // store it on your local machine
//            File reportFile = new File("c:\\test\\" + fileName + "." + type); 
//            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(reportFile)); 
//            byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report); 
//            output.write(reportBytes); output.close(); 
//        } catch (Exception ex) { 
//            System.out.println("Got exception " + ex); }
//    }
	
	private static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}	
	
	protected void textCheckpoint(String textToFind, Integer timeout) {
		
			perfectoCommand.put("content", textToFind);
			perfectoCommand.put("timeout", timeout);
			Object result = driver.executeScript("mobile:checkpoint:text", perfectoCommand);
			Boolean resultBool = Boolean.valueOf(result.toString());
			perfectoCommand.clear();
			takeScreenshot();
			softAssert.assertEquals(Boolean.valueOf(resultBool), Boolean.valueOf(true), textToFind);
		
	}
	
	
	protected void textCheckpointisMobile(String textToFind, Integer timeout) {
		if (isMobile) {
			perfectoCommand.put("content", textToFind);
			perfectoCommand.put("timeout", timeout);
			Object result = driver.executeScript("mobile:checkpoint:text", perfectoCommand);
			Boolean resultBool = Boolean.valueOf(result.toString());
			perfectoCommand.clear();
			takeScreenshot();
			softAssert.assertEquals(Boolean.valueOf(resultBool), Boolean.valueOf(true), textToFind);
		}
	}
    //@Attachment
    public byte[] takeScreenshot() {
        System.out.println("Taking screenshot");
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
	
    
    //@Step("Is {0} displayed")
    protected void isElementDisplayed(String xpath) {
    	softAssert.assertTrue(isElementPresent(By.xpath(xpath)), xpath);
    }
    
    
    protected boolean isElementPresent(By by){
        try{
        	driver.findElement(by);
            return true;
        }
        catch(NoSuchElementException e){
            return false;
        }
    }
    
   // @Step("Click {1}")
    protected void clickLink(String xpath, String desc) {
    	try {
    		driver.findElement(By.xpath(xpath)).click();
    	}
    	catch (Exception e) {
    		System.out.println(e.toString());
    	}
    	
    }
    
   // @Step("Enter {1}")
    protected void enterData(String xpath, String desc, String valueToSet){
    	driver.findElement(By.xpath(xpath)).sendKeys(valueToSet);
    }
    
//    protected void verifyText(String textToFind, Integer timeout) {
//    	softAssert.assertTrue(textCheckpoint(textToFind, timeout), textToFind);
//    }
    
    protected void verifyValue(String xpath, String value){
    	String fieldValue = driver.findElement(By.xpath(xpath)).getAttribute("value");
    	//softAssert.assertTrue(fieldValue.equals(value), xpath);
    	if (fieldValue.equals(value)) {
    			report.updateTestLog(xpath + " vefication in Resume Parsing", value + " Successfully verified after resume upload", Status.PASS);
    	}else   
    	    {       		
    		    driver.findElement(By.xpath(xpath)).clear();
        		driver.findElement(By.xpath(xpath)).sendKeys(value);
        		report.updateTestLog(xpath + " vefication in Resume Parsing", value + " Missing in Parsing value after resume upload", Status.FAIL);
        		
        	}
    	
    }
    
    
    protected void desktopCheckpoint(String xpath, String value){
    	String fieldValue = driver.findElement(By.xpath(xpath)).getAttribute("value");
    	System.out.println(fieldValue);
    	if(fieldValue.equals(value)){
    		
    		report.updateTestLog(value, value + " text found", Status.PASS);
    	}else
    	{
    		report.updateTestLog(value, value + " text not found", Status.FAIL);
    	}
    	
//    	if (!fieldValue.equals(value)) {
//    		driver.findElement(By.xpath(xpath)).clear();
//    		driver.findElement(By.xpath(xpath)).sendKeys(value);
//    	}
    }
//    protected void verifyValue(String xpath, String value, String selectXpath){
//    	String fieldValue = driver.findElement(By.xpath(xpath)).getAttribute("value");
//    	softAssert.assertTrue(fieldValue.equals(value), xpath);
//    	if (!fieldValue.equals(value)) {
//    		WebElement select = driver.findElement(By.xpath(xpath));
//    		WebElement option = select.findElement(By.xpath(selectXpath));
//    		option.click();
//    	}
//
//    }
//    
    
    protected void verifyValue(String xpath, String value, String selectXpath){
    	String fieldValue = driver.findElement(By.xpath(xpath)).getAttribute("value");
    	//softAssert.assertTrue(fieldValue.equals(value), xpath);
    	if (fieldValue.equals(value)) {
    		
    		report.updateTestLog(xpath + " vefication in Resume Parsing", value + " Successfully verified after resume upload", Status.PASS);
    	} else{
    		WebElement select = driver.findElement(By.xpath(xpath));
    		WebElement option = select.findElement(By.xpath(selectXpath));
    		option.click();
    		report.updateTestLog(xpath + " vefication in Resume Parsing", value + " Missing in Parsing value after resume upload", Status.FAIL);
    	}

    }
    
    protected void safeClick(String xpath) {
    	try {
    		driver.findElement(By.xpath(xpath)).click();
    	} catch (NoSuchElementException e) {}
    }
	
	
	
}
*/