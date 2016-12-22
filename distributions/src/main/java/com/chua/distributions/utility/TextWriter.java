package com.chua.distributions.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 11, 2016
 */
public class TextWriter {
	
	private TextWriter() {
		
	}

	public static void write(String message, String path) {
		try {
			File file = new File(path);
			if(file.getParentFile() != null) file.getParentFile().mkdirs();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
			
			String[] tokens = message.split("\n");
			for(String s : tokens) {
				bw.write(s);
				bw.newLine();
			}
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
