package com.chua.distributions.rest.handler.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.beans.ProductFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.database.service.CategoryService;
import com.chua.distributions.database.service.CompanyService;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.database.service.WarehouseItemService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ProductHandler;
import com.chua.distributions.utility.Html;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
@Transactional
@Component
public class ProductHandlerImpl implements ProductHandler {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private WarehouseItemService warehouseItemService;
	
	@Override
	public Product getProduct(Long productId, Warehouse warehouse) {
		final Product product = productService.find(productId);
		if(product != null) setProductStock(product, warehouse);
		return product;
	}
	
	@Override
	public ObjectList<Product> getProductObjectList(Integer pageNumber, String searchKey, Long companyId, Long categoryId, Warehouse warehouse) {
		ObjectList<Product> objProducts = productService.findAllWithPagingOrderByName(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, companyId, categoryId);
		for(Product product : objProducts.getList()) setProductStock(product, warehouse);
		return objProducts;
	}
	
	@Override
	public ObjectList<WarehouseItem> getWarehouseItemObjectList(Integer pageNumber, String searchKey, Warehouse warehouse) {
		return warehouseItemService.findAllWithPagingOrderByProductName(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, warehouse);
	}

	@Override
	public ResultBean createProduct(ProductFormBean productForm) {
		final ResultBean result;
		final ResultBean validateForm = validateProductForm(productForm);
		
		if(validateForm.getSuccess()) {
			final String displayName = getDisplayName(productForm);
			if(productService.isExistsByDisplayName(displayName)) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Product name already exists.")));
			} else if(productService.isExistsByProductCode(productForm.getProductCode())) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Product code already exists.")));
			} else {
				final Product product = new Product();
				product.setDisplayName(displayName);
				setProduct(product, productForm);
				
				result = new ResultBean();
				result.setSuccess(productService.insert(product) != null);
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created Product " + Html.text(Color.BLUE, product.getDisplayName()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			}
		} else {
			result = validateForm;
		}
		
		return result;
	}

	@Override
	public ResultBean updateProduct(ProductFormBean productForm) {
		final ResultBean result;
		final Product product = productService.find(productForm.getId());
		
		if(product != null) {
			final ResultBean validateForm = validateProductForm(productForm);
			if(validateForm.getSuccess()) {
				final String displayName = getDisplayName(productForm);
				final Product productt = productService.findByDisplayName(displayName);
				if(productt != null && product.getId() != productt.getId()) {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Product name already exists.")));
				} else {
					product.setDisplayName(displayName);
					setProduct(product, productForm);
					
					result = new ResultBean();
					result.setSuccess(productService.update(product));
					if(result.getSuccess()) {
						result.setMessage(Html.line("Product " + Html.text(Color.BLUE, product.getDisplayName()) + " has been successfully " + Html.text(Color.GREEN, "updated") + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				}
			} else {
				result = validateForm;
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean removeProduct(Long productId) {
		final ResultBean result;
		final Product product = productService.find(productId);
		
		if(product != null) {
			result = new ResultBean();
			
			result.setSuccess(productService.delete(product));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Product " + Html.text(Color.BLUE, product.getName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
		}
		
		return result;
	}
	
	private void setProduct(Product product, ProductFormBean productForm) {
		product.setCompany(companyService.find(productForm.getCompanyId()));
		product.setCategory(categoryService.find(productForm.getCategoryId()));
		product.setProductCode(productForm.getProductCode().trim());
		product.setName(productForm.getName().trim());
		product.setSize(productForm.getSize().trim());
		product.setPackaging(productForm.getPackaging());
		product.setDescription(productForm.getDescription().trim());
		product.setGrossPrice(productForm.getPackageGrossPrice() / productForm.getPackaging());
		product.setDiscount(productForm.getDiscount());
		product.setSellingPrice(productForm.getPackageSellingPrice() / productForm.getPackaging());
		product.setPercentProfit(productForm.getPercentProfit());
	}
	
	private void setProductStock(Product product, Warehouse warehouse) {
		final List<WarehouseItem> warehouseItems = warehouseItemService.findAllByProduct(product.getId());
		Integer stockCountAll = 0;
		Integer stockCountCurrent = 0;
		for(WarehouseItem warehouseItem : warehouseItems) {
			stockCountAll += warehouseItem.getStockCount();
			if(warehouse != null && warehouseItem.getWarehouse().equals(warehouse)) {
				stockCountCurrent = warehouseItem.getStockCount();
			}
		}
		if(warehouse != null) product.setStockCountCurrent(stockCountCurrent);
		product.setStockCountAll(stockCountAll);
	}
	
	private String getDisplayName(ProductFormBean productForm) {
		String displayName = productForm.getName() + " ";
		
		if(productForm.getPackaging() > 1) displayName += productForm.getPackaging() + "x";
		displayName += productForm.getSize() + "";
		
		return displayName;
	}
	
	private ResultBean validateProductForm(ProductFormBean productForm) {
		final ResultBean result;
		
		if(productForm.getCompanyId() == null || productForm.getCategoryId() == null ||
				productForm.getProductCode() == null || productForm.getProductCode().trim().length() < 3 ||
				productForm.getName() == null || productForm.getName().trim().length() < 3 ||
				productForm.getSize() == null ||
				productForm.getPackaging() == null || productForm.getPackaging() <= 0 ||
				productForm.getDescription() == null || productForm.getDescription().trim().length() < 3 ||
				productForm.getPackageGrossPrice() == null || productForm.getPackageGrossPrice() <= 0 ||
				productForm.getDiscount() == null || productForm.getDiscount() <= 0 ||
				productForm.getPackageNetPrice() == null || productForm.getPackageNetPrice() <= 0 ||
				productForm.getPackageSellingPrice() == null || productForm.getPackageSellingPrice() <= 0||
				productForm.getPercentProfit() == null || productForm.getPercentProfit() <= 0) {
			result = new ResultBean(Boolean.FALSE, Html.line("All fields are " + Html.text(Color.RED, "required") + " and must contain at least 3 characters or must have a value greater than 0."));
		} else if(productForm.getPackaging() > 999) {
			result = new ResultBean(Boolean.FALSE, Html.line("Packaging " + Html.text(Color.RED, "cannot exceed") + " the value of 999."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
