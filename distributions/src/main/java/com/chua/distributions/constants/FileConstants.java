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
	
	public static String SALES_HOME;

	public static String PURCHASES_HOME;

	public static String DISPATCH_HOME;
	
	@Value("${file.home}")
    public void setFileHome(String home) {
        FileConstants.FILE_HOME = home;
        FileConstants.SALES_HOME = home + "files\\sales_report\\";
        FileConstants.PURCHASES_HOME = home + "files\\purchase_order\\";
        FileConstants.DISPATCH_HOME = home + "files\\dispatch\\";
    }
}
