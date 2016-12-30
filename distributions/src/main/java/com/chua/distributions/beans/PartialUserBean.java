package com.chua.distributions.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 30, 2016
 */
public class PartialUserBean {

	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String emailAddress;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
