package com.chua.distributions.beans;

import com.chua.distributions.database.entity.User;
import com.chua.distributions.enums.UserType;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	21 Feb 2017
 */
public class PartialUserBean extends PartialEntityBean<User> {

	private String firstName;
	
	private String lastName;
	
	private String emailAddress;
	
	private String contactNumber;
	
	private String username;
	
	private Integer itemsPerPage;
	
	private UserType userType;
	
	public PartialUserBean(User user) {
		super(user);
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setEmailAddress(user.getEmailAddress());
		setContactNumber(user.getContactNumber());
		setUsername(user.getUsername());
		setItemsPerPage(user.getItemsPerPage());
		setUserType(user.getUserType());
	}

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
	
	public String getFullName() {
		return firstName + " " + lastName;
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
}
