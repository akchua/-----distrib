package com.chua.distributions.database.dao.impl;

import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.ProductRankDAO;
import com.chua.distributions.database.entity.ProductRank;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	17 Feb 2017
 */
@Repository
public class ProductRankDAOImpl 
		extends AbstractDAO<ProductRank, Long>
		implements ProductRankDAO {

}
