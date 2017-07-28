package com.chua.distributions.beans;

import com.chua.distributions.database.entity.ProductImage;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   28 Jul 2017
 */
public class PartialProductImageBean extends PartialEntityBean<ProductImage> {

	private String fileName;
	
	public PartialProductImageBean(ProductImage productImage) {
		super(productImage);
		setFileName(productImage.getFileName());
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
