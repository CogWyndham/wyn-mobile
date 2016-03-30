'#####################################################################################################################
'Script Description		: Initialization Script
'Test Tool/Version		: VAPI-XP
'Test Tool Settings		: N.A.
'Application Automated	: N.A.
'Author					: Cognizant
'Date Created			: 09/11/2012
'#####################################################################################################################
Option Explicit	'Forcing Variable declarations

'Associate required libraries
Dim gobjFso, gobjMyFile
Dim gstrRelativePath

Set gobjFso = CreateObject("Scripting.FileSystemObject")
gstrRelativePath = gobjFso.GetParentFolderName(WScript.ScriptFullName)

Set gobjMyFile = gobjFso.OpenTextFile(gstrRelativePath & "\allocator\Allocator_QC.vbs", 1) ' 1 - For Reading
Execute gobjMyFile.ReadAll()

Set gobjMyFile = Nothing
Set gobjFso = Nothing

'Setup the required inputs to the Allocator
gobjAllocator.TestSetPath = "Root\mCRAFT"
gobjAllocator.TestSetName = "Regression"

'Execute the test batch
On Error Resume Next
ExecuteTestBatch()
If Err.Number <> 0 Then
	WScript.Echo Err.Description
	WScript.Quit Err.Number
End If
'#######################################################################################################################


'#######################################################################################################################
'Function Description	: Function to execute the test batch
'Input Parameters		: None
'Return Value			: None
'Author					: Cognizant
'Date Created			: 09/11/2012
'#######################################################################################################################
Sub ExecuteTestBatch()

	gobjAllocator.ConnectToQc "<qcurl>", "<username>", "<password>", "<domain>", "<project>"
	gobjAllocator.SetRelativePath()
	gobjAllocator.InitializeTestBatch()
	gobjAllocator.InitializeSummaryReport()
	gobjAllocator.DriveBatchExecution()
	gobjAllocator.WrapUp()

End Sub
'#######################################################################################################################
