package com.chua.distributions.database.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.database.entity.base.BaseObject;
import com.chua.distributions.enums.Status;
import com.chua.distributions.serializer.json.CompanySerializer;
import com.chua.distributions.serializer.json.UserSerializer;
import com.chua.distributions.serializer.json.WarehouseSerializer;
import com.chua.distributions.utility.DateUtil;
import com.chua.distributions.utility.format.CurrencyFormatter;
import com.chua.distributions.utility.format.DateFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
@MappedSuperclass
public class Order extends BaseObject {

	private static final long serialVersionUID = -39416674040943778L;

	@JsonSerialize(using = UserSerializer.class)
	private User creator;
	
	@JsonSerialize(using = CompanySerializer.class)
	private Company company;
	
	private Float grossTotal;
	
	private Float discountTotal;
	
	private Status status;
	
	@JsonSerialize(using = WarehouseSerializer.class)
	private Warehouse warehouse;

	private Date requestedOn;
	
	private Date deliveredOn;
	
	private Date paidOn;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	@ManyToOne(targetEntity = Company.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Basic
	@Column(name = "gross_total")
	public Float getGrossTotal() {
		return grossTotal;
	}
	
	@Transient
	public String getFormattedGrossTotal() {
		return CurrencyFormatter.pesoFormat(grossTotal);
	}

	public void setGrossTotal(Float grossTotal) {
		this.grossTotal = grossTotal;
	}
	
	@Basic
	@Column(name = "discount_total")
	public Float getDiscountTotal() {
		return discountTotal;
	}
	
	@Transient
	public String getFormattedDiscountTotal() {
		return CurrencyFormatter.pesoFormat(discountTotal);
	}
	
	@Transient
	public Float getNetTotal() {
		return getGrossTotal() - getDiscountTotal();
	}
	
	@Transient
	public String getFormattedNetTotal() {
		return CurrencyFormatter.pesoFormat(getNetTotal());
	}

	public void setDiscountTotal(Float discountTotal) {
		this.discountTotal = discountTotal;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 50)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@ManyToOne(targetEntity = Warehouse.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouse_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "requested_on")
	public Date getRequestedOn() {
		return requestedOn;
	}
	
	@Transient
	public String getFormattedRequestedOn() {
		final String formattedRequestedOn;
		Calendar cal = Calendar.getInstance();
		cal.setTime(requestedOn);
		
		if(cal.getTimeInMillis() == DateUtil.getDefaultDateInMillis()) {
			formattedRequestedOn = "n/a";
		} else {
			formattedRequestedOn = DateFormatter.shortFormat(requestedOn);
		}
		return formattedRequestedOn;
	}

	public void setRequestedOn(Date requestedOn) {
		this.requestedOn = requestedOn;
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

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "paid_on")
	public Date getPaidOn() {
		return paidOn;
	}
	
	@Transient
	public String getFormattedPaidOn() {
		final String formattedPaidOn;
		Calendar cal = Calendar.getInstance();
		cal.setTime(paidOn);
		
		if(cal.getTimeInMillis() == DateUtil.getDefaultDateInMillis()) {
			formattedPaidOn = "n/a";
		} else {
			formattedPaidOn = DateFormatter.shortFormat(paidOn);
		}
		return formattedPaidOn;
	}

	public void setPaidOn(Date paidOn) {
		this.paidOn = paidOn;
	}
}
