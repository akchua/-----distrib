package com.chua.distributions.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.database.entity.base.BaseObject;
import com.chua.distributions.serializer.json.ProductSerializer;
import com.chua.distributions.serializer.json.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	29 Mar 2017
 */
@Entity(name = "ClientPromo")
@Table(name = ClientPromo.TABLE_NAME)
public class ClientPromo extends BaseObject {

	private static final long serialVersionUID = -8394137020152499543L;

	public static final String TABLE_NAME = "client_promo";
	
	@JsonSerialize(using = UserSerializer.class)
	private User client;
	
	@JsonSerialize(using = ProductSerializer.class)
	private Product product;
	
	private Float discount;

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

	@ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Basic
	@Column(name = "discount")
	public Float getDiscount() {
		return discount;
	}
	
	@Transient
	public Float getDiscountAmount() {
		return Math.round(product.getGrossPrice() * discount) / 100.0f;
	}
	
	@Transient
	public Float getPackageDiscountAmount() {
		return Math.round(product.getPackageGrossPrice() * discount) / 100.0f;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}
}
