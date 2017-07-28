package com.chua.distributions.database.prototype;

import java.util.List;

import com.chua.distributions.database.entity.ProductImage;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   27 Jul 2017
 */
public interface ProductImagePrototype extends Prototype<ProductImage, Long> {

	List<ProductImage> findAllByProductId(Long productId);
}
