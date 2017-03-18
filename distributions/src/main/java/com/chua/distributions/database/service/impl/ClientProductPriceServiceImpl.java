package com.chua.distributions.database.service.impl;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.ClientProductPriceDAO;
import com.chua.distributions.database.entity.ClientProductPrice;
import com.chua.distributions.database.service.ClientProductPriceService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 18, 2017
 */
@Service
public class ClientProductPriceServiceImpl
		extends AbstractService<ClientProductPrice, Long, ClientProductPriceDAO>
		implements ClientProductPriceService {

	@Autowired
	protected ClientProductPriceServiceImpl(ClientProductPriceDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<ClientProductPrice> findAllWithPagingOrderByProductName(int pageNumber, int resultsPerPage,
			Long clientId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, clientId, new Order[] { Order.asc("prod.name") });
	}

	@Override
	public ClientProductPrice findByClientAndProduct(Long clientId, Long productId) {
		return dao.findByClientAndProduct(clientId, productId);
	}

	@Override
	public boolean isExistsByClientAndProduct(Long clientId, Long productId) {
		return dao.findByClientAndProduct(clientId, productId) != null;
	}
}
