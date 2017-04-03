package com.chua.distributions.database.service;

import com.chua.distributions.database.entity.ClientPromo;
import com.chua.distributions.database.prototype.ClientPromoPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	29 Mar 2017
 */
public interface ClientPromoService extends Service<ClientPromo, Long>, ClientPromoPrototype {

	ObjectList<ClientPromo> findAllWithPagingOrderByProductName(int pageNumber, int resultsPerPage, Long clientId);
	
	ClientPromo findByClientAndProduct(Long clientId, Long productId);
	
	boolean isExistsByClientAndProduct(Long clientId, Long productId);
}
