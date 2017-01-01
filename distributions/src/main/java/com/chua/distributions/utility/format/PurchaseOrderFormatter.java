package com.chua.distributions.utility.format;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.chua.distributions.constants.BusinessConstants;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.utility.StringHelper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 11, 2016
 */
public class PurchaseOrderFormatter {
	
	private static final int CHARACTERS_PER_LINE = 107;
	
	private PurchaseOrderFormatter() {
		
	}
	
	public static String format(PurchaseOrder purchaseOrder, List<PurchaseOrderItem> orderItems) {
		String format = "";
		
		format += StringHelper.center("PURCHASE ORDER", CHARACTERS_PER_LINE) + "\n";
		format += "\n";
		format += String.format("%15s", "SUPPLIER: ") + purchaseOrder.getCompany().getName() + "\n";
		format += String.format("%15s", "DELIVER TO: ") + BusinessConstants.BUSINESS_NAME + "\n";
		format += String.format("%15s", "ADDRESS: ") + purchaseOrder.getWarehouse().getAddress() + "\n";
		format += String.format("%15s", "Date: ") + new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(new Date()) + "\n";
		format += "\n";
		format += " Purchase Order #" + purchaseOrder.getId() + "\n";
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		format += "|" + StringHelper.center("Quantity", 14) + "|";
		format += StringHelper.center("Item Description", 49) + "|";
		format += StringHelper.center("Unit Price", 19) + "|";
		format += StringHelper.center("Amount", 20) + " |" + "\n";
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		for(PurchaseOrderItem orderItem : orderItems) {
			format += "|";
			format += StringHelper.center(orderItem.getFormattedQuantity() + "", 14) + "|";
			format += StringHelper.center(orderItem.getDisplayName(), 49) + "|";
			format += StringHelper.center(orderItem.getFormattedPackageUnitPrice(), 19) + "|";
			format += StringHelper.center(orderItem.getFormattedGrossPrice(), 20);
			format += " |\n";
		}
		format += " "; for(int i = 0; i < CHARACTERS_PER_LINE - 1; i++) format += "-"; format += "\n";
		format += "\n";
		format += String.format("%85s", "Total:") + StringHelper.center(purchaseOrder.getFormattedGrossTotal(), 20) + "\n";
		format += "\n";
		format += String.format("%20s", "Prepared By: ") + purchaseOrder.getCreator().getFormattedName();
		format += "\n";
		format += String.format("%20s", "Approved By: ") + BusinessConstants.BUSINESS_CHIEF_OFFICER;
		
		return format;
	}
}
