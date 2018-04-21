package com.chua.distributions.rest.handler;

import java.util.List;

import com.chua.distributions.beans.PartialClientOrderBean;
import com.chua.distributions.beans.ClientRankQueryBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.ClientRankType;
import com.chua.distributions.enums.ClientSalesReportType;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
public interface ClientOrderHandler {

	ClientOrder getClientOrder(Long clientOrderId);
	
	PartialClientOrderBean getPartialClientOrder(Long clientOrderId);

	/**
	 * This method is used for auto generating destination instances for ClientOrderItem transfer.
	 * Get an empty instance of ClientOrder from the same creator with the TO_FOLLOW status.
	 * @param sourceId The id of the source ClientOrder.
	 * @return The ClientOrder instance based on the source given. Return source instance if source is an empty order.
	 */
	ClientOrder getTransferInstance(Long sourceId);
	
	ObjectList<PartialClientOrderBean> getPartialClientOrderObjectList(Integer pageNumber, Boolean showPaid);
	
	ObjectList<ClientOrder> getClientOrderRequestObjectListCreatedByCurrentUser(Integer pageNumber);
	
	ObjectList<ClientOrder> getClientOrderRequestObjectList(Integer pageNumber, Boolean showAccepted);
	
	ObjectList<ClientOrder> getAcceptedClientOrderObjectList(Integer pageNumber, Long warehouseId);
	
	ObjectList<ClientOrder> getReceivedClientOrderObjectList(Integer pageNumber, Long warehouseId, Long clientId);
	
	ObjectList<ClientOrder> getPaidClientOrderObjectList(Integer pageNumber, Long warehouseId);
	
	ObjectList<ClientOrder> getClientOrderObjectListBySalesReportQuery(Integer pageNumber, SalesReportQueryBean salesReportQuery);
	
	String getFormattedTotalPayable();
	
	ResultBean addClientOrder(Long companyId);
	
	ResultBean addClientOrder(Long companyId, Long clientId);
	
	ResultBean submitClientOrder(Long clientOrderId);
	
	ResultBean acceptClientOrder(Long clientOrderId);
	
	ResultBean payClientOrder(Long clientOrderId);
	
	ResultBean removeClientOrder(Long clientOrderId);
	
	ResultBean generateReport(SalesReportQueryBean salesReportQuery);
	
	ResultBean generateClientRanking(ClientRankQueryBean rankQueryBean);
	
	List<ClientSalesReportType> getClientSalesReportTypes();
	
	List<ClientRankType> getClientRankTypes();
	
	List<Area> getAreaList();
}
