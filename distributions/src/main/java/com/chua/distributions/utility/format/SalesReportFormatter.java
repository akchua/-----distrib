package com.chua.distributions.utility.format;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.enums.Status;
import com.chua.distributions.utility.StringHelper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 30, 2016
 */
public class SalesReportFormatter {
	
	private static final int CHARACTERS_PER_LINE = 127;
	
	private SalesReportFormatter() {
		
	}
	
	public static String format(SalesReportQueryBean salesReportQuery, User client, List<ClientOrder> clientOrders) {
		String format = "";
		float tempTotal = 0.0f;
		
		format += StringHelper.center("SALES REPORT", CHARACTERS_PER_LINE) + "\n";
		format += "\n";
		format += String.format("%20s", "Date From: ") + DateFormatter.longFormat(salesReportQuery.getFrom()) + "\n";
		format += String.format("%20s", "Date To: ") + DateFormatter.longFormat(salesReportQuery.getTo()) + "\n";
		if(salesReportQuery.getWarehouse() != null) format += String.format("%20s", "Warehouse: ") + salesReportQuery.getWarehouse().getDisplayName() + "\n";
		else format += String.format("%20s", "Warehouse: ") + "ALL WAREHOUSES" + "\n";
		if(client != null) format += String.format("%20s", "Client: ") + client.getFormattedName() + "\n";
		else format += String.format("%20s", "Client: ") + "ALL CLIENTS" + "\n";
		format += String.format("%20s", "Report Date: ") + DateFormatter.longFormat(new Date()) + "\n";
		format += "\n";
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		format += "|" + StringHelper.center("Status", 19) + "|";
		
		// CHECK IF ONLY 1 STATUS IS SELECTED
		List<Status> includedStatus = getIncludedStatus(salesReportQuery);
		if(includedStatus.size() == 1) format += StringHelper.center(includedStatus.get(0).getDisplayName() + " On", 19) + "|";
		else format += StringHelper.center("Last Status Change", 19) + "|";
		//
		
		format += StringHelper.center("Order ID", 9) + "|";
		format += StringHelper.center("Client", 39) + "|";
		format += StringHelper.center("Warehouse", 14) + "|";
		format += StringHelper.center("Amount", 20) + " |" + "\n";
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		for(ClientOrder clientOrder : clientOrders) {
			format += "|";
			format += StringHelper.center(clientOrder.getStatus().getDisplayName(), 19) + "|";
			format += StringHelper.center(DateFormatter.longFormat(clientOrder.getUpdatedOn()), 19) + "|";
			format += StringHelper.center(clientOrder.getId() + "", 9) + "|";
			format += StringHelper.center(clientOrder.getCreator().getBusinessName(), 39) + "|";
			format += StringHelper.center((clientOrder.getWarehouse() != null) ? clientOrder.getWarehouse().getDisplayName() : "", 14) + "|";
			format += String.format("%20s", clientOrder.getFormattedNetTotal());
			format += " |\n";
			if(salesReportQuery.getShowNetTrail() != null && salesReportQuery.getShowNetTrail()) {
				format += "|"; format += String.format("%105s", "Gross Total: ") + String.format("%20s", clientOrder.getFormattedGrossTotal()) + " |\n";
				format += "|"; format += String.format("%105s", "Total Discount: ") + String.format("%20s", clientOrder.getFormattedDiscountTotal()) + " |\n";
				format += "|"; format += String.format("%105s", "Client Discount: ") + String.format("%20s", clientOrder.getFormattedAdditionalDiscountAmount()) + " |\n";
				format += "|"; format += String.format("%105s", "Less VAT: ") + String.format("%20s", clientOrder.getFormattedLessVatAmount()) + " |\n";
				format += "|"; format += String.format("%105s", "Net Total: ") + String.format("%20s", clientOrder.getFormattedNetTotal()) + " |\n";
			}
			tempTotal += clientOrder.getNetTotal();
		}
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		format += "\n";
		format += String.format("%90s", "(" + DateFormatter.longFormat(salesReportQuery.getFrom()) + " - " + DateFormatter.longFormat(salesReportQuery.getTo()) + ") Total Sales: ") + CurrencyFormatter.pesoFormat(tempTotal) + "\n";
		
		return format;
	}
	
	private static List<Status> getIncludedStatus(SalesReportQueryBean salesReportQueryBean) {
		final List<Status> includedStatus = new ArrayList<Status>();
		
		if(salesReportQueryBean.getIncludePaid()) includedStatus.add(Status.PAID);
		if(salesReportQueryBean.getIncludeDelivered()) includedStatus.add(Status.RECEIVED);
		if(salesReportQueryBean.getIncludeDispatched()) includedStatus.add(Status.DISPATCHED);
		if(salesReportQueryBean.getIncludeAccepted()) includedStatus.add(Status.ACCEPTED);
		if(salesReportQueryBean.getIncludeSubmitted()) includedStatus.add(Status.SUBMITTED);
		if(salesReportQueryBean.getIncludeCreating()) includedStatus.add(Status.CREATING);
		
		return includedStatus;
	}
}
