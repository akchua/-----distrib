package com.chua.distributions.rest.handler;

import com.chua.distributions.beans.DispatchFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Dispatch;
import com.chua.distributions.database.entity.DispatchItem;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
public interface DispatchHandler {

	Dispatch getDispatch(Long dispatchId);

	ObjectList<Dispatch> getDispatchObjectList(Integer pageNumber, Boolean showReceived);
	
	ObjectList<DispatchItem> getDispatchItemObjectList(Integer pageNumber, Long dispatchId);
	
	ResultBean createDispatch(DispatchFormBean dispatchForm);
	
	ResultBean dispatch(Long dispatchId);
	
	ResultBean completeDispatch(Long dispatchId);
	
	ResultBean removeDispatch(Long dispatchId);
	
	ResultBean addItem(Long clientOrderId, Long dispatchId);
	
	ResultBean removeItem(Long dispatchItemId);
}
