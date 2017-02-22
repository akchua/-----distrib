package com.chua.distributions.beans;

import com.chua.distributions.database.entity.base.BaseEntity;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public abstract class PartialEntityBean<T extends BaseEntity> {

	private Long id;

	public PartialEntityBean(T t) {
		this.id = t.getId();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
