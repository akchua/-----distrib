package com.chua.distributions.rest.handler;

import java.io.File;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	9 Feb 2017
 */
public interface FileHandler {

	File findSalesReportByFileName(String fileName);
	
	File findPriceListByFileName(String fileName);
	
	File findClientRankingByFileName(String fileName);
}
