package com.chua.distributions.rest.handler;

import com.chua.distributions.beans.ClientProductPriceFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientProductPrice;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 18, 2017
 */
public interface ClientProductPriceHandler {

	ClientProductPrice getClientProductPrice(Long clientProductPriceId);
	
	ObjectList<ClientProductPrice> getClientProductPriceObjectList(Integer pageNumber, Long clientId);
	
	ResultBean createClientProductPrice(ClientProductPriceFormBean clientProductPriceForm);
	
	ResultBean updateClientProductPrice(ClientProductPriceFormBean clientProductPriceForm);
	
	ResultBean removeClientProductPrice(Long clientProductPriceId);
}
