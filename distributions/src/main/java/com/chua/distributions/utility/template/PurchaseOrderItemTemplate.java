package com.chua.distributions.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.utility.StringHelper;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public class PurchaseOrderItemTemplate implements Template {

	private PurchaseOrderItem purchaseOrderItem;
	
	public PurchaseOrderItemTemplate(PurchaseOrderItem purchaseOrderItem) {
		this.purchaseOrderItem = purchaseOrderItem;
	}
	
	public String merge(VelocityEngine velocityEngine) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "purchaseOrderItem.vm", "UTF-8", model);
	}
	
	public String getFormattedQuantity() {
		return StringHelper.center(purchaseOrderItem.getFormattedQuantity(), 14);
	}
	
	public String getFormattedDescription() {
		return StringHelper.center(purchaseOrderItem.getDisplayName(), 49);
	}
	
	public String getFormattedPackageUnitPrice() {
		return " " + String.format("%12s", purchaseOrderItem.getFormattedPackageUnitPrice()) + " ";
	}
	
	public String getFormattedGrossPrice() {
		return " " + String.format("%12s", purchaseOrderItem.getFormattedGrossPrice()) + " ";
	}
}
