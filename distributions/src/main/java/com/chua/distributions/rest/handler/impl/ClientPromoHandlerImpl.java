package com.chua.distributions.rest.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.annotations.CheckAuthority;
import com.chua.distributions.beans.ClientPromoFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientPromo;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.service.ClientPromoService;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientPromoHandler;
import com.chua.distributions.utility.Html;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	29 Mar 2017
 */
@Component
@Transactional
public class ClientPromoHandlerImpl implements ClientPromoHandler {

	@Autowired
	private ClientPromoService clientPromoService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ClientPromo getClientPromo(Long clientPromoId) {
		return clientPromoService.find(clientPromoId);
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ObjectList<ClientPromo> getClientPromoObjectList(Integer pageNumber, Long clientId) {
		return clientPromoService.findAllWithPagingOrderByProductName(pageNumber, UserContextHolder.getItemsPerPage(), clientId);
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean createClientPromo(ClientPromoFormBean clientPromoForm) {
		final ResultBean result;
		final ResultBean validateForm = validateClientPromoForm(clientPromoForm);
		
		if(validateForm.getSuccess()) {
			if(clientPromoService.isExistsByClientAndProduct(clientPromoForm.getClientId(), clientPromoForm.getProductId())) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Client has an existing price promo for the given product.")));
			} else {
				final ClientPromo clientPromo = new ClientPromo();
				final User client = userService.find(clientPromoForm.getClientId());
				final Product product = productService.find(clientPromoForm.getProductId());
				
				if(client != null && product != null) {
					clientPromo.setClient(client);
					clientPromo.setProduct(product);
					setClientPromo(clientPromo, clientPromoForm);
					
					result = new ResultBean();
					result.setSuccess(clientPromoService.insert(clientPromo) != null);
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created price promo for " + Html.text(Color.BLUE, client.getFormattedName()) + " on product " + Html.text(Color.BLUE, product.getDisplayName()) + "."));
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
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean updateClientPromo(ClientPromoFormBean clientPromoForm) {
		final ResultBean result;
		final ClientPromo clientPromo = clientPromoService.find(clientPromoForm.getId());
		
		if(clientPromo != null) {
			final ResultBean validateForm = validateClientPromoForm(clientPromoForm);
			if(validateForm.getSuccess()) {
				setClientPromo(clientPromo, clientPromoForm);
				
				result = new ResultBean();
				result.setSuccess(clientPromoService.update(clientPromo));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " updated price promo for " + Html.text(Color.BLUE, clientPromo.getClient().getFormattedName()) + " on product " + Html.text(Color.BLUE, clientPromo.getProduct().getDisplayName()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = validateForm;
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load price promo. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean removeClientPromo(Long clientPromoId) {
		final ResultBean result;
		final ClientPromo clientPromo = clientPromoService.find(clientPromoId);
		
		if(clientPromo != null) {
			result = new ResultBean();
			
			result.setSuccess(clientPromoService.delete(clientPromo));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed price promo for " + Html.text(Color.BLUE, clientPromo.getClient().getFormattedName()) + " on product " + Html.text(Color.BLUE, clientPromo.getProduct().getDisplayName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));	
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load price promo. Please refresh the page."));
		}
		
		return result;
	}
	
	private void setClientPromo(ClientPromo clientPromo, ClientPromoFormBean clientPromoForm) {
		clientPromo.setDiscount((clientPromoForm.getDiscount() != null) ? clientPromoForm.getDiscount() : 0.0f);
	}
	
	private ResultBean validateClientPromoForm(ClientPromoFormBean clientPromoForm) {
		final ResultBean result;
		
		if(clientPromoForm.getClientId() == null) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Please select a client to create price promo for."));
		} else if(clientPromoForm.getProductId() == null) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Please select a product to set price promo for."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
