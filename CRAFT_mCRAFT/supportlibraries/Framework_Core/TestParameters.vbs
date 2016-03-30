'#####################################################################################################################
'Script Description		: Test Parameters Library
'Test Tool/Version		: HP Quick Test Professional 11+
'Test Tool Settings		: N.A.
'Application Automated	: N.A.
'Author					: Cognizant
'Date Created			: 04/07/2011
'#######################################################################################################################
Option Explicit	'Forcing Variable declarations

Dim gobjTestParameters: Set gobjTestParameters = New TestParameters

'#######################################################################################################################
'Class Description		: Class to get/set TestParameters
'Author					: Cognizant
'Date Created			: 23/07/2012
'#######################################################################################################################
Class TestParameters
	Private m_strCurrentScenario
	Private m_strCurrentTestcase
	Private m_strCurrentTestInstance
	Private m_strCurrentTestDescription
	Private m_strAdditionalDetails
	Private m_strIterationMode
	Private m_intStartIteration
	Private m_intEndIteration
	
	'###################################################################################################################
	Public Property Get CurrentScenario
		CurrentScenario = m_strCurrentScenario
	End Property
	
	Public Property Let CurrentScenario(strCurrentScenario)
		m_strCurrentScenario = strCurrentScenario
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get CurrentTestcase
		CurrentTestcase = m_strCurrentTestcase
	End Property
	
	Public Property Let CurrentTestcase(strCurrentTestcase)
		m_strCurrentTestcase = strCurrentTestcase
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get CurrentTestInstance
		CurrentTestInstance = m_strCurrentTestInstance
	End Property
	
	Public Property Let CurrentTestInstance(strCurrentTestInstance)
		m_strCurrentTestInstance = strCurrentTestInstance
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get CurrentTestDescription
		CurrentTestDescription = m_strCurrentTestDescription
	End Property
	
	Public Property Let CurrentTestDescription(strCurrentTestDescription)
		m_strCurrentTestDescription = strCurrentTestDescription
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get AdditionalDetails
		AdditionalDetails = m_strAdditionalDetails
	End Property
	
	Public Property Let AdditionalDetails(strAdditionalDetails)
		m_strAdditionalDetails = strAdditionalDetails
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get IterationMode
		IterationMode = m_strIterationMode
	End Property
	
	Public Property Let IterationMode(strIterationMode)
		m_strIterationMode = strIterationMode
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get StartIteration
		StartIteration = m_intStartIteration
	End Property
	
	Public Property Let StartIteration(intStartIteration)
		If intStartIteration > 0 Then	'Defaults to 1 if the input is less than or equal to 0
			m_intStartIteration = intStartIteration
		End If
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get EndIteration
		EndIteration = m_intEndIteration
	End Property
	
	Public Property Let EndIteration(intEndIteration)
		If intEndIteration > 0 Then	'Defaults to 1 if the input is less than or equal to 0
			m_intEndIteration = intEndIteration
		End If
	End Property
	'###################################################################################################################
	
	
	'*********************THIS SECTION IS FOR USE WITH AUTOMATION OF WEB BASED APPLICATIONS ONLY***********************'
	Private m_strBrowser
	Private m_strBrowserVersion
	Private m_strPlatform
	
	'###################################################################################################################
	Public Property Get Browser
		Browser = m_strBrowser
	End Property
	
	Public Property Let Browser(strBrowser)
		m_strBrowser = strBrowser
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get BrowserVersion
		BrowserVersion = m_strBrowserVersion
	End Property
	
	Public Property Let BrowserVersion(strBrowserVersion)
		m_strBrowserVersion = strBrowserVersion
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get Platform
		Platform = m_strPlatform
	End Property
	
	Public Property Let Platform(strPlatform)
		m_strPlatform = strPlatform
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to get the browser and platform on which the test is to be executed
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 07/11/2012
	'###################################################################################################################
	Public Function GetBrowserAndPlatform()
		If(m_strBrowser = "") Then
			Err.Raise 7001, "TestParameters Library", "The browser has not been initialized!"
		End If
		
		Dim strBrowserAndPlatform
		strBrowserAndPlatform = m_strBrowser
		If(m_strBrowserVersion <> "") Then
			strBrowserAndPlatform = strBrowserAndPlatform & " " & m_strBrowserVersion
		End If
		
		If(m_strPlatform <> "") Then
			strBrowserAndPlatform = strBrowserAndPlatform & " on " & m_strPlatform
		End If
		
		GetBrowserAndPlatform = strBrowserAndPlatform
	End Function
	'###################################################################################################################
	'***************************************************END SECTION****************************************************'
	
	
	'###################################################################################################################
	Private Sub Class_Initialize()
		'Set default values for all test parameters
		m_strCurrentTestInstance = "Instance1"
		m_strCurrentTestDescription = ""
		m_strAdditionalDetails = ""
		m_strIterationMode = "RunAllIterations"
		m_intStartIteration = 1
		m_intEndIteration = 1
	End Sub
	'###################################################################################################################
	
End Class
'#######################################################################################################################