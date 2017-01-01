package com.chua.distributions.beans;

import java.util.Calendar;
import java.util.Date;

import com.chua.distributions.enums.Warehouse;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 30, 2016
 */
public class SalesReportQueryBean {

	private Date from;
	
	private Date to;
	
	private Warehouse warehouse;
	
	private Long clientId;
	
	private Boolean paidOnly;
	
	private Boolean showNetTrail;
	
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(to);
		calendar.add(Calendar.HOUR_OF_DAY, 23);
		calendar.add(Calendar.MINUTE, 59);
		calendar.add(Calendar.SECOND, 59);
		this.to= calendar.getTime();
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Boolean getPaidOnly() {
		return paidOnly;
	}

	public void setPaidOnly(Boolean paidOnly) {
		this.paidOnly = paidOnly;
	}

	public Boolean getShowNetTrail() {
		return showNetTrail;
	}

	public void setShowNetTrail(Boolean showNetTrail) {
		this.showNetTrail = showNetTrail;
	}
}
