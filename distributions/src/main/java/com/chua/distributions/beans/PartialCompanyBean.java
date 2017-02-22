package com.chua.distributions.beans;

import com.chua.distributions.database.entity.Company;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public class PartialCompanyBean extends PartialEntityBean<Company> {

	private String name;

	public PartialCompanyBean(Company company) {
		super(company);
		setName(company.getName());
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
