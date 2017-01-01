package com.chua.distributions.utility.format;

import java.text.SimpleDateFormat;
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
	
	private static final int CHARACTERS_PER_LINE = 127;
	
	private ClientOrderFormatter() {
		
	}
	
	public static String format(ClientOrder clientOrder, List<ClientOrderItem> orderItems) {
		String format = "";
		
		format += StringHelper.center("SALES ORDER", CHARACTERS_PER_LINE) + "\n";
		format += "\n";
		format += String.format("%20s", "DISTRIBUTOR: ") + BusinessConstants.BUSINESS_NAME + "\n";
		format += String.format("%20s", "DELIVER TO: ") + clientOrder.getCreator().getBusinessName() + "\n";
		format += String.format("%20s", "ADDRESS: ") + clientOrder.getCreator().getBusinessAddress() + "\n";
		format += String.format("%20s", "CONTACT #: ") + clientOrder.getCreator().getBusinessContactNumber() + "\n";
		format += String.format("%20s", "Date: ") + new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(new Date()) + "\n";
		format += "\n";
		format += " Sales Order #" + clientOrder.getId() + "\n";
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		format += "|" + StringHelper.center("Quantity", 14) + "|";
		format += StringHelper.center("Item Description", 49) + "|";
		format += StringHelper.center("Unit Price", 19) + "|";
		format += StringHelper.center("Discount", 19) + "|";
		format += StringHelper.center("Amount", 20) + " |" + "\n";
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		for(ClientOrderItem orderItem : orderItems) {
			format += "|";
			format += StringHelper.center(orderItem.getFormattedQuantity() + "", 14) + "|";
			format += StringHelper.center(orderItem.getDisplayName(), 49) + "|";
			format += StringHelper.center(orderItem.getFormattedPackageUnitPrice(), 19) + "|";
			format += StringHelper.center(orderItem.getFormattedDiscountAmount(), 19) + "|";
			format += StringHelper.center(orderItem.getFormattedNetPrice(), 20);
			format += " |\n";
		}
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		format += "\n";
		format += String.format("%105s", "Gross Total: ") + clientOrder.getFormattedGrossTotal() + "\n";
		format += String.format("%105s", "Total Discount: ") + clientOrder.getFormattedDiscountTotal() + "\n";
		format += String.format("%105s", "Client Discount: ") + clientOrder.getFormattedAdditionalDiscountAmount() + "\n";
		format += String.format("%105s", "Less VAT: ") + clientOrder.getFormattedLessVatAmount() + "\n";
		format += String.format("%105s", "Net Total: ") + clientOrder.getFormattedNetTotal() + "\n";
		
		return format;
	}
}
