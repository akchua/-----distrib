package com.chua.distributions.database.entity;

import java.text.DecimalFormat;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.database.entity.base.BaseObject;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.Status;
import com.chua.distributions.serializer.json.CompanySerializer;
import com.chua.distributions.serializer.json.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
@Entity(name = "PurchaseOrder")
@Table(name = PurchaseOrder.TABLE_NAME)
public class PurchaseOrder extends BaseObject {

	private static final long serialVersionUID = 5443831736325937649L;
	
	public static final String TABLE_NAME = "purchase_order";
	
	private Area area;
	
	@JsonSerialize(using = CompanySerializer.class)
	private Company company;
	
	@JsonSerialize(using = UserSerializer.class)
	private User creator;
	
	private Float grossTotal;
	
	private Float discountTotal;
	
	private Float netTotal;
	
	private Status status;

	@Enumerated(EnumType.STRING)
	@Column(name = "area", length = 50)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
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

	public void setDiscountTotal(Float discountTotal) {
		this.discountTotal = discountTotal;
	}
	
	@Basic
	@Column(name = "net_total")
	public Float getNetTotal() {
		return netTotal;
	}
	
	@Transient
	public String getFormattedNetTotal() {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return "Php " + df.format(netTotal);
	}

	public void setNetTotal(Float netTotal) {
		this.netTotal = netTotal;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 50)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
