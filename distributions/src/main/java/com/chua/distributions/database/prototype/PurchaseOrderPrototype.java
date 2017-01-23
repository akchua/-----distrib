package com.chua.distributions.database.prototype;

import com.chua.distributions.beans.PurchaseReportQueryBean;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
public interface PurchaseOrderPrototype extends Prototype<PurchaseOrder, Long> {

	ObjectList<PurchaseOrder> findByPurchaseReportQueryWithPaging(int pageNumber, int resultsPerPage, PurchaseReportQueryBean purchaseReportQuery);
}
