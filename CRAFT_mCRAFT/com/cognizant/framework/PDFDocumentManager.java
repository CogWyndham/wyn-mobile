package com.cognizant.framework;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

class PDFDocumentManager {

	private final String filePath, fileName;

	/**
	 * Constructor to initialize the PDF Document filepath and filename
	 * 
	 * @param filePath
	 *            The absolute path where the Word Document is stored
	 * @param fileName
	 *            The name of the PDF Document (without the extension).
	 */
	PDFDocumentManager(String filePath, String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
	}

	/**
	 * Function to create a new PDF document
	 * 
	 * @param screenshots
	 */
	void createDocument(File[] screenshots, boolean isMobile) {
		String absoluteFilePath = filePath + Util.getFileSeparator() + fileName
				+ ".pdf";
		try {
			Document document = new Document();
			File f = new File(absoluteFilePath);
			@SuppressWarnings("unused")
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(f));
			document.open();
			addPicture(screenshots, isMobile, document);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while writing into the specified PDF document \""
							+ absoluteFilePath + "\"");

		}

	}

	public void addPicture(File[] screenshots, boolean isMobile,
			Document document) {
		int totalScreenshots = screenshots.length;
		int count = 0;
		try {
			for (File screenshot : screenshots) {
				count++;
				Paragraph graphicalReport = new Paragraph(screenshot.getName());
				graphicalReport.setAlignment(Element.ALIGN_CENTER);
				graphicalReport.setSpacingBefore(20f);
				document.add(graphicalReport);
				Image logo = Image.getInstance(screenshot.toString());
				if (isMobile) {
					logo.scaleAbsolute(475f, 250f);
				} else {
					logo.scaleAbsolute(225f, 400f);
				}
				logo.setAlignment(Element.ALIGN_CENTER);
				document.add(logo);
				if (count < totalScreenshots) {
					document.newPage();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
