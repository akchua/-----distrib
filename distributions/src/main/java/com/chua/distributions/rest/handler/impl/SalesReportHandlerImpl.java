package com.chua.distributions.rest.handler.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.constants.FileConstants;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.service.ClientOrderService;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.rest.handler.SalesReportHandler;
import com.chua.distributions.utility.Html;
import com.chua.distributions.utility.SimplePdfWriter;
import com.chua.distributions.utility.StringHelper;
import com.chua.distributions.utility.format.DateFormatter;
import com.chua.distributions.utility.template.SalesReportTemplate;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	19 Jan 2017
 */
@Component
@Transactional
public class SalesReportHandlerImpl implements SalesReportHandler {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ClientOrderService clientOrderService;

	@Autowired
	private VelocityEngine velocityEngine;
	
	@Override
	public ResultBean generateReport(SalesReportQueryBean salesReportQuery) {
		return generateReport(salesReportQuery, "SalesReport_" + DateFormatter.fileSafeFormat(new Date()) + ".pdf");
	}

	@Override
	public ResultBean generateReport(SalesReportQueryBean salesReportQuery, String fileName) {
		final ResultBean result;
		final ResultBean validateQuery = validateSalesReportQuery(salesReportQuery);
		
		if(validateQuery.getSuccess()) {
			final List<ClientOrder> clientOrders = clientOrderService.findAllBySalesReportQuery(salesReportQuery);
			
			if(clientOrders != null && !clientOrders.isEmpty()) {
				final String filePath = FileConstants.FILE_HOME + "files/sales_report/" + StringHelper.convertToFileSafeFormat(fileName);
				result = new ResultBean();
				result.setSuccess(
						SimplePdfWriter.write(
								new SalesReportTemplate(
										salesReportQuery, 
										(salesReportQuery.getClientId() != null) ? userService.find(salesReportQuery.getClientId()) : null, 
										clientOrders
											).merge(velocityEngine), 
								filePath,
								true)
						);
				if(result.getSuccess()) {
					final Map<String, Object> extras = new HashMap<String, Object>();
					extras.put("filePath", filePath);
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created sales report."));
					result.setExtras(extras);
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.TURQUOISE, "No sales found within the given restrictions."));
			}
		} else {
			result = validateQuery;
		}
		
		return result;
	}
	
	private ResultBean validateSalesReportQuery(SalesReportQueryBean salesReportQuery) {
		final ResultBean result;
		
		if(salesReportQuery.getFrom() == null || salesReportQuery.getTo() == null) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Date from and to cannot be empty."));
		} else if(!(salesReportQuery.getIncludePaid() || salesReportQuery.getIncludeDelivered() 
				|| salesReportQuery.getIncludeDispatched() || salesReportQuery.getIncludeAccepted()
				|| salesReportQuery.getIncludeSubmitted() || salesReportQuery.getIncludeCreating())) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "You must include at least 1 Status."));
		} else if(!salesReportQuery.getSendMail() && !salesReportQuery.getDownloadFile()) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Please select how you will receive the report."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
