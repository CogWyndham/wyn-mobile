package com.cognizant.framework;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Hyperlink; // not available under hssf
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper; // not available under hssf

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import org.apache.poi.hssf.util.CellRangeAddress;	not used because this is deprecated

/**
 * Class to encapsulate the excel data access layer of the framework
 * 
 * @author Cognizant
 */
public class ExcelDataAccess {
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
	public ExcelDataAccess(String filePath, String fileName) {
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

	
	private void writeIntoFile(XSSFWorkbook workbook) {
		String absoluteFilePath = filePath + Util.getFileSeparator() + fileName
				+ ".xlsx";

		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified file \""
					+ absoluteFilePath + "\" does not exist!");
		}

		try {
			workbook.write(fileOutputStream);
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while writing into the specified Excel workbook \""
							+ absoluteFilePath + "\"");
		}
	}

	private XSSFSheet getWorkSheet(XSSFWorkbook workbook) {
		XSSFSheet worksheet = workbook.getSheet(datasheetName);
		if (worksheet == null) {
			throw new FrameworkException("The specified sheet \""
					+ datasheetName + "\""
					+ "does not exist within the workbook \"" + fileName
					+ ".xlsx\"");
		}

		return worksheet;
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding row number
	 * 
	 * @param key
	 *            The value being searched for
	 * @param columnNum
	 *            The column number in which the key should be searched
	 * @param startRowNum
	 *            The row number from which the search should start
	 * @return The row number in which the specified key is found (-1 if the key
	 *         is not found)
	 */
	public int getRowNum(String key, int columnNum, int startRowNum) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		String currentValue;
		for (int currentRowNum = startRowNum; currentRowNum <= worksheet
				.getLastRowNum(); currentRowNum++) {

			XSSFRow row = worksheet.getRow(currentRowNum);
			XSSFCell cell = row.getCell(columnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(key)) {
				return currentRowNum;
			}
		}

		return -1;
	}

	/*
	 * private String getCellValueAsString(HSSFCell cell, FormulaEvaluator
	 * formulaEvaluator) { if (cell == null) { return ""; } else {
	 * switch(formulaEvaluator.evaluate(cell).getCellType()) { case
	 * HSSFCell.CELL_TYPE_BLANK: case HSSFCell.CELL_TYPE_STRING:
	 * System.out.print("string: "); return cell.getStringCellValue().trim();
	 * 
	 * case HSSFCell.CELL_TYPE_BOOLEAN: System.out.print("bool: "); return
	 * Boolean.toString(cell.getBooleanCellValue());
	 * 
	 * case HSSFCell.CELL_TYPE_NUMERIC: if
	 * (HSSFDateUtil.isCellDateFormatted(cell)) { System.out.print("date: ");
	 * return cell.getDateCellValue().toString(); } else {
	 * System.out.print("numeric: "); return
	 * Double.toString(cell.getNumericCellValue()); }
	 * 
	 * case HSSFCell.CELL_TYPE_ERROR: System.out.print("error: "); throw new
	 * FrameworkException("Error in formula within this cell! " + "Error code: "
	 * + cell.getErrorCellValue());
	 * 
	 * //case HSSFCell.CELL_TYPE_FORMULA: // This will never occur!
	 * 
	 * default: throw new FrameworkException("Unhandled cell type!"); } } }
	 */

	private String getCellValueAsString(XSSFCell cell,
			FormulaEvaluator formulaEvaluator) {
		if (cell == null || cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
			return "";
		} else {
			if (formulaEvaluator.evaluate(cell).getCellType() == HSSFCell.CELL_TYPE_ERROR) {
				throw new FrameworkException(
						"Error in formula within this cell! " + "Error code: "
								+ cell.getErrorCellValue());
			}

			DataFormatter dataFormatter = new DataFormatter();
			return dataFormatter.formatCellValue(formulaEvaluator
					.evaluateInCell(cell));
		}
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding row number
	 * 
	 * @param key
	 *            The value being searched for
	 * @param columnNum
	 *            The column number in which the key should be searched
	 * @return The row number in which the specified key is found (-1 if the key
	 *         is not found)
	 */
	public int getRowNum(String key, int columnNum) {
		return getRowNum(key, columnNum, 0);
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
	 * Function to search for a specified key within a column, and return the
	 * corresponding occurence count
	 * 
	 * @param key
	 *            The value being searched for
	 * @param columnNum
	 *            The column number in which the key should be searched
	 * @param startRowNum
	 *            The row number from which the search should start
	 * @return The occurence count of the specified key
	 */
	public int getRowCount(String key, int columnNum, int startRowNum) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		int rowCount = 0;
		boolean keyFound = false;

		String currentValue;
		for (int currentRowNum = startRowNum; currentRowNum <= worksheet
				.getLastRowNum(); currentRowNum++) {

			XSSFRow row = worksheet.getRow(currentRowNum);
			XSSFCell cell = row.getCell(columnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(key)) {
				rowCount++;
				keyFound = true;
			} else {
				if (keyFound) {
					break; // Assumption: Keys always appear contiguously
				}
			}
		}

		return rowCount;
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding occurence count
	 * 
	 * @param key
	 *            The value being searched for
	 * @param columnNum
	 *            The column number in which the key should be searched
	 * @return The occurence count of the specified key
	 */
	public int getRowCount(String key, int columnNum) {
		return getRowCount(key, columnNum, 0);
	}

	/**
	 * Function to search for a specified key within a row, and return the
	 * corresponding column number
	 * 
	 * @param key
	 *            The value being searched for
	 * @param rowNum
	 *            The row number in which the key should be searched
	 * @return The column number in which the specified key is found (-1 if the
	 *         key is not found)
	 */
	public int getColumnNum(String key, int rowNum) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(rowNum);
		String currentValue;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); currentColumnNum++) {

			XSSFCell cell = row.getCell(currentColumnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(key)) {
				return currentColumnNum;
			}
		}

		return -1;
	}

	/**
	 * Function to get the value in the cell identified by the specified row and
	 * column numbers
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnNum
	 *            The column number of the cell
	 * @return The value present in the cell
	 */
	public String getValue(int rowNum, int columnNum) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(rowNum);
		XSSFCell cell = row.getCell(columnNum);
		return getCellValueAsString(cell, formulaEvaluator);
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

	private XSSFCellStyle applyCellStyle(XSSFWorkbook workbook,
			ExcelCellFormatting cellFormatting) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		if (cellFormatting.centred) {
			cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		}
		cellStyle.setFillForegroundColor(cellFormatting.getBackColorIndex());
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

		XSSFFont font = workbook.createFont();
		font.setFontName(cellFormatting.getFontName());
		font.setFontHeightInPoints(cellFormatting.getFontSize());
		if (cellFormatting.bold) {
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		}
		font.setColor(cellFormatting.getForeColorIndex());
		cellStyle.setFont(font);

		return cellStyle;
	}

	/**
	 * Function to set the specified value in the cell identified by the
	 * specified row and column numbers
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnNum
	 *            The column number of the cell
	 * @param value
	 *            The value to be set in the cell
	 */
	public void setValue(int rowNum, int columnNum, String value) {
		setValue(rowNum, columnNum, value, null);
	}

	/**
	 * Function to set the specified value in the cell identified by the
	 * specified row and column numbers
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnNum
	 *            The column number of the cell
	 * @param value
	 *            The value to be set in the cell
	 * @param cellFormatting
	 *            The {@link ExcelCellFormatting} to be applied to the cell
	 */
	public void setValue(int rowNum, int columnNum, String value,
			ExcelCellFormatting cellFormatting) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		XSSFRow row = worksheet.getRow(rowNum);
		XSSFCell cell = row.createCell(columnNum);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);

		if (cellFormatting != null) {
			XSSFCellStyle cellStyle = applyCellStyle(workbook, cellFormatting);
			cell.setCellStyle(cellStyle);
		}

		writeIntoFile(workbook);
	}

	/**
	 * Function to set the specified value in the cell identified by the
	 * specified row number and column header
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnHeader
	 *            The column header of the cell
	 * @param value
	 *            The value to be set in the cell
	 */
	public void setValue(int rowNum, String columnHeader, String value) {
		setValue(rowNum, columnHeader, value, null);
	}

	/**
	 * Function to set the specified value in the cell identified by the
	 * specified row number and column header
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnHeader
	 *            The column header of the cell
	 * @param value
	 *            The value to be set in the cell
	 * @param cellFormatting
	 *            The {@link ExcelCellFormatting} to be applied to the cell
	 */
	public void setValue(int rowNum, String columnHeader, String value,
			ExcelCellFormatting cellFormatting) {
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
			XSSFCell cell = row.createCell(columnNum);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(value);

			if (cellFormatting != null) {
				XSSFCellStyle cellStyle = applyCellStyle(workbook,
						cellFormatting);
				cell.setCellStyle(cellStyle);
			}

			writeIntoFile(workbook);
		}
	}

	/**
	 * Function to set a hyperlink in the cell identified by the specified row
	 * and column numbers
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnNum
	 *            The column number of the cell
	 * @param linkAddress
	 *            The link address to be set
	 */
	public void setHyperlink(int rowNum, int columnNum, String linkAddress) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		XSSFRow row = worksheet.getRow(rowNum);
		XSSFCell cell = row.getCell(columnNum);
		if (cell == null) {
			throw new FrameworkException("Specified cell is empty! "
					+ "Please set a value before including a hyperlink...");
		}

		setCellHyperlink(workbook, cell, linkAddress);

		writeIntoFile(workbook);
	}

	private void setCellHyperlink(XSSFWorkbook workbook, XSSFCell cell,
			String linkAddress) {
		XSSFCellStyle cellStyle = cell.getCellStyle();
		//XSSFFont font = cellStyle.getFont(workbook);
		XSSFFont font = cellStyle.getFont();
		font.setUnderline(XSSFFont.U_SINGLE);
		cellStyle.setFont(font);

		CreationHelper creationHelper = workbook.getCreationHelper();
		Hyperlink hyperlink = creationHelper
				.createHyperlink(Hyperlink.LINK_URL);
		hyperlink.setAddress(linkAddress);

		cell.setCellStyle(cellStyle);
		cell.setHyperlink(hyperlink);
	}

	/**
	 * Function to set a hyperlink in the cell identified by the specified row
	 * number and column header
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnHeader
	 *            The column header of the cell
	 * @param linkAddress
	 *            The link address to be set
	 */
	public void setHyperlink(int rowNum, String columnHeader, String linkAddress) {
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
			if (cell == null) {
				throw new FrameworkException("Specified cell is empty! "
						+ "Please set a value before including a hyperlink...");
			}

			setCellHyperlink(workbook, cell, linkAddress);

			writeIntoFile(workbook);
		}
	}

	/**
	 * Function to create a new Excel workbook
	 */
	public void createWorkbook() {
		XSSFWorkbook workbook = new XSSFWorkbook();

		writeIntoFile(workbook);
	}

	/**
	 * Function to add a sheet to the Excel workbook
	 * 
	 * @param sheetName
	 *            The sheet name to be added
	 */
	public void addSheet(String sheetName) {
		XSSFWorkbook workbook = openFileForReading();

		XSSFSheet worksheet = workbook.createSheet(sheetName);
		worksheet.createRow(0); // include a blank row in the sheet created

		writeIntoFile(workbook);

		this.datasheetName = sheetName;
	}

	/**
	 * Function to add a new row to the Excel worksheet
	 * 
	 * @return The row number of the newly added row
	 */
	public int addRow() {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		int newRowNum = worksheet.getLastRowNum() + 1;
		worksheet.createRow(newRowNum);

		writeIntoFile(workbook);

		return newRowNum;
	}

	/**
	 * Function to add a new column to the Excel worksheet
	 * 
	 * @param columnHeader
	 *            The column header to be added
	 */
	public void addColumn(String columnHeader) {
		addColumn(columnHeader, null);
	}

	/**
	 * Function to add a new column to the Excel worksheet
	 * 
	 * @param columnHeader
	 *            The column header to be added
	 * @param cellFormatting
	 *            The {@link ExcelCellFormatting} to be applied to the column
	 *            header
	 */
	public void addColumn(String columnHeader,
			ExcelCellFormatting cellFormatting) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		XSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int lastCellNum = row.getLastCellNum();
		if (lastCellNum == -1) {
			lastCellNum = 0;
		}

		XSSFCell cell = row.createCell(lastCellNum);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(columnHeader);

		if (cellFormatting != null) {
			XSSFCellStyle cellStyle = applyCellStyle(workbook, cellFormatting);
			cell.setCellStyle(cellStyle);
		}

		writeIntoFile(workbook);
	}

	/**
	 * Function to set a specified color at the specified index within the
	 * custom palette
	 * 
	 * @param index
	 *            The index at which the color should be set within the palette
	 * @param hexColor
	 *            The hex value of the color to be set within the palette
	 */
	public void setCustomPaletteColor(short index, String hexColor) {
		XSSFWorkbook workbook = openFileForReading();
		//XSSFPalette palette = workbook.getCustomPalette();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell( 0);
		cell.setCellValue("custom XSSF colors");
		
		if (index < 0x8 || index > 0x40) {
			throw new FrameworkException(
					"Valid indexes for the Excel custom palette are from 0x8 to 0x40 (inclusive)!");
		}

		Color color = Color.decode(hexColor);
		//palette.setColorAtIndex(index, (byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
		
//		XSSFColor red = new XSSFColor(new java.awt.Color(192,192,192));
//		XSSFcellStyle.setFillForegroundColor(red);
//		XSSFColor blue = new XSSFColor(new java.awt.Color(192,192,192));
//		
//		palette.setColorAtIndex(index, (byte) color.getRed(),
//				(byte) color.getGreen(), (byte) color.getBlue());
		    XSSFCellStyle style1 = workbook.createCellStyle();
		    style1.setFillForegroundColor(new XSSFColor(new java.awt.Color(128, 0, 128)));
		    style1.setFillPattern(CellStyle.SOLID_FOREGROUND);

		writeIntoFile(workbook);
	}

	/**
	 * Function to merge the specified range of cells (all inputs are 0-based)
	 * 
	 * @param firstRow
	 *            The first row
	 * @param lastRow
	 *            The last row
	 * @param firstCol
	 *            The first column
	 * @param lastCol
	 *            The last column
	 */
	public void mergeCells(int firstRow, int lastRow, int firstCol, int lastCol) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow,
				lastRow, firstCol, lastCol);
		worksheet.addMergedRegion(cellRangeAddress);

		writeIntoFile(workbook);
	}

	/**
	 * Function to specify whether the row summaries appear below the detail
	 * within an outline (grouped set of rows)
	 * 
	 * @param rowSumsBelow
	 *            Boolean value to specify row summaries below detail within an
	 *            outline
	 */
	public void setRowSumsBelow(boolean rowSumsBelow) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		worksheet.setRowSumsBelow(rowSumsBelow);

		writeIntoFile(workbook);
	}

	/**
	 * Function to outline (i.e., group together) the specified rows
	 * 
	 * @param firstRow
	 *            The first row
	 * @param lastRow
	 *            The last row
	 */
	public void groupRows(int firstRow, int lastRow) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		worksheet.groupRow(firstRow, lastRow);

		writeIntoFile(workbook);
	}

	/**
	 * Function to automatically adjust the column width to fit the contents for
	 * the specified range of columns (all inputs are 0-based)
	 * 
	 * @param firstCol
	 *            The first column
	 * @param lastCol
	 *            The last column
	 */
	public void autoFitContents(int firstCol, int lastCol) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		if (firstCol < 0) {
			firstCol = 0;
		}

		if (firstCol > lastCol) {
			throw new FrameworkException(
					"First column cannot be greater than last column!");
		}

		for (int currentColumn = firstCol; currentColumn <= lastCol; currentColumn++) {
			worksheet.autoSizeColumn(currentColumn);
		}

		writeIntoFile(workbook);
	}

	/**
	 * Function to add an outer border around the specified range of columns
	 * (all inputs are 0-based)
	 * 
	 * @param firstCol
	 *            The first column
	 * @param lastCol
	 *            The last column
	 */
	
	//Commented by Ram

	public void addOuterBorder(int firstCol, int lastCol) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		CellRangeAddress cellRangeAddress = new CellRangeAddress(0,
				worksheet.getLastRowNum(), firstCol, lastCol);
		
		//XSSFRegionUtil.setBorderBottom(XSSFCellStyle.BORDER_THIN,
				//cellRangeAddress, worksheet, workbook);
		
		
				
		//XSSFRegionUtil.setBorderRight(XSSFCellStyle.BORDER_THIN,
				//cellRangeAddress, worksheet, workbook);

		writeIntoFile(workbook);
	}

	/**
	 * Function to add an outer border around the specified range of columns
	 * (all inputs are 0-based)
	 * 
	 * @param firstRow
	 *            The first row
	 * @param lastRow
	 *            The last row
	 * @param firstCol
	 *            The first column
	 * @param lastCol
	 *            The last column
	 */
	
	//Commented by Ram
	
	public void addOuterBorder(int firstRow, int lastRow, int firstCol,
			int lastCol) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow,
				lastRow, firstCol, lastCol);
//		XSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
//				cellRangeAddress, worksheet, workbook);
//		XSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
//				cellRangeAddress, worksheet, workbook);
//		XSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
//				cellRangeAddress, worksheet, workbook);
//		XSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
//				cellRangeAddress, worksheet, workbook);

		writeIntoFile(workbook);
	}
	
}