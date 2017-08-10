package com.chua.distributions.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.serializer.json.ClientOrderSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
@Entity(name = "ClientOrderItem")
@Table(name = ClientOrderItem.TABLE_NAME)
public class ClientOrderItem extends OrderItem {

	private static final long serialVersionUID = -8510224917223164963L;

	public static final String TABLE_NAME = "client_order_item";
	
	@JsonSerialize(using = ClientOrderSerializer.class)
	private ClientOrder clientOrder;
	
	private Boolean allowRetail;

	@ManyToOne(targetEntity = ClientOrder.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "client_order_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public ClientOrder getClientOrder() {
		return clientOrder;
	}

	public void setClientOrder(ClientOrder clientOrder) {
		this.clientOrder = clientOrder;
	}

	@Basic
	@Column(name = "allow_retail")
	public Boolean getAllowRetail() {
		return allowRetail;
	}

	public void setAllowRetail(Boolean allowRetail) {
		this.allowRetail = allowRetail;
	}
}
