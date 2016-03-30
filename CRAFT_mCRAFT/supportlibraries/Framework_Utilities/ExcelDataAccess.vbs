'#######################################################################################################################
'Script Description		: Excel Data Access library
'Test Tool/Version		: HP Quick Test Professional 11+
'Test Tool Settings		: N.A.
'Application Automated	: N.A.
'Author					: Cognizant
'Date Created			: 30/07/2008
'#######################################################################################################################
Option Explicit	'Forcing Variable declarations

Dim gobjExcelDataAccess: Set gobjExcelDataAccess = New ExcelDataAccess

'#######################################################################################################################
'Class Description		: Class to encapsulate utility functions of the framework
'Author					: Cognizant
'Date Created			: 23/07/2012
'#######################################################################################################################
Class ExcelDataAccess
	Private m_strDatabasePath, m_strDatabaseName
	Private m_objConn
	
	'###################################################################################################################
	Public Property Let DatabasePath(strDatabasePath)
		m_strDatabasePath = strDatabasePath
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Let DatabaseName(strDatabaseName)
		m_strDatabaseName = strDatabaseName
	End Property
	'###################################################################################################################
	
	
	'###################################################################################################################
	'Function Description	: Function to establish connection to excel
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Sub Connect()
		Dim objFso
		Set objFso = CreateObject("Scripting.FileSystemObject")
		If Not objFso.FolderExists(m_strDatabasePath) Then
			Err.Raise 5001, "Data Access Library", "ExcelDataAccess class: The given database path does not exist!"
		End If
		Set objFso = Nothing
		
		If m_strDatabaseName = "" Then
			Err.Raise 5002, "Data Access Library", "ExcelDataAccess class: The database name cannot be blank!"
		End If
		
		Dim strFilePath, strConnectionString
		strFilePath = m_strDatabasePath & "\" & m_strDatabaseName & ".xls"
		strConnectionString = "Provider=Microsoft.Jet.OLEDB.4.0;Data Source=" & strFilePath &_
																	";Extended Properties=""Excel 8.0;HDR=Yes;IMEX=2"""
		
		Set m_objConn = CreateObject("ADODB.Connection")
		m_objConn.Open strConnectionString
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to execute query
	'Input Parameters		: strQuery
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Function ExecuteQuery(strQuery)
		CheckConnection()
		Dim objCommand: Set objCommand = CreateObject("ADODB.Command")
		objCommand.ActiveConnection = m_objConn
		objCommand.CommandText = strQuery
		
		Dim objTestData: Set objTestData = CreateObject("ADODB.Recordset")
		objTestData.CursorLocation = 3
		objTestData.Open objCommand
		
		Set ExecuteQuery = objTestData
		
		Set objTestData.ActiveConnection = Nothing
		Set objTestData = Nothing
		Set objCommand = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub CheckConnection()
		If IsEmpty(m_objConn) Then
			Err.Raise 5003, "Data Access Library", "ExcelDataAccess class: Database not connected!"
		End If
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to execute non query
	'Input Parameters		: strNonQuery
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Sub ExecuteNonQuery(strNonQuery)
		CheckConnection()
		
		Dim objCommand: Set objCommand = CreateObject("ADODB.Command")
		objCommand.ActiveConnection = m_objConn
		objCommand.CommandText = strNonQuery
		
		objCommand.Execute()
		
		Set objCommand = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to disconnect from excel
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 23/07/2012
	'###################################################################################################################
	Public Sub Disconnect()
		CheckConnection()
		
		m_objConn.Close
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description	: Function to refresh the Excel workbook after executing a non-query and disconnecting.
	'							This ensures that any formulae dependent on the data that was updated by the non-query
	'							are refreshed immediately.
	'Input Parameters		: None
	'Return Value			: None
	'Author					: Cognizant
	'Date Created			: 21/08/2013
	'###################################################################################################################
	Public Sub Refresh()
		Dim strFilePath
		strFilePath = m_strDatabasePath & "\" & m_strDatabaseName & ".xls"
		Dim objExcel: Set objExcel = CreateObject("Excel.Application")
		objExcel.Workbooks.Open strFilePath
		
		objExcel.ActiveWorkbook.Save
		objExcel.ActiveWorkbook.Close
		
		Set objExcel = Nothing
	End Sub
	'###################################################################################################################
	
End Class
'#######################################################################################################################