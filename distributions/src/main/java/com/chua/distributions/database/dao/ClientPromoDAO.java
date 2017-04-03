package com.chua.distributions.database.dao;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.ClientPromo;
import com.chua.distributions.database.prototype.ClientPromoPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	29 Mar 2017
 */
public interface ClientPromoDAO extends DAO<ClientPromo, Long>, ClientPromoPrototype {

	ClientPromo findByClientAndProduct(Long clientId, Long productId);
	
	ObjectList<ClientPromo> findAllWithPaging(int pageNumber, int resultsPerPage, Long clientId);
	
	ObjectList<ClientPromo> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long clientId, Order[] orders);
}
