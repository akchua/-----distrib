package com.chua.distributions.utility.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.beans.ClientRankQueryBean;
import com.chua.distributions.beans.ClientStatisticsBean;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.ClientRankType;
import com.chua.distributions.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Apr 21, 2018
 */
public class ClientRankTemplate implements Template {

	private ClientRankQueryBean clientRankQuery;
	
	private Area area;
	
	private List<ClientStatisticsBean> clientStats;
	
	private List<String> summarizedClientStats;
	
	public ClientRankTemplate(ClientRankQueryBean clientRankQuery, List<ClientStatisticsBean> clientStats) {
		this.clientRankQuery = clientRankQuery;
		this.area = clientRankQuery.getArea();
		this.clientStats = clientStats;
		this.summarizedClientStats = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		for(ClientStatisticsBean clientStat : clientStats) {
			final ClientStatTemplate clientStatTemplate = new ClientStatTemplate(clientStat);
			summarizedClientStats.add(clientStatTemplate.merge(velocityEngine));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/clientRank.vm", "UTF-8", model);
	}
	
	public String getMonthFrom() {
		return DateFormatter.monthFormat(clientRankQuery.getMonthFrom());
	}
	
	public String getMonthTo() {
		return DateFormatter.monthFormat(clientRankQuery.getMonthTo());
	}
	
	public String getArea() {
		final String area;
		if(this.area != null) {
			area = this.area.getDisplayName();
		} else {
			area = "ALL AREAS";
		}
		return area;
	}
	
	public String getClientRankType() {
		final String clientRankType;
		
		if(this.clientRankQuery.getClientRankType() != null) {
			clientRankType = this.clientRankQuery.getClientRankType().getDisplayName();
		} else {
			clientRankType = ClientRankType.TOP_DELIVERED.getDisplayName();
		}
		
		return clientRankType;
	}
	
	public String getDate() {
		return DateFormatter.longFormat(new Date());
	}
	
	public List<String> getSummarizedClientStats() {
		return summarizedClientStats;
	}
}
