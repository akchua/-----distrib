package com.chua.distributions.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chua.distributions.database.entity.base.BaseObject;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 4, 2016
 */
@Entity(name = "Category")
@Table(name = Category.TABLE_NAME)
public class Category extends BaseObject {

	private static final long serialVersionUID = -8579726842902861051L;
	
	public static final String TABLE_NAME = "category";
	
	private String name;

	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
