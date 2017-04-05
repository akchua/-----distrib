package com.chua.distributions.rest.handler;

import java.util.List;

import com.chua.distributions.beans.PartialProductBean;
import com.chua.distributions.beans.ProductFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
public interface ProductHandler {

	Product getProduct(Long productId, Warehouse warehouse);
	
	PartialProductBean getPartialProduct(Long productId, Warehouse warehouse);
	
	PartialProductBean getPartialProductFor(Long productId, Long userId);

	ObjectList<Product> getProductObjectList(Integer pageNumber, String searchKey, Long companyId, Long categoryId, Warehouse warehouse);
	
	ObjectList<PartialProductBean> getPartialProductObjectList(Integer pageNumber, String searchKey, Long companyId, Long categoryId, Warehouse warehouse);
	
	List<Product> getProductListOrderByName();
	
	ResultBean createProduct(ProductFormBean productForm);
	
	ResultBean updateProduct(ProductFormBean productForm);
	
	ResultBean removeProduct(Long productId);
	
	/**
	 * Method used to get the final base price of a single piece of the given product based on the price settings of the given user.
	 * 
	 * Final base price includes applying of markup or price overrides if any exists.
	 * If any price overrides exists the markup will not apply.
	 * 
	 * The final base price is the price the price that the client will see while choosing a product.
	 * Client discount and less vat still applies on top of the final base price.
	 * @param product The product to get the final base price of.
	 * @param user The user where the price settings will be based.
	 * @return The final base price.
	 */
	Float getFinalBaseUnitSellingPrice(Product product, User user);
	
	Float getFinalBasePackageSellingPrice(Product product, User user);

	Float getFinalSellingDiscount(Product product, User user);
}
