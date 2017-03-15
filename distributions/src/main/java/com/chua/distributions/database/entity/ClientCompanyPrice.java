package com.chua.distributions.database.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.database.entity.base.BaseObject;
import com.chua.distributions.serializer.json.CompanySerializer;
import com.chua.distributions.serializer.json.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Mar 2017
 */
@Entity(name = "ClientCompanyPrice")
@Table(name = ClientCompanyPrice.TABLE_NAME)
public class ClientCompanyPrice extends BaseObject {

	private static final long serialVersionUID = -5057041016830855065L;

	public static final String TABLE_NAME = "client_company_price";
	
	@JsonSerialize(using = UserSerializer.class)
	private User client;
	
	@JsonSerialize(using = CompanySerializer.class)
	private Company company;
	
	private Float discount;
	
	private Float markup;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getClient() {
		return client;
	}

	public void setClient(User client) {
		this.client = client;
	}

	@ManyToOne(targetEntity = Company.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Float getMarkup() {
		return markup;
	}

	public void setMarkup(Float markup) {
		this.markup = markup;
	}
}
