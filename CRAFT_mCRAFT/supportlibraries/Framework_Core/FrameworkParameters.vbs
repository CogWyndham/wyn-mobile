'#######################################################################################################################
'Script Description		: Framework Parameters library
'Test Tool/Version		: HP Quick Test Professional 11+
'Test Tool Settings		: N.A.
'Application Automated	: N.A.
'Author					: Cognizant
'Date Created			: 30/07/2008
'#######################################################################################################################
Option Explicit	'Forcing Variable declarations

Dim gobjFrameworkParameters: Set gobjFrameworkParameters = New FrameworkParameters

'#######################################################################################################################
'Class Description		: Class to encapsulate parameters which are global to the entire framework
'Author					: Cognizant
'Date Created			: 23/07/2012
'#######################################################################################################################
Class FrameworkParameters
	
	Private m_strRelativePath
	Private m_strRunConfiguration
	
	'###################################################################################################################
	Public Property Get RelativePath
		Dim objFso : Set objFso = CreateObject("Scripting.FileSystemObject")
		If Not objFso.FolderExists(m_strRelativePath) Then
			Err.Raise 2001, "FrameworkParameters Library", "FrameworkParameters class: Invalid relative path!"
		End If
		
		RelativePath = m_strRelativePath
	End Property
	
	Public Property Let RelativePath(strRelativePath)
		m_strRelativePath = strRelativePath
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Get RunConfiguration
		If (Trim(m_strRunConfiguration) = "") Then
			Err.Raise 4003, "FrameworkParameters Library", "FrameworkParameters Class: Run configuration cannot be blank!"
		End If
		
		RunConfiguration = m_strRunConfiguration
	End Property
	
	Public Property Let RunConfiguration(strRunConfiguration)
		m_strRunConfiguration = strRunConfiguration
	End Property
	'###################################################################################################################
	
End Class
'#######################################################################################################################