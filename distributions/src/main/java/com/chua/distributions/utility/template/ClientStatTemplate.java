package com.chua.distributions.utility.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.beans.ClientStatisticsBean;
import com.chua.distributions.utility.StringHelper;
import com.chua.distributions.utility.format.CurrencyFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Apr 21, 2018
 */
public class ClientStatTemplate implements Template {

	private ClientStatisticsBean clientStat;
	
	public ClientStatTemplate(ClientStatisticsBean clientStat) {
		this.clientStat = clientStat;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/clientStat.vm", "UTF-8", model);
	}
	
	public String getClientName() {
		return StringHelper.center(clientStat.getClientName(), 39);
	}
	
	public String getFormattedPurchaseAmount() {
		return " " + String.format("%14s", CurrencyFormatter.pesoFormat(clientStat.getPurchaseAmount())) + " ";
	}
}
