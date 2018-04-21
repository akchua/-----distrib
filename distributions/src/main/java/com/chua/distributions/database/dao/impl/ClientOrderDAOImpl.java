package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

import com.chua.distributions.beans.ClientRankQueryBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.database.dao.ClientOrderDAO;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.enums.ClientSalesReportType;
import com.chua.distributions.enums.Status;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
@Repository
public class ClientOrderDAOImpl
		extends AbstractDAO<ClientOrder, Long>
		implements ClientOrderDAO {

	@Override
	public ObjectList<ClientOrder> findByClientWithPaging(int pageNumber, int resultsPerPage, Long clientId, boolean showPaid) {
		return findByClientWithPagingAndOrder(pageNumber, resultsPerPage, clientId, showPaid, null);
	}

	@Override
	public ObjectList<ClientOrder> findByClientWithPagingAndOrder(int pageNumber, int resultsPerPage, Long clientId, boolean showPaid,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("client.id", clientId));
		
		if(!showPaid) {
			conjunction.add(Restrictions.ne("status", Status.PAID));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
	
	@Override
	public ObjectList<ClientOrder> findAllWithPagingAndStatus(int pageNumber, int resultsPerPage, Status[] status) {
		return findAllWithPagingStatusAndOrder(pageNumber, resultsPerPage, status, null);
	}
	
	@Override
	public ObjectList<ClientOrder> findAllWithPagingStatusAndOrder(int pageNumber, int resultsPerPage, Status[] status,
			Order[] orders) {
		return findByWarehouseWithPagingStatusAndOrder(pageNumber, resultsPerPage, null, status, orders);
	}
	
	@Override
	public ObjectList<ClientOrder> findByCreatorWithPagingStatusAndOrder(int pageNumber, int resultsPerPage,
			Status[] status, Long creatorId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(creatorId != null) {
			conjunction.add(Restrictions.eq("creator.id", creatorId));
		}
		
		if(status != null && status.length > 0) {
			final Junction disjunction = Restrictions.disjunction();
			for(Status stat : status) {
				disjunction.add(Restrictions.eq("status", stat));
			}
			conjunction.add(disjunction);
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
	
	@Override
	public ObjectList<ClientOrder> findByWarehouseWithPagingStatusAndOrder(int pageNumber, int resultsPerPage,
			Long warehouseId, Status[] status, Order[] orders) {
		return findByWarehouseAndClientWithPagingStatusAndOrder(pageNumber, resultsPerPage, warehouseId, null, status, orders);
	}
	
	@Override
	public ObjectList<ClientOrder> findByWarehouseAndClientWithPagingStatusAndOrder(int pageNumber, int resultsPerPage,
			Long warehouseId, Long clientId, Status[] status, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(warehouseId != null) {
			conjunction.add(Restrictions.eq("warehouse.id", warehouseId));
		}
		
		if(clientId != null) {
			conjunction.add(Restrictions.eq("client.id", clientId));
		}
		
		if(status != null && status.length > 0) {
			final Junction disjunction = Restrictions.disjunction();
			for(Status stat : status) {
				disjunction.add(Restrictions.eq("status", stat));
			}
			conjunction.add(disjunction);
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}

	@Override
	public List<ClientOrder> findAllByClientAndStatus(Long clientId, Status[] status) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("client.id", clientId));
		
		if(status != null && status.length > 0) {
			final Junction disjunction = Restrictions.disjunction();
			for(Status stat : status) {
				disjunction.add(Restrictions.eq("status", stat));
			}
			conjunction.add(disjunction);
		}
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}

	@Override
	public List<ClientOrder> findAllByStatus(Status[] status) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(status != null && status.length > 0) {
			final Junction disjunction = Restrictions.disjunction();
			for(Status stat : status) {
				disjunction.add(Restrictions.eq("status", stat));
			}
			conjunction.add(disjunction);
		}
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}

	@Override
	public ObjectList<ClientOrder> findBySalesReportQueryWithPagingAndOrder(int pageNumber, int resultsPerPage,
			SalesReportQueryBean salesReportQuery, Order[] orders) {
		String[] associatedPaths = { "client" };
		String[] aliasNames = { "cli" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN };
		
		return findAllByCriterion(pageNumber, resultsPerPage, associatedPaths, aliasNames, joinTypes, orders, generateConjunction(salesReportQuery));
	}
	
	@Override
	public List<ClientOrder> findAllBySalesReportQuery(SalesReportQueryBean salesReportQuery) {
		String[] associatedPaths = { "client" };
		String[] aliasNames = { "cli" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN };
		
		return findAllByCriterionList(associatedPaths, aliasNames, joinTypes, new Order[] { Order.asc("status"), Order.desc("updatedOn") }, generateConjunction(salesReportQuery));
	}
	
	@Override
	public List<ClientOrder> findAllByClientRankQuery(ClientRankQueryBean clientRankQuery) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(clientRankQuery.getMonthFrom() != null && clientRankQuery.getMonthTo() != null) {
			String dateBasis = "";
			if(clientRankQuery.getClientRankType() != null) {
				switch(clientRankQuery.getClientRankType()) {
				case TOP_DELIVERED:
					dateBasis = "deliveredOn";
					break;
				}
			} else {
				dateBasis = "deliveredOn";
			}
			
			conjunction.add(Restrictions.between(dateBasis, clientRankQuery.getMonthFrom(), clientRankQuery.getMonthTo()));
		}
		
		if(clientRankQuery.getArea() != null) {
			conjunction.add(Restrictions.eq("cli.businessArea", clientRankQuery.getArea()));
		}
		
		String[] associatedPaths = { "client" };
		String[] aliasNames = { "cli" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN };
		
		return findAllByCriterionList(associatedPaths, aliasNames, joinTypes, new Order[] { Order.asc("status"), Order.desc("updatedOn") }, conjunction);
	}
	
	private Junction generateConjunction(SalesReportQueryBean salesReportQuery) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(salesReportQuery.getFrom() != null && salesReportQuery.getTo() != null) {
			String dateBasis = "";
			switch(salesReportQuery.getClientSalesReportType()) {
			case STATUS_BASED:
				dateBasis = "updatedOn";
				break;
			case DELIVERY:
				dateBasis = "deliveredOn";
				break;
			case PAYMENTS:
				dateBasis = "paidOn";
				break;
			}
			conjunction.add(Restrictions.between(dateBasis, salesReportQuery.getFrom(), salesReportQuery.getTo()));
		}
		
		if(salesReportQuery.getWarehouseId() != null) {
			conjunction.add(Restrictions.eq("warehouse.id", salesReportQuery.getWarehouseId()));
		}
		
		if(salesReportQuery.getClientId() != null) {
			conjunction.add(Restrictions.eq("cli.id", salesReportQuery.getClientId()));
		}
		
		if(salesReportQuery.getCompanyId() != null) {
			conjunction.add(Restrictions.eq("company.id", salesReportQuery.getCompanyId()));
		}
		
		if(salesReportQuery.getArea() != null) {
			conjunction.add(Restrictions.eq("cli.businessArea", salesReportQuery.getArea()));
		}
		
		if(salesReportQuery.getClientSalesReportType().equals(ClientSalesReportType.STATUS_BASED)) {
			final Junction disjunction = Restrictions.disjunction();
			if(salesReportQuery.getIncludePaid()) disjunction.add(Restrictions.eq("status", Status.PAID));
			if(salesReportQuery.getIncludeDelivered()) disjunction.add(Restrictions.eq("status", Status.RECEIVED));
			if(salesReportQuery.getIncludeDispatched()) disjunction.add(Restrictions.eq("status", Status.DISPATCHED));
			if(salesReportQuery.getIncludeToFollow()) disjunction.add(Restrictions.eq("status", Status.TO_FOLLOW));
			if(salesReportQuery.getIncludeAccepted()) disjunction.add(Restrictions.eq("status", Status.ACCEPTED));
			if(salesReportQuery.getIncludeSubmitted()) disjunction.add(Restrictions.eq("status", Status.SUBMITTED));
			if(salesReportQuery.getIncludeCreating()) disjunction.add(Restrictions.eq("status", Status.CREATING));
			conjunction.add(disjunction);
		}
		
		return conjunction;
	}
}
