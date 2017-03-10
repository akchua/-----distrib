package com.chua.distributions.beans;

import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.enums.Status;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	14 Feb 2017
 */
public class PartialClientOrderBean extends PartialEntityBean<ClientOrder> {

	private String formattedCreatedOn;
	
	private Long creatorId;

	private String creatorName;
	
	private Long clientId;
	
	private String clientName;
	
	private Long companyId;
	
	private String companyName;
	
	private String formattedGrossTotal;
	
	private String formattedDiscountTotal;
	
	private Float lessVat;
	
	private String formattedLessVatAmount;
	
	private Float additionalDiscount;
	
	private String formattedAdditionalDiscount;
	
	private Status status;
	
	private String formattedNetTotal;

	public PartialClientOrderBean(ClientOrder clientOrder) {
		super(clientOrder);
		setFormattedCreatedOn(clientOrder.getFormattedCreatedOn());
		setCreatorId(clientOrder.getCreator().getId());
		setCreatorName(clientOrder.getCreator().getFormattedName());
		setClientId(clientOrder.getClient().getId());
		setClientName(clientOrder.getClient().getFormattedName());
		setCompanyId(clientOrder.getCompany().getId());
		setCompanyName(clientOrder.getCompany().getName());
		setFormattedGrossTotal(clientOrder.getFormattedGrossTotal());
		setFormattedDiscountTotal(clientOrder.getFormattedDiscountTotal());
		setLessVat(clientOrder.getLessVat());
		setFormattedLessVatAmount(clientOrder.getFormattedLessVatAmount());
		setAdditionalDiscount(clientOrder.getAdditionalDiscount());
		setFormattedAdditionalDiscount(clientOrder.getFormattedAdditionalDiscountAmount());
		setStatus(clientOrder.getStatus());
		setFormattedNetTotal(clientOrder.getFormattedNetTotal());
	}
	
	public String getFormattedCreatedOn() {
		return formattedCreatedOn;
	}

	public void setFormattedCreatedOn(String formattedCreatedOn) {
		this.formattedCreatedOn = formattedCreatedOn;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFormattedGrossTotal() {
		return formattedGrossTotal;
	}

	public void setFormattedGrossTotal(String formattedGrossTotal) {
		this.formattedGrossTotal = formattedGrossTotal;
	}

	public String getFormattedDiscountTotal() {
		return formattedDiscountTotal;
	}

	public void setFormattedDiscountTotal(String formattedDiscountTotal) {
		this.formattedDiscountTotal = formattedDiscountTotal;
	}

	public Float getLessVat() {
		return lessVat;
	}

	public void setLessVat(Float lessVat) {
		this.lessVat = lessVat;
	}

	public String getFormattedLessVatAmount() {
		return formattedLessVatAmount;
	}

	public void setFormattedLessVatAmount(String formattedLessVatAmount) {
		this.formattedLessVatAmount = formattedLessVatAmount;
	}

	public Float getAdditionalDiscount() {
		return additionalDiscount;
	}

	public void setAdditionalDiscount(Float additionalDiscount) {
		this.additionalDiscount = additionalDiscount;
	}

	public String getFormattedAdditionalDiscount() {
		return formattedAdditionalDiscount;
	}

	public void setFormattedAdditionalDiscount(String formattedAdditionalDiscount) {
		this.formattedAdditionalDiscount = formattedAdditionalDiscount;
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
