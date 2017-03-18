package com.chua.distributions.database.dao;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.ClientProductPrice;
import com.chua.distributions.database.prototype.ClientProductPricePrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 18, 2017
 */
public interface ClientProductPriceDAO 
		extends DAO<ClientProductPrice, Long>, ClientProductPricePrototype {

	ClientProductPrice findByClientAndProduct(Long clientId, Long productId);
	
	ObjectList<ClientProductPrice> findAllWithPaging(int pageNumber, int resultsPerPage, Long clientId);
	
	ObjectList<ClientProductPrice> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long clientId, Order[] orders);
}
