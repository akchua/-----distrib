package com.chua.distributions.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.chua.distributions.database.entity.base.BaseObject;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.UserType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 13, 2016
 */
@Entity(name = "User")
@Table(name = User.TABLE_NAME)
public class User extends BaseObject {

	private static final long serialVersionUID = -6304724786725941698L;
	
	public static final String TABLE_NAME = "user";
	
	private String firstName;
	
	private String lastName;
	
	private String emailAddress;
	
	private String contactNumber;
	
	private String username;
	
	private String password;
	
	private Integer itemsPerPage;
	
	private UserType userType;
	
	private String businessName;
	
	private String businessAddress;
	
	private String businessContactNumber;
	
	private Area businessArea;

	@Basic
	@Column(name = "first_name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Basic
	@Column(name = "last_name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Transient
	public String getFormattedName() {
		return lastName + ", " + firstName;
	}

	@Basic
	@Column(name = "email_address")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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
	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Basic
	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Basic
	@Column(name = "items_per_page")
	public Integer getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "user_type", length = 50)
	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@Basic
	@Column(name = "business_name")
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	@Basic
	@Column(name = "business_address")
	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	@Basic
	@Column(name = "business_contact_number")
	public String getBusinessContactNumber() {
		return businessContactNumber;
	}

	public void setBusinessContactNumber(String businessContactNumber) {
		this.businessContactNumber = businessContactNumber;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "business_area", length = 50)
	public Area getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(Area businessArea) {
		this.businessArea = businessArea;
	}
}
