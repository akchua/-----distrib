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
import com.chua.distributions.utility.format.CurrencyFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 18, 2017
 */
@Entity(name = "ClientProductPrice")
@Table(name = ClientProductPrice.TABLE_NAME)
public class ClientProductPrice extends BaseObject {

	private static final long serialVersionUID = -8521471302649531255L;

	public static final String TABLE_NAME = "client_product_price";
	
	@JsonSerialize(using = UserSerializer.class)
	private User client;
	
	@JsonSerialize(using = ProductSerializer.class)
	private Product product;
	
	private Float packageSellingPrice;

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
	@Column(name = "package_selling_price")
	public Float getPackageSellingPrice() {
		return packageSellingPrice;
	}
	
	@Transient
	public Float getSellingPrice() {
		return packageSellingPrice / product.getPackaging();
	}
	
 	@Transient
	public String getFormattedPackageSellingPrice() {
		return CurrencyFormatter.pesoFormat(packageSellingPrice);
	}

	public void setPackageSellingPrice(Float packageSellingPrice) {
		this.packageSellingPrice = packageSellingPrice;
	}
}
