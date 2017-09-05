package com.chua.distributions.utility.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.database.entity.Product;
import com.chua.distributions.utility.StringHelper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   28 Jul 2017
 */
public class PerCategoryPriceListTemplate implements Template {
	
	private List<Product> productsInCategory;
	
	private List<String> productPrices;
	
	public PerCategoryPriceListTemplate(List<Product> productsInCategory) {
		this.productsInCategory = productsInCategory;
		
		this.productPrices = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		for(int i = 0; i < productsInCategory.size(); i++) {
			final ProductPriceTemplate productPriceTemplate = new ProductPriceTemplate(Long.valueOf(i + 1), 
																						productsInCategory.get(i),
																						(i > 0) ? productsInCategory.get(i - 1) : null);
			productPrices.add(productPriceTemplate.merge(velocityEngine));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/perCategoryPriceList.vm", "UTF-8", model);
	}
	
	public String getCategoryName() {
		return StringHelper.center(" " + productsInCategory.get(0).getCategory().getName() + " ", 96, '-');
	}
	
	public List<String> getProductPrices() {
		return productPrices;
	}
}
