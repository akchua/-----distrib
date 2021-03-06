package com.chua.distributions.rest.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.constants.BusinessConstants;
import com.chua.distributions.constants.FileConstants;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.entity.Company;
import com.chua.distributions.database.entity.Warehouse;
import com.chua.distributions.database.service.ClientOrderService;
import com.chua.distributions.database.service.CompanyService;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.database.service.WarehouseService;
import com.chua.distributions.enums.ClientSalesReportType;
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
	private CompanyService companyService;
	
	@Autowired
	private WarehouseService warehouseService;
	
	@Autowired
	private ClientOrderService clientOrderService;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Autowired
	private BusinessConstants businessConstants;

	@Autowired
	private VelocityEngine velocityEngine;
	
	@Override
	public ResultBean generateReport(SalesReportQueryBean salesReportQuery) {
		final ResultBean validateQuery = validateSalesReportQuery(salesReportQuery);
		
		if(validateQuery.getSuccess()) {
			final Company company = companyService.find(salesReportQuery.getCompanyId());
			final Warehouse warehouse = warehouseService.find(salesReportQuery.getWarehouseId());
			String fileName = "";
			
			// DIVERSEY_PAYMENTS_MAIN_DateFROM_DateTO.pdf
			if(company != null) fileName += company.getShortName() + "_";
			fileName += salesReportQuery.getClientSalesReportType().getDisplayName() + "_";
			if(warehouse != null) fileName += warehouse.getName() + "_";
			if(salesReportQuery.getArea() != null) fileName += salesReportQuery.getArea().getDisplayName() + "_";
			
			fileName += DateFormatter.shortFormat(salesReportQuery.getFrom()) + "_to_";
			fileName += DateFormatter.shortFormat(salesReportQuery.getTo());
			fileName += ".pdf";
			
			return generateReport(salesReportQuery, fileName);
		} else {
			return validateQuery;
		}
	}

	@Override
	public ResultBean generateReport(SalesReportQueryBean salesReportQuery, String fileName) {
		final ResultBean result;
		final ResultBean validateQuery = validateSalesReportQuery(salesReportQuery);
		
		if(validateQuery.getSuccess()) {
			final List<ClientOrder> clientOrders = clientOrderService.findAllBySalesReportQuery(salesReportQuery);
			
			if(clientOrders != null && !clientOrders.isEmpty()) {
				fileName = StringHelper.convertToFileSafeFormat(fileName);
				final String filePath = fileConstants.getSalesHome() + fileName;
				result = new ResultBean();
				result.setSuccess(
						SimplePdfWriter.write(
								new SalesReportTemplate(
										salesReportQuery, 
										(salesReportQuery.getClientId() != null) ? userService.find(salesReportQuery.getClientId()) : null,
										(salesReportQuery.getCompanyId() != null) ? companyService.find(salesReportQuery.getCompanyId()) : null,
										(salesReportQuery.getWarehouseId() != null) ? warehouseService.find(salesReportQuery.getWarehouseId()) : null,
										clientOrders
											).merge(velocityEngine), 
								businessConstants.getBusinessShortName(),
								filePath,
								true)
						);
				if(result.getSuccess()) {
					final Map<String, Object> extras = new HashMap<String, Object>();
					extras.put("fileName", fileName);
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created sales report."));
					result.setExtras(extras);
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.TURQUOISE, "No sale found for the given restriction."));
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
		} else if(salesReportQuery.getClientSalesReportType() == null) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "You must choose a report type."));
		} else if(salesReportQuery.getClientSalesReportType() == ClientSalesReportType.STATUS_BASED &&
				(!(salesReportQuery.getIncludePaid() || salesReportQuery.getIncludeDelivered() 
				|| salesReportQuery.getIncludeDispatched() || salesReportQuery.getIncludeAccepted()
				|| salesReportQuery.getIncludeSubmitted() || salesReportQuery.getIncludeCreating()))) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "You must include at least 1 Status."));
		} else if(!salesReportQuery.getSendMail() && !salesReportQuery.getDownloadFile()) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Please select how you will receive the report."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
