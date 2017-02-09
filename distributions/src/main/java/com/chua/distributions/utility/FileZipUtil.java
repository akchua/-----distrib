package com.chua.distributions.utility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	8 Feb 2017
 */
public class FileZipUtil {

	/**
	 * Used to zip all files pointed by the given file paths
	 * @param filePaths The list of file paths to zip
	 * @param zipFilePath The path to save the zipped files at
	 */
	public static void zipFile(List<String> filePaths, String zipFilePath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		FileInputStream fis = null;
		
		try {
			File zipFile = new File(convertPathToZip(zipFilePath));
			if(zipFile.getParentFile() != null) zipFile.getParentFile().mkdirs();
			
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(new BufferedOutputStream(fos));
			
			for(String filePath : filePaths) {
				File file = new File(filePath);
				
				zos.putNextEntry(new ZipEntry(file.getName()));
				fis = new FileInputStream(file);
				
				byte[] buf = new byte[1024];
				int bytesRead;
				
				while((bytesRead = fis.read(buf)) > 0) {
					zos.write(buf, 0, bytesRead);
				}
				
				zos.flush();
				fis.close();
			}
			
			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method checks if the given path specifies a zip file.
	 * If not then it will append .zip at the end of the path
	 * @param path The path to be checked
	 * @return The converted path
	 */
	public static String convertPathToZip(String path) {
		String[] tokens = path.split("\\.");
		if(!tokens[tokens.length - 1].equals("zip")) {
			path += ".zip";
		}
		return path;
	}
}
