'#######################################################################################################################
'Script Description		: Framework Settings library for Java
'Test Tool/Version		: VAPI-XP
'Test Tool Settings		: N.A.
'Application Automated	: N.A.
'Author					: Cognizant
'Date Created			: 30/07/2008
'#######################################################################################################################
Option Explicit	'Forcing Variable declarations

Dim gobjSettings: Set gobjSettings = New Settings

'#######################################################################################################################
'Class Description		: Class to encapsulate utility functions of the framework
'Author					: Cognizant
'Date Created			: 23/07/2012
'#######################################################################################################################
Class Settings
	
	'###################################################################################################################
	'Function Description	: Function to get configuration data from the Global Settings.properties file
	'Input Parameters		: strKey
	'Return Value			: strValue
	'Author					: Cognizant
	'Date Created			: 02/04/2012
	'###################################################################################################################
	Public Function GetValue(strKey)
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		Dim objSettingsFile: Set objSettingsFile =_
			objFso.OpenTextFile(gobjFrameworkParameters.RelativePath & "\Global Settings.properties", 1)	' 1 - For Reading
		
		Dim strCurrentLine, arrKeyValuePair, strValue
		While Not objSettingsFile.AtEndOfStream
			strCurrentLine = Trim(objSettingsFile.ReadLine)
			If Len(strCurrentLine) <> 0 AND Instr(strCurrentLine, "#") <> 1 Then	'Ignore blank lines and comments
				arrKeyValuePair = Split(strCurrentLine, "=")
				If arrKeyValuePair(0) = strKey Then
					strValue = arrKeyValuePair(1)
				End If
			End If
		Wend
		
		If strValue = "" Then
			Err.Raise 7001, "Settings class", "The specified key was not found in the settings file!"
		End If
		
		Set objSettingsFile = Nothing
		Set objFso = Nothing
		
		GetValue = CStr(strValue)
	End Function
	'###################################################################################################################
	
End Class
'#######################################################################################################################