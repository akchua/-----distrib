package com.chua.distributions.scheduling;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.chua.distributions.constants.BusinessConstants;
import com.chua.distributions.constants.FileConstants;
import com.chua.distributions.database.entity.Company;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.entity.Warehouse;
import com.chua.distributions.database.service.CompanyService;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.database.service.WarehouseService;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.ClientSalesReportType;
import com.chua.distributions.rest.handler.SalesReportHandler;
import com.chua.distributions.rest.handler.UserHandler;
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
	private WarehouseService warehouseService;
	
	@Autowired
	private SalesReportHandler salesReportHandler;
	
	@Autowired
	private UserHandler userHandler;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Autowired
	private BusinessConstants businessConstants;
	
	/**
	 * Weekly Report (Generic).
	 * fires at 1:00AM every Monday
	 * includes: a report containing delivered orders during the week
	 * 			 a report containing paid orders during the week
	 */
	@Scheduled(cron = "0 0 1 * * MON")
	public void weeklyReport() {
		LOG.info("### Creating weekly sales report");
		
		Calendar lastWeek = Calendar.getInstance();
		lastWeek.add(Calendar.DAY_OF_MONTH, -7);
		
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		
		List<ClientSalesReportType> reportsIncluded = new ArrayList<ClientSalesReportType>();
		reportsIncluded.add(ClientSalesReportType.DELIVERY);
		reportsIncluded.add(ClientSalesReportType.PAYMENTS);
		
		generateSalesReport(lastWeek.getTime(), yesterday.getTime(), Arrays.asList(Area.values()), companyService.findAllList(), null, 
				warehouseService.findAllList(), reportsIncluded);
		
		LOG.info("### Weekly sales report complete...");
	}
	
	/**
	 * Monthly Delivery Report (Generic).
	 * fires at 1:00AM every 1st day of the month
	 * includes: all delivered orders during the month; for each client
	 */
	@Scheduled(cron = "0 0 1 1 * ?")
	public void monthlyReport() {
		LOG.info("### Creating monthly sales report");
		
		Calendar firstDayOfLastMonth = Calendar.getInstance();
		firstDayOfLastMonth.add(Calendar.MONTH, -1);
		firstDayOfLastMonth.set(Calendar.DATE, 1);
		
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		
		List<ClientSalesReportType> reportsIncluded = new ArrayList<ClientSalesReportType>();
		reportsIncluded.add(ClientSalesReportType.DELIVERY);
		
		generateSalesReport(firstDayOfLastMonth.getTime(), yesterday.getTime(), Arrays.asList((Area) null), companyService.findAllList(), userService.findAllClients(), 
				null, reportsIncluded);
		
		LOG.info("### Monthly sales report complete...");
	}
	
	public void generateSalesReport(Date from, Date to, 
			List<Area> areas, List<Company> companies, List<User> clients, 
			List<Warehouse> warehouses, List<ClientSalesReportType> clientSalesReportTypes) {
		final SalesReportQueryBean salesReportQuery = new SalesReportQueryBean();
		
		salesReportQuery.setFrom(from);
		salesReportQuery.setTo(to);
		salesReportQuery.setSendMail(true);
		salesReportQuery.setDownloadFile(false);
		
		clientSalesReportTypes = fillListIfNull(clientSalesReportTypes);
		companies = fillListIfNull(companies);
		clients = fillListIfNull(clients);
		warehouses = fillListIfNull(warehouses);
		
		for(Company company : companies) {
			salesReportQuery.setCompanyId((company != null) ? company.getId() : null);
			
			final List<String> attachments = new ArrayList<String>();
			
			for(Area area : areas) {
				salesReportQuery.setArea((area != null) ? area : null);
				
				final List<String> filePaths = new ArrayList<String>();
				
				for(User client : clients) {
					salesReportQuery.setClientId((client != null) ? client.getId() : null);
					
					for(Warehouse warehouse : warehouses) {
						salesReportQuery.setWarehouseId((warehouse != null) ? warehouse.getId() : null);
						
						for(ClientSalesReportType clientSalesReportType : clientSalesReportTypes) {
							setReportType(salesReportQuery, clientSalesReportType);
							
							String fileName= "";
							if(client != null) fileName += client.getBusinessName() + "_";
							if(warehouse != null) fileName += warehouse.getName() + "_";
							if(area != null) fileName += area.getDisplayName() + "_";
							fileName += clientSalesReportType.getDisplayName() + "_Report.pdf";
							
							final ResultBean result = salesReportHandler.generateReport(salesReportQuery, fileName);
							if(result.getSuccess()) {
								filePaths.add(fileConstants.getSalesHome() + (String) result.getExtras().get("fileName"));
							} else {
								LOG.error(fileName + " : " + result.getMessage());
							}
						}
					}
				}
				
				if(filePaths.size() > 3) {
					final String zipFileName = fileConstants.getSalesHome() + company.getShortName() + "_Sales_Report" + ".zip";
					FileZipUtil.zipFile(filePaths, zipFileName);
					attachments.add(zipFileName);
				} else {
					attachments.addAll(filePaths);
				}
			}
			
			String message = "Report for " + company.getName() + "\n" +
									DateFormatter.prettyFormat(salesReportQuery.getFrom()) + 
									" to " +
									DateFormatter.prettyFormat(salesReportQuery.getTo()) + "\n\n";
			if(attachments.isEmpty()) {
				message += "No sales.";
			} else {
				message += "See reports attached";
			}
			
			emailUtil.send(company.getReportReceiver(),
					null,
					userHandler.getEmailOfAllAdminAndManagers() + ", " + businessConstants.getDefaultReportReceiver(),
					company.getShortName() + " Client Sales Report",
					message,
					attachments.isEmpty() ? null : attachments.toArray(new String[0]));
		}
	}
	
	// Defaults report type to status based.
	// Status based is temporarily defaulted to include paid and delivered only.
	private void setReportType(SalesReportQueryBean salesReportQuery, ClientSalesReportType clientSalesReportType) {
		salesReportQuery.setClientSalesReportType((clientSalesReportType == null) ? ClientSalesReportType.STATUS_BASED : clientSalesReportType);
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
	
	/**
	 * Used to convert null list to a list with one null object.
	 * @param objects The list to be converted.
	 * @return New list if objects is null else the same list.
	 */
	private <T> List<T> fillListIfNull(List<T> objects) {
		if(objects == null) {
			objects = new ArrayList<T>();
			objects.add(null);
		}
		return objects;
	}
}
