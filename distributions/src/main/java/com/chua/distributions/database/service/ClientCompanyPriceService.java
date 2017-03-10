package com.chua.distributions.database.service;

import com.chua.distributions.database.entity.ClientCompanyPrice;
import com.chua.distributions.database.prototype.ClientCompanyPricePrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Mar 2017
 */
public interface ClientCompanyPriceService extends Service<ClientCompanyPrice, Long>, ClientCompanyPricePrototype {

	ObjectList<ClientCompanyPrice> findAllWithPagingOrderByCompanyName(int pageNumber, int resultsPerPage, Long clientId);
	
	ClientCompanyPrice findByClientAndCompany(Long clientId, Long companyId);
	
	boolean isExistsByClientAndCompany(Long clientId, Long companyId);
}
