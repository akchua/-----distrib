package com.chua.distributions.rest.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.beans.ClientProductPriceFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientProductPrice;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.service.ClientProductPriceService;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientProductPriceHandler;
import com.chua.distributions.utility.Html;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 18, 2017
 */
@Transactional
@Component
public class ClientProductPriceHandlerImpl implements ClientProductPriceHandler {

	@Autowired
	private ClientProductPriceService clientProductPriceService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;

	@Override
	public ClientProductPrice getClientProductPrice(Long clientProductPriceId) {
		return clientProductPriceService.find(clientProductPriceId);
	}

	@Override
	public ObjectList<ClientProductPrice> getClientProductPriceObjectList(Integer pageNumber, Long clientId) {
		return clientProductPriceService.findAllWithPagingOrderByProductName(pageNumber, UserContextHolder.getItemsPerPage(), clientId);
	}

	@Override
	public ResultBean createClientProductPrice(ClientProductPriceFormBean clientProductPriceForm) {
		final ResultBean result;
		final ResultBean validateForm = validateClientProductPriceForm(clientProductPriceForm);
		
		if(validateForm.getSuccess()) {
			if(clientProductPriceService.isExistsByClientAndProduct(clientProductPriceForm.getClientId(), clientProductPriceForm.getProductId())) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Client has an existing price settings for the given product.")));
			} else {
				final ClientProductPrice clientProductPrice = new ClientProductPrice();
				final User client = userService.find(clientProductPriceForm.getClientId());
				final Product product = productService.find(clientProductPriceForm.getProductId());
				
				if(client != null && product != null) {
					clientProductPrice.setClient(client);
					clientProductPrice.setProduct(product);
					setClientProductPrice(clientProductPrice, clientProductPriceForm);
					
					result = new ResultBean();
					result.setSuccess(clientProductPriceService.insert(clientProductPrice) != null);
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created price setting for " + Html.text(Color.BLUE, client.getFormattedName()) + " on product " + Html.text(Color.BLUE, product.getDisplayName()) + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load client or product. Please refresh the page."));
				}
			}
		} else {
			result = validateForm;
		}
		
		return result;
	}

	@Override
	public ResultBean updateClientProductPrice(ClientProductPriceFormBean clientProductPriceForm) {
		final ResultBean result;
		final ClientProductPrice clientProductPrice = clientProductPriceService.find(clientProductPriceForm.getId());
		
		if(clientProductPrice != null) {
			final ResultBean validateForm = validateClientProductPriceForm(clientProductPriceForm);
			if(validateForm.getSuccess()) {
				setClientProductPrice(clientProductPrice, clientProductPriceForm);
				
				result = new ResultBean();
				result.setSuccess(clientProductPriceService.update(clientProductPrice));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " updated price setting for " + Html.text(Color.BLUE, clientProductPrice.getClient().getFormattedName()) + " on product " + Html.text(Color.BLUE, clientProductPrice.getProduct().getDisplayName()) + "."));
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
	public ResultBean removeClientProductPrice(Long clientProductPriceId) {
		final ResultBean result;
		final ClientProductPrice clientProductPrice = clientProductPriceService.find(clientProductPriceId);
		
		if(clientProductPrice != null) {
			result = new ResultBean();
			
			result.setSuccess(clientProductPriceService.delete(clientProductPrice));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed price setting for " + Html.text(Color.BLUE, clientProductPrice.getClient().getFormattedName()) + " on product " + Html.text(Color.BLUE, clientProductPrice.getProduct().getDisplayName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));	
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load price settings. Please refresh the page."));
		}
		
		return result;
	}
	
	private void setClientProductPrice(ClientProductPrice clientProductPrice, ClientProductPriceFormBean clientProductPriceForm) {
		clientProductPrice.setPackageSellingPrice((clientProductPriceForm.getPackageSellingPrice() != null) ? clientProductPriceForm.getPackageSellingPrice() : 0.0f);
	}
	
	private ResultBean validateClientProductPriceForm(ClientProductPriceFormBean clientProductPriceForm) {
		final ResultBean result;
		
		if(clientProductPriceForm.getClientId() == null) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Please select a client to create price settings for."));
		} else if(clientProductPriceForm.getProductId() == null) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Please select a product to set price settings for."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
