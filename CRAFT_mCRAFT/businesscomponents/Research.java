package businesscomponents;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import com.cognizant.framework.Status;
import supportlibraries.*;

/**
 * Class for storing general purpose business components
 * 
 * @author Cognizant
 */ 

public class Research  extends ReusableLibrary {
	
	/**
	 * Constructor to initialize the component library
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object passed from the
	 *            {@link DriverScript}
	 */
	public Research(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	
	public void indeedSearch(){
		driver.manage().window().maximize();
		driver.get("http://www.indeed.com/");
				
		driver.findElement(By.xpath(".//*[@id='what']")).sendKeys("Selenium");
		driver.findElement(By.xpath(".//*[@id='where']")).clear();
		driver.findElement(By.xpath(".//*[@id='where']")).sendKeys("New York");
		
		
		driver.findElement(By.xpath(".//*[@id='fj']")).click();
		System.out.println(driver.findElement(By.xpath(".//*[@id='searchCount']")).getText());
		System.out.println(driver.getTitle());
		
		
		driver.findElement(By.xpath("//*[@id='prime-popover-close-button']")).click();
		
		String homeWindow = driver.getWindowHandle();
		System.out.println(homeWindow);
		
		//Locating by LinkText
		driver.findElement(By.xpath("//*[@class='turnstileLink' and @title = 'Automation Tester']")).click();
		String curentWindow = driver.getTitle();
		System.out.println(curentWindow);
		
		
		if(curentWindow.equals("Selenium Jobs, Employment in New York | Indeed.com")){
			System.out.println("PASS");
		}
			else {
			System.out.println("FAIL");	
		}
				
		Set<String> mutiplePopups = driver.getWindowHandles();
		System.out.println(mutiplePopups);
		System.out.println(mutiplePopups.size());
		
		Iterator<String> iterator = mutiplePopups.iterator();
		String  currentWindowID;
		
		while(iterator.hasNext()) {
			currentWindowID = iterator.next().toString();
			System.out.println(currentWindowID);
			
			if(!currentWindowID.equals(homeWindow)){
				driver.switchTo().window(currentWindowID);
				//Thread.sleep(5000);
				driver.findElement(By.xpath("html/body/table[2]/tbody/tr/td[2]/div/div[1]/div/div[1]/p[3]/span/a/span/span[1]")).click();
				//driver.findElement(By.xpath("//*[@id='applicant.name']")).sendKeys("Ramanna");
				//driver.findElement(By.xpath("//a[@class='secondary' and @title='Cancel']")).click();

//				Set<String> mutiplePopups2 = driver.getWindowHandles();
//				System.out.println(mutiplePopups2);
//				System.out.println(mutiplePopups2.size());
//				
//				Iterator<String> iterator2 = mutiplePopups2.iterator();
//				String  currentWindowID2;
//				
//				while(iterator2.hasNext()) {d
//					 currentWindowID2 = iterator2.next().toString();
//					System.out.println(currentWindowID);
//					
//					
//				if(!currentWindowID2.equals(homeWindow)){
//					driver.switchTo().window(currentWindowID2);
//					Thread.sleep(5000);
//					driver.findElement(By.xpath("//a[@class='secondary' and @title='Cancel']")).click();
//					
//				}
//				
//					}
//				
				
			}
				}
		
		
		report.updateTestLog("SearchText", "Searching the text", Status.PASS);
	}

}
