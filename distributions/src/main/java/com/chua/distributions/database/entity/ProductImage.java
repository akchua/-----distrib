package com.chua.distributions.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.database.entity.base.BaseObject;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   27 Jul 2017
 */
@Entity(name = "ProductImage")
@Table(name = ProductImage.TABLE_NAME)
public class ProductImage extends BaseObject {

	private static final long serialVersionUID = 1693445316789109439L;

	public static final String TABLE_NAME = "product_image";
	
	private Product product;
	
	private String fileName;

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
	@Column(name = "file_name")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
