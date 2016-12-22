package com.chua.distributions.rest.handler;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
public interface ClientOrderItemHandler {

	ObjectList<ClientOrderItem> getClientOrderItemObjectList(Integer pageNumber, Long clientOrderId);
	
	ObjectList<ClientOrderItem> getClientOrderItemByProductObjectList(Integer pageNumber, Long productId);
	
	ResultBean addItem(Long productId, Long clientOrderId);
	
	ResultBean removeItem(Long clientOrderItemId);
	
	ResultBean changePieceQuantity(Long clientOrderItemId, Integer pieceQuantity);
	
	ResultBean changePackageQuantity(Long clientOrderItemId, Integer packageQuantity);
}
