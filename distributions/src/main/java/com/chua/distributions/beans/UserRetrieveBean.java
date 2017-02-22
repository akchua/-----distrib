package com.chua.distributions.beans;

import com.chua.distributions.database.entity.User;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 30, 2016
 */
public class UserRetrieveBean extends PartialEntityBean<User> {

	private String firstName;
	
	private String lastName;
	
	private String emailAddress;
	
	public UserRetrieveBean(User user) {
		super(user);
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setEmailAddress(user.getEmailAddress());
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
