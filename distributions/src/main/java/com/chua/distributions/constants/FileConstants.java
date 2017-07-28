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
	
	public static String PRICE_LIST_HOME;
	
	public static String SALES_HOME;

	public static String PURCHASES_HOME;

	public static String DISPATCH_HOME;
	
	public static String PRODUCT_IMAGE_HOME;
	
	@Value("${file.home}")
    public void setFileHome(String home) {
        FileConstants.FILE_HOME = home;
        FileConstants.PRICE_LIST_HOME = home + "files/price_list/";
        FileConstants.SALES_HOME = home + "files/sales_report/";
        FileConstants.PURCHASES_HOME = home + "files/purchase_order/";
        FileConstants.DISPATCH_HOME = home + "files/dispatch/";
        
        FileConstants.PRODUCT_IMAGE_HOME = home + "program_data/product_image/";
    }
}
