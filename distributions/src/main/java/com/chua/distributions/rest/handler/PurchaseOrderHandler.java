package com.chua.distributions.rest.handler;

import java.util.List;

import com.chua.distributions.beans.PurchaseOrderFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
public interface PurchaseOrderHandler {

	PurchaseOrder getPurchaseOrder(Long purchaseOrderId);

	ObjectList<PurchaseOrder> getPurchaseOrderObjectList(Integer pageNumber, Long companyId, Warehouse warehouse, Boolean showPaid);
	
	ObjectList<PurchaseOrder> getPaidPurchaseOrderObjectList(Integer pageNumber, Long companyId, Warehouse warehouse);
	
	ResultBean createPurchaseOrder(PurchaseOrderFormBean purchaseOrderForm);
	
	ResultBean updatePurchaseOrder(PurchaseOrderFormBean purchaseOrderForm);
	
	ResultBean submitPurchaseOrder(Long purchaseOrderId);
	
	ResultBean sendPurchaseOrder(Long purchaseOrderId);
	
	ResultBean receivePurchaseOrder(Long purchaseOrderId);
	
	ResultBean payPurchaseOrder(Long purchaseOrderId);
	
	ResultBean removePurchaseOrder(Long purchaseOrderId);
	
	List<Warehouse> getWarehouseList();
}
