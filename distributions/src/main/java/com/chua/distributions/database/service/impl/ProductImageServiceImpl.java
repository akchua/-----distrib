package com.chua.distributions.database.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.ProductImageDAO;
import com.chua.distributions.database.entity.ProductImage;
import com.chua.distributions.database.service.ProductImageService;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   27 Jul 2017
 */
@Service
public class ProductImageServiceImpl
		extends AbstractService<ProductImage, Long, ProductImageDAO>
		implements ProductImageService {

	@Autowired
	protected ProductImageServiceImpl(ProductImageDAO dao) {
		super(dao);
	}

	@Override
	public List<ProductImage> findAllByProductId(Long productId) {
		return dao.findAllByProductId(productId);
	}
}
