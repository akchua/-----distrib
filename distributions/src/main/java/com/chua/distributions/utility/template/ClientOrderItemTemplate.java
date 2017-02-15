package com.chua.distributions.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.utility.StringHelper;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public class ClientOrderItemTemplate implements Template {

	private ClientOrderItem clientOrderItem;
	
	public ClientOrderItemTemplate(ClientOrderItem clientOrderItem) {
		this.clientOrderItem = clientOrderItem;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/clientOrderItem.vm", "UTF-8", model);
	}

	public String getFormattedQuantity() {
		return StringHelper.center(clientOrderItem.getFormattedQuantity() + "", 14);
	}
	
	public String getFormattedDisplayName() {
		return StringHelper.center(clientOrderItem.getDisplayName(), 49);
	}
	
	public String getFormattedPackageUnitPrice() {
		return " " + String.format("%12s", clientOrderItem.getFormattedPackageUnitPrice()) + " ";
	}
	
	public String getFormattedDiscountAmount() {
		return " " + String.format("%17s", clientOrderItem.getFormattedDiscountAmount()) + " ";
	}
	
	public String getFormattedNetPrice() {
		return " " + String.format("%12s", clientOrderItem.getFormattedNetPrice()) + " ";
	}
}
