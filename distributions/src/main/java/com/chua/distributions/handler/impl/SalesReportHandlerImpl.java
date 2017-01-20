package com.chua.distributions.handler.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.constants.FileConstants;
import com.chua.distributions.constants.MailConstants;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.service.ClientOrderService;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.handler.SalesReportHandler;
import com.chua.distributions.utility.EmailUtil;
import com.chua.distributions.utility.Html;
import com.chua.distributions.utility.SimplePdfWriter;
import com.chua.distributions.utility.format.DateFormatter;
import com.chua.distributions.utility.format.SalesReportFormatter;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	19 Jan 2017
 */
@Component
@Transactional
public class SalesReportHandlerImpl implements SalesReportHandler {

	@Autowired
	UserService userService;
	
	@Autowired
	ClientOrderService clientOrderService;
	
	@Override
	public ResultBean generateReport(SalesReportQueryBean salesReportQuery, String recipient) {
		final ResultBean result;
		final ResultBean validateQuery = validateSalesReportQuery(salesReportQuery);
		
		if(validateQuery.getSuccess()) {
			final List<ClientOrder> clientOrders = clientOrderService.findAllBySalesReportQuery(salesReportQuery);
			
			if(clientOrders != null && !clientOrders.isEmpty()) {
				final String filePath = FileConstants.FILE_HOME + "files/sales_report/SalesReport_" + DateFormatter.fileSafeFormat(new Date()) + ".pdf";
				SimplePdfWriter.write(SalesReportFormatter.format(salesReportQuery, 
						(salesReportQuery.getClientId() != null) ? userService.find(salesReportQuery.getClientId()) : null, 
								clientOrders), filePath, true);

				result = new ResultBean();
				
				result.setSuccess(EmailUtil.send(recipient, 
							null,
							MailConstants.DEFAULT_EMAIL,
							"Sales Report",
							"Sales Report for " + salesReportQuery.getFrom() + " - " + salesReportQuery.getTo() + ".",
							new String[] { filePath }));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created sales report and it has been emailed to " + Html.text(Color.TURQUOISE, recipient) + "."));
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
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
