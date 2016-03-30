package businesscomponents;

import java.awt.AWTException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.cognizant.framework.Status;

//import ru.yandex.qatools.allure.annotations.Attachment;
//import ru.yandex.qatools.allure.annotations.Step;
//import test.java.Constants;
import supportlibraries.DriverScript;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

/**
 * Class for storing general purpose business components
 * 
 * @author Cognizant
 */

public class LoopNewApplicant_old extends ReusableLibrary {
	/**
	 * Constructor to initialize the component library
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object passed from the
	 *            {@link DriverScript}
	 */
	// protected RemoteWebDriver driver1;
	public LoopNewApplicant_old(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	// Copied
	private Map<String, Object> perfectoCommand = new HashMap<>();
	protected SoftAssert softAssert = new SoftAssert();
	private boolean rotate;
	private boolean isMobile;

	static int count = 1;
	static String questionsRadio[] = {
			"1. All offers of employment are conditioned upon your ability to provide evidence of your right to be legally employed. Are you authorized to work in the Country in which the job is located?*",
			"2. Have you previously been employed by Wyndham Worldwide or any of its subsidiaries or affiliates, including Wyndham Hotel Group, Wyndham Vacation Ownership, Wyndham Vacation Resorts - Asia Pacific or Wyndham Destination Network (formerly known as Wyndham Exchange & Rentals)?*",
			"3. Have you ever been terminated, laid off, discharged or asked to resign from any employment?*",
			"6. Are you currently employed by Wyndham Worldwide or any of its subsidiaries or affiliates, including Wyndham Hotel Group, Wyndham Vacation Ownership, Wyndham Vacation Resorts - Asia Pacific or Wyndham Destination Network (formerly known as Wyndham Exchange & Rentals)?*" };

	static String answersRadio[] = { "Yes, but I require sponsorship", "No", "Yes", "Yes" };

	static String questionsTextBox[] = {
			"4. If yes, give the employer(s) and reason(s) for each discharge/resignation." };
	static String answersTextBox[] = { "Not Applicable" };

	static String questionsCheckBox = "5. Please select any languages, in addition to English, in which you are proficient (select all that apply).";
	static String answersCheckBox[] = { "Arabic", "Danish" };

	@Test
	public void runLoopTest() throws Exception {
		try {
			createProfile();
		
			privacyAgreement();
			uploadResumeFromDropBox();
			// uploadResumeFromLocal();
			// uploadResumeFromGoogleDrive();
			// dataVerification();
			TellUsAboutYourself();
			WorkExperience();
			Education();
			
			answerRadioButton();
			answerTextBox();
			answerCheckBox();
			
			// AreWeGoodMatch();
			Diversity();
			eSignature();
			Attachments();
			ReviewAndSubmit();
			//ApplicationCompleted();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			testTearDown();
		}
	}
	
	public void answerRadioButton()
	{
		WebElement option = driver.findElement(By.xpath("//span[@class='ui-btn-text' and text()=' Yes, but I require sponsorship']"));
		option.click();
		
		option = driver.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' Yes'])[2]"));
		option.click();
		
		option = driver.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' No'])[3]"));
		option.click();
		
		option = driver.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' Yes'])[4]"));
		option.click();
	}
	
	public void answerTextBox()
	{
		WebElement option = driver.findElement(By.xpath("//textarea[contains(@id,'stepElement')]"));
		option.sendKeys("Not Applicable");
		
	}
	
	public void answerCheckBox()
	{
		WebElement option = driver.findElement(By.xpath("//span[@class='ui-btn-text' and text()=' Italian']"));
		option.click();
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
	}

	// @Step("Create Profile")
	public void createProfile() throws MalformedURLException, InterruptedException {

		// driver.get("http://wynexternalstage.loop.jobs");
		driver.get("https://wynstaging.loopapply.com/apply/70/1184765");
		driver.findElement(By.xpath("//span[text()='Create an account']")).click();

		String output;
		output = new String(new Timestamp(System.currentTimeMillis()).toString());
		output = output.replace("-", "");
		output = output.replace(" ", "_");
		output = output.replace(":", "");
		output = "TaleoWyndham+" + output + "@gmail.com";

		driver.findElement(By.xpath("//input[@id='Email']")).sendKeys(output);
		driver.findElement(By.xpath("//input[@id='Password']")).sendKeys("Wyndham1");
		driver.findElement(By.xpath("//input[@id='ConfirmPassword']")).sendKeys("Wyndham1");

		// driver.findElement(By.xpath("//input[@name='Create']")).click();

		// textCheckpoint("Create Account", 30);
		desktopCheckpoint("//input[@value='Create Account']", "Create Account");

		driver.findElement(By.xpath("//input[@value='Create Account']")).click();
		Thread.sleep(5000);
		// verifyValue("//input[@name='LastName']", "Smith");
		// softAssert.assertAll();
		report.updateTestLog("createProfile", "createProfile Success", Status.PASS);

	}

	// @Step("Privacy Agreement")
	private void privacyAgreement() {
		// textCheckpoint("Privacy Agreement", 30);
		desktopCheckpoint("//input[@name='ContinueBtn']", "Continue");
		driver.findElement(By.xpath("//input[@name='ContinueBtn']")).click();
		isElementDisplayed("//span[contains(text(),'Please accept our privacy agreement if you wish to continue')]");
		// desktopCheckpoint("//span[contains(text(),'Please accept our privacy
		// agreement if you wish to continue')]", "Please accept our privacy
		// agreement if you wish to continue");
		// desktopCheckpoint("//span[contains(text(),'Please accept our privacy
		// agreement if you wish to continue')]", "Please accept our privacy
		// agreement if you wish to continue");
		// desktopCheckpoint("//span[text()='I accept the privacy agreement", "I
		// accept the privacy agreement");
		driver.findElement(By.xpath("//span[text()='I accept the privacy agreement']")).click();
		// desktopCheckpoint("//span[contains(text(),'Yes!')]", "Yes");
		driver.findElement(By.xpath("//span[contains(text(),'Yes!')]")).click();
		// takeScreenshot();
		driver.findElement(By.xpath("//input[@name='ContinueBtn']")).click();
		// softAssert.assertAll();
		report.updateTestLog("privacyAgreement", "privacyAgreement selection success", Status.PASS);
	}

	// @Step("Upload Resume")

	private void uploadResumeFromLocal() throws InterruptedException, AWTException {

		driver.findElement(By.xpath("//span[text()='Upload local file']")).click();
		Thread.sleep(5000);
		// driver.findElement(By.xpath("//span[text()='Browse']")).click();
		driver.findElement(By.xpath("//input[@data-buttontext='Choose file']")).click();

		WebDriverWait wait = new WebDriverWait((WebDriver) driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());

		Alert alert = driver.switchTo().alert();
		System.out.println(driver.getTitle());

		// // enter the filename
		// alert.sendKeys("C:\\Mobile testing\\Mike Smith.doc");
		//
		// // hit enter
		// Robot r = new Robot();
		// //r.keyPress(KeyEvent);
		// //r.keyRelease(KeyEvent.VK_ENTER);
		//
		// // switch back
		// driver.switchTo().activeElement();

	}

	private void uploadResumeFromDropBox() throws InterruptedException {
		report.updateTestLog("Resume Option", "Resume upload option displayed ", Status.PASS);
		driver.findElement(By.xpath("//span[text()='Dropbox']")).click();

		Thread.sleep(5000);
		driver.findElement(By.xpath("//input[@name='login_email']")).sendKeys("TaleoWyndham@gmail.com");
		driver.findElement(By.xpath("//input[@name='login_password']")).sendKeys("Wyndham1");
		report.updateTestLog("Dropbox Option", "Dropbox option selected for profile upload", Status.PASS);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//button[@name='allow_access']")).click();
		Thread.sleep(4000);
		report.updateTestLog("Dropbox Option", "Resume displayed from Dropbox", Status.PASS);
		driver.findElement(By.xpath("//div[text()='SallySample.doc']")).click();
		// driver.findElement(By.xpath("//div[@class='lwFile']")).click();
		Thread.sleep(5000);
		report.updateTestLog("Dropbox Option", "Resume selected from Dropbox", Status.PASS);
		driver.findElement(By.xpath("//span[text()='Click to continue']")).click();
		Thread.sleep(10000);
		// span[@class='ui-btn-text'],

	}

	private void TellUsAboutYourself() throws InterruptedException {

		new Select(driver.findElement(
				By.name("et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-0")))
						.selectByVisibleText("United States");
		Thread.sleep(5000);
		new Select(driver.findElement(
				By.name("et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-1")))
						.selectByVisibleText("Alabama");
		Thread.sleep(5000);
		new Select(driver.findElement(
				By.name("et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-2")))
						.selectByVisibleText("Anniston");
		Thread.sleep(5000);
		new Select(driver.findElement(By.name("PreferredPhone"))).selectByVisibleText("Home Phone");
		Thread.sleep(5000);
		report.updateTestLog("Tell us about yourself", "Details entered successfully", Status.PASS);
		new Select(driver.findElement(
				By.name("et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_1-sourceTrackingBlock-recruitmentSourceType")))
						.selectByVisibleText("Company Website");
		Thread.sleep(5000);
		new Select(driver.findElement(By.name("dynamicElement1"))).selectByVisibleText("RCI");
		Thread.sleep(5000);
		report.updateTestLog("Tell us about yourself", "Source details entered successfully", Status.PASS);
		// driver.findElement(By.xpath("//input[@id='lwAppBtnSaveNext']")).click();
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		// driver.findElement(By.id("lwAppBtnSaveNext")).click();
		// driver.findElement(By.xpath("//input[type='button' and value='Next'
		// and class ='lwAppActionButton ui-btn-hidden']")).click();
		Thread.sleep(5000);
	}

	private void WorkExperience() throws InterruptedException {
		// driver.findElement(By.xpath("//input[name='Responsibility']")).sendKeys("Program
		// Manager");
		driver.findElement(By.name("Responsibility")).sendKeys("Program Manager");

		Thread.sleep(10000);
		report.updateTestLog("Work Experience", "Work Experience Details entered successfully", Status.PASS);
		// driver.findElement(By.xpath("//input[@id='lwAppBtnSaveNext']")).click();
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		// driver.findElement(By.id("lwAppBtnSaveNext")).click();
		Thread.sleep(5000);

	}

	private void Education() throws InterruptedException {
		new Select(driver.findElement(By.name("StudyLevel"))).selectByVisibleText("Higher Degree");
		Thread.sleep(5000);
		new Select(driver.findElement(By.name("OBTAINED"))).selectByVisibleText("Yes");
		// name="graduationDate.month"
		// driver.findElement(By.name("graduationDate.month")).click();
		// Thread.sleep(5000);
		// driver.findElement(By.xpath("//div[@class='ui-block-a']/input")).clear();
		// Thread.sleep(5000);
		// driver.findElement(By.xpath("//div[@class='ui-block-a']/input")).click();
		// Thread.sleep(2000);
		// driver.findElement(By.xpath("//div[@class='ui-block-a']/input")).sendKeys(Keys.ARROW_RIGHT);
		// Thread.sleep(2000);
		// driver.findElement(By.xpath("//div[@class='ui-block-a']/input")).sendKeys(Keys.BACK_SPACE);
		// Thread.sleep(2000);
		// driver.findElement(By.xpath("//div[@class='ui-block-a']/input")).sendKeys(Keys.DELETE);
		// Thread.sleep(2000);
		// driver.findElement(By.xpath("//div[@class='ui-block-a']/input")).sendKeys("2");
		// Thread.sleep(2000);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).clear();
		// Thread.sleep(5000);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).click();
		// Thread.sleep(2000);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys(Keys.ARROW_RIGHT);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys(Keys.ARROW_RIGHT);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys(Keys.ARROW_RIGHT);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys(Keys.ARROW_RIGHT);
		// Thread.sleep(5000);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys(Keys.BACK_SPACE);
		// Thread.sleep(1000);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys(Keys.BACK_SPACE);
		// Thread.sleep(1000);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys(Keys.BACK_SPACE);
		// Thread.sleep(1000);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys(Keys.BACK_SPACE);
		// Thread.sleep(1000);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys(Keys.DELETE);
		// Thread.sleep(2000);
		// driver.findElement(By.xpath("//div[@class='ui-block-a']/input")).sendKeys(Keys.BACK_SPACE);
		// Thread.sleep(2000);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).click();
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys(Keys.BACK_SPACE);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys(Keys.BACK_SPACE);
		// driver.findElement(By.xpath("//div[@class='ui-block-b']/input")).sendKeys("2008");
		// driver.findElement(By.xpath("//span[@class='ui-btn-text']")).click();
		// Thread.sleep(5000);
		// safeClick("//a[@title='Close']");
		Thread.sleep(2000);
		report.updateTestLog("Education", "Education Details entered successfully", Status.PASS);
		driver.findElement(By.xpath("//input[@value='Next']")).click();
		// driver.findElement(By.id("lwAppBtnSaveNext")).click();
		// driver.findElement(By.id("lwAppBtnSaveNext")).click();
		// driver.findElement(By.id("lwAppBtnSaveNext")).click();
		// Thread.sleep(5000);
		// questions
		// driver.findElement(By.id("lwAppBtnSaveNext")).click();
		// Thread.sleep(5000);
	}

	
	
	
	private void updateImportantQuestionsRadio(String questn, String ans) {
		// Enter answer for Yes / no questions in Important Questions page
		List<WebElement> questions = driver.findElements(By.xpath("//fieldset[contains(@id,'stepElement')]/div/label[@class='question-label']"));
		int qustnNo = 1;
		for (WebElement question : questions)
			if (!question.getText().equals(questn))
				qustnNo++;
			else
				break;
		if (qustnNo <= questions.size()) {
			String radioButtons = "//fieldset[contains(@id,'stepElement')][" + qustnNo
					+ "]//div[@class='ui-radio']//span[@class='ui-btn-text']";
			List<WebElement> wb_radioButtons = driver.findElements(By.xpath(radioButtons));

			for (WebElement wb_radioButton : wb_radioButtons)
				if (ans.equals(wb_radioButton.getText()))
					wb_radioButton.click();
		} else
			System.out.println("Question Didn't find");
	}

	private void updateImportantQuestionsTextBox(String questn, String ans) {
		List<WebElement> questions = driver
				.findElements(By.xpath("//textarea[contains(@id,'stepElement')]/preceding-sibling::label"));
		int textboxNo = 1;
		for (WebElement question : questions)
			if (!question.getText().equals(questn))
				textboxNo++;
			else
				break;
		if (textboxNo <= questions.size()) {
			WebElement wb_textbox = driver
					.findElement(By.xpath("//textarea[contains(@id,'stepElement')][" + textboxNo + "]"));
			wb_textbox.sendKeys(ans);
		} else
			System.out.println("Question Didn't find");
	}

	private void updateImportantQuestionsMultipleCheckBox(String questn, String ans) {

		int optionNo = 1;
		List<WebElement> optionValues = driver
				.findElements(By.xpath("//div[@class='ui-checkbox']//span[@class='ui-btn-text']"));
		for (WebElement optionValue : optionValues)
			if (!optionValue.getText().equals(ans))
				optionNo++;
			else
				break;

		WebElement checkBox = driver
				.findElement(By.xpath("//div[contains(@class,'ui-checkbox')][" + optionNo + "]/input"));
		if (!checkBox.getAttribute("checked").equals("true"))
			checkBox.click();

	}

	private void Diversity() throws InterruptedException {
		new Select(driver.findElement(
				By.name("et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-diversityBlock-j_id_jsp_2086517189_11pc10-0-j_id_jsp_2086517189_14pc10-0-questionSingleList")))
						.selectByVisibleText("Male");
		Thread.sleep(2000);
		new Select(driver.findElement(
				By.name("et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-diversityBlock-j_id_jsp_2086517189_11pc10-0-j_id_jsp_2086517189_14pc10-1-questionSingleList")))
						.selectByVisibleText("Hispanic or Latino");
		Thread.sleep(2000);
		new Select(driver.findElement(
				By.name("et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-diversityBlock-j_id_jsp_2086517189_11pc10-0-j_id_jsp_2086517189_14pc10-2-questionSingleList")))
						.selectByVisibleText("No, I do not have a disability");
		Thread.sleep(2000);
		new Select(driver.findElement(
				By.name("et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-diversityBlock-j_id_jsp_2086517189_11pc10-0-j_id_jsp_2086517189_14pc10-3-questionSingleList")))
						.selectByVisibleText("Yes");
		Thread.sleep(2000);
		report.updateTestLog("Diversity", "Diversity Details entered successfully", Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		Thread.sleep(5000);

	}

	private void eSignature() throws InterruptedException {
		driver.findElement(By.name("FullName")).sendKeys("SallyAdamson");
		Thread.sleep(2000);
		report.updateTestLog("eSignature", "eSignature Details entered successfully", Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		Thread.sleep(5000);
		// File Upload
	}

	private void Attachments() throws InterruptedException {
		// driver.findElement(By.id("fileSelect")).click();
		Thread.sleep(5000);

		driver.findElement(By.id("fileComment")).sendKeys("Cover Letter comment");
		report.updateTestLog("Attachment", "Attachment Details entered successfully", Status.PASS);
		Thread.sleep(5000);
		driver.findElement(By.xpath("//input[@value='Next']")).click();
		Thread.sleep(5000);
	}

	private void ReviewAndSubmit() throws InterruptedException {
		
		List<WebElement> lables = driver.findElements(By.xpath("//div[@class ='summaryLine']/label[@class = 'SummaryTitle']"));
		List<WebElement> answers = driver.findElements(By.xpath("//div[@class ='summaryLine']/label[@class = 'SummaryAnswer']"));
		//lables.get(1).getText()
		int sizeLables=lables.size();
		//int sizeAnswers=answers.size();
		//System.out.println("Size" + sizeLables);
		//System.out.println("Size" + sizeAnswers);
		
		//for i=1
	//	for (WebElement lable : lables)
		//if (!lable.getText().equals(questn))
		//	qustnNo++;
	//	else
		//	break;
		
		
		
		
		
		report.updateTestLog("Review and Submit", "Application reviewed and submitted successfully", Status.PASS);
		
		
		
		//driver.findElement(By.xpath("//input[@value='Next']")).click();
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		Thread.sleep(5000);

	}

	private void ApplicationCompleted() throws InterruptedException {
		// driver.findElement(By.id("fileComment")).sendKeys("Cover Letter
		// comment");
		verifyValue("//h2[@class='lwAppTitleText lwAppElement']", "Application Complete");
		report.updateTestLog("Application Completed", "Application is completed successfully", Status.PASS);
	}

	
	
	private void AreWeGoodMatch() throws InterruptedException {

	}

	private void uploadResumeFromGoogleDrive() throws InterruptedException {

		driver.findElement(By.xpath("//span[text()='Google Drive']")).click();

		Thread.sleep(5000);
		if (isElementPresent(By.xpath("//a[text()='" + "ramanna.satyaph@gmail.com" + "']"))) {
			System.out.println("Logged In");
		} else if (isElementPresent(By.xpath("//span[@id='reauthEmail']"))) {
			System.out.println("Partially logged in");

			driver.findElement(By.xpath("//input[@id='Passwd']")).sendKeys("KAmol1218");
			driver.findElement(By.xpath("//input[@id='signIn']")).click();

		} else {
			System.out.println("Not logged in");
			// driver.findElement(By.xpath("//input[@id='Email']")).sendKeys(Constants.GMAIL_EMAIL);
			driver.findElement(By.xpath("//input[@id='Email']")).sendKeys("ramanna.satyaph@gmail.com");
			driver.findElement(By.xpath("//input[@id='next']")).click();
			// driver.findElement(By.xpath("//input[@id='Passwd']")).sendKeys(Constants.GMAIL_PASSWORD);
			driver.findElement(By.xpath("//input[@id='Passwd']")).sendKeys("KAmol1218");
			driver.findElement(By.xpath("//input[@id='signIn']")).click();
		}

		driver.findElement(By.xpath("//button[@id='submit_approve_access']")).click();
		driver.findElement(By.xpath("//div[text()='Mike Smith.doc']")).click();
		isElementDisplayed("//p[contains(text(),'Success')]");
		// takeScreenshot();
		driver.findElement(By.xpath("//span[text()='Click to continue']")).click();
		softAssert.assertAll();
		report.updateTestLog("uploadResume from GoogleDrive", "uploadResume from GoogleDrive Success", Status.PASS);
	}

	// @Step("Data Verification")
	private void dataVerification() {
		textCheckpoint("Tell us about you", 30);
		verifyValue("//input[@name='FirstName']", "Mike");
		verifyValue("//input[@name='LastName']", "Smith");
		verifyValue("//input[@name='Address']", "1 Smith Road");
		verifyValue("//input[@name='City']", "Parsippany");
		verifyValue("//input[@name='ZipCode']", "07054");
		verifyValue("//label[contains(text(),'Country')]/following-sibling::div/div/select", "United States",
				"//label[contains(text(),'Country')]/following-sibling::div/div/select/option[contains(text(),'United States')]");
		verifyValue("//label[contains(text(),'State')]/following-sibling::div/div/select", "New Jersey",
				"//label[contains(text(),'State')]/following-sibling::div/div/select/option[contains(text(),'New Jersey')]");
		verifyValue("//label[contains(text(),'Nearest Metro Area')]/following-sibling::div/div/select", "Parsippany",
				"//label[contains(text(),'Nearest Metro Area')]/following-sibling::div/div/select/option[contains(text(),'Parsippany')]");
		verifyValue("//label[contains(text(),'Primary Number')]/following-sibling::div/div/select", "Home Phone",
				"//label[contains(text(),'Primary Number')]/following-sibling::div/div/select/option[contains(text(),'Home Phone')]");
		verifyValue("//input[@name='HomePhone']", "+1 570-297-2001");
		// takeScreenshot();
		driver.findElement(By.xpath("//input[@value='Next']")).click();

		textCheckpoint("Work Experience", 30);
		verifyValue("//input[@placeholder='Employer*']", "Doc's Irish Inn");
		verifyValue("//input[@placeholder='Job Title*']", "Bartender");
		verifyValue("//input[@name='BeginDate.month']", "01/2011");
		safeClick("//a[@title='Close']");
		verifyValue("//input[@name='EndDate.month']", "09/2015");
		// safeClick("//a[@title='Close']");
		driver.findElement(By.xpath("(//a[child::span/span[contains(text(),'Work Experience')]])[2]")).click();
		// safeClick("//a[@title='Close']");
		// takeScreenshot();
		driver.findElement(By.xpath("//input[@value='Next']")).click();

		textCheckpoint("Education", 30);
		verifyValue("//input[@placeholder='Institution*']", "Mansfield University of Pennsylvania");
		verifyValue("//input[@placeholder='Program*']", "Bachelors");
		verifyValue("//input[@name='graduationDate.month']", "05/2011");
		// safeClick("//a[@title='Close']");
		verifyValue("//select[@name='StudyLevel']", "Bachelor's Degree (±16 years)",
				"//label[contains(text(),'Education Level')]/following-sibling::div/div/select/option[contains(text(),'Bachelor')]");
		verifyValue("//select[@name='OBTAINED']", "Yes",
				"//label[contains(text(),'Degree')]/following-sibling::div/div/select/option[contains(text(),'Yes')]");
		// takeScreenshot();
		driver.findElement(By.xpath("//input[@value='Next']")).click();

		textCheckpoint("Important Questions", 30);
		softAssert.assertAll();
	}

	/////

	// @Step("Tear Down and Download Report")
	public void testTearDown() throws Exception {
		if (driver != null) {
			driver.close();
			if (isMobile) {
				downloadReport("html");
			}
			driver.quit();
		}
	}

	// @Attachment
	public byte[] saveImage(byte[] imageToSave) {
		return imageToSave;
	}

	@AfterTest
	public void closeWebDriver() throws SessionNotFoundException, IOException {
		// make sure web driver is closed
		try {
			if (driver.getSessionId() != null) {
				driver.close();
			}
			driver.quit();
		} catch (SessionNotFoundException e) {
		}
	}

	// @Attachment
	private byte[] downloadReport(String type) throws IOException {
		String command = "mobile:report:download";
		Map<String, String> params = new HashMap<>();
		params.put("type", type);
		String report = (String) driver.executeScript(command, params);
		byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report);
		return reportBytes;
	}

	// public static void downloadReportToLocal(String type, String fileName)
	// throws IOException {
	// try {
	// // call executeScript command to get the report
	// String command = "mobile:report:download";
	// Map<String, Object> params = new HashMap<>();
	// params.put("type", type);
	// String report = (String)driver.executeScript(command, params);
	// // store it on your local machine
	// File reportFile = new File("c:\\test\\" + fileName + "." + type);
	// BufferedOutputStream output = new BufferedOutputStream(new
	// FileOutputStream(reportFile));
	// byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report);
	// output.write(reportBytes); output.close();
	// } catch (Exception ex) {
	// System.out.println("Got exception " + ex); }
	// }

	private static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String, String> params = new HashMap<String, String>();
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

	// @Attachment
	public byte[] takeScreenshot() {
		System.out.println("Taking screenshot");
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

	// @Step("Is {0} displayed")
	protected void isElementDisplayed(String xpath) {
		softAssert.assertTrue(isElementPresent(By.xpath(xpath)), xpath);
	}

	protected boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	// @Step("Click {1}")
	protected void clickLink(String xpath, String desc) {
		try {
			driver.findElement(By.xpath(xpath)).click();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	// @Step("Enter {1}")
	protected void enterData(String xpath, String desc, String valueToSet) {
		driver.findElement(By.xpath(xpath)).sendKeys(valueToSet);
	}

	// protected void verifyText(String textToFind, Integer timeout) {
	// softAssert.assertTrue(textCheckpoint(textToFind, timeout), textToFind);
	// }

	protected void verifyValue(String xpath, String value) {
		String fieldValue = driver.findElement(By.xpath(xpath)).getAttribute("value");
		softAssert.assertTrue(fieldValue.equals(value), xpath);
		if (!fieldValue.equals(value)) {
			driver.findElement(By.xpath(xpath)).clear();
			driver.findElement(By.xpath(xpath)).sendKeys(value);
		}
	}

	protected void desktopCheckpoint(String xpath, String value) {
		String fieldValue = driver.findElement(By.xpath(xpath)).getAttribute("value");
		System.out.println(fieldValue);
		if (fieldValue.equals(value)) {

			report.updateTestLog(value, value + " text found", Status.PASS);
		} else {
			report.updateTestLog(value, value + " text not found", Status.FAIL);
		}

		// if (!fieldValue.equals(value)) {
		// driver.findElement(By.xpath(xpath)).clear();
		// driver.findElement(By.xpath(xpath)).sendKeys(value);
		// }
	}

	protected void verifyValue(String xpath, String value, String selectXpath) {
		String fieldValue = driver.findElement(By.xpath(xpath)).getAttribute("value");
		softAssert.assertTrue(fieldValue.equals(value), xpath);
		if (!fieldValue.equals(value)) {
			WebElement select = driver.findElement(By.xpath(xpath));
			WebElement option = select.findElement(By.xpath(selectXpath));
			option.click();
		}

	}

	protected void safeClick(String xpath) {
		try {
			driver.findElement(By.xpath(xpath)).click();
		} catch (NoSuchElementException e) {
		}
	}

}
