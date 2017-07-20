package com.chua.distributions.database.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.chua.distributions.database.entity.base.BaseObject;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.utility.DateUtil;
import com.chua.distributions.utility.format.CurrencyFormatter;
import com.chua.distributions.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
@Entity(name = "Dispatch")
@Table(name = Dispatch.TABLE_NAME)
public class Dispatch extends BaseObject {

	private static final long serialVersionUID = -7282402017538952116L;

	public static final String TABLE_NAME = "dispatch";
	
	private Warehouse warehouse;
	
	private Integer orderCount;
	
	private Float totalAmount;
	
	private Status status;
	
	private Date deliveredOn;

	@Enumerated(EnumType.STRING)
	@Column(name = "warehouse", length = 50)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@Basic
	@Column(name = "order_count")
	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	@Basic
	@Column(name = "total_amount")
	public Float getTotalAmount() {
		return totalAmount;
	}
	
	@Transient
	public String getFormattedTotalAmount() {
		return CurrencyFormatter.pesoFormat(totalAmount);
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 50)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "delivered_on")
	public Date getDeliveredOn() {
		return deliveredOn;
	}
	
	@Transient
	public String getFormattedDeliveredOn() {
		final String formattedDeliveredOn;
		Calendar cal = Calendar.getInstance();
		cal.setTime(deliveredOn);
		
		if(cal.getTimeInMillis() == DateUtil.getDefaultDateInMillis()) {
			formattedDeliveredOn = "n/a";
		} else {
			formattedDeliveredOn = DateFormatter.shortFormat(deliveredOn);
		}
		return formattedDeliveredOn;
	}

	public void setDeliveredOn(Date deliveredOn) {
		this.deliveredOn = deliveredOn;
	}
}
