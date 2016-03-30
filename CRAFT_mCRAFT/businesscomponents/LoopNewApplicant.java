package businesscomponents;

import java.awt.AWTException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.cognizant.framework.selenium.WebDriverUtil;
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler;

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

public class LoopNewApplicant extends ReusableLibrary {
	/**
	 * Constructor to initialize the component library
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object passed from the
	 *            {@link DriverScript}
	 */

	// protected RemoteWebDriver driver1;

	public LoopNewApplicant(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	private Map<String, Object> perfectoCommand = new HashMap<>();
	protected SoftAssert softAssert = new SoftAssert();
	private boolean rotate;
	private boolean isMobile;

	private String wyndhamGmailAcnt = "TaleoWyndham@gmail.com";
	private String wyndhamGmailAcntPwd = "Wyndham1";

	int waitTimeout = 10000;
	int wait = 5000;

	WebDriverUtil driverUtil = new WebDriverUtil(driver);

	// @input String would be : LocalDrive or DropBox or GoogleDrive or LinkedI
	// or Email or NoResume
	
	
	String resumeSource = "DropBox";

	String stagingURL = "http://wynexternalstage.loop.jobs/";
	// // String stagingURL = "http://wynp2stage.loop.jobs/";

	 String jobKeyword = "US REQUISITION - TEST AUTOMATION";
	 String jobLocation = "Parsippany";

	String browserTitle = "Welcome - Apply Process";

	@Test
	public void runLoopTest() throws Exception {
		try {

			navigateToStagingURL(stagingURL);
			searchAndApplyForRequisition(jobKeyword, jobLocation);
			switchBrowserWindow(browserTitle);

			createAccount();
			privacyAgreement();
			uploadResumeSourceSelection(resumeSource);
			tellUsAboutYourself();
			updateWorkExperience();
			updateEducation();
			selectImportantQuestionsAnswer_workAround();
			updateAreWeGoodMatch();
			updateDiversity();
			enterEsignature();
			verifyAttachments();
			reviewAndSubmit();
			reviewCompletedApplication();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			testTearDown();
		}
	}

	public void navigateToStagingURL(String stagingURL) {
		driver.get(stagingURL);
		driverUtil.waitUntilElementVisible(By.xpath("//table[@id='jobResults']"), 300);

		if (driver.findElement(By.xpath("//table[@id='jobResults']")).isDisplayed()) {

			report.updateTestLog("Staging Page Navigation", "Navigation to Staging page Success", Status.PASS);
			try {
				WebElement attentionWindow = driver.findElement(By.xpath("//div[@id='domainCheckWindow']"));
				if (attentionWindow.isDisplayed()) {
					WebElement closeBtn = driver.findElement(By.xpath("//div[@id='domainCheckWindowClose']"));
					closeBtn.click();
					System.out.println("Attention dialog is dispalyed and then closed");
				}
			} catch (Exception e) {
				System.out.println("Attention dialog is not dispalyed");
			}
		} else {
			report.updateTestLog("Staging Page Navigation", "Navigation to Staging page not Success", Status.FAIL);
		}
	}

	public void searchAndApplyForRequisition(String jobKeyword, String jobLocation) {

		driver.findElement(By.xpath("//input[contains(@class,'jobsearch_keyword')]")).sendKeys(jobKeyword);
		driver.findElement(By.xpath("//input[contains(@class,'jobsearch_location')]")).sendKeys(jobLocation);
		driver.findElement(By.xpath("//input[@value='SEARCH']")).click();

		driverUtil.waitUntilElementDisabled(By.xpath("//img[@id='jobloader']"), 300);
		driverUtil.waitFor(wait);

		new Select(driver.findElement(By.id("jobResultsOrderBy"))).selectByVisibleText("Date posted");

		driver.findElement(By.xpath("//input[@value='SEARCH']")).click();

		driverUtil.waitUntilElementDisabled(By.xpath("//img[@id='jobloader']"), 300);
		driverUtil.waitFor(wait);

		List<WebElement> jobLists = driver.findElements(By.xpath("//td[@class='jobResult']//a[@class='joblink']"));

		for (WebElement jobList : jobLists) {
			if (jobList.getText().equals(jobKeyword)) {
				jobList.click();
				break;
			}
		}
		driverUtil.waitUntilElementVisible(By.xpath("//div[@class='lbl_Jobdetail_desc']//a[text()='APPLY NOW']"), 120);
		driver.findElement(By.xpath("//div[@class='lbl_Jobdetail_desc']//a[text()='APPLY NOW']")).click();
	}

	// @ To switch browser window while applying from external staging page

	public void switchBrowserWindow(String browserTitle) {
		Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			System.out.println("window title: " + handle);
			driver.switchTo().window(handle);
			System.out.println(driver.getTitle());
			if (driver.getTitle().equals(browserTitle))
				break;
		}
	}

	// @ Login to external staging page

	String extLoginID = "TaleoWyndham+123@gmail.com";
	String extLoginPwd = "Wyndham1";

	public void externalLoginToLoopApplication(String extLoginID, String extLoginPwd) {
		driver.findElement(By.id("Email")).sendKeys(extLoginID);
		driver.findElement(By.id("Password")).sendKeys(extLoginPwd);
		driver.findElement(By.name("Login")).click();
	}

	// @ Login to internal staging page

	String intLoginID = "TaleoWyndham@wyn.com";
	String intLoginPwd = "Wyndham1";

	public void internalLoginToLoopApplication(String intLoginID, String intLoginPwd) {
		driver.findElement(By.id("email")).sendKeys(intLoginID);
		driver.findElement(By.id("password")).sendKeys(intLoginPwd);
		driver.findElement(By.id("btnSubmit")).click();
	}

	// @Step("Create Profile") or ("Create Account")

	private String URL = "https://wynstaging.loopapply.com/apply/70/1184765";
	private String emailID = "TaleoWyndham+";
	private String domain = "@gmail.com";
	private String password = "Wyndham1";

	public void createAccount() throws MalformedURLException, InterruptedException {

		// driver.get(URL);
		driverUtil.waitUntilElementVisible(By.xpath("//span[text()='Create an account']"), 300);

		driver.findElement(By.xpath("//span[text()='Create an account']")).click();

		String output;
		output = new String(new Timestamp(System.currentTimeMillis()).toString());
		output = output.replace("-", "");
		output = output.replace(" ", "_");
		output = output.replace(":", "");
		output = emailID + output + domain;

		driver.findElement(By.xpath("//input[@id='Email']")).sendKeys(output);
		driver.findElement(By.xpath("//input[@id='Password']")).sendKeys(password);
		driver.findElement(By.xpath("//input[@id='ConfirmPassword']")).sendKeys(password);

		desktopCheckpoint("//input[@value='Create Account']", "Create Account");

		driver.findElement(By.xpath("//input[@value='Create Account']")).click();
		Thread.sleep(waitTimeout);

		report.updateTestLog("Create Account", "Create Account Success", Status.PASS);
	}

	// @Step("Privacy Agreement")

	private void privacyAgreement() {

		desktopCheckpoint("//input[@name='ContinueBtn']", "Continue");
		driver.findElement(By.xpath("//input[@name='ContinueBtn']")).click();
		isElementDisplayed("//span[contains(text(),'Please accept our privacy agreement if you wish to continue')]");
		driver.findElement(By.xpath("//span[text()='I accept the privacy agreement']")).click();
		driver.findElement(By.xpath("//span[contains(text(),'Yes!')]")).click();
		driver.findElement(By.xpath("//input[@name='ContinueBtn']")).click();
		report.updateTestLog("privacyAgreement", "privacyAgreement selection success", Status.PASS);
	}

	// @Step("Upload Resume")

	private void uploadResumeSourceSelection(String soruceType) throws InterruptedException, AWTException {

		switch (soruceType) {
		case "LocalDrive":
			uploadResumeFromLocal();
			break;
		case "GoogleDrive":
			uploadResumeFromGoogleDrive();
			break;
		case "DropBox":
			uploadResumeFromDropBox();
			break;
		case "LinkedIn":
			uploadResumeFromLinkedIn();
			break;
		case "Email":
			uploadResumeFromEmail();
			break;
		case "NoResume":
			continueWithoutResume();
			break;
		}
	}

	private void uploadResumeFromLocal() throws InterruptedException, AWTException {

		driver.findElement(By.xpath("//span[text()='Upload local file']")).click();
		Thread.sleep(waitTimeout);

		driver.findElement(By.xpath("//input[@data-buttontext='Choose file']")).click();

		WebDriverWait wait = new WebDriverWait((WebDriver) driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());

		Alert alert = driver.switchTo().alert();
		System.out.println(driver.getTitle());
	}

	private void uploadResumeFromGoogleDrive() throws InterruptedException {

		driver.findElement(By.xpath("//span[text()='Google Drive']")).click();
		Thread.sleep(waitTimeout);

		if (isElementPresent(By.xpath("//a[text()='" + wyndhamGmailAcnt + "']"))) {
			System.out.println("Logged In");
		} else if (isElementPresent(By.xpath("//span[@id='reauthEmail']"))) {
			System.out.println("Partially logged in");

			driver.findElement(By.xpath("//input[@id='Passwd']")).sendKeys(wyndhamGmailAcntPwd);
			driver.findElement(By.xpath("//input[@id='signIn']")).click();

		} else {
			System.out.println("Not logged in");
			driver.findElement(By.xpath("//input[@id='Email']")).sendKeys(wyndhamGmailAcnt);
			driver.findElement(By.xpath("//input[@id='next']")).click();
			driver.findElement(By.xpath("//input[@id='Passwd']")).sendKeys(wyndhamGmailAcntPwd);
			driver.findElement(By.xpath("//input[@id='signIn']")).click();
		}

		driver.findElement(By.xpath("//button[@id='submit_approve_access']")).click();
		driver.findElement(By.xpath("//div[text()='Mike Smith.doc']")).click();
		isElementDisplayed("//p[contains(text(),'Success')]");
		driver.findElement(By.xpath("//span[text()='Click to continue']")).click();
		softAssert.assertAll();
		report.updateTestLog("uploadResume from GoogleDrive", "uploadResume from GoogleDrive Success", Status.PASS);
	}

	private void uploadResumeFromDropBox() throws InterruptedException {

		report.updateTestLog("Resume Option", "Resume upload options displayed ", Status.PASS);
		driver.findElement(By.xpath("//span[text()='Dropbox']")).click();
		Thread.sleep(wait);

		driver.findElement(By.xpath("//input[@name='login_email']")).sendKeys(wyndhamGmailAcnt);
		driver.findElement(By.xpath("//input[@name='login_password']")).sendKeys(wyndhamGmailAcntPwd);

		report.updateTestLog("Dropbox Option", "Dropbox option selected for resume upload", Status.PASS);
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		Thread.sleep(wait);
		driver.findElement(By.xpath("//button[@name='allow_access']")).click();
		Thread.sleep(wait);

		report.updateTestLog("Dropbox Option", "Resume uploaded from Dropbox", Status.PASS);
		driver.findElement(By.xpath("//div[text()='SallySample.doc']")).click();
		Thread.sleep(wait);

		report.updateTestLog("Dropbox Option", "Resume selected from Dropbox", Status.PASS);
		driver.findElement(By.xpath("//span[text()='Click to continue']")).click();
		Thread.sleep(waitTimeout);
	}

	private void uploadResumeFromLinkedIn() throws InterruptedException {

		report.updateTestLog("Resume Option", "Resume upload options displayed ", Status.PASS);
		driver.findElement(By.xpath("//span[text()='LinkedIn']")).click();
		Thread.sleep(waitTimeout);

		driver.findElement(By.xpath("//input[contains(@id='session_key')]")).sendKeys(wyndhamGmailAcnt);
		driver.findElement(By.xpath("//input[contains(@id='session_password')]")).sendKeys(wyndhamGmailAcntPwd);
		driver.findElement(By.xpath("//input[@class='allow']]")).click();
		Thread.sleep(waitTimeout);
	}

	private void uploadResumeFromEmail() throws InterruptedException {
		driver.findElement(By.xpath("//span[text()='Email']")).click();
		Thread.sleep(waitTimeout);
	}

	private void continueWithoutResume() throws InterruptedException {
		driver.findElement(By.xpath("//span[text()='Continue without résumé/CV']")).click();
		Thread.sleep(waitTimeout);
	}

	String countryName = "United States";
	String stateName = "Alabama";
	String nearestMetroName = "Anniston";
	String PreferredPhone = "Home Phone";

	// @Step("Tell Us About Yourself")

	private void tellUsAboutYourself() throws InterruptedException {

		String countryXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-0";
		String stateXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-1";
		String nearstMetroXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-2";
		String companyWebsiteXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_1-sourceTrackingBlock-recruitmentSourceType";

		Thread.sleep(waitTimeout);
		new Select(driver.findElement(By.name(countryXpath))).selectByVisibleText(countryName);
		Thread.sleep(wait);

		new Select(driver.findElement(By.name(stateXpath))).selectByVisibleText(stateName);
		Thread.sleep(wait);

		new Select(driver.findElement(By.name(nearstMetroXpath))).selectByVisibleText(nearestMetroName);
		Thread.sleep(wait);

		new Select(driver.findElement(By.name("PreferredPhone"))).selectByVisibleText(PreferredPhone);
		Thread.sleep(wait);

		report.updateTestLog("Tell us about yourself", "Details entered successfully", Status.PASS);

		new Select(driver.findElement(By.name(companyWebsiteXpath))).selectByVisibleText("Company Website");
		Thread.sleep(wait);

		new Select(driver.findElement(By.name("dynamicElement1"))).selectByVisibleText("RCI");
		Thread.sleep(wait);

		report.updateTestLog("Tell us about yourself", "Source details entered successfully", Status.PASS);

		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		Thread.sleep(wait);
	}

	// @Step("Work Experience")

	private void updateWorkExperience() throws InterruptedException {

		driver.findElement(By.name("Responsibility")).sendKeys("Program Manager");
		Thread.sleep(wait);
		report.updateTestLog("Work Experience", "Work Experience Details entered successfully", Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		Thread.sleep(wait);
	}

	// @Step("Education")

	String degreeName = "Higher Degree";
	String degreeObtanied = "Yes";

	private void updateEducation() throws InterruptedException {
		new Select(driver.findElement(By.name("StudyLevel"))).selectByVisibleText(degreeName);
		Thread.sleep(wait);
		new Select(driver.findElement(By.name("OBTAINED"))).selectByVisibleText(degreeObtanied);
		Thread.sleep(wait);
		report.updateTestLog("Education", "Education Details entered successfully", Status.PASS);
		driver.findElement(By.xpath("//input[@value='Next']")).click();
	}

	public void selectImportantQuestionsAnswer_workAround() {
		answerRadioButton();
		answerTextBox();
		answerCheckBox();
	}

	public void answerRadioButton() {
		WebElement option = driver
				.findElement(By.xpath("//span[@class='ui-btn-text' and text()=' Yes, but I require sponsorship']"));
		option.click();

		option = driver
				.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' Yes'])[2]"));
		option.click();

		option = driver
				.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' No'])[3]"));
		option.click();

		option = driver
				.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' Yes'])[4]"));
		option.click();
	}

	public void answerTextBox() {
		WebElement option = driver.findElement(By.xpath("//textarea[contains(@id,'stepElement')]"));
		option.sendKeys("Not Applicable");

	}

	public void answerCheckBox() {
		WebElement option = driver.findElement(By.xpath("//span[@class='ui-btn-text' and text()=' Italian']"));
		option.click();
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
	}

	// @Step("Important Questions")

	public void selectImportantQuestionsAnswer() {
		String questionsRadio[] = {
				"1. All offers of employment are conditioned upon your ability to provide evidence of your right to be legally employed. Are you authorized to work in the Country in which the job is located?*",
				"2. Have you previously been employed by Wyndham Worldwide or any of its subsidiaries or affiliates, including Wyndham Hotel Group, Wyndham Vacation Ownership, Wyndham Vacation Resorts - Asia Pacific or Wyndham Destination Network (formerly known as Wyndham Exchange & Rentals)?*",
				"3. Have you ever been terminated, laid off, discharged or asked to resign from any employment?*",
				"6. Are you currently employed by Wyndham Worldwide or any of its subsidiaries or affiliates, including Wyndham Hotel Group, Wyndham Vacation Ownership, Wyndham Vacation Resorts - Asia Pacific or Wyndham Destination Network (formerly known as Wyndham Exchange & Rentals)?*" };

		String answersRadio[] = { "Yes, but I require sponsorship", "No", "Yes", "Yes" };

		String questionsTextBox[] = { "4. If yes, give the employer(s) and reason(s) for each discharge/resignation." };
		String answersTextBox[] = { "Not Applicable" };

		String questionsCheckBox = "5. Please select any languages, in addition to English, in which you are proficient (select all that apply).";
		String answersCheckBox[] = { "Arabic", "Danish" };

		for (int j = 0; j < questionsRadio.length; j++)
			updateImportantQuestionsRadio(questionsRadio[j], answersRadio[j]);

		for (int j = 0; j < questionsTextBox.length; j++)
			updateImportantQuestionsTextBox(questionsTextBox[j], answersTextBox[j]);

		for (int j = 0; j < answersCheckBox.length; j++)
			updateImportantQuestionsMultipleCheckBox(questionsCheckBox, answersCheckBox[j]);
	}

	private void updateImportantQuestionsRadio(String questn, String ans) {
		// Enter answer for Yes / no questions in Important Questions page

		// List<WebElement> questions =
		// driver.findElements(By.xpath("//fieldset[contains(@id,'stepElement')]/div/label[@class='question-label']"));

		Set<WebElement> questions = (Set<WebElement>) driver
				.findElements(By.xpath("//fieldset[contains(@id,'stepElement')]/div/label[@class='question-label']"));

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

	// @Step("Diversity")

	String gender = "Male";
	String ethnicity = "Hispanic or Latino";
	String disablity = "No, I do not have a disability";
	String option = "Yes";

	private void updateDiversity() throws InterruptedException {

		String genderXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-diversityBlock-j_id_jsp_2086517189_11pc10-0-j_id_jsp_2086517189_14pc10-0-questionSingleList";
		String ethinicXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-diversityBlock-j_id_jsp_2086517189_11pc10-0-j_id_jsp_2086517189_14pc10-1-questionSingleList";
		String disablilityXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-diversityBlock-j_id_jsp_2086517189_11pc10-0-j_id_jsp_2086517189_14pc10-2-questionSingleList";
		String optionXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-diversityBlock-j_id_jsp_2086517189_11pc10-0-j_id_jsp_2086517189_14pc10-3-questionSingleList";

		new Select(driver.findElement(By.name(genderXpath))).selectByVisibleText(gender);
		Thread.sleep(wait);

		new Select(driver.findElement(By.name(ethinicXpath))).selectByVisibleText(ethnicity);
		Thread.sleep(wait);

		new Select(driver.findElement(By.name(disablilityXpath))).selectByVisibleText(disablity);
		Thread.sleep(wait);

		new Select(driver.findElement(By.name(optionXpath))).selectByVisibleText(option);
		Thread.sleep(wait);

		report.updateTestLog("Diversity", "Diversity Details entered successfully", Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		Thread.sleep(wait);
	}

	// @Step("eSignature")

	String eSignature = "Sally Adamson";

	private void enterEsignature() throws InterruptedException {
		driver.findElement(By.name("FullName")).sendKeys(eSignature);
		Thread.sleep(2000);
		report.updateTestLog("eSignature", "eSignature Details entered successfully", Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		Thread.sleep(wait);
	}

	private void verifyAttachments() throws InterruptedException {

		driver.findElement(By.id("fileComment")).sendKeys("Cover Letter comment");
		report.updateTestLog("Attachment", "Attachment Details entered successfully", Status.PASS);
		Thread.sleep(wait);
		driver.findElement(By.xpath("//input[@value='Next']")).click();
		Thread.sleep(wait);
	}

	private void reviewAndSubmit() throws InterruptedException {
		report.updateTestLog("Review and Submit", "Application reviewed and submitted successfully", Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		Thread.sleep(wait);
	}

	private void reviewCompletedApplication() throws InterruptedException {
		Thread.sleep(wait);

		String fieldValue = driver.findElement(By.xpath("//span[@class='lwprogressbartext']")).getText();
		// if fieldValue.equals(anObject)
		if (fieldValue.equals("Application complete!")) {
			report.updateTestLog("Application Completed", "Application is completed successfully", Status.PASS);
		} else

			report.updateTestLog("Application Completed", "Application is completed successfully", Status.FAIL);
		Thread.sleep(wait);
	}

	// verifyValue("//span[@class='lwprogressbartext']", "Application
	// complete!");

	private void updateAreWeGoodMatch() throws InterruptedException {
		GoodMatchanswerCheckBox();
		GoodMatchanswerRadioButton();
		// answerTextBox();
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
	}

	public void GoodMatchanswerRadioButton() {

		WebElement option = driver
				.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' Yes'])[1]"));
		option.click();

		option = driver
				.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' No'])[2]"));
		option.click();

		option = driver
				.findElement(By.xpath("//span[@class='ui-btn-text' and text()=' Yes, but I require sponsorship']"));
		option.click();

		option = driver
				.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' Yes'])[4]"));
		option.click();

		option = driver
				.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' No'])[5]"));
		option.click();

	}

	public void GoodMatchanswerCheckBox() {
		WebElement option = driver.findElement(By.xpath(
				"//span[@class='ui-btn-text' and text()='I have provided an accurate assessment of my experience, knowledge and abilities for the listed competencies.']"));
		option.click();
		// driver.findElement(By.id("lwAppBtnSaveNext")).click();
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