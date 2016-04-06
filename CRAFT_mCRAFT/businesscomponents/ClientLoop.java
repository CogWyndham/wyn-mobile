package businesscomponents;

import org.openqa.selenium.By;

import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class ClientLoop extends ReusableLibrary {

	private int wait = 5000;	
	

	public ClientLoop(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}

	public void ClientLoopCRM(String ClientLoopURL,String EmailId,String CandidateType) throws InterruptedException{
		driver.get(ClientLoopURL);
		Thread.sleep(wait);
		report.updateTestLog("CRM Loop Validation", "CRM Loop URL is opened", Status.PASS);
		driver.findElement(By.id("UserName")).sendKeys("TestAutomation");
		driver.findElement(By.id("Password")).sendKeys("Wyndham1");
		driver.findElement(By.xpath("//input[@value='LOG IN']")).click();
		Thread.sleep(wait);
		report.updateTestLog("CRM Loop Validation", "Login successfully into CRM Loop ", Status.PASS);
		driver.findElement(By.xpath("//a[@class='navLinks navPeople']")).click();
		Thread.sleep(wait);
		Thread.sleep(wait);
		//output = output.replace("-", "");
		String ApplicantSearch=EmailId;
		ApplicantSearch=ApplicantSearch.replace("@gmail.com","");
		
		driver.findElement(By.xpath("//input[@id='keyword']")).sendKeys(ApplicantSearch);
		Thread.sleep(wait);
		
		driver.findElement(By.xpath("//*[@id='peopleSearchKeyword']")).click();
		//driver.findElement(By.xpath("//*[contains(text(),'Search')]")).click();
		Thread.sleep(wait);
		report.updateTestLog("CRM Loop Validation", "Searched in the CRM Loop with the Applicant ID ", Status.PASS);
		//driver.findElement(By.xpath("//a[@class='tileTitle tileTextOverflow']")).click();
		//Thread.sleep(5000);
		driver.findElement(By.xpath("//img[@src='//mresourcemedia.s3.amazonaws.com/portal/loop_avatar.gif']")).click();
		Thread.sleep(wait);
		String CRMElementtype=driver.findElement(By.id("CandidateDetailPanelCandidateDetailsType")).getText();
		if (CRMElementtype.equals(CandidateType)) {
			report.updateTestLog("CRM Loop Validation", "Candidate Applicant Type is "+CRMElementtype, Status.PASS);
		}else{
			report.updateTestLog("CRM Loop Validation", "Candidate Applicant Type is "+CRMElementtype, Status.FAIL);
		}
		//External
		String CRMEmailId=driver.findElement(By.id("CandidateDetailPanelCandidateDetailsEmail")).getText();
		if (CRMEmailId.equals(EmailId)) {
			report.updateTestLog("CRM Loop Validation", "Candidate Applicant Email ID is "+CRMEmailId, Status.PASS);	
		}else{
			report.updateTestLog("CRM Loop Validation", "Candidate Applicant Email ID is "+CRMEmailId, Status.FAIL);	
		}

		Thread.sleep(wait);
		if (driver.findElement(By.xpath("//*[@class='CandidateActivityFeedPreview' and contains(text(),'Wyndham Worldwide Careers')]")).isDisplayed()) {
			report.updateTestLog("CRM Loop Validation", "Wyndham Worldwide Careers Email is present in CRM Loop Dashboard", Status.PASS);
		}else{
			report.updateTestLog("CRM Loop Validation", "Wyndham Worldwide Careers Email is not present in CRM Loop Dashboard", Status.FAIL);
	}
		
		if (driver.findElement(By.xpath("//*[@class='CandidateActivityFeedPreview' and contains(text(),'Thank you for Registering')]")).isDisplayed()) {
			report.updateTestLog("CRM Loop Validation", "Thank you for Registering Email is present in CRM Loop Dashboard", Status.PASS);
		}else{
			report.updateTestLog("CRM Loop Validation", "Thank you for Registering Email is not present in CRM Loop Dashboard", Status.FAIL);
	}
		
		if (driver.findElement(By.xpath("//*[@class='CandidateActivityFeedPreview' and contains(text(),'Confirmation of Application')]")).isDisplayed()) {
			report.updateTestLog("CRM Loop Validation", "Confirmation of Application Email is present in CRM Loop Dashboard", Status.PASS);
		}else{
			report.updateTestLog("CRM Loop Validation", "Confirmation of Application Email is not present in CRM Loop Dashboard", Status.FAIL);
	}
		
		if (driver.findElement(By.xpath("//*[@class='CandidateActivityFeedPreview' and contains(text(),'Wyndham Worldwide')]")).isDisplayed()) {
			report.updateTestLog("CRM Loop Validation", "Wyndham Worldwide Email is present in CRM Loop Dashboard", Status.PASS);
		}else{
			report.updateTestLog("CRM Loop Validation", "Wyndham Worldwide Email is not present in CRM Loop Dashboard", Status.FAIL);
	}
		
		
		Thread.sleep(wait);
}
	
}
