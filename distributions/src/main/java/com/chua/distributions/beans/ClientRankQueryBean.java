package com.chua.distributions.beans;

import java.util.Date;

import com.chua.distributions.deserializer.json.MonthDeserializer;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.ClientRankType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Apr 20, 2018
 */
public class ClientRankQueryBean {

	@JsonDeserialize(using = MonthDeserializer.class)
	private Date monthFrom;
	
	@JsonDeserialize(using = MonthDeserializer.class)
	private Date monthTo;
	
	private Area area;
	
	private Long companyId;
	
	private ClientRankType clientRankType;
	
	private Boolean sendMail;
	
	private Boolean downloadFile;

	public Date getMonthFrom() {
		return monthFrom;
	}

	public void setMonthFrom(Date monthFrom) {
		this.monthFrom = monthFrom;
	}

	public Date getMonthTo() {
		return monthTo;
	}

	public void setMonthTo(Date monthTo) {
		this.monthTo = monthTo;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public ClientRankType getClientRankType() {
		return clientRankType;
	}

	public void setClientRankType(ClientRankType clientRankType) {
		this.clientRankType = clientRankType;
	}

	public Boolean getSendMail() {
		return sendMail;
	}

	public void setSendMail(Boolean sendMail) {
		this.sendMail = sendMail;
	}

	public Boolean getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(Boolean downloadFile) {
		this.downloadFile = downloadFile;
	}
}
