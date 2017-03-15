package com.chua.distributions.database.service.impl;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.ClientCompanyPriceDAO;
import com.chua.distributions.database.entity.ClientCompanyPrice;
import com.chua.distributions.database.service.ClientCompanyPriceService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Mar 2017
 */
@Service
public class ClientCompanyPriceServiceImpl
		extends AbstractService<ClientCompanyPrice, Long, ClientCompanyPriceDAO>
		implements ClientCompanyPriceService {

	@Autowired
	protected ClientCompanyPriceServiceImpl(ClientCompanyPriceDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<ClientCompanyPrice> findAllWithPagingOrderByCompanyName(int pageNumber, int resultsPerPage,
			Long clientId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, clientId, new Order[] { Order.asc("comp.name") });
	}
	
	@Override
	public ClientCompanyPrice findByClientAndCompany(Long clientId, Long companyId) {
		return dao.findByClientAndCompany(clientId, companyId);
	}

	@Override
	public boolean isExistsByClientAndCompany(Long clientId, Long companyId) {
		return dao.findByClientAndCompany(clientId, companyId) != null;
	}
}
