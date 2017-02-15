package com.chua.distributions.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.database.entity.ClientOrder;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public class NetTrailTemplate implements Template {

	private ClientOrder clientOrder;
	
	public NetTrailTemplate(ClientOrder clientOrder) {
		this.clientOrder = clientOrder;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/netTrail.vm", "UTF-8", model);
	}

	public String getFormattedGrossTotal() {
		return String.format("%-20s", clientOrder.getFormattedGrossTotal());
	}
	
	public String getFormattedDiscountTotal() {
		return String.format("%-20s", clientOrder.getFormattedDiscountTotal());
	}
	
	public String getFormattedAdditionalDiscountAmount() {
		return String.format("%-20s", clientOrder.getFormattedAdditionalDiscountAmount());
	}
	
	public String getFormattedLessVatAmount() {
		return String.format("%-20s", clientOrder.getFormattedLessVatAmount());
	}
	
	public String getFormattedNetTotal() {
		return String.format("%-20s", clientOrder.getFormattedNetTotal());
	}
}
