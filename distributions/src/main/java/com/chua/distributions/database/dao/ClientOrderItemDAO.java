package com.chua.distributions.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.database.prototype.ClientOrderItemPrototype;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
public interface ClientOrderItemDAO extends DAO<ClientOrderItem, Long>, ClientOrderItemPrototype {

	ObjectList<ClientOrderItem> findAllWithPaging(int pageNumber, int resultsPerPage, Long clientOrderId);
	
	ObjectList<ClientOrderItem> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long clientOrderId, Order[] orders);
	
	ObjectList<ClientOrderItem> findByProductWithPagingAndOrder(int pageNumber, int resultsPerPage, Long productId, Order[] orders);
	
	/**
	 * This is a heavy process used to locate specific group of client order items.
	 * @param productId The id of the product to locate. Cannot be null.
	 * @param warehouse The warehouse where the client order item was unstocked. Any warehouse if null.
	 * @param status The status of the client order it belongs to. Any Status if null.
	 * @return A list of client order items belonging to the group.
	 */
	List<ClientOrderItem> findAllByProductWarehouseAndStatus(Long productId, Warehouse warehouse, Status[] status);
}
