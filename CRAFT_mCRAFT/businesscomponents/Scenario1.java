package businesscomponents;
import org.testng.annotations.Test;

import businesscomponents.*;
import supportlibraries.ScriptHelper;
public class Scenario1 extends LoopNewApplicant {

	public Scenario1(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	
	String resumeSource;
	String stagingURL;
	String jobKeyword;
	String jobLocation;
	String browserTitle;
	
	//String resumeSource = "DropBox";

	//String stagingURL = "http://wynexternalstage.loop.jobs/";
	// String stagingURL = "http://wynp2stage.loop.jobs/";

	//String jobKeyword = "US REQUISITION - TEST AUTOMATION";
	//String jobLocation = "Parsippany";

	//String browserTitle = "Welcome - Apply Process";
	
	@Test
	public void ScenarioExecution1() throws Exception {
		try {
		// TODO Auto-generated method stub

			navigateToStagingURL(stagingURL);
			searchAndApplyForRequisition(jobKeyword, jobLocation);
			switchBrowserWindow(browserTitle);
			
			
	} catch (Exception e) {
		System.out.println("Scenario is not executed");
		e.printStackTrace();
	}finally {
		testTearDown();
	}
	}
}


