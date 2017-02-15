package com.chua.distributions.utility.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.constants.BusinessConstants;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.utility.format.DateFormatter;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public class ClientOrderTemplate implements Template {

	private ClientOrder clientOrder;
	
	private List<ClientOrderItem> orderItems;
	
	private List<String> formattedOrderItems;
	
	private String netTrail;
	
	public ClientOrderTemplate(ClientOrder clientOrder, List<ClientOrderItem> orderItems) {
		this.clientOrder = clientOrder;
		this.orderItems = orderItems;
		this.formattedOrderItems = new ArrayList<String>();
		this.netTrail = "";
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		for(ClientOrderItem orderItem : orderItems) {
			final ClientOrderItemTemplate orderItemTemplate = new ClientOrderItemTemplate(orderItem);
			formattedOrderItems.add(orderItemTemplate.merge(velocityEngine));
		}
		
		final NetTrailTemplate netTrailTemplate = new NetTrailTemplate(clientOrder);
		netTrail = netTrailTemplate.merge(velocityEngine);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/clientOrder.vm", "UTF-8", model);
	}

	public String getDistributor() {
		return BusinessConstants.BUSINESS_NAME;
	}
	
	public String getClient() {
		return clientOrder.getClient().getBusinessName();
	}
	
	public String getAddress() {
		return clientOrder.getClient().getBusinessAddress();
	}
	
	public String getContactNumber() {
		return clientOrder.getClient().getBusinessContactNumber();
	}
	
	public String getDate() {
		return DateFormatter.longFormat(new Date());
	}
	
	public Long getOrderId() {
		return clientOrder.getId();
	}
	
	public List<String> getFormattedOrderItems() {
		return formattedOrderItems;
	}
	
	public String getNetTrail() {
		return netTrail;
	}
}
