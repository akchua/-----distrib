package com.chua.distributions.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.database.entity.ClientOrder;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 4, 2017
 */
public class ClientOrderTimestampTemplate implements Template {

	private ClientOrder clientOrder;
	
	public ClientOrderTimestampTemplate(ClientOrder clientOrder) {
		this.clientOrder = clientOrder;
	}

	@Override
	public String merge(VelocityEngine velocityEngine) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/clientOrderTimestamp.vm", "UTF-8", model);
	}
		
	public String getFormattedCreatedOn() {
		return String.format("%-10s", clientOrder.getFormattedCreatedOn());
	}
	
	public String getFormattedRequestedOn() {
		return String.format("%-10s", clientOrder.getFormattedRequestedOn());
	}
	
	public String getFormattedDeliveredOn() {
		return String.format("%-10s", clientOrder.getFormattedDeliveredOn());
	}
	
	public String getFormattedPaidOn() {
		return String.format("%-10s", clientOrder.getFormattedPaidOn());
	}
}
