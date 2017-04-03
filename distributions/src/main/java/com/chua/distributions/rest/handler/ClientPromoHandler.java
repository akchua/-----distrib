package com.chua.distributions.rest.handler;

import com.chua.distributions.beans.ClientPromoFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientPromo;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	29 Mar 2017
 */
public interface ClientPromoHandler {

	ClientPromo getClientPromo(Long clientPromoId);
	
	ObjectList<ClientPromo> getClientPromoObjectList(Integer pageNumber, Long clientId);
	
	ResultBean createClientPromo(ClientPromoFormBean clientPromoForm);
	
	ResultBean updateClientPromo(ClientPromoFormBean clientPromoForm);
	
	ResultBean removeClientPromo(Long clientPromoId);
}
