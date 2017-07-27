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
import com.chua.distributions.database.entity.Company;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.service.CompanyService;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.enums.ClientSalesReportType;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.rest.handler.SalesReportHandler;
import com.chua.distributions.rest.handler.UserHandler;
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
	private CompanyService companyService;
	
	@Autowired
	private SalesReportHandler salesReportHandler;
	
	@Autowired
	private UserHandler userHandler;
	
	@Autowired
	private EmailUtil emailUtil;
	
	/**
	 * Weekly Report (Generic).
	 * fires at 1:00AM every Monday
	 * includes: a report containing delivered orders during the week
	 * 			 a report containing paid orders during the week
	 */
	@Scheduled(cron = "0 0 1 * * MON")
	public void weeklyReport() {
		List<ClientSalesReportType> reportsIncluded = new ArrayList<ClientSalesReportType>();
		
		reportsIncluded.add(ClientSalesReportType.DELIVERY);
		reportsIncluded.add(ClientSalesReportType.PAYMENTS);
		
		weeklySalesReport(reportsIncluded);
	}
	
	/**
	 * Monthly Delivery Report (Generic).
	 * fires at 1:00AM every 1st day of the month
	 * includes: all delivered orders during the month; for each client
	 */
	@Scheduled(cron = "0 0 1 1 * ?")
	public void monthlyDeliveryReport() {
		monthlySalesReport(ClientSalesReportType.DELIVERY);
	}
	
	/**
	 * Generic weekly sales report that can be adjusted to the given report type
	 * Creates 1 report for each company in each warehouse
	 * 
	 * @param clientSalesReportType The given report type
	 */
	public void weeklySalesReport(List<ClientSalesReportType> clientSalesReportTypes) {
		LOG.info("### Creating weekly sales report");
		
		SalesReportQueryBean salesReportQuery = new SalesReportQueryBean();
		
		Calendar lastWeek = Calendar.getInstance();
		lastWeek.add(Calendar.DAY_OF_MONTH, -7);
		
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		
		salesReportQuery.setFrom(lastWeek.getTime());
		salesReportQuery.setTo(yesterday.getTime());
		salesReportQuery.setClientId(null);
		salesReportQuery.setSendMail(true);
		salesReportQuery.setDownloadFile(false);
		
		final List<Company> companies = companyService.findAllList();
		
		for(Company company : companies) {
			LOG.info("### Starting weekly sales report for " + company.getName() + ".");
			
			salesReportQuery.setCompanyId(company.getId());
			
			for(Warehouse warehouse : Warehouse.values()) {
				salesReportQuery.setWarehouse(warehouse);
				
				List<String> attachments = new ArrayList<String>();
				
				for(ClientSalesReportType clientSalesReportType : clientSalesReportTypes) {
					setReportType(salesReportQuery, clientSalesReportType);
					
					final ResultBean result = salesReportHandler.generateReport(salesReportQuery);
					
					if(result.getSuccess()) {
						LOG.info(result.getMessage());
						attachments.add(FileConstants.SALES_HOME + (String) result.getExtras().get("fileName"));
					}
				}
				
				if(!attachments.isEmpty()) {
				emailUtil.send(company.getReportReceiver(),
						null,
						MailConstants.DEFAULT_EMAIL + ", " + userHandler.getEmailOfAllAdminAndManagers() + ", " + MailConstants.DEFAULT_REPORT_RECEIVER,
						"Weekly Sales Report",
						"Report for " + salesReportQuery.getFrom() + " - " + salesReportQuery.getTo() + ".",
						attachments.toArray(new String[0]));
				} else {
					// NOTIFY ADMINS
					String recipient = "";
					final List<User> administrators = userService.findAllAdministrators();
					
					for(User admin : administrators) {
						recipient += admin.getEmailAddress() + ", ";
					}
					
					emailUtil.send(recipient,
							"Weekly Sales Report",
							"Report for " + salesReportQuery.getFrom() + " - " + salesReportQuery.getTo() + "." + "\n"
							+ "\n"
							+ "No sales this week.");
				}
					
			}
			
			LOG.info("### Weekly sales report for " + company.getName() + " complete...");
		}
		
		LOG.info("### Weekly sales report complete...");
	}
	
	/**
	 * Generic monthly sales report that can be adjusted to the given report type
	 * Creates 1 report for each company in each warehouse
	 * 
	 * @param clientSalesReportType The given report type
	 */
	public void monthlySalesReport(ClientSalesReportType clientSalesReportType) {
		LOG.info("### Creating monthly client " + clientSalesReportType.getDisplayName() + " report");
		
		SalesReportQueryBean salesReportQuery = new SalesReportQueryBean();
		
		Calendar firstDayOfLastMonth = Calendar.getInstance();
		firstDayOfLastMonth.add(Calendar.MONTH, -1);
		firstDayOfLastMonth.set(Calendar.DATE, 1);
		
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		
		salesReportQuery.setFrom(firstDayOfLastMonth.getTime());
		salesReportQuery.setTo(yesterday.getTime());
		salesReportQuery.setClientSalesReportType(clientSalesReportType);
		if(clientSalesReportType.equals(ClientSalesReportType.STATUS_BASED)) {
			salesReportQuery.setIncludePaid(true);
			salesReportQuery.setIncludeDelivered(true);
			salesReportQuery.setIncludeDispatched(false);
			salesReportQuery.setIncludeAccepted(false);
			salesReportQuery.setIncludeToFollow(false);
			salesReportQuery.setIncludeSubmitted(false);
			salesReportQuery.setIncludeCreating(false);
		}
		salesReportQuery.setWarehouse(null);
		salesReportQuery.setSendMail(true);
		salesReportQuery.setDownloadFile(false);
		
		final List<Company> companies = companyService.findAllList();
		final List<User> clients = userService.findAllClients();
		
		for(Company company : companies) {
			LOG.info("### Starting monthly client " + clientSalesReportType.getDisplayName() + " report for " + company.getName() + ".");
			
			salesReportQuery.setCompanyId(company.getId());
			
			final List<String> filePaths = new ArrayList<String>();
			
			for(User client : clients) {
				salesReportQuery.setClientId(client.getId());
				
				final ResultBean result = salesReportHandler.generateReport(salesReportQuery, 
						client.getFormattedName() + "_-_" + DateUtil.getNameOfMonth(firstDayOfLastMonth) + "_Sales_Report.pdf");
				if(result.getSuccess()) {
					filePaths.add(FileConstants.SALES_HOME + (String) result.getExtras().get("fileName"));
				} else {
					LOG.error(client.getFormattedName() + " : " + result.getMessage());
				}
			}
			
			final String attachment;
			
			if(filePaths.size() > 1) {
				attachment = FileConstants.SALES_HOME + "MonthlyClient" + clientSalesReportType.getDisplayName() + "Report_" + DateFormatter.fileSafeFormat(new Date()) + ".zip";
				FileZipUtil.zipFile(filePaths, attachment);
			} else if(filePaths.size() == 1) {
				attachment = filePaths.get(1);
			} else {
				attachment = "";
			}
			
			if(!attachment.isEmpty()) {
				emailUtil.send(company.getReportReceiver(),
						null,
						MailConstants.DEFAULT_EMAIL + ", " + userHandler.getEmailOfAllAdminAndManagers() + ", " + MailConstants.DEFAULT_REPORT_RECEIVER,
						"Monthly Client " + clientSalesReportType.getDisplayName() + " Report",
						clientSalesReportType.getDisplayName() + " Report for " + salesReportQuery.getFrom() + " - " + salesReportQuery.getTo() + ".",
						new String[] { attachment });
			}
			
			LOG.info("### Monthly client " + clientSalesReportType.getDisplayName() + " report for " + company.getName() + " complete...");
		}
		
		LOG.info("### Monthly client " + clientSalesReportType.getDisplayName() + " report complete...");
	}
	
	private void setReportType(SalesReportQueryBean salesReportQuery, ClientSalesReportType clientSalesReportType) {
		salesReportQuery.setClientSalesReportType(clientSalesReportType);
		if(clientSalesReportType.equals(ClientSalesReportType.STATUS_BASED)) {
			salesReportQuery.setIncludePaid(true);
			salesReportQuery.setIncludeDelivered(true);
			salesReportQuery.setIncludeDispatched(false);
			salesReportQuery.setIncludeAccepted(false);
			salesReportQuery.setIncludeToFollow(false);
			salesReportQuery.setIncludeSubmitted(false);
			salesReportQuery.setIncludeCreating(false);
		}
	}
}
