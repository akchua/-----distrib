package com.chua.distributions.rest.handler;

import java.util.List;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.beans.StringWrapper;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
public interface ClientOrderHandler {

	ClientOrder getClientOrder(Long clientOrderId);

	/**
	 * This method is used for auto generating destination instances for ClientOrderItem transfer.
	 * Get an empty instance of ClientOrder from the same creator with the TO_FOLLOW status.
	 * @param sourceId The id of the source ClientOrder.
	 * @return The ClientOrder instance based on the source given. Return source instance if source is an empty order.
	 */
	ClientOrder getTransferInstance(Long sourceId);
	
	ObjectList<ClientOrder> getClientOrderObjectList(Integer pageNumber, Boolean showPaid);
	
	ObjectList<ClientOrder> getClientOrderRequestObjectList(Integer pageNumber, Boolean showAccepted);
	
	ObjectList<ClientOrder> getAcceptedClientOrderObjectList(Integer pageNumber, Warehouse warehouse);
	
	ObjectList<ClientOrder> getReceivedClientOrderObjectList(Integer pageNumber, Warehouse warehouse);
	
	ObjectList<ClientOrder> getPaidClientOrderObjectList(Integer pageNumber, Warehouse warehouse);
	
	ObjectList<ClientOrder> getClientOrderObjectListBySalesReportQuery(Integer pageNumber, SalesReportQueryBean salesReportQuery);
	
	StringWrapper getFormattedTotalPayable();
	
	ResultBean addClientOrder();
	
	ResultBean submitClientOrder(Long clientOrderId);
	
	ResultBean acceptClientOrder(Long clientOrderId);
	
	ResultBean payClientOrder(Long clientOrderId);
	
	ResultBean removeClientOrder(Long clientOrderId);
	
	ResultBean generateReport(SalesReportQueryBean salesReportQuery);
	
	List<Warehouse> getWarehouseList();
}
