'#######################################################################################################################
'Script Description     : Script to invoke the corresponding test script in the local machine
'Test Tool/Version      : VAPI-XP
'Test Tool Settings     : N.A.
'Application Automated  : Mercury Tours
'Author                 : Cognizant
'Date Created           : 26/12/2012
'#######################################################################################################################
Option Explicit	'Forcing Variable declarations

'#######################################################################################################################
'Function Description   : Main function which is the entry point of execution
'Input Parameters       : Debug, CurrentTestSet, CurrentTSTest, CurrentRun
'Return Value           : None
'Author                 : Cognizant
'Date Created           : 26/12/2012
'#######################################################################################################################
Sub Test_Main(Debug, CurrentTestSet, CurrentTSTest, CurrentRun)
	TDOutput.Clear
	
	If Debug Then
		MsgBox "This test cannot be run in debug mode! Please choose the test mode...", , "Error"
		Exit Sub
	End If
	
	Dim strRelativePath
	Dim objCurrentTestSet: Set objCurrentTestSet = TDConnection.TestSetFactory.Item(CurrentTestSet.ID)
	strRelativePath = objCurrentTestSet.Field("CY_USER_01")	'Customize this line as required
	
	Dim objFso
	Set objFso = CreateObject("Scripting.FileSystemObject")
	
	If Not objFso.FolderExists(strRelativePath) Then
		TDOutput.Print "The specified framework path ' " & strRelativePath & "' does not exist!"
		CurrentRun.Status = "Failed"
		CurrentTSTest.Status = "Failed"
		PublishCommandLineOutput CurrentRun
		Exit Sub
	End If
    
	'Associate required libraries
	Dim objMyFile: Set objMyFile =_
					objFso.OpenTextFile(strRelativePath & "\supportlibraries\DriverScript_QC.vbs", 1) ' 1 - For Reading
	Execute objMyFile.ReadAll()
	Set objMyFile = Nothing
	Set objFso = Nothing
	
	Dim objDriverScript: Set objDriverScript = New DriverScript
	objDriverScript.RelativePath = strRelativePath
	objDriverScript.Debug = Debug
	Set objDriverScript.CurrentTestSet = CurrentTestSet
	Set objDriverScript.CurrentTSTest = CurrentTSTest
	Set objDriverScript.CurrentRun = CurrentRun
	
	On Error Resume Next
	Dim strTestStatus
	strTestStatus = objDriverScript.DriveTestExecution()
	CurrentRun.Status = strTestStatus
	CurrentTSTest.Status = strTestStatus
	
	'Handle run-time errors
	If Err.Number <> 0 Then
		TDOutput.Print "Run-time error [" & Err.Number & "] : " & Err.Description
		CurrentRun.Status = "Failed"
		CurrentTSTest.Status = "Failed"
	End If
	
	PublishCommandLineOutput CurrentRun
End Sub
'#######################################################################################################################

'#######################################################################################################################
'Function Description   : Function to publish the command line output into the test run results 
'Input Parameters       : CurrentRun
'Return Value           : None
'Author                 : Cognizant
'Date Created           : 26/12/2012
'#######################################################################################################################
Sub PublishCommandLineOutput(CurrentRun)
	Dim objStep: Set objStep =  CurrentRun.StepFactory.AddItem(Null)
	objStep.Name = "Execution Log"
	objStep.Status = "N/A"
	objStep.Field("ST_DESCRIPTION") = TDOutput.Text
	objStep.Post
	Set objStep = Nothing
End Sub
'#######################################################################################################################
