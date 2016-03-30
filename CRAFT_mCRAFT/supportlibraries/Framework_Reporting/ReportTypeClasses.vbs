'#######################################################################################################################
'Script Description		: Excel Report Library
'Test Tool/Version		: HP Quick Test Professional 11+
'Test Tool Settings		: N.A.
'Application Automated	: N.A.
'Author					: Cognizant
'Date Created			: 04/07/2011
'#######################################################################################################################
Option Explicit	'Forcing Variable declarations

Dim gobjExcelReport: Set gobjExcelReport = New ExcelReport
Dim gobjHtmlReport: Set gobjHtmlReport = New HtmlReport

'#######################################################################################################################
'Class Description		: Class to handle Excel reporting
'Author					: Cognizant
'Date Created			: 23/07/2012
'#######################################################################################################################
Class ExcelReport
	Private m_intCurrentRowNumber
	Private m_intCurrentSummaryRowNumber
	
	
	'###################################################################################################################
	Private Sub Class_Initialize()
		m_intCurrentRowNumber = 1
		m_intCurrentSummaryRowNumber = 1
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to create test log file
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function InitializeTestLog()
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = gobjReportSettings.ReportName
		gobjExcelDataAccess.Connect()
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Create table [Cover_Page] " &_
						"(Test memo, Log memo, Cover memo, Page memo)"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		
		strNonQuery = "Create table [Test_Log] " &_
						"(Step_No varchar(200), Step_Name varchar(200), Description memo, " &_
						"Status varchar(200), Step_Time varchar(200))"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add test log heading
	'Input Parameters		: strHeading
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogHeading(strHeading)
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = gobjReportSettings.ReportName
		gobjExcelDataAccess.Connect()
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Insert into [Cover_Page] " &_
															"values ('" & HandleInvalidSqlCharacters(strHeading) & "', ' ', ' ', ' ')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
		
		m_intCurrentRowNumber = m_intCurrentRowNumber + 1
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function HandleInvalidSqlCharacters(strQueryParameter)
		HandleInvalidSqlCharacters = Replace(strQueryParameter, "'", "''")
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add test log subheading
	'Input Parameters		: strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogSubHeading(strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4)
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = gobjReportSettings.ReportName
		gobjExcelDataAccess.Connect()
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Insert into [Cover_Page] " &_
										"values ('" & HandleInvalidSqlCharacters(strSubHeading1) & "', '" & _
														HandleInvalidSqlCharacters(strSubHeading2) & "', '" &_
														HandleInvalidSqlCharacters(strSubHeading3) & "', '" &_
														HandleInvalidSqlCharacters(strSubHeading4) & "')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
		
		m_intCurrentRowNumber = m_intCurrentRowNumber + 1
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add test log table headings
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogTableHeadings()
		'Nothing to do
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add test log section
	'Input Parameters		: strSection
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogSection(strSection)
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = gobjReportSettings.ReportName
		gobjExcelDataAccess.Connect()
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Insert into [Test_Log] (Step_No) values ('" & HandleInvalidSqlCharacters(strSection) & "')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add test log sub-section
	'Input Parameters		: strSubSection
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogSubSection(strSubSection)
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = gobjReportSettings.ReportName
		gobjExcelDataAccess.Connect()
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Insert into [Test_Log] (Step_No) values ('" & HandleInvalidSqlCharacters(strSubSection) & "')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to update test log
	'Input Parameters		: intStepNumber, strStepName, strStepDescription, strStepStatus, strScreenShotName
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function UpdateTestLog(intStepNumber, strStepName, strStepDescription, strStepStatus, strScreenShotName)
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = gobjReportSettings.ReportName
		gobjExcelDataAccess.Connect()
		
		If (strStepStatus = "Fail") Then
			If (Environment.Value("TakeScreenshotFailedStep")) Then
				strStepDescription = strStepDescription + " (Refer Screenshot @ " + strScreenshotName + ")"
			End If
		End If
		If (strStepStatus = "Pass") Then
			If (Environment.Value("TakeScreenshotPassedStep")) Then
				strStepDescription = strStepDescription + " (Refer Screenshot @ " + strScreenshotName + ")"
			End If
		End If
		If (strStepStatus = "Screenshot") Then
			strStepDescription = strStepDescription + " (Refer Screenshot @ " + strScreenshotName + ")"
		End If
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Insert into [Test_Log] " &_
							"values ('" & intStepNumber & "' , '" & HandleInvalidSqlCharacters(strStepName) &_
										"' , '" & HandleInvalidSqlCharacters(strStepDescription) &_
										"' , '" & strStepStatus & "' , '" & Time() & "')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add test log footer
	'Input Parameters		: strExecutionTime, intStepsPassed, intStepsFailed
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogFooter(strExecutionTime, intStepsPassed, intStepsFailed)
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = gobjReportSettings.ReportName
		gobjExcelDataAccess.Connect()
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Insert into [Test_Log] (Step_Name, Description) " &_
									"values ('Execution Time (mins): ' , '" & strExecutionTime & "')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		
		strNonQuery = "Insert into [Test_Log] (Step_No, Step_Name, Status, Step_Time) " &_
										"values ('Steps Passed: ' , '" & intStepsPassed &_
												"', 'Steps Failed: ' , '" & intStepsFailed & "')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
	End Function
	'###################################################################################################################
	
	
	'###################################################################################################################
	'Function Description	: Function to initialize result summary
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function InitializeResultSummary()
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = "Summary"
		gobjExcelDataAccess.Connect()
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Create table [Cover_Page] " &_
							"(Result memo, Summary memo, Cover memo, Page memo)"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		
		strNonQuery = "Create table [Result_Summary] " &_
							"(Test_Scenario varchar(200), Test_Case varchar(200), Test_Description memo, " &_
							"Execution_Time varchar(200), Test_Status varchar(200))"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add heading to the result summary
	'Input Parameters		: strHeading
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddResultSummaryHeading(strHeading)
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = "Summary"
		gobjExcelDataAccess.Connect()
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Insert into [Cover_Page] " &_
									"values ('" & HandleInvalidSqlCharacters(strHeading) & "', ' ', ' ', ' ')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
		
		m_intCurrentSummaryRowNumber = m_intCurrentSummaryRowNumber + 1
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add sub-heading to the result summary
	'Input Parameters		: strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddResultSummarySubHeading(strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4)
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = "Summary"
		gobjExcelDataAccess.Connect()
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Insert into [Cover_Page] " &_
												"values ('" & HandleInvalidSqlCharacters(strSubHeading1) & "', '" &_
																HandleInvalidSqlCharacters(strSubHeading2) & "', '" &_
																HandleInvalidSqlCharacters(strSubHeading3) & "', '" &_
																HandleInvalidSqlCharacters(strSubHeading4) & "')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add table headings to the result summary
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddResultSummaryTableHeadings()
		'Nothing to do
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to update result summary
	'Input Parameters		: strScenarioName, strTestcaseName, strTestcaseDescription, strExecutionTime, strTestStatus
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function UpdateResultSummary(strScenarioName, strTestcaseName, strTestcaseDescription, _
																					strExecutionTime, strTestStatus)
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = "Summary"
		gobjExcelDataAccess.Connect()
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Insert into [Result_Summary] " &_
									"values ('" & HandleInvalidSqlCharacters(strScenarioName) & "', '" &_
													HandleInvalidSqlCharacters(strTestCaseName) & "', '" &_
													HandleInvalidSqlCharacters(strTestCaseDescription) & "', '" &_
													strExecutionTime & "', '" & strTestStatus & "')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add footer to the result summary
	'Input Parameters		: strTotalExecutionTime, intTestsPassed, intTestsFailed
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddResultSummaryFooter(strTotalExecutionTime, intTestsPassed, intTestsFailed)
		gobjExcelDataAccess.DatabasePath = gobjReportSettings.ReportPath & "\Excel Results"
		gobjExcelDataAccess.DatabaseName = "Summary"
		gobjExcelDataAccess.Connect()
		
		Dim strNonQuery, objTestData
		Set objTestData = CreateObject("ADODB.Recordset")
		strNonQuery = "Insert into [Result_Summary] (Test_Scenario, Test_Case) " &_
					"values ( 'Total Duration: ' , '" & strTotalExecutionTime & "')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		
		strNonQuery = "Insert into [Result_Summary] (Test_Scenario, Test_Case, Execution_Time, Test_Status) " &_
					"values ('Tests Passed: ' , '" & intTestsPassed & "', 'Tests Failed: ' , '" & intTestsFailed & "')"
		gobjExcelDataAccess.ExecuteNonQuery(strNonQuery)
		gobjExcelDataAccess.Disconnect()
	End Function
	'###################################################################################################################
	
End Class
'#######################################################################################################################
'#######################################################################################################################


'#######################################################################################################################
'Class Description		: Class to handle Html reporting
'Author					: Cognizant
'Date Created			: 23/07/2012
'#######################################################################################################################
Class HtmlReport
	Private m_intCurrentContentNumber
	Private m_strCurrentSection
	Private m_strCurrentSubSection
	Private m_blnTestLogMainTableCreated
	Private m_blnTestLogHeaderTableCreated
	Private m_blnResultSummaryHeaderTableCreated
	Private m_blnResultSummaryMainTableCreated
	
	
	'###################################################################################################################
	Private Sub Class_Initialize()
		m_intCurrentContentNumber = 0
		m_strCurrentSection = ""
		m_strCurrentSubSection = ""
		m_blnTestLogHeaderTableCreated = False
		m_blnTestLogMainTableCreated = False
		m_blnResultSummaryHeaderTableCreated = False
		m_blnResultSummaryMainTableCreated = False
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function GetThemeCss()
		Dim strThemeCss
		strThemeCss = vbTab & vbTab & "<style type='text/css'>" & vbcrlf &_
						vbTab & vbTab & vbTab &	"body {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "background-color: " & gobjReportTheme.ContentForeColor & ";" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab &	"font-family: Verdana, Geneva, sans-serif;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "text-align: center;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "small {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-size: 0.7em;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "table {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "border: 1px solid #4D7C7B;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "border-collapse: collapse;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "border-spacing: 0px;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "width: 95%;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "margin-left: auto;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "margin-right: auto;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "tr.heading {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "background-color: " & gobjReportTheme.HeadingBackColor & ";" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "color: " & gobjReportTheme.HeadingForeColor & ";" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-size: 0.9em;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-weight: bold;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "tr.subheading {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "background-color: " & gobjReportTheme.HeadingForeColor & ";" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "color: " & gobjReportTheme.HeadingBackColor & ";" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-weight: bold;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-size: 0.9em;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "text-align: justify;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "tr.section {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "background-color: " & gobjReportTheme.SectionBackColor & ";" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "color: " & gobjReportTheme.SectionForeColor & ";" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "cursor: pointer;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-weight: bold;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-size: 0.9em;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "text-align: justify;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "tr.subsection {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "cursor: pointer;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "tr.content {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "background-color: " & gobjReportTheme.ContentBackColor & ";" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "color: " & gobjReportTheme.ContentForeColor & ";" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-size: 0.9em;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "display: table-row;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "td {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "padding: 4px;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "text-align: inherit\0/;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "word-wrap: break-word;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "max-width: 450px;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "th {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "padding: 4px;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "text-align: inherit\0/;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "word-break: break-all;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "max-width: 450px;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "td.justified {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "text-align: justify;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "td.pass {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-weight: bold;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "color: green;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "td.fail {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-weight: bold;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "color: red;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "td.screenshot {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-weight: bold;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "color: navy;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "td.done {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-weight: bold;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "color: black;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "td.debug {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-weight: bold;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "color: blue;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf & vbcrlf &_
						_
						vbTab & vbTab & vbTab & "td.warning {" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "font-weight: bold;" & vbcrlf &_
						vbTab & vbTab & vbTab & vbTab & "color: orange;" & vbcrlf &_
						vbTab & vbTab & vbTab & "}" & vbcrlf &_
						vbTab & vbTab & "</style>" & vbcrlf & vbcrlf
		
		GetThemeCss = strThemeCss
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function GetJavascriptFunctions()
		Dim strJavascriptFunctions
		strJavascriptFunctions = vbTab & vbTab & "<script>" & vbcrlf &_
									vbTab & vbTab & vbTab & "function toggleMenu(objID) {" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "if (!document.getElementById) return;" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "var ob = document.getElementById(objID).style;" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "if(ob.display === 'none') {" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "try {" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & "ob.display='table-row-group';" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "} catch(ex) {" & vbcrlf &_	
									vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & "ob.display='block';" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "}" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "}" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "else {" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "ob.display='none';" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "}" & vbcrlf &_
									vbTab & vbTab & vbTab & "}" & vbcrlf &_
									_
									vbTab & vbTab & vbTab & "function toggleSubMenu(objId) {" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "for(i=1; i<10000; i++) {" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "var ob = document.getElementById(objId.concat(i));" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "if(ob === null) {" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & "break;" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "}" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "if(ob.style.display === 'none') {" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & vbTab &	"try { " & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & "ob.style.display='table-row';" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & "} catch(ex) {" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & "ob.style.display='block';" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & "}" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "}" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "else {" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & "ob.style.display='none';" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "}" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "}" & vbcrlf &_
									vbTab & vbTab & vbTab & "}" & vbcrlf &_
									vbTab & vbTab & "</script>" & vbcrlf
		
		GetJavascriptFunctions = strJavascriptFunctions
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to create test log file
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function InitializeTestLog()
		Dim strTestLogPath
		strTestLogPath = gobjReportSettings.ReportPath & "\HTML Results\" & gobjReportSettings.ReportName & ".html"
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.CreateTextFile(strTestLogPath, True)
		objMyFile.Close
		
		Set objMyFile = objFso.OpenTextFile(strTestLogPath, 8)	' 8 - Append Mode
		Dim strTestLogHeader
		strTestLogHeader =	"<!DOCTYPE html>" & vbcrlf &_
								"<html>" & vbcrlf &_
								vbTab & "<head>" & vbcrlf &_
								vbTab & vbTab & "<meta charset='UTF-8'>" & vbcrlf &_
								vbTab & vbTab & "<title>" &_
								gobjReportSettings.ProjectName &_
								" - " &	gobjReportSettings.ReportName &_
								" - Automation Execution Results" &_
								"</title>" & vbcrlf & vbcrlf &_
								GetThemeCss() &_
								GetJavascriptFunctions() &_
								vbTab & "</head>" & vbcrlf
		objMyFile.WriteLine(strTestLogHeader)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add test log heading
	'Input Parameters		: strHeading
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogHeading(strHeading)
		Dim strTestLogPath
		strTestLogPath = gobjReportSettings.ReportPath & "\HTML Results\" & gobjReportSettings.ReportName & ".html"
		
		If Not m_blnTestLogHeaderTableCreated Then
			CreateTestLogHeaderTable()
			m_blnTestLogHeaderTableCreated = True
		End If
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strTestLogPath, 8)
		
		Dim strTestLogHeading
		strTestLogHeading =	vbTab & vbTab & vbTab & vbTab & "<tr class='heading'>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & vbTab & "<th colspan='5' style='font-family:Copperplate Gothic Bold; font-size:1.4em;'>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & strHeading & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & vbTab & "</th>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf
		objMyFile.WriteLine(strTestLogHeading)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function CreateTestLogHeaderTable()
		Dim strTestLogPath
		strTestLogPath = gobjReportSettings.ReportPath & "\HTML Results\" & gobjReportSettings.ReportName & ".html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strTestLogPath, 8)
		
		Dim strTestLogHeaderTable
		strTestLogHeaderTable =	vbTab & "<body>" & vbcrlf &_
									vbTab & vbTab & "<table id='header'>" & vbcrlf &_
									vbTab & vbTab & vbTab & "<thead>" & vbcrlf
		objMyFile.WriteLine(strTestLogHeaderTable)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add test log subheading
	'Input Parameters		: strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogSubHeading(strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4)
		Dim strTestLogPath
		strTestLogPath = gobjReportSettings.ReportPath & "\HTML Results\" & gobjReportSettings.ReportName & ".html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strTestLogPath, 8)
		
		Dim strTestLogSubHeading
		strTestLogSubHeading =	vbTab & vbTab & vbTab & vbTab & "<tr class='subheading'>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "<th>&nbsp;" & Replace(strSubHeading1, " ", "&nbsp;") & "</th>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "<th>&nbsp;" & Replace(strSubHeading2, " ", "&nbsp;") & "</th>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "<th>&nbsp;" & Replace(strSubHeading3, " ", "&nbsp;") & "</th>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "<th>&nbsp;" & Replace(strSubHeading4, " ", "&nbsp;") & "</th>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf
		objMyFile.WriteLine(strTestLogSubHeading)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function CreateTestLogMainTable()
		Dim strTestLogPath
		strTestLogPath = gobjReportSettings.ReportPath & "\HTML Results\" & gobjReportSettings.ReportName & ".html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strTestLogPath, 8)
		
		Dim strTestLogMainTable
		strTestLogMainTable =	vbTab & vbTab & vbTab & "</thead>" & vbcrlf &_
									vbTab & vbTab & "</table>" & vbcrlf & vbcrlf &_
									_
									vbTab & vbTab & "<table id='main'>" & vbcrlf
		objMyFile.WriteLine(strTestLogMainTable)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add test log table headings
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogTableHeadings()
		Dim strTestLogPath
		strTestLogPath = gobjReportSettings.ReportPath & "\HTML Results\" & gobjReportSettings.ReportName & ".html"
		If Not m_blnTestLogMainTableCreated Then
			CreateTestLogMainTable()
			m_blnTestLogMainTableCreated = True
		End If
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strTestLogPath, 8)
		
		Dim strTestLogTableHeading
		strTestLogTableHeading =	vbTab & vbTab & vbTab & "<thead>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & "<tr class='heading'>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>Step No</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>Step Name</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>Description</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>Status</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>Step Time</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf &_
										vbTab & vbTab & vbTab & "</thead>" & vbcrlf & vbcrlf
		objMyFile.WriteLine(strTestLogTableHeading)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add test log section
	'Input Parameters		: strSection
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogSection(strSection)
		Dim strTestLogPath
		If Len(m_strCurrentSection) > 0 Then 'not empty
			strTestLogSection = vbTab & vbTab & vbTab & "</tbody>"
		End If
		
		Dim objRegExp
		Set objRegExp = New Regexp
		objRegExp.IgnoreCase = True
		objRegExp.Global = True
		objRegExp.Pattern = "[^a-zA-Z0-9]"
		m_strCurrentSection = objRegExp.Replace(strSection, "")
		
		strTestLogPath = gobjReportSettings.ReportPath & "\HTML Results\" &_
							gobjReportSettings.ReportName & ".html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strTestLogPath, 8)
		
		Dim strTestLogSection
		strTestLogSection =	strTestLogSection & vbTab & vbTab & vbTab & "<tbody>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & "<tr class='section'>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & vbTab & "<td colspan='5' onclick="" toggleMenu('" + m_strCurrentSection + "')"">+ " &_
								strSection + "</td>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & " </tr>" & vbcrlf &_
								vbTab & vbTab & vbTab & " </tbody>" & vbcrlf &_
								vbTab & vbTab & vbTab & "<tbody id='" & m_strCurrentSection & "' style='display:table-row-group'>" & vbcrlf
		objMyFile.WriteLine(strTestLogSection)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add test log sub-section
	'Input Parameters		: strSubSection
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogSubSection(strSubSection)
		Dim objRegExp
		Set objRegExp = New Regexp
		objRegExp.IgnoreCase = True
		objRegExp.Global = True
		objRegExp.Pattern = "[^a-zA-Z0-9]"
		m_strCurrentSubSection = objRegExp.Replace(strSubSection, "")
		m_intCurrentContentNumber = 1
		
		Dim strTestLogPath
		strTestLogPath = gobjReportSettings.ReportPath & "\HTML Results\" &_
							gobjReportSettings.ReportName & ".html"
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strTestLogPath, 8)
		
		Dim strTestLogSubSection
		strTestLogSubSection =	vbTab & vbTab & vbTab & vbTab & "<tr class='subheading subsection'>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "<td colspan='5' onclick="" toggleSubMenu('" + m_strCurrentSection + m_strCurrentSubSection + "')"">&nbsp;+ " &_
									strSubSection + "</td>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf
		objMyFile.WriteLine(strTestLogSubSection)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'#################################################################################################################
	
	'#################################################################################################################
	'Function Description	: Function to update test log
	'Input Parameters		: intStepNumber, strStepName, strStepDescription, strStepStatus, strScreenShotName
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'#################################################################################################################
	Public Function UpdateTestLog(intStepNumber, strStepName, strStepDescription, strStepStatus, strScreenShotName)
		Dim strTestLogPath
		strTestLogPath = gobjReportSettings.ReportPath & "\HTML Results\" &	gobjReportSettings.ReportName & ".html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strTestLogPath,8)
		
		Dim strTestStepRow
		strTestStepRow = vbTab & vbTab & vbTab & vbTab & "<tr class='content' id='" & m_strCurrentSection & m_strCurrentSubSection & m_intCurrentContentNumber & "'>" & vbcrlf &_
							vbTab & vbTab & vbTab & vbTab & vbTab & "<td>" & intStepNumber & "</td>" & vbcrlf &_
							vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='justified'>" + strStepName + "</td>" & vbcrlf
		
		m_intCurrentContentNumber = m_intCurrentContentNumber + 1
		
		Select Case strStepStatus
			Case "Fail"
				If(Environment.Value("TakeScreenshotFailedStep")) Then
				strTestStepRow = strTestStepRow &_
									GetTestStepWithScreenshot(strStepDescription, strStepStatus, strScreenShotName)
				Else
					strTestStepRow = strTestStepRow &_
										GetTestStepWithoutScreenshot(strStepDescription, strStepStatus)
				End If
			Case "Pass"
				If (Environment.Value("TakeScreenshotPassedStep")) Then
				strTestStepRow = strTestStepRow &_
									GetTestStepWithScreenshot(strStepDescription, strStepStatus, strScreenShotName)
				Else
				strTestStepRow = strTestStepRow &_
									GetTestStepWithoutScreenshot(strStepDescription, strStepStatus)
				End If
			Case "Screenshot"
				strTestStepRow = strTestStepRow &_
									GetTestStepWithScreenshot(strStepDescription, strStepStatus, strScreenShotName)
			Case else
				strTestStepRow = strTestStepRow &_
									GetTestStepWithoutScreenshot(strStepDescription, strStepStatus)	
		End Select
		
		strTestStepRow = strTestStepRow + vbTab & vbTab & vbTab & vbTab & vbTab & "<td>" &_
		"<small>" & Time() & "</small>" &_
		"</td>" & vbcrlf &_
		vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf
		objMyFile.WriteLine(strTestStepRow)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function GetTestStepWithScreenshot(strStepDescription, strStepStatus, strScreenShotName)
		If (gobjReportSettings.LinkScreenshotsToTestLog) Then
			GetTestStepWithScreenshot =_
					vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='justified'>" &_
						strStepDescription &_
					"</td>" & vbcrlf &_
					vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='" + LCase(strStepStatus) + "'>" &_
						"<a href='..\\Screenshots\\" + strScreenShotName + "'>" &_
							strStepStatus &_
						"</a>" &_
					"</td>" & vbcrlf
		Else
			GetTestStepWithScreenshot =_
					vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='justified'>" &_
						strStepDescription & " (Refer Screenshot @ " & strScreenShotName & ")" &_
					"</td>" & vbcrlf &_
					vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='" + LCase(strStepStatus) + "'>" &_
						strStepStatus &_
					"</td>" & vbcrlf
		End If
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function GetTestStepWithoutScreenshot(strStepDescription, strStepStatus)
		GetTestStepWithoutScreenshot =_
				vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='justified'>" &_
					strStepDescription &_
				"</td>" & vbcrlf &_
				vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='" + LCase(strStepStatus) + "'>" &_
					strStepStatus &_
				"</td>" & vbcrlf
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to create test log footer
	'Input Parameters		: strExecutionTime, intStepsPassed, intStepsFailed
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddTestLogFooter(strExecutionTime, intStepsPassed, intStepsFailed)
		Dim strTestLogPath
		strTestLogPath = gobjReportSettings.ReportPath & "\HTML Results\" & gobjReportSettings.ReportName & ".html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strTestLogPath, 8)
		
		Dim strTestLogFooter
		strTestLogFooter =	vbTab & vbTab & vbTab & "</tbody>" & vbcrlf &_
								vbTab & vbTab & "</table>" & vbcrlf & vbcrlf &_
								_
								vbTab & vbTab & "<table id='footer'>" & vbcrlf &_
								vbTab & vbTab & vbTab & "<colgroup>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & "<col style='width: 25%' />" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & "<col style='width: 25%' />" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & "<col style='width: 25%' />" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & "<col style='width: 25%' />" & vbcrlf &_
								vbTab & vbTab & vbTab & "</colgroup>" & vbcrlf & vbcrlf &_
								_
								vbTab & vbTab & vbTab & "<tfoot>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & "<tr class='heading'>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & vbTab & "<th colspan='4'>Execution Duration: " + strExecutionTime + "</th>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & "<tr class='subheading'>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='pass'>&nbsp;Steps passed</td>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='pass'>&nbsp;: " & intStepsPassed & "</td>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='fail'>&nbsp;Steps failed</td>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='fail'>&nbsp;: " & intStepsFailed & "</td>" & vbcrlf &_
								vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf &_
								vbTab & vbTab & vbTab & "</tfoot>" & vbcrlf &_
								vbTab & vbTab & "</table>" & vbcrlf &_
								vbTab & "</body>" & vbcrlf &_
								"</html>"
		
		objMyFile.WriteLine(strTestLogFooter)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	
	'###################################################################################################################
	'Function Description	: Function to initialize result summary
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function InitializeResultSummary()
		Dim strResultSummaryPath
		strResultSummaryPath = gobjReportSettings.ReportPath & "\HTML Results\Summary.html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.CreateTextFile(strResultSummaryPath, True)
		objMyFile.Close
		
		Set objMyFile = objFso.OpenTextFile(strResultSummaryPath, 8)
		Dim strResultSummaryHeader
		strResultSummaryHeader =	"<!DOCTYPE html>" & vbcrlf &_
										"<html>" & vbcrlf &_
										vbTab & "<head>" & vbcrlf &_
										vbTab & vbTab & "<meta charset='UTF-8'>" & vbcrlf &_
										vbTab & vbTab & "<title>" &_
										gobjReportSettings.ProjectName &_
										" - " &_
										"Automation Execution Result Summary" &_
										"</title>" & vbcrlf & vbcrlf &_
										GetThemeCss() &_
										GetJavascriptFunctions() &_
										vbTab & "</head>" & vbcrlf
		
		objMyFile.WriteLine(strResultSummaryHeader)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function CreateResultSummaryHeaderTable()
		Dim strResultSummaryPath
		strResultSummaryPath = gobjReportSettings.ReportPath & "\HTML Results\Summary.html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strResultSummaryPath, 8)
		
		Dim strResultSummaryHeaderTable
		strResultSummaryHeaderTable = vbTab & "<body> " & vbcrlf &_
										vbTab & vbTab & "<table id='header'>" & vbcrlf &_
										vbTab & vbTab & vbTab & "<thead>" & vbcrlf
		
		objMyFile.WriteLine(strResultSummaryHeaderTable)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add heading to the result summary
	'Input Parameters		: strHeading
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddResultSummaryHeading(strHeading)
		Dim strResultSummaryPath
		strResultSummaryPath = gobjReportSettings.ReportPath & "\HTML Results\Summary.html"
		
		If Not m_blnResultSummaryHeaderTableCreated Then
			CreateResultSummaryHeaderTable()
			m_blnResultSummaryHeaderTableCreated = True
		End If
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strResultSummaryPath, 8)
		
		Dim strResultSummaryHeading
		strResultSummaryHeading = vbTab & vbTab & vbTab & vbTab & "<tr class='heading'>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "<th colspan='4' style='font-family:Copperplate Gothic Bold; font-size:1.4em;'>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & vbTab & strHeading & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "</th>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf
		
		objMyFile.WriteLine(strResultSummaryHeading)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add sub-heading to the result summary
	'Input Parameters		: strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddResultSummarySubHeading(strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4)
		Dim strResultSummaryPath
		strResultSummaryPath = gobjReportSettings.ReportPath & "\HTML Results\Summary.html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strResultSummaryPath, 8)
		
		Dim strResultSummarySubHeading
		strResultSummarySubHeading = vbTab & vbTab & vbTab & vbTab & "<tr class='subheading'>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>&nbsp;" + Replace(strSubHeading1, " ", "&nbsp;") + "</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>&nbsp;" + Replace(strSubHeading2, " ", "&nbsp;") + "</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>&nbsp;" + Replace(strSubHeading3, " ", "&nbsp;") + "</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>&nbsp;" + Replace(strSubHeading4, " ", "&nbsp;") + "</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf
		
		objMyFile.WriteLine(strResultSummarySubHeading)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function CreateResultSummaryMainTable()
		Dim strResultSummaryPath
		strResultSummaryPath = gobjReportSettings.ReportPath & "\HTML Results\Summary.html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strResultSummaryPath, 8)
		
		Dim strResultSummaryMainTable
		strResultSummaryMainTable = vbTab & vbTab & vbTab & "</thead>" & vbcrlf &_
										vbTab & vbTab & "</table>" & vbcrlf & vbcrlf &_
										_
										vbTab & vbTab & "<table id='main'>" & vbcrlf
		
		objMyFile.WriteLine(strResultSummaryMainTable)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add table headings to the result summary
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddResultSummaryTableHeadings()
		Dim strResultSummaryPath
		strResultSummaryPath = gobjReportSettings.ReportPath & "\HTML Results\Summary.html"
		
		If Not m_blnResultSummaryMainTableCreated Then
			CreateResultSummaryMainTable()
			m_blnResultSummaryMainTableCreated = True
		End If
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strResultSummaryPath, 8)
		
		Dim strResultSummaryTableHeadings
		strResultSummaryTableHeadings = vbTab & vbTab & vbTab & " <thead>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & "<tr class='heading'>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>Test Scenario</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>Test Case</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>Test Description</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>Execution Time</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & vbTab & "<th>Test Status</th>" & vbcrlf &_
										vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf &_
										vbTab & vbTab & vbTab & "</thead>" & vbcrlf & vbcrlf &_
										vbTab & vbTab & vbTab & "<tbody>" & vbcrlf
										
		objMyFile.WriteLine(strResultSummaryTableHeadings)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to update result summary
	'Input Parameters		: strScenarioName, strTestcaseName, strTestcaseDescription, strExecutionTime, strTestStatus
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function UpdateResultSummary(strScenarioName, strTestcaseName, strTestcaseDescription, strExecutionTime, _
																										strTestStatus)
		Dim strResultSummaryPath
		strResultSummaryPath = gobjReportSettings.ReportPath & "\HTML Results\Summary.html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strResultSummaryPath, 8)
		
		Dim strTestCaseRow
		If (gobjReportSettings.LinkTestLogsToSummary) Then
			strTestCaseRow = vbTab & vbTab & vbTab & vbTab & "<tr class='content' >" & vbcrlf &_
							vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='justified'>" + strScenarioName + "</td>" & vbcrlf &_
							vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='justified'><a href='" + strScenarioName + "_" + strTestcaseName + ".html' " &_
							"target='about_blank'>" + strTestcaseName + "</a>" &_
							"</td>" & vbcrlf &_
							vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='justified'>" + strTestcaseDescription + "</td>" & vbcrlf &_
							vbTab & vbTab & vbTab & vbTab & vbTab & "<td>" + strExecutionTime + "</td>" & vbcrlf
		Else
			strTestCaseRow = vbTab & vbTab & vbTab & vbTab & "<tr class='content' >" & vbcrlf &_
							vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='justified'>" + strScenarioName + "</td>" & vbcrlf &_
							vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='justified'>" + strTestcaseName + "</td>" & vbcrlf &_
							vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='justified'>" + strTestcaseDescription + "</td>" & vbcrlf &_
							vbTab & vbTab & vbTab & vbTab & vbTab & "<td>" + strExecutionTime + "</td>" & vbcrlf
		End If
		If UCase(strTestStatus) = "PASSED" Then
			strTestCaseRow = strTestCaseRow + vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='pass'>" + strTestStatus + "</td>" & vbcrlf &_
			vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf
		Else
			strTestCaseRow = strTestCaseRow + vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='fail'>" + strTestStatus + "</td>" & vbcrlf &_
			vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf
		End If
		
		objMyFile.WriteLine(strTestCaseRow)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to update result summary
	'Input Parameters		: strTotalExecutionTime, intTestsPassed, intTestsFailed
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function AddResultSummaryFooter(strTotalExecutionTime, intTestsPassed, intTestsFailed)
		Dim strResultSummaryPath
		strResultSummaryPath = gobjReportSettings.ReportPath & "\HTML Results\Summary.html"
		
		Dim objFso, objMyFile
		Set objFso = CreateObject("Scripting.FileSystemObject")
		Set objMyFile = objFso.OpenTextFile(strResultSummaryPath, 8)
		
		Dim strResultSummaryFooter
		strResultSummaryFooter = vbTab & vbTab & vbTab & "</tbody>" & vbcrlf &_
									vbTab & vbTab & "</table>" & vbcrlf & vbcrlf &_
									_
									vbTab & vbTab & "<table id='footer'>" & vbcrlf &_
									vbTab & vbTab & vbTab & "<colgroup>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "<col style='width: 25%' />" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "<col style='width: 25%' />" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "<col style='width: 25%' />" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "<col style='width: 25%' />" & vbcrlf &_
									vbTab & vbTab & vbTab & "</colgroup>" & vbcrlf & vbcrlf &_
									_
									vbTab & vbTab & vbTab & "<tfoot>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "<tr class='heading'>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "<th colspan='4'>Total Duration: " + strTotalExecutionTime + "</th>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "<tr class='subheading'>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='pass'>&nbsp;Tests passed</td>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='pass'>&nbsp;: " & intTestsPassed & "</td>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='fail'>&nbsp;Tests failed</td>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & vbTab & "<td class='fail'>&nbsp;: " & intTestsFailed & "</td>" & vbcrlf &_
									vbTab & vbTab & vbTab & vbTab & "</tr>" & vbcrlf &_
									vbTab & vbTab & vbTab & "</tfoot>" & vbcrlf &_
									vbTab & vbTab & "</table>" & vbcrlf &_
									vbTab & "</body>" & vbcrlf &_
									"</html>"
		
		objMyFile.WriteLine(strResultSummaryFooter)
		
		objMyFile.Close
		Set objMyFile = Nothing
		Set objFso = Nothing
	End Function
	'###################################################################################################################
	
End Class
'#######################################################################################################################
'#######################################################################################################################
