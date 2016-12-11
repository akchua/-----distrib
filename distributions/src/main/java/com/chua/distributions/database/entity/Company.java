package com.chua.distributions.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chua.distributions.database.entity.base.BaseObject;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 3, 2016
 */
@Entity(name = "Company")
@Table(name = Company.TABLE_NAME)
public class Company extends BaseObject {

	private static final long serialVersionUID = 2230679005604648036L;
	
	public static final String TABLE_NAME = "company";
	
	private String name;
	
	private String contactPerson;
	
	private String contactNumber;
	
	private String emailAddress;
	
	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "contact_person")
	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	@Basic
	@Column(name = "contact_number")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Basic
	@Column(name = "email_address")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
