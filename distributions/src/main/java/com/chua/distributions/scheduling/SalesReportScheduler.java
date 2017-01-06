package com.chua.distributions.scheduling;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.rest.handler.ClientOrderHandler;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	6 Jan 2017
 */
@Component
public class SalesReportScheduler {

	@Autowired
	private ClientOrderHandler clientOrderHandler;
	
	/**
	 * Daily Sales Report
	 * fires at 1:00AM everyday
	 * includes: all paid, all clients, all warehouses
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void dailySalesReport() {
		SalesReportQueryBean salesReportQuery = new SalesReportQueryBean();
		
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		
		salesReportQuery.setFrom(yesterday.getTime());
		salesReportQuery.setTo(yesterday.getTime());
		salesReportQuery.setPaidOnly(true);
		salesReportQuery.setShowNetTrail(true);
		salesReportQuery.setClientId(null);
		salesReportQuery.setWarehouse(null);
		
		final ResultBean result = clientOrderHandler.generateReport(salesReportQuery);
		System.out.println(result.getMessage());
	}
}
