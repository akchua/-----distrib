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
import com.chua.distributions.utility.format.DateFormatter;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public class ClientOrderSummaryTemplate implements Template {

	private ClientOrder clientOrder;
	
	private Boolean showNetTrail;
	
	private List<String> netTrail;
	
	public ClientOrderSummaryTemplate(ClientOrder clientOrder, Boolean showNetTrail) {
		this.clientOrder = clientOrder;
		this.showNetTrail = showNetTrail;
		this.netTrail = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		final NetTrailTemplate netTrailTemplate = new NetTrailTemplate(clientOrder);
		netTrail = Arrays.asList(netTrailTemplate.merge(velocityEngine).split("\\R"));

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/clientOrderSummary.vm", "UTF-8", model);
	}
	
	public String getFormattedStatus() {
		return StringHelper.center(clientOrder.getStatus().getDisplayName(), 19);
	}
	
	public String getFormattedUpdatedOn() {
		return StringHelper.center(DateFormatter.longFormat(clientOrder.getUpdatedOn()), 19);
	}
	
	public String getFormattedOrderId() {
		return StringHelper.center(clientOrder.getId() + "", 9);
	}
	
	public String getFormattedClient() {
		return StringHelper.center(clientOrder.getClient().getBusinessName(), 39);
	}
	
	public String getFormattedWarehouse() {
		return StringHelper.center((clientOrder.getWarehouse() != null) ? clientOrder.getWarehouse().getDisplayName() : "", 14);
	}
	
	public String getFormattedNetTotal() {
		return " " + String.format("%14s", clientOrder.getFormattedNetTotal()) + " ";
	}
	
	public Boolean getShowNetTrail() {
		return showNetTrail;
	}
	
	public List<String> getNetTrail() {
		return netTrail;
	}
}
