package com.chua.distributions.beans;

import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.UserType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 14, 2016
 */
public class UserFormBean extends FormBean {

	private String firstName;
	
	private String lastName;
	
	private String emailAddress;
	
	private String contactNumber;
	
	private String username;
	
	private String password;
	
	private String confirmPassword;
	
	private Integer itemsPerPage;
	
	private UserType userType;
	
	private String businessName;
	
	private String businessAddress;
	
	private String businessContactNumber;
	
	private Area businessArea;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Integer getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getBusinessContactNumber() {
		return businessContactNumber;
	}

	public void setBusinessContactNumber(String businessContactNumber) {
		this.businessContactNumber = businessContactNumber;
	}

	public Area getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(Area businessArea) {
		this.businessArea = businessArea;
	}
}
