package com.chua.distributions.utility.format;

import java.util.Date;
import java.util.List;

import com.chua.distributions.constants.BusinessConstants;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.utility.StringHelper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
public class ClientOrderFormatter {
	
	private final int CHARACTERS_PER_LINE = 115;
	
	public ClientOrderFormatter() {
		
	}
	
	public String format(ClientOrder clientOrder, List<ClientOrderItem> orderItems) {
		String format = "";
		
		format += StringHelper.center("SALES ORDER", CHARACTERS_PER_LINE) + "\n";
		format += "\n";
		format += String.format("%20s", "DISTRIBUTOR: ") + BusinessConstants.BUSINESS_NAME + "\n";
		format += String.format("%20s", "DELIVER TO: ") + clientOrder.getCreator().getBusinessName() + "\n";
		format += String.format("%20s", "ADDRESS: ") + clientOrder.getCreator().getBusinessAddress() + "\n";
		format += String.format("%20s", "CONTACT #: ") + clientOrder.getCreator().getBusinessContactNumber() + "\n";
		format += String.format("%20s", "Date: ") + DateFormatter.longFormat(new Date()) + "\n";
		format += "\n";
		format += " Sales Order #" + clientOrder.getId() + "\n";
		format += "+"; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "+\n";
		format += "|" + StringHelper.center("Quantity", 14) + "|";
		format += StringHelper.center("Item Description", 49) + "| ";
		format += StringHelper.center("Unit Price", 12) + " | ";
		format += StringHelper.center("Discount", 17) + " | ";
		format += StringHelper.center("Amount", 12) + " |" + "\n";
		format += "+"; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "+\n";
		for(ClientOrderItem orderItem : orderItems) {
			format += "|";
			format += StringHelper.center(orderItem.getFormattedQuantity() + "", 14) + "|";
			format += StringHelper.center(orderItem.getDisplayName(), 49) + "| ";
			format += String.format("%12s", orderItem.getFormattedPackageUnitPrice()) + " | ";
			format += String.format("%17s", orderItem.getFormattedDiscountAmount()) + " | ";
			format += String.format("%12s", orderItem.getFormattedNetPrice());
			format += " |\n";
		}
		format += "+"; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "+\n";
		format += "\n";
		format += String.format("%95s", "Gross Total: ") + String.format("%-20s", clientOrder.getFormattedGrossTotal()) + "\n";
		format += String.format("%95s", "Total Discount: ") + String.format("%-20s", clientOrder.getFormattedDiscountTotal()) + "\n";
		format += String.format("%95s", "Client Discount: ") + String.format("%-20s", clientOrder.getFormattedAdditionalDiscountAmount()) + "\n";
		format += String.format("%95s", "Less VAT: ") + String.format("%-20s", clientOrder.getFormattedLessVatAmount()) + "\n";
		format += String.format("%95s", "Net Total: ") + String.format("%-20s", clientOrder.getFormattedNetTotal()) + "\n";
		
		return format;
	}
}
