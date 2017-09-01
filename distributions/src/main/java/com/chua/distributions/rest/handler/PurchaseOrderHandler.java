package com.chua.distributions.rest.handler;

import com.chua.distributions.beans.PurchaseOrderFormBean;
import com.chua.distributions.beans.PurchaseReportQueryBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
public interface PurchaseOrderHandler {

	PurchaseOrder getPurchaseOrder(Long purchaseOrderId);

	/**
	 * This method is used for auto generating destination instances for PurchaseOrderItem transfer.
	 * Get an empty instance of PurchaseOrder from the same creator with the TO_FOLLOW status.
	 * @param sourceId The id of the source PurchaseOrder.
	 * @return The PurchaseOrder instance based on the source given. Return source instance if source is an empty order.
	 */
	PurchaseOrder getTransferInstance(Long sourceId);
	
	ObjectList<PurchaseOrder> getPurchaseOrderObjectList(Integer pageNumber, Long companyId, Long warehouseId, Boolean showPaid);
	
	ObjectList<PurchaseOrder> getPaidPurchaseOrderObjectList(Integer pageNumber, Long companyId, Long warehouseId);
	
	ObjectList<PurchaseOrder> getPurchaseOrderObjectListByPurchaseReportQuery(Integer pageNumber, PurchaseReportQueryBean purchaseReportQuery);
	
	ResultBean createPurchaseOrder(PurchaseOrderFormBean purchaseOrderForm);
	
	ResultBean updatePurchaseOrder(PurchaseOrderFormBean purchaseOrderForm);
	
	ResultBean submitPurchaseOrder(Long purchaseOrderId);
	
	ResultBean sendPurchaseOrder(Long purchaseOrderId);
	
	ResultBean receivePurchaseOrder(Long purchaseOrderId);
	
	ResultBean payPurchaseOrder(Long purchaseOrderId);
	
	ResultBean removePurchaseOrder(Long purchaseOrderId);
}
