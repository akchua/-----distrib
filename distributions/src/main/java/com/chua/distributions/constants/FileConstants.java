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
	
	public static final String SALES_HOME = FILE_HOME + "files/sales_report/";

	public static final String PURCHASES_HOME = FILE_HOME + "files/purchase_order/";

	public static final String DISPATCH_HOME = FILE_HOME + "files/dispatch/";
	
	@Value("${file.home}")
    public void setFileHome(String home) {
        FileConstants.FILE_HOME = home;
    }
}
