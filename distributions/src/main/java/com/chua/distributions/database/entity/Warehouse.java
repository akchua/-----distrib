package com.chua.distributions.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.chua.distributions.database.entity.base.BaseObject;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   31 Aug 2017
 */
@Entity(name = "Warehouse")
@Table(name = Warehouse.TABLE_NAME)
public class Warehouse extends BaseObject {

	private static final long serialVersionUID = 4985443998913628205L;

	public static final String TABLE_NAME = "warehouse";
	
	private String name;
	
	private String address;

	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	// LEGACY SUPPORT
	@Deprecated
	@Transient
	public String getDisplayName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
