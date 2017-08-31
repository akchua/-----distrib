package com.chua.distributions.rest.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.distributions.constants.BusinessConstants;
import com.chua.distributions.constants.SystemConstants;
import com.chua.distributions.rest.handler.PublicConstantsHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   30 Aug 2017
 */
@Component
public class PublicConstantsHandlerImpl implements PublicConstantsHandler {

	@Autowired
	private SystemConstants systemConstants;
	
	@Autowired
	private BusinessConstants businessConstants;

	@Override
	public String getBusinessOfficialEmail() {
		return businessConstants.getBusinessOfficialEmail();
	}

	@Override
	public String getBusinessPrimaryContactNumber() {
		return businessConstants.getBusinessPrimaryContactNumber();
	}

	@Override
	public String getSystemVersion() {
		return systemConstants.getVersion();
	}
}
