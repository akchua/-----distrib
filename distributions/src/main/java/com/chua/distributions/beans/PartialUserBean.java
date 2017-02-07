package com.chua.distributions.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 30, 2016
 */
public class PartialUserBean extends PartialEntityBean {

	private String firstName;
	
	private String lastName;
	
	private String emailAddress;

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
