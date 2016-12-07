package com.chua.distributions.database.service;

import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.database.prototype.PurchaseOrderPrototype;
import com.chua.distributions.enums.Area;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
public interface PurchaseOrderService extends Service<PurchaseOrder, Long>, PurchaseOrderPrototype {

	ObjectList<PurchaseOrder> findAllWithPaging(int pageNumber, int resultsPerPage, Long companyId, Area area);
}
