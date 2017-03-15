package com.chua.distributions.rest.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.annotations.CheckAuthority;
import com.chua.distributions.beans.ClientCompanyPriceFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientCompanyPrice;
import com.chua.distributions.database.entity.Company;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.service.ClientCompanyPriceService;
import com.chua.distributions.database.service.CompanyService;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientCompanyPriceHandler;
import com.chua.distributions.utility.Html;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Mar 2017
 */
@Transactional
@Component
public class ClientCompanyPriceHandlerImpl implements ClientCompanyPriceHandler {

	@Autowired
	private ClientCompanyPriceService clientCompanyPriceService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyService companyService;
	
	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ClientCompanyPrice getClientCompanyPrice(Long clientCompanyPriceId) {
		return clientCompanyPriceService.find(clientCompanyPriceId);
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ObjectList<ClientCompanyPrice> getClientCompanyPriceObjectList(Integer pageNumber, Long clientId) {
		return clientCompanyPriceService.findAllWithPagingOrderByCompanyName(pageNumber, UserContextHolder.getItemsPerPage(), clientId);
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean createClientCompanyPrice(ClientCompanyPriceFormBean clientCompanyPriceForm) {
		final ResultBean result;
		final ResultBean validateForm = validateClientCompanyPriceForm(clientCompanyPriceForm);
		
		if(validateForm.getSuccess()) {
			if(clientCompanyPriceService.isExistsByClientAndCompany(clientCompanyPriceForm.getClientId(), clientCompanyPriceForm.getCompanyId())) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Client has an existing price settings for the given company.")));
			} else {
				final ClientCompanyPrice clientCompanyPrice = new ClientCompanyPrice();
				final User client = userService.find(clientCompanyPriceForm.getClientId());
				final Company company = companyService.find(clientCompanyPriceForm.getCompanyId());
				
				clientCompanyPrice.setClient(client);
				clientCompanyPrice.setCompany(company);
				setClientCompanyPrice(clientCompanyPrice, clientCompanyPriceForm);
				
				result = new ResultBean();
				result.setSuccess(clientCompanyPriceService.insert(clientCompanyPrice) != null);
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created price setting for " + Html.text(Color.BLUE, client.getFormattedName()) + " on company " + Html.text(Color.BLUE, company.getName()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			}
		} else {
			result = validateForm;
		}
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean updateClientCompanyPrice(ClientCompanyPriceFormBean clientCompanyPriceForm) {
		final ResultBean result;
		final ClientCompanyPrice clientCompanyPrice = clientCompanyPriceService.find(clientCompanyPriceForm.getId());
		
		if(clientCompanyPrice != null) {
			final ResultBean validateForm = validateClientCompanyPriceForm(clientCompanyPriceForm);
			if(validateForm.getSuccess()) {
				setClientCompanyPrice(clientCompanyPrice, clientCompanyPriceForm);
				
				result = new ResultBean();
				result.setSuccess(clientCompanyPriceService.update(clientCompanyPrice));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " updated price setting for " + Html.text(Color.BLUE, clientCompanyPrice.getClient().getFormattedName()) + " on company " + Html.text(Color.BLUE, clientCompanyPrice.getCompany().getName()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = validateForm;
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load price settings. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean removeClientCompanyPrice(Long clientCompanyPriceId) {
		final ResultBean result;
		final ClientCompanyPrice clientCompanyPrice = clientCompanyPriceService.find(clientCompanyPriceId);
		
		if(clientCompanyPrice != null) {
			result = new ResultBean();
			
			result.setSuccess(clientCompanyPriceService.delete(clientCompanyPrice));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed price setting for " + Html.text(Color.BLUE, clientCompanyPrice.getClient().getFormattedName()) + " on company " + Html.text(Color.BLUE, clientCompanyPrice.getCompany().getName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));	
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load price settings. Please refresh the page."));
		}
		
		return result;
	}
	
	private void setClientCompanyPrice(ClientCompanyPrice clientCompanyPrice, ClientCompanyPriceFormBean clientCompanyPriceForm) {
		clientCompanyPrice.setDiscount((clientCompanyPriceForm.getDiscount() != null) ? clientCompanyPriceForm.getDiscount() : 0.0f);
		clientCompanyPrice.setMarkup((clientCompanyPriceForm.getMarkup() != null) ? clientCompanyPriceForm.getMarkup() : 0.0f);
	}
	
	private ResultBean validateClientCompanyPriceForm(ClientCompanyPriceFormBean clientCompanyPriceForm) {
		final ResultBean result;
		
		if(clientCompanyPriceForm.getClientId() == null) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Please select a client to create price settings for."));
		} else if(clientCompanyPriceForm.getCompanyId() == null) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Please select a company to set price settings for."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
