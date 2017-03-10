package com.chua.distributions.database.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
@Entity(name = "PurchaseOrder")
@Table(name = PurchaseOrder.TABLE_NAME)
public class PurchaseOrder extends Order {

	private static final long serialVersionUID = 5443831736325937649L;
	
	public static final String TABLE_NAME = "purchase_order";
}
