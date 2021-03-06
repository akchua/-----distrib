package com.chua.distributions.utility.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.entity.Company;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.entity.Warehouse;
import com.chua.distributions.enums.Area;
import com.chua.distributions.utility.format.CurrencyFormatter;
import com.chua.distributions.utility.format.DateFormatter;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public class SalesReportTemplate implements Template {

	private SalesReportQueryBean salesReportQuery;
	
	private User client;
	
	private Company company;
	
	private Warehouse warehouse;
	
	private Area area;
	
	private List<ClientOrder> clientOrders;
	
	private List<String> summarizedClientOrders;
	
	public SalesReportTemplate(SalesReportQueryBean salesReportQuery, User client, Company company, Warehouse warehouse, List<ClientOrder> clientOrders) {
		this.salesReportQuery = salesReportQuery;
		this.client = client;
		this.company = company;
		this.warehouse = warehouse;
		this.area = salesReportQuery.getArea();
		this.clientOrders = clientOrders;
		this.summarizedClientOrders = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		for(ClientOrder clientOrder : clientOrders) {
			final ClientOrderSummaryTemplate orderSummaryTemplate = new ClientOrderSummaryTemplate(clientOrder);
			summarizedClientOrders.add(orderSummaryTemplate.merge(velocityEngine));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/salesReport.vm", "UTF-8", model);
	}
	
	public String getDateFrom() {
		return DateFormatter.longFormat(salesReportQuery.getFrom());
	}
	
	public String getDateTo() {
		return DateFormatter.longFormat(salesReportQuery.getTo());
	}
	
	public String getWarehouse() {
		final String warehouse;
		if(this.warehouse != null) {
			warehouse = this.warehouse.getName();
		} else {
			warehouse = "ALL WAREHOUSES";
		}
		return warehouse;
	}
	
	public String getClient() {
		final String client;
		if(this.client != null) {
			client = this.client.getBusinessName();
		} else {
			client = "ALL CLIENTS";
		}
		return client;
	}
	
	public String getCompany() {
		final String company;
		if(this.company != null) {
			company = this.company.getName();
		} else {
			company = "ALL COMPANIES";
		}
		return company;
	}
	
	public String getArea() {
		final String area;
		if(this.area != null) {
			area = this.area.getDisplayName();
		} else {
			area = "ALL AREAS";
		}
		return area;
	}
	
	public String getDate() {
		return DateFormatter.longFormat(new Date());
	}
	
	public List<String> getSummarizedClientOrders() {
		return summarizedClientOrders;
	}
	
	public String getTotalSales() {
		float totalSales = 0;
		for(ClientOrder clientOrder : clientOrders) {
			totalSales += clientOrder.getNetTotal();
		}
		return CurrencyFormatter.pesoFormat(totalSales);
	}
}
