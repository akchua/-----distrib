package com.chua.distributions.database.service.impl;

import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.ProductRankDAO;
import com.chua.distributions.database.entity.ProductRank;
import com.chua.distributions.database.service.ProductRankService;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	17 Feb 2017
 */
@Service
public class ProductRankServiceImpl
		extends AbstractService<ProductRank, Long, ProductRankDAO>
		implements ProductRankService {

	protected ProductRankServiceImpl(ProductRankDAO dao) {
		super(dao);
	}
}
