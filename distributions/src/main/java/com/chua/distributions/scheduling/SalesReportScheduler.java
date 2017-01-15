package com.chua.distributions.scheduling;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.constants.MailConstants;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.rest.handler.ClientOrderHandler;
import com.chua.distributions.utility.EmailUtil;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	6 Jan 2017
 */
@Component
public class SalesReportScheduler {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ClientOrderHandler clientOrderHandler;
	
	/**
	 * Weekly Sales Report
	 * fires at 1:00AM every Saturday
	 * includes: all paid and delivered, all clients, all warehouses
	 */
	@Scheduled(cron = "0 0 1 * * SAT")
	public void weeklySalesReport() {
		LOG.info("Creating Weekly Sales Report");
		
		SalesReportQueryBean salesReportQuery = new SalesReportQueryBean();
		
		Calendar lastWeek = Calendar.getInstance();
		lastWeek.add(Calendar.DAY_OF_MONTH, -7);
		
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		
		salesReportQuery.setFrom(lastWeek.getTime());
		salesReportQuery.setTo(yesterday.getTime());
		salesReportQuery.setIncludePaid(true);
		salesReportQuery.setIncludeDelivered(true);
		salesReportQuery.setIncludeDispatched(false);
		salesReportQuery.setIncludeAccepted(false);
		salesReportQuery.setIncludeSubmitted(false);
		salesReportQuery.setIncludeCreating(false);
		salesReportQuery.setShowNetTrail(true);
		salesReportQuery.setClientId(null);
		
		for(Warehouse warehouse : Warehouse.values()) {
			salesReportQuery.setWarehouse(warehouse);
			
			final ResultBean result = clientOrderHandler.generateReport(salesReportQuery);
			
			if(result.getSuccess()) LOG.info(result.getMessage());
			else {
				LOG.error(result.getMessage());
				EmailUtil.send(MailConstants.DEFAULT_REPORT_RECEIVER, 
						null,
						MailConstants.DEFAULT_EMAIL,
						"Sales Report",
						"Sales Report for " + salesReportQuery.getFrom() + " - " + salesReportQuery.getTo() + "." + "\n"
						+ "\n"
						+ result.getMessage(),
						null);
			}
		}
	}
}
