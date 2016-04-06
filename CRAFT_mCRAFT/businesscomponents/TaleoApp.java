package businesscomponents;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

import java.io.FileWriter;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import com.cognizant.framework.Status;
import com.cognizant.framework.selenium.CraftDriver;


public class TaleoApp extends ReusableLibrary {
	
	public TaleoApp(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	
	
	String Url = "https://stgwyndham.taleo.net" ;
	String uName = "testautomation";
	String password = "Wyndham1";
	
	int wait = 50000;
	
	WebDriver driver2 = new FirefoxDriver();
	Actions builder = new Actions(driver2);
	//Actions builder2 = new Actions(Keyboard );
		
	//LoopNewApplicant loopObj = new LoopNewApplicant(scriptHelper);
	
	
	@Test
	public void taleoTest() throws Exception {
		try {

			//System.out.println("Hello This is Ganesh here");
			
			
				
			driver2.manage().window().maximize();
			driver2.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver2.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver2.get(Url);
			
			Thread.sleep(1000);
			String tHnd1 = driver2.getWindowHandle();
			driver2.switchTo().window(tHnd1);
			/*String tHnd = driver.getWindowHandle();
			
			System.out.print(tHnd1);
			System.out.print(tHnd);*/
			
			report.updateTestLog("Login Page", "Login Page Displayed Successfully", Status.PASS);
			driver2.findElement(By.xpath("//input[contains(@id,'login-name1')]")).sendKeys(uName);
			driver2.findElement(By.xpath("//input[contains(@id,'login-password')]")).sendKeys(password);
			driver2.findElement(By.xpath(".//*[@id='dialogTemplate-dialogForm-content-login-defaultCmd']/span/span/span/span")).click();
				
			//driver.findElement(By.xpath
			//Thread.sleep(1000);
			//driver.findElement(By.xpath(".//*[@id='tocTemplate-dialogForm-content-tocContent-j_id_jsp_975824191_12pc7-0-commandLink_text']/span")).click();
			//driver.findElement(By.xpath("//span[contains(text()[contains(.,'Recruiting')]")).click();
			driver2.findElement(By.xpath("//*[contains(text(),'Recruiting')]")).click();
					
			//text()[contains(.,'Alliance Consulting')]

			Thread.sleep(20000);
			report.updateTestLog("Candidate Search Page", "Candidate Search Page Displayed Successfully", Status.PASS);	
			builder.moveToElement(driver2.findElement(By.xpath(".//*[@id='baseForm_noFlashPanel']")),1000,40).click().perform();					
			Thread.sleep(10000);
			
			Runtime.getRuntime().exec("cscript C:\\NarenMobile\\muruga.vbs");
					
			Thread.sleep(10000);
			report.updateTestLog("Candidate Home", "Candidate Home Page Displayed Successfully", Status.PASS);
			builder.moveToElement(driver2.findElement(By.xpath(".//*[@id='baseForm_noFlashPanel']")),1153,40).click().perform();					
			
			Thread.sleep(20000);
			
			builder.moveToElement(driver2.findElement(By.xpath(".//*[@id='baseForm_noFlashPanel']")),300,215).click().perform();					
			Thread.sleep(10000);
			
			report.updateTestLog("Attachhments", "Attachments displayed successfully", Status.PASS);
			builder.moveToElement(driver2.findElement(By.xpath(".//*[@id='baseForm_noFlashPanel']")),360,205).click().perform();					
			Thread.sleep(10000);
			
			String wHnd1 = driver2.getWindowHandle();
			System.out.println(wHnd1);
			
			builder.moveToElement(driver2.findElement(By.xpath(".//*[@id='baseForm_noFlashPanel']")),500,370).click().perform();					
			Thread.sleep(10000);
			
			String wHnd2 = driver2.getWindowHandle();
			System.out.println(wHnd2);
				
			for(String winHandle : driver2.getWindowHandles()){
			    driver2.switchTo().window(winHandle);
			}

			String wHnd3 = driver2.getWindowHandle();
			System.out.println(wHnd3);
			
			
			//driver2.switchTo().frame(arg0);
			
			driver2.switchTo().frame(driver2.findElement(By.tagName("iframe")));
			String msg1 =driver2.findElement(By.tagName("body")).getText();
			String[] parts = msg1.split("Wyndham and its subsidiary");
			//div[contains(@class, 'c') and text()='First Name: Sally']
			//div[contains(.,'First Name')]
			//div[@class='c' and normalize-space(div[last()]/following-sibling::text())='First Name: Sally']
			System.out.println(parts[0]);
			report.updateTestLog("PDF Contents", "Following are the PDF Contents" + parts[0], Status.PASS);
			//System.out.println("Hello This is Ganesh here");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	
		}
	}
			

}
