package com.chua.distributions.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This is a utility class used for writing in Portable Document Format (.pdf)
 * The default font used is Courier with font size 9
 * 
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 23, 2016
 */
public class SimplePdfWriter {

	/**
	 * The default font
	 * 
	 * Courier font is used for monospacing
	 * Font size 9 in landscape can contain at most 133 characters
	 * Font size 9 in portrait can contain at most 96 characters
	 */
	private static final Font defaultFont = new Font(Font.FontFamily.COURIER, 9, Font.NORMAL);
	
	@SuppressWarnings("unused")
	private static final Paragraph newLine = new Paragraph(" ");
	
	/**
	 * This class does not need to be instantiated
	 */
	private SimplePdfWriter() {
		
	}
	
	/**
	 * This static method is used to write a Java String to a pdf file
	 * 
	 * @param message The message to be written
	 * @param path The path where the file will be created
	 * @param landscape True if orientation is landscape
	 */
	public static void write(String message, String path, boolean landscape) {
		try {
			File file = new File(path);
			if(file.getParentFile() != null) file.getParentFile().mkdirs();
			
			Document document = new Document();
			if(landscape) document.setPageSize(PageSize.LETTER.rotate());
			
			PdfWriter.getInstance(document, new FileOutputStream(file));
			
			document.open();
			
			Paragraph paragraph = new Paragraph(message, defaultFont);
			document.add(paragraph);
			
			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
