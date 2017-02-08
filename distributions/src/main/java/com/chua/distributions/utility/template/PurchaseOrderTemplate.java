package com.chua.distributions.utility.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.constants.BusinessConstants;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.utility.format.DateFormatter;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public class PurchaseOrderTemplate implements Template {

	private PurchaseOrder purchaseOrder;
	
	private List<PurchaseOrderItem> orderItems;
	
	private List<String> formattedOrderItems;
	
	public PurchaseOrderTemplate(PurchaseOrder purchaseOrder, List<PurchaseOrderItem> orderItems) {
		this.purchaseOrder = purchaseOrder;
		this.orderItems = orderItems;
		this.formattedOrderItems = new ArrayList<String>();
	}
	
	public String merge(VelocityEngine velocityEngine) {
		for(PurchaseOrderItem orderItem : orderItems) {
			final PurchaseOrderItemTemplate orderItemTemplate = new PurchaseOrderItemTemplate(orderItem);
			formattedOrderItems.add(orderItemTemplate.merge(velocityEngine));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "purchaseOrder.vm", "UTF-8", model);
	}
	
	public String getSupplier() {
		return purchaseOrder.getCompany().getName();
	}
	
	public String getBusinessName() {
		return BusinessConstants.BUSINESS_NAME;
	}
	
	public String getAddress() {
		return purchaseOrder.getWarehouse().getAddress();
	}
	
	public String getDate() {
		return DateFormatter.longFormat(new Date());
	}
	
	public Long getOrderId() {
		return purchaseOrder.getId();
	}
	
	public List<String> getFormattedOrderItems() {
		return formattedOrderItems;
	}
	
	public String getGrossTotal() {
		return purchaseOrder.getFormattedGrossTotal();
	}
	
	public String getCreator() {
		return purchaseOrder.getCreator().getFormattedName();
	}
	
	public String getApprover() {
		return BusinessConstants.BUSINESS_CHIEF_OFFICER;
	}
}
