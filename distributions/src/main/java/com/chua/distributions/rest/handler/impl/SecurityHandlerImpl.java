package com.chua.distributions.rest.handler.impl;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.beans.UserBean;
import com.chua.distributions.rest.handler.SalesReportHandler;
import com.chua.distributions.rest.handler.SecurityHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 13, 2016
 */
@Transactional
@Component
public class SecurityHandlerImpl implements SecurityHandler {

	@Autowired
	private SalesReportHandler salesReportHandler;
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
	}

	@Override
	public UserBean getUser() {
		SalesReportQueryBean salesReportQuery = new SalesReportQueryBean();
		
		Calendar lastWeek = Calendar.getInstance();
		lastWeek.add(Calendar.DAY_OF_MONTH, -20);
		
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		
		salesReportQuery.setFrom(lastWeek.getTime());
		salesReportQuery.setTo(yesterday.getTime());
		salesReportQuery.setIncludePaid(true);
		salesReportQuery.setIncludeDelivered(true);
		salesReportQuery.setIncludeDispatched(false);
		salesReportQuery.setIncludeAccepted(false);
		salesReportQuery.setIncludeToFollow(false);
		salesReportQuery.setIncludeSubmitted(false);
		salesReportQuery.setIncludeCreating(false);
		salesReportQuery.setShowNetTrail(true);
		salesReportQuery.setClientId(null);
		salesReportQuery.setWarehouse(null);
		salesReportQuery.setSendMail(true);
		salesReportQuery.setDownloadFile(false);
		
		salesReportHandler.generateReport(salesReportQuery);
		
		return UserContextHolder.getUser();
	}
}
