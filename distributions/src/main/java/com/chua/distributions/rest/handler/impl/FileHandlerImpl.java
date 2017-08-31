package com.chua.distributions.rest.handler.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.distributions.annotations.CheckAuthority;
import com.chua.distributions.constants.FileConstants;
import com.chua.distributions.rest.handler.FileHandler;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	9 Feb 2017
 */
@Component
public class FileHandlerImpl implements FileHandler {

	@Autowired
	private FileConstants fileConstants;
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public File findSalesReportByFileName(String fileName) {
		return new File(fileConstants.getSalesHome() + fileName);
	}

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public File findPriceListByFileName(String fileName) {
		return new File(fileConstants.getPriceListHome() + fileName);
	}
}
