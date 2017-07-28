package com.chua.distributions.utility.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.distributions.database.entity.Category;
import com.chua.distributions.database.entity.Company;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.utility.format.DateFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   28 Jul 2017
 */
public class PriceListTemplate implements Template {

	private Company company;
	
	private List<Product> products;
	
	private List<String> perCategoryPriceLists;
	
	public PriceListTemplate(Company company, List<Product> products) {
		this.company = company;
		this.products = products;
		
		this.perCategoryPriceLists = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine) {
		for(List<Product> productsInCategory : separateByCategory(products)) {
			final PerCategoryPriceListTemplate perCategoryPriceListTemplate = new PerCategoryPriceListTemplate(productsInCategory);
			perCategoryPriceLists.add(perCategoryPriceListTemplate.merge(velocityEngine));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/priceList.vm", "UTF-8", model);
	}
	
	private List<List<Product>> separateByCategory(List<Product> products) {
		final List<List<Product>> productsPerCategory = new ArrayList<List<Product>>();
		Category temp = null;
		
		List<Product> productsInCategory = new ArrayList<Product>();
		
		for(Product product : products) {
			if(temp == null || !product.getCategory().getId().equals(temp.getId())) {
				if(!productsInCategory.isEmpty()) productsPerCategory.add(productsInCategory);
				temp = product.getCategory();
				productsInCategory = new ArrayList<Product>();
			}
			
			productsInCategory.add(product);
		}
		
		return productsPerCategory;
	}
	
	public String getCompanyName() {
		return company.getName();
	}
	
	public String getDate() {
		return DateFormatter.longFormat(new Date());
	}
	
	public List<String> getPerCategoryPriceLists() {
		return perCategoryPriceLists;
	}
}
