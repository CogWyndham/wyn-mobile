package com.cognizant.framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelDataAccessforxlsm {
	
	private final String filePath, fileName;

	private String datasheetName;

	/**
	 * Function to get the Excel sheet name
	 * 
	 * @return The Excel sheet name
	 */
	public String getDatasheetName() {
		return datasheetName;
	}

	/**
	 * Function to set the Excel sheet name
	 * 
	 * @param datasheetName
	 *            The Excel sheet name
	 */
	public void setDatasheetName(String datasheetName) {
		this.datasheetName = datasheetName;
	}

	/**
	 * Constructor to initialize the excel data filepath and filename
	 * 
	 * @param filePath
	 *            The absolute path where the excel data file is stored
	 * @param fileName
	 *            The name of the excel data file (without the extension). Note
	 *            that .xlsx files are not supported, only .xls files are
	 *            supported
	 */
	public ExcelDataAccessforxlsm(String filePath, String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
	}
	
	private void checkPreRequisites() {
		if (datasheetName == null) {
			throw new FrameworkException(
					"ExcelDataAccess.datasheetName is not set!");
		}
	}

	private XSSFWorkbook openFileForReading() {

		String absoluteFilePath = filePath + Util.getFileSeparator() + fileName
				+ ".xlsx";

		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified file \""
					+ absoluteFilePath + "\" does not exist!");
		}

		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(fileInputStream);
			// fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while opening the specified Excel workbook \""
							+ absoluteFilePath + "\"");
		}

		return workbook;
	}
	
	private XSSFSheet getWorkSheet(XSSFWorkbook workbook) {
		XSSFSheet worksheet = workbook.getSheet(datasheetName);
		if (worksheet == null) {
			throw new FrameworkException("The specified sheet \""
					+ datasheetName + "\""
					+ "does not exist within the workbook \"" + fileName
					+ ".xls\"");
		}

		return worksheet;
	}
	
	/**
	 * Function to get the last row number within the worksheet
	 * 
	 * @return The last row number within the worksheet
	 */
	public int getLastRowNum() {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		return worksheet.getLastRowNum();
	}
	
	/**
	 * Function to get the value in the cell identified by the specified row
	 * number and column header
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnHeader
	 *            The column header of the cell
	 * @return The value present in the cell
	 */
	public String getValue(int rowNum, String columnHeader) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int columnNum = -1;
		String currentValue;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); currentColumnNum++) {

			XSSFCell cell = row.getCell(currentColumnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(columnHeader)) {
				columnNum = currentColumnNum;
				break;
			}
		}

		if (columnNum == -1) {
			throw new FrameworkException("The specified column header \""
					+ columnHeader + "\"" + "is not found in the sheet \""
					+ datasheetName + "\"!");
		} else {
			row = worksheet.getRow(rowNum);
			XSSFCell cell = row.getCell(columnNum);
			return getCellValueAsString(cell, formulaEvaluator);
		}
	}
	
	private String getCellValueAsString(XSSFCell cell,
			FormulaEvaluator formulaEvaluator) {
		if (cell == null || cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
			return "";
		} else {
			if (formulaEvaluator.evaluate(cell).getCellType() == XSSFCell.CELL_TYPE_ERROR) {
				throw new FrameworkException(
						"Error in formula within this cell! " + "Error code: "
								+ cell.getErrorCellValue());
			}

			DataFormatter dataFormatter = new DataFormatter();
			return dataFormatter.formatCellValue(formulaEvaluator
					.evaluateInCell(cell));
		}
	}



}
