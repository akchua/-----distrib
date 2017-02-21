package com.chua.distributions.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.enums.ProductRankType;
import com.chua.distributions.serializer.json.ProductSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	17 Feb 2017
 */
@Entity(name = "ProductRank")
@Table(name = ProductRank.TABLE_NAME)
public class ProductRank extends Rank {

	private static final long serialVersionUID = -5016240677233463342L;

	public static final String TABLE_NAME = "product_rank";
	
	@JsonSerialize(using = ProductSerializer.class)
	private Product product;
	
	private ProductRankType productRankType;
	
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

	@Enumerated(EnumType.STRING)
	@Column(name = "product_rank_type", length = 50)
	public ProductRankType getProductRankType() {
		return productRankType;
	}

	public void setProductRankType(ProductRankType productRankType) {
		this.productRankType = productRankType;
	}
}
