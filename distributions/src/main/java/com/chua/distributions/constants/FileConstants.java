package com.chua.distributions.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 17, 2016
 */
@Component
public class FileConstants {

	public static String FILE_HOME;
	
	@Value("${file.home}")
    public void setFileHome(String home) {
        FileConstants.FILE_HOME = home;
    }
}
