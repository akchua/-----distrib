package com.chua.distributions.database.prototype;

import java.util.List;

import com.chua.distributions.beans.ClientRankQueryBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.database.entity.ClientOrder;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
public interface ClientOrderPrototype extends Prototype<ClientOrder, Long> {

	List<ClientOrder> findAllBySalesReportQuery(SalesReportQueryBean salesReportQuery);
	
	List<ClientOrder> findAllByClientRankQuery(ClientRankQueryBean clientRankQuery);
}
