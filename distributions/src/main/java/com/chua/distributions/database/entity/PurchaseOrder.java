package com.chua.distributions.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.serializer.json.CompanySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
	
	@JsonSerialize(using = CompanySerializer.class)
	private Company company;
	
	private Warehouse warehouse;

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

	@Enumerated(EnumType.STRING)
	@Column(name = "warehouse", length = 50)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
}
