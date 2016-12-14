package com.chua.distributions.database.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.database.entity.base.BaseObject;
import com.chua.distributions.serializer.json.ClientOrderSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
@Entity(name = "DispatchItem")
@Table(name = DispatchItem.TABLE_NAME)
public class DispatchItem extends BaseObject {

	private static final long serialVersionUID = -1663068392896044614L;

	public static final String TABLE_NAME = "dispatch_item";
	
	private Dispatch dispatch;
	
	@JsonSerialize(using = ClientOrderSerializer.class)
	private ClientOrder clientOrder;

	@ManyToOne(targetEntity = Dispatch.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "dispatch_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Dispatch getDispatch() {
		return dispatch;
	}

	public void setDispatch(Dispatch dispatch) {
		this.dispatch = dispatch;
	}

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
}
