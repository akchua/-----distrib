package com.chua.distributions.rest.handler.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.NotAuthorizedException;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.annotations.CheckAuthority;
import com.chua.distributions.beans.ClientRankQueryBean;
import com.chua.distributions.beans.ClientStatisticsBean;
import com.chua.distributions.beans.PartialClientOrderBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.beans.UserBean;
import com.chua.distributions.constants.BusinessConstants;
import com.chua.distributions.constants.FileConstants;
import com.chua.distributions.constants.SystemConstants;
import com.chua.distributions.database.entity.ClientCompanyPrice;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.database.entity.Company;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.service.ClientCompanyPriceService;
import com.chua.distributions.database.service.ClientOrderItemService;
import com.chua.distributions.database.service.ClientOrderService;
import com.chua.distributions.database.service.CompanyService;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.ClientRankType;
import com.chua.distributions.enums.ClientSalesReportType;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.UserType;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientOrderHandler;
import com.chua.distributions.rest.handler.SalesReportHandler;
import com.chua.distributions.rest.handler.UserHandler;
import com.chua.distributions.utility.DateUtil;
import com.chua.distributions.utility.EmailUtil;
import com.chua.distributions.utility.Html;
import com.chua.distributions.utility.SimplePdfWriter;
import com.chua.distributions.utility.format.CurrencyFormatter;
import com.chua.distributions.utility.format.DateFormatter;
import com.chua.distributions.utility.template.ClientRankTemplate;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Dec 12, 2016
 */
@Transactional
@Component
public class ClientOrderHandlerImpl implements ClientOrderHandler {

	@Autowired
	private ClientOrderService clientOrderService;

	@Autowired
	private ClientOrderItemService clientOrderItemService;

	@Autowired
	private UserService userService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ClientCompanyPriceService clientCompanyPriceService;

	@Autowired
	private SalesReportHandler salesReportHandler;

	@Autowired
	private UserHandler userHandler;

	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private SystemConstants systemConstants;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Autowired
	private BusinessConstants businessConstants;

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ClientOrder getClientOrder(Long clientOrderId) {
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
		final UserBean currentUser = UserContextHolder.getUser();

		if (currentUser.getUserType().equals(UserType.CLIENT)
				&& !currentUser.getId().equals(clientOrder.getCreator().getId())) {
			throw new NotAuthorizedException("User is not authenticated.");
		} else {
			refreshClientOrder(clientOrderId);
			return clientOrder;
		}
	}

	@Override
	public PartialClientOrderBean getPartialClientOrder(Long clientOrderId) {
		final PartialClientOrderBean partialClientOrder;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
		final UserBean currentUser = UserContextHolder.getUser();

		if (clientOrder != null) {
			if (currentUser.getUserType().equals(UserType.CLIENT)
					&& !currentUser.getId().equals(clientOrder.getClient().getId())) {
				throw new NotAuthorizedException("User is not authenticated.");
			} else {
				refreshClientOrder(clientOrderId);
				partialClientOrder = new PartialClientOrderBean(clientOrder);
			}
		} else {
			partialClientOrder = null;
		}

		return partialClientOrder;
	}

	@Override
	@CheckAuthority(minimumAuthority = 4)
	public ClientOrder getTransferInstance(Long sourceId) {
		final ClientOrder sourceOrder = clientOrderService.find(sourceId);
		ClientOrder transferInstance = null;

		if (!sourceOrder.getNetTotal().equals(0.0f)) {
			final List<ClientOrder> toFollowOrders = clientOrderService
					.findAllToFollowByClient(sourceOrder.getClient().getId());

			for (ClientOrder clientOrder : toFollowOrders) {
				if (clientOrder.getNetTotal().equals(0.0f) && !clientOrder.getId().equals(sourceId)) {
					transferInstance = clientOrder;
					break;
				}
			}

			if (transferInstance == null) {
				final ClientOrder newClientOrder = new ClientOrder();

				newClientOrder.setCreator(UserContextHolder.getUser().getUserEntity());
				newClientOrder.setClient(sourceOrder.getClient());
				newClientOrder.setCompany(sourceOrder.getCompany());
				newClientOrder.setGrossTotal(0.0f);
				newClientOrder.setDiscountTotal(0.0f);
				newClientOrder.setStatus(Status.TO_FOLLOW);
				newClientOrder.setWarehouse(null);
				newClientOrder.setAdditionalDiscount(sourceOrder.getAdditionalDiscount());
				newClientOrder.setLessVat(sourceOrder.getLessVat());

				newClientOrder.setRequestedOn(sourceOrder.getRequestedOn());
				newClientOrder.setDeliveredOn(DateUtil.getDefaultDate());
				newClientOrder.setPaidOn(DateUtil.getDefaultDate());

				clientOrderService.insert(newClientOrder);
				transferInstance = newClientOrder;
			}
		} else {
			transferInstance = sourceOrder;
		}

		return transferInstance;
	}

	@Override
	public ObjectList<PartialClientOrderBean> getPartialClientOrderObjectList(Integer pageNumber, Boolean showPaid) {
		final ObjectList<PartialClientOrderBean> objPartialClientOrders = new ObjectList<PartialClientOrderBean>();
		final ObjectList<ClientOrder> objClientOrders = clientOrderService.findByClientWithPaging(pageNumber,
				UserContextHolder.getItemsPerPage(), UserContextHolder.getUser().getId(), showPaid);
		if (objClientOrders != null) {
			objPartialClientOrders.setTotal(objClientOrders.getTotal());
			final List<PartialClientOrderBean> partialClientOrders = new ArrayList<PartialClientOrderBean>();
			for (ClientOrder clientOrder : objClientOrders.getList()) {
				partialClientOrders.add(new PartialClientOrderBean(clientOrder));
			}
			objPartialClientOrders.setList(partialClientOrders);
		}
		return objPartialClientOrders;
	}

	@Override
	@CheckAuthority(minimumAuthority = 3)
	public ObjectList<ClientOrder> getClientOrderRequestObjectListCreatedByCurrentUser(Integer pageNumber) {
		return clientOrderService.findAllRequestByCreatorWithPagingOrderByRequestedOn(pageNumber,
				UserContextHolder.getItemsPerPage(), UserContextHolder.getUser().getId());
	}

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<ClientOrder> getClientOrderRequestObjectList(Integer pageNumber, Boolean showAccepted) {
		return clientOrderService.findAllRequestWithPagingOrderByRequestedOn(pageNumber,
				UserContextHolder.getItemsPerPage(), showAccepted);
	}

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<ClientOrder> getAcceptedClientOrderObjectList(Integer pageNumber, Long warehouseId) {
		return clientOrderService.findAllAcceptedWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), warehouseId);
	}

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<ClientOrder> getReceivedClientOrderObjectList(Integer pageNumber, Long warehouseId,
			Long clientId) {
		return clientOrderService.findAllReceivedWithPagingOrderByDeliveredOn(pageNumber,
				UserContextHolder.getItemsPerPage(), warehouseId, clientId);
	}

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<ClientOrder> getPaidClientOrderObjectList(Integer pageNumber, Long warehouseId) {
		return clientOrderService.findAllPaidWithPagingOrderByPaidOn(pageNumber, UserContextHolder.getItemsPerPage(),
				warehouseId);
	}

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<ClientOrder> getClientOrderObjectListBySalesReportQuery(Integer pageNumber,
			SalesReportQueryBean salesReportQuery) {
		return clientOrderService.findBySalesReportQueryWithPaging(pageNumber, UserContextHolder.getItemsPerPage(),
				salesReportQuery);
	}

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public String getFormattedTotalPayable() {
		List<ClientOrder> clientOrders = clientOrderService.findAllReceived();

		float total = 0.0f;
		for (ClientOrder clientOrder : clientOrders) {
			total += clientOrder.getNetTotal();
		}

		return CurrencyFormatter.pesoFormat(total);
	}

	@Override
	@CheckAuthority(authority = "10")
	public ResultBean addClientOrder(Long companyId) {
		final ResultBean result;
		
		if(companyId != null) {
			final Company company = companyService.find(companyId);

			if (company != null) {
				result = addClientOrder(company, UserContextHolder.getUser().getUserEntity());
			} else {
				result = new ResultBean(Boolean.FALSE,
						Html.line(Html.text(Color.RED, "Failed") + " to load company. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Please select a company."));
		}

		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 3)
	public ResultBean addClientOrder(Long companyId, Long clientId) {
		final ResultBean result;
		
		if(companyId != null) {
			final Company company = companyService.find(companyId);

			if (company != null) {
				final User client = userService.find(clientId);
				if (client != null) {
					result = addClientOrder(company, client);
					if (result.getSuccess()) {

						emailUtil.send(client.getEmailAddress(), 
								null,
								userHandler.getEmailOfAllAdminAndManagers(),
								"Order Created",
								UserContextHolder.getUser().getFullName() + " of " + businessConstants.getBusinessName()
										+ " has just created an order on your behalf." + "\n\n"
										+ "The ID of the created order is " + result.getExtras().get("clientOrderId")
										+ ". You can verify the contents of the order by logging in at "
										+ systemConstants.getServerDomain() + ". "
										+ "If you did not request this order, please inform us as soon as possible via replying to this email.",
								null);
					}
				} else {
					result = new ResultBean(Boolean.FALSE,
							Html.line(Html.text(Color.RED, "Failed") + " to load client. Please refresh the page."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE,
						Html.line(Html.text(Color.RED, "Failed") + " to load company. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Please select a company."));
		}

		return result;
	}

	private ResultBean addClientOrder(Company company, User client) {
		final ResultBean result;

		final List<ClientOrder> clientOrders = clientOrderService.findAllCreatingOrSubmittedByClient(client.getId());

		if (clientOrders != null && clientOrders.size() < 5) {
			final ClientOrder clientOrder = generateNewClientOrder(company, client);

			result = new ResultBean();
			result.setSuccess(clientOrderService.insert(clientOrder) != null);
			if (result.getSuccess()) {
				Map<String, Object> extras = new HashMap<String, Object>();
				extras.put("clientOrderId", clientOrder.getId());
				result.setExtras(extras);

				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created Order for "
						+ Html.text(Color.BLUE, client.getBusinessName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("You are " + Html.text(Color.RED, "NOT ALLOWED")
					+ " to create more than 5 simultaneous order requests."));
		}

		return result;
	}

	@Override
	public ResultBean submitClientOrder(Long clientOrderId) {
		final ResultBean result;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);

		if (clientOrder != null) {
			if (clientOrder.getClient().getId().equals(UserContextHolder.getUser().getId())
					|| clientOrder.getCreator().getId().equals(UserContextHolder.getUser().getId())) {
				if (clientOrder.getStatus().equals(Status.CREATING)) {
					if (!clientOrder.getNetTotal().equals(Float.valueOf(0.0f))) {
						clientOrder.setStatus(Status.SUBMITTED);
						clientOrder.setRequestedOn(new Date());

						result = new ResultBean();
						result.setSuccess(clientOrderService.update(clientOrder));
						if (result.getSuccess()) {
							result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " submitted Order of "
									+ Html.text(Color.BLUE, "ID #" + clientOrder.getId()) + "."));
						} else {
							result.setMessage(
									Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
						}
					} else {
						result = new ResultBean(Boolean.FALSE,
								Html.line(Html.text(Color.RED, "Submit Failed.") + " Please submit a non-empty form."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE,
							Html.line(Color.RED, "Request Denied!")
									+ Html.line(" You are not authorized to submit order with status "
											+ Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
				}
			} else {
				throw new NotAuthorizedException("User is not authenticated.");
			}
		} else {
			result = new ResultBean(Boolean.FALSE,
					Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
		}

		return result;
	}

	/*
	 * @Override public ResultBean testAcceptClientOrder(Long clientOrderId,
	 * Warehouse warehouse) { final ResultBean result; final ClientOrder
	 * clientOrder = clientOrderService.find(clientOrderId);
	 * 
	 * if(clientOrder != null && warehouse != null &&
	 * (clientOrder.getWarehouse() == null ||
	 * !clientOrder.getWarehouse().equals(warehouse))) { List<ClientOrderItem>
	 * clientOrderItems =
	 * clientOrderItemService.findAllByClientOrder(clientOrderId); int itemCount
	 * = clientOrderItems.size();
	 * 
	 * result = new ResultBean(); result.setSuccess(Boolean.TRUE);
	 * result.setMessage(Html.line(Color.RED, "Missing Items: "));
	 * for(ClientOrderItem orderItem : clientOrderItems) { WarehouseItem
	 * warehouseItem =
	 * warehouseItemService.findByProductAndWarehouse(orderItem.getProductId(),
	 * warehouse); if(warehouseItem == null || warehouseItem.getStockCount() <
	 * orderItem.getQuantity()) { result.setSuccess(Boolean.FALSE);
	 * if(warehouseItem == null) result.setMessage(result.getMessage() +
	 * Html.line(Color.BLUE, QuantityFormatter.format((orderItem.getQuantity()),
	 * orderItem.getPackaging()) + " " + orderItem.getDisplayName())); else
	 * result.setMessage(result.getMessage() + Html.line(Color.BLUE,
	 * QuantityFormatter.format((orderItem.getQuantity() -
	 * warehouseItem.getStockCount()), orderItem.getPackaging()) + " " +
	 * orderItem.getDisplayName())); itemCount--; } }
	 * 
	 * if(itemCount == 0) { result.setMessage(Html.line(Color.RED,
	 * "All items are missing. Adjusting will result to the cancellation of the order."
	 * )); } } else { result = new ResultBean(Boolean.FALSE, ""); }
	 * 
	 * return result; }
	 */

	@Override
	@CheckAuthority(minimumAuthority = 4)
	public ResultBean acceptClientOrder(Long clientOrderId) {
		final ResultBean result;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);

		if (clientOrder != null) {
			if (clientOrder.getStatus().equals(Status.SUBMITTED)) {
				if (clientOrder.getNetTotal() != 0.0f) {
					result = acceptClientOrder(clientOrder);
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Unable to accept empty order."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE,
						Html.line(Color.RED, "Request Denied!")
								+ Html.line(" You are not authorized to accept order with status "
										+ Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE,
					Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
		}

		return result;
	}

	/*
	 * @Override public ResultBean adjustAndAcceptClientOrder(Long
	 * clientOrderId, Warehouse warehouse) { final ResultBean result; final
	 * ClientOrder clientOrder = clientOrderService.find(clientOrderId);
	 * 
	 * if(clientOrder != null) {
	 * if(clientOrder.getStatus().equals(Status.SUBMITTED) ||
	 * clientOrder.getStatus().equals(Status.ACCEPTED)) { boolean flag = true;
	 * if(clientOrder.getWarehouse() != null) { flag =
	 * addToWarehouse(clientOrder, clientOrder.getWarehouse()); } if(flag) {
	 * adjustClientOrder(clientOrder, warehouse); if(clientOrder.getNetTotal()
	 * != 0.0f) { result = acceptClientOrder(clientOrder, warehouse); } else {
	 * result = removeClientOrder(clientOrder.getId()); } } else { result = new
	 * ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") +
	 * " to load order. Please refresh the page.")); } } else { result = new
	 * ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
	 * Html.line(" You are not authorized to accept order with status " +
	 * Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
	 * } } else { result = new ResultBean(Boolean.FALSE,
	 * Html.line(Html.text(Color.RED, "Failed") +
	 * " to load order. Please refresh the page.")); }
	 * 
	 * return result; }
	 */

	private ResultBean acceptClientOrder(ClientOrder clientOrder) {
		final ResultBean result;

		clientOrder.setStatus(Status.ACCEPTED);

		result = new ResultBean();
		result.setSuccess(clientOrderService.update(clientOrder) && 
				emailUtil.send(clientOrder.getClient().getEmailAddress(), 
						null,
						userHandler.getEmailOfAllAdminAndManagers(), "Order Accepted",
						"Thank you " + clientOrder.getClient().getFormattedName() + "("
								+ clientOrder.getClient().getBusinessName() + ") for ordering at Prime Pad."
								+ "This email is to inform you that your order has just been accepted and will be delivered to you as soon as possible.",
						null));
		if (result.getSuccess()) {
			result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " accepted Order of "
					+ Html.text(Color.BLUE, "ID #" + clientOrder.getId())
					+ ". An email notification has been sent to the client."));
		} else {
			result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
		}

		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean payClientOrder(Long clientOrderId) {
		final ResultBean result;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);

		if (clientOrder != null) {
			if (clientOrder.getStatus().equals(Status.RECEIVED)) {
				clientOrder.setStatus(Status.PAID);
				clientOrder.setPaidOn(new Date());

				result = new ResultBean();
				result.setSuccess(clientOrderService.update(clientOrder));
				if (result.getSuccess()) {

					result.setMessage(Html
							.line(Html.text(Color.GREEN, "Successfully") + " finalized and received payment of Order "
									+ Html.text(Color.BLUE, "ID #" + clientOrder.getId()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE,
						Html.line(Color.RED, "Request Denied!")
								+ Html.line(" You are not authorized to accept payment of order with status "
										+ Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE,
					Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
		}

		return result;
	}

	@Override
	public ResultBean removeClientOrder(Long clientOrderId) {
		final ResultBean result;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);

		if (clientOrder != null) {
			if (clientOrder.getStatus().equals(Status.CREATING) || clientOrder.getStatus().equals(Status.SUBMITTED)
					|| (UserContextHolder.getUser().getUserType().getAuthority() <= Integer.valueOf(2))
							&& (clientOrder.getStatus().equals(Status.ACCEPTED)
									|| clientOrder.getStatus().equals(Status.TO_FOLLOW))) {
				result = new ResultBean();

				clientOrder.setStatus(Status.CANCELLED);

				result.setSuccess(clientOrderService.delete(clientOrder));
				if (result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Order of "
							+ Html.text(Color.BLUE, "ID #" + clientOrder.getId()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE,
						Html.line(Color.RED, "Request Denied!")
								+ Html.line(" You are not authorized to remove order with status "
										+ Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE,
					Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
		}

		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ResultBean generateReport(SalesReportQueryBean salesReportQuery) {
		final ResultBean result = salesReportHandler.generateReport(salesReportQuery);
		if (result.getSuccess() && salesReportQuery.getSendMail()) {
			emailUtil.send(UserContextHolder.getUser().getEmailAddress(), 
					"Sales Report",
					"Sales Report for " + salesReportQuery.getFrom() + " - " + salesReportQuery.getTo() + ".",
					new String[] { fileConstants.getSalesHome() + (String) result.getExtras().get("fileName") });
		}
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ResultBean generateClientRanking(ClientRankQueryBean clientRankQuery) {
		final ResultBean result;
		
		if(clientRankQuery.getMonthFrom() != null && clientRankQuery.getMonthTo() != null) {
			// converting monthTo to inclusive end month
			Calendar monthTo = Calendar.getInstance();
			monthTo.setTime(clientRankQuery.getMonthTo());
			monthTo.add(Calendar.MONTH, 1);
			monthTo.add(Calendar.SECOND, -1);
			clientRankQuery.setMonthTo(monthTo.getTime());
			
			final List<ClientOrder> clientOrders = clientOrderService.findAllByClientRankQuery(clientRankQuery);
			
			final Map<String, Float> clientPurchases = new HashMap<String, Float>();
			
			for(ClientOrder clientOrder : clientOrders) {
				String clientName = clientOrder.getClient().getBusinessName();
				Float clientPurchase = clientPurchases.get(clientName) == null ? 0.0f : clientPurchases.get(clientName);
				clientPurchase += clientOrder.getNetTotal();
				clientPurchases.put(clientName, clientPurchase);
			}
			
			List<ClientStatisticsBean> clientStats = new ArrayList<ClientStatisticsBean>();
			
			for(Map.Entry<String, Float> entry : clientPurchases.entrySet()) {
				final ClientStatisticsBean clientStat = new ClientStatisticsBean();
				clientStat.setClientName(entry.getKey());
				clientStat.setPurchaseAmount(entry.getValue());
				clientStats.add(clientStat);
			}
			
			clientStats.sort((cs1, cs2) -> Float.compare(cs2.getPurchaseAmount(), cs1.getPurchaseAmount()));
			
			String fileName = "";
			if(clientRankQuery.getClientRankType() != null) {
				fileName += clientRankQuery.getClientRankType().getName() + "_";
			} else {
				fileName += ClientRankType.TOP_DELIVERED.name() + "_";
			}
			fileName += DateFormatter.fileSafeMonthFormat(clientRankQuery.getMonthFrom()) + "_";
			fileName += DateFormatter.fileSafeMonthFormat(clientRankQuery.getMonthTo());
			fileName += ".pdf";
			
			String filePath = fileConstants.getClientRankingHome() + fileName;
			
			result = new ResultBean();
			
			result.setSuccess(
						SimplePdfWriter.write(
									new ClientRankTemplate(clientRankQuery, clientStats).merge(velocityEngine) , 
									businessConstants.getBusinessShortName(),
									filePath,
									false)
					);
			
			if(result.getSuccess()) {
				final Map<String, Object> extras = new HashMap<String, Object>();
				extras.put("fileName", fileName);
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created client rankings."));
				result.setExtras(extras);
				
				if(clientRankQuery.getSendMail()) {
					emailUtil.send(UserContextHolder.getUser().getEmailAddress(), 
							"Client Ranking",
							"Client Ranking for " + DateFormatter.monthFormat(clientRankQuery.getMonthFrom()) + " - " + DateFormatter.monthFormat(clientRankQuery.getMonthTo()) + ".",
							new String[] { fileConstants.getClientRankingHome() + fileName });
				}
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Month from and month to are required fields."));
		}
		
		return result;
	}

	@Override
	public List<ClientSalesReportType> getClientSalesReportTypes() {
		return Stream.of(ClientSalesReportType.values()).collect(Collectors.toList());
	}
	
	@Override
	public List<ClientRankType> getClientRankTypes() {
		return Stream.of(ClientRankType.values()).collect(Collectors.toList());
	}

	@Override
	public List<Area> getAreaList() {
		return Stream.of(Area.values()).collect(Collectors.toList());
	}

	/*
	 * private void adjustClientOrder(ClientOrder clientOrder, Warehouse
	 * warehouse) { List<ClientOrderItem> clientOrderItems =
	 * clientOrderItemService.findAllByClientOrder(clientOrder.getId());
	 * for(ClientOrderItem clientOrderItem : clientOrderItems) { final
	 * WarehouseItem warehouseItem =
	 * warehouseItemService.findByProductAndWarehouse(clientOrderItem.
	 * getProductId(), warehouse); if(warehouseItem == null ||
	 * warehouseItem.getStockCount() <= 0) {
	 * clientOrderItemService.delete(clientOrderItem); } else
	 * if(warehouseItem.getStockCount() < clientOrderItem.getQuantity()) {
	 * clientOrderItem.setQuantity(warehouseItem.getStockCount());
	 * clientOrderItemService.update(clientOrderItem); } }
	 * refreshClientOrder(clientOrder.getId()); }
	 */

	/*
	 * private boolean addToWarehouse(ClientOrder clientOrder, Warehouse
	 * warehouse) { List<ClientOrderItem> clientOrderItems =
	 * clientOrderItemService.findAllByClientOrder(clientOrder.getId());
	 * for(ClientOrderItem clientOrderItem : clientOrderItems) { final
	 * WarehouseItem warehouseItem =
	 * warehouseItemService.findByProductAndWarehouse(clientOrderItem.
	 * getProductId(), warehouse); if(warehouseItem == null) { final Product
	 * product = productService.find(clientOrderItem.getProductId()); final
	 * WarehouseItem warehouzeItem = new WarehouseItem();
	 * 
	 * warehouzeItem.setProduct(product); warehouzeItem.setWarehouse(warehouse);
	 * warehouzeItem.setStockCount(clientOrderItem.getQuantity());
	 * 
	 * if(warehouseItemService.insert(warehouzeItem) == null) return false; }
	 * else { warehouseItem.setStockCount(warehouseItem.getStockCount() +
	 * clientOrderItem.getQuantity());
	 * 
	 * if(!warehouseItemService.update(warehouseItem)) return false;; } }
	 * 
	 * return true; }
	 */

	private void refreshClientOrder(Long clientOrderId) {
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);

		if (clientOrder != null && !clientOrder.getStatus().equals(Status.PAID)) {
			Float grossTotal = 0.0f;
			Float discountTotal = 0.0f;
			List<ClientOrderItem> clientOrderItems = clientOrderItemService.findAllByClientOrder(clientOrderId);

			for (ClientOrderItem clientOrderItem : clientOrderItems) {
				grossTotal += clientOrderItem.getGrossPrice();
				discountTotal += clientOrderItem.getDiscountAmount();
			}

			clientOrder.setGrossTotal(grossTotal);
			clientOrder.setDiscountTotal(discountTotal);
			clientOrderService.update(clientOrder);
		}
	}

	private ClientOrder generateNewClientOrder(Company company, User client) {
		final ClientOrder clientOrder = new ClientOrder();
		final ClientCompanyPrice clientCompanyPrice = clientCompanyPriceService.findByClientAndCompany(client.getId(),
				company.getId());

		clientOrder.setCreator(UserContextHolder.getUser().getUserEntity());
		clientOrder.setClient(client);
		clientOrder.setCompany(company);
		clientOrder.setGrossTotal(0.0f);
		clientOrder.setDiscountTotal(0.0f);
		clientOrder.setStatus(Status.CREATING);
		clientOrder.setWarehouse(null);
		clientOrder.setAdditionalDiscount((clientCompanyPrice != null) ? clientCompanyPrice.getDiscount() : 0.0f);
		clientOrder.setLessVat(client.getVatType().getLessVat());

		clientOrder.setRequestedOn(DateUtil.getDefaultDate());
		clientOrder.setDeliveredOn(DateUtil.getDefaultDate());
		clientOrder.setPaidOn(DateUtil.getDefaultDate());

		return clientOrder;
	}
}
