package com.chua.distributions.database.dao;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.ClientCompanyPrice;
import com.chua.distributions.database.prototype.ClientCompanyPricePrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Mar 2017
 */
public interface ClientCompanyPriceDAO extends DAO<ClientCompanyPrice, Long>, ClientCompanyPricePrototype {

	ClientCompanyPrice findByClientAndCompany(Long clientId, Long companyId);
	
	ObjectList<ClientCompanyPrice> findAllWithPaging(int pageNumber, int resultsPerPage, Long clientId);
	
	ObjectList<ClientCompanyPrice> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long clientId, Order[] orders);
}
