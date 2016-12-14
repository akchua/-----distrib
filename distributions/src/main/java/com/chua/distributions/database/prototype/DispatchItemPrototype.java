package com.chua.distributions.database.prototype;

import java.util.List;

import com.chua.distributions.database.entity.DispatchItem;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
public interface DispatchItemPrototype extends Prototype<DispatchItem, Long> {

	List<DispatchItem> findAllByDispatch(Long dispatchId);
	
	DispatchItem findByDispatchAndOrder(Long dispatchId, Long clientOrderId);
}
