package com.chua.distributions.database.prototype;

import com.chua.distributions.database.entity.Product;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
public interface ProductPrototype extends Prototype<Product, Long> {

	Product findByDisplayName(String displayName);
}
