package com.chua.distributions.rest.handler;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
public interface PurchaseOrderItemHandler {

	ObjectList<PurchaseOrderItem> getPurchaseOrderItemObjectList(Integer pageNumber, Long purchaseOrderId);
	
	ResultBean addItem(Long productId, Long purchaseOrderId);
	
	ResultBean removeItem(Long purchaseOrderItemId);
	
	ResultBean changePieceQuantity(Long purchaseOrderItemId, Integer pieceQuantity);
	
	ResultBean changePackageQuantity(Long purchaseOrderItemId, Integer packageQuantity);
}
