package com.chua.distributions.utility;

import java.io.BufferedWriter;
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
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, false));
			
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
