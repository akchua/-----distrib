package com.chua.distributions.utility.format;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.enums.Warehouse;
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
	
	public static String format(Date from, Date to, User client, Warehouse warehouse, Boolean paidOnly, Boolean showNetTrail, List<ClientOrder> clientOrders) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		DecimalFormat df = new DecimalFormat("#,##0.00");
		
		String format = "";
		float tempTotal = 0.0f;
		
		format += StringHelper.center("SALES REPORT", CHARACTERS_PER_LINE) + "\n";
		format += "\n";
		format += String.format("%20s", "Date From: ") + dateFormat.format(from) + "\n";
		format += String.format("%20s", "Date To: ") + dateFormat.format(to) + "\n";
		if(warehouse != null) format += String.format("%20s", "Warehouse: ") + warehouse.getDisplayName() + "\n";
		else format += String.format("%20s", "Warehouse: ") + "ALL WAREHOUSES" + "\n";
		if(client != null) format += String.format("%20s", "Client: ") + client.getFormattedName() + "\n";
		else format += String.format("%20s", "Client: ") + "ALL CLIENTS" + "\n";
		format += String.format("%20s", "Report Date: ") + dateFormat.format(new Date()) + "\n";
		format += "\n";
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		format += "|" + StringHelper.center("Created On", 19) + "|";
		if(paidOnly) format += StringHelper.center("Paid On", 19) + "|";
		else format += StringHelper.center("Last Status Change", 19) + "|";
		format += StringHelper.center("Order ID", 9) + "|";
		format += StringHelper.center("Client", 39) + "|";
		format += StringHelper.center("Warehouse", 14) + "|";
		format += StringHelper.center("Amount", 20) + " |" + "\n";
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		for(ClientOrder clientOrder : clientOrders) {
			format += "|";
			format += StringHelper.center(dateFormat.format(clientOrder.getCreatedOn()) + "", 19) + "|";
			format += StringHelper.center(dateFormat.format(clientOrder.getUpdatedOn()), 19) + "|";
			format += StringHelper.center(clientOrder.getId() + "", 19) + "|";
			format += StringHelper.center(clientOrder.getCreator().getFormattedName(), 39) + "|";
			format += StringHelper.center(clientOrder.getWarehouse().getDisplayName(), 14) + "|";
			format += StringHelper.center(clientOrder.getFormattedNetTotal(), 20);
			format += " |\n";
			if(showNetTrail) {
				format += "|"; format += String.format("%95s", "Gross Total: ") + String.format("%30s", clientOrder.getFormattedGrossTotal()) + "|\n";
				format += "|"; format += String.format("%95s", "Total Discount: ") + String.format("%30s", clientOrder.getFormattedDiscountTotal()) + "|\n";
				format += "|"; format += String.format("%95s", "Client Discount: ") + String.format("%30s", clientOrder.getFormattedAdditionalDiscountAmount()) + "|\n";
				format += "|"; format += String.format("%95s", "Less VAT: ") + String.format("%30s", clientOrder.getFormattedLessVatAmount()) + "|\n";
				format += "|"; format += String.format("%95s", "Net Total: ") + String.format("%30s", clientOrder.getFormattedNetTotal()) + "|\n";
			}
			tempTotal += clientOrder.getNetTotal();
		}
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		format += "\n";
		format += String.format("%90s", "(" + dateFormat.format(from) + " - " + dateFormat.format(to) + ") Total Sales: ") + df.format(tempTotal) + "\n";
		
		return format;
	}
}
