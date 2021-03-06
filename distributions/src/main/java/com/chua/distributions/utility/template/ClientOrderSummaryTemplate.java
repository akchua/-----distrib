package com.chua.distributions.utility.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.utility.StringHelper;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public class ClientOrderSummaryTemplate implements Template {

	private ClientOrder clientOrder;
	
	private List<String> timestamp;
	
	private List<String> netTrail;
	
	public ClientOrderSummaryTemplate(ClientOrder clientOrder) {
		this.clientOrder = clientOrder;
		this.timestamp = new ArrayList<String>();
		this.netTrail = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		final ClientOrderNetTrailTemplate netTrailTemplate = new ClientOrderNetTrailTemplate(clientOrder);
		netTrail = Arrays.asList(netTrailTemplate.merge(velocityEngine).split("\\R"));
		
		final ClientOrderTimestampTemplate timestampTemplate = new ClientOrderTimestampTemplate(clientOrder);
		timestamp = Arrays.asList(timestampTemplate.merge(velocityEngine).split("\\R"));
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/clientOrderSummary.vm", "UTF-8", model);
	}
	
	public String getFormattedStatus() {
		return StringHelper.center(clientOrder.getStatus().getDisplayName(), 19);
	}
	
	public String getFormattedOrderId() {
		return StringHelper.center(clientOrder.getId() + "", 9);
	}
	
	public String getFormattedClient() {
		return StringHelper.center(clientOrder.getClient().getBusinessName(), 39);
	}
	
	public String getFormattedCompany() {
		return StringHelper.center(clientOrder.getCompany().getName(), 29);
	}
	
	public String getFormattedWarehouse() {
		return StringHelper.center((clientOrder.getWarehouse() != null) ? clientOrder.getWarehouse().getName() : "", 14);
	}
	
	public String getFormattedNetTotal() {
		return " " + String.format("%14s", clientOrder.getFormattedNetTotal()) + " ";
	}
	
	public List<String> getTimestamp() {
		return timestamp;
	}
	
	public List<String> getNetTrail() {
		return netTrail;
	}
}
