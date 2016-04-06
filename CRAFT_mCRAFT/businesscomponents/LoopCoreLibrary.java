package businesscomponents;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

public class LoopCoreLibrary extends ReusableLibrary {
	/**
	 * Constructor to initialize the component library
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object passed from the
	 *            {@link DriverScript}
	 */

	// protected RemoteWebDriver driver1;

	public LoopCoreLibrary(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}
	WebDriverUtil driverUtil = new WebDriverUtil(driver);
	private int waitTimeout = 10000;
	private int wait = 5000;
	
	private Map<String, Object> perfectoCommand = new HashMap<>();
	protected SoftAssert softAssert = new SoftAssert();
	private boolean rotate;
	private boolean isMobile;

	public void navigateToStagingURL(String stagingURL) {
	
		driver.get(stagingURL);
		// ((WebDriver) driver).manage().timeouts().implicitlyWait(2,TimeUnit.MINUTES);
		driverUtil.waitUntilElementVisible(By.xpath("//table[@id='jobResults']"), 400);

		if (driver.findElement(By.xpath("//table[@id='jobResults']")).isDisplayed()) {

			report.updateTestLog("Staging Page Navigation", "Navigation to Staging page Success", Status.PASS);
			try {
				WebElement attentionWindow = driver.findElement(By.xpath("//div[@id='domainCheckWindow']"));
				if (attentionWindow.isDisplayed()) {
					WebElement closeBtn = driver.findElement(By.xpath("//div[@id='domainCheckWindowClose']"));
					closeBtn.click();
					System.out.println("Attention dialog is displayed and then closed");
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

	protected void switchBrowserWindow(String browserTitle) {
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

	protected void externalLoginToLoopApplication(String extLoginID, String extLoginPwd) {
		driver.findElement(By.id("Email")).sendKeys(extLoginID);
		driver.findElement(By.id("Password")).sendKeys(extLoginPwd);
		driver.findElement(By.name("Login")).click();
	}

	// @ Login to internal staging page

	String intLoginID = "TaleoWyndham@wyn.com";
	String intLoginPwd = "Wyndham1";

	protected void internalLoginToLoopApplication(String intLoginID, String intLoginPwd) {
		driver.findElement(By.id("email")).sendKeys(intLoginID);
		driver.findElement(By.id("password")).sendKeys(intLoginPwd);
		driver.findElement(By.id("btnSubmit")).click();
	}

	// @Step("Create Profile") or ("Create Account")

	protected String createAccount(String userID, String password) {

		// driver.get(URL);
		driverUtil.waitUntilElementVisible(By.xpath("//span[text()='Create an account']"), 300);

		driver.findElement(By.xpath("//span[text()='Create an account']")).click();

		String output;
		output = new String(new Timestamp(System.currentTimeMillis()).toString());
		output = output.replace("-", "");
		output = output.replace(" ", "_");
		output = output.replace(":", "");
		output = userID + output + "@gmail.com";

		driver.findElement(By.xpath("//input[@id='Email']")).sendKeys(output);
		driver.findElement(By.xpath("//input[@id='Password']")).sendKeys(password);
		driver.findElement(By.xpath("//input[@id='ConfirmPassword']")).sendKeys(password);

		desktopCheckpoint("//input[@value='Create Account']", "Create Account");

		driver.findElement(By.xpath("//input[@value='Create Account']")).click();
		driverUtil.waitFor(waitTimeout);

		report.updateTestLog("Create Account", "Create Account Success", Status.PASS);
		return output;
	}

	// @Step("Privacy Agreement")

	protected void privacyAgreement() {

		desktopCheckpoint("//input[@name='ContinueBtn']", "Continue");
		driver.findElement(By.xpath("//input[@name='ContinueBtn']")).click();
		isElementDisplayed("//span[contains(text(),'Please accept our privacy agreement if you wish to continue')]");
		driver.findElement(By.xpath("//span[text()='I accept the privacy agreement']")).click();
		driver.findElement(By.xpath("//span[contains(text(),'Yes!')]")).click();
		driver.findElement(By.xpath("//input[@name='ContinueBtn']")).click();
		report.updateTestLog("privacyAgreement", "privacyAgreement selection success", Status.PASS);
	}

	// @Step("Upload Resume")

	protected void uploadResume(String srcType, String ID, String pwd, String localPath)
			throws InterruptedException, AWTException {

		switch (srcType) {
		case "LocalDrive":
			uploadResumeFromLocal(localPath);
			break;
		case "GoogleDrive":
			uploadResumeFromGoogleDrive(ID, pwd);
			break;
		case "DropBox":
			uploadResumeFromDropBox(ID, pwd);
			break;
		case "LinkedIn":
			uploadResumeFromLinkedIn(ID, pwd);
			break;
		case "Email":
			uploadResumeFromEmail(ID, pwd);
			break;
		case "NoResume":
			continueWithoutResume();
			break;
		}
	}

	protected void uploadResumeFromLocal(String localPath){
		driver.findElement(By.xpath("//span[text()='Upload local file']")).click();
		driverUtil.waitFor(waitTimeout);
		driver.findElement(By.xpath("//input[@data-buttontext='Choose file']")).click();
		WebDriverWait wait = new WebDriverWait((WebDriver) driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		System.out.println(driver.getTitle());
	}

	protected void uploadResumeFromGoogleDrive(String ID, String pwd){

		driver.findElement(By.xpath("//span[text()='Google Drive']")).click();
		driverUtil.waitFor(waitTimeout);

		if (isElementPresent(By.xpath("//a[text()='" + ID + "']"))) {
			System.out.println("Logged In");
		} else if (isElementPresent(By.xpath("//span[@id='reauthEmail']"))) {
			System.out.println("Partially logged in");

			driver.findElement(By.xpath("//input[@id='Passwd']")).sendKeys(pwd);
			driver.findElement(By.xpath("//input[@id='signIn']")).click();

		} else {
			System.out.println("Not logged in");
			driver.findElement(By.xpath("//input[@id='Email']")).sendKeys(ID);
			driver.findElement(By.xpath("//input[@id='next']")).click();
			driver.findElement(By.xpath("//input[@id='Passwd']")).sendKeys(pwd);
			driver.findElement(By.xpath("//input[@id='signIn']")).click();
		}

		driver.findElement(By.xpath("//button[@id='submit_approve_access']")).click();
		driver.findElement(By.xpath("//div[text()='Mike Smith.doc']")).click();
		isElementDisplayed("//p[contains(text(),'Success')]");
		driver.findElement(By.xpath("//span[text()='Click to continue']")).click();
		softAssert.assertAll();
		report.updateTestLog("uploadResume from GoogleDrive", "uploadResume from GoogleDrive Success", Status.PASS);
	}

	protected void uploadResumeFromDropBox(String ID, String pwd){

		report.updateTestLog("Resume Option", "Resume upload options displayed ", Status.PASS);
		driver.findElement(By.xpath("//span[text()='Dropbox']")).click();
		driverUtil.waitFor(wait);

		driver.findElement(By.xpath("//input[@name='login_email']")).sendKeys(ID);
		driver.findElement(By.xpath("//input[@name='login_password']")).sendKeys(pwd);

		report.updateTestLog("Dropbox Option", "Dropbox option selected for resume upload", Status.PASS);
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		driverUtil.waitFor(wait);
		driver.findElement(By.xpath("//button[@name='allow_access']")).click();
		driverUtil.waitFor(wait);

		report.updateTestLog("Dropbox Option", "Resume uploaded from Dropbox", Status.PASS);
		driver.findElement(By.xpath("//div[text()='SallySample.doc']")).click();
		driverUtil.waitFor(wait);

		report.updateTestLog("Dropbox Option", "Resume selected from Dropbox", Status.PASS);
		driver.findElement(By.xpath("//span[text()='Click to continue']")).click();
		driverUtil.waitFor(waitTimeout);
	}

	protected void uploadResumeFromLinkedIn(String ID, String pwd) {

		report.updateTestLog("Resume Option", "Resume upload options displayed ", Status.PASS);
		driver.findElement(By.xpath("//span[text()='LinkedIn']")).click();
		driverUtil.waitFor(waitTimeout);

		driver.findElement(By.xpath("//input[contains(@id='session_key')]")).sendKeys(ID);
		driver.findElement(By.xpath("//input[contains(@id='session_password')]")).sendKeys(pwd);
		driver.findElement(By.xpath("//input[@class='allow']]")).click();
		driverUtil.waitFor(waitTimeout);
	}

	protected void uploadResumeFromEmail(String ID, String pwd) throws InterruptedException {
		driver.findElement(By.xpath("//span[text()='Email']")).click();
		driverUtil.waitFor(waitTimeout);
	}

	protected void continueWithoutResume() throws InterruptedException {
		driver.findElement(By.xpath("//span[text()='Continue without résumé/CV']")).click();
		driverUtil.waitFor(waitTimeout);
	}

	// @Step("Tell Us About Yourself")

	protected void tellUsAboutYourself(String country, String state, String metro, String prfPhone, String phoneNo,
			String jobSrc, String srcVal) {
		//String countryXpath="candidate_personal_info_ResidenceLocation-0" ; 
		//String stateXpath="candidate_personal_info_ResidenceLocation-1" ; 
		//String nearstMetroXpath="candidate_personal_info_ResidenceLocation-2" ; 
		//String companyWebsiteXpath ="sourceTrackingBlock-recruitmentSourceType";
		//String countryXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-0";
		//String countryXpath ="///div/div/*[@name='et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-0']";
		//String stateXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-1";
		//String nearstMetroXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-2";
		//String companyWebsiteXpath = "et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_1-sourceTrackingBlock-recruitmentSourceType";

		driverUtil.waitFor(waitTimeout);
		new Select(driver.findElement(By.xpath("//*[contains(@name,'candidate_personal_info_ResidenceLocation-0')]"))).selectByVisibleText(country);
	//	new Select(driver.findElement(By.xpath()).selectByVisibleText(country);
		//new Select(driver.findElement(By.name("et-ef-content-ftf-gp-j_id_jsp_1295489848_15pc9-page_0-cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-0"))).selectByVisibleText(country);
		//new Select(driver.findElement(By.xpath("//*[class='ui-select']/div//select[contains(@name='cpi-cfrmsub-frm-dv_cs_candidate_personal_info_ResidenceLocation-0')]")).
		//new Select(driver.findElement(By.xpath(countryXpath))).selectByVisibleText(country);
		driverUtil.waitFor(wait);

		new Select(driver.findElement(By.xpath("//*[contains(@name,'candidate_personal_info_ResidenceLocation-1')]"))).selectByVisibleText(state);
		driverUtil.waitFor(wait);

		new Select(driver.findElement(By.xpath("//*[contains(@name,'candidate_personal_info_ResidenceLocation-2')]"))).selectByVisibleText(metro);
		driverUtil.waitFor(wait);

		new Select(driver.findElement(By.name("PreferredPhone"))).selectByVisibleText(prfPhone);
		driverUtil.waitFor(wait);

		report.updateTestLog("Tell us about yourself", "Details entered successfully", Status.PASS);

		new Select(driver.findElement(By.xpath("//*[contains(@name,'sourceTrackingBlock-recruitmentSourceType')]"))).selectByVisibleText(jobSrc);
		driverUtil.waitFor(wait);

		new Select(driver.findElement(By.name("dynamicElement1"))).selectByVisibleText(srcVal);
		driverUtil.waitFor(wait);

		report.updateTestLog("Tell us about yourself", "Source details entered successfully", Status.PASS);

		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		driverUtil.waitFor(wait);
	}

	// @Step("Work Experience")

	protected void updateWorkExperience(String responsibility) {
		driver.findElement(By.name("Responsibility")).sendKeys(responsibility);
		driverUtil.waitFor(wait);
		report.updateTestLog("Work Experience", "Work Experience Details entered successfully", Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		driverUtil.waitFor(wait);
	}

	// @Step("Education")

	protected void updateEducation(String degreeName, String degreeObtnd) {
		new Select(driver.findElement(By.name("StudyLevel"))).selectByVisibleText(degreeName);
		driverUtil.waitFor(wait);

		
		
		new Select(driver.findElement(By.name("OBTAINED"))).selectByVisibleText(degreeObtnd);
		driverUtil.waitFor(wait);
		report.updateTestLog("Education", "Education Details entered successfully", Status.PASS);
		driver.findElement(By.xpath("//input[@value='Next']")).click();
		driverUtil.waitFor(wait);
	}

	protected void selectImportantQuestionsAnswer_workAround() {
		answerRadioButton();
		answerTextBox();
		answerCheckBox();
		report.updateTestLog("Questions", "Details are entered successfully in questions tab", Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
	}

	protected void answerRadioButton() {
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

	protected void answerTextBox() {
		WebElement option = driver.findElement(By.xpath("//textarea[contains(@id,'stepElement')]"));
		option.sendKeys("Not Applicable");

	}

	protected void answerCheckBox() {
		WebElement option = driver.findElement(By.xpath("//span[@class='ui-btn-text' and text()=' Italian']"));
		option.click();
		
	}

	// @Step("Important Questions")

	protected void selectImportantQuestionsAnswer() {
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

	protected void updateImportantQuestionsRadio(String questn, String ans) {
		// Enter answer for Yes / no questions in Important Questions page

		List<WebElement> questions = driver
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

	protected void updateImportantQuestionsTextBox(String questn, String ans) {
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

	protected void updateImportantQuestionsMultipleCheckBox(String questn, String ans) {

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

	protected void updateDiversity(String gender, String ethnicity, String disablity, String option) {

		//String genderXpath = "pc10-0-questionSingleList";
		//String ethinicXpath = "pc10-1-questionSingleList";
		//String disablilityXpath = "pc10-2-questionSingleList";
		//String optionXpath = "pc10-3-questionSingleList";
	//	new Select(driver.findElement(By.xpath("//*[contains(@name,'candidate_personal_info_ResidenceLocation-0')]"))).selectByVisibleText(country);

		new Select(driver.findElement(By.xpath("//*[contains(@name,'pc10-0-questionSingleList')]"))).selectByVisibleText(gender);
		driverUtil.waitFor(wait);

		new Select(driver.findElement(By.xpath("//*[contains(@name,'pc10-1-questionSingleList')]"))).selectByVisibleText(ethnicity);
		driverUtil.waitFor(wait);

		new Select(driver.findElement(By.xpath("//*[contains(@name,'pc10-2-questionSingleList')]"))).selectByVisibleText(disablity);
		driverUtil.waitFor(wait);

		new Select(driver.findElement(By.xpath("//*[contains(@name,'pc10-3-questionSingleList')]"))).selectByVisibleText(option);
		driverUtil.waitFor(wait);

		report.updateTestLog("Diversity", "Diversity Details entered successfully", Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		driverUtil.waitFor(wait);
	}

	// @Step("eSignature")

	protected void enterEsignature(String eSignature){
		driver.findElement(By.name("FullName")).sendKeys(eSignature);
		driverUtil.waitFor(2000);
		report.updateTestLog("eSignature", "eSignature Details entered successfully", Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		driverUtil.waitFor(wait);
	}

	protected void verifyAttachments(String comment) {

		driver.findElement(By.id("fileComment")).sendKeys(comment);
		report.updateTestLog("Attachment", "Attachment Details entered successfully", Status.PASS);
		driverUtil.waitFor(wait);
		driver.findElement(By.xpath("//input[@value='Next']")).click();
		driverUtil.waitFor(wait);
	}

	protected void reviewAndSubmit() {
		report.updateTestLog("Review and Submit", "Application reviewed and submitted successfully", Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
		driverUtil.waitFor(wait);
	}

	protected void reviewCompletedApplication(){
		driverUtil.waitFor(wait);

		String fieldValue = driver.findElement(By.xpath("//span[@class='lwprogressbartext']")).getText();
		// if fieldValue.equals(anObject)
		if (fieldValue.equals("Application complete!")) {
			report.updateTestLog("Application Completed", "Application is completed successfully", Status.PASS);
		} else

			report.updateTestLog("Application Completed", "Application is completed successfully", Status.FAIL);
		driverUtil.waitFor(wait);
	}

	// verifyValue("//span[@class='lwprogressbartext']", "Application
	// complete!");

	protected void updateAreWeGoodMatch(){
		GoodMatchanswerCheckBox();
		GoodMatchanswerRadioButton();
		GoodMatchanswerTextBox();
		GoodMatchanswerSelectBox();
		report.updateTestLog("Are we good Match", "Details entered in Are we good Match tab successfully",Status.PASS);
		driver.findElement(By.id("lwAppBtnSaveNext")).click();
	}

	public void GoodMatchanswerRadioButton() {

		WebElement option = driver
				.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' Yes'])[1]"));
		option.click();

	//	option = driver
	//			.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' No'])[2]"));
	//	option.click();

	//	option = driver
//				.findElement(By.xpath("//span[@class='ui-btn-text' and text()=' Yes, but I require sponsorship']"));
	//	option.click();

	//	option = driver
	//			.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' Yes'])[4]"));
	//	option.click();

	//	option = driver
	//			.findElement(By.xpath("(//div[@class='ui-radio']//span[@class='ui-btn-text' and text()=' No'])[5]"));
	//	option.click();

	}

	public void GoodMatchanswerCheckBox() {
		WebElement option = driver
					.findElement(By.xpath("//span[@class='ui-btn-text' and text()='I have provided an accurate assessment of my experience, knowledge and abilities for the listed competencies.']"));
		option.click();
		
		
		// driver.findElement(By.id("lwAppBtnSaveNext")).click();
		option = driver
		.findElement(By.xpath("//span[@class='ui-btn-text' and text()=' English']"));
		option.click();
		
		option = driver
		.findElement(By.xpath("//span[@class='ui-btn-text' and text()=' French']"));
		option.click();
		
	}
	
	protected void GoodMatchanswerTextBox() {
		WebElement option = driver.findElement(By.xpath("//textarea[contains(@id,'stepElement')]"));
		option.sendKeys("No but i am ready to take the ASE Test");

	}
	
	protected void GoodMatchanswerSelectBox() {
		new Select(driver.findElement(By.xpath("//*[contains(@name,'2pc12-2-qsl')]"))).selectByVisibleText("I seek an hourly or contract opportunity");
		driverUtil.waitFor(wait);
		// and contains(@id,'stepElement')

	}

	// @Step("Data Verification")
	protected void dataVerification() {
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
	protected byte[] downloadReport(String type) throws IOException {
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

	protected static void switchToContext(RemoteWebDriver driver, String context) {
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


