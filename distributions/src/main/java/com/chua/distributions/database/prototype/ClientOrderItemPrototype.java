package com.chua.distributions.database.prototype;

import java.util.List;

import com.chua.distributions.database.entity.ClientOrderItem;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
public interface ClientOrderItemPrototype extends Prototype<ClientOrderItem, Long> {

	ClientOrderItem findByNameAndClientOrder(String displayName, Long clientOrderId);
	
	List<ClientOrderItem> findAllByClientOrder(Long clientOrderId);
}
