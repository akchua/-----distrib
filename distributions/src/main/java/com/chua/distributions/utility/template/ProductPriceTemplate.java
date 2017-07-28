package com.chua.distributions.utility.template;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.database.entity.Product;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   28 Jul 2017
 */
public class ProductPriceTemplate implements Template {
	
	private Long numbering;
	
	private Product product;

	public ProductPriceTemplate(Long numbering, Product product) {
		this.numbering = numbering;
		this.product = product;
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/productPrice.vm", "UTF-8", model);
	}
	
	public String getNumbering() {
		DecimalFormat numberingFormat = new DecimalFormat("000.");
		return numberingFormat.format(numbering);
	}
	
	public String getProductDisplayName() {
		return String.format("%-55s", product.getDisplayName());
	}
	
	public String getFormattedPackageSellingPrice() {
		return String.format("%15s", product.getFormattedPackageNetSellingPrice());
	}
}
