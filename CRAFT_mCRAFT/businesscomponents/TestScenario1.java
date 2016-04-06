package businesscomponents;

import supportlibraries.ScriptHelper;

import java.io.FileWriter;
import java.io.Writer;

import org.testng.annotations.Test;
import businesscomponents.LoopCoreLibrary;;

public class TestScenario1 extends LoopCoreLibrary {

	public TestScenario1(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	final String LOOP_DATA = "General_Data_E";

	private String stagingURL = dataTable.getData(LOOP_DATA, "stagingURL");
	
	private String jobKeyword = dataTable.getData(LOOP_DATA, "jobKeyword");
	private String jobLocation = dataTable.getData(LOOP_DATA, "jobLocation");
	
	private String browserTitle = dataTable.getData(LOOP_DATA, "browserTitle");
	
	private String userName = dataTable.getData(LOOP_DATA, "Username");
	private String password = dataTable.getData(LOOP_DATA, "Password");

	private String ID =dataTable.getData(LOOP_DATA, "gmailID");
	private String pwd = dataTable.getData(LOOP_DATA, "gmailPwd");
	
	private String resumeSource = dataTable.getData(LOOP_DATA, "resumeSource");
	private String localResumePath = dataTable.getData(LOOP_DATA, "localResumePath");
	
	private String countryName = dataTable.getData(LOOP_DATA, "countryName");
	private String stateName = dataTable.getData(LOOP_DATA, "stateName");
	private String metroName = dataTable.getData(LOOP_DATA, "nearestMetroName");
	private String prefrdPhone = dataTable.getData(LOOP_DATA, "preferredPhone");
	private String phoneNo = dataTable.getData(LOOP_DATA, "phoneNo");
	private String jobSource = dataTable.getData(LOOP_DATA, "jobSource");
	private String sourceVal = dataTable.getData(LOOP_DATA, "sourceVal");
	
	private String responsibility = dataTable.getData(LOOP_DATA, "responsibility");
	
	private String degreeName = dataTable.getData(LOOP_DATA, "degreeName");
	private String degreeObtanied = dataTable.getData(LOOP_DATA, "degreeObtained");
	
	private String gender = dataTable.getData(LOOP_DATA, "gender");
	private String ethnicity = dataTable.getData(LOOP_DATA, "ethnicity");
	private String disablity = dataTable.getData(LOOP_DATA, "disablity");
	private String option = dataTable.getData(LOOP_DATA, "option");

	private String eSignature = dataTable.getData(LOOP_DATA, "eSignature");
	
	private String comment = dataTable.getData(LOOP_DATA, "comment");
	private String ClientLoopURL ="https://client.loopworks.com/" ;
	ClientLoop Obj=new ClientLoop(scriptHelper);
	LoopCoreLibrary objLoopCoreLibrary=new LoopCoreLibrary(scriptHelper);
	String CandidateType ="External";
	
	TaleoApp objTaleoApp =new TaleoApp(scriptHelper);
	
	//String EmailId="TaleoWyndham+20160405_063704.89@gmail.com";
	@Test
	public void runTestScenario() throws Exception {
		try {
			
			
		//	private String stagingURLnew="https://wynstaging.loopapply.com/apply/70/1184765"
			//driver.get("https://wynstaging.loopapply.com/apply/70/1184765");
			
		//	Obj.ClientLoopCRM(ClientLoopURL,EmailId,CandidateType);
			//objTaleoApp.taleoTest();
			
			
	
	/*...*/
	 
			navigateToStagingURL(stagingURL);
			searchAndApplyForRequisition(jobKeyword, jobLocation);
			switchBrowserWindow(browserTitle);
			String EmailId=objLoopCoreLibrary.createAccount(userName, password);			
			//createAccount(userName, password);
			/*privacyAgreement();
			uploadResume(resumeSource,ID,pwd,localResumePath);
			tellUsAboutYourself(countryName,stateName,metroName,prefrdPhone,phoneNo,jobSource,sourceVal);
			updateWorkExperience(responsibility);
			updateEducation(degreeName,degreeObtanied);
			//selectImportantQuestionsAnswer();
			selectImportantQuestionsAnswer_workAround();
			updateAreWeGoodMatch();
			updateDiversity(gender,ethnicity,disablity,option);
			enterEsignature(eSignature);
			verifyAttachments(comment);
			reviewAndSubmit();
			reviewCompletedApplication();
			Obj.ClientLoopCRM(ClientLoopURL,EmailId,CandidateType);*/
			
			//runTaleo//
			//Writer wr = new FileWriter("C:\\NarenMobile\\vignesh.txt");
			//wr.write(EmailId);
			//wr.close()
							
			objTaleoApp.taleoTest();
		/*................................................................................... */

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			testTearDown();
		}
	}

	
}
