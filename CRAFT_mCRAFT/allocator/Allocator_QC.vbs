'#######################################################################################################################
'Script Description		: Allocator Script to manage the batch execution of test cases
'Test Tool/Version		: VAPI-XP
'Test Tool Settings		: N.A.
'Application Automated	: N.A.
'Author					: Cognizant
'Date Created			: 04/07/2011
'#######################################################################################################################
Option Explicit	'Forcing Variable declarations

Dim gobjAllocator: Set gobjAllocator = New Allocator

'Associate required libraries
Dim gobjFso, gobjMyFile
Dim gstrRelativePath

Set gobjFso = CreateObject("Scripting.FileSystemObject")
gstrRelativePath = gobjFso.GetParentFolderName(WScript.ScriptFullName)
Set gobjMyFile = gobjFso.OpenTextFile(gstrRelativePath & "\supportlibraries\Framework_Reporting\ReportClasses.vbs", 1)	' 1 - For Reading
Execute gobjMyFile.ReadAll()

Set gobjMyFile = gobjFso.OpenTextFile(gstrRelativePath & "\supportlibraries\Framework_Reporting\ReportTypeClasses.vbs", 1)
Execute gobjMyFile.ReadAll()

Set gobjMyFile = gobjFso.OpenTextFile(gstrRelativePath & "\supportlibraries\Framework_Utilities\ExcelDataAccess.vbs", 1)
Execute gobjMyFile.ReadAll()

Set gobjMyFile = gobjFso.OpenTextFile(gstrRelativePath & "\supportlibraries\Framework_Utilities\GeneralUtility.vbs", 1)
Execute gobjMyFile.ReadAll()

Set gobjMyFile = gobjFso.OpenTextFile(gstrRelativePath & "\supportlibraries\Framework_Core\TestParameters.vbs", 1)
Execute gobjMyFile.ReadAll()

Set gobjMyFile = gobjFso.OpenTextFile(gstrRelativePath & "\supportlibraries\Framework_Core\FrameworkParameters.vbs", 1)
Execute gobjMyFile.ReadAll()

Set gobjMyFile = gobjFso.OpenTextFile(gstrRelativePath & "\supportlibraries\Framework_Core\Settings_Java.vbs", 1)
Execute gobjMyFile.ReadAll()


Set gobjMyFile = Nothing
Set gobjFso = Nothing

'#######################################################################################################################
'Class Description		: Class to manage the batch execution of test cases
'Author					: Cognizant
'Date Created			: 23/07/2012
'#######################################################################################################################
Class Allocator
	Private m_objQcConnection
	Private m_strTestSetPath, m_strTestSetName
	Private m_intTestBatchStatus, m_blnStopExecution
	Private m_dtmOverallStartTime, m_dtmOverallEndTime
	Private m_strQcUrl, m_strUsername, m_strDomain, m_strProject
	
	
	'###################################################################################################################
	Public Property Let TestSetPath(strTestSetPath)
		m_strTestSetPath = strTestSetPath
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Let TestSetName(strTestSetName)
		m_strTestSetName = strTestSetName
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub Class_Initialize()
		m_intTestBatchStatus = 0
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to connect to QC
	'Input Parameters		: strQcUrl, strUsername, strPassword, strDomain, strProject
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 28/07/2014
	'###################################################################################################################
	Public Sub ConnectToQc(strQcUrl, strUsername, strPassword, strDomain, strProject)
		Set m_objQcConnection = CreateObject("tdapiole80.tdconnection")
		m_objQcConnection.InitConnectionEx strQcUrl
		m_objQcConnection.ConnectProjectEx strDomain, strProject, strUsername, strPassword

		
		If (m_objQcConnection.Connected <> True) Then
			Err.Raise 6001, "Allocator", "Failed to connect to the specified QC project!"
		End If
		
		m_strQcUrl = strQcUrl
		m_strUsername = strUsername
		m_strDomain = strDomain
		m_strProject = strProject
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to set relative path
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 24/04/2012
	'###################################################################################################################
	Public Sub SetRelativePath()
		Dim objFso : Set objFso = CreateObject("Scripting.FileSystemObject")
		gobjFrameworkParameters.RelativePath = objFso.GetParentFolderName(WScript.ScriptFullName)
		Set objFso = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to initialize the test batch execution
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 24/04/2012
	'###################################################################################################################
	Public Sub InitializeTestBatch()
		m_dtmOverallStartTime = Now()
		
		If WScript.Arguments.Count > 0 Then
			m_strTestSetPath = WScript.Arguments.Item(0)
			m_strTestSetName = WScript.Arguments.Item(1)
		End If
		
		gobjFrameworkParameters.RunConfiguration = m_strTestSetName
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to initialize summary report
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 03/05/2012
	'###################################################################################################################
	Public Function InitializeSummaryReport()
		InitializeReportSettings()
		
		gobjReport.InitializeReport()
		gobjReport.InitializeResultSummary()
		gobjReport.AddResultSummaryHeading gobjReportSettings.ProjectName + " - " + "Automation Execution Result Summary"
		gobjReport.AddResultSummarySubHeading "Date & Time", ": " & m_dtmOverallStartTime, _
												"On Error", ": " & gobjSettings.GetValue("OnError")
		gobjReport.AddResultSummarySubHeading "QC URL", ": " & m_strQcUrl, _
												"Username", ": " & m_strUsername
		gobjReport.AddResultSummarySubHeading "Domain", ": " & m_strDomain, _
												"Project", ": " & m_strProject
		gobjReport.AddResultSummarySubHeading "Test Set Path", ": " & m_strTestSetPath, _
												"Test Set Name", ": " & m_strTestSetName
		gobjReport.AddResultSummarySubHeading "Framework Path", ": " & gobjFrameworkParameters.RelativePath, _
												"No. of threads", ": 1"
		
		gobjReport.AddResultSummaryTableHeadings()
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function InitializeReportSettings()
		gobjReportSettings.ReportPath = SetUpTempResultFolder()
		gobjReportSettings.ProjectName = gobjSettings.GetValue("ProjectName")
		gobjReportSettings.ExcelReport = False
		gobjReportSettings.HtmlReport = True
		gobjReportSettings.LinkTestLogsToSummary = False
		gobjReportSettings.ReportTheme = gobjSettings.GetValue("ReportsTheme")
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function SetUpTempResultFolder()
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		
		Dim strTempResultPath	'Using the Windows temp folder to store the results before uploading to QC
		strTempResultPath = objFso.GetSpecialFolder(2) & "\Run_mm-dd-yyyy_hh-mm-ss_XX"
		
		'Create Temp results folder if it does not exist
		If Not objFso.FolderExists (strTempResultPath) Then
			objFso.CreateFolder(strTempResultPath)
		End If
		
		strTempResultPath = strTempResultPath & "\Summary Report"
		
		'Delete summary report folder if it already exists
		If objFso.FolderExists(strTempResultPath) Then
			objFso.DeleteFolder(strTempResultPath)
			
			'Wait until the folder is successfully deleted
			Do While(1)
				If Not objFso.FolderExists(strTempResultPath) Then
					Exit Do
				End If
			Loop
		End If
		
		'Create fresh summary report folder
		objFso.CreateFolder(strTempResultPath)
		
		SetUpTempResultFolder = strTempResultPath
		
		'Release all objects
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to execute the Test Batch Run
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 27/04/2006
	'###################################################################################################################
	Public Sub DriveBatchExecution()
		Dim objTestSetFactory: Set objTestSetFactory = m_objQcConnection.TestSetFactory
		Dim objTestSetTreeManager: Set objTestSetTreeManager = m_objQcConnection.TestSetTreeManager
		Dim objTestSetFolder: Set objTestSetFolder = objTestSetTreeManager.NodeByPath(m_strTestSetPath)
		
		If objTestSetFolder Is Nothing Then
			Err.Raise 6002, "Allocator", "The specified Test Set path does not exist!"
		End If
		
		Dim objTestSetList: Set objTestSetList = objTestSetFolder.FindTestSets(m_strTestSetName)
		If objTestSetList.Count > 1 Then
			Err.Raise 6003, "Allocator",_
						"The specified Test Set name matches more than one test set within the given path! " &_
						"Please provide a unique name..."
		ElseIf objTestSetList.Count < 1 Then
			Err.Raise 6003, "Allocator", "The specified Test Set is not found in the given path!"
		End If
		
		Dim objTestSet: Set objTestSet = objTestSetList.Item(1)
		Dim objScheduler: Set objScheduler = objTestSet.StartExecution("")
		objScheduler.RunAllLocally = True
		
		Dim lstTSTests: Set lstTSTests = GetRunInfo(objTestSet)
		objScheduler.Run(lstTSTests)
		
		Dim objExecutionStatus: Set objExecutionStatus = objScheduler.ExecutionStatus
		WaitUntilTestSetExecutionFinished objExecutionStatus
		UpdateTestBatchStatus lstTSTests

		
		'Release all objects
		Set objExecutionStatus = Nothing
		Set lstTSTests = Nothing
		Set objScheduler = Nothing
		Set objTestSet = Nothing
		Set objTestSetList = Nothing
		Set objTestSetFolder = Nothing
		Set objTestSetTreeManager = Nothing
		Set objTestSetFactory = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function GetRunInfo(objTestSet)
		Dim objTSTestFactory: Set objTSTestFactory = objTestSet.TSTestFactory
		Dim lstTSTests: Set lstTSTests = objTSTestFactory.NewList("")
		Dim i, arrTestParameters
		For i = lstTSTests.Count To 1 Step -1
			If IsFlaggedForExecution(lstTSTests.Item(i).Test.Field("TS_SUBJECT").Name,_
											lstTSTests.Item(i).Test.Name,_
											lstTSTests.Item(i).Instance, arrTestParameters) Then						
				SetTestParameters lstTSTests.Item(i), arrTestParameters
			Else
				lstTSTests.Remove(i)
			End If
		Next
		
		If lstTSTests.Count = 0 Then
			Err.Raise 6003, "Allocator", "No test cases flagged for execution in the specified run configuration!"
		End If
		
		Set GetRunInfo = lstTSTests
		
		'Release all objects
		Set lstTSTests = Nothing
		Set objTSTestFactory = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function IsFlaggedForExecution(strTestScenario, strTestCase, intTestInstance, arrTestParameters)
		gobjExcelDataAccess.DatabasePath = gobjFrameworkParameters.RelativePath
		gobjExcelDataAccess.DatabaseName = "Run Manager"
		gobjExcelDataAccess.Connect()
		
		Dim strSheetName, strQuery, objTestData
		strSheetName = gobjFrameworkParameters.RunConfiguration
		Set objTestData = CreateObject("ADODB.Recordset")
		objTestData.CursorLocation = 3
		strQuery = "Select * from [" & strSheetName & "$]" &_
						" where TestScenario = '" & strTestScenario &_
						"' and TestCase = '" & strTestCase &_
						"' and TestInstance = " & intTestInstance &_
						" and Execute = 'Yes'"
		Set objTestData = gobjExcelDataAccess.ExecuteQuery(strQuery)
		gobjExcelDataAccess.Disconnect()
		If objTestData.RecordCount > 0 Then
			arrTestParameters = objTestData.GetRows()
			IsFlaggedForExecution = True
		Else
			IsFlaggedForExecution = False
		End If
		
		objTestData.Close
		Set objTestData = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub SetTestParameters(objCurrentTSTest, arrTestParameters)
		Dim objParamValueFactory: Set objParamValueFactory = objCurrentTSTest.ParameterValueFactory
		Dim lstParams: Set lstParams = objParamValueFactory.NewList("")
		Dim objParam
				
		For Each objParam in lstParams
			Select Case objParam.Name
				Case "IterationMode":
					objParam.ActualValue = arrTestParameters(5, 0)
				Case "StartIteration":
					objParam.ActualValue = arrTestParameters(6, 0)
				Case "EndIteration":
					objParam.ActualValue = arrTestParameters(7, 0)
				Case "ExecutionMode":
					objParam.ActualValue = arrTestParameters(8, 0)
				Case "MobileToolName":
					objParam.ActualValue = arrTestParameters(9, 0)
				Case "MobileExecutionPlatform":
					objParam.ActualValue = arrTestParameters(10, 0)		
				Case "DeviceName":
					objParam.ActualValue = arrTestParameters(12, 0)
				Case "Browser":
					objParam.ActualValue = arrTestParameters(13, 0)
					gobjTestParameters.Browser = arrTestParameters(13, 0)
					If IsNull(gobjTestParameters.Browser) Then
						gobjTestParameters.Browser = gobjSettings.GetValue("DefaultBrowser")
					End If
				Case "BrowserVersion":
					objParam.ActualValue = arrTestParameters(14, 0)
					gobjTestParameters.BrowserVersion = arrTestParameters(14, 0)
				Case "Platform":
					objParam.ActualValue = arrTestParameters(15, 0)
					gobjTestParameters.Platform = arrTestParameters(15, 0)
					If IsNull(gobjTestParameters.Platform) Then
						gobjTestParameters.Platform = gobjSettings.GetValue("DefaultPlatform")
					End If
			End Select
			
			objParam.Post
		Next
		
		'Release all objects
		Set objParam = Nothing
		Set lstParams = Nothing
		Set objParamValueFactory = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub WaitUntilTestSetExecutionFinished(objExecutionStatus)
		Dim blnRunFinished: blnRunFinished = False
		
		While blnRunFinished = False
			objExecutionStatus.RefreshExecStatusInfo "all", True
			blnRunFinished = objExecutionStatus.Finished
			
			WScript.Sleep(30000)
		Wend
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub UpdateTestBatchStatus(lstTSTests)
		Dim strExecutionTime, strTestStatus
		Dim i

		For i = 1 to lstTSTests.Count
			gobjTestParameters.CurrentScenario = lstTSTests.Item(i).Test.Field("TS_SUBJECT").Name
			gobjTestParameters.CurrentTestcase = lstTSTests.Item(i).Test.Name
			gobjTestParameters.CurrentTestInstance = "Instance" & lstTSTests.Item(i).Instance
			m_objQcConnection.IgnoreHTMLFormat = True
			gobjTestParameters.CurrentTestDescription = lstTSTests.Item(i).Test.Field("TS_DESCRIPTION")
			'gobjTestParameters.AdditionalDetails = gobjTestParameters.GetBrowserAndPlatform()
			gobjReportSettings.ReportName = "N/A"
			strExecutionTime = "Approx. " & (lstTSTests.Item(i).LastRun.Field("RN_DURATION") + 1) & " minute(s)"
			strTestStatus = lstTSTests.Item(i).LastRun.Status
			
			If strTestStatus = "Failed" Then
				m_intTestBatchStatus = 1	'Any non-zero outcome indicates a failure in vbscript
			End If

			
			gobjReport.UpdateResultSummary gobjTestParameters.CurrentScenario,gobjTestParameters.CurrentTestcase,gobjTestParameters.CurrentTestDescription,_
			strExecutionTime, strTestStatus
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to do wrap-up activities after completing the test batch execution
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 10/07/2011
	'###################################################################################################################
	Public Sub WrapUp()
		DisconnectFromQc()
		m_dtmOverallEndTime = Now()
		CloseSummaryReport()

		If WScript.Arguments.Count = 0 Then
			LaunchHtmlSummaryReport()
		Else
			WScript.Quit m_intTestBatchStatus
		End If
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub DisconnectFromQc()
		'Disconnect from project
		If m_objQcConnection.Connected Then
			m_objQcConnection.Disconnect()
		End If
		
		'Logout active user
		If m_objQcConnection.LoggedIn Then
			m_objQcConnection.Logout()
		End If
		
		'Release the QC connection
		m_objQcConnection.ReleaseConnection()
		
		Set m_objQcConnection = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub CloseSummaryReport()
		Dim strTotalExecutionTime
		strTotalExecutionTime = gobjUtil.GetTimeDifference(m_dtmOverallStartTime, m_dtmOverallEndTime)
		gobjReport.AddResultSummaryFooter(strTotalExecutionTime)
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub LaunchHtmlSummaryReport()
		Dim objShell
		If gobjReportSettings.HtmlReport Then
			Set objShell = CreateObject("WScript.Shell")
			objShell.Run """" & gobjReportSettings.ReportPath & "\Html Results\Summary.html"""
			Set objShell = Nothing
		End If
	End Sub
	'###################################################################################################################
	
End Class
'#######################################################################################################################