'#######################################################################################################################
'Script Description		: Library to generate different types of reports
'Test Tool/Version		: HP Quick Test Professional 11+
'Test Tool Settings		: N.A.
'Application Automated	: N.A.
'Author					: Cognizant
'Date Created			: 04/07/2011
'#######################################################################################################################
Option Explicit	'Forcing Variable declarations

Dim gobjReport: Set gobjReport = New Report
Dim gobjReportSettings : Set gobjReportSettings = New ReportSettings
Dim gobjReportTheme: Set gobjReportTheme = New ReportTheme

'#######################################################################################################################
'Class Description		: Class to handle Reporting
'Author					: Cognizant
'Date Created			: 23/07/2012
'#######################################################################################################################
Class Report
	Private m_intStepNumber
	Private m_intStepsPassed
	Private m_intStepsFailed
	Private m_intTestsPassed
	Private m_intTestsFailed
	Private m_objReportTypes
	
	'###################################################################################################################
	Private Sub Class_Initialize()
		m_intStepNumber = 1
		m_intStepsPassed = 0
		m_intStepsFailed = 0
		m_intTestsPassed = 0
		m_intTestsFailed = 0
	End Sub
	'###################################################################################################################
	
	
	'###################################################################################################################
	'Function Description	: Function to initialize report
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant	
	'Date Created			: 11/10/2012
	'###################################################################################################################
	Public Function InitializeReport()
		ValidateReportPath()
		InitializeReportTypes()
		InitializeReportTheme()
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub ValidateReportPath()
		Dim objFso
		Set objFso = CreateObject("Scripting.FileSystemObject")
		
		If Not objFso.FolderExists(gobjReportSettings.ReportPath) Then
			Err.Raise 4001, "Reporting Library", "Report Class: The given report path " &_
																	gobjReportSettings.ReportPath & " does not exist!"
		End If
		
		Set objFso = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub InitializeReportTypes()
		Dim objFso
		Set objFso = CreateObject("Scripting.FileSystemObject")
		
		Set m_objReportTypes = CreateObject("Scripting.Dictionary")
		If (gobjReportSettings.HtmlReport) Then
			If Not objFso.FolderExists(gobjReportSettings.ReportPath + "\HTML Results") Then
				objFso.CreateFolder(gobjReportSettings.ReportPath + "\HTML Results")
			End If
			m_objReportTypes.Add m_objReportTypes.Count, gobjHtmlReport
		End If
		
		If (gobjReportSettings.ExcelReport) Then
			If Not objFso.FolderExists(gobjReportSettings.ReportPath + "\Excel Results") Then
				objFso.CreateFolder(gobjReportSettings.ReportPath + "\Excel Results")
			End If
			m_objReportTypes.Add m_objReportTypes.Count, gobjExcelReport
		End If
		
		If Not objFso.FolderExists(gobjReportSettings.ReportPath + "\QTP Results") Then
			objFso.CreateFolder(gobjReportSettings.ReportPath + "\QTP Results")
		End If
		
		If Not objFso.FolderExists(gobjReportSettings.ReportPath + "\Screenshots") Then
			objFso.CreateFolder(gobjReportSettings.ReportPath + "\Screenshots")
		End If
		
		Set objFso = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub InitializeReportTheme()
		Select Case UCase(gobjReportSettings.ReportTheme)
			Case "AUTUMN"
				gobjReportTheme.HeadingBackColor="#62536D"
				gobjReportTheme.HeadingForeColor="#DAD0E1"
				gobjReportTheme.SectionBackColor="#AD96BD"
				gobjReportTheme.SectionForeColor="#413748"
				gobjReportTheme.ContentBackColor="#F3F0F6"
				gobjReportTheme.ContentForeColor="#000000"
			Case "OLIVE"
				gobjReportTheme.HeadingBackColor="#333300"
				gobjReportTheme.HeadingForeColor="#DED05E"
				gobjReportTheme.SectionBackColor="#70704C"
				gobjReportTheme.SectionForeColor="#001F00"
				gobjReportTheme.ContentBackColor="#E8DEBA"
				gobjReportTheme.ContentForeColor="#003326"
			Case "CLASSIC"
				gobjReportTheme.HeadingBackColor="#334C00"
				gobjReportTheme.HeadingForeColor="#FFD98C"
				gobjReportTheme.SectionBackColor="#849366"
				gobjReportTheme.SectionForeColor="#1E2D00"
				gobjReportTheme.ContentBackColor="#FFE7B7"
				gobjReportTheme.ContentForeColor="#000000"
			Case "RETRO"
				gobjReportTheme.HeadingBackColor="#5E5661"
				gobjReportTheme.HeadingForeColor="#FFE4B5"
				gobjReportTheme.SectionBackColor="#9E99A0"
				gobjReportTheme.SectionForeColor="#252226"
				gobjReportTheme.ContentBackColor="#FFF5EE"
				gobjReportTheme.ContentForeColor="#413f49"
			Case "MYSTIC"
				gobjReportTheme.HeadingBackColor="#4D7C7B"
				gobjReportTheme.HeadingForeColor="#FFFF95"
				gobjReportTheme.SectionBackColor="#89B6B5"
				gobjReportTheme.SectionForeColor="#333300"
				gobjReportTheme.ContentBackColor="#FAFAC5"
				gobjReportTheme.ContentForeColor="#000000"
			Case "SERENE"
				gobjReportTheme.HeadingBackColor="#005B96"
				gobjReportTheme.HeadingForeColor="#B1DAFB"
				gobjReportTheme.SectionBackColor="#669CC0"
				gobjReportTheme.SectionForeColor="#1E2D35"
				gobjReportTheme.ContentBackColor="#D8ECFD"
				gobjReportTheme.ContentForeColor="#000000"
			Case "REBEL"
				gobjReportTheme.HeadingBackColor="#80423A"
				gobjReportTheme.HeadingForeColor="#B2B2B2"
				gobjReportTheme.SectionBackColor="#B28D88"
				gobjReportTheme.SectionForeColor="#1E1E1E"
				gobjReportTheme.ContentBackColor="#D1D1D1"
				gobjReportTheme.ContentForeColor="#3D1F00"
			Case Else	'Defaults to the MYSTIC theme
				gobjReportTheme.HeadingBackColor="#4D7C7B"
				gobjReportTheme.HeadingForeColor="#FFFF95"
				gobjReportTheme.SectionBackColor="#89B6B5"
				gobjReportTheme.SectionForeColor="#333300"
				gobjReportTheme.ContentBackColor="#FAFAC5"
				gobjReportTheme.ContentForeColor="#000000"
		End Select
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to initialize test log
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub InitializeTestLog()
		If gobjReportSettings.ReportName = "" Then
			Err.Raise 4002, "Reporting Library", "Report Class: The report name cannot be empty!"
		End If
		
		Dim i
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).InitializeTestLog()
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add a heading to the test log
	'Input Parameters		: strHeading
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub AddTestLogHeading(strHeading)
		Dim i
		
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).AddTestLogHeading(strHeading)
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add sub-headings to the test log
	'Input Parameters		: strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub AddTestLogSubHeading(strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4)
		Dim i
		
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).AddTestLogSubHeading strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add the overall table headings to the test log
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub AddTestLogTableHeadings()
		Dim i
		
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).AddTestLogTableHeadings()
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add a section to the test log
	'Input Parameters		: strSection
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub AddTestLogSection(strSection)
		Reporter.ReportEvent micDone, strSection, strSection
		
		Dim i
		
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).AddTestLogSection(strSection)
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add a sub-section to the test log	
	'Input Parameters		: strSubSection
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub AddTestLogSubSection(strSubSection)
		Reporter.ReportEvent micDone, strSubSection, strSubSection
		Dim i
		
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).AddTestLogSubSection(strSubSection)
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to report any event related to the current test case
	'Input Parameters		: strStepName, strStepDescription, strStepStatus
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 21/07/2008
	'###################################################################################################################
	Public Sub UpdateTestLog(strStepName, strStepDescription, strStepStatus)
		If (UCase(strStepStatus) = "PASS") Then
			m_intStepsPassed = m_intStepsPassed + 1
		End If
		If (UCase(strStepStatus) = "FAIL") Then
			m_intStepsFailed = m_intStepsFailed + 1
		End If
		
		Dim intStatus
		intStatus = GetLogLevel(strStepStatus)
		If (intStatus < gobjReportSettings.LogLevel) Then
			'Update the QTP results
			Reporter.ReportEvent GetQtpStatus(strStepStatus), strStepName, strStepDescription
			
			Dim objFso, strScreenshotName, strScreenshotPath, strCurrentTime
			strCurrentTime = Now()
			Set objFso = CreateObject("Scripting.FileSystemObject")
			strScreenshotName = gobjReportSettings.ReportName & "_" &_
									Replace(Replace(Replace(strCurrentTime, " ", "_"), ":", "-"), "/","-") &".png"
			strScreenshotPath = gobjReportSettings.ReportPath & "\Screenshots\" & strScreenshotName
			
			If((strStepStatus = "Fail" Or strStepStatus = "Warning") _
			And gobjReportSettings.TakeScreenshotFailedStep) _
			And objFso.FileExists(strScreenshotPath) = False Then	'check if another screenshot was taken already in the very same second
				Desktop.CaptureBitmap(strScreenshotPath)
			End If
			
			If((strStepStatus = "Pass") _
			And gobjReportSettings.TakeScreenshotPassedStep) _
			And objFso.FileExists(strScreenshotPath) = False Then	'check if another screenshot was taken already in the very same second
				Desktop.CaptureBitmap(strScreenshotPath)
			End If
			
			If(strStepStatus = "Screenshot") _
			And objFso.FileExists(strScreenshotPath) = False Then	'check if another screenshot was taken already in the very same second
				Desktop.CaptureBitmap(strScreenshotPath)
			End If
			
			Set objFso = Nothing
			
			'Update the CRAFT custom results
			Dim i
			For i = 0 To (m_objReportTypes.Count - 1)
				m_objReportTypes.Item(i).UpdateTestLog m_intStepNumber, strStepName, strStepDescription, _
																						strStepStatus, strScreenShotName
			Next
			
			m_intStepNumber = m_intStepNumber + 1
		End If
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function GetLogLevel(strStepStatus)
		Dim intStatus
		Select Case strStepStatus
			Case "Fail"
				intStatus = 0
			Case "Warning"
				intStatus = 1
			Case "Pass"
				intStatus = 2
			Case "Screenshot"
				intStatus = 3
			Case "Done"
				intStatus = 4
			Case "Debug"
				intStatus = 5
		End Select
		
		GetLogLevel = intStatus	
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function GetQtpStatus(strStepStatus)
		Dim intQtpStatus
		Select Case strStepStatus
			Case "Pass"
				intQtpStatus = micPass
			Case "Fail"
				intQtpStatus = micFail
			Case "Done"
				intQtpStatus = micDone
			Case "Warning"
				intQtpStatus = micWarning
			Case "Screenshot"
				intQtpStatus = micDone
		End Select
		
		GetQtpStatus = intQtpStatus
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add footer to the test log
	'Input Parameters		: strExecutionTime
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub AddTestLogFooter(strExecutionTime)
		Dim i
		
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).AddTestLogFooter strExecutionTime, m_intStepsPassed, m_intStepsFailed
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to consolidate all the screenshots into a Word document
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 03/12/2013
	'###################################################################################################################
	Public Sub ConsolidateScreenshotsInWordDoc()
		Dim objWord: Set objWord = CreateObject("Word.Application")
		Dim objDocument: Set objDocument = objWord.Documents.Add()
		Dim objSelection: Set objSelection = objWord.Selection
		
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		Dim objConsolidatedScreenshotsFolder
		If Not objFso.FolderExists(gobjReportSettings.ReportPath + "\Screenshots (Consolidated)") Then
			Set objConsolidatedScreenshotsFolder =_
							objFso.CreateFolder(gobjReportSettings.ReportPath + "\Screenshots (Consolidated)")
		Else
			Set objConsolidatedScreenshotsFolder =_
							objFso.GetFolder(gobjReportSettings.ReportPath + "\Screenshots (Consolidated)")
		End If
		
		Dim objScreenshotsFolder: Set objScreenshotsFolder =_
							objFso.GetFolder(gobjReportSettings.ReportPath + "\Screenshots")
		
		Dim objScreenshot
		For Each objScreenshot In objScreenshotsFolder.Files
			If InStr(objScreenshot.Name, gobjReportSettings.ReportName) > 0 Then
				'objSelection.EndKey 6, 0
				objSelection.TypeText objScreenshot.Name
				objSelection.InlineShapes.AddPicture(objScreenshot.Path)
				objSelection.InsertBreak()
			End If
		Next
		
		objSelection.TypeBackspace
		objSelection.TypeBackspace
		objDocument.SaveAs(objConsolidatedScreenshotsFolder.Path & "\" & gobjReportSettings.ReportName & ".doc")
		objWord.Quit
		
		'objFso.DeleteFolder(objScreenshotsFolder.Path)
		
		Set objScreenshot = Nothing
		Set objScreenshotsFolder = Nothing
		Set objConsolidatedScreenshotsFolder = Nothing
		Set objFso = Nothing
		Set objSelection = Nothing
		Set objDocument = Nothing
		Set objWord = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to initialize result summary
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub InitializeResultSummary()
		Dim i
		
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).InitializeResultSummary()
		Next
		End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add a heading to the result summary
	'Input Parameters		: strHeading
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub AddResultSummaryHeading(strHeading)
		Dim i
		
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).AddResultSummaryHeading(strHeading)
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add sub-headings to the result summary
	'Input Parameters		: strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub AddResultSummarySubHeading(strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4)
		Dim i
		
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).AddResultSummarySubHeading strSubHeading1, strSubHeading2, strSubHeading3, _
																										strSubHeading4
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add the overall table headings to the result summary
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub AddResultSummaryTableHeadings()
		Dim i
		
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).AddResultSummaryTableHeadings()
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to update the results summary with the status of the executed test case
	'Input Parameters		: strScenarioName, strTestcaseName, strTestcaseDescription, strExecutionTime, strTestStatus
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub UpdateResultSummary(strScenarioName, strTestcaseName, strTestcaseDescription, strExecutionTime, _
																										strTestStatus)
		If (strTestStatus = "Passed") Then
			m_intTestsPassed = m_intTestsPassed + 1
		ElseIf (strTestStatus = "Failed") Then
			m_intTestsFailed = m_intTestsFailed + 1
		End If
		
		Dim i
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).UpdateResultSummary strScenarioName, strTestcaseName, strTestcaseDescription, _
																						strExecutionTime, strTestStatus
		Next
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to add footer to the results summary
	'Input Parameters		: strTotalExecutionTime
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 04/09/2012
	'###################################################################################################################
	Public Sub AddResultSummaryFooter(strTotalExecutionTime)
		Dim i
		
		For i = 0 To (m_objReportTypes.Count - 1)
			m_objReportTypes.Item(i).AddResultSummaryFooter strTotalExecutionTime, m_intTestsPassed, m_intTestsFailed
		Next
	End Sub
	'###################################################################################################################
	
End Class
'#######################################################################################################################
'#######################################################################################################################


'#######################################################################################################################
'Class Description		: Class to get/set Report settings
'Author					: Cognizant
'Date Created			: 23/07/2012
'#######################################################################################################################
Class ReportSettings
	Private m_strReportPath, m_strReportName
	Private m_strReportTheme
	Private m_strProjectName
	Private m_intLogLevel
	Private m_blnExcelReport, m_blnHtmlReport
	Private m_blnTakeScreenshotPassedStep, m_blnTakeScreenshotFailedStep
	Private m_blnConsolidateScreenshotsInWordDoc
	Private m_blnLinkScreenshotsToTestLog
	Private m_blnLinkTestLogsToSummary
	
	'###################################################################################################################
	Public Property Get ReportPath
		ReportPath = m_strReportPath
	End Property
	
	Public Property Let ReportPath(strReportPath)
		m_strReportPath = strReportPath
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get ReportName
		ReportName = m_strReportName
	End Property
	
	Public Property Let ReportName(strReportName)
		m_strReportName = strReportName
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get ReportTheme
		ReportTheme = m_strReportTheme
	End Property
	
	Public Property Let ReportTheme(strReportTheme)
		m_strReportTheme = strReportTheme
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get ProjectName
		ProjectName = m_strProjectName
	End Property
	
	Public Property Let ProjectName(strProjectName)
		m_strProjectName = strProjectName
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get LogLevel
		LogLevel = m_intLogLevel
	End Property
	
	Public Property Let LogLevel(intLogLevel)
		If intLogLevel < 0 Then
			intLogLevel = 0
		ElseIF intLogLevel > 5 Then
			intLogLevel = 5
		End If
		m_intLogLevel = intLogLevel
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get ExcelReport
		ExcelReport = m_blnExcelReport
	End Property
	
	Public Property Let ExcelReport(blnExcelReport)
		m_blnExcelReport = blnExcelReport
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get HtmlReport
		HtmlReport = m_blnHtmlReport
	End Property
	
	Public Property Let HtmlReport(blnHtmlReport)
		m_blnHtmlReport = blnHtmlReport
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get TakeScreenshotPassedStep
		TakeScreenshotPassedStep = m_blnTakeScreenshotPassedStep
	End Property
	
	Public Property Let TakeScreenshotPassedStep(blnTakeScreenshotPassedStep)
		m_blnTakeScreenshotPassedStep = blnTakeScreenshotPassedStep
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get TakeScreenshotFailedStep
		TakeScreenshotFailedStep = m_blnTakeScreenshotFailedStep
	End Property
	
	Public Property Let TakeScreenshotFailedStep(blnTakeScreenshotFailedStep)
		m_blnTakeScreenshotFailedStep = blnTakeScreenshotFailedStep
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get ConsolidateScreenshotsInWordDoc
		ConsolidateScreenshotsInWordDoc = m_blnConsolidateScreenshotsInWordDoc
	End Property
	
	Public Property Let ConsolidateScreenshotsInWordDoc(blnConsolidateScreenshotsInWordDoc)
		m_blnConsolidateScreenshotsInWordDoc = blnConsolidateScreenshotsInWordDoc
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get LinkScreenshotsToTestLog
		LinkScreenshotsToTestLog = m_blnLinkScreenshotsToTestLog
	End Property
	
	Public Property Let LinkScreenshotsToTestLog(blnLinkScreenshotsToTestLog)
		m_blnLinkScreenshotsToTestLog = blnLinkScreenshotsToTestLog
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get LinkTestLogsToSummary
		LinkTestLogsToSummary = m_blnLinkTestLogsToSummary
	End Property
	
	Public Property Let LinkTestLogsToSummary(blnLinkTestLogsToSummary)
		m_blnLinkTestLogsToSummary = blnLinkTestLogsToSummary
	End Property
	'###################################################################################################################
	
	
	'###################################################################################################################
	Private Sub Class_Initialize()
		m_strProjectName = ""
		m_intLogLevel = 4
		m_blnExcelReport = False
		m_blnHtmlReport = True
		m_blnTakeScreenshotFailedStep = True
		m_blnTakeScreenshotPassedStep = False
		m_blnLinkScreenshotsToTestLog = True
		m_blnLinkTestLogsToSummary = True
	End Sub
	'###################################################################################################################
	
End Class
'#######################################################################################################################
'#######################################################################################################################


'#######################################################################################################################
'Class Description		: Class to get/set Report theme
'Author					: Cognizant
'Date Created			: 23/07/2012
'#######################################################################################################################
Class ReportTheme
	Private m_strHeadingBackColor
	Private m_strHeadingForeColor
	Private m_strSectionBackColor
	Private m_strSectionForeColor
	Private m_strContentBackColor
	Private m_strContentForeColor
	
	'###################################################################################################################
	Public Property Get HeadingBackColor
		HeadingBackColor = m_strHeadingBackColor
	End Property
	
	Public Property Let HeadingBackColor(strHeadingBackColor)
		m_strHeadingBackColor = strHeadingBackColor
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get HeadingForeColor
		HeadingForeColor = m_strHeadingForeColor
	End Property
	
	Public Property Let HeadingForeColor(strHeadingForeColor)
		m_strHeadingForeColor = strHeadingForeColor
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get SectionBackColor
		SectionBackColor = m_strSectionBackColor
	End Property
	
	Public Property Let SectionBackColor(strSectionBackColor)
		m_strSectionBackColor = strSectionBackColor
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get SectionForeColor
		SectionForeColor = m_strSectionForeColor
	End Property
	
	Public Property Let SectionForeColor(strSectionForeColor)
		m_strSectionForeColor = strSectionForeColor
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get ContentBackColor
		ContentBackColor = m_strContentBackColor
	End Property
	
	Public Property Let ContentBackColor(strContentBackColor)
		m_strContentBackColor = strContentBackColor
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get ContentForeColor
		ContentForeColor = m_strContentForeColor
	End Property
	
	Public Property Let ContentForeColor(strContentForeColor)
		m_strContentForeColor = strContentForeColor
	End Property
	'###################################################################################################################
	
End Class
'#######################################################################################################################
'#######################################################################################################################