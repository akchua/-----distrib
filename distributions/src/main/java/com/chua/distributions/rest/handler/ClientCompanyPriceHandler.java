package com.chua.distributions.rest.handler;

import com.chua.distributions.beans.ClientCompanyPriceFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientCompanyPrice;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Mar 2017
 */
public interface ClientCompanyPriceHandler {

	ClientCompanyPrice getClientCompanyPrice(Long clientCompanyPriceId);
	
	ObjectList<ClientCompanyPrice> getClientCompanyPriceObjectList(Integer pageNumber, Long clientId);
	
	ResultBean createClientCompanyPrice(ClientCompanyPriceFormBean clientCompanyPriceForm);
	
	ResultBean updateClientCompanyPrice(ClientCompanyPriceFormBean clientCompanyPriceForm);
	
	ResultBean removeClientCompanyPrice(Long clientCompanyPriceId);
}
