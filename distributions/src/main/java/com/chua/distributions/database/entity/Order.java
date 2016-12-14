package com.chua.distributions.database.entity;

import java.text.DecimalFormat;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.database.entity.base.BaseObject;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.serializer.json.UserSerializer;
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
	
	private Float grossTotal;
	
	private Float discountTotal;
	
	private Status status;
	
	private Warehouse warehouse;
	
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

	@Basic
	@Column(name = "gross_total")
	public Float getGrossTotal() {
		return grossTotal;
	}
	
	@Transient
	public String getFormattedGrossTotal() {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return "Php " + df.format(grossTotal);
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
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return "Php " + df.format(discountTotal);
	}
	
	@Transient
	public Float getNetTotal() {
		return getGrossTotal() - getDiscountTotal();
	}
	
	@Transient
	public String getFormattedNetTotal() {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return "Php " + df.format(getNetTotal());
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
	
	@Enumerated(EnumType.STRING)
	@Column(name = "warehouse", length = 50)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
}
