package com.chua.distributions.beans;

import com.chua.distributions.enums.Status;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	14 Feb 2017
 */
public class PartialClientOrderBean extends PartialEntityBean {

	private String formattedCreatedOn;
	
	private String formattedUpdatedOn;
	
	private String creatorName;
	
	private Status status;
	
	private String formattedNetTotal;

	public String getFormattedCreatedOn() {
		return formattedCreatedOn;
	}

	public void setFormattedCreatedOn(String formattedCreatedOn) {
		this.formattedCreatedOn = formattedCreatedOn;
	}

	public String getFormattedUpdatedOn() {
		return formattedUpdatedOn;
	}

	public void setFormattedUpdatedOn(String formattedUpdatedOn) {
		this.formattedUpdatedOn = formattedUpdatedOn;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getFormattedNetTotal() {
		return formattedNetTotal;
	}

	public void setFormattedNetTotal(String formattedNetTotal) {
		this.formattedNetTotal = formattedNetTotal;
	}
}
