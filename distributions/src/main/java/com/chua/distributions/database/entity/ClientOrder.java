package com.chua.distributions.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.serializer.json.UserSerializer;
import com.chua.distributions.utility.format.CurrencyFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
@Entity(name = "ClientOrder")
@Table(name = ClientOrder.TABLE_NAME)
public class ClientOrder extends Order {

	private static final long serialVersionUID = -5813966907202296099L;

	public static final String TABLE_NAME = "client_order";
	
	@JsonSerialize(using = UserSerializer.class)
	private User client;
	
	private Float additionalDiscount;
	
	private Float lessVat;
	
	@JsonSerialize(using = UserSerializer.class)
	private User dispatcher;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getClient() {
		return client;
	}

	public void setClient(User client) {
		this.client = client;
	}
	
	@Basic
	@Column(name = "additional_discount")
	public Float getAdditionalDiscount() {
		return additionalDiscount;
	}
	
	@Transient
	public Float getAdditionalDiscountAmount() {
		return super.getNetTotal() * additionalDiscount / 100.0f;
	}
	
	@Transient
	public String getFormattedAdditionalDiscountAmount() {
		return CurrencyFormatter.pesoFormat(getAdditionalDiscountAmount());
	}

	@Basic
	@Column(name = "less_vat")
	public Float getLessVat() {
		return lessVat;
	}
	
	@Transient
	public Float getLessVatAmount() {
		return (super.getNetTotal() - getAdditionalDiscountAmount()) * lessVat / (100.0f + lessVat);
	}
	
	@Transient
	public String getFormattedLessVatAmount() {
		return CurrencyFormatter.pesoFormat(getLessVatAmount());
	}

	public void setLessVat(Float lessVat) {
		this.lessVat = lessVat;
	}

	@Transient
	@Override
	public Float getNetTotal() {
		return super.getNetTotal() - getAdditionalDiscountAmount() - getLessVatAmount();
	}
	
	@Transient
	@Override
	public String getFormattedNetTotal() {
		return CurrencyFormatter.pesoFormat(this.getNetTotal());
	}

	public void setAdditionalDiscount(Float additionalDiscount) {
		this.additionalDiscount = additionalDiscount;
	}

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "dispatcher_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(User dispatcher) {
		this.dispatcher = dispatcher;
	}
}
