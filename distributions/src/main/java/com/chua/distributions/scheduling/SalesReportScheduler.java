package com.chua.distributions.scheduling;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.constants.FileConstants;
import com.chua.distributions.constants.MailConstants;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.rest.handler.SalesReportHandler;
import com.chua.distributions.utility.DateUtil;
import com.chua.distributions.utility.EmailUtil;
import com.chua.distributions.utility.FileZipUtil;
import com.chua.distributions.utility.format.DateFormatter;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	6 Jan 2017
 */
@Component
@Transactional
public class SalesReportScheduler {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	
	@Autowired
	private SalesReportHandler salesReportHandler;
	
	@Autowired
	private EmailUtil emailUtil;
	
	/**
	 * Weekly Warehouse Sales Report.
	 * Includes report for each warehouse
	 * fires at 1:00AM every Saturday
	 * includes: all paid and delivered, all clients, all warehouses
	 */
	@Scheduled(cron = "0 0 1 * * SAT")
	public void weeklyWarehouseSalesReport() {
		LOG.info("Creating Weekly Warehouse Sales Report");
		
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
		salesReportQuery.setIncludeToFollow(false);
		salesReportQuery.setIncludeSubmitted(false);
		salesReportQuery.setIncludeCreating(false);
		salesReportQuery.setShowNetTrail(true);
		salesReportQuery.setClientId(null);
		salesReportQuery.setSendMail(true);
		salesReportQuery.setDownloadFile(false);
		
		for(Warehouse warehouse : Warehouse.values()) {
			salesReportQuery.setWarehouse(warehouse);
			
			final ResultBean result = salesReportHandler.generateReport(salesReportQuery);
			emailUtil.send(MailConstants.DEFAULT_REPORT_RECEIVER,
					null,
					MailConstants.DEFAULT_EMAIL,
					"Weekly Warehouse Sales Report",
					"Sales Report for " + salesReportQuery.getFrom() + " - " + salesReportQuery.getTo() + ".",
					new String[] { (String) result.getExtras().get("filePath") });
			
			if(result.getSuccess()) LOG.info(result.getMessage());
			else {
				LOG.error(result.getMessage());
				emailUtil.send(MailConstants.DEFAULT_REPORT_RECEIVER, 
						null,
						MailConstants.DEFAULT_EMAIL,
						"Weekly Warehouse Sales Report",
						"Sales Report for " + salesReportQuery.getFrom() + " - " + salesReportQuery.getTo() + "." + "\n"
						+ "\n"
						+ result.getMessage(),
						null);
			}
		}
	}
	
	/**
	 * Monthly Client Sales Report.
	 * Includes report for each client
	 * fires at 1:00AM every 1st day of the month
	 * includes: all paid and delivered, all clients, all warehouses
	 */
	@Scheduled(cron = "0 0 1 1 * ?")
	public void monthlyClientSalesReport() {
		LOG.info("Creating Weekly Warehouse Sales Report");
		
		SalesReportQueryBean salesReportQuery = new SalesReportQueryBean();
		
		Calendar firstDayOfLastMonth = Calendar.getInstance();
		firstDayOfLastMonth.add(Calendar.MONTH, -1);
		firstDayOfLastMonth.set(Calendar.DATE, 1);
		
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		
		salesReportQuery.setFrom(firstDayOfLastMonth.getTime());
		salesReportQuery.setTo(yesterday.getTime());
		salesReportQuery.setIncludePaid(true);
		salesReportQuery.setIncludeDelivered(true);
		salesReportQuery.setIncludeDispatched(false);
		salesReportQuery.setIncludeAccepted(false);
		salesReportQuery.setIncludeToFollow(false);
		salesReportQuery.setIncludeSubmitted(false);
		salesReportQuery.setIncludeCreating(false);
		salesReportQuery.setShowNetTrail(true);
		salesReportQuery.setWarehouse(null);
		salesReportQuery.setSendMail(true);
		salesReportQuery.setDownloadFile(false);
		
		final List<User> clients = userService.findAllClients();
		final List<String> filePaths = new ArrayList<String>();
		
		for(User client : clients) {
			salesReportQuery.setClientId(client.getId());
			
			final ResultBean result = salesReportHandler.generateReport(salesReportQuery, 
					client.getFormattedName() + "_-_" + DateUtil.getNameOfMonth(firstDayOfLastMonth) + "_Sales_Report.pdf");
			if(result.getSuccess()) {
				filePaths.add((String) result.getExtras().get("filePath"));
			} else {
				LOG.error(client.getFormattedName() + " : " + result.getMessage());
			}
		}
		
		final String attachment;
		
		if(filePaths.size() > 1) {
			attachment = FileConstants.FILE_HOME + "files/zip/MonthlyClientSales_" + DateFormatter.fileSafeFormat(new Date()) + ".zip";
			FileZipUtil.zipFile(filePaths, attachment);
		} else if(filePaths.size() == 1) {
			attachment = filePaths.get(1);
		} else {
			attachment = "";
		}
		
		emailUtil.send(MailConstants.DEFAULT_REPORT_RECEIVER,
				null,
				MailConstants.DEFAULT_EMAIL,
				"Monthly Client Sales Report",
				"Sales Report for " + salesReportQuery.getFrom() + " - " + salesReportQuery.getTo() + ".",
				new String[] { attachment });
	}
}
