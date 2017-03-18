package com.chua.distributions.database.service;

import com.chua.distributions.database.entity.ClientProductPrice;
import com.chua.distributions.database.prototype.ClientProductPricePrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 18, 2017
 */
public interface ClientProductPriceService 
		extends Service<ClientProductPrice, Long>, ClientProductPricePrototype {

	ObjectList<ClientProductPrice> findAllWithPagingOrderByProductName(int pageNumber, int resultsPerPage, Long clientId);
	
	ClientProductPrice findByClientAndProduct(Long clientId, Long productId);
	
	boolean isExistsByClientAndProduct(Long clientId, Long productId);
}
