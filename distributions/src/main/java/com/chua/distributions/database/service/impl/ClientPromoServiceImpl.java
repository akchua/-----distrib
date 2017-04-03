package com.chua.distributions.database.service.impl;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.ClientPromoDAO;
import com.chua.distributions.database.entity.ClientPromo;
import com.chua.distributions.database.service.ClientPromoService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	29 Mar 2017
 */
@Service
public class ClientPromoServiceImpl
		extends AbstractService<ClientPromo, Long, ClientPromoDAO>
		implements ClientPromoService {

	@Autowired
	protected ClientPromoServiceImpl(ClientPromoDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<ClientPromo> findAllWithPagingOrderByProductName(int pageNumber, int resultsPerPage,
			Long clientId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, clientId, new Order[] { Order.asc("prod.name") });
	}

	@Override
	public ClientPromo findByClientAndProduct(Long clientId, Long productId) {
		return dao.findByClientAndProduct(clientId, productId);
	}

	@Override
	public boolean isExistsByClientAndProduct(Long clientId, Long productId) {
		return dao.findByClientAndProduct(clientId, productId) != null;
	}
}
