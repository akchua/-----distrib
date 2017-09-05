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
	
	private Product previousProduct;

	public ProductPriceTemplate(Long numbering, Product product, Product previousProduct) {
		this.numbering = numbering;
		this.product = product;
		this.previousProduct = previousProduct;
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
		final String productDisplayName;
		
		if(previousProduct != null) {
			String temp = "";
			String[] productTokens = product.getDisplayName().split(" ");
			String[] previousProductTokens = previousProduct.getDisplayName().split(" ");
			
			int i = 0;
			while(i < productTokens.length && i < previousProductTokens.length &&
					productTokens[i].equals(previousProductTokens[i])) {
				for(int j = 0; j <= productTokens[i].length(); j++) temp += " ";
				i++;
			}
			
			for(int k = i; k < productTokens.length; k++) {
				temp += productTokens[k] + " ";
			}
			
			productDisplayName = String.format("%-55s", temp);
		} else {
			productDisplayName = String.format("%-55s", product.getDisplayName());
		}
		
		return productDisplayName;
	}
	
	public String getFormattedPackageSellingPrice() {
		return String.format("%10s", product.getFormattedPackageNetSellingPrice());
	}
}
